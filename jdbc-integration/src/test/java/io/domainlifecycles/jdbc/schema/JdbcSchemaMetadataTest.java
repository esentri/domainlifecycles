package io.domainlifecycles.jdbc.schema;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcSchemaMetadataTest {

    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection(
            "jdbc:h2:mem:jdbc_schema_metadata_test_" + UUID.randomUUID());
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE PARENT (
                    ID BIGINT PRIMARY KEY,
                    NAME VARCHAR(255) NOT NULL,
                    CONCURRENCY_VERSION INT NOT NULL
                )
                """);
            stmt.execute("""
                CREATE TABLE CHILD (
                    ID BIGINT PRIMARY KEY,
                    PARENT_ID BIGINT NOT NULL,
                    DESCRIPTION VARCHAR(255),
                    EXTERNAL_REF UUID,
                    CONSTRAINT FK_CHILD_PARENT FOREIGN KEY (PARENT_ID) REFERENCES PARENT(ID)
                )
                """);
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void discoversTableNames() {
        var schema = JdbcSchemaMetadata.read(connection);

        assertThat(schema.tableNames()).contains("PARENT", "CHILD");
    }

    @Test
    void discoversSingleColumnPrimaryKey() {
        var schema = JdbcSchemaMetadata.read(connection);

        var parent = schema.table("PARENT");
        assertThat(parent.primaryKeyName()).isEqualTo("ID");
        assertThat(parent.column("ID").primaryKey()).isTrue();
        assertThat(parent.column("NAME").primaryKey()).isFalse();
    }

    @Test
    void discoversColumnNullabilityAndJavaType() {
        var schema = JdbcSchemaMetadata.read(connection);

        var parent = schema.table("PARENT");
        assertThat(parent.column("NAME").nullable()).isFalse();
        assertThat(parent.column("NAME").javaType()).isEqualTo(String.class);
        assertThat(parent.column("CONCURRENCY_VERSION").javaType()).isEqualTo(Integer.class);
    }

    @Test
    void discoversNativeUuidColumnType() {
        var schema = JdbcSchemaMetadata.read(connection);

        var child = schema.table("CHILD");
        assertThat(child.column("EXTERNAL_REF").javaType()).isEqualTo(UUID.class);
        assertThat(child.column("EXTERNAL_REF").nullable()).isTrue();
    }

    @Test
    void discoversForeignKeyAndItsNullability() {
        var schema = JdbcSchemaMetadata.read(connection);

        var child = schema.table("CHILD");
        var foreignKey = child.foreignKey("PARENT_ID").orElseThrow();
        assertThat(foreignKey.referencedTableName()).isEqualTo("PARENT");
        assertThat(foreignKey.referencedColumnName()).isEqualTo("ID");
        assertThat(child.column("PARENT_ID").nullable()).isFalse();
    }

    @Test
    void findTableIsCaseInsensitiveAndAbsentReturnsEmpty() {
        var schema = JdbcSchemaMetadata.read(connection);

        assertThat(schema.findTable("parent")).isPresent();
        assertThat(schema.findTable("does_not_exist")).isEmpty();
    }
}
