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
 * <p>
 * Unlike the other {@code *TestDatabase} classes, the suite does not run as the container's built-in {@code
 * sa} login: {@code sa} always maps to the fixed {@code dbo} user, whose default schema can never be changed,
 * so a dedicated application login/user is created instead (see {@link #createApplicationUser()}), matching
 * every other dialect's use of a non-superuser account.
 */
final class SqlServerTestDatabase {

    static final String SCHEMA = "test_domain";

    private static final String APP_USER = "dlc_test";

    private static final String APP_PASSWORD = "dlc_test";

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
            createApplicationUser();
            migrated = true;
        }
        return new SimpleDataSource(CONTAINER.getJdbcUrl(), APP_USER, APP_PASSWORD);
    }

    /**
     * Creates a dedicated login/user (as the admin {@code sa} login) whose default schema is {@link #SCHEMA},
     * so that {@code JdbcTestPersistenceConfiguration}'s connections resolve the unqualified sequence names
     * {@code JdbcEntityIdentityProvider}/{@code JdbcValueObjectIdProvider} generate without needing to be
     * schema-qualified - mssql-jdbc's {@code Connection.setSchema(...)} does not affect that resolution
     * (unlike Oracle's {@code ALTER SESSION SET CURRENT_SCHEMA}), so it has to be set at user-creation time
     * instead. {@code CHECK_POLICY = OFF} avoids the container's password-complexity policy for this fixed
     * test password.
     */
    private static void createApplicationUser() {
        try (Connection connection = new SimpleDataSource(
            CONTAINER.getJdbcUrl(), CONTAINER.getUsername(), CONTAINER.getPassword()).getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(
                "CREATE LOGIN [" + APP_USER + "] WITH PASSWORD = '" + APP_PASSWORD + "', CHECK_POLICY = OFF");
            statement.execute(
                "CREATE USER [" + APP_USER + "] FOR LOGIN [" + APP_USER + "] WITH DEFAULT_SCHEMA = " + SCHEMA);
            statement.execute("ALTER ROLE db_owner ADD MEMBER [" + APP_USER + "]");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create application user for SQL Server tests.", e);
        }
    }
}
