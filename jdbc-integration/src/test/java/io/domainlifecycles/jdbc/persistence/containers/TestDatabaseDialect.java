package io.domainlifecycles.jdbc.persistence.containers;

import io.domainlifecycles.jdbc.dialect.H2JdbcDialect;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.dialect.MySqlJdbcDialect;
import io.domainlifecycles.jdbc.dialect.OracleJdbcDialect;
import io.domainlifecycles.jdbc.dialect.PostgresJdbcDialect;
import io.domainlifecycles.jdbc.dialect.SqlServerJdbcDialect;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.util.Locale;

/**
 * Selects which database {@code JdbcTestPersistenceConfiguration} runs the JDBC integration test suite
 * against, driven by the {@code dlc.jdbc.testDialect} system property (default {@link #H2}, matching this
 * module's default, Docker-free {@code ./gradlew test} behaviour exactly as before this enum existed). Every
 * other constant lazily starts a singleton Testcontainers database the first time {@link #dataSource()} is
 * called - see the {@code *TestDatabase} classes in this package.
 */
public enum TestDatabaseDialect {

    H2 {
        @Override
        public DataSource dataSource() {
            var ds = new JdbcDataSource();
            ds.setURL("jdbc:h2:file:./build/h2-db/test;NON_KEYWORDS=VALUE;AUTO_SERVER=TRUE");
            ds.setUser("sa");
            ds.setPassword("");
            return ds;
        }

        @Override
        public JdbcDialect jdbcDialect() {
            return new H2JdbcDialect();
        }

        @Override
        public String metadataCatalog() {
            return null;
        }

        @Override
        public String metadataSchema() {
            return "TEST_DOMAIN";
        }

        @Override
        public String connectionSchema() {
            return "TEST_DOMAIN";
        }
    },

    POSTGRES {
        @Override
        public DataSource dataSource() {
            return PostgresTestDatabase.start();
        }

        @Override
        public JdbcDialect jdbcDialect() {
            return new PostgresJdbcDialect();
        }

        @Override
        public String metadataCatalog() {
            return null;
        }

        @Override
        public String metadataSchema() {
            return PostgresTestDatabase.SCHEMA;
        }

        @Override
        public String connectionSchema() {
            return PostgresTestDatabase.SCHEMA;
        }
    },

    MYSQL {
        @Override
        public DataSource dataSource() {
            return MySqlTestDatabase.start();
        }

        @Override
        public JdbcDialect jdbcDialect() {
            return new MySqlJdbcDialect();
        }

        @Override
        public String metadataCatalog() {
            // MySQL Connector/J reports the selected database as the JDBC *catalog*, not schema.
            return MySqlTestDatabase.SCHEMA;
        }

        @Override
        public String metadataSchema() {
            return null;
        }

        @Override
        public String connectionSchema() {
            return MySqlTestDatabase.SCHEMA;
        }
    },

    SQLSERVER {
        @Override
        public DataSource dataSource() {
            return SqlServerTestDatabase.start();
        }

        @Override
        public JdbcDialect jdbcDialect() {
            return new SqlServerJdbcDialect();
        }

        @Override
        public String metadataCatalog() {
            return null;
        }

        @Override
        public String metadataSchema() {
            return SqlServerTestDatabase.SCHEMA;
        }

        @Override
        public String connectionSchema() {
            return SqlServerTestDatabase.SCHEMA;
        }
    },

    ORACLE {
        @Override
        public DataSource dataSource() {
            return OracleTestDatabase.start();
        }

        @Override
        public JdbcDialect jdbcDialect() {
            return new OracleJdbcDialect();
        }

        @Override
        public String metadataCatalog() {
            return null;
        }

        @Override
        public String metadataSchema() {
            return OracleTestDatabase.SCHEMA;
        }

        @Override
        public String connectionSchema() {
            return OracleTestDatabase.SCHEMA;
        }
    };

    /**
     * Returns the {@link DataSource} to run the test suite against, starting this dialect's backing
     * Testcontainers database (and migrating it) on first call if it isn't {@link #H2}.
     */
    public abstract DataSource dataSource();

    /**
     * Returns the {@link JdbcDialect} matching this test dialect.
     */
    public abstract JdbcDialect jdbcDialect();

    /**
     * The catalog to pass to {@code JdbcSchemaMetadata.read(connection, catalog, schema)}, or {@code null} to
     * not narrow by catalog.
     */
    public abstract String metadataCatalog();

    /**
     * The schema to pass to {@code JdbcSchemaMetadata.read(connection, catalog, schema)}, or {@code null} to
     * not narrow by schema (e.g. because {@link #metadataCatalog()} already does).
     */
    public abstract String metadataSchema();

    /**
     * The schema {@code JdbcTestPersistenceConfiguration.startTransaction()} switches each connection to, so
     * that sequence names - referenced unqualified by {@code JdbcEntityIdentityProvider}/{@code
     * JdbcValueObjectIdProvider} - resolve without needing to be schema-qualified.
     */
    public abstract String connectionSchema();

    /**
     * Reads the {@code dlc.jdbc.testDialect} system property (default {@code H2}) and returns the matching
     * constant.
     */
    public static TestDatabaseDialect fromSystemProperty() {
        var value = System.getProperty("dlc.jdbc.testDialect", "H2");
        return TestDatabaseDialect.valueOf(value.toUpperCase(Locale.ROOT));
    }
}
