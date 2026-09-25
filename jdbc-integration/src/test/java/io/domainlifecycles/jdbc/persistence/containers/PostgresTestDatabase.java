package io.domainlifecycles.jdbc.persistence.containers;

import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

/**
 * Singleton Testcontainers-backed Postgres database for the JDBC integration suite, started once per JVM the
 * first time {@link #start()} is called and left running for Ryuk/JVM-shutdown to reap - shared across every
 * {@code _ITest} class that runs with {@code -Ddlc.jdbc.testDialect=POSTGRES}.
 */
final class PostgresTestDatabase {

    static final String SCHEMA = "test_domain";

    private static final PostgreSQLContainer<?> CONTAINER = new PostgreSQLContainer<>("postgres:16-alpine")
        .withDatabaseName("dlc_test")
        .withUsername("dlc_test")
        .withPassword("dlc_test");

    private static volatile boolean migrated = false;

    private PostgresTestDatabase() {
    }

    static synchronized DataSource start() {
        if (!CONTAINER.isRunning()) {
            CONTAINER.start();
        }
        if (!migrated) {
            ContainerMigrations.migrate(
                CONTAINER.getJdbcUrl(), CONTAINER.getUsername(), CONTAINER.getPassword(),
                SCHEMA, "db/migration-postgres");
            migrated = true;
        }
        return new SimpleDataSource(CONTAINER.getJdbcUrl(), CONTAINER.getUsername(), CONTAINER.getPassword());
    }
}
