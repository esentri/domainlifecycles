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

package io.domainlifecycles.jdbc.connection;

import io.domainlifecycles.jdbc.imp.JdbcEntityIdentityProvider;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.persistence.JdbcTestPersistenceConfiguration;
import io.domainlifecycles.jdbc.persistence.containers.TestDatabaseDialect;
import io.domainlifecycles.jdbc.persistence.tests.simple.SimpleAggregateRootRepository;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.jdbc.util.JdbcRecordMapper;
import io.domainlifecycles.persistence.cache.AggregateCacheSupport;
import io.domainlifecycles.persistence.cache.ThreadBoundTransactionCacheProvider;
import io.domainlifecycles.persistence.cache.TransactionCacheProvider;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.spring.cache.SpringTransactionCacheProvider;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.DelegatingDataSource;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.TransactionDefinition;
import tests.shared.persistence.ConnectionTrackingDataSource;
import tests.shared.persistence.PersistenceEventTestHelper;
import tests.shared.persistence.domain.simple.TestRootSimple;
import tests.shared.persistence.domain.simple.TestRootSimpleId;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Verifies that the JDBC integration hands back every connection it obtains - with the transaction cache, with and
 * without Spring, within and outside a transaction, committed and rolled back - so that a connection pool can never
 * run dry. Every connection comes from a {@link ConnectionTrackingDataSource} counting the ones not closed yet.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JdbcConnectionRelease_ITest {

    private static final String TABLE = "TEST_ROOT_SIMPLE";

    private final TestDatabaseDialect database = TestDatabaseDialect.fromSystemProperty();

    private final JdbcTestPersistenceConfiguration testConfiguration = new JdbcTestPersistenceConfiguration();

    private final PersistenceEventTestHelper persistenceEventTestHelper = new PersistenceEventTestHelper();

    // sequences are referenced unqualified, so every connection needs the schema set
    private final ConnectionTrackingDataSource dataSource = new ConnectionTrackingDataSource(
        new DelegatingDataSource(database.dataSource()) {
            @Override
            public Connection getConnection() throws SQLException {
                Connection connection = super.getConnection();
                connection.setSchema(database.connectionSchema());
                return connection;
            }
        });

    private JdbcDomainPersistenceProvider persistenceProvider(
        JdbcConnectionProvider connectionProvider, TransactionCacheProvider<JdbcRecord> cacheProvider) {
        return testConfiguration.newDomainPersistenceProvider(connectionProvider, cacheProvider);
    }

    private SimpleAggregateRootRepository repository(JdbcDomainPersistenceProvider provider) {
        persistenceEventTestHelper.resetEventsCaught();
        return new SimpleAggregateRootRepository(provider, persistenceEventTestHelper.testEventPublisher);
    }

    private static TestRootSimple newRoot(JdbcDomainPersistenceProvider provider) {
        var id = (TestRootSimpleId) new JdbcEntityIdentityProvider(provider.connectionProvider, provider.dialect)
            .provideFor(TestRootSimple.class.getName());
        return TestRootSimple.builder().setId(id).setName("ConnectionRelease").build();
    }

    /**
     * Every operation of the repository, each one followed by the check that no connection is left open.
     */
    private void eachOperationReleasesItsConnection(JdbcDomainPersistenceProvider provider) {
        var repository = repository(provider);

        var root = newRoot(provider);
        assertThat(dataSource.openConnections()).as("after providing an id").isZero();
        repository.insert(root);
        assertThat(dataSource.openConnections()).as("after insert").isZero();
        var found = repository.findById(root.getId()).orElseThrow();
        assertThat(dataSource.openConnections()).as("after findById").isZero();
        found.setName("ConnectionReleaseUpdated");
        repository.update(found);
        assertThat(dataSource.openConnections()).as("after update").isZero();
        JdbcRecordMapper.selectByColumn(provider.connectionProvider, provider.dialect,
            provider.schemaMetadata.table(TABLE), "NAME", "ConnectionReleaseUpdated");
        assertThat(dataSource.openConnections()).as("after a custom query").isZero();
        assertThatThrownBy(() -> repository.insert(found)).isInstanceOf(DLCPersistenceException.class);
        assertThat(dataSource.openConnections()).as("after a failing insert").isZero();
        repository.deleteById(root.getId());
        assertThat(dataSource.openConnections()).as("after deleteById").isZero();
        assertThat(repository.findById(root.getId())).isEmpty();
    }

    /**
     * Loads and updates a root in what the caller runs as one transaction: exactly the transaction's connection is
     * in use meanwhile, and the update takes the state loaded before from the transaction cache.
     */
    private TestRootSimpleId loadAndUpdateInOneTransaction(JdbcDomainPersistenceProvider provider,
                                                           TestRootSimpleId id) {
        var repository = repository(provider);
        var found = repository.findById(id).orElseThrow();
        found.setName("UpdatedInTransaction");
        dataSource.clearPreparedStatements();
        repository.update(found);
        assertThat(dataSource.selectsFrom(TABLE)).as("SELECTs of the update").isZero();
        assertThat(dataSource.openConnections()).as("connections within the transaction").isEqualTo(1);
        return id;
    }

    private TestRootSimpleId insertedRoot(JdbcDomainPersistenceProvider provider) {
        var root = newRoot(provider);
        repository(provider).insert(root);
        return root.getId();
    }

    /**
     * Several reads and writes of two aggregates in what the caller runs as one transaction: they all share one
     * transaction cache, and every update takes the state loaded before from it - also for an aggregate loaded again
     * after it was written.
     */
    private void severalReadsAndWritesShareOneCache(JdbcDomainPersistenceProvider provider,
                                                    TestRootSimpleId first, TestRootSimpleId second) {
        var repository = repository(provider);
        var a = repository.findById(first).orElseThrow();
        var cache = provider.transactionCacheProvider.currentTransactionCache().orElseThrow();
        var b = repository.findById(second).orElseThrow();

        a.setName("FirstWrite");
        dataSource.clearPreparedStatements();
        repository.update(a);
        assertThat(dataSource.selectsFrom(TABLE)).as("SELECTs of the first update").isZero();

        b.setName("SecondWrite");
        dataSource.clearPreparedStatements();
        repository.update(b);
        assertThat(dataSource.selectsFrom(TABLE)).as("SELECTs of the second update").isZero();

        var aAgain = repository.findById(first).orElseThrow();
        aAgain.setName("ThirdWrite");
        dataSource.clearPreparedStatements();
        repository.update(aAgain);
        assertThat(dataSource.selectsFrom(TABLE)).as("SELECTs of the update after loading again").isZero();

        assertThat(provider.transactionCacheProvider.currentTransactionCache()).containsSame(cache);
        assertThat(dataSource.openConnections()).as("connections within the transaction").isEqualTo(1);
    }

    /**
     * Outside a transaction, nothing is cached: an update reads the current state again.
     */
    private void nothingIsCachedOutsideATransaction(JdbcDomainPersistenceProvider provider) {
        var id = insertedRoot(provider);
        var repository = repository(provider);
        var found = repository.findById(id).orElseThrow();
        assertThat(provider.transactionCacheProvider.currentTransactionCache()).isEmpty();
        found.setName("UpdatedWithoutTransaction");
        dataSource.clearPreparedStatements();
        repository.update(found);
        assertThat(dataSource.selectsFrom(TABLE)).as("SELECTs of the update").isEqualTo(1);
        assertThat(provider.transactionCacheProvider.currentTransactionCache()).isEmpty();
        repository.deleteById(id);
    }

    private String nameOf(JdbcDomainPersistenceProvider provider, TestRootSimpleId id) {
        return repository(provider).findById(id).map(TestRootSimple::getName).orElseThrow();
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class WithoutSpring {

        private final ThreadLocal<Connection> transactionConnection = new ThreadLocal<>();

        /**
         * Hands out the connection of the running transaction, if any, and otherwise a new one from the pool,
         * which is closed again when released.
         */
        private final JdbcConnectionProvider pooled = new JdbcConnectionProvider() {
            @Override
            public Connection getConnection() {
                var bound = transactionConnection.get();
                if (bound != null) {
                    return bound;
                }
                try {
                    return dataSource.getConnection();
                } catch (SQLException e) {
                    throw new IllegalStateException(e);
                }
            }

            @Override
            public void releaseConnection(Connection connection) {
                if (connection != transactionConnection.get()) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }
            }
        };

        private final ThreadBoundTransactionCacheProvider<JdbcRecord> cacheProvider =
            new ThreadBoundTransactionCacheProvider<>();

        private final JdbcDomainPersistenceProvider provider = persistenceProvider(pooled, cacheProvider);

        /**
         * Runs the work as one plain JDBC transaction, with a transaction cache scope opened around it - as an
         * application driving its own transactions does.
         */
        private void inTransaction(Runnable work, boolean commit) throws SQLException {
            var connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            transactionConnection.set(connection);
            try (var scope = cacheProvider.open()) {
                work.run();
                if (commit) {
                    connection.commit();
                } else {
                    connection.rollback();
                }
            } finally {
                transactionConnection.remove();
                connection.close();
            }
        }

        @Test
        void everyOperationOutsideATransactionReleasesItsConnection() {
            eachOperationReleasesItsConnection(provider);
        }

        @Test
        void aCommittedTransactionLeavesNoConnectionOpen() throws SQLException {
            var id = insertedRoot(provider);

            inTransaction(() -> loadAndUpdateInOneTransaction(provider, id), true);

            assertThat(dataSource.openConnections()).isZero();
            assertThat(provider.transactionCacheProvider.currentTransactionCache()).isEmpty();
            assertThat(nameOf(provider, id)).isEqualTo("UpdatedInTransaction");
            repository(provider).deleteById(id);
        }

        @Test
        void aRolledBackTransactionLeavesNoConnectionOpen() throws SQLException {
            var id = insertedRoot(provider);

            inTransaction(() -> loadAndUpdateInOneTransaction(provider, id), false);

            assertThat(dataSource.openConnections()).isZero();
            assertThat(provider.transactionCacheProvider.currentTransactionCache()).isEmpty();
            assertThat(nameOf(provider, id)).isEqualTo("ConnectionRelease");
            repository(provider).deleteById(id);
        }
        @Test
        void aTransactionKeepsOneCacheForAllItsReadsAndWrites() throws SQLException {
            var first = insertedRoot(provider);
            var second = insertedRoot(provider);

            inTransaction(() -> severalReadsAndWritesShareOneCache(provider, first, second), true);

            assertThat(dataSource.openConnections()).isZero();
            assertThat(provider.transactionCacheProvider.currentTransactionCache()).isEmpty();
            assertThat(nameOf(provider, first)).isEqualTo("ThirdWrite");
            assertThat(nameOf(provider, second)).isEqualTo("SecondWrite");
            repository(provider).deleteById(first);
            repository(provider).deleteById(second);
        }

        @Test
        void nothingIsCachedOutsideATransaction() {
            JdbcConnectionRelease_ITest.this.nothingIsCachedOutsideATransaction(provider);
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class WithSpring {

        private final SpringTransactionCacheProvider<JdbcRecord> cacheProvider =
            new SpringTransactionCacheProvider<>();

        // as DlcJdbcPersistenceAutoConfiguration wires it
        private final JdbcDomainPersistenceProvider provider = persistenceProvider(
            new JdbcConnectionProvider() {
                @Override
                public Connection getConnection() {
                    return DataSourceUtils.getConnection(dataSource);
                }

                @Override
                public void releaseConnection(Connection connection) {
                    DataSourceUtils.releaseConnection(connection, dataSource);
                }
            },
            cacheProvider);

        private final DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);

        private final TransactionTemplate transaction = new TransactionTemplate(transactionManager);

        @Test
        void everyOperationOutsideATransactionReleasesItsConnection() {
            eachOperationReleasesItsConnection(provider);
        }

        @Test
        void aCommittedTransactionLeavesNoConnectionOpen() {
            var id = insertedRoot(provider);

            transaction.executeWithoutResult(status -> loadAndUpdateInOneTransaction(provider, id));

            assertThat(dataSource.openConnections()).isZero();
            assertThat(cacheProvider.currentTransactionCache()).isEmpty();
            assertThat(nameOf(provider, id)).isEqualTo("UpdatedInTransaction");
            repository(provider).deleteById(id);
        }

        @Test
        void aRolledBackTransactionLeavesNoConnectionOpen() {
            var id = insertedRoot(provider);

            assertThatThrownBy(() -> transaction.executeWithoutResult(status -> {
                loadAndUpdateInOneTransaction(provider, id);
                throw new IllegalStateException("roll back");
            })).isInstanceOf(IllegalStateException.class);

            assertThat(dataSource.openConnections()).isZero();
            assertThat(cacheProvider.currentTransactionCache()).isEmpty();
            assertThat(nameOf(provider, id)).isEqualTo("ConnectionRelease");
            repository(provider).deleteById(id);
        }
        @Test
        void aTransactionKeepsOneCacheForAllItsReadsAndWrites() {
            var first = insertedRoot(provider);
            var second = insertedRoot(provider);

            transaction.executeWithoutResult(status -> severalReadsAndWritesShareOneCache(provider, first, second));

            assertThat(dataSource.openConnections()).isZero();
            assertThat(cacheProvider.currentTransactionCache()).isEmpty();
            assertThat(nameOf(provider, first)).isEqualTo("ThirdWrite");
            assertThat(nameOf(provider, second)).isEqualTo("SecondWrite");
            repository(provider).deleteById(first);
            repository(provider).deleteById(second);
        }

        @Test
        void nothingIsCachedOutsideATransaction() {
            JdbcConnectionRelease_ITest.this.nothingIsCachedOutsideATransaction(provider);
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
            var id = insertedRoot(provider);

            transaction.executeWithoutResult(outer -> {
                repository(provider).findById(id).orElseThrow();
                var outerCache = cacheProvider.currentTransactionCache().orElseThrow();

                transaction(TransactionDefinition.PROPAGATION_REQUIRES_NEW).executeWithoutResult(inner -> {
                    repository(provider).findById(id).orElseThrow();
                    assertThat(cacheProvider.currentTransactionCache()).isPresent()
                        .get().isNotSameAs(outerCache);
                });

                assertThat(cacheProvider.currentTransactionCache()).containsSame(outerCache);
                assertThat(cached(id)).as("what the outer transaction loaded").isPresent();
            });

            assertThat(dataSource.openConnections()).isZero();
            assertThat(cacheProvider.currentTransactionCache()).isEmpty();
            repository(provider).deleteById(id);
        }

        @Test
        void aRolledBackRequiresNewTransactionLeavesNothingInTheCacheOfTheOuterOne() {
            var id = insertedRoot(provider);

            transaction.executeWithoutResult(outer -> {
                var outerRoot = repository(provider).findById(id).orElseThrow();

                assertThatThrownBy(() -> transaction(TransactionDefinition.PROPAGATION_REQUIRES_NEW)
                    .executeWithoutResult(inner -> {
                        var innerRoot = repository(provider).findById(id).orElseThrow();
                        innerRoot.setName("RolledBackInner");
                        repository(provider).update(innerRoot);
                        repository(provider).findById(id).orElseThrow();
                        throw new IllegalStateException("roll back");
                    })).isInstanceOf(IllegalStateException.class);

                assertThat(cached(id).map(TestRootSimple::getName)).as("cached state of the outer transaction")
                    .hasValue("ConnectionRelease");
                outerRoot.setName("Outer");
                repository(provider).update(outerRoot);
            });

            assertThat(nameOf(provider, id)).isEqualTo("Outer");
            assertThat(dataSource.openConnections()).isZero();
            repository(provider).deleteById(id);
        }

        @Test
        void anOuterTransactionDoesNotOverwriteWhatACommittedRequiresNewTransactionChanged() {
            var id = insertedRoot(provider);

            assertThatThrownBy(() -> transaction.executeWithoutResult(outer -> {
                var outerRoot = repository(provider).findById(id).orElseThrow();

                transaction(TransactionDefinition.PROPAGATION_REQUIRES_NEW).executeWithoutResult(inner -> {
                    var innerRoot = repository(provider).findById(id).orElseThrow();
                    innerRoot.setName("CommittedInner");
                    repository(provider).update(innerRoot);
                });

                outerRoot.setName("Outer");
                repository(provider).update(outerRoot);
            })).as("optimistic locking conflict").isInstanceOf(RuntimeException.class);

            assertThat(nameOf(provider, id)).isEqualTo("CommittedInner");
            assertThat(dataSource.openConnections()).isZero();
            assertThat(cacheProvider.currentTransactionCache()).isEmpty();
            repository(provider).deleteById(id);
        }

        @Test
        void aRolledBackNestedTransactionLeavesNothingInTheCache() {
            var id = insertedRoot(provider);

            transaction.executeWithoutResult(outer -> {
                var outerRoot = repository(provider).findById(id).orElseThrow();

                assertThatThrownBy(() -> transaction(TransactionDefinition.PROPAGATION_NESTED)
                    .executeWithoutResult(nested -> {
                        var nestedRoot = repository(provider).findById(id).orElseThrow();
                        nestedRoot.setName("RolledBackNested");
                        repository(provider).update(nestedRoot);
                        repository(provider).findById(id).orElseThrow();
                        throw new IllegalStateException("roll back to the savepoint");
                    })).isInstanceOf(IllegalStateException.class);

                assertThat(cached(id).map(TestRootSimple::getName))
                    .as("cached state rolled back to the savepoint")
                    .isNotEqualTo(Optional.of("RolledBackNested"));
                outerRoot.setName("Outer");
                repository(provider).update(outerRoot);
            });

            assertThat(nameOf(provider, id)).isEqualTo("Outer");
            assertThat(dataSource.openConnections()).isZero();
            assertThat(cacheProvider.currentTransactionCache()).isEmpty();
            repository(provider).deleteById(id);
        }

        @Test
        void nothingIsCachedWithoutATransaction_When_OnlySynchronizationIsActive() {
            transaction(TransactionDefinition.PROPAGATION_SUPPORTS)
                .executeWithoutResult(status -> JdbcConnectionRelease_ITest.this.nothingIsCachedOutsideATransaction(provider));

            assertThat(dataSource.openConnections()).isZero();
        }
    }
}
