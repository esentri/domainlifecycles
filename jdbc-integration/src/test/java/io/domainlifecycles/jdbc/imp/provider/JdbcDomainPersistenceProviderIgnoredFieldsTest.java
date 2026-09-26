package io.domainlifecycles.jdbc.imp.provider;

import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilderProvider;
import io.domainlifecycles.jdbc.configuration.JdbcDomainPersistenceConfiguration;
import io.domainlifecycles.jdbc.connection.SingleJdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.H2JdbcDialect;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.mapping.IgnoredFieldProvider;
import io.domainlifecycles.persistence.mirror.api.ValueObjectRecordMirror;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tests.shared.persistence.domain.oneToManyIdentityEnum.EntityIdEnumList;
import tests.shared.persistence.domain.oneToManyIdentityEnum.RootIdEnumList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Fields excluded from auto mapping via {@code withIgnoredDomainObjectFields} (typically because a custom record
 * mapper handles them, e.g. an enum set serialized into a single column of the containing record) must not
 * require a value object table of their own - neither for List&lt;Enum&gt;/List&lt;Identity&gt; fields, nor for
 * list fields nested within an ignored value object field.
 */
class JdbcDomainPersistenceProviderIgnoredFieldsTest {

    private Connection connection;

    @BeforeAll
    static void initDomain() {
        //scoped to a single, self-contained domain package: every entity/aggregate root found by the
        //provider must resolve to a table
        Domain.initialize(new ReflectiveDomainMirrorFactory("tests.shared.persistence.domain.oneToManyIdentityEnum"));
    }

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection(
            "jdbc:h2:mem:jdbc_domain_persistence_provider_ignored_fields_test_" + UUID.randomUUID()
                //the scalar list tables' single column is named VALUE, a reserved word in H2
                + ";NON_KEYWORDS=VALUE");
        //only the tables of the aggregate root and its entity - none of the value object tables of the
        //list-valued fields
        execute("""
            CREATE TABLE ROOT_ID_ENUM_LIST (
                ID BIGINT PRIMARY KEY,
                CONCURRENCY_VERSION BIGINT NOT NULL,
                NAME VARCHAR(200)
            )
            """);
        execute("""
            CREATE TABLE ENTITY_ID_ENUM_LIST (
                ID BIGINT PRIMARY KEY,
                ROOT_ID BIGINT NOT NULL,
                CONCURRENCY_VERSION BIGINT NOT NULL,
                FOREIGN KEY (ROOT_ID) REFERENCES ROOT_ID_ENUM_LIST(ID)
            )
            """);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void ignoredScalarListAndValueObjectFieldsDoNotRequireTables() throws SQLException {
        //when
        var provider = new JdbcDomainPersistenceProvider(config(ignoring(
            "enumList", "idList", "uuidIdList", "valueWithListsList", "valueWithLists")));

        //then
        assertThat(provider.persistenceMirror.getEntityRecordMirror(RootIdEnumList.class.getName())
            .valueObjectRecords()).isEmpty();
        assertThat(provider.persistenceMirror.getEntityRecordMirror(EntityIdEnumList.class.getName())
            .valueObjectRecords()).isEmpty();
    }

    @Test
    void ignoringAValueObjectFieldAlsoSkipsTheListFieldsNestedWithinIt() throws SQLException {
        //given - tables for the scalar list fields, but none for the value object fields' nested lists
        createScalarListTable("ROOT_ID_ENUM_LIST_ENUM_LIST", "ROOT_ID_ENUM_LIST", "VARCHAR(20)");
        createScalarListTable("ROOT_ID_ENUM_LIST_ID_LIST", "ROOT_ID_ENUM_LIST", "BIGINT");
        createScalarListTable("ROOT_ID_ENUM_LIST_UUID_ID_LIST", "ROOT_ID_ENUM_LIST", "VARCHAR(36)");
        createScalarListTable("ENTITY_ID_ENUM_LIST_ENUM_LIST", "ENTITY_ID_ENUM_LIST", "VARCHAR(20)");
        createScalarListTable("ENTITY_ID_ENUM_LIST_ID_LIST", "ENTITY_ID_ENUM_LIST", "BIGINT");

        //when
        var provider = new JdbcDomainPersistenceProvider(config(ignoring("valueWithListsList", "valueWithLists")));

        //then
        assertThat(provider.persistenceMirror.getEntityRecordMirror(RootIdEnumList.class.getName())
            .valueObjectRecords())
            .extracting(ValueObjectRecordMirror::completePath)
            .containsExactlyInAnyOrder("enumList", "idList", "uuidIdList");
        assertThat(provider.persistenceMirror.getEntityRecordMirror(EntityIdEnumList.class.getName())
            .valueObjectRecords())
            .extracting(ValueObjectRecordMirror::completePath)
            .containsExactlyInAnyOrder("enumList", "idList");
    }

    @Test
    void notIgnoredScalarListFieldStillRequiresATable() throws SQLException {
        //given - enumList is not ignored, but there is no table for it
        var configuration = config(ignoring("idList", "uuidIdList", "valueWithListsList", "valueWithLists"));

        //when / then
        assertThatThrownBy(() -> new JdbcDomainPersistenceProvider(configuration))
            .isInstanceOf(DLCPersistenceException.class)
            .hasMessageContaining("No table found for composition of");
    }

    private JdbcDomainPersistenceConfiguration config(IgnoredFieldProvider ignoredFieldProvider) throws SQLException {
        return JdbcDomainPersistenceConfiguration.JdbcPersistenceConfigurationBuilder.newConfig()
            .withSchemaMetadata(JdbcSchemaMetadata.read(connection))
            .withConnectionProvider(new SingleJdbcConnectionProvider(connection))
            .withDialect(new H2JdbcDialect())
            .withDomainObjectBuilderProvider(new InnerClassDomainObjectBuilderProvider())
            .withIgnoredDomainObjectFields(ignoredFieldProvider)
            .make();
    }

    private static IgnoredFieldProvider ignoring(String... fieldNames) {
        var names = Set.of(fieldNames);
        return (FieldMirror f) -> names.contains(f.getName());
    }

    private void createScalarListTable(String tableName, String containerTableName, String valueType)
        throws SQLException {
        execute("CREATE TABLE " + tableName + " (ID BIGINT PRIMARY KEY, CONTAINER_ID BIGINT NOT NULL, VALUE "
            + valueType + ", FOREIGN KEY (CONTAINER_ID) REFERENCES " + containerTableName + "(ID))");
    }

    private void execute(String sql) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }
}
