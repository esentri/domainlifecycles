package io.domainlifecycles.jdbc.persistence;

import io.domainlifecycles.jdbc.persistence.containers.TestDatabaseDialect;

import java.util.Locale;

/**
 * Adjusts a hand-written table/column name literal (written in upper case, e.g. {@code "TEST_ROOT_ID"}) to
 * match the actual, dialect-dependent physical casing this test module's custom {@code JdbcRecordMapper}s
 * need, since {@link io.domainlifecycles.jdbc.records.JdbcRecord}'s column map is case-sensitive (it stores
 * physical names "exactly as reported by {@code DatabaseMetaData}", see its Javadoc) and unquoted identifiers
 * fold to upper case in H2/Oracle but to lower case in Postgres/MySQL/SQL Server.
 * <p>
 * This only matters for the handful of custom mappers under {@code persistence/mapper/**} - every other
 * mapping in this test suite is auto-mapped via {@link io.domainlifecycles.jdbc.configuration.def
 * .JdbcRecordPropertyAccessor}, which already resolves column names from {@code JdbcSchemaMetadata} rather
 * than from a literal.
 */
public final class PhysicalNames {

    private PhysicalNames() {
    }

    /**
     * Returns {@code upperCaseLiteral} unchanged for {@link TestDatabaseDialect#H2} and {@link
     * TestDatabaseDialect#ORACLE} (both fold unquoted identifiers to upper case), or lower-cased for every
     * other dialect this test suite's DDL targets (Postgres, MySQL, SQL Server - all fold unquoted
     * identifiers to lower case, or in SQL Server's case simply store them exactly as written, which this
     * module's own {@code db/migration-*} scripts always write in lower case).
     */
    public static String name(String upperCaseLiteral) {
        return switch (TestDatabaseDialect.fromSystemProperty()) {
            case H2, ORACLE -> upperCaseLiteral;
            case POSTGRES, MYSQL, SQLSERVER -> upperCaseLiteral.toLowerCase(Locale.ROOT);
        };
    }
}
