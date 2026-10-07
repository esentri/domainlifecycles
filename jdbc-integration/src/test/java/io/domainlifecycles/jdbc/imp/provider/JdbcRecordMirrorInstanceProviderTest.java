package io.domainlifecycles.jdbc.imp.provider;

import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.mapping.RecordMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class JdbcRecordMirrorInstanceProviderTest {

    private Connection connection;
    private JdbcRecordMirrorInstanceProvider provider;

    @SuppressWarnings("unchecked")
    private final RecordMapper<JdbcRecord, ?, ?> mapper = mock(RecordMapper.class);

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection(
            "jdbc:h2:mem:jdbc_record_mirror_instance_provider_test_" + UUID.randomUUID());
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE PARENT (
                    ID BIGINT PRIMARY KEY,
                    NAME VARCHAR(255)
                )
                """);
            stmt.execute("""
                CREATE TABLE CHILD (
                    ID BIGINT PRIMARY KEY,
                    PARENT_ID BIGINT NOT NULL,
                    CONSTRAINT FK_CHILD_PARENT FOREIGN KEY (PARENT_ID) REFERENCES PARENT(ID)
                )
                """);
        }
        var schemaMetadata = JdbcSchemaMetadata.read(connection);
        provider = new JdbcRecordMirrorInstanceProvider(schemaMetadata);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void computesEnforcedReferencesFromForeignKeys() {
        Map<String, List<String>> recordCanonicalNameToDomainObjectTypeMap = Map.of(
            "PARENT", List.of("some.domain.Parent"));

        var mirror = provider.provideEntityRecordMirror(
            "CHILD", "some.domain.Child", mapper, List.of(), recordCanonicalNameToDomainObjectTypeMap);

        assertThat(mirror.recordTypeName()).isEqualTo("CHILD");
        assertThat(mirror.domainObjectTypeName()).isEqualTo("some.domain.Child");
        assertThat(mirror.enforcedReferences()).containsExactly("some.domain.Parent");
    }

    @Test
    void entityWithoutForeignKeysHasNoEnforcedReferences() {
        var mirror = provider.provideEntityRecordMirror(
            "PARENT", "some.domain.Parent", mapper, List.of(), Map.of());

        assertThat(mirror.enforcedReferences()).isEmpty();
    }

    @Test
    void valueObjectMirrorRequiresAtLeastOneReference() {
        assertThatThrownBy(() -> provider.provideValueObjectRecordMirror(
            "some.domain.Parent", "some.domain.ParentVo", "PARENT", List.of("someVo"), mapper, Map.of()))
            .isInstanceOf(DLCPersistenceException.class);
    }

    @Test
    void valueObjectMirrorSucceedsWhenReferenceResolves() {
        Map<String, List<String>> recordCanonicalNameToDomainObjectTypeMap = Map.of(
            "PARENT", List.of("some.domain.Parent"));

        var vorm = provider.provideValueObjectRecordMirror(
            "some.domain.Child", "some.domain.ChildVo", "CHILD", List.of("someVo"), mapper,
            recordCanonicalNameToDomainObjectTypeMap);

        assertThat(vorm.recordTypeName()).isEqualTo("CHILD");
        assertThat(vorm.containingEntityTypeName()).isEqualTo("some.domain.Child");
        assertThat(vorm.enforcedReferences()).containsExactly("some.domain.Parent");
    }
}
