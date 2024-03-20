/*
 *
 *     ___
 *     │   ╲                 _
 *     │    ╲ ___ _ __  __ _(_)_ _
 *     |     ╲ _ ╲ '  ╲╱ _` │ │ ' ╲
 *     |_____╱___╱_│_│_╲__,_│_│_||_|
 *     │ │  (_)╱ _│___ __ _  _ __│ |___ ___
 *     │ │__│ │  _╱ -_) _│ ││ ╱ _│ ╱ -_|_-<
 *     │____│_│_│ ╲___╲__│╲_, ╲__│_╲___╱__╱
 *                      |__╱
 *
 *  Copyright 2019-2024 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package nitrox.dlc.jooq.persistence;

import nitrox.dlc.builder.DomainObjectBuilderProvider;
import nitrox.dlc.builder.innerclass.InnerClassDomainObjectBuilderProvider;
import nitrox.dlc.jooq.configuration.JooqDomainPersistenceConfiguration;
import nitrox.dlc.jooq.imp.provider.JooqDomainPersistenceProvider;
import nitrox.dlc.mirror.api.Domain;
import nitrox.dlc.mirror.reflect.ReflectiveDomainMirrorFactory;
import nitrox.dlc.persistence.mapping.RecordMapper;
import nitrox.dlc.persistence.records.EntityValueObjectRecordClassProvider;
import nitrox.dlc.persistence.records.EntityValueObjectRecordTypeConfiguration;
import nitrox.dlc.jooq.persistence.mapper.complex.Test1JooqRecordMapper;
import nitrox.dlc.jooq.persistence.mapper.complex.Test2JooqRecordMapper;
import nitrox.dlc.jooq.persistence.mapper.complex.Test3JooqRecordMapper;
import nitrox.dlc.jooq.persistence.mapper.complex.Test4JooqRecordMapper;
import nitrox.dlc.jooq.persistence.mapper.complex.Test5JooqRecordMapper;
import nitrox.dlc.jooq.persistence.mapper.complex.Test6JooqRecordMapper;
import nitrox.dlc.jooq.persistence.mapper.complex.TestRootJooqRecordMapper;
import nitrox.dlc.jooq.persistence.mapper.hierarchical.TestRootHierarchicalJooqRecordMapper;
import nitrox.dlc.jooq.persistence.mapper.hierarchicalBackRef.TestRootHierarchicalBackrefJooqRecordMapper;
import nitrox.dlc.jooq.persistence.mapper.inheritance.VehicleRecordMapper;
import nitrox.dlc.jooq.persistence.mapper.inheritanceextended.VehicleExtendedRecordMapper;
import nitrox.dlc.jooq.persistence.mapper.manyToManyWithJoinEntity.TestManyToManyAJooqRecordMapper;
import nitrox.dlc.jooq.persistence.mapper.manyToManyWithJoinEntity.TestManyToManyBJooqRecordMapper;
import nitrox.dlc.jooq.persistence.mapper.manyToManyWithJoinEntity.TestManyToManyJoinJooqRecordMapper;
import nitrox.dlc.jooq.persistence.mapper.manyToManyWithJoinEntity.TestRootManyToManyJooqRecordMapper;
import nitrox.dlc.jooq.persistence.mapper.oneToMany.TestOneToManyJooqRecordMapper;
import nitrox.dlc.jooq.persistence.mapper.oneToMany.TestRootOneToManyJooqRecordMapper;
import nitrox.dlc.jooq.persistence.mapper.oneToOneFollowingFK.TestOneToOneFollowingJooqRecordMapper;
import nitrox.dlc.jooq.persistence.mapper.oneToOneFollowingFK.TestRootOneToOneFollowingJooqRecordMapper;
import nitrox.dlc.jooq.persistence.mapper.oneToOneFollowingLeadingFK.TestAOneToOneFollowingLeadingJooqRecordMapper;
import nitrox.dlc.jooq.persistence.mapper.oneToOneFollowingLeadingFK.TestBOneToOneFollowingLeadingJooqRecordMapper;
import nitrox.dlc.jooq.persistence.mapper.oneToOneFollowingLeadingFK.TestRootOneToOneFollowingLeadingJooqRecordMapper;
import nitrox.dlc.jooq.persistence.mapper.oneToOneLeadingFK.TestOneToOneLeadingJooqRecordMapper;
import nitrox.dlc.jooq.persistence.mapper.oneToOneLeadingFK.TestRootOneToOneLeadingJooqRecordMapper;
import nitrox.dlc.jooq.persistence.mapper.simple.TestRootSimpleJooqRecordMapper;
import nitrox.dlc.jooq.persistence.mapper.simpleUuid.TestRootSimpleUuidJooqRecordMapper;
import nitrox.dlc.jooq.persistence.mapper.valueobjects.SimpleVoOneToMany2JooqRecordMapper;
import nitrox.dlc.jooq.persistence.mapper.valueobjects.SimpleVoOneToMany3JooqRecordMapper;
import nitrox.dlc.jooq.persistence.mapper.valueobjects.SimpleVoOneToManyJooqRecordMapper;
import nitrox.dlc.jooq.persistence.mapper.valueobjects.VoOneToManyEntity2JooqRecordMapper;
import nitrox.dlc.jooq.persistence.mapper.valueobjects.VoOneToManyEntityJooqRecordMapper;
import nitrox.dlc.test.tables.records.AktionsCodeBv3Record;
import nitrox.dlc.test.tables.records.AktionsCodeRecord;
import nitrox.dlc.test.tables.records.SimpleVoOneToManyRecord;
import nitrox.dlc.test.tables.records.SimpleVoOneToMany_2Record;
import nitrox.dlc.test.tables.records.SimpleVoOneToMany_3Record;
import nitrox.dlc.test.tables.records.TestRootOneToOneVoDedicatedVoRecord;
import nitrox.dlc.test.tables.records.VoAggregatePrimitiveRecordMappedComplexRecord;
import nitrox.dlc.test.tables.records.VoAggregatePrimitiveRecordMappedNestedRecord;
import nitrox.dlc.test.tables.records.VoAggregatePrimitiveRecordMappedSimpleRecord;
import nitrox.dlc.test.tables.records.VoOneToManyEntityRecord;
import nitrox.dlc.test.tables.records.VoOneToManyEntity_2Record;
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

public class BaseNitroxTestPersistenceConfiguration {

    public final JooqDomainPersistenceProvider domainPersistenceProvider;
    public final DSLContext dslContext;
    public final DataSource dataSource;

    public final DomainObjectBuilderProvider domainObjectBuilderProvider;
    private Connection currentConnection;


    public BaseNitroxTestPersistenceConfiguration() {
        dataSource = initDatasource();
        dslContext = initDslContext();
        initDomainMirror();
        domainObjectBuilderProvider = initDomainObjectBuilderProvider();
        domainPersistenceProvider = initDomainPersistenceProvider();
    }

    private DataSource initDatasource(){
        var ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:./build/h2-db/test;AUTO_SERVER=TRUE");
        ds.setUser("sa");
        ds.setPassword("");
        return ds;
    }

    private DSLContext initDslContext(){
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

    private void initDomainMirror(){
        Domain.initialize(new ReflectiveDomainMirrorFactory("tests"));
    }

    private DomainObjectBuilderProvider initDomainObjectBuilderProvider(){
        return new InnerClassDomainObjectBuilderProvider();
    }

    private JooqDomainPersistenceProvider initDomainPersistenceProvider(){
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
            .withRecordPackage("nitrox.dlc.test.tables.records")
            .withIgnoredDomainObjectFields(f -> {
                if (f.getName().equals("gesamtPreis")) return true;
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

    public void startTransaction(){
        try {
            currentConnection = dataSource.getConnection();
            currentConnection.setAutoCommit(false);
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void rollbackTransaction(){
        try {
            currentConnection.rollback();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
