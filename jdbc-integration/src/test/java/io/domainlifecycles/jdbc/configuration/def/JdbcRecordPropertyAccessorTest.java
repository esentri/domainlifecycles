package io.domainlifecycles.jdbc.configuration.def;

import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.persistence.records.RecordProperty;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcRecordPropertyAccessorTest {

    private Connection connection;
    private JdbcRecordPropertyAccessor accessor;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection(
            "jdbc:h2:mem:jdbc_record_property_accessor_test_" + UUID.randomUUID());
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE PARENT (
                    ID BIGINT PRIMARY KEY,
                    NAME VARCHAR(255),
                    EXTERNAL_REFERENCE_CODE VARCHAR(255)
                )
                """);
        }
        JdbcSchemaMetadata schemaMetadata = JdbcSchemaMetadata.read(connection);
        accessor = new JdbcRecordPropertyAccessor(schemaMetadata);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void getsAndSetsValuesByCamelCasePropertyName() {
        var record = new JdbcRecord("PARENT");
        var idProperty = new RecordProperty("id", "PARENT", Long.class, true, false);

        accessor.setPropertyValue(idProperty, record, 42L);

        assertThat(record.get("ID")).isEqualTo(42L);
        assertThat(accessor.getPropertyValue(idProperty, record)).isEqualTo(42L);
    }

    @Test
    void resolvesMultiWordCamelCasePropertyToSnakeCaseColumn() {
        var record = new JdbcRecord("PARENT");
        var property = new RecordProperty("externalReferenceCode", "PARENT", String.class, false, false);

        accessor.setPropertyValue(property, record, "REF-1");

        assertThat(record.get("EXTERNAL_REFERENCE_CODE")).isEqualTo("REF-1");
        assertThat(accessor.getPropertyValue(property, record)).isEqualTo("REF-1");
    }
}
