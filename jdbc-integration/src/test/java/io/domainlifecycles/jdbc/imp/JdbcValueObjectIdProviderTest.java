package io.domainlifecycles.jdbc.imp;

import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilderProvider;
import io.domainlifecycles.jdbc.configuration.JdbcDomainPersistenceConfiguration;
import io.domainlifecycles.jdbc.connection.SingleJdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.H2JdbcDialect;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcValueObjectIdProviderTest {

    private Connection connection;
    private JdbcValueObjectIdProvider provider;

    @BeforeAll
    static void initDomain() {
        Domain.initialize(new ReflectiveDomainMirrorFactory("tests.shared.persistence.domain.simple"));
    }

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection(
            "jdbc:h2:mem:jdbc_value_object_id_provider_test_" + UUID.randomUUID());
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE TEST_ROOT_SIMPLE (ID BIGINT PRIMARY KEY, NAME VARCHAR(255), " +
                "CONCURRENCY_VERSION BIGINT NOT NULL)");
            stmt.execute("CREATE SEQUENCE SEQ_VO_SEQ START WITH 1");
            stmt.execute("CREATE TABLE SEQ_VO (ID BIGINT PRIMARY KEY, CONTAINER_ID BIGINT)");
            stmt.execute("CREATE TABLE UUID_VO (ID UUID PRIMARY KEY, CONTAINER_ID UUID)");
            stmt.execute("CREATE TABLE STRING_UUID_VO (ID VARCHAR(36) PRIMARY KEY, CONTAINER_ID VARCHAR(36))");
        }
        var schemaMetadata = JdbcSchemaMetadata.read(connection);
        var configuration = JdbcDomainPersistenceConfiguration.JdbcPersistenceConfigurationBuilder.newConfig()
            .withSchemaMetadata(schemaMetadata)
            .withConnectionProvider(new SingleJdbcConnectionProvider(connection))
            .withDialect(new H2JdbcDialect())
            .withDomainObjectBuilderProvider(new InnerClassDomainObjectBuilderProvider())
            .make();
        var domainPersistenceProvider = new JdbcDomainPersistenceProvider(configuration);
        provider = new JdbcValueObjectIdProvider(domainPersistenceProvider);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void generatesIncrementingSequenceIdsForLongPrimaryKey() {
        var first = new JdbcRecord("SEQ_VO");
        provider.provideNewTechIdForValueObjectRecord(first);
        var second = new JdbcRecord("SEQ_VO");
        provider.provideNewTechIdForValueObjectRecord(second);

        assertThat(first.get("ID")).isEqualTo(1L);
        assertThat(second.get("ID")).isEqualTo(2L);
    }

    @Test
    void generatesRandomUuidForNativeUuidPrimaryKey() {
        var record = new JdbcRecord("UUID_VO");

        provider.provideNewTechIdForValueObjectRecord(record);

        assertThat(record.get("ID")).isInstanceOf(UUID.class);
    }

    @Test
    void generatesRandomUuidStoredAsStringForVarcharPrimaryKey() {
        var record = new JdbcRecord("STRING_UUID_VO");

        provider.provideNewTechIdForValueObjectRecord(record);

        assertThat(record.get("ID")).isInstanceOf(String.class);
        assertThat(UUID.fromString((String) record.get("ID"))).isNotNull();
    }

    @Test
    void setsContainerIdConvertingUuidToPhysicalStringRepresentation() {
        var record = new JdbcRecord("STRING_UUID_VO");
        var containerId = UUID.randomUUID();

        provider.setContainerIdInNewVoRecord(record, containerId);

        assertThat(record.get("CONTAINER_ID")).isEqualTo(containerId.toString());
    }

    @Test
    void setsContainerIdForLongTypedColumnUnchanged() {
        var record = new JdbcRecord("SEQ_VO");

        provider.setContainerIdInNewVoRecord(record, 42L);

        assertThat(record.get("CONTAINER_ID")).isEqualTo(42L);
    }

    @Test
    void selectsExistingTechIdNormalisingStringBackToUuid() {
        var record = new JdbcRecord("STRING_UUID_VO");
        var id = UUID.randomUUID();
        record.set("ID", id.toString());

        var techId = provider.selectExistingTechIdOfValueObject(record);

        assertThat(techId).isEqualTo(id);
    }

    @Test
    void selectsExistingTechIdForLongTypedColumnUnchanged() {
        var record = new JdbcRecord("SEQ_VO");
        record.set("ID", 7L);

        var techId = provider.selectExistingTechIdOfValueObject(record);

        assertThat(techId).isEqualTo(7L);
    }
}
