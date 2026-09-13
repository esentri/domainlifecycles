package io.domainlifecycles.jdbc.imp;

import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilderProvider;
import io.domainlifecycles.jdbc.configuration.JdbcDomainPersistenceConfiguration;
import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.connection.SingleJdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.H2JdbcDialect;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tests.shared.persistence.domain.oneToMany.TestEntityOneToMany;
import tests.shared.persistence.domain.oneToMany.TestEntityOneToManyId;
import tests.shared.persistence.domain.oneToMany.TestRootOneToMany;
import tests.shared.persistence.domain.oneToMany.TestRootOneToManyId;
import tests.shared.persistence.domain.simple.TestRootSimple;
import tests.shared.persistence.domain.simple.TestRootSimpleId;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcAggregateRepositoryTest {

    private Connection connection;
    private JdbcConnectionProvider connectionProvider;
    private JdbcSchemaMetadata schemaMetadata;
    private JdbcDomainPersistenceProvider domainPersistenceProvider;
    private JdbcEntityIdentityProvider identityProvider;
    private final List<Object> publishedActions = new ArrayList<>();

    @BeforeAll
    static void initDomain() {
        Domain.initialize(new ReflectiveDomainMirrorFactory(
            "tests.shared.persistence.domain.simple", "tests.shared.persistence.domain.oneToMany"));
    }

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection(
            "jdbc:h2:mem:jdbc_aggregate_repository_test_" + UUID.randomUUID());
        connectionProvider = new SingleJdbcConnectionProvider(connection);
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE SEQUENCE TEST_ROOT_SIMPLE_ID_SEQ START WITH 1");
            stmt.execute("""
                CREATE TABLE TEST_ROOT_SIMPLE (
                    ID BIGINT PRIMARY KEY,
                    NAME VARCHAR(255),
                    CONCURRENCY_VERSION BIGINT NOT NULL
                )
                """);

            stmt.execute("CREATE SEQUENCE TEST_ROOT_ONE_TO_MANY_ID_SEQ START WITH 1");
            stmt.execute("CREATE SEQUENCE TEST_ENTITY_ONE_TO_MANY_ID_SEQ START WITH 100");
            stmt.execute("""
                CREATE TABLE TEST_ROOT_ONE_TO_MANY (
                    ID BIGINT PRIMARY KEY,
                    CONCURRENCY_VERSION BIGINT NOT NULL,
                    NAME VARCHAR(255)
                )
                """);
            stmt.execute("""
                CREATE TABLE TEST_ENTITY_ONE_TO_MANY (
                    ID BIGINT PRIMARY KEY,
                    CONCURRENCY_VERSION BIGINT NOT NULL,
                    TEST_ROOT_ID BIGINT NOT NULL,
                    NAME VARCHAR(255),
                    CONSTRAINT FK_TEOTM_ROOT FOREIGN KEY (TEST_ROOT_ID) REFERENCES TEST_ROOT_ONE_TO_MANY(ID)
                )
                """);
        }
        schemaMetadata = JdbcSchemaMetadata.read(connection);
        var configuration = JdbcDomainPersistenceConfiguration.JdbcPersistenceConfigurationBuilder.newConfig()
            .withSchemaMetadata(schemaMetadata)
            .withDomainObjectBuilderProvider(new InnerClassDomainObjectBuilderProvider())
            .make();
        domainPersistenceProvider = new JdbcDomainPersistenceProvider(configuration);
        identityProvider = new JdbcEntityIdentityProvider(connectionProvider, new H2JdbcDialect());
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void insertsFindsUpdatesAndDeletesARootOnlyAggregate() {
        var repository = new JdbcAggregateRepository<TestRootSimple, TestRootSimpleId>(
            TestRootSimple.class, connectionProvider, new H2JdbcDialect(), schemaMetadata,
            domainPersistenceProvider, publishedActions::add);

        var id = (TestRootSimpleId) identityProvider.provideFor(TestRootSimple.class.getName());
        var root = TestRootSimple.builder().setId(id).setConcurrencyVersion(0L).setName("Alice").build();

        repository.insert(root);

        var found = repository.findById(id);
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Alice");
        // mirrors jOOQ's recordVersionFields codegen option: the initial version on INSERT is always 1
        assertThat(found.get().concurrencyVersion()).isEqualTo(1L);

        found.get().setName("Alice Updated");
        repository.update(found.get());

        var updated = repository.findById(id).orElseThrow();
        assertThat(updated.getName()).isEqualTo("Alice Updated");
        assertThat(updated.concurrencyVersion()).isEqualTo(2L);

        var deleted = repository.deleteById(id);
        assertThat(deleted).isPresent();
        assertThat(repository.findById(id)).isEmpty();

        assertThat(publishedActions).isNotEmpty();
    }

    @Test
    void insertsAndFetchesAggregateWithOneToManyChildren() {
        var repository = new JdbcAggregateRepository<TestRootOneToMany, TestRootOneToManyId>(
            TestRootOneToMany.class, connectionProvider, new H2JdbcDialect(), schemaMetadata,
            domainPersistenceProvider, publishedActions::add);

        var rootId = (TestRootOneToManyId) identityProvider.provideFor(TestRootOneToMany.class.getName());
        var childId1 = (TestEntityOneToManyId) identityProvider.provideFor(TestEntityOneToMany.class.getName());
        var childId2 = (TestEntityOneToManyId) identityProvider.provideFor(TestEntityOneToMany.class.getName());

        var child1 = TestEntityOneToMany.builder()
            .setId(childId1).setConcurrencyVersion(0L).setName("Child A").setTestRootId(rootId).build();
        var child2 = TestEntityOneToMany.builder()
            .setId(childId2).setConcurrencyVersion(0L).setName("Child B").setTestRootId(rootId).build();
        var root = TestRootOneToMany.builder()
            .setId(rootId).setConcurrencyVersion(0L).setName("Root")
            .setTestEntityOneToManyList(new ArrayList<>(List.of(child1, child2)))
            .build();

        repository.insert(root);

        var found = repository.findById(rootId).orElseThrow();
        assertThat(found.getName()).isEqualTo("Root");
        assertThat(found.getTestEntityOneToManyList())
            .extracting(TestEntityOneToMany::getName)
            .containsExactlyInAnyOrder("Child A", "Child B");

        var deleted = repository.deleteById(rootId);
        assertThat(deleted).isPresent();
        assertThat(repository.findById(rootId)).isEmpty();
    }
}
