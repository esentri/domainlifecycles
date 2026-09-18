package io.domainlifecycles.jdbc.persistence;

import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilderProvider;
import io.domainlifecycles.jdbc.configuration.JdbcDomainPersistenceConfiguration;
import io.domainlifecycles.jdbc.configuration.JdbcEntityValueObjectRecordTypeConfiguration;
import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.H2JdbcDialect;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.persistence.mapper.complex.Test1JdbcRecordMapper;
import io.domainlifecycles.jdbc.persistence.mapper.hierarchical.TestRootHierarchicalJdbcRecordMapper;
import io.domainlifecycles.jdbc.persistence.mapper.hierarchicalBackRef.TestRootHierarchicalBackrefJdbcRecordMapper;
import io.domainlifecycles.jdbc.persistence.mapper.inheritance.VehicleJdbcRecordMapper;
import io.domainlifecycles.jdbc.persistence.mapper.inheritanceextended.VehicleExtendedJdbcRecordMapper;
import io.domainlifecycles.jdbc.persistence.mapper.manyToManyWithJoinEntity.TestManyToManyAJdbcRecordMapper;
import io.domainlifecycles.jdbc.persistence.mapper.manyToManyWithJoinEntity.TestManyToManyJoinJdbcRecordMapper;
import io.domainlifecycles.jdbc.persistence.mapper.oneToOneFollowingLeadingFK.TestRootOneToOneFollowingLeadingJdbcRecordMapper;
import io.domainlifecycles.jdbc.persistence.mapper.oneToOneLeadingFK.TestRootOneToOneLeadingJdbcRecordMapper;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.persistence.mapping.RecordMapper;
import org.h2.jdbcx.JdbcDataSource;
import tests.shared.complete.ecommerce.order.PromoCodeBv3;
import tests.shared.complete.ecommerce.order.OrderBv3;
import tests.shared.persistence.domain.oneToOneVoDedicatedTable.TestRootOneToOneVoDedicated;
import tests.shared.persistence.domain.oneToOneVoDedicatedTable.VoDedicated;
import tests.shared.persistence.domain.valueobjects.SimpleVoOneToMany;
import tests.shared.persistence.domain.valueobjects.SimpleVoOneToMany2;
import tests.shared.persistence.domain.valueobjects.SimpleVoOneToMany3;
import tests.shared.persistence.domain.valueobjects.VoAggregateRoot;
import tests.shared.persistence.domain.valueobjects.VoEntity;
import tests.shared.persistence.domain.valueobjects.VoOneToManyEntity;
import tests.shared.persistence.domain.valueobjects.VoOneToManyEntity2;
import tests.shared.persistence.domain.valueobjectsPrimitive.ComplexVoPrimitive;
import tests.shared.persistence.domain.valueobjectsPrimitive.NestedVoPrimitive;
import tests.shared.persistence.domain.valueobjectsPrimitive.SimpleVoPrimitive;
import tests.shared.persistence.domain.valueobjectsPrimitive.VoAggregatePrimitive;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Test-only counterpart of jooq-integration's {@code BaseDLCTestPersistenceConfiguration}: wires up the same
 * full H2 test schema and the same "tests" domain model scan, but through the plain JDBC persistence stack
 * instead of jOOQ.
 */
public class JdbcTestPersistenceConfiguration {

    public final JdbcDomainPersistenceProvider domainPersistenceProvider;
    public final JdbcConnectionProvider connectionProvider;
    public final JdbcSchemaMetadata schemaMetadata;
    public final JdbcDialect dialect;
    public final DomainObjectBuilderProvider domainObjectBuilderProvider;
    private final DataSource dataSource;
    private Connection currentConnection;

    public JdbcTestPersistenceConfiguration() {
        dataSource = initDataSource();
        initDomainMirror();
        domainObjectBuilderProvider = initDomainObjectBuilderProvider();
        dialect = new H2JdbcDialect();
        schemaMetadata = readSchemaMetadata();
        connectionProvider = () -> currentConnection;
        domainPersistenceProvider = initDomainPersistenceProvider();
    }

    private DataSource initDataSource() {
        var ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:file:./build/h2-db/test;NON_KEYWORDS=VALUE;AUTO_SERVER=TRUE");
        ds.setUser("sa");
        ds.setPassword("");
        return ds;
    }

    private void initDomainMirror() {
        Domain.initialize(new ReflectiveDomainMirrorFactory("tests"));
    }

    private DomainObjectBuilderProvider initDomainObjectBuilderProvider() {
        return new InnerClassDomainObjectBuilderProvider();
    }

    private JdbcSchemaMetadata readSchemaMetadata() {
        try (Connection connection = dataSource.getConnection()) {
            return JdbcSchemaMetadata.read(connection, "TEST_DOMAIN");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private JdbcDomainPersistenceProvider initDomainPersistenceProvider() {
        Set<RecordMapper<?, ?, ?>> customRecordMappers = new HashSet<>();
        // custom mappers are only needed where the physical schema genuinely diverges from this module's
        // naming convention (single-table inheritance discriminators, abbreviated foreign key columns, ...) -
        // unlike jooq-integration's test suite, most entities here rely on plain auto-mapping instead
        customRecordMappers.add(new Test1JdbcRecordMapper());
        customRecordMappers.add(new TestRootHierarchicalJdbcRecordMapper());
        customRecordMappers.add(new TestRootHierarchicalBackrefJdbcRecordMapper());
        customRecordMappers.add(new VehicleJdbcRecordMapper());
        customRecordMappers.add(new VehicleExtendedJdbcRecordMapper());
        customRecordMappers.add(new TestManyToManyAJdbcRecordMapper());
        customRecordMappers.add(new TestManyToManyJoinJdbcRecordMapper());
        customRecordMappers.add(new TestRootOneToOneFollowingLeadingJdbcRecordMapper());
        customRecordMappers.add(new TestRootOneToOneLeadingJdbcRecordMapper());

        var configuration = JdbcDomainPersistenceConfiguration.JdbcPersistenceConfigurationBuilder.newConfig()
            .withDomainObjectBuilderProvider(domainObjectBuilderProvider)
            .withSchemaMetadata(schemaMetadata)
            .withConnectionProvider(connectionProvider)
            .withDialect(dialect)
            .withCustomRecordMappers(customRecordMappers)
            .withIgnoredDomainObjectFields(f -> f.getName().equals("totalPrice") || f.getName().equals(
                "ignoredField"))
            .withIgnoredRecordProperties(p -> p.getName().equals("ignoredColumn"))
            .withEntityValueObjectRecordTypeConfiguration(
                new JdbcEntityValueObjectRecordTypeConfiguration(
                    VoAggregateRoot.class,
                    SimpleVoOneToMany.class,
                    "SIMPLE_VO_ONE_TO_MANY",
                    "valueObjectsOneToMany"
                ),
                new JdbcEntityValueObjectRecordTypeConfiguration(
                    VoAggregateRoot.class,
                    SimpleVoOneToMany2.class,
                    "SIMPLE_VO_ONE_TO_MANY_2",
                    "valueObjectsOneToMany2"
                ),
                new JdbcEntityValueObjectRecordTypeConfiguration(
                    VoAggregateRoot.class,
                    SimpleVoOneToMany3.class,
                    "SIMPLE_VO_ONE_TO_MANY_3",
                    "valueObjectsOneToMany2", "oneToMany3Set"
                ),
                new JdbcEntityValueObjectRecordTypeConfiguration(
                    VoEntity.class,
                    VoOneToManyEntity.class,
                    "VO_ONE_TO_MANY_ENTITY",
                    "valueObjectsOneToMany"
                ),
                new JdbcEntityValueObjectRecordTypeConfiguration(
                    VoEntity.class,
                    VoOneToManyEntity2.class,
                    "VO_ONE_TO_MANY_ENTITY_2",
                    "valueObjectsOneToMany", "oneToManySet"
                ),
                new JdbcEntityValueObjectRecordTypeConfiguration(
                    OrderBv3.class,
                    PromoCodeBv3.class,
                    "PROMO_CODE_BV3",
                    "promoCodes"
                ),
                new JdbcEntityValueObjectRecordTypeConfiguration(
                    TestRootOneToOneVoDedicated.class,
                    VoDedicated.class,
                    "TEST_ROOT_ONE_TO_ONE_VO_DEDICATED_VO",
                    "vo"
                ),
                new JdbcEntityValueObjectRecordTypeConfiguration(
                    VoAggregatePrimitive.class,
                    SimpleVoPrimitive.class,
                    "VO_AGGREGATE_PRIMITIVE_RECORD_MAPPED_SIMPLE",
                    "recordMappedSimple"
                ),
                new JdbcEntityValueObjectRecordTypeConfiguration(
                    VoAggregatePrimitive.class,
                    ComplexVoPrimitive.class,
                    "VO_AGGREGATE_PRIMITIVE_RECORD_MAPPED_COMPLEX",
                    "recordMappedComplex"
                ),
                new JdbcEntityValueObjectRecordTypeConfiguration(
                    VoAggregatePrimitive.class,
                    NestedVoPrimitive.class,
                    "VO_AGGREGATE_PRIMITIVE_RECORD_MAPPED_NESTED",
                    "recordMappedNested"
                )
            )
            .make();
        return new JdbcDomainPersistenceProvider(configuration);
    }

    public void startTransaction() {
        try {
            currentConnection = dataSource.getConnection();
            currentConnection.setAutoCommit(false);
            // sequences (unlike tables, see JdbcPersister/JdbcAggregateFetcher's use of
            // TableMetadata.qualifiedName()) are referenced unqualified by JdbcEntityIdentityProvider/
            // JdbcValueObjectIdProvider, so the connection's default schema must resolve them
            currentConnection.setSchema("TEST_DOMAIN");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void rollbackTransaction() {
        try {
            currentConnection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
