package io.domainlifecycles.jdbc.persistence.containers;

import org.testcontainers.oracle.OracleContainer;

import javax.sql.DataSource;

/**
 * Singleton Testcontainers-backed Oracle database for the JDBC integration suite, started once per JVM the
 * first time {@link #start()} is called and left running for Ryuk/JVM-shutdown to reap - shared across every
 * {@code _ITest} class that runs with {@code -Ddlc.jdbc.testDialect=ORACLE}.
 * <p>
 * Oracle has no {@code CREATE SCHEMA} in the H2/Postgres sense - a schema there is simply the objects owned
 * by a user - so the container's application user is named {@link #SCHEMA} itself (Oracle folds the
 * unquoted username to upper case, matching {@link #SCHEMA}), and the test DDL
 * ({@code db/migration-oracle}) creates every object schema-qualified with that same name, but performs no
 * separate schema-creation statement.
 */
final class OracleTestDatabase {

    static final String SCHEMA = "TEST_DOMAIN";

    private static final OracleContainer CONTAINER =
        new OracleContainer("gvenzl/oracle-free:23-slim")
            .withUsername("test_domain")
            .withPassword("dlc_test");

    private static volatile boolean migrated = false;

    private OracleTestDatabase() {
    }

    static synchronized DataSource start() {
        if (!CONTAINER.isRunning()) {
            CONTAINER.start();
        }
        if (!migrated) {
            ContainerMigrations.migrate(
                CONTAINER.getJdbcUrl(), CONTAINER.getUsername(), CONTAINER.getPassword(),
                "db/migration-oracle");
            migrated = true;
        }
        return new SimpleDataSource(CONTAINER.getJdbcUrl(), CONTAINER.getUsername(), CONTAINER.getPassword());
    }
}
