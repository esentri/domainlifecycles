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

import com.atomikos.icatch.jta.TransactionSynchronizationRegistryImp;
import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import io.domainlifecycles.jooq.imp.JooqEntityIdentityProvider;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.jooq.persistence.BaseDLCTestPersistenceConfiguration;
import io.domainlifecycles.jooq.persistence.tests.oneToMany.OneToManyAggregateRootRepository;
import io.domainlifecycles.jooq.persistence.tests.simple.SimpleAggregateRootRepository;
import io.domainlifecycles.persistence.cache.AggregateCacheSupport;
import io.domainlifecycles.persistence.cache.ThreadBoundTransactionCacheProvider;
import io.domainlifecycles.persistence.jta.cache.JtaTransactionCacheProvider;
import io.domainlifecycles.persistence.spring.cache.SpringTransactionCacheProvider;
import org.h2.jdbcx.JdbcDataSource;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.UpdatableRecord;
import org.jooq.TransactionListener;
import org.jooq.TransactionContext;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultTransactionListenerProvider;
import org.jooq.impl.ThreadLocalTransactionProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DelegatingDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;
import tests.shared.persistence.ConnectionTrackingDataSource;
import tests.shared.persistence.PersistenceEventTestHelper;
import tests.shared.persistence.domain.oneToMany.TestEntityOneToMany;
import tests.shared.persistence.domain.oneToMany.TestEntityOneToManyId;
import tests.shared.persistence.domain.oneToMany.TestRootOneToMany;
import tests.shared.persistence.domain.oneToMany.TestRootOneToManyId;
import tests.shared.persistence.domain.simple.TestRootSimple;
import tests.shared.persistence.domain.simple.TestRootSimpleId;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Checks the transaction cache of the jOOQ integration against the transaction boundaries it relies on - jOOQ's own
 * (nested) transactions, JTA with and without Spring, and a commit that fails. Each test expects the correct
 * behavior: a state that was rolled back, or belongs to another transaction, must never be used for a write.
 * <p>
 * The central scenario is a lost delete: a part of the work that is rolled back removes a child entity and loads the
 * aggregate again, so that a cache scope holds the aggregate without that child - which is back in the database once
 * the part is rolled back. The work that remains then removes the same child from the aggregate it loaded before. Is
 * the rolled back state taken from the cache as the current one, the removal looks like no change, and the child is
 * never deleted.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JooqTransactionCacheBoundaries_ITest {

    private static final String TABLE = "TEST_ROOT_SIMPLE";
    private static final String H2_URL = "jdbc:h2:./build/h2-db/test;AUTO_SERVER=TRUE";
    private static final String KEPT = "Kept";
    private static final String REMOVED = "Removed";

    private final BaseDLCTestPersistenceConfiguration testConfiguration = new BaseDLCTestPersistenceConfiguration();

    private final PersistenceEventTestHelper persistenceEventTestHelper = new PersistenceEventTestHelper();

    private static DSLContext dslContext(Configuration configuration) {
        configuration.settings().setExecuteWithOptimisticLocking(true);
        configuration.set(SQLDialect.H2);
        return new DefaultDSLContext(configuration);
    }

    private SimpleAggregateRootRepository repository(DSLContext dslContext, JooqDomainPersistenceProvider provider) {
        persistenceEventTestHelper.resetEventsCaught();
        return new SimpleAggregateRootRepository(dslContext, persistenceEventTestHelper.testEventPublisher, provider);
    }

    private OneToManyAggregateRootRepository aggregates(DSLContext dslContext, JooqDomainPersistenceProvider provider) {
        persistenceEventTestHelper.resetEventsCaught();
        return new OneToManyAggregateRootRepository(dslContext, persistenceEventTestHelper.testEventPublisher, provider);
    }

    private TestRootSimpleId insertedRoot(DSLContext dslContext, JooqDomainPersistenceProvider provider) {
        var id = (TestRootSimpleId) new JooqEntityIdentityProvider(dslContext).provideFor(TestRootSimple.class.getName());
        repository(dslContext, provider).insert(TestRootSimple.builder().setId(id).setName("Original").build());
        return id;
    }

    /**
     * An aggregate with two children, {@link #KEPT} and {@link #REMOVED}.
     */
    private TestRootOneToManyId insertedAggregate(DSLContext dslContext, JooqDomainPersistenceProvider provider) {
        var id = new TestRootOneToManyId(newId());
        aggregates(dslContext, provider).insert(TestRootOneToMany.builder()
            .setId(id)
            .setName("Original")
            .setTestEntityOneToManyList(new ArrayList<>(List.of(child(id, KEPT), child(id, REMOVED))))
            .build());
        return id;
    }

    private static TestEntityOneToMany child(TestRootOneToManyId rootId, String name) {
        return TestEntityOneToMany.builder()
            .setId(new TestEntityOneToManyId(newId()))
            .setName(name)
            .setTestRootId(rootId)
            .build();
    }

    private static long newId() {
        return ThreadLocalRandom.current().nextLong(1_000_000_000L, 100_000_000_000_000_000L);
    }

    private List<String> childrenOf(DSLContext dslContext, JooqDomainPersistenceProvider provider,
                                    TestRootOneToManyId id) {
        return aggregates(dslContext, provider).findById(id).orElseThrow().getTestEntityOneToManyList().stream()
            .map(TestEntityOneToMany::getName)
            .toList();
    }

    private static void removeChild(TestRootOneToMany root) {
        root.setTestEntityOneToManyList(new ArrayList<>(root.getTestEntityOneToManyList().stream()
            .filter(child -> !REMOVED.equals(child.getName()))
            .toList()));
    }

    /**
     * The part that is rolled back afterwards: removes {@link #REMOVED} and loads the aggregate again.
     */
    private void removeAndLoadAgain(DSLContext dslContext, JooqDomainPersistenceProvider provider,
                                    TestRootOneToManyId id) {
        var aggregates = aggregates(dslContext, provider);
        var root = aggregates.findById(id).orElseThrow();
        removeChild(root);
        aggregates.update(root);
        aggregates.findById(id).orElseThrow();
    }

    /**
     * The work that remains: removes {@link #REMOVED} from the aggregate it loaded before.
     */
    private void removeFrom(DSLContext dslContext, JooqDomainPersistenceProvider provider,
                            TestRootOneToMany loadedBefore) {
        removeChild(loadedBefore);
        aggregates(dslContext, provider).update(loadedBefore);
    }

    /**
     * Updates a root loaded in the same transaction and returns the SELECTs the update issued, besides the one
     * jOOQ's optimistic locking issues.
     */
    private long selectsOfAnUpdateAfterLoading(ConnectionTrackingDataSource dataSource, DSLContext dslContext,
                                               JooqDomainPersistenceProvider provider, TestRootSimpleId id) {
        var root = repository(dslContext, provider).findById(id).orElseThrow();
        root.setName("Updated");
        dataSource.clearPreparedStatements();
        repository(dslContext, provider).update(root);
        var lockingSelects = dataSource.preparedStatements().stream()
            .filter(sql -> sql.toUpperCase().contains("FOR UPDATE"))
            .count();
        return dataSource.selectsFrom(TABLE) - lockingSelects;
    }

    /**
     * Without Spring: jOOQ's own transactions, the DSLContext taking part via its ThreadLocalTransactionProvider.
     */
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class JooqTransactionsWithoutSpring {

        private final ConnectionTrackingDataSource dataSource =
            new ConnectionTrackingDataSource(testConfiguration.dataSource);

        private final DSLContext dslContext = dslContext(new DefaultConfiguration()
            .set(new ThreadLocalTransactionProvider(new DataSourceConnectionProvider(dataSource))));

        // registers the TransactionCacheJooqBinder on the DSLContext
        private final JooqDomainPersistenceProvider provider = new JooqDomainPersistenceProvider(
            testConfiguration.newDomainPersistenceConfiguration(null), dslContext);

        @Test
        void aRolledBackNestedTransactionLosesNoWriteOfTheOuterOne() {
            var id = insertedAggregate(dslContext, provider);

            dslContext.transaction(() -> {
                var loadedBefore = aggregates(dslContext, provider).findById(id).orElseThrow();
                assertThatThrownBy(() -> dslContext.transaction(() -> {
                    removeAndLoadAgain(dslContext, provider, id);
                    throw new IllegalStateException("roll back to the savepoint");
                })).isInstanceOf(IllegalStateException.class);
                removeFrom(dslContext, provider, loadedBefore);
            });

            assertThat(childrenOf(dslContext, provider, id)).as("removed after the nested transaction rolled back")
                .containsExactly(KEPT);
            assertThat(dataSource.openConnections()).isZero();
        }

        @Test
        void withoutTheCacheARolledBackNestedTransactionLosesNoWriteOfTheOuterOne() {
            // no binder, so no scope is ever opened: every write reads the current state
            var withoutCache = new JooqDomainPersistenceProvider(
                testConfiguration.newDomainPersistenceConfiguration(new ThreadBoundTransactionCacheProvider<>()));
            var id = insertedAggregate(dslContext, withoutCache);

            dslContext.transaction(() -> {
                var loadedBefore = aggregates(dslContext, withoutCache).findById(id).orElseThrow();
                assertThatThrownBy(() -> dslContext.transaction(() -> {
                    removeAndLoadAgain(dslContext, withoutCache, id);
                    throw new IllegalStateException("roll back to the savepoint");
                })).isInstanceOf(IllegalStateException.class);
                removeFrom(dslContext, withoutCache, loadedBefore);
            });

            assertThat(childrenOf(dslContext, withoutCache, id)).containsExactly(KEPT);
        }

        @Test
        void aTransactionDoesNotInheritTheCacheOfOneRolledBackBefore() {
            var id = insertedAggregate(dslContext, provider);
            var loadedBefore = aggregates(dslContext, provider).findById(id).orElseThrow();

            assertThatThrownBy(() -> dslContext.transaction(() -> {
                removeAndLoadAgain(dslContext, provider, id);
                throw new IllegalStateException("roll back");
            })).isInstanceOf(IllegalStateException.class);
            dslContext.transaction(() -> removeFrom(dslContext, provider, loadedBefore));

            assertThat(childrenOf(dslContext, provider, id)).containsExactly(KEPT);
            assertThat(dataSource.openConnections()).isZero();
        }
    }

    /**
     * Without Spring, jOOQ's own transactions failing on their way: when beginning or committing, also a nested one.
     * jOOQ reports the end of every transaction it began - a failing commit even twice, as committed and then as
     * rolled back.
     */
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class JooqTransactionsFailingWithoutSpring {

        private boolean failToConnect;
        private boolean failToCommit;
        private boolean failToCommitTheNextTransaction;

        private final ConnectionTrackingDataSource dataSource = new ConnectionTrackingDataSource(
            new DelegatingDataSource(testConfiguration.dataSource) {
                @Override
                public Connection getConnection() throws SQLException {
                    if (failToConnect) {
                        throw new SQLException("no connection");
                    }
                    var connection = super.getConnection();
                    return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
                        new Class<?>[]{Connection.class}, (proxy, method, args) -> {
                            if (failToCommit && "commit".equals(method.getName())) {
                                throw new SQLException("commit fails");
                            }
                            try {
                                return method.invoke(connection, args);
                            } catch (InvocationTargetException e) {
                                throw e.getCause();
                            }
                        });
                }
            });

        // fails the commit of a (nested) transaction the way jOOQ fails it on an error of the transaction provider
        private final DSLContext dslContext = dslContext(new DefaultConfiguration()
            .set(new ThreadLocalTransactionProvider(new DataSourceConnectionProvider(dataSource)))
            .set(new DefaultTransactionListenerProvider(new TransactionListener() {
                @Override
                public void commitStart(TransactionContext ctx) {
                    if (failToCommitTheNextTransaction) {
                        failToCommitTheNextTransaction = false;
                        throw new IllegalStateException("commit fails");
                    }
                }
            })));

        // registers the TransactionCacheJooqBinder on the DSLContext, after the listener above
        private final JooqDomainPersistenceProvider provider = new JooqDomainPersistenceProvider(
            testConfiguration.newDomainPersistenceConfiguration(null), dslContext);

        @AfterEach
        void failNoMore() {
            failToConnect = false;
            failToCommit = false;
            failToCommitTheNextTransaction = false;
        }

        @Test
        void aTransactionFailingToBeginLeavesNoCacheForTheNextOne() {
            failToConnect = true;
            assertThatThrownBy(() -> dslContext.transaction(() -> { })).isInstanceOf(RuntimeException.class);
            failToConnect = false;

            assertThat(provider.transactionCacheProvider.currentTransactionCache()).isEmpty();
            var cacheOfTheNextOne = dslContext.transactionResult(
                () -> provider.transactionCacheProvider.currentTransactionCache().orElseThrow());
            assertThat(provider.transactionCacheProvider.currentTransactionCache())
                .as("closed with the next transaction").isEmpty();
            assertThat(cacheOfTheNextOne).isNotNull();
            assertThat(dataSource.openConnections()).isZero();
        }

        @Test
        void aTransactionFailingToCommitLeavesNothingForTheNextOne() {
            var id = insertedAggregate(dslContext, provider);
            var loadedBefore = aggregates(dslContext, provider).findById(id).orElseThrow();

            assertThatThrownBy(() -> dslContext.transaction(() -> {
                removeAndLoadAgain(dslContext, provider, id);
                failToCommit = true;
            })).isInstanceOf(RuntimeException.class);
            failToCommit = false;

            assertThat(provider.transactionCacheProvider.currentTransactionCache()).isEmpty();
            assertThat(childrenOf(dslContext, provider, id)).as("rolled back").containsExactlyInAnyOrder(KEPT, REMOVED);
            dslContext.transaction(() -> removeFrom(dslContext, provider, loadedBefore));
            assertThat(childrenOf(dslContext, provider, id)).containsExactly(KEPT);
            assertThat(dataSource.openConnections()).isZero();
        }

        @Test
        void aNestedTransactionFailingToCommitLosesNoWriteOfTheOuterOne() {
            var id = insertedAggregate(dslContext, provider);

            dslContext.transaction(() -> {
                var loadedBefore = aggregates(dslContext, provider).findById(id).orElseThrow();
                assertThatThrownBy(() -> dslContext.transaction(() -> {
                    removeAndLoadAgain(dslContext, provider, id);
                    failToCommitTheNextTransaction = true;
                })).isInstanceOf(RuntimeException.class);
                removeFrom(dslContext, provider, loadedBefore);
            });

            assertThat(childrenOf(dslContext, provider, id)).as("removed after the nested transaction rolled back")
                .containsExactly(KEPT);
            assertThat(dataSource.openConnections()).isZero();
        }

        @Test
        void theOuterTransactionKeepsItsCache_When_ANestedOneFailsToCommit() {
            var id = insertedRoot(dslContext, provider);

            dslContext.transaction(() -> {
                var cache = provider.transactionCacheProvider.currentTransactionCache().orElseThrow();
                assertThatThrownBy(() -> dslContext.transaction(() -> failToCommitTheNextTransaction = true))
                    .isInstanceOf(RuntimeException.class);

                assertThat(provider.transactionCacheProvider.currentTransactionCache())
                    .as("the cache of the outer transaction").containsSame(cache);
                assertThat(selectsOfAnUpdateAfterLoading(dataSource, dslContext, provider, id))
                    .as("SELECTs of an update after the nested transaction failed").isZero();
            });

            assertThat(provider.transactionCacheProvider.currentTransactionCache()).isEmpty();
        }
    }

    /**
     * JTA without Spring: Atomikos as transaction manager, an XA data source, and the cache provided per JTA
     * transaction by a {@link JtaTransactionCacheProvider}.
     */
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class JtaWithoutSpring {

        private UserTransactionManager transactionManager;
        private AtomikosDataSourceBean xaDataSource;
        private ConnectionTrackingDataSource dataSource;
        private DSLContext dslContext;
        private JooqDomainPersistenceProvider provider;

        @BeforeAll
        void startTransactionManager() {
            transactionManager = atomikos();
            xaDataSource = xaDataSource("dlcJooqJta");
            dataSource = new ConnectionTrackingDataSource(xaDataSource);
            dslContext = dslContext(new DefaultConfiguration().set(new DataSourceConnectionProvider(dataSource)));
            provider = new JooqDomainPersistenceProvider(testConfiguration.newDomainPersistenceConfiguration(
                new JtaTransactionCacheProvider<>(new TransactionSynchronizationRegistryImp())));
        }

        @Test
        void aRolledBackTransactionLosesNoWriteOfTheOneSuspendedForIt() throws Exception {
            var id = insertedAggregate(dslContext, provider);

            transactionManager.begin();
            var loadedBefore = aggregates(dslContext, provider).findById(id).orElseThrow();
            var outer = transactionManager.suspend();
            transactionManager.begin();
            removeAndLoadAgain(dslContext, provider, id);
            transactionManager.rollback();
            transactionManager.resume(outer);
            removeFrom(dslContext, provider, loadedBefore);
            transactionManager.commit();

            assertThat(childrenOf(dslContext, provider, id)).containsExactly(KEPT);
            assertThat(dataSource.openConnections()).isZero();
        }

        @AfterAll
        void stopTransactionManager() {
            xaDataSource.close();
            transactionManager.close();
        }

        @Test
        void aTransactionDoesNotInheritTheCacheOfOneRolledBackBefore() throws Exception {
            var id = insertedAggregate(dslContext, provider);
            var loadedBefore = aggregates(dslContext, provider).findById(id).orElseThrow();

            transactionManager.begin();
            removeAndLoadAgain(dslContext, provider, id);
            transactionManager.rollback();

            transactionManager.begin();
            removeFrom(dslContext, provider, loadedBefore);
            transactionManager.commit();

            assertThat(childrenOf(dslContext, provider, id)).containsExactly(KEPT);
            assertThat(dataSource.openConnections()).isZero();
        }

        @Test
        void theCacheWorksWithinATransaction() throws Exception {
            var id = insertedRoot(dslContext, provider);

            transactionManager.begin();
            var selects = selectsOfAnUpdateAfterLoading(dataSource, dslContext, provider, id);
            transactionManager.commit();

            assertThat(selects).as("SELECTs of the update").isZero();
        }
    }

    /**
     * Spring with JTA: Spring's JtaTransactionManager on Atomikos, wired as DlcJooqPersistenceAutoConfiguration does.
     */
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class SpringWithJta {

        private UserTransactionManager atomikos;
        private AtomikosDataSourceBean xaDataSource;
        private ConnectionTrackingDataSource dataSource;
        private final SpringTransactionCacheProvider<UpdatableRecord<?>> cacheProvider =
            new SpringTransactionCacheProvider<>();
        private DSLContext dslContext;
        private JooqDomainPersistenceProvider provider;
        private PlatformTransactionManager transactionManager;

        @BeforeAll
        void startTransactionManager() throws Exception {
            atomikos = atomikos();
            xaDataSource = xaDataSource("dlcJooqSpringJta");
            dataSource = new ConnectionTrackingDataSource(xaDataSource);
            dslContext = springDslContext(dataSource);
            provider = new JooqDomainPersistenceProvider(testConfiguration.newDomainPersistenceConfiguration(cacheProvider));
            transactionManager = new JtaTransactionManager(new UserTransactionImp(), atomikos);
        }

        @AfterAll
        void stopTransactionManager() {
            xaDataSource.close();
            atomikos.close();
        }

        private TransactionTemplate transaction(int propagation) {
            var template = new TransactionTemplate(transactionManager);
            template.setPropagationBehavior(propagation);
            return template;
        }

        @Test
        void theCacheWorksWithinATransactionAndEndsWithIt() {
            var id = insertedRoot(dslContext, provider);

            var selects = transaction(TransactionDefinition.PROPAGATION_REQUIRED)
                .execute(status -> selectsOfAnUpdateAfterLoading(dataSource, dslContext, provider, id));

            assertThat(selects).as("SELECTs of the update").isZero();
            assertThat(cacheProvider.currentTransactionCache()).isEmpty();
            assertThat(dataSource.openConnections()).isZero();
        }

        @Test
        void aRolledBackRequiresNewTransactionLosesNoWriteOfTheOuterOne() {
            var id = insertedAggregate(dslContext, provider);

            transaction(TransactionDefinition.PROPAGATION_REQUIRED).executeWithoutResult(outer -> {
                var loadedBefore = aggregates(dslContext, provider).findById(id).orElseThrow();
                assertThatThrownBy(() -> transaction(TransactionDefinition.PROPAGATION_REQUIRES_NEW)
                    .executeWithoutResult(inner -> {
                        removeAndLoadAgain(dslContext, provider, id);
                        throw new IllegalStateException("roll back");
                    })).isInstanceOf(IllegalStateException.class);
                removeFrom(dslContext, provider, loadedBefore);
            });

            assertThat(childrenOf(dslContext, provider, id)).containsExactly(KEPT);
            assertThat(cacheProvider.currentTransactionCache()).isEmpty();
            assertThat(dataSource.openConnections()).isZero();
        }

        @Test
        void aTransactionDoesNotInheritTheCacheOfOneRolledBackBefore() {
            var id = insertedAggregate(dslContext, provider);
            var loadedBefore = aggregates(dslContext, provider).findById(id).orElseThrow();

            assertThatThrownBy(() -> transaction(TransactionDefinition.PROPAGATION_REQUIRED)
                .executeWithoutResult(status -> {
                    removeAndLoadAgain(dslContext, provider, id);
                    throw new IllegalStateException("roll back");
                })).isInstanceOf(IllegalStateException.class);
            transaction(TransactionDefinition.PROPAGATION_REQUIRED)
                .executeWithoutResult(status -> removeFrom(dslContext, provider, loadedBefore));

            assertThat(childrenOf(dslContext, provider, id)).containsExactly(KEPT);
            assertThat(dataSource.openConnections()).isZero();
        }
    }

    /**
     * Spring with a DataSourceTransactionManager, as DlcJooqPersistenceAutoConfiguration wires it.
     */
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class SpringWithAFailingCommit {

        private final ConnectionTrackingDataSource dataSource =
            new ConnectionTrackingDataSource(testConfiguration.dataSource);
        private final SpringTransactionCacheProvider<UpdatableRecord<?>> cacheProvider =
            new SpringTransactionCacheProvider<>();
        private final DSLContext dslContext = springDslContext(dataSource);
        private final JooqDomainPersistenceProvider provider =
            new JooqDomainPersistenceProvider(testConfiguration.newDomainPersistenceConfiguration(cacheProvider));
        private final TransactionTemplate transaction =
            new TransactionTemplate(new DataSourceTransactionManager(dataSource));

        @Test
        void aWriteFailingMidwayLeavesNoStateOfTheAggregateInTheCache() {
            var id = insertedAggregate(dslContext, provider);
            // loaded outside the transaction: the update loads the current state itself, which fills the cache
            var loadedBefore = aggregates(dslContext, provider).findById(id).orElseThrow();
            loadedBefore.setName("Changed");
            var children = new ArrayList<>(loadedBefore.getTestEntityOneToManyList());
            children.add(child(id, "x".repeat(201)));
            loadedBefore.setTestEntityOneToManyList(children);

            transaction.executeWithoutResult(status -> {
                assertThatThrownBy(() -> aggregates(dslContext, provider).update(loadedBefore)).as("the name is too long")
                    .isInstanceOf(RuntimeException.class);

                // the application goes on in the same transaction, the statements before the failing one written
                assertThat(cacheProvider.currentTransactionCache().orElseThrow()
                    .take(AggregateCacheSupport.keyFor(id))).as("state of the aggregate in the cache").isEmpty();
                status.setRollbackOnly();
            });

            assertThat(cacheProvider.currentTransactionCache()).isEmpty();
            assertThat(dataSource.openConnections()).isZero();
        }

        @Test
        void aFailingCommitLeavesNeitherCacheNorConnectionBehind() {
            var id = insertedAggregate(dslContext, provider);
            var loadedBefore = aggregates(dslContext, provider).findById(id).orElseThrow();

            assertThatThrownBy(() -> transaction.executeWithoutResult(status -> {
                removeAndLoadAgain(dslContext, provider, id);
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override
                    public void beforeCommit(boolean readOnly) {
                        throw new IllegalStateException("commit fails");
                    }
                });
            })).isInstanceOf(IllegalStateException.class);

            assertThat(cacheProvider.currentTransactionCache()).isEmpty();
            assertThat(dataSource.openConnections()).isZero();
            assertThat(childrenOf(dslContext, provider, id)).as("rolled back").containsExactlyInAnyOrder(KEPT, REMOVED);
            transaction.executeWithoutResult(status -> removeFrom(dslContext, provider, loadedBefore));
            assertThat(childrenOf(dslContext, provider, id)).containsExactly(KEPT);
        }
    }

    // as DlcJooqPersistenceAutoConfiguration wires it: the cache is a resource of the Spring transaction
    private static DSLContext springDslContext(DataSource dataSource) {
        return dslContext(new DefaultConfiguration()
            .set(new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource))));
    }

    private static UserTransactionManager atomikos() {
        System.setProperty("com.atomikos.icatch.log_base_dir", "build/atomikos");
        System.setProperty("com.atomikos.icatch.output_dir", "build/atomikos");
        var transactionManager = new UserTransactionManager();
        transactionManager.setForceShutdown(true);
        try {
            transactionManager.init();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return transactionManager;
    }

    private static AtomikosDataSourceBean xaDataSource(String name) {
        var xa = new JdbcDataSource();
        xa.setURL(H2_URL);
        xa.setUser("sa");
        xa.setPassword("");
        var dataSource = new AtomikosDataSourceBean();
        dataSource.setUniqueResourceName(name);
        dataSource.setXaDataSource(xa);
        dataSource.setPoolSize(5);
        // inserting and reading back outside a transaction
        dataSource.setLocalTransactionMode(true);
        return dataSource;
    }
}
