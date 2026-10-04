package io.domainlifecycles.jdbc.persistence;

import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilderProvider;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.jdbc.configuration.JdbcDomainPersistenceConfiguration;
import io.domainlifecycles.jdbc.configuration.JdbcEntityValueObjectRecordTypeConfiguration;
import io.domainlifecycles.jdbc.configuration.JdbcValueObjectColumnNameOverride;
import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.persistence.cache.ThreadBoundTransactionCacheProvider;
import io.domainlifecycles.persistence.cache.TransactionCacheProvider;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.imp.matcher.JdbcRecordPropertyMatcher;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.persistence.containers.TestDatabaseDialect;
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
import tests.shared.complete.ecommerce.order.PromoCodeBv3;
import tests.shared.complete.ecommerce.order.OrderBv3;
import tests.shared.persistence.domain.oneToOneVoDedicatedTable.TestRootOneToOneVoDedicated;
import tests.shared.persistence.domain.oneToOneVoDedicatedTable.VoDedicated;
import tests.shared.persistence.domain.optional.OptionalAggregate;
import tests.shared.persistence.domain.optional.OptionalEntity;
import tests.shared.persistence.domain.valueobjects.SimpleVoOneToMany;
import tests.shared.persistence.domain.valueobjects.SimpleVoOneToMany2;
import tests.shared.persistence.domain.valueobjects.SimpleVoOneToMany3;
import tests.shared.persistence.domain.valueobjects.VoAggregateRoot;
import tests.shared.persistence.domain.valueobjects.VoEntity;
import tests.shared.persistence.domain.valueobjects.VoOneToManyEntity;
import tests.shared.persistence.domain.valueobjects.VoOneToManyEntity2;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedSimpleVoOneToMany3;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedVoAggregateRoot;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedVoEntity;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedVoOneToManyEntity2;
import tests.shared.persistence.domain.valueobjectsPrimitive.ComplexVoPrimitive;
import tests.shared.persistence.domain.valueobjectsPrimitive.NestedVoPrimitive;
import tests.shared.persistence.domain.valueobjectsPrimitive.SimpleVoPrimitive;
import tests.shared.persistence.domain.valueobjectsPrimitive.VoAggregatePrimitive;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private final TestDatabaseDialect testDatabaseDialect;
    private final DataSource dataSource;
    private Connection currentConnection;

    public JdbcTestPersistenceConfiguration() {
        testDatabaseDialect = TestDatabaseDialect.fromSystemProperty();
        dataSource = testDatabaseDialect.dataSource();
        initDomainMirror();
        domainObjectBuilderProvider = initDomainObjectBuilderProvider();
        dialect = testDatabaseDialect.jdbcDialect();
        schemaMetadata = readSchemaMetadata();
        connectionProvider = () -> currentConnection;
        // a provider no scope is opened for unless a test does so itself, around what it runs as one transaction
        domainPersistenceProvider = newDomainPersistenceProvider(connectionProvider, new ThreadBoundTransactionCacheProvider<>());
    }

    private void initDomainMirror() {
        Domain.initialize(new ReflectiveDomainMirrorFactory("tests"));
    }

    private DomainObjectBuilderProvider initDomainObjectBuilderProvider() {
        return new InnerClassDomainObjectBuilderProvider();
    }

    private JdbcSchemaMetadata readSchemaMetadata() {
        try (Connection connection = dataSource.getConnection()) {
            return JdbcSchemaMetadata.read(
                connection, testDatabaseDialect.metadataCatalog(), testDatabaseDialect.metadataSchema());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Builds a provider with this configuration's mapping, but the given connection provider - e.g. to verify how
     * connections are obtained and released.
     *
     * @param connectionProvider       the connection provider
     * @param transactionCacheProvider the transaction cache provider, or {@code null} for none, which leaves the cache
     *                                 off
     * @return the provider
     */
    public JdbcDomainPersistenceProvider newDomainPersistenceProvider(
        JdbcConnectionProvider connectionProvider, TransactionCacheProvider<JdbcRecord> transactionCacheProvider) {
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

        List<JdbcEntityValueObjectRecordTypeConfiguration> voConfigs = new ArrayList<>(List.of(
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
        ));
        // Postgres (63 usable chars, silently truncated - not even a DDL error) and MySQL (64-char hard
        // limit) can't hold these two auto-mapped VO-list tables' convention-derived physical names (see
        // db/migration-postgres and db/migration-mysql), so those two dialects register explicit, short
        // physical names for them instead - H2, Oracle and SQL Server keep relying on pure naming convention.
        if (testDatabaseDialect == TestDatabaseDialect.POSTGRES || testDatabaseDialect == TestDatabaseDialect.MYSQL) {
            voConfigs.add(new JdbcEntityValueObjectRecordTypeConfiguration(
                AutoMappedVoAggregateRoot.class,
                AutoMappedSimpleVoOneToMany3.class,
                "AMVO_ROOT_O2M2_O2M3",
                "valueObjectsOneToMany2", "oneToMany3Set"
            ));
            voConfigs.add(new JdbcEntityValueObjectRecordTypeConfiguration(
                AutoMappedVoEntity.class,
                AutoMappedVoOneToManyEntity2.class,
                "AMVO_ENTITY_O2M_O2M",
                "valueObjectsOneToMany", "oneToManySet"
            ));
        }

        // The same Postgres/MySQL 64-ish-char identifier limit (see the voConfigs block above) also forces four
        // deeply-nested "optional" value-object columns to be physically shortened in db/migration-postgres
        // and db/migration-mysql - unlike a VO-list's own table, an *embedded* field has no separate physical
        // name to override via JdbcEntityValueObjectRecordTypeConfiguration, so these instead register an
        // explicit JdbcValueObjectColumnNameOverride per shortened path, consulted by a JdbcRecordPropertyMatcher
        // in place of the naming convention - see JdbcValueObjectColumnNameOverride's Javadoc.
        List<JdbcValueObjectColumnNameOverride> columnNameOverrides = new ArrayList<>();
        if (testDatabaseDialect == TestDatabaseDialect.POSTGRES || testDatabaseDialect == TestDatabaseDialect.MYSQL) {
            for (Class<? extends Entity<?>> entityType : List.of(OptionalEntity.class, OptionalAggregate.class)) {
                columnNameOverrides.add(new JdbcValueObjectColumnNameOverride(
                    entityType, "mandatory_complex_vo_mandatory_simple_vo_value",
                    "mandatoryComplexValueObject", "mandatorySimpleValueObject", "value"));
                columnNameOverrides.add(new JdbcValueObjectColumnNameOverride(
                    entityType, "optional_complex_vo_mandatory_simple_vo_value",
                    "optionalComplexValueObject", "mandatorySimpleValueObject", "value"));
                columnNameOverrides.add(new JdbcValueObjectColumnNameOverride(
                    entityType, "mandatory_complex_vo_optional_simple_vo_value",
                    "mandatoryComplexValueObject", "optionalSimpleValueObject", "value"));
                columnNameOverrides.add(new JdbcValueObjectColumnNameOverride(
                    entityType, "optional_complex_vo_optional_simple_vo_value",
                    "optionalComplexValueObject", "optionalSimpleValueObject", "value"));
            }
        }

        var configuration = JdbcDomainPersistenceConfiguration.JdbcPersistenceConfigurationBuilder.newConfig()
            .withDomainObjectBuilderProvider(domainObjectBuilderProvider)
            .withSchemaMetadata(schemaMetadata)
            .withConnectionProvider(connectionProvider)
            .withDialect(dialect)
            .withCustomRecordMappers(customRecordMappers)
            .withIgnoredDomainObjectFields(f -> f.getName().equals("totalPrice") || f.getName().equals(
                "ignoredField"))
            .withIgnoredRecordProperties(p -> p.getName().equals("ignoredColumn"))
            .withRecordEntityPropertyMatcher(new JdbcRecordPropertyMatcher(columnNameOverrides))
            .withEntityValueObjectRecordTypeConfiguration(
                voConfigs.toArray(new JdbcEntityValueObjectRecordTypeConfiguration[0]));
        if (transactionCacheProvider != null) {
            configuration.withTransactionCacheProvider(transactionCacheProvider);
        }
        return new JdbcDomainPersistenceProvider(configuration.make());
    }

    public void startTransaction() {
        try {
            currentConnection = dataSource.getConnection();
            currentConnection.setAutoCommit(false);
            // sequences (unlike tables, see JdbcPersister/JdbcAggregateFetcher's use of
            // TableMetadata.qualifiedName()) are referenced unqualified by JdbcEntityIdentityProvider/
            // JdbcValueObjectIdProvider, so the connection's default schema must resolve them
            currentConnection.setSchema(testDatabaseDialect.connectionSchema());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void rollbackTransaction() {
        try {
            currentConnection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // startTransaction() opens a fresh physical connection per test method (this configuration is
            // shared across a whole @TestInstance(PER_CLASS) test class), so it must be closed here too -
            // otherwise every method but the last in a class leaks one, held open server-side for the rest
            // of the run (observed as connection/session exhaustion against containerized databases with
            // low connection limits, e.g. Oracle).
            try {
                currentConnection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
