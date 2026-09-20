package io.domainlifecycles.jdbc.persistence.containers;

import org.testcontainers.containers.MSSQLServerContainer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Singleton Testcontainers-backed SQL Server database for the JDBC integration suite, started once per JVM
 * the first time {@link #start()} is called and left running for Ryuk/JVM-shutdown to reap - shared across
 * every {@code _ITest} class that runs with {@code -Ddlc.jdbc.testDialect=SQLSERVER}.
 */
final class SqlServerTestDatabase {

    static final String SCHEMA = "test_domain";

    private static final MSSQLServerContainer<?> CONTAINER =
        new MSSQLServerContainer<>("mcr.microsoft.com/mssql/server:2022-latest")
            .acceptLicense();

    private static volatile boolean migrated = false;

    private SqlServerTestDatabase() {
    }

    static synchronized DataSource start() {
        if (!CONTAINER.isRunning()) {
            CONTAINER.start();
        }
        if (!migrated) {
            ContainerMigrations.migrate(
                CONTAINER.getJdbcUrl(), CONTAINER.getUsername(), CONTAINER.getPassword(),
                SCHEMA, "db/migration-mssql");
            // mssql-jdbc's Connection.setSchema(...) does not change the *default* schema resolved for
            // unqualified identifiers within a session (unlike Oracle's ALTER SESSION SET CURRENT_SCHEMA) -
            // JdbcTestPersistenceConfiguration relies on the connection's default schema to resolve the
            // unqualified sequence names JdbcEntityIdentityProvider/JdbcValueObjectIdProvider generate, so
            // the login's default schema is set once, here, at the database level instead.
            setLoginDefaultSchema();
            migrated = true;
        }
        return new SimpleDataSource(CONTAINER.getJdbcUrl(), CONTAINER.getUsername(), CONTAINER.getPassword());
    }

    private static void setLoginDefaultSchema() {
        try (Connection connection = new SimpleDataSource(
            CONTAINER.getJdbcUrl(), CONTAINER.getUsername(), CONTAINER.getPassword()).getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("ALTER USER [" + CONTAINER.getUsername() + "] WITH DEFAULT_SCHEMA = " + SCHEMA);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to set default schema for SQL Server test user.", e);
        }
    }
}
