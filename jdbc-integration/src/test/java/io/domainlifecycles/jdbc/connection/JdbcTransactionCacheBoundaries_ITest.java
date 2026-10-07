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

import com.atomikos.icatch.jta.TransactionSynchronizationRegistryImp;
import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import io.domainlifecycles.jdbc.imp.JdbcEntityIdentityProvider;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.persistence.JdbcTestPersistenceConfiguration;
import io.domainlifecycles.jdbc.persistence.containers.TestDatabaseDialect;
import io.domainlifecycles.jdbc.persistence.tests.oneToMany.OneToManyAggregateRootRepository;
import io.domainlifecycles.jdbc.persistence.tests.simple.SimpleAggregateRootRepository;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.persistence.cache.AggregateCacheSupport;
import io.domainlifecycles.persistence.cache.NoOpTransactionCacheProvider;
import io.domainlifecycles.persistence.cache.ThreadBoundTransactionCacheProvider;
import io.domainlifecycles.persistence.cache.TransactionCacheProvider;
import io.domainlifecycles.persistence.jta.cache.JtaTransactionCacheProvider;
import io.domainlifecycles.persistence.spring.cache.SpringTransactionCacheProvider;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.DelegatingDataSource;
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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Checks the transaction cache of the JDBC integration against the transaction boundaries it relies on - plain JDBC
 * transactions and savepoints with scopes the application opens itself, JTA with and without Spring, and a commit
 * that fails. Each test expects the correct
 * behavior: a state that was rolled back, or belongs to another transaction, must never be used for a write.
 * <p>
 * The central scenario is a lost delete: a part of the work that is rolled back removes a child entity and loads the
 * aggregate again, so that a cache scope holds the aggregate without that child - which is back in the database once
 * the part is rolled back. The work that remains then removes the same child from the aggregate it loaded before. Is
 * the rolled back state taken from the cache as the current one, the removal looks like no change, and the child is
 * never deleted. (Changed root fields are written anyway: the version a write increases makes the cached root differ
 * from the one loaded before.)
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JdbcTransactionCacheBoundaries_ITest {

    private static final String TABLE = "TEST_ROOT_SIMPLE";
    private static final String ORIGINAL = "Original";
    private static final String WRITTEN = "Written";
    private static final String KEPT = "Kept";
    private static final String REMOVED = "Removed";

    private final TestDatabaseDialect database = TestDatabaseDialect.fromSystemProperty();

    private final JdbcTestPersistenceConfiguration testConfiguration = new JdbcTestPersistenceConfiguration();

    private final PersistenceEventTestHelper persistenceEventTestHelper = new PersistenceEventTestHelper();

    private SimpleAggregateRootRepository repository(JdbcDomainPersistenceProvider provider) {
        persistenceEventTestHelper.resetEventsCaught();
        return new SimpleAggregateRootRepository(provider, persistenceEventTestHelper.testEventPublisher);
    }

    private OneToManyAggregateRootRepository aggregates(JdbcDomainPersistenceProvider provider) {
        persistenceEventTestHelper.resetEventsCaught();
        return new OneToManyAggregateRootRepository(provider, persistenceEventTestHelper.testEventPublisher);
    }

    private TestRootSimpleId insertedRoot(JdbcDomainPersistenceProvider provider) {
        var id = (TestRootSimpleId) new JdbcEntityIdentityProvider(provider.connectionProvider, provider.dialect)
            .provideFor(TestRootSimple.class.getName());
        repository(provider).insert(TestRootSimple.builder().setId(id).setName(ORIGINAL).build());
        return id;
    }

    private String nameOf(JdbcDomainPersistenceProvider provider, TestRootSimpleId id) {
        return repository(provider).findById(id).map(TestRootSimple::getName).orElseThrow();
    }

    /**
     * An aggregate with two children, {@link #KEPT} and {@link #REMOVED}.
     */
    private TestRootOneToManyId insertedAggregate(JdbcDomainPersistenceProvider provider) {
        var id = new TestRootOneToManyId(newId());
        aggregates(provider).insert(TestRootOneToMany.builder()
            .setId(id)
            .setName(ORIGINAL)
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

    private List<String> childrenOf(JdbcDomainPersistenceProvider provider, TestRootOneToManyId id) {
        return aggregates(provider).findById(id).orElseThrow().getTestEntityOneToManyList().stream()
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
    private void removeAndLoadAgain(JdbcDomainPersistenceProvider provider, TestRootOneToManyId id) {
        var aggregates = aggregates(provider);
        var root = aggregates.findById(id).orElseThrow();
        removeChild(root);
        aggregates.update(root);
        aggregates.findById(id).orElseThrow();
    }

    /**
     * The work that remains: removes {@link #REMOVED} from the aggregate it loaded before.
     */
    private void removeFrom(JdbcDomainPersistenceProvider provider, TestRootOneToMany loadedBefore) {
        removeChild(loadedBefore);
        aggregates(provider).update(loadedBefore);
    }

    /**
     * Without Spring: plain JDBC transactions on a connection the application binds to the thread.
     */
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class PlainJdbcWithoutSpring {

        private final ConnectionTrackingDataSource dataSource = new ConnectionTrackingDataSource(schemaSet(database));

        private final ThreadLocal<Connection> transactionConnection = new ThreadLocal<>();

        private final JdbcConnectionProvider connectionProvider = new JdbcConnectionProvider() {
            @Override
            public Connection getConnection() {
                var bound = transactionConnection.get();
                return bound != null ? bound : open(dataSource);
            }

            @Override
            public void releaseConnection(Connection connection) {
                if (connection != transactionConnection.get()) {
                    close(connection);
                }
            }
        };

        private final ThreadBoundTransactionCacheProvider<JdbcRecord> cacheProvider =
            new ThreadBoundTransactionCacheProvider<>();

        // the cache in use only within the scopes the application opens around its transactions
        private final JdbcDomainPersistenceProvider provider =
            testConfiguration.newDomainPersistenceProvider(connectionProvider, cacheProvider);

        private Connection begin() throws SQLException {
            var connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            transactionConnection.set(connection);
            return connection;
        }

        private void end(Connection connection) throws SQLException {
            transactionConnection.remove();
            connection.setAutoCommit(true);
            connection.close();
        }

        @Test
        void withoutACacheProviderTheCacheIsOffAndARollbackToASavepointLosesNoWrite() throws SQLException {
            var withoutCache = testConfiguration.newDomainPersistenceProvider(connectionProvider, null);
            assertThat(withoutCache.transactionCacheProvider).isInstanceOf(NoOpTransactionCacheProvider.class);
            var id = insertedAggregate(withoutCache);
            var connection = begin();
            try {
                var loadedBefore = aggregates(withoutCache).findById(id).orElseThrow();
                var savepoint = connection.setSavepoint();
                removeAndLoadAgain(withoutCache, id);
                connection.rollback(savepoint);

                removeFrom(withoutCache, loadedBefore);
                connection.commit();
            } finally {
                end(connection);
            }

            assertThat(childrenOf(withoutCache, id)).containsExactly(KEPT);
        }

        @Test
        void aRollbackToASavepointLosesNoWrite_When_TheApplicationClearsItsScope() throws SQLException {
            var id = insertedAggregate(provider);
            var connection = begin();
            try (var scope = cacheProvider.open()) {
                var loadedBefore = aggregates(provider).findById(id).orElseThrow();
                var savepoint = connection.setSavepoint();
                removeAndLoadAgain(provider, id);
                connection.rollback(savepoint);
                scope.clear();

                removeFrom(provider, loadedBefore);
                connection.commit();
            } finally {
                end(connection);
            }

            assertThat(childrenOf(provider, id)).as("removed after the rollback to the savepoint").containsExactly(KEPT);
        }

        @Test
        void aTransactionDoesNotInheritTheCacheOfOneRolledBackBefore_When_TheApplicationClosesItsScope()
            throws SQLException {
            var id = insertedAggregate(provider);
            var loadedBefore = aggregates(provider).findById(id).orElseThrow();
            var connection = begin();
            try {
                try (var scope = cacheProvider.open()) {
                    removeAndLoadAgain(provider, id);
                    connection.rollback();
                }
                try (var scope = cacheProvider.open()) {
                    removeFrom(provider, loadedBefore);
                    connection.commit();
                }
            } finally {
                end(connection);
            }

            assertThat(childrenOf(provider, id)).as("removed in the transaction after the rollback")
                .containsExactly(KEPT);
        }

        @Test
        void theCacheWorksWithinTheScopeOfATransaction() throws SQLException {
            var id = insertedRoot(provider);
            var connection = begin();
            long selectsOfTheUpdate;
            try (var scope = cacheProvider.open()) {
                var root = repository(provider).findById(id).orElseThrow();
                root.setName(WRITTEN);
                dataSource.clearPreparedStatements();
                repository(provider).update(root);
                selectsOfTheUpdate = dataSource.selectsFrom(TABLE);
                connection.commit();
            } finally {
                end(connection);
            }

            assertThat(selectsOfTheUpdate).as("SELECTs of the update").isZero();
            assertThat(nameOf(provider, id)).isEqualTo(WRITTEN);
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
        private JdbcDomainPersistenceProvider provider;

        @BeforeAll
        void startTransactionManager() {
            assumeTrue(database == TestDatabaseDialect.H2, "JTA is checked against H2 only");
            transactionManager = atomikos();
            xaDataSource = xaDataSource("dlcJdbcJta");
            dataSource = new ConnectionTrackingDataSource(schemaSet(xaDataSource));
            provider = testConfiguration.newDomainPersistenceProvider(new JdbcConnectionProvider() {
                @Override
                public Connection getConnection() {
                    return open(dataSource);
                }

                @Override
                public void releaseConnection(Connection connection) {
                    close(connection);
                }
            }, new JtaTransactionCacheProvider<>(new TransactionSynchronizationRegistryImp()));
        }

        @AfterAll
        void stopTransactionManager() {
            if (xaDataSource != null) {
                xaDataSource.close();
                transactionManager.close();
            }
        }

        @Test
        void aRolledBackTransactionLosesNoWriteOfTheOneSuspendedForIt() throws Exception {
            var id = insertedAggregate(provider);

            transactionManager.begin();
            var loadedBefore = aggregates(provider).findById(id).orElseThrow();
            var outer = transactionManager.suspend();
            transactionManager.begin();
            removeAndLoadAgain(provider, id);
            transactionManager.rollback();
            transactionManager.resume(outer);
            removeFrom(provider, loadedBefore);
            transactionManager.commit();

            assertThat(childrenOf(provider, id)).containsExactly(KEPT);
            assertThat(dataSource.openConnections()).isZero();
        }

        @Test
        void aTransactionDoesNotInheritTheCacheOfOneRolledBackBefore() throws Exception {
            var id = insertedAggregate(provider);
            var loadedBefore = aggregates(provider).findById(id).orElseThrow();

            transactionManager.begin();
            removeAndLoadAgain(provider, id);
            transactionManager.rollback();

            transactionManager.begin();
            removeFrom(provider, loadedBefore);
            transactionManager.commit();

            assertThat(childrenOf(provider, id)).as("removed in the transaction after the rollback")
                .containsExactly(KEPT);
            assertThat(dataSource.openConnections()).isZero();
        }

        @Test
        void theCacheWorksWithinATransaction() throws Exception {
            var id = insertedRoot(provider);

            transactionManager.begin();
            var root = repository(provider).findById(id).orElseThrow();
            root.setName(WRITTEN);
            dataSource.clearPreparedStatements();
            repository(provider).update(root);
            var selectsOfTheUpdate = dataSource.selectsFrom(TABLE);
            transactionManager.commit();

            assertThat(selectsOfTheUpdate).as("SELECTs of the update").isZero();
            assertThat(nameOf(provider, id)).isEqualTo(WRITTEN);
        }

        @Test
        void noCacheScopeOutlivesATransaction() throws Exception {
            var id = insertedRoot(provider);

            transactionManager.begin();
            repository(provider).findById(id).orElseThrow();
            transactionManager.commit();

            assertThat(provider.transactionCacheProvider.currentTransactionCache()).isEmpty();
        }
    }

    /**
     * Spring with JTA: Spring's JtaTransactionManager on Atomikos, wired as DlcJdbcPersistenceAutoConfiguration does.
     */
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class SpringWithJta {

        private UserTransactionManager atomikos;
        private AtomikosDataSourceBean xaDataSource;
        private ConnectionTrackingDataSource dataSource;
        private final SpringTransactionCacheProvider<JdbcRecord> cacheProvider =
            new SpringTransactionCacheProvider<>();
        private JdbcDomainPersistenceProvider provider;
        private JtaTransactionManager transactionManager;

        @BeforeAll
        void startTransactionManager() throws Exception {
            assumeTrue(database == TestDatabaseDialect.H2, "JTA is checked against H2 only");
            atomikos = atomikos();
            xaDataSource = xaDataSource("dlcJdbcSpringJta");
            dataSource = new ConnectionTrackingDataSource(schemaSet(xaDataSource));
            provider = springProvider(dataSource, cacheProvider);
            transactionManager = new JtaTransactionManager(new UserTransactionImp(), atomikos);
        }

        @AfterAll
        void stopTransactionManager() {
            if (xaDataSource != null) {
                xaDataSource.close();
                atomikos.close();
            }
        }

        private TransactionTemplate transaction(int propagation) {
            var template = new TransactionTemplate(transactionManager);
            template.setPropagationBehavior(propagation);
            return template;
        }

        @Test
        void theCacheWorksWithinATransactionAndEndsWithIt() {
            var id = insertedRoot(provider);

            var selectsOfTheUpdate = transaction(TransactionDefinition.PROPAGATION_REQUIRED).execute(status -> {
                var root = repository(provider).findById(id).orElseThrow();
                root.setName(WRITTEN);
                dataSource.clearPreparedStatements();
                repository(provider).update(root);
                return dataSource.selectsFrom(TABLE);
            });

            assertThat(selectsOfTheUpdate).as("SELECTs of the update").isZero();
            assertThat(cacheProvider.currentTransactionCache()).isEmpty();
            assertThat(dataSource.openConnections()).isZero();
            assertThat(nameOf(provider, id)).isEqualTo(WRITTEN);
        }

        @Test
        void aRolledBackRequiresNewTransactionLosesNoWriteOfTheOuterOne() {
            var id = insertedAggregate(provider);

            transaction(TransactionDefinition.PROPAGATION_REQUIRED).executeWithoutResult(outer -> {
                var loadedBefore = aggregates(provider).findById(id).orElseThrow();
                assertThatThrownBy(() -> transaction(TransactionDefinition.PROPAGATION_REQUIRES_NEW)
                    .executeWithoutResult(inner -> {
                        removeAndLoadAgain(provider, id);
                        throw new IllegalStateException("roll back");
                    })).isInstanceOf(IllegalStateException.class);
                removeFrom(provider, loadedBefore);
            });

            assertThat(childrenOf(provider, id)).containsExactly(KEPT);
            assertThat(cacheProvider.currentTransactionCache()).isEmpty();
            assertThat(dataSource.openConnections()).isZero();
        }

        @Test
        void aTransactionDoesNotInheritTheCacheOfOneRolledBackBefore() {
            var id = insertedAggregate(provider);
            var loadedBefore = aggregates(provider).findById(id).orElseThrow();

            assertThatThrownBy(() -> transaction(TransactionDefinition.PROPAGATION_REQUIRED)
                .executeWithoutResult(status -> {
                    removeAndLoadAgain(provider, id);
                    throw new IllegalStateException("roll back");
                })).isInstanceOf(IllegalStateException.class);
            transaction(TransactionDefinition.PROPAGATION_REQUIRED)
                .executeWithoutResult(status -> removeFrom(provider, loadedBefore));

            assertThat(childrenOf(provider, id)).containsExactly(KEPT);
            assertThat(dataSource.openConnections()).isZero();
        }
    }

    /**
     * Spring with a DataSourceTransactionManager, as DlcJdbcPersistenceAutoConfiguration wires it.
     */
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class SpringWithAFailingCommit {

        private final ConnectionTrackingDataSource dataSource = new ConnectionTrackingDataSource(schemaSet(database));
        private final SpringTransactionCacheProvider<JdbcRecord> cacheProvider =
            new SpringTransactionCacheProvider<>();
        private final JdbcDomainPersistenceProvider provider = springProvider(dataSource, cacheProvider);
        private final TransactionTemplate transaction =
            new TransactionTemplate(new DataSourceTransactionManager(dataSource));

        @Test
        void aWriteFailingMidwayLeavesNoStateOfTheAggregateInTheCache() {
            var id = insertedAggregate(provider);
            // loaded outside the transaction: the update loads the current state itself, which fills the cache
            var loadedBefore = aggregates(provider).findById(id).orElseThrow();
            loadedBefore.setName("Changed");
            var children = new ArrayList<>(loadedBefore.getTestEntityOneToManyList());
            children.add(child(id, "x".repeat(201)));
            loadedBefore.setTestEntityOneToManyList(children);

            transaction.executeWithoutResult(status -> {
                assertThatThrownBy(() -> aggregates(provider).update(loadedBefore)).as("the name is too long")
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
            var id = insertedAggregate(provider);
            var loadedBefore = aggregates(provider).findById(id).orElseThrow();

            assertThatThrownBy(() -> transaction.executeWithoutResult(status -> {
                removeAndLoadAgain(provider, id);
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override
                    public void beforeCommit(boolean readOnly) {
                        throw new IllegalStateException("commit fails");
                    }
                });
            })).isInstanceOf(IllegalStateException.class);

            assertThat(cacheProvider.currentTransactionCache()).isEmpty();
            assertThat(dataSource.openConnections()).isZero();
            assertThat(childrenOf(provider, id)).as("rolled back").containsExactlyInAnyOrder(KEPT, REMOVED);
            transaction.executeWithoutResult(status -> removeFrom(provider, loadedBefore));
            assertThat(childrenOf(provider, id)).containsExactly(KEPT);
        }
    }

    private JdbcDomainPersistenceProvider springProvider(DataSource dataSource,
                                                         SpringTransactionCacheProvider<JdbcRecord> cacheProvider) {
        return testConfiguration.newDomainPersistenceProvider(
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
    }

    // sequences are referenced unqualified, so every connection needs the schema set
    private DataSource schemaSet(TestDatabaseDialect database) {
        return schemaSet(database.dataSource());
    }

    private DataSource schemaSet(DataSource target) {
        return new DelegatingDataSource(target) {
            @Override
            public Connection getConnection() throws SQLException {
                Connection connection = super.getConnection();
                connection.setSchema(database.connectionSchema());
                return connection;
            }
        };
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
        xa.setURL("jdbc:h2:file:./build/h2-db/test;NON_KEYWORDS=VALUE;AUTO_SERVER=TRUE");
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

    private static Connection open(DataSource dataSource) {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void close(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
