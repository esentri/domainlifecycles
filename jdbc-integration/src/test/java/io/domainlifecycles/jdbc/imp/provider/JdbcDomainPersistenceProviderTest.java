package io.domainlifecycles.jdbc.imp.provider;

import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilderProvider;
import io.domainlifecycles.jdbc.configuration.JdbcDomainPersistenceConfiguration;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tests.shared.persistence.domain.simple.TestRootSimple;
import tests.shared.persistence.domain.simple.TestRootSimpleId;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcDomainPersistenceProviderTest {

    private Connection connection;
    private JdbcDomainPersistenceProvider provider;

    @BeforeAll
    static void initDomain() {
        //scoped to a single, self-contained domain package: every entity/aggregate root found by the
        //provider must resolve to a table, so the scan is kept narrow on purpose rather than pulling in all
        //of test-shared-impl's much larger domain model
        Domain.initialize(new ReflectiveDomainMirrorFactory("tests.shared.persistence.domain.simple"));
    }

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection(
            "jdbc:h2:mem:jdbc_domain_persistence_provider_test_" + UUID.randomUUID());
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE TEST_ROOT_SIMPLE (
                    ID BIGINT PRIMARY KEY,
                    NAME VARCHAR(255),
                    CONCURRENCY_VERSION BIGINT NOT NULL
                )
                """);
        }
        var schemaMetadata = JdbcSchemaMetadata.read(connection);
        var configuration = JdbcDomainPersistenceConfiguration.JdbcPersistenceConfigurationBuilder.newConfig()
            .withSchemaMetadata(schemaMetadata)
            .withDomainObjectBuilderProvider(new InnerClassDomainObjectBuilderProvider())
            .make();
        provider = new JdbcDomainPersistenceProvider(configuration);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void buildsEntityRecordMirrorForSimpleAggregateRoot() {
        var mirror = provider.persistenceMirror.getEntityRecordMirror(TestRootSimple.class.getName());

        assertThat(mirror.recordTypeName()).isEqualTo("TEST_ROOT_SIMPLE");
        assertThat(mirror.domainObjectTypeName()).isEqualTo(TestRootSimple.class.getName());
        assertThat(mirror.enforcedReferences()).isEmpty();
        assertThat(mirror.valueObjectRecords()).isEmpty();
        assertThat(provider.entityRecordType(TestRootSimple.class.getName())).isEqualTo("TEST_ROOT_SIMPLE");
    }

    @Test
    @SuppressWarnings("unchecked")
    void mapsDomainObjectToRecordAndBack() {
        var mirror = provider.persistenceMirror.getEntityRecordMirror(TestRootSimple.class.getName());
        var root = TestRootSimple.builder()
            .setId(new TestRootSimpleId(42L))
            .setConcurrencyVersion(0L)
            .setName("Alice")
            .build();

        var mapper = (AbstractRecordMapper<JdbcRecord, TestRootSimple, TestRootSimple>) mirror.recordMapper();
        JdbcRecord record = mapper.from(root, root);

        assertThat(record.tableName()).isEqualTo("TEST_ROOT_SIMPLE");
        assertThat(record.get("ID")).isEqualTo(42L);
        assertThat(record.get("NAME")).isEqualTo("Alice");
        assertThat(record.get("CONCURRENCY_VERSION")).isEqualTo(0L);

        var mapped = mapper.to(record);
        assertThat(mapped.getId()).isEqualTo(new TestRootSimpleId(42L));
        assertThat(mapped.getName()).isEqualTo("Alice");
        assertThat(mapped.concurrencyVersion()).isEqualTo(0L);
    }
}
