package io.domainlifecycles.jdbc.imp;

import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilderProvider;
import io.domainlifecycles.jdbc.configuration.JdbcDomainPersistenceConfiguration;
import io.domainlifecycles.jdbc.connection.SingleJdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.H2JdbcDialect;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JdbcPersisterTest {

    private Connection connection;
    private JdbcPersister persister;

    @BeforeAll
    static void initDomain() {
        Domain.initialize(new ReflectiveDomainMirrorFactory("tests.shared.persistence.domain.simple"));
    }

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:mem:jdbc_persister_test_" + UUID.randomUUID());
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE TEST_ROOT_SIMPLE (
                    ID BIGINT PRIMARY KEY,
                    NAME VARCHAR(255),
                    CONCURRENCY_VERSION BIGINT NOT NULL
                )
                """);
            stmt.execute("""
                CREATE TABLE WIDGET (
                    ID BIGINT PRIMARY KEY,
                    NAME VARCHAR(255),
                    CONCURRENCY_VERSION BIGINT NOT NULL
                )
                """);
            stmt.execute("""
                CREATE TABLE GADGET (
                    ID BIGINT PRIMARY KEY,
                    NAME VARCHAR(255)
                )
                """);
        }
        var schemaMetadata = JdbcSchemaMetadata.read(connection);
        var configuration = JdbcDomainPersistenceConfiguration.JdbcPersistenceConfigurationBuilder.newConfig()
            .withSchemaMetadata(schemaMetadata)
            .withConnectionProvider(new SingleJdbcConnectionProvider(connection))
            .withDialect(new H2JdbcDialect())
            .withDomainObjectBuilderProvider(new InnerClassDomainObjectBuilderProvider())
            .make();
        var domainPersistenceProvider = new JdbcDomainPersistenceProvider(configuration);
        persister = new JdbcPersister(domainPersistenceProvider);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void insertsAllMappedColumns() throws SQLException {
        var record = new JdbcRecord("WIDGET");
        record.set("ID", 1L);
        record.set("NAME", "Alice");
        record.set("CONCURRENCY_VERSION", 0L);

        persister.doInsert(record);

        // mirrors jOOQ's recordVersionFields codegen option: the initial version on INSERT is always 1,
        // regardless of what value the record carried beforehand
        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery("SELECT NAME, CONCURRENCY_VERSION FROM WIDGET WHERE ID = 1")) {
            assertThat(rs.next()).isTrue();
            assertThat(rs.getString("NAME")).isEqualTo("Alice");
            assertThat(rs.getLong("CONCURRENCY_VERSION")).isEqualTo(1L);
        }
    }

    @Test
    void updatesRowAndBumpsConcurrencyVersionOnSuccess() throws SQLException {
        insertWidget(1L, "Alice", 0L);

        var record = new JdbcRecord("WIDGET");
        record.set("ID", 1L);
        record.set("NAME", "Alice Updated");
        record.set("CONCURRENCY_VERSION", 0L);

        persister.doUpdate(record);

        assertThat(record.get("CONCURRENCY_VERSION")).isEqualTo(1L);
        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery("SELECT NAME, CONCURRENCY_VERSION FROM WIDGET WHERE ID = 1")) {
            assertThat(rs.next()).isTrue();
            assertThat(rs.getString("NAME")).isEqualTo("Alice Updated");
            assertThat(rs.getLong("CONCURRENCY_VERSION")).isEqualTo(1L);
        }
    }

    @Test
    void updateFailsOnStaleConcurrencyVersion() throws SQLException {
        insertWidget(1L, "Alice", 5L);

        var record = new JdbcRecord("WIDGET");
        record.set("ID", 1L);
        record.set("NAME", "Alice Updated");
        record.set("CONCURRENCY_VERSION", 0L);

        assertThatThrownBy(() -> persister.doUpdate(record))
            .isInstanceOf(DLCPersistenceException.class)
            .hasMessageContaining("Optimistic locking conflict");

        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery("SELECT NAME, CONCURRENCY_VERSION FROM WIDGET WHERE ID = 1")) {
            assertThat(rs.next()).isTrue();
            assertThat(rs.getString("NAME")).isEqualTo("Alice");
            assertThat(rs.getLong("CONCURRENCY_VERSION")).isEqualTo(5L);
        }
    }

    @Test
    void updateWithoutConcurrencyColumnIsUnconditional() throws SQLException {
        try (var stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO GADGET (ID, NAME) VALUES (1, 'Old')");
        }

        var record = new JdbcRecord("GADGET");
        record.set("ID", 1L);
        record.set("NAME", "New");

        persister.doUpdate(record);

        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery("SELECT NAME FROM GADGET WHERE ID = 1")) {
            assertThat(rs.next()).isTrue();
            assertThat(rs.getString("NAME")).isEqualTo("New");
        }
    }

    @Test
    void deleteFailsOnStaleConcurrencyVersion() throws SQLException {
        insertWidget(1L, "Alice", 3L);

        var record = new JdbcRecord("WIDGET");
        record.set("ID", 1L);
        record.set("CONCURRENCY_VERSION", 0L);

        assertThatThrownBy(() -> persister.doDelete(record))
            .isInstanceOf(DLCPersistenceException.class)
            .hasMessageContaining("Optimistic locking conflict");

        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery("SELECT COUNT(*) FROM WIDGET WHERE ID = 1")) {
            rs.next();
            assertThat(rs.getInt(1)).isEqualTo(1);
        }
    }

    @Test
    void deleteRemovesRowWhenVersionMatches() throws SQLException {
        insertWidget(1L, "Alice", 0L);

        var record = new JdbcRecord("WIDGET");
        record.set("ID", 1L);
        record.set("CONCURRENCY_VERSION", 0L);

        persister.doDelete(record);

        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery("SELECT COUNT(*) FROM WIDGET WHERE ID = 1")) {
            rs.next();
            assertThat(rs.getInt(1)).isZero();
        }
    }

    @Test
    void increaseVersionBumpsVersionOfAllColumns() throws SQLException {
        insertWidget(1L, "Alice", 0L);

        var record = new JdbcRecord("WIDGET");
        record.set("ID", 1L);
        record.set("NAME", "Alice");
        record.set("CONCURRENCY_VERSION", 0L);

        persister.doIncreaseVersion(record);

        assertThat(record.get("CONCURRENCY_VERSION")).isEqualTo(1L);
        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery("SELECT CONCURRENCY_VERSION FROM WIDGET WHERE ID = 1")) {
            assertThat(rs.next()).isTrue();
            assertThat(rs.getLong("CONCURRENCY_VERSION")).isEqualTo(1L);
        }
    }

    private void insertWidget(long id, String name, long concurrencyVersion) throws SQLException {
        try (var stmt = connection.prepareStatement(
            "INSERT INTO WIDGET (ID, NAME, CONCURRENCY_VERSION) VALUES (?, ?, ?)")) {
            stmt.setLong(1, id);
            stmt.setString(2, name);
            stmt.setLong(3, concurrencyVersion);
            stmt.executeUpdate();
        }
    }
}
