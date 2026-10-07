/*
 *     ___
 *     │   ╲                 _
 *     │    ╲ ___ _ __  __ _(_)_ _
 *     |     ╲ _ ╲ '  ╲╱ _` │ │ ' ╲
 *     |_____╱___╱_│_│_╲__,_│_│_||_|
 *     │ │  (_)╱ _│___ __ _  _ __│ |___ ___
 *     │ │__│ │  _╱ -_) _│ ││ ╱ _│ ╱ -_|_-<
 *     │____│_│_│ ╲___╲__│╲_, ╲__│_╲___╱__╱
 *                      |__╱
 *
 *  Copyright 2019-2024 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.domainlifecycles.jooq.connection;

import io.domainlifecycles.jooq.imp.JooqEntityIdentityProvider;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.jooq.persistence.BaseDLCTestPersistenceConfiguration;
import io.domainlifecycles.jooq.persistence.tests.simple.SimpleAggregateRootRepository;
import io.domainlifecycles.persistence.cache.AggregateCacheSupport;
import io.domainlifecycles.persistence.cache.ThreadBoundTransactionCacheProvider;
import io.domainlifecycles.persistence.spring.cache.SpringTransactionCacheProvider;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.UpdatableRecord;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.ThreadLocalTransactionProvider;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.TransactionDefinition;
import tests.shared.persistence.ConnectionTrackingDataSource;
import tests.shared.persistence.PersistenceEventTestHelper;
import tests.shared.persistence.domain.simple.TestRootSimple;
import tests.shared.persistence.domain.simple.TestRootSimpleId;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Verifies that the jOOQ integration hands back every connection it obtains - with the transaction cache, with and
 * without Spring, within and outside a transaction, committed and rolled back - so that a connection pool can never
 * run dry. Every connection comes from a {@link ConnectionTrackingDataSource} counting the ones not closed yet.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JooqConnectionRelease_ITest {

    private static final String TABLE = "TEST_ROOT_SIMPLE";

    private final BaseDLCTestPersistenceConfiguration testConfiguration = new BaseDLCTestPersistenceConfiguration();

    private final PersistenceEventTestHelper persistenceEventTestHelper = new PersistenceEventTestHelper();

    private final ConnectionTrackingDataSource dataSource = new ConnectionTrackingDataSource(testConfiguration.dataSource);

    private static DSLContext dslContext(Configuration configuration) {
        configuration.settings().setExecuteWithOptimisticLocking(true);
        configuration.set(SQLDialect.H2);
        return new DefaultDSLContext(configuration);
    }

    private SimpleAggregateRootRepository repository(DSLContext dslContext, JooqDomainPersistenceProvider provider) {
        persistenceEventTestHelper.resetEventsCaught();
        return new SimpleAggregateRootRepository(dslContext, persistenceEventTestHelper.testEventPublisher, provider);
    }

    private static TestRootSimple newRoot(DSLContext dslContext) {
        var id = (TestRootSimpleId) new JooqEntityIdentityProvider(dslContext).provideFor(TestRootSimple.class.getName());
        return TestRootSimple.builder().setId(id).setName("ConnectionRelease").build();
    }

    /**
     * Every operation of the repository, each one followed by the check that no connection is left open.
     */
    private void eachOperationReleasesItsConnection(DSLContext dslContext, JooqDomainPersistenceProvider provider) {
        var repository = repository(dslContext, provider);

        var root = newRoot(dslContext);
        assertThat(dataSource.openConnections()).as("after providing an id").isZero();
        repository.insert(root);
        assertThat(dataSource.openConnections()).as("after insert").isZero();
        var found = repository.findById(root.getId()).orElseThrow();
        assertThat(dataSource.openConnections()).as("after findById").isZero();
        found.setName("ConnectionReleaseUpdated");
        repository.update(found);
        assertThat(dataSource.openConnections()).as("after update").isZero();
        dslContext.fetch("SELECT * FROM TEST_DOMAIN.TEST_ROOT_SIMPLE WHERE NAME = ?", "ConnectionReleaseUpdated");
        assertThat(dataSource.openConnections()).as("after a custom query").isZero();
        assertThatThrownBy(() -> repository.insert(found)).isInstanceOf(RuntimeException.class);
        assertThat(dataSource.openConnections()).as("after a failing insert").isZero();
        repository.deleteById(root.getId());
        assertThat(dataSource.openConnections()).as("after deleteById").isZero();
        assertThat(repository.findById(root.getId())).isEmpty();
    }

    /**
     * Loads and updates a root in what the caller runs as one transaction: exactly the transaction's connection is
     * in use meanwhile, and the update takes the state loaded before from the transaction cache - the only SELECT
     * left is the one jOOQ's optimistic locking issues for the record it updates.
     */
    private void loadAndUpdateInOneTransaction(DSLContext dslContext, JooqDomainPersistenceProvider provider,
                                               TestRootSimpleId id) {
        var repository = repository(dslContext, provider);
        var found = repository.findById(id).orElseThrow();
        found.setName("UpdatedInTransaction");
        dataSource.clearPreparedStatements();
        repository.update(found);
        assertThat(dataSource.selectsFrom(TABLE) - lockingSelects())
            .as("SELECTs of the update besides the locking one").isZero();
        assertThat(dataSource.openConnections()).as("connections within the transaction").isEqualTo(1);
    }

    private long lockingSelects() {
        return dataSource.preparedStatements().stream()
            .filter(sql -> sql.toUpperCase().contains("FOR UPDATE"))
            .count();
    }

    private TestRootSimpleId insertedRoot(DSLContext dslContext, JooqDomainPersistenceProvider provider) {
        var root = newRoot(dslContext);
        repository(dslContext, provider).insert(root);
        return root.getId();
    }

    /**
     * Several reads and writes of two aggregates in what the caller runs as one transaction: they all share one
     * transaction cache, and every update takes the state loaded before from it - also for an aggregate loaded again
     * after it was written. The only SELECTs left are the ones jOOQ's optimistic locking issues.
     */
    private void severalReadsAndWritesShareOneCache(DSLContext dslContext, JooqDomainPersistenceProvider provider,
                                                    TestRootSimpleId first, TestRootSimpleId second) {
        var repository = repository(dslContext, provider);
        var a = repository.findById(first).orElseThrow();
        var cache = provider.transactionCacheProvider.currentTransactionCache().orElseThrow();
        var b = repository.findById(second).orElseThrow();

        a.setName("FirstWrite");
        dataSource.clearPreparedStatements();
        repository.update(a);
        assertThat(dataSource.selectsFrom(TABLE) - lockingSelects()).as("SELECTs of the first update").isZero();

        b.setName("SecondWrite");
        dataSource.clearPreparedStatements();
        repository.update(b);
        assertThat(dataSource.selectsFrom(TABLE) - lockingSelects()).as("SELECTs of the second update").isZero();

        var aAgain = repository.findById(first).orElseThrow();
        aAgain.setName("ThirdWrite");
        dataSource.clearPreparedStatements();
        repository.update(aAgain);
        assertThat(dataSource.selectsFrom(TABLE) - lockingSelects())
            .as("SELECTs of the update after loading again").isZero();

        assertThat(provider.transactionCacheProvider.currentTransactionCache()).containsSame(cache);
        assertThat(dataSource.openConnections()).as("connections within the transaction").isEqualTo(1);
    }

    /**
     * Outside a transaction, nothing is cached: an update reads the current state again.
     */
    private void nothingIsCachedOutsideATransaction(DSLContext dslContext, JooqDomainPersistenceProvider provider) {
        var id = insertedRoot(dslContext, provider);
        var repository = repository(dslContext, provider);
        var found = repository.findById(id).orElseThrow();
        assertThat(provider.transactionCacheProvider.currentTransactionCache()).isEmpty();
        found.setName("UpdatedWithoutTransaction");
        dataSource.clearPreparedStatements();
        repository.update(found);
        assertThat(dataSource.selectsFrom(TABLE) - lockingSelects()).as("SELECTs of the update").isEqualTo(1);
        assertThat(provider.transactionCacheProvider.currentTransactionCache()).isEmpty();
        repository.deleteById(id);
    }

    private String nameOf(DSLContext dslContext, JooqDomainPersistenceProvider provider, TestRootSimpleId id) {
        return repository(dslContext, provider).findById(id).map(TestRootSimple::getName).orElseThrow();
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class WithoutSpring {

        // a connection per operation, or the one of the transaction jOOQ runs on this thread
        private final DSLContext dslContext = dslContext(new DefaultConfiguration()
            .set(new ThreadLocalTransactionProvider(new DataSourceConnectionProvider(dataSource))));

        // registers the TransactionCacheJooqBinder on the DSLContext
        private final JooqDomainPersistenceProvider provider = new JooqDomainPersistenceProvider(
            testConfiguration.newDomainPersistenceConfiguration(null), dslContext);

        @Test
        void everyOperationOutsideATransactionReleasesItsConnection() {
            eachOperationReleasesItsConnection(dslContext, provider);
        }

        @Test
        void aCommittedTransactionLeavesNoConnectionOpen() {
            var id = insertedRoot(dslContext, provider);

            dslContext.transaction(() -> loadAndUpdateInOneTransaction(dslContext, provider, id));

            assertThat(dataSource.openConnections()).isZero();
            assertThat(provider.transactionCacheProvider.currentTransactionCache()).isEmpty();
            assertThat(nameOf(dslContext, provider, id)).isEqualTo("UpdatedInTransaction");
            repository(dslContext, provider).deleteById(id);
        }

        @Test
        void aRolledBackTransactionLeavesNoConnectionOpen() {
            var id = insertedRoot(dslContext, provider);

            assertThatThrownBy(() -> dslContext.transaction(() -> {
                loadAndUpdateInOneTransaction(dslContext, provider, id);
                throw new IllegalStateException("roll back");
            })).isInstanceOf(IllegalStateException.class);

            assertThat(dataSource.openConnections()).isZero();
            assertThat(provider.transactionCacheProvider.currentTransactionCache()).isEmpty();
            assertThat(nameOf(dslContext, provider, id)).isEqualTo("ConnectionRelease");
            repository(dslContext, provider).deleteById(id);
        }
        @Test
        void aTransactionKeepsOneCacheForAllItsReadsAndWrites() {
            var first = insertedRoot(dslContext, provider);
            var second = insertedRoot(dslContext, provider);

            dslContext.transaction(() -> severalReadsAndWritesShareOneCache(dslContext, provider, first, second));

            assertThat(dataSource.openConnections()).isZero();
            assertThat(provider.transactionCacheProvider.currentTransactionCache()).isEmpty();
            assertThat(nameOf(dslContext, provider, first)).isEqualTo("ThirdWrite");
            assertThat(nameOf(dslContext, provider, second)).isEqualTo("SecondWrite");
            repository(dslContext, provider).deleteById(first);
            repository(dslContext, provider).deleteById(second);
        }

        @Test
        void nothingIsCachedOutsideATransaction() {
            JooqConnectionRelease_ITest.this.nothingIsCachedOutsideATransaction(dslContext, provider);
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class WithSpring {

        private final SpringTransactionCacheProvider<UpdatableRecord<?>> cacheProvider =
            new SpringTransactionCacheProvider<>();

        // as DlcJooqPersistenceAutoConfiguration wires it
        private final DSLContext dslContext = dslContext(new DefaultConfiguration()
            .set(new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource))));

        private final JooqDomainPersistenceProvider provider = new JooqDomainPersistenceProvider(
            testConfiguration.newDomainPersistenceConfiguration(cacheProvider));

        private final DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);

        private final TransactionTemplate transaction = new TransactionTemplate(transactionManager);

        @Test
        void everyOperationOutsideATransactionReleasesItsConnection() {
            eachOperationReleasesItsConnection(dslContext, provider);
        }

        @Test
        void aCommittedTransactionLeavesNoConnectionOpen() {
            var id = insertedRoot(dslContext, provider);

            transaction.executeWithoutResult(status -> loadAndUpdateInOneTransaction(dslContext, provider, id));

            assertThat(dataSource.openConnections()).isZero();
            assertThat(cacheProvider.currentTransactionCache()).isEmpty();
            assertThat(nameOf(dslContext, provider, id)).isEqualTo("UpdatedInTransaction");
            repository(dslContext, provider).deleteById(id);
        }

        @Test
        void aRolledBackTransactionLeavesNoConnectionOpen() {
            var id = insertedRoot(dslContext, provider);

            assertThatThrownBy(() -> transaction.executeWithoutResult(status -> {
                loadAndUpdateInOneTransaction(dslContext, provider, id);
                throw new IllegalStateException("roll back");
            })).isInstanceOf(IllegalStateException.class);

            assertThat(dataSource.openConnections()).isZero();
            assertThat(cacheProvider.currentTransactionCache()).isEmpty();
            assertThat(nameOf(dslContext, provider, id)).isEqualTo("ConnectionRelease");
            repository(dslContext, provider).deleteById(id);
        }
        @Test
        void aTransactionKeepsOneCacheForAllItsReadsAndWrites() {
            var first = insertedRoot(dslContext, provider);
            var second = insertedRoot(dslContext, provider);

            transaction.executeWithoutResult(
                status -> severalReadsAndWritesShareOneCache(dslContext, provider, first, second));

            assertThat(dataSource.openConnections()).isZero();
            assertThat(cacheProvider.currentTransactionCache()).isEmpty();
            assertThat(nameOf(dslContext, provider, first)).isEqualTo("ThirdWrite");
            assertThat(nameOf(dslContext, provider, second)).isEqualTo("SecondWrite");
            repository(dslContext, provider).deleteById(first);
            repository(dslContext, provider).deleteById(second);
        }

        @Test
        void nothingIsCachedOutsideATransaction() {
            JooqConnectionRelease_ITest.this.nothingIsCachedOutsideATransaction(dslContext, provider);
        }
        private TransactionTemplate transaction(int propagation) {
            var template = new TransactionTemplate(transactionManager);
            template.setPropagationBehavior(propagation);
            return template;
        }

        private Optional<TestRootSimple> cached(TestRootSimpleId id) {
            return cacheProvider.currentTransactionCache().orElseThrow()
                .take(AggregateCacheSupport.keyFor(id))
                .flatMap(result -> result.resultValue().map(TestRootSimple.class::cast));
        }

        @Test
        void aRequiresNewTransactionGetsACacheOfItsOwn() {
            var id = insertedRoot(dslContext, provider);

            transaction.executeWithoutResult(outer -> {
                repository(dslContext, provider).findById(id).orElseThrow();
                var outerCache = cacheProvider.currentTransactionCache().orElseThrow();

                transaction(TransactionDefinition.PROPAGATION_REQUIRES_NEW).executeWithoutResult(inner -> {
                    repository(dslContext, provider).findById(id).orElseThrow();
                    assertThat(cacheProvider.currentTransactionCache()).isPresent()
                        .get().isNotSameAs(outerCache);
                });

                assertThat(cacheProvider.currentTransactionCache()).containsSame(outerCache);
                assertThat(cached(id)).as("what the outer transaction loaded").isPresent();
            });

            assertThat(dataSource.openConnections()).isZero();
            assertThat(cacheProvider.currentTransactionCache()).isEmpty();
            repository(dslContext, provider).deleteById(id);
        }

        @Test
        void aRolledBackRequiresNewTransactionLeavesNothingInTheCacheOfTheOuterOne() {
            var id = insertedRoot(dslContext, provider);

            transaction.executeWithoutResult(outer -> {
                var outerRoot = repository(dslContext, provider).findById(id).orElseThrow();

                assertThatThrownBy(() -> transaction(TransactionDefinition.PROPAGATION_REQUIRES_NEW)
                    .executeWithoutResult(inner -> {
                        var innerRoot = repository(dslContext, provider).findById(id).orElseThrow();
                        innerRoot.setName("RolledBackInner");
                        repository(dslContext, provider).update(innerRoot);
                        repository(dslContext, provider).findById(id).orElseThrow();
                        throw new IllegalStateException("roll back");
                    })).isInstanceOf(IllegalStateException.class);

                assertThat(cached(id).map(TestRootSimple::getName)).as("cached state of the outer transaction")
                    .hasValue("ConnectionRelease");
                outerRoot.setName("Outer");
                repository(dslContext, provider).update(outerRoot);
            });

            assertThat(nameOf(dslContext, provider, id)).isEqualTo("Outer");
            assertThat(dataSource.openConnections()).isZero();
            repository(dslContext, provider).deleteById(id);
        }

        @Test
        void anOuterTransactionDoesNotOverwriteWhatACommittedRequiresNewTransactionChanged() {
            var id = insertedRoot(dslContext, provider);

            assertThatThrownBy(() -> transaction.executeWithoutResult(outer -> {
                var outerRoot = repository(dslContext, provider).findById(id).orElseThrow();

                transaction(TransactionDefinition.PROPAGATION_REQUIRES_NEW).executeWithoutResult(inner -> {
                    var innerRoot = repository(dslContext, provider).findById(id).orElseThrow();
                    innerRoot.setName("CommittedInner");
                    repository(dslContext, provider).update(innerRoot);
                });

                outerRoot.setName("Outer");
                repository(dslContext, provider).update(outerRoot);
            })).as("optimistic locking conflict").isInstanceOf(RuntimeException.class);

            assertThat(nameOf(dslContext, provider, id)).isEqualTo("CommittedInner");
            assertThat(dataSource.openConnections()).isZero();
            assertThat(cacheProvider.currentTransactionCache()).isEmpty();
            repository(dslContext, provider).deleteById(id);
        }

        @Test
        void aRolledBackNestedTransactionLeavesNothingInTheCache() {
            var id = insertedRoot(dslContext, provider);

            transaction.executeWithoutResult(outer -> {
                var outerRoot = repository(dslContext, provider).findById(id).orElseThrow();

                assertThatThrownBy(() -> transaction(TransactionDefinition.PROPAGATION_NESTED)
                    .executeWithoutResult(nested -> {
                        var nestedRoot = repository(dslContext, provider).findById(id).orElseThrow();
                        nestedRoot.setName("RolledBackNested");
                        repository(dslContext, provider).update(nestedRoot);
                        repository(dslContext, provider).findById(id).orElseThrow();
                        throw new IllegalStateException("roll back to the savepoint");
                    })).isInstanceOf(IllegalStateException.class);

                assertThat(cached(id).map(TestRootSimple::getName))
                    .as("cached state rolled back to the savepoint")
                    .isNotEqualTo(Optional.of("RolledBackNested"));
                outerRoot.setName("Outer");
                repository(dslContext, provider).update(outerRoot);
            });

            assertThat(nameOf(dslContext, provider, id)).isEqualTo("Outer");
            assertThat(dataSource.openConnections()).isZero();
            assertThat(cacheProvider.currentTransactionCache()).isEmpty();
            repository(dslContext, provider).deleteById(id);
        }

        @Test
        void nothingIsCachedWithoutATransaction_When_OnlySynchronizationIsActive() {
            transaction(TransactionDefinition.PROPAGATION_SUPPORTS)
                .executeWithoutResult(status -> JooqConnectionRelease_ITest.this.nothingIsCachedOutsideATransaction(dslContext, provider));

            assertThat(dataSource.openConnections()).isZero();
        }
    }
}
