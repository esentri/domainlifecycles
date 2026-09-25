package io.domainlifecycles.jdbc.persistence.containers;

import org.testcontainers.containers.MySQLContainer;

import javax.sql.DataSource;

/**
 * Singleton Testcontainers-backed MySQL database for the JDBC integration suite, started once per JVM the
 * first time {@link #start()} is called and left running for Ryuk/JVM-shutdown to reap - shared across every
 * {@code _ITest} class that runs with {@code -Ddlc.jdbc.testDialect=MYSQL}.
 * <p>
 * MySQL has no notion of a "schema" separate from the database itself, so the database name selected via the
 * container's JDBC URL ({@link #SCHEMA}) already is the schema {@code JdbcTestPersistenceConfiguration} reads
 * metadata from and switches to per connection.
 */
final class MySqlTestDatabase {

    static final String SCHEMA = "test_domain";

    private static final MySQLContainer<?> CONTAINER = new MySQLContainer<>("mysql:8.0")
        .withDatabaseName(SCHEMA)
        .withUsername("dlc_test")
        .withPassword("dlc_test");

    private static volatile boolean migrated = false;

    private MySqlTestDatabase() {
    }

    static synchronized DataSource start() {
        if (!CONTAINER.isRunning()) {
            CONTAINER.start();
        }
        if (!migrated) {
            ContainerMigrations.migrate(
                CONTAINER.getJdbcUrl(), CONTAINER.getUsername(), CONTAINER.getPassword(),
                "db/migration-mysql");
            migrated = true;
        }
        return new SimpleDataSource(CONTAINER.getJdbcUrl(), CONTAINER.getUsername(), CONTAINER.getPassword());
    }
}
