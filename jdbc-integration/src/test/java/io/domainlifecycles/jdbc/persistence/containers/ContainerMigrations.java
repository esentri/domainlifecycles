package io.domainlifecycles.jdbc.persistence.containers;

import org.flywaydb.core.Flyway;

/**
 * Runs a dialect-specific Flyway migration programmatically against a Testcontainers-provided JDBC URL. This
 * cannot use the Gradle Flyway plugin's {@code flywayMigrate} task (as the H2 test schema does, see this
 * module's {@code build.gradle}), because that task is bound to a fixed URL at Gradle configuration time -
 * these containers only get a JDBC URL (with a dynamically assigned port) once started at test runtime.
 */
final class ContainerMigrations {

    private ContainerMigrations() {
    }

    /**
     * Migrates the schema-less/catalog-selected case (e.g. MySQL, where the "schema" is already selected by
     * the JDBC URL's database name).
     */
    static void migrate(String jdbcUrl, String user, String password, String classpathLocation) {
        migrate(jdbcUrl, user, password, null, classpathLocation);
    }

    /**
     * Migrates against the given schema, creating it first if it does not already exist.
     *
     * @param schema the schema to migrate into and to create, or {@code null} to use the connection's default
     *               schema without creating anything (Oracle: the connected user's own schema; MySQL: the
     *               database already selected via the JDBC URL)
     */
    static void migrate(String jdbcUrl, String user, String password, String schema, String classpathLocation) {
        var configBuilder = Flyway.configure()
            .dataSource(jdbcUrl, user, password)
            .locations("classpath:" + classpathLocation);
        if (schema != null) {
            configBuilder = configBuilder.schemas(schema).defaultSchema(schema).createSchemas(true);
        }
        configBuilder.load().migrate();
    }
}
