package io.domainlifecycles.jooq.persistence;

import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilderProvider;
import io.domainlifecycles.jooq.configuration.JooqDomainPersistenceConfiguration;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.jooq.persistence.mapper.complex.Test1JooqRecordMapper;
import io.domainlifecycles.jooq.persistence.mapper.complex.Test2JooqRecordMapper;
import io.domainlifecycles.jooq.persistence.mapper.complex.Test3JooqRecordMapper;
import io.domainlifecycles.jooq.persistence.mapper.complex.Test4JooqRecordMapper;
import io.domainlifecycles.jooq.persistence.mapper.complex.Test5JooqRecordMapper;
import io.domainlifecycles.jooq.persistence.mapper.complex.Test6JooqRecordMapper;
import io.domainlifecycles.jooq.persistence.mapper.complex.TestRootJooqRecordMapper;
import io.domainlifecycles.jooq.persistence.mapper.hierarchical.TestRootHierarchicalJooqRecordMapper;
import io.domainlifecycles.jooq.persistence.mapper.hierarchicalBackRef.TestRootHierarchicalBackrefJooqRecordMapper;
import io.domainlifecycles.jooq.persistence.mapper.inheritance.VehicleRecordMapper;
import io.domainlifecycles.jooq.persistence.mapper.inheritanceextended.VehicleExtendedRecordMapper;
import io.domainlifecycles.jooq.persistence.mapper.manyToManyWithJoinEntity.TestManyToManyAJooqRecordMapper;
import io.domainlifecycles.jooq.persistence.mapper.manyToManyWithJoinEntity.TestManyToManyBJooqRecordMapper;
import io.domainlifecycles.jooq.persistence.mapper.manyToManyWithJoinEntity.TestManyToManyJoinJooqRecordMapper;
import io.domainlifecycles.jooq.persistence.mapper.manyToManyWithJoinEntity.TestRootManyToManyJooqRecordMapper;
import io.domainlifecycles.jooq.persistence.mapper.oneToMany.TestOneToManyJooqRecordMapper;
import io.domainlifecycles.jooq.persistence.mapper.oneToMany.TestRootOneToManyJooqRecordMapper;
import io.domainlifecycles.jooq.persistence.mapper.oneToOneFollowingFK.TestOneToOneFollowingJooqRecordMapper;
import io.domainlifecycles.jooq.persistence.mapper.oneToOneFollowingFK.TestRootOneToOneFollowingJooqRecordMapper;
import io.domainlifecycles.jooq.persistence.mapper.oneToOneFollowingLeadingFK.TestAOneToOneFollowingLeadingJooqRecordMapper;
import io.domainlifecycles.jooq.persistence.mapper.oneToOneFollowingLeadingFK.TestBOneToOneFollowingLeadingJooqRecordMapper;
import io.domainlifecycles.jooq.persistence.mapper.oneToOneFollowingLeadingFK.TestRootOneToOneFollowingLeadingJooqRecordMapper;
import io.domainlifecycles.jooq.persistence.mapper.oneToOneLeadingFK.TestOneToOneLeadingJooqRecordMapper;
import io.domainlifecycles.jooq.persistence.mapper.oneToOneLeadingFK.TestRootOneToOneLeadingJooqRecordMapper;
import io.domainlifecycles.jooq.persistence.mapper.simple.TestRootSimpleJooqRecordMapper;
import io.domainlifecycles.jooq.persistence.mapper.simpleUuid.TestRootSimpleUuidJooqRecordMapper;
import io.domainlifecycles.jooq.persistence.mapper.valueobjects.SimpleVoOneToMany2JooqRecordMapper;
import io.domainlifecycles.jooq.persistence.mapper.valueobjects.SimpleVoOneToMany3JooqRecordMapper;
import io.domainlifecycles.jooq.persistence.mapper.valueobjects.SimpleVoOneToManyJooqRecordMapper;
import io.domainlifecycles.jooq.persistence.mapper.valueobjects.VoOneToManyEntity2JooqRecordMapper;
import io.domainlifecycles.jooq.persistence.mapper.valueobjects.VoOneToManyEntityJooqRecordMapper;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainModelFactory;
import io.domainlifecycles.persistence.mapping.RecordMapper;
import io.domainlifecycles.persistence.records.EntityValueObjectRecordClassProvider;
import io.domainlifecycles.persistence.records.EntityValueObjectRecordTypeConfiguration;
import io.domainlifecycles.test.tables.records.AktionsCodeBv3Record;
import io.domainlifecycles.test.tables.records.AktionsCodeRecord;
import io.domainlifecycles.test.tables.records.SimpleVoOneToManyRecord;
import io.domainlifecycles.test.tables.records.SimpleVoOneToMany_2Record;
import io.domainlifecycles.test.tables.records.SimpleVoOneToMany_3Record;
import io.domainlifecycles.test.tables.records.TestRootOneToOneVoDedicatedVoRecord;
import io.domainlifecycles.test.tables.records.VoAggregatePrimitiveRecordMappedComplexRecord;
import io.domainlifecycles.test.tables.records.VoAggregatePrimitiveRecordMappedNestedRecord;
import io.domainlifecycles.test.tables.records.VoAggregatePrimitiveRecordMappedSimpleRecord;
import io.domainlifecycles.test.tables.records.VoOneToManyEntityRecord;
import io.domainlifecycles.test.tables.records.VoOneToManyEntity_2Record;
import org.h2.jdbcx.JdbcDataSource;
import org.jooq.ConnectionProvider;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import tests.shared.complete.onlinehandel.bestellung.AktionsCodeBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellungBv3;
import tests.shared.persistence.domain.bestellung.bv2.AktionsCode;
import tests.shared.persistence.domain.bestellung.bv2.Bestellung;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BaseDLCTestPersistenceConfiguration {

    public final JooqDomainPersistenceProvider domainPersistenceProvider;
    public final DSLContext dslContext;
    public final DataSource dataSource;

    public final DomainObjectBuilderProvider domainObjectBuilderProvider;
    private Connection currentConnection;


    public BaseDLCTestPersistenceConfiguration() {
        dataSource = initDatasource();
        dslContext = initDslContext();
        initDomainMirror();
        domainObjectBuilderProvider = initDomainObjectBuilderProvider();
        domainPersistenceProvider = initDomainPersistenceProvider();
    }

    private DataSource initDatasource() {
        var ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:./build/h2-db/test;AUTO_SERVER=TRUE");
        ds.setUser("sa");
        ds.setPassword("");
        return ds;
    }

    private DSLContext initDslContext() {
        //connection provider
        var connectionProvider = new ConnectionProvider() {
            @Override
            public Connection acquire() throws DataAccessException {
                return currentConnection;
            }

            @Override
            public void release(Connection connection) throws DataAccessException {

            }
        };

        //jooq dls context
        var jooqConfiguration = new DefaultConfiguration();
        jooqConfiguration.settings().setExecuteWithOptimisticLocking(true);
        jooqConfiguration.setConnectionProvider(connectionProvider);
        jooqConfiguration.set(SQLDialect.H2);
        return new DefaultDSLContext(jooqConfiguration);
    }

    private void initDomainMirror() {
        Domain.initialize(new ReflectiveDomainModelFactory("tests"));
    }

    private DomainObjectBuilderProvider initDomainObjectBuilderProvider() {
        return new InnerClassDomainObjectBuilderProvider();
    }

    private JooqDomainPersistenceProvider initDomainPersistenceProvider() {
        Set<RecordMapper<?, ?, ?>> customRecordMappers = new HashSet<>();
        customRecordMappers.add(new Test1JooqRecordMapper());
        customRecordMappers.add(new Test2JooqRecordMapper());
        customRecordMappers.add(new Test3JooqRecordMapper());
        customRecordMappers.add(new Test4JooqRecordMapper());
        customRecordMappers.add(new Test5JooqRecordMapper());
        customRecordMappers.add(new Test6JooqRecordMapper());
        customRecordMappers.add(new TestRootJooqRecordMapper());
        customRecordMappers.add(new TestRootHierarchicalJooqRecordMapper());
        customRecordMappers.add(new TestRootHierarchicalBackrefJooqRecordMapper());
        customRecordMappers.add(new VehicleRecordMapper());
        customRecordMappers.add(new VehicleExtendedRecordMapper());
        customRecordMappers.add(new TestManyToManyAJooqRecordMapper());
        customRecordMappers.add(new TestManyToManyBJooqRecordMapper());
        customRecordMappers.add(new TestManyToManyJoinJooqRecordMapper());
        customRecordMappers.add(new TestRootManyToManyJooqRecordMapper());
        customRecordMappers.add(new TestOneToManyJooqRecordMapper());
        customRecordMappers.add(new TestRootOneToManyJooqRecordMapper());
        customRecordMappers.add(new TestOneToOneFollowingJooqRecordMapper());
        customRecordMappers.add(new TestRootOneToOneFollowingJooqRecordMapper());
        customRecordMappers.add(new TestAOneToOneFollowingLeadingJooqRecordMapper());
        customRecordMappers.add(new TestBOneToOneFollowingLeadingJooqRecordMapper());
        customRecordMappers.add(new TestRootOneToOneFollowingLeadingJooqRecordMapper());
        customRecordMappers.add(new TestOneToOneLeadingJooqRecordMapper());
        customRecordMappers.add(new TestRootOneToOneLeadingJooqRecordMapper());
        customRecordMappers.add(new TestRootSimpleJooqRecordMapper());
        customRecordMappers.add(new TestRootSimpleUuidJooqRecordMapper());
        customRecordMappers.add(new SimpleVoOneToMany2JooqRecordMapper());
        customRecordMappers.add(new SimpleVoOneToMany3JooqRecordMapper());
        customRecordMappers.add(new SimpleVoOneToManyJooqRecordMapper());
        customRecordMappers.add(new VoOneToManyEntity2JooqRecordMapper());
        customRecordMappers.add(new VoOneToManyEntityJooqRecordMapper());

        JooqDomainPersistenceConfiguration jooqDomainPersistenceConfiguration = JooqDomainPersistenceConfiguration
            .JooqPersistenceConfigurationBuilder
            .newConfig()
            .withDomainObjectBuilderProvider(domainObjectBuilderProvider)
            .withCustomRecordMappers(customRecordMappers)
            .withRecordPackage("io.domainlifecycles.test.tables.records")
            .withIgnoredDomainObjectFields(f -> {
                if (f.getName().equals("gesamtPreis")) return true;
                if (f.getName().equals("ignoredField")) return true;
                return false;
            })
            .withIgnoredRecordProperties(p -> {
                if (p.getName().equals("ignoredColumn")) return true;
                return false;
            })
            .withEntityValueObjectRecordClassProvider(
                new EntityValueObjectRecordClassProvider() {
                    @Override
                    public List<EntityValueObjectRecordTypeConfiguration<?>> provideContainedValueObjectRecordClassConfigurations() {
                        return Arrays.asList(new EntityValueObjectRecordTypeConfiguration<>(
                                VoAggregateRoot.class,
                                SimpleVoOneToMany.class,
                                SimpleVoOneToManyRecord.class,
                                "valueObjectsOneToMany"
                            ),
                            new EntityValueObjectRecordTypeConfiguration<>(
                                VoAggregateRoot.class,
                                SimpleVoOneToMany2.class,
                                SimpleVoOneToMany_2Record.class,
                                "valueObjectsOneToMany2"
                            ),
                            new EntityValueObjectRecordTypeConfiguration<>(
                                VoAggregateRoot.class,
                                SimpleVoOneToMany3.class,
                                SimpleVoOneToMany_3Record.class,
                                "valueObjectsOneToMany2", "oneToMany3Set"
                            ),
                            new EntityValueObjectRecordTypeConfiguration<>(
                                VoEntity.class,
                                VoOneToManyEntity.class,
                                VoOneToManyEntityRecord.class,
                                "valueObjectsOneToMany"
                            ),
                            new EntityValueObjectRecordTypeConfiguration<>(
                                VoEntity.class,
                                VoOneToManyEntity2.class,
                                VoOneToManyEntity_2Record.class,
                                "valueObjectsOneToMany", "oneToManySet"
                            ),
                            new EntityValueObjectRecordTypeConfiguration<>(
                                Bestellung.class,
                                AktionsCode.class,
                                AktionsCodeRecord.class,
                                "aktionsCodes"
                            ),
                            new EntityValueObjectRecordTypeConfiguration<>(
                                BestellungBv3.class,
                                AktionsCodeBv3.class,
                                AktionsCodeBv3Record.class,
                                "aktionsCodes"
                            ),
                            new EntityValueObjectRecordTypeConfiguration<>(
                                TestRootOneToOneVoDedicated.class,
                                VoDedicated.class,
                                TestRootOneToOneVoDedicatedVoRecord.class,
                                "vo"
                            ),
                            new EntityValueObjectRecordTypeConfiguration<>(
                                VoAggregatePrimitive.class,
                                SimpleVoPrimitive.class,
                                VoAggregatePrimitiveRecordMappedSimpleRecord.class,
                                "recordMappedSimple"
                            ),
                            new EntityValueObjectRecordTypeConfiguration<>(
                                VoAggregatePrimitive.class,
                                ComplexVoPrimitive.class,
                                VoAggregatePrimitiveRecordMappedComplexRecord.class,
                                "recordMappedComplex"
                            ),
                            new EntityValueObjectRecordTypeConfiguration<>(
                                VoAggregatePrimitive.class,
                                NestedVoPrimitive.class,
                                VoAggregatePrimitiveRecordMappedNestedRecord.class,
                                "recordMappedNested"
                            )
                        );
                    }
                }
            )
            .make();
        return new JooqDomainPersistenceProvider(jooqDomainPersistenceConfiguration);
    }

    public void startTransaction() {
        try {
            currentConnection = dataSource.getConnection();
            currentConnection.setAutoCommit(false);
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
