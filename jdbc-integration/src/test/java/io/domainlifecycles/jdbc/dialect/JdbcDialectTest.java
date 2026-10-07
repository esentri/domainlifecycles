package io.domainlifecycles.jdbc.dialect;

import io.domainlifecycles.jdbc.schema.TableMetadata;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the default SQL-building methods on {@link JdbcDialect} - both the plain, unquoted defaults, and how
 * overriding just {@link JdbcDialect#quoteIdentifier(String)} flows through {@code insertSql}/{@code
 * updateSql}/{@code deleteSql} without having to override those individually.
 */
class JdbcDialectTest {

    private static final JdbcDialect PLAIN = plainDialect();
    private static final JdbcDialect QUOTING = quotingDialect();

    private static JdbcDialect plainDialect() {
        return new JdbcDialect() {
            @Override
            public String name() {
                return "PLAIN";
            }

            @Override
            public long nextSequenceValue(java.sql.Connection connection, String sequenceName) {
                throw new UnsupportedOperationException();
            }
        };
    }

    private static JdbcDialect quotingDialect() {
        return new JdbcDialect() {
            @Override
            public String name() {
                return "QUOTING";
            }

            @Override
            public long nextSequenceValue(java.sql.Connection connection, String sequenceName) {
                throw new UnsupportedOperationException();
            }

            @Override
            public String quoteIdentifier(String identifier) {
                return "\"" + identifier + "\"";
            }
        };
    }

    private static TableMetadata table(String schema, String name) {
        return new TableMetadata(schema, name, List.of(), "ID", List.of());
    }

    @Test
    void quotedTableNameDefaultsToNoQuoting() {
        assertThat(PLAIN.quotedTableName(table(null, "ORDER"))).isEqualTo("ORDER");
        assertThat(PLAIN.quotedTableName(table("APP", "ORDER"))).isEqualTo("APP.ORDER");
    }

    @Test
    void quotedTableNameQuotesSchemaAndNameSeparately() {
        assertThat(QUOTING.quotedTableName(table(null, "ORDER"))).isEqualTo("\"ORDER\"");
        assertThat(QUOTING.quotedTableName(table("APP", "ORDER"))).isEqualTo("\"APP\".\"ORDER\"");
    }

    @Test
    void insertSqlBuildsOneColumnAndOnePlaceholderPerColumnInOrder() {
        var sql = PLAIN.insertSql(table(null, "WIDGET"), List.of("ID", "NAME"));
        assertThat(sql).isEqualTo("INSERT INTO WIDGET (ID, NAME) VALUES (?, ?)");
    }

    @Test
    void insertSqlQuotesTableAndColumnsViaQuoteIdentifier() {
        var sql = QUOTING.insertSql(table(null, "WIDGET"), List.of("ID", "NAME"));
        assertThat(sql).isEqualTo("INSERT INTO \"WIDGET\" (\"ID\", \"NAME\") VALUES (?, ?)");
    }

    @Test
    void updateSqlWithoutVersionCheckOmitsConcurrencyClauses() {
        var sql = PLAIN.updateSql(table(null, "WIDGET"), List.of("NAME"), "ID", null, false);
        assertThat(sql).isEqualTo("UPDATE WIDGET SET NAME = ? WHERE ID = ?");
    }

    @Test
    void updateSqlWithVersionCheckAddsConcurrencyClausesToSetAndWhere() {
        var sql = PLAIN.updateSql(table(null, "WIDGET"), List.of("NAME"), "ID", "CONCURRENCY_VERSION", true);
        assertThat(sql).isEqualTo(
            "UPDATE WIDGET SET NAME = ?, CONCURRENCY_VERSION = ? WHERE ID = ? AND CONCURRENCY_VERSION = ?");
    }

    @Test
    void updateSqlQuotesEveryIdentifier() {
        var sql = QUOTING.updateSql(table(null, "WIDGET"), List.of("NAME"), "ID", "CONCURRENCY_VERSION", true);
        assertThat(sql).isEqualTo(
            "UPDATE \"WIDGET\" SET \"NAME\" = ?, \"CONCURRENCY_VERSION\" = ? WHERE \"ID\" = ? AND "
                + "\"CONCURRENCY_VERSION\" = ?");
    }

    @Test
    void deleteSqlWithoutVersionCheckOmitsConcurrencyClause() {
        var sql = PLAIN.deleteSql(table(null, "WIDGET"), "ID", null, false);
        assertThat(sql).isEqualTo("DELETE FROM WIDGET WHERE ID = ?");
    }

    @Test
    void deleteSqlWithVersionCheckAddsConcurrencyClause() {
        var sql = PLAIN.deleteSql(table(null, "WIDGET"), "ID", "CONCURRENCY_VERSION", true);
        assertThat(sql).isEqualTo("DELETE FROM WIDGET WHERE ID = ? AND CONCURRENCY_VERSION = ?");
    }

    @Test
    void deleteSqlQuotesEveryIdentifier() {
        var sql = QUOTING.deleteSql(table(null, "WIDGET"), "ID", "CONCURRENCY_VERSION", true);
        assertThat(sql).isEqualTo("DELETE FROM \"WIDGET\" WHERE \"ID\" = ? AND \"CONCURRENCY_VERSION\" = ?");
    }

    @Test
    void selectByColumnSqlDefaultsToNoQuoting() {
        var sql = PLAIN.selectByColumnSql(table(null, "WIDGET"), "OWNER_ID");
        assertThat(sql).isEqualTo("SELECT * FROM WIDGET WHERE OWNER_ID = ?");
    }

    @Test
    void selectByColumnSqlQuotesTableAndColumn() {
        var sql = QUOTING.selectByColumnSql(table(null, "WIDGET"), "OWNER_ID");
        assertThat(sql).isEqualTo("SELECT * FROM \"WIDGET\" WHERE \"OWNER_ID\" = ?");
    }
}
