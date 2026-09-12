package io.domainlifecycles.jdbc.configuration.def;

import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JdbcRecordPropertyProviderTest {

    private Connection connection;
    private JdbcSchemaMetadata schemaMetadata;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection(
            "jdbc:h2:mem:jdbc_record_property_provider_test_" + UUID.randomUUID());
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
                    CONSTRAINT FK_CHILD_PARENT FOREIGN KEY (PARENT_ID) REFERENCES PARENT(ID)
                )
                """);
            stmt.execute("CREATE TABLE NO_PK (NAME VARCHAR(255))");
        }
        schemaMetadata = JdbcSchemaMetadata.read(connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void providesCamelCasePropertiesWithPrimaryKeyFlag() {
        var provider = new JdbcRecordPropertyProvider(schemaMetadata);

        var properties = provider.provideProperties("PARENT");

        assertThat(properties).extracting(RecordProperty::getName)
            .containsExactlyInAnyOrder("id", "name", "concurrencyVersion");
        var idProperty = properties.stream().filter(p -> p.getName().equals("id")).findFirst().orElseThrow();
        assertThat(idProperty.isPrimaryKey()).isTrue();
        assertThat(idProperty.getRecordClassName()).isEqualTo("PARENT");
        assertThat(idProperty.getPropertyType()).isEqualTo(Long.class);
        var nameProperty = properties.stream().filter(p -> p.getName().equals("name")).findFirst().orElseThrow();
        assertThat(nameProperty.isPrimaryKey()).isFalse();
    }

    @Test
    void marksNonNullForeignKeyColumns() {
        var provider = new JdbcRecordPropertyProvider(schemaMetadata);

        var properties = provider.provideProperties("CHILD");

        var parentIdProperty = properties.stream()
            .filter(p -> p.getName().equals("parentId"))
            .findFirst()
            .orElseThrow();
        assertThat(parentIdProperty.isNonNullForeignKey()).isTrue();

        var descriptionProperty = properties.stream()
            .filter(p -> p.getName().equals("description"))
            .findFirst()
            .orElseThrow();
        assertThat(descriptionProperty.isNonNullForeignKey()).isFalse();
    }

    @Test
    void failsWhenTableHasNoPrimaryKey() {
        var provider = new JdbcRecordPropertyProvider(schemaMetadata);

        assertThatThrownBy(() -> provider.provideProperties("NO_PK"))
            .isInstanceOf(DLCPersistenceException.class);
    }
}
