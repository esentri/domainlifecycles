package io.domainlifecycles.jooq.persistence.tests.fetcher;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.jooq.imp.JooqAggregateFetcher;
import io.domainlifecycles.jooq.persistence.BasePersistence_ITest;
import io.domainlifecycles.jooq.persistence.tests.complex.ComplexAggregateRootRepository;
import io.domainlifecycles.jooq.persistence.tests.hierarchical.HierarchicalAggregateRootRepository;
import io.domainlifecycles.jooq.persistence.tests.hierarchicalBackRef.HierarchicalAggregateRootBackrefRepository;
import io.domainlifecycles.jooq.persistence.tests.manyToManyWithJoinEntity.ManyToManyAggregateRootRepository;
import io.domainlifecycles.jooq.persistence.tests.oneToMany.OneToManyAggregateRootRepository;
import io.domainlifecycles.jooq.persistence.tests.oneToOneFollowingFK.OneToOneFollowingAggregateRootRepository;
import io.domainlifecycles.jooq.persistence.tests.oneToOneFollowingLeadingFK.OneToOneFollowingLeadingAggregateRootRepository;
import io.domainlifecycles.jooq.persistence.tests.oneToOneLeadingFK.OneToOneLeadingAggregateRootRepository;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.fetcher.RecordProvider;
import io.domainlifecycles.persistence.mapping.RecordMapper;
import io.domainlifecycles.test.Tables;
import io.domainlifecycles.test.tables.records.TestEntityOneToManyRecord;
import io.domainlifecycles.test.tables.records.TestEntityOneToOneFollowingRecord;
import io.domainlifecycles.test.tables.records.TestEntity_1Record;
import io.domainlifecycles.test.tables.records.TestEntity_2Record;
import io.domainlifecycles.test.tables.records.TestRootHierarchicalBackrefRecord;
import io.domainlifecycles.test.tables.records.TestRootHierarchicalRecord;
import io.domainlifecycles.test.tables.records.TestRootOneToManyRecord;
import io.domainlifecycles.test.tables.records.TestRootOneToOneFollowingRecord;
import io.domainlifecycles.test.tables.records.TestRootOneToOneLeadingRecord;
import org.assertj.core.api.Assertions;
import org.jooq.UpdatableRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tests.shared.TestDataGenerator;
import tests.shared.persistence.domain.complex.TestEntity1;
import tests.shared.persistence.domain.complex.TestEntity2;
import tests.shared.persistence.domain.complex.TestRoot;
import tests.shared.persistence.domain.complex.TestRootId;
import tests.shared.persistence.domain.hierarchical.TestRootHierarchical;
import tests.shared.persistence.domain.hierarchical.TestRootHierarchicalId;
import tests.shared.persistence.domain.hierarchicalBackRef.TestRootHierarchicalBackref;
import tests.shared.persistence.domain.hierarchicalBackRef.TestRootHierarchicalBackrefId;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestRootManyToMany;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestRootManyToManyId;
import tests.shared.persistence.domain.oneToMany.TestEntityOneToMany;
import tests.shared.persistence.domain.oneToMany.TestRootOneToMany;
import tests.shared.persistence.domain.oneToMany.TestRootOneToManyId;
import tests.shared.persistence.domain.oneToOneFollowingFK.TestEntityOneToOneFollowing;
import tests.shared.persistence.domain.oneToOneFollowingFK.TestRootOneToOneFollowing;
import tests.shared.persistence.domain.oneToOneFollowingFK.TestRootOneToOneFollowingId;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestRootOneToOneFollowingLeading;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestRootOneToOneFollowingLeadingId;
import tests.shared.persistence.domain.oneToOneLeadingFK.TestEntityOneToOneLeading;
import tests.shared.persistence.domain.oneToOneLeadingFK.TestRootOneToOneLeading;
import tests.shared.persistence.domain.oneToOneLeadingFK.TestRootOneToOneLeadingId;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FetcherTest extends BasePersistence_ITest {

    private static OneToOneFollowingAggregateRootRepository oneToOneFollowingAggregateRootRepository;

    private static OneToOneLeadingAggregateRootRepository oneToOneLeadingAggregateRootRepository;

    private static OneToManyAggregateRootRepository oneToManyAggregateRootRepository;

    private static OneToOneFollowingLeadingAggregateRootRepository oneToOneFollowingLeadingAggregateRootRepository;

    private static ComplexAggregateRootRepository complexAggregateRootRepository;

    private static HierarchicalAggregateRootRepository hierarchicalAggregateRootRepository;

    private static HierarchicalAggregateRootBackrefRepository hierarchicalAggregateRootBackrefRepository;

    private static ManyToManyAggregateRootRepository manyToManyAggregateRootRepository;

    @BeforeAll
    public void init() {
        manyToManyAggregateRootRepository = new ManyToManyAggregateRootRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider);
        hierarchicalAggregateRootBackrefRepository = new HierarchicalAggregateRootBackrefRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider
        );
        hierarchicalAggregateRootRepository = new HierarchicalAggregateRootRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider
        );
        complexAggregateRootRepository = new ComplexAggregateRootRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider
        );
        oneToOneFollowingLeadingAggregateRootRepository = new OneToOneFollowingLeadingAggregateRootRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider
        );
        oneToManyAggregateRootRepository = new OneToManyAggregateRootRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider
        );
        oneToOneLeadingAggregateRootRepository = new OneToOneLeadingAggregateRootRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider
        );
        oneToOneFollowingAggregateRootRepository = new OneToOneFollowingAggregateRootRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider
        );


    }

    @Test
    public void testFetcherOneToOneFollowingCompleteByRootRecord() {
        JooqAggregateFetcher<TestRootOneToOneFollowing, TestRootOneToOneFollowingId> jooqEntityFetcher =
            new JooqAggregateFetcher<>(TestRootOneToOneFollowing.class, persistenceConfiguration.dslContext,
                persistenceConfiguration.domainPersistenceProvider);
        List<TestRootOneToOneFollowing> inserted = TestDataGenerator.buildManyOneToOneFollowingComplete().stream().map(
            r -> oneToOneFollowingAggregateRootRepository.insert(r)
        ).collect(Collectors.toList());

        List<TestRootOneToOneFollowing> result = persistenceConfiguration.dslContext.select()
            .from(Tables.TEST_ROOT_ONE_TO_ONE_FOLLOWING)
            .where(Tables.TEST_ROOT_ONE_TO_ONE_FOLLOWING.NAME.like("%Root%"))
            .orderBy(Tables.TEST_ROOT_ONE_TO_ONE_FOLLOWING.ID)
            .fetch().stream()
            .map(r -> jooqEntityFetcher.fetchDeep((UpdatableRecord<?>) r).resultValue().get()).collect(
                Collectors.toList());

        for (int i = 0; i < inserted.size(); i++) {
            assertInsertedWithResult(inserted.get(i), result.get(i));
        }
    }

    @Test
    public void testFetcherOneToOneFollowingEmpty() {
        JooqAggregateFetcher<TestRootOneToOneFollowing, TestRootOneToOneFollowingId> jooqEntityFetcher =
            new JooqAggregateFetcher<>(TestRootOneToOneFollowing.class, persistenceConfiguration.dslContext,
                persistenceConfiguration.domainPersistenceProvider);

        Optional<TestRootOneToOneFollowing> result = jooqEntityFetcher.fetchDeep(
            new TestRootOneToOneFollowingId(1L)).resultValue();
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void testMitigateOneToNQueryProblem() {
        List<TestRootOneToOneFollowing> inserted = TestDataGenerator.buildManyOneToOneFollowingComplete().stream().map(
            r -> oneToOneFollowingAggregateRootRepository.insert(r)
        ).collect(Collectors.toList());

        RecordMapper<TestRootOneToOneFollowingRecord, TestRootOneToOneFollowing, TestRootOneToOneFollowing> rmRoot =
            (RecordMapper<TestRootOneToOneFollowingRecord, TestRootOneToOneFollowing, TestRootOneToOneFollowing>)
                persistenceConfiguration.domainPersistenceProvider
                    .persistenceMirror
                    .getEntityRecordMapper(TestRootOneToOneFollowing.class.getName());
        RecordMapper<TestEntityOneToOneFollowingRecord, TestEntityOneToOneFollowing, TestRootOneToOneFollowing> rmEntity =
            (RecordMapper<TestEntityOneToOneFollowingRecord, TestEntityOneToOneFollowing, TestRootOneToOneFollowing>)
                persistenceConfiguration.domainPersistenceProvider
                    .persistenceMirror
                    .getEntityRecordMapper(TestEntityOneToOneFollowing.class.getName());

        List<TestRootOneToOneFollowing> result = persistenceConfiguration.dslContext.select()
            .from(Tables.TEST_ROOT_ONE_TO_ONE_FOLLOWING)
            .join(Tables.TEST_ENTITY_ONE_TO_ONE_FOLLOWING)
            .on(Tables.TEST_ROOT_ONE_TO_ONE_FOLLOWING.ID.eq(Tables.TEST_ENTITY_ONE_TO_ONE_FOLLOWING.TEST_ROOT_ID))
            .where(Tables.TEST_ROOT_ONE_TO_ONE_FOLLOWING.NAME.like("%Root%"))
            .orderBy(Tables.TEST_ROOT_ONE_TO_ONE_FOLLOWING.ID)
            .fetch().stream()
            .map(r -> {
                TestRootOneToOneFollowingRecord recRoot = r.into(Tables.TEST_ROOT_ONE_TO_ONE_FOLLOWING);
                TestEntityOneToOneFollowingRecord recEntity = r.into(Tables.TEST_ENTITY_ONE_TO_ONE_FOLLOWING);
                DomainObjectBuilder<TestRootOneToOneFollowing> rootDomainObjectBuilder =
                    rmRoot.recordToDomainObjectBuilder(
                        recRoot);
                TestEntityOneToOneFollowing entity = rmEntity.recordToDomainObjectBuilder(recEntity).build();
                rootDomainObjectBuilder.setFieldValue(entity, "testEntityOneToOneFollowing");
                return rootDomainObjectBuilder.build();
            })
            .collect(Collectors.toList());

        for (int i = 0; i < inserted.size(); i++) {
            assertInsertedWithResult(inserted.get(i), result.get(i));
        }
    }

    @Test
    public void testFetcherOneToOneFollowingComplete() {
        JooqAggregateFetcher<TestRootOneToOneFollowing, TestRootOneToOneFollowingId> jooqEntityFetcher =
            new JooqAggregateFetcher<>(TestRootOneToOneFollowing.class, persistenceConfiguration.dslContext,
                persistenceConfiguration.domainPersistenceProvider);

        TestRootOneToOneFollowing inserted = oneToOneFollowingAggregateRootRepository.insert(
            TestDataGenerator.buildOneToOneFollowingComplete());

        Optional<TestRootOneToOneFollowing> result = jooqEntityFetcher.fetchDeep(
            new TestRootOneToOneFollowingId(1l)).resultValue();
        Assertions.assertThat(result).isPresent();
        assertInsertedWithResult(inserted, result.get());
    }

    @Test
    public void testFetcherOneToOneFollowingOnlyRoot() {
        JooqAggregateFetcher<TestRootOneToOneFollowing, TestRootOneToOneFollowingId> jooqEntityFetcher =
            new JooqAggregateFetcher<>(TestRootOneToOneFollowing.class, persistenceConfiguration.dslContext,
                persistenceConfiguration.domainPersistenceProvider);
        TestRootOneToOneFollowing inserted = oneToOneFollowingAggregateRootRepository.insert(
            TestDataGenerator.buildOneToOneFollowingOnlyRoot());
        Optional<TestRootOneToOneFollowing> result = jooqEntityFetcher.fetchDeep(
            new TestRootOneToOneFollowingId(1l)).resultValue();
        Assertions.assertThat(result).isPresent();
        assertInsertedWithResult(inserted, result.get());
    }

    @Test
    public void testFetcherOneToOneLeadingComplete() {
        JooqAggregateFetcher<TestRootOneToOneLeading, TestRootOneToOneLeadingId> jooqEntityFetcher =
            new JooqAggregateFetcher<>(TestRootOneToOneLeading.class, persistenceConfiguration.dslContext,
                persistenceConfiguration.domainPersistenceProvider);
        TestRootOneToOneLeading inserted = oneToOneLeadingAggregateRootRepository.insert(
            TestDataGenerator.buildOneToOneLeadingComplete());
        Optional<TestRootOneToOneLeading> result = jooqEntityFetcher.fetchDeep(
            new TestRootOneToOneLeadingId(1l)).resultValue();
        Assertions.assertThat(result).isPresent();
        assertInsertedWithResult(inserted, result.get());
    }

    @Test
    public void testFetcherOneToOneLeadingCompleteCustomPropertyProvider() {
        JooqAggregateFetcher<TestRootOneToOneLeading, TestRootOneToOneLeadingId> jooqEntityFetcher =
            new JooqAggregateFetcher<>(TestRootOneToOneLeading.class, persistenceConfiguration.dslContext,
                persistenceConfiguration.domainPersistenceProvider);
        RecordProvider prp = new RecordProvider() {
            @Override
            public Object provide(Object parentRecord) {
                TestRootOneToOneLeadingRecord r = (TestRootOneToOneLeadingRecord) parentRecord;
                return persistenceConfiguration.dslContext
                    .fetchSingle(Tables.TEST_ENTITY_ONE_TO_ONE_LEADING,
                        Tables.TEST_ENTITY_ONE_TO_ONE_LEADING.ID.eq(r.getTestEntityId()));
            }

            @Override
            public Collection provideCollection(Object parentRecord) {
                return null;
            }
        };

        jooqEntityFetcher.withRecordProvider(prp, TestRootOneToOneLeading.class, TestEntityOneToOneLeading.class,
            List.of("testEntityOneToOneLeading"));
        TestRootOneToOneLeading inserted = oneToOneLeadingAggregateRootRepository.insert(
            TestDataGenerator.buildOneToOneLeadingComplete());
        Optional<TestRootOneToOneLeading> result = jooqEntityFetcher.fetchDeep(
            new TestRootOneToOneLeadingId(1l)).resultValue();
        Assertions.assertThat(result).isPresent();
        assertInsertedWithResult(inserted, result.get());
    }

    @Test
    public void testFetcherOneToOneLeadingOnlyRoot() {
        JooqAggregateFetcher<TestRootOneToOneLeading, TestRootOneToOneLeadingId> jooqEntityFetcher =
            new JooqAggregateFetcher<>(TestRootOneToOneLeading.class, persistenceConfiguration.dslContext,
                persistenceConfiguration.domainPersistenceProvider);

        TestRootOneToOneLeading inserted = oneToOneLeadingAggregateRootRepository.insert(
            TestDataGenerator.buildOneToOneLeadingOnlyRoot());
        Optional<TestRootOneToOneLeading> result = jooqEntityFetcher.fetchDeep(
            new TestRootOneToOneLeadingId(1l)).resultValue();
        Assertions.assertThat(result).isPresent();
        assertInsertedWithResult(inserted, result.get());
    }

    @Test
    public void testFetcherOneToOneLeadingEmpty() {
        JooqAggregateFetcher<TestRootOneToOneLeading, TestRootOneToOneLeadingId> jooqEntityFetcher =
            new JooqAggregateFetcher<>(TestRootOneToOneLeading.class, persistenceConfiguration.dslContext,
                persistenceConfiguration.domainPersistenceProvider);

        Optional<TestRootOneToOneLeading> result = jooqEntityFetcher.fetchDeep(
            new TestRootOneToOneLeadingId(1L)).resultValue();
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void testFetcherOneToManyComplete() {
        JooqAggregateFetcher<TestRootOneToMany, TestRootOneToManyId> jooqEntityFetcher =
            new JooqAggregateFetcher<>(TestRootOneToMany.class, persistenceConfiguration.dslContext,
                persistenceConfiguration.domainPersistenceProvider);

        TestRootOneToMany inserted = oneToManyAggregateRootRepository.insert(
            TestDataGenerator.buildOneToManyComplete());
        Optional<TestRootOneToMany> result = jooqEntityFetcher.fetchDeep(new TestRootOneToManyId(1l)).resultValue();
        Assertions.assertThat(result).isPresent();
        assertInsertedWithResult(inserted, result.get());
    }

    @Test
    public void testFetcherOneToManyEmpty() {
        JooqAggregateFetcher<TestRootOneToMany, TestRootOneToManyId> jooqEntityFetcher =
            new JooqAggregateFetcher<>(TestRootOneToMany.class, persistenceConfiguration.dslContext,
                persistenceConfiguration.domainPersistenceProvider);

        Optional<TestRootOneToMany> result = jooqEntityFetcher.fetchDeep(new TestRootOneToManyId(1L)).resultValue();
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void testFetcherOneToManyCompleteCustomProvider() {
        JooqAggregateFetcher<TestRootOneToMany, TestRootOneToManyId> jooqEntityFetcher =
            new JooqAggregateFetcher<>(TestRootOneToMany.class, persistenceConfiguration.dslContext,
                persistenceConfiguration.domainPersistenceProvider);

        RecordProvider<TestEntityOneToManyRecord, TestRootOneToManyRecord> prp = new RecordProvider<>() {
            @Override
            public TestEntityOneToManyRecord provide(TestRootOneToManyRecord parentRecord) {
                return null;
            }

            @Override
            public Collection<TestEntityOneToManyRecord> provideCollection(TestRootOneToManyRecord parentRecord) {
                return persistenceConfiguration.dslContext.fetch(Tables.TEST_ENTITY_ONE_TO_MANY
                    .where(Tables.TEST_ENTITY_ONE_TO_MANY.TEST_ROOT_ID.eq(parentRecord.getId())));
            }

        };

        jooqEntityFetcher.withRecordProvider(prp,
            TestRootOneToMany.class,
            TestEntityOneToMany.class,
            List.of("testEntityOneToManyList"));
        TestRootOneToMany inserted = oneToManyAggregateRootRepository.insert(
            TestDataGenerator.buildOneToManyComplete());
        Optional<TestRootOneToMany> result = jooqEntityFetcher.fetchDeep(new TestRootOneToManyId(1l)).resultValue();
        Assertions.assertThat(result).isPresent();
        assertInsertedWithResult(inserted, result.get());
    }

    @Test
    public void testFetcherOneToManyOnlyRoot() {
        JooqAggregateFetcher<TestRootOneToMany, TestRootOneToManyId> jooqEntityFetcher =
            new JooqAggregateFetcher<>(TestRootOneToMany.class, persistenceConfiguration.dslContext,
                persistenceConfiguration.domainPersistenceProvider);

        TestRootOneToMany inserted = oneToManyAggregateRootRepository.insert(
            TestDataGenerator.buildOneToManyOnlyRoot());

        Optional<TestRootOneToMany> result = jooqEntityFetcher.fetchDeep(new TestRootOneToManyId(1l)).resultValue();
        Assertions.assertThat(result).isPresent();
        assertInsertedWithResult(inserted, result.get());
    }

    @Test
    public void testFetcherOneToOneFollowingLeadingComplete() {
        JooqAggregateFetcher<TestRootOneToOneFollowingLeading, TestRootOneToOneFollowingLeadingId> jooqEntityFetcher =
            new JooqAggregateFetcher<>(TestRootOneToOneFollowingLeading.class, persistenceConfiguration.dslContext,
                persistenceConfiguration.domainPersistenceProvider);

        TestRootOneToOneFollowingLeading inserted = oneToOneFollowingLeadingAggregateRootRepository.insert(
            TestDataGenerator.buildOneToOneFollowingLeadingComplete());
        Optional<TestRootOneToOneFollowingLeading> result = jooqEntityFetcher.fetchDeep(
            new TestRootOneToOneFollowingLeadingId(1l)).resultValue();
        Assertions.assertThat(result).isPresent();
        assertInsertedWithResult(inserted, result.get());
    }

    @Test
    public void testFetcherOneToOneFollowingLeadingOnlyRoot() {
        JooqAggregateFetcher<TestRootOneToOneFollowingLeading, TestRootOneToOneFollowingLeadingId> jooqEntityFetcher =
            new JooqAggregateFetcher<>(TestRootOneToOneFollowingLeading.class, persistenceConfiguration.dslContext,
                persistenceConfiguration.domainPersistenceProvider);

        TestRootOneToOneFollowingLeading inserted = oneToOneFollowingLeadingAggregateRootRepository.insert(
            TestDataGenerator.buildOneToOneFollowingLeadingOnlyRoot());
        Optional<TestRootOneToOneFollowingLeading> result = jooqEntityFetcher.fetchDeep(
            new TestRootOneToOneFollowingLeadingId(1l)).resultValue();
        assertInsertedWithResult(inserted, result.get());
    }

    @Test
    public void testFetcherOneToOneFollowingLeadingEmpty() {
        JooqAggregateFetcher<TestRootOneToOneFollowingLeading, TestRootOneToOneFollowingLeadingId> jooqEntityFetcher =
            new JooqAggregateFetcher<>(TestRootOneToOneFollowingLeading.class, persistenceConfiguration.dslContext,
                persistenceConfiguration.domainPersistenceProvider);

        Optional<TestRootOneToOneFollowingLeading> result = jooqEntityFetcher.fetchDeep(
            new TestRootOneToOneFollowingLeadingId(1L)).resultValue();
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void testFetcherComplexExpectedException() {
        JooqAggregateFetcher<TestRoot, TestRootId> jooqEntityFetcher =
            new JooqAggregateFetcher<>(TestRoot.class, persistenceConfiguration.dslContext,
                persistenceConfiguration.domainPersistenceProvider);

        complexAggregateRootRepository.insert(TestDataGenerator.buildTestRootComplex());

        DLCPersistenceException ex = assertThrows(DLCPersistenceException.class, () -> {
            jooqEntityFetcher.fetchDeep(new TestRootId(1l));
        });
        assertThat(ex.getMessage()).contains("multiple foreign key relations");

    }

    @Test
    public void testFetcherComplex() {
        JooqAggregateFetcher<TestRoot, TestRootId> jooqEntityFetcher =
            new JooqAggregateFetcher<>(TestRoot.class, persistenceConfiguration.dslContext,
                persistenceConfiguration.domainPersistenceProvider);

        RecordProvider<TestEntity_2Record, TestEntity_1Record> prpA = new RecordProvider<>() {
            @Override
            public TestEntity_2Record provide(TestEntity_1Record parentRecord) {
                return persistenceConfiguration.dslContext.fetchSingle(Tables.TEST_ENTITY_2
                    .where(Tables.TEST_ENTITY_2.ID.eq(parentRecord.getTestEntity_2IdA())));
            }

            @Override
            public Collection<TestEntity_2Record> provideCollection(TestEntity_1Record parentRecord) {
                return null;
            }

        };

        RecordProvider<TestEntity_2Record, TestEntity_1Record> prpB = new RecordProvider<>() {
            @Override
            public TestEntity_2Record provide(TestEntity_1Record parentRecord) {
                return persistenceConfiguration.dslContext.fetchSingle(Tables.TEST_ENTITY_2
                    .where(Tables.TEST_ENTITY_2.ID.eq(parentRecord.getTestEntity_2IdB())));
            }

            @Override
            public Collection<TestEntity_2Record> provideCollection(TestEntity_1Record parentRecord) {
                return null;
            }

        };

        TestRoot inserted = complexAggregateRootRepository.insert(TestDataGenerator.buildTestRootComplex());
        Optional<TestRoot> result = jooqEntityFetcher
            .withRecordProvider(prpA, TestEntity1.class, TestEntity2.class, List.of("testEntity2A"))
            .withRecordProvider(prpB, TestEntity1.class, TestEntity2.class, List.of("testEntity2B"))
            .fetchDeep(new TestRootId(1l)).resultValue();
        Assertions.assertThat(result).isPresent();
        assertInsertedWithResult(inserted, result.get());
    }

    @Test
    public void testFetcherHierarchicalEmpty() {
        JooqAggregateFetcher<TestRootHierarchical, TestRootHierarchicalId> jooqEntityFetcher =
            new JooqAggregateFetcher<>(TestRootHierarchical.class, persistenceConfiguration.dslContext,
                persistenceConfiguration.domainPersistenceProvider);

        Optional<TestRootHierarchical> result = jooqEntityFetcher.fetchDeep(
            new TestRootHierarchicalId(1L)).resultValue();
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void testFetcherHierarchicalComplete() {
        JooqAggregateFetcher<TestRootHierarchical, TestRootHierarchicalId> jooqEntityFetcher =
            new JooqAggregateFetcher<>(TestRootHierarchical.class, persistenceConfiguration.dslContext,
                persistenceConfiguration.domainPersistenceProvider);

        TestRootHierarchical inserted = hierarchicalAggregateRootRepository.insert(
            TestDataGenerator.buildTestRootHierarchicalCompleteLevel3());
        Optional<TestRootHierarchical> result = jooqEntityFetcher
            .withRecordProvider(new RecordProvider() {
                                    @Override
                                    public Object provide(Object parentRecord) {
                                        TestRootHierarchicalRecord pr = (TestRootHierarchicalRecord) parentRecord;
                                        Optional<TestRootHierarchicalRecord> childOptional =
                                            persistenceConfiguration.dslContext.fetchOptional(
                                                Tables.TEST_ROOT_HIERARCHICAL,
                                                Tables.TEST_ROOT_HIERARCHICAL.PARENT_ID.eq(pr.getId()));
                                        if (childOptional.isPresent()) {
                                            return childOptional.get();
                                        }
                                        return null;
                                    }
                                },
                TestRootHierarchical.class,
                TestRootHierarchical.class,
                List.of("child"))
            .fetchDeep(new TestRootHierarchicalId(1l)).resultValue();
        Assertions.assertThat(result).isPresent();
        assertInsertedWithResult(inserted, result.get());
    }

    @Test
    public void testFetcherHierarchicalBackRefComplete() {
        JooqAggregateFetcher<TestRootHierarchicalBackref, TestRootHierarchicalBackrefId> jooqEntityFetcher =
            new JooqAggregateFetcher<>(TestRootHierarchicalBackref.class, persistenceConfiguration.dslContext,
                persistenceConfiguration.domainPersistenceProvider);

        TestRootHierarchicalBackref inserted = hierarchicalAggregateRootBackrefRepository.insert(
            TestDataGenerator.buildTestRootHierarchicalBackrefCompleteLevel3());
        Optional<TestRootHierarchicalBackref> result = jooqEntityFetcher
            .withRecordProvider(new RecordProvider() {
                                    @Override
                                    public Object provide(Object parentRecord) {
                                        TestRootHierarchicalBackrefRecord pr =
                                            (TestRootHierarchicalBackrefRecord) parentRecord;
                                        Optional<TestRootHierarchicalBackrefRecord> childOptional =
                                            persistenceConfiguration.dslContext.fetchOptional(
                                                Tables.TEST_ROOT_HIERARCHICAL_BACKREF,
                                                Tables.TEST_ROOT_HIERARCHICAL_BACKREF.PARENT_ID.eq(pr.getId()));
                                        if (childOptional.isPresent()) {
                                            return childOptional.get();
                                        }
                                        return null;
                                    }
                                },
                TestRootHierarchicalBackref.class,
                TestRootHierarchicalBackref.class,
                List.of("child"))
            .withRecordProvider(new RecordProvider() {
                                    @Override
                                    public Object provide(Object parentRecord) {
                                        TestRootHierarchicalBackrefRecord pr =
                                            (TestRootHierarchicalBackrefRecord) parentRecord;
                                        if (((TestRootHierarchicalBackrefRecord) parentRecord).getParentId() != null) {
                                            Optional<TestRootHierarchicalBackrefRecord> childOptional =
                                                persistenceConfiguration.dslContext.fetchOptional(
                                                    Tables.TEST_ROOT_HIERARCHICAL_BACKREF,
                                                    Tables.TEST_ROOT_HIERARCHICAL_BACKREF.ID.eq(pr.getParentId()));
                                            if (childOptional.isPresent()) {
                                                return childOptional.get();
                                            }
                                        }
                                        return null;
                                    }
                                },
                TestRootHierarchicalBackref.class,
                TestRootHierarchicalBackref.class,
                List.of("parent"))
            .fetchDeep(new TestRootHierarchicalBackrefId(1l)).resultValue();
        Assertions.assertThat(result).isPresent();
        assertInsertedWithResult(inserted, result.get());
    }

    @Test
    public void testFetcherComplexEmpty() {
        JooqAggregateFetcher<TestRoot, TestRootId> jooqEntityFetcher =
            new JooqAggregateFetcher<>(TestRoot.class, persistenceConfiguration.dslContext,
                persistenceConfiguration.domainPersistenceProvider);

        Optional<TestRoot> result = jooqEntityFetcher.fetchDeep(new TestRootId(1L)).resultValue();
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void testFetcherManyToManyEmpty() {
        JooqAggregateFetcher<TestRootManyToMany, TestRootManyToManyId> jooqEntityFetcher =
            new JooqAggregateFetcher<>(TestRootManyToMany.class, persistenceConfiguration.dslContext,
                persistenceConfiguration.domainPersistenceProvider);

        Optional<TestRootManyToMany> result = jooqEntityFetcher.fetchDeep(new TestRootManyToManyId(1L)).resultValue();
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void testFetcherManyToManyComplete() {
        JooqAggregateFetcher<TestRootManyToMany, TestRootManyToManyId> jooqEntityFetcher =
            new JooqAggregateFetcher<>(TestRootManyToMany.class, persistenceConfiguration.dslContext,
                persistenceConfiguration.domainPersistenceProvider);

        TestRootManyToMany inserted = manyToManyAggregateRootRepository.insert(
            TestDataGenerator.buildManyToManyComplete());
        Optional<TestRootManyToMany> result = jooqEntityFetcher.fetchDeep(new TestRootManyToManyId(1l)).resultValue();
        Assertions.assertThat(result).isPresent();
        assertInsertedWithResult(inserted, result.get());
    }

    protected <T extends Entity> void assertInsertedWithResult(T inserted, T result) {

        assertThat(result)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .ignoringAllOverriddenEquals()
            .withStrictTypeChecking()
            .ignoringFieldsOfTypes(UUID.class)
            .isEqualTo(inserted);
    }
}
