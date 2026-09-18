package io.domainlifecycles.jdbc.persistence.tests.complex;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.internal.DomainObject;
import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.imp.JdbcPersister;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.jdbc.util.JdbcRecordMapper;
import io.domainlifecycles.persistence.fetcher.AggregateFetcher;
import io.domainlifecycles.persistence.fetcher.FetcherResult;
import io.domainlifecycles.persistence.fetcher.RecordProvider;
import io.domainlifecycles.persistence.fetcher.simple.SimpleAggregateFetcher;
import io.domainlifecycles.persistence.fetcher.simple.SimpleFetcherContext;
import io.domainlifecycles.persistence.mapping.RecordMapper;
import io.domainlifecycles.persistence.repository.PersistenceActionPublishingRepository;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.complex.TestEntity1;
import tests.shared.persistence.domain.complex.TestEntity2;
import tests.shared.persistence.domain.complex.TestEntity3;
import tests.shared.persistence.domain.complex.TestEntity4;
import tests.shared.persistence.domain.complex.TestEntity5;
import tests.shared.persistence.domain.complex.TestEntity6;
import tests.shared.persistence.domain.complex.TestRoot;
import tests.shared.persistence.domain.complex.TestRootId;

import java.util.List;
import java.util.Optional;

/**
 * {@code TestEntity1} has two foreign keys to {@code TestEntity2} (see {@code Test1JdbcRecordMapper}) - the
 * standard {@link io.domainlifecycles.jdbc.imp.JdbcAggregateFetcher} rejects this as an ambiguous foreign key
 * relation (proven separately in {@code FetcherTest.testFetcherComplexExpectedException}), so - mirroring
 * jooq-integration's own {@code ComplexAggregateRootRepository} - this repository walks the whole
 * TestEntity1..6 tree itself via plain JDBC, then delegates only the persist side (insert/update/delete) to the
 * inherited {@link JdbcPersister}.
 */
public class ComplexAggregateRootRepository
    extends PersistenceActionPublishingRepository<TestRootId, TestRoot, JdbcRecord> {

    private final JdbcConnectionProvider connectionProvider;
    private final JdbcSchemaMetadata schemaMetadata;
    private final JdbcDomainPersistenceProvider domainPersistenceProvider;
    private final SimpleAggregateFetcher<Long, TestRoot, TestRootId, JdbcRecord> simpleAggregateFetcher;

    public ComplexAggregateRootRepository(JdbcDomainPersistenceProvider domainPersistenceProvider,
                                           PersistenceEventPublisher persistenceEventPublisher) {
        super(
            new JdbcPersister(domainPersistenceProvider),
            domainPersistenceProvider,
            persistenceEventPublisher
        );
        this.connectionProvider = domainPersistenceProvider.connectionProvider;
        this.schemaMetadata = domainPersistenceProvider.schemaMetadata;
        this.domainPersistenceProvider = domainPersistenceProvider;
        this.simpleAggregateFetcher = provideFetcher();
    }

    private SimpleAggregateFetcher<Long, TestRoot, TestRootId, JdbcRecord> provideFetcher() {
        return new SimpleAggregateFetcher<>() {
            @Override
            public AggregateFetcher<TestRoot, TestRootId, JdbcRecord> withRecordProvider(
                RecordProvider<? extends JdbcRecord, ? extends JdbcRecord> recordProvider,
                Class<? extends Entity<?>> containingEntityClass,
                Class<? extends DomainObject> propertyClass,
                List<String> propertyPath) {
                throw new IllegalStateException("Not implemented!");
            }

            @Override
            public TestRoot fetchBasicByIdValue(Long aLong, SimpleFetcherContext<JdbcRecord> fetcherContext) {
                return findByIdCustom(aLong);
            }

            @Override
            public TestRoot fetchBasicByRecord(JdbcRecord aggregateRecord,
                                                SimpleFetcherContext<JdbcRecord> fetcherContext) {
                throw new IllegalStateException("Not implemented!");
            }
        };
    }

    @SuppressWarnings("unchecked")
    private TestRoot findByIdCustom(Long id) {
        JdbcRecord testRootRecord = selectOne("TEST_ROOT", "ID", id);
        if (testRootRecord == null) {
            return null;
        }
        TestEntity1 te1 = null;
        JdbcRecord testEntity1Record = selectOne("TEST_ENTITY_1", "TEST_ROOT_ID", id);
        if (testEntity1Record != null) {
            TestEntity2 testEntity2A = fetchSubTreeEntity2((Long) testEntity1Record.get("TEST_ENTITY_2_ID_A"));
            TestEntity2 testEntity2B = fetchSubTreeEntity2((Long) testEntity1Record.get("TEST_ENTITY_2_ID_B"));
            RecordMapper<JdbcRecord, TestEntity1, TestRoot> mapper1 =
                (RecordMapper<JdbcRecord, TestEntity1, TestRoot>) domainPersistenceProvider
                    .persistenceMirror
                    .getEntityRecordMapper(TestEntity1.class.getName());
            DomainObjectBuilder<TestEntity1> b = mapper1.recordToDomainObjectBuilder(testEntity1Record);
            b.setFieldValue(testEntity2A, "testEntity2A");
            b.setFieldValue(testEntity2B, "testEntity2B");
            te1 = b.build();
        }
        RecordMapper<JdbcRecord, TestRoot, TestRoot> mapper =
            (RecordMapper<JdbcRecord, TestRoot, TestRoot>) domainPersistenceProvider
                .persistenceMirror
                .getEntityRecordMapper(TestRoot.class.getName());
        DomainObjectBuilder<TestRoot> b2 = mapper.recordToDomainObjectBuilder(testRootRecord);
        b2.setFieldValue(te1, "testEntity1");
        return b2.build();
    }

    @SuppressWarnings("unchecked")
    private TestEntity2 fetchSubTreeEntity2(Long testEntity2Id) {
        if (testEntity2Id == null) {
            return null;
        }
        JdbcRecord testEntity2Record = selectOne("TEST_ENTITY_2", "ID", testEntity2Id);
        if (testEntity2Record == null) {
            return null;
        }
        RecordMapper<JdbcRecord, TestEntity2, TestRoot> mapper =
            (RecordMapper<JdbcRecord, TestEntity2, TestRoot>) domainPersistenceProvider
                .persistenceMirror
                .getEntityRecordMapper(TestEntity2.class.getName());
        DomainObjectBuilder<TestEntity2> b = mapper.recordToDomainObjectBuilder(testEntity2Record);

        List<JdbcRecord> testEntity3List = selectMany("TEST_ENTITY_3", "TEST_ENTITY_2_ID", testEntity2Id);
        for (JdbcRecord te3 : testEntity3List) {
            TestEntity3 te3Entity = fetchSubTreeEntity3(te3);
            b.addValueToCollection(te3Entity, "testEntity3List");
        }
        return b.build();
    }

    @SuppressWarnings("unchecked")
    private TestEntity3 fetchSubTreeEntity3(JdbcRecord te3) {
        List<JdbcRecord> testEntity4List = selectMany("TEST_ENTITY_4", "TEST_ENTITY_3_ID", (Long) te3.get("ID"));
        RecordMapper<JdbcRecord, TestEntity3, TestRoot> mapper =
            (RecordMapper<JdbcRecord, TestEntity3, TestRoot>) domainPersistenceProvider
                .persistenceMirror
                .getEntityRecordMapper(TestEntity3.class.getName());
        DomainObjectBuilder<TestEntity3> b = mapper.recordToDomainObjectBuilder(te3);
        for (JdbcRecord te4 : testEntity4List) {
            TestEntity4 te4Entity = fetchSubTreeEntity4(te4);
            b.addValueToCollection(te4Entity, "testEntity4List");
        }
        return b.build();
    }

    @SuppressWarnings("unchecked")
    private TestEntity4 fetchSubTreeEntity4(JdbcRecord te4) {
        List<JdbcRecord> testEntity5List = selectMany("TEST_ENTITY_5", "TEST_ENTITY_4_ID", (Long) te4.get("ID"));
        RecordMapper<JdbcRecord, TestEntity4, TestRoot> mapper =
            (RecordMapper<JdbcRecord, TestEntity4, TestRoot>) domainPersistenceProvider
                .persistenceMirror
                .getEntityRecordMapper(TestEntity4.class.getName());
        DomainObjectBuilder<TestEntity4> b = mapper.recordToDomainObjectBuilder(te4);
        for (JdbcRecord te5 : testEntity5List) {
            TestEntity5 te5Entity = fetchSubTreeEntity5(te5);
            b.addValueToCollection(te5Entity, "testEntity5List");
        }
        return b.build();
    }

    @SuppressWarnings("unchecked")
    private TestEntity5 fetchSubTreeEntity5(JdbcRecord te5) {
        RecordMapper<JdbcRecord, TestEntity5, TestRoot> mapper5 =
            (RecordMapper<JdbcRecord, TestEntity5, TestRoot>) domainPersistenceProvider
                .persistenceMirror
                .getEntityRecordMapper(TestEntity5.class.getName());
        DomainObjectBuilder<TestEntity5> b = mapper5.recordToDomainObjectBuilder(te5);
        JdbcRecord testEntity6Record = selectOne("TEST_ENTITY_6", "ID", (Long) te5.get("TEST_ENTITY_6_ID"));
        if (testEntity6Record != null) {
            RecordMapper<JdbcRecord, TestEntity6, TestRoot> mapper6 =
                (RecordMapper<JdbcRecord, TestEntity6, TestRoot>) domainPersistenceProvider
                    .persistenceMirror
                    .getEntityRecordMapper(TestEntity6.class.getName());
            b.setFieldValue(mapper6.recordToDomainObjectBuilder(testEntity6Record).build(), "testEntity6");
        }
        return b.build();
    }

    private JdbcRecord selectOne(String tableName, String columnName, Object value) {
        if (value == null) {
            return null;
        }
        return JdbcRecordMapper.selectOneByColumn(connectionProvider, schemaMetadata.table(tableName), columnName, value);
    }

    private List<JdbcRecord> selectMany(String tableName, String columnName, Object value) {
        if (value == null) {
            return List.of();
        }
        return JdbcRecordMapper.selectByColumn(connectionProvider, schemaMetadata.table(tableName), columnName, value);
    }

    @Override
    public FetcherResult<TestRoot, JdbcRecord> findResultById(TestRootId rootId) {
        return simpleAggregateFetcher.fetchDeep(rootId);
    }
}
