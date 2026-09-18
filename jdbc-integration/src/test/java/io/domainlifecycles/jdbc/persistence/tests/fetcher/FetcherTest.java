package io.domainlifecycles.jdbc.persistence.tests.fetcher;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.jdbc.imp.JdbcAggregateFetcher;
import io.domainlifecycles.jdbc.persistence.JdbcBasePersistence_ITest;
import io.domainlifecycles.jdbc.persistence.tests.complex.ComplexAggregateRootRepository;
import io.domainlifecycles.jdbc.persistence.tests.hierarchical.HierarchicalAggregateRootRepository;
import io.domainlifecycles.jdbc.persistence.tests.hierarchicalBackRef.HierarchicalAggregateRootBackrefRepository;
import io.domainlifecycles.jdbc.persistence.tests.manyToManyWithJoinEntity.ManyToManyAggregateRootRepository;
import io.domainlifecycles.jdbc.persistence.tests.oneToMany.OneToManyAggregateRootRepository;
import io.domainlifecycles.jdbc.persistence.tests.oneToOneFollowingFK.OneToOneFollowingAggregateRootRepository;
import io.domainlifecycles.jdbc.persistence.tests.oneToOneFollowingLeadingFK.OneToOneFollowingLeadingAggregateRootRepository;
import io.domainlifecycles.jdbc.persistence.tests.oneToManyIdentityEnum.RootIdEnumListRepository;
import io.domainlifecycles.jdbc.persistence.tests.oneToOneLeadingFK.OneToOneLeadingAggregateRootRepository;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.jdbc.schema.TableMetadata;
import io.domainlifecycles.jdbc.util.JdbcRecordMapper;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.fetcher.RecordProvider;
import io.domainlifecycles.persistence.mapping.RecordMapper;
import org.assertj.core.api.Assertions;
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
import tests.shared.persistence.domain.oneToManyIdentityEnum.RootIdEnumList;
import tests.shared.persistence.domain.oneToManyIdentityEnum.RootIdEnumListId;
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
public class FetcherTest extends JdbcBasePersistence_ITest {

    private static OneToOneFollowingAggregateRootRepository oneToOneFollowingAggregateRootRepository;

    private static OneToOneLeadingAggregateRootRepository oneToOneLeadingAggregateRootRepository;

    private static OneToManyAggregateRootRepository oneToManyAggregateRootRepository;

    private static OneToOneFollowingLeadingAggregateRootRepository oneToOneFollowingLeadingAggregateRootRepository;

    private static ComplexAggregateRootRepository complexAggregateRootRepository;

    private static HierarchicalAggregateRootRepository hierarchicalAggregateRootRepository;

    private static HierarchicalAggregateRootBackrefRepository hierarchicalAggregateRootBackrefRepository;

    private static ManyToManyAggregateRootRepository manyToManyAggregateRootRepository;

    private static RootIdEnumListRepository rootIdEnumListRepository;

    @BeforeAll
    public void init() {
        rootIdEnumListRepository = new RootIdEnumListRepository(
            persistenceConfiguration.domainPersistenceProvider,
            persistenceEventTestHelper.testEventPublisher
        );
        manyToManyAggregateRootRepository = new ManyToManyAggregateRootRepository(
            persistenceConfiguration.domainPersistenceProvider,
            persistenceEventTestHelper.testEventPublisher);
        hierarchicalAggregateRootBackrefRepository = new HierarchicalAggregateRootBackrefRepository(
            persistenceConfiguration.domainPersistenceProvider,
            persistenceEventTestHelper.testEventPublisher
        );
        hierarchicalAggregateRootRepository = new HierarchicalAggregateRootRepository(
            persistenceConfiguration.domainPersistenceProvider,
            persistenceEventTestHelper.testEventPublisher
        );
        complexAggregateRootRepository = new ComplexAggregateRootRepository(
            persistenceConfiguration.domainPersistenceProvider,
            persistenceEventTestHelper.testEventPublisher
        );
        oneToOneFollowingLeadingAggregateRootRepository = new OneToOneFollowingLeadingAggregateRootRepository(
            persistenceConfiguration.domainPersistenceProvider,
            persistenceEventTestHelper.testEventPublisher
        );
        oneToManyAggregateRootRepository = new OneToManyAggregateRootRepository(
            persistenceConfiguration.domainPersistenceProvider,
            persistenceEventTestHelper.testEventPublisher
        );
        oneToOneLeadingAggregateRootRepository = new OneToOneLeadingAggregateRootRepository(
            persistenceConfiguration.domainPersistenceProvider,
            persistenceEventTestHelper.testEventPublisher
        );
        oneToOneFollowingAggregateRootRepository = new OneToOneFollowingAggregateRootRepository(
            persistenceConfiguration.domainPersistenceProvider,
            persistenceEventTestHelper.testEventPublisher
        );
    }

    @Test
    public void testFetcherOneToOneFollowingCompleteByRootRecord() {
        JdbcAggregateFetcher<TestRootOneToOneFollowing, TestRootOneToOneFollowingId> jdbcEntityFetcher =
            new JdbcAggregateFetcher<>(TestRootOneToOneFollowing.class, persistenceConfiguration.domainPersistenceProvider);
        List<TestRootOneToOneFollowing> inserted = TestDataGenerator.buildManyOneToOneFollowingComplete().stream().map(
            r -> oneToOneFollowingAggregateRootRepository.insert(r)
        ).collect(Collectors.toList());

        var table = persistenceConfiguration.schemaMetadata.table("TEST_ROOT_ONE_TO_ONE_FOLLOWING");
        List<TestRootOneToOneFollowing> result = selectMany(table,
                "SELECT * FROM " + table.qualifiedName() + " WHERE NAME LIKE ? ORDER BY ID", "%Root%")
            .stream()
            .map(r -> jdbcEntityFetcher.fetchDeep(r).resultValue().get()).collect(
                Collectors.toList());

        for (int i = 0; i < inserted.size(); i++) {
            assertInsertedWithResult(inserted.get(i), result.get(i));
        }
    }

    @Test
    public void testFetcherOneToOneFollowingEmpty() {
        JdbcAggregateFetcher<TestRootOneToOneFollowing, TestRootOneToOneFollowingId> jdbcEntityFetcher =
            new JdbcAggregateFetcher<>(TestRootOneToOneFollowing.class, persistenceConfiguration.domainPersistenceProvider);

        Optional<TestRootOneToOneFollowing> result = jdbcEntityFetcher.fetchDeep(
            new TestRootOneToOneFollowingId(1L)).resultValue();
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testMitigateOneToNQueryProblem() {
        List<TestRootOneToOneFollowing> inserted = TestDataGenerator.buildManyOneToOneFollowingComplete().stream().map(
            r -> oneToOneFollowingAggregateRootRepository.insert(r)
        ).collect(Collectors.toList());

        RecordMapper<JdbcRecord, TestRootOneToOneFollowing, TestRootOneToOneFollowing> rmRoot =
            (RecordMapper<JdbcRecord, TestRootOneToOneFollowing, TestRootOneToOneFollowing>)
                persistenceConfiguration.domainPersistenceProvider
                    .persistenceMirror
                    .getEntityRecordMapper(TestRootOneToOneFollowing.class.getName());
        RecordMapper<JdbcRecord, TestEntityOneToOneFollowing, TestRootOneToOneFollowing> rmEntity =
            (RecordMapper<JdbcRecord, TestEntityOneToOneFollowing, TestRootOneToOneFollowing>)
                persistenceConfiguration.domainPersistenceProvider
                    .persistenceMirror
                    .getEntityRecordMapper(TestEntityOneToOneFollowing.class.getName());

        var rootTable = persistenceConfiguration.schemaMetadata.table("TEST_ROOT_ONE_TO_ONE_FOLLOWING");
        var entityTable = persistenceConfiguration.schemaMetadata.table("TEST_ENTITY_ONE_TO_ONE_FOLLOWING");

        List<TestRootOneToOneFollowing> result = selectMany(rootTable,
                "SELECT * FROM " + rootTable.qualifiedName() + " WHERE NAME LIKE ? ORDER BY ID", "%Root%")
            .stream()
            .map(recRoot -> {
                JdbcRecord recEntity = selectOne(entityTable,
                    "SELECT * FROM " + entityTable.qualifiedName() + " WHERE TEST_ROOT_ID = ?", recRoot.get("ID"));
                DomainObjectBuilder<TestRootOneToOneFollowing> rootDomainObjectBuilder =
                    rmRoot.recordToDomainObjectBuilder(recRoot);
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
        JdbcAggregateFetcher<TestRootOneToOneFollowing, TestRootOneToOneFollowingId> jdbcEntityFetcher =
            new JdbcAggregateFetcher<>(TestRootOneToOneFollowing.class, persistenceConfiguration.domainPersistenceProvider);

        TestRootOneToOneFollowing inserted = oneToOneFollowingAggregateRootRepository.insert(
            TestDataGenerator.buildOneToOneFollowingComplete());

        Optional<TestRootOneToOneFollowing> result = jdbcEntityFetcher.fetchDeep(
            inserted.getId()).resultValue();
        Assertions.assertThat(result).isPresent();
        assertInsertedWithResult(inserted, result.get());
    }

    @Test
    public void testFetcherOneToOneFollowingOnlyRoot() {
        JdbcAggregateFetcher<TestRootOneToOneFollowing, TestRootOneToOneFollowingId> jdbcEntityFetcher =
            new JdbcAggregateFetcher<>(TestRootOneToOneFollowing.class, persistenceConfiguration.domainPersistenceProvider);
        TestRootOneToOneFollowing inserted = oneToOneFollowingAggregateRootRepository.insert(
            TestDataGenerator.buildOneToOneFollowingOnlyRoot());
        Optional<TestRootOneToOneFollowing> result = jdbcEntityFetcher.fetchDeep(
            inserted.getId()).resultValue();
        Assertions.assertThat(result).isPresent();
        assertInsertedWithResult(inserted, result.get());
    }

    @Test
    public void testFetcherOneToOneLeadingComplete() {
        JdbcAggregateFetcher<TestRootOneToOneLeading, TestRootOneToOneLeadingId> jdbcEntityFetcher =
            new JdbcAggregateFetcher<>(TestRootOneToOneLeading.class, persistenceConfiguration.domainPersistenceProvider);
        TestRootOneToOneLeading inserted = oneToOneLeadingAggregateRootRepository.insert(
            TestDataGenerator.buildOneToOneLeadingComplete());
        Optional<TestRootOneToOneLeading> result = jdbcEntityFetcher.fetchDeep(
            inserted.getId()).resultValue();
        Assertions.assertThat(result).isPresent();
        assertInsertedWithResult(inserted, result.get());
    }

    @Test
    public void testFetcherOneToOneLeadingCompleteCustomPropertyProvider() {
        JdbcAggregateFetcher<TestRootOneToOneLeading, TestRootOneToOneLeadingId> jdbcEntityFetcher =
            new JdbcAggregateFetcher<>(TestRootOneToOneLeading.class, persistenceConfiguration.domainPersistenceProvider);
        var entityTable = persistenceConfiguration.schemaMetadata.table("TEST_ENTITY_ONE_TO_ONE_LEADING");
        RecordProvider<JdbcRecord, JdbcRecord> prp = new RecordProvider<>() {
            @Override
            public JdbcRecord provide(JdbcRecord parentRecord) {
                return selectOne(entityTable,
                    "SELECT * FROM " + entityTable.qualifiedName() + " WHERE ID = ?",
                    parentRecord.get("TEST_ENTITY_ID"));
            }

            @Override
            public Collection<JdbcRecord> provideCollection(JdbcRecord parentRecord) {
                return null;
            }
        };

        jdbcEntityFetcher.withRecordProvider(prp, TestRootOneToOneLeading.class, TestEntityOneToOneLeading.class,
            List.of("testEntityOneToOneLeading"));
        TestRootOneToOneLeading inserted = oneToOneLeadingAggregateRootRepository.insert(
            TestDataGenerator.buildOneToOneLeadingComplete());
        Optional<TestRootOneToOneLeading> result = jdbcEntityFetcher.fetchDeep(
            inserted.getId()).resultValue();
        Assertions.assertThat(result).isPresent();
        assertInsertedWithResult(inserted, result.get());
    }

    @Test
    public void testFetcherOneToOneLeadingOnlyRoot() {
        JdbcAggregateFetcher<TestRootOneToOneLeading, TestRootOneToOneLeadingId> jdbcEntityFetcher =
            new JdbcAggregateFetcher<>(TestRootOneToOneLeading.class, persistenceConfiguration.domainPersistenceProvider);

        TestRootOneToOneLeading inserted = oneToOneLeadingAggregateRootRepository.insert(
            TestDataGenerator.buildOneToOneLeadingOnlyRoot());
        Optional<TestRootOneToOneLeading> result = jdbcEntityFetcher.fetchDeep(
            inserted.getId()).resultValue();
        Assertions.assertThat(result).isPresent();
        assertInsertedWithResult(inserted, result.get());
    }

    @Test
    public void testFetcherOneToOneLeadingEmpty() {
        JdbcAggregateFetcher<TestRootOneToOneLeading, TestRootOneToOneLeadingId> jdbcEntityFetcher =
            new JdbcAggregateFetcher<>(TestRootOneToOneLeading.class, persistenceConfiguration.domainPersistenceProvider);

        Optional<TestRootOneToOneLeading> result = jdbcEntityFetcher.fetchDeep(
            new TestRootOneToOneLeadingId(1L)).resultValue();
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void testFetcherOneToManyComplete() {
        JdbcAggregateFetcher<TestRootOneToMany, TestRootOneToManyId> jdbcEntityFetcher =
            new JdbcAggregateFetcher<>(TestRootOneToMany.class, persistenceConfiguration.domainPersistenceProvider);

        TestRootOneToMany inserted = oneToManyAggregateRootRepository.insert(
            TestDataGenerator.buildOneToManyComplete());
        Optional<TestRootOneToMany> result = jdbcEntityFetcher.fetchDeep(inserted.getId()).resultValue();
        Assertions.assertThat(result).isPresent();
        assertInsertedWithResult(inserted, result.get());
    }

    @Test
    public void testFetcherOneToManyEmpty() {
        JdbcAggregateFetcher<TestRootOneToMany, TestRootOneToManyId> jdbcEntityFetcher =
            new JdbcAggregateFetcher<>(TestRootOneToMany.class, persistenceConfiguration.domainPersistenceProvider);

        Optional<TestRootOneToMany> result = jdbcEntityFetcher.fetchDeep(new TestRootOneToManyId(1L)).resultValue();
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void testFetcherOneToManyCompleteCustomProvider() {
        JdbcAggregateFetcher<TestRootOneToMany, TestRootOneToManyId> jdbcEntityFetcher =
            new JdbcAggregateFetcher<>(TestRootOneToMany.class, persistenceConfiguration.domainPersistenceProvider);

        var entityTable = persistenceConfiguration.schemaMetadata.table("TEST_ENTITY_ONE_TO_MANY");
        RecordProvider<JdbcRecord, JdbcRecord> prp = new RecordProvider<>() {
            @Override
            public JdbcRecord provide(JdbcRecord parentRecord) {
                return null;
            }

            @Override
            public Collection<JdbcRecord> provideCollection(JdbcRecord parentRecord) {
                return selectMany(entityTable,
                    "SELECT * FROM " + entityTable.qualifiedName() + " WHERE TEST_ROOT_ID = ?",
                    parentRecord.get("ID"));
            }

        };

        jdbcEntityFetcher.withRecordProvider(prp,
            TestRootOneToMany.class,
            TestEntityOneToMany.class,
            List.of("testEntityOneToManyList"));
        TestRootOneToMany inserted = oneToManyAggregateRootRepository.insert(
            TestDataGenerator.buildOneToManyComplete());
        Optional<TestRootOneToMany> result = jdbcEntityFetcher.fetchDeep(inserted.getId()).resultValue();
        Assertions.assertThat(result).isPresent();
        assertInsertedWithResult(inserted, result.get());
    }

    @Test
    public void testFetcherOneToManyOnlyRoot() {
        JdbcAggregateFetcher<TestRootOneToMany, TestRootOneToManyId> jdbcEntityFetcher =
            new JdbcAggregateFetcher<>(TestRootOneToMany.class, persistenceConfiguration.domainPersistenceProvider);

        TestRootOneToMany inserted = oneToManyAggregateRootRepository.insert(
            TestDataGenerator.buildOneToManyOnlyRoot());

        Optional<TestRootOneToMany> result = jdbcEntityFetcher.fetchDeep(inserted.getId()).resultValue();
        Assertions.assertThat(result).isPresent();
        assertInsertedWithResult(inserted, result.get());
    }

    @Test
    public void testFetcherOneToOneFollowingLeadingComplete() {
        JdbcAggregateFetcher<TestRootOneToOneFollowingLeading, TestRootOneToOneFollowingLeadingId> jdbcEntityFetcher =
            new JdbcAggregateFetcher<>(TestRootOneToOneFollowingLeading.class,
                persistenceConfiguration.domainPersistenceProvider);

        TestRootOneToOneFollowingLeading inserted = oneToOneFollowingLeadingAggregateRootRepository.insert(
            TestDataGenerator.buildOneToOneFollowingLeadingComplete());
        Optional<TestRootOneToOneFollowingLeading> result = jdbcEntityFetcher.fetchDeep(
            inserted.getId()).resultValue();
        Assertions.assertThat(result).isPresent();
        assertInsertedWithResult(inserted, result.get());
    }

    @Test
    public void testFetcherOneToOneFollowingLeadingOnlyRoot() {
        JdbcAggregateFetcher<TestRootOneToOneFollowingLeading, TestRootOneToOneFollowingLeadingId> jdbcEntityFetcher =
            new JdbcAggregateFetcher<>(TestRootOneToOneFollowingLeading.class,
                persistenceConfiguration.domainPersistenceProvider);

        TestRootOneToOneFollowingLeading inserted = oneToOneFollowingLeadingAggregateRootRepository.insert(
            TestDataGenerator.buildOneToOneFollowingLeadingOnlyRoot());
        Optional<TestRootOneToOneFollowingLeading> result = jdbcEntityFetcher.fetchDeep(
            inserted.getId()).resultValue();
        assertInsertedWithResult(inserted, result.get());
    }

    @Test
    public void testFetcherOneToOneFollowingLeadingEmpty() {
        JdbcAggregateFetcher<TestRootOneToOneFollowingLeading, TestRootOneToOneFollowingLeadingId> jdbcEntityFetcher =
            new JdbcAggregateFetcher<>(TestRootOneToOneFollowingLeading.class,
                persistenceConfiguration.domainPersistenceProvider);

        Optional<TestRootOneToOneFollowingLeading> result = jdbcEntityFetcher.fetchDeep(
            new TestRootOneToOneFollowingLeadingId(1L)).resultValue();
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void testFetcherComplexExpectedException() {
        JdbcAggregateFetcher<TestRoot, TestRootId> jdbcEntityFetcher =
            new JdbcAggregateFetcher<>(TestRoot.class, persistenceConfiguration.domainPersistenceProvider);

        complexAggregateRootRepository.insert(TestDataGenerator.buildTestRootComplex());

        DLCPersistenceException ex = assertThrows(DLCPersistenceException.class, () -> {
            jdbcEntityFetcher.fetchDeep(new TestRootId(1L));
        });
        assertThat(ex.getMessage()).contains("multiple foreign key relations");

    }

    @Test
    public void testFetcherComplex() {
        JdbcAggregateFetcher<TestRoot, TestRootId> jdbcEntityFetcher =
            new JdbcAggregateFetcher<>(TestRoot.class, persistenceConfiguration.domainPersistenceProvider);

        var entity2Table = persistenceConfiguration.schemaMetadata.table("TEST_ENTITY_2");
        RecordProvider<JdbcRecord, JdbcRecord> prpA = new RecordProvider<>() {
            @Override
            public JdbcRecord provide(JdbcRecord parentRecord) {
                return selectOne(entity2Table,
                    "SELECT * FROM " + entity2Table.qualifiedName() + " WHERE ID = ?",
                    parentRecord.get("TEST_ENTITY_2_ID_A"));
            }

            @Override
            public Collection<JdbcRecord> provideCollection(JdbcRecord parentRecord) {
                return null;
            }

        };

        RecordProvider<JdbcRecord, JdbcRecord> prpB = new RecordProvider<>() {
            @Override
            public JdbcRecord provide(JdbcRecord parentRecord) {
                return selectOne(entity2Table,
                    "SELECT * FROM " + entity2Table.qualifiedName() + " WHERE ID = ?",
                    parentRecord.get("TEST_ENTITY_2_ID_B"));
            }

            @Override
            public Collection<JdbcRecord> provideCollection(JdbcRecord parentRecord) {
                return null;
            }

        };

        TestRoot inserted = complexAggregateRootRepository.insert(TestDataGenerator.buildTestRootComplex());
        Optional<TestRoot> result = jdbcEntityFetcher
            .withRecordProvider(prpA, TestEntity1.class, TestEntity2.class, List.of("testEntity2A"))
            .withRecordProvider(prpB, TestEntity1.class, TestEntity2.class, List.of("testEntity2B"))
            .fetchDeep(inserted.getId()).resultValue();
        Assertions.assertThat(result).isPresent();
        assertInsertedWithResult(inserted, result.get());
    }

    @Test
    public void testFetcherHierarchicalEmpty() {
        JdbcAggregateFetcher<TestRootHierarchical, TestRootHierarchicalId> jdbcEntityFetcher =
            new JdbcAggregateFetcher<>(TestRootHierarchical.class, persistenceConfiguration.domainPersistenceProvider);

        Optional<TestRootHierarchical> result = jdbcEntityFetcher.fetchDeep(
            new TestRootHierarchicalId(1L)).resultValue();
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void testFetcherHierarchicalComplete() {
        JdbcAggregateFetcher<TestRootHierarchical, TestRootHierarchicalId> jdbcEntityFetcher =
            new JdbcAggregateFetcher<>(TestRootHierarchical.class, persistenceConfiguration.domainPersistenceProvider);

        TestRootHierarchical inserted = hierarchicalAggregateRootRepository.insert(
            TestDataGenerator.buildTestRootHierarchicalCompleteLevel3());
        var table = persistenceConfiguration.schemaMetadata.table("TEST_ROOT_HIERARCHICAL");
        Optional<TestRootHierarchical> result = jdbcEntityFetcher
            .withRecordProvider(new RecordProvider<JdbcRecord, JdbcRecord>() {
                                    @Override
                                    public JdbcRecord provide(JdbcRecord parentRecord) {
                                        var rows = selectMany(table,
                                            "SELECT * FROM " + table.qualifiedName() + " WHERE PARENT_ID = ?",
                                            parentRecord.get("ID"));
                                        return rows.isEmpty() ? null : rows.get(0);
                                    }
                                },
                TestRootHierarchical.class,
                TestRootHierarchical.class,
                List.of("child"))
            .fetchDeep(inserted.getId()).resultValue();
        Assertions.assertThat(result).isPresent();
        assertInsertedWithResult(inserted, result.get());
    }

    @Test
    public void testFetcherHierarchicalBackRefComplete() {
        JdbcAggregateFetcher<TestRootHierarchicalBackref, TestRootHierarchicalBackrefId> jdbcEntityFetcher =
            new JdbcAggregateFetcher<>(TestRootHierarchicalBackref.class, persistenceConfiguration.domainPersistenceProvider);

        TestRootHierarchicalBackref inserted = hierarchicalAggregateRootBackrefRepository.insert(
            TestDataGenerator.buildTestRootHierarchicalBackrefCompleteLevel3());
        var table = persistenceConfiguration.schemaMetadata.table("TEST_ROOT_HIERARCHICAL_BACKREF");
        Optional<TestRootHierarchicalBackref> result = jdbcEntityFetcher
            .withRecordProvider(new RecordProvider<JdbcRecord, JdbcRecord>() {
                                    @Override
                                    public JdbcRecord provide(JdbcRecord parentRecord) {
                                        var rows = selectMany(table,
                                            "SELECT * FROM " + table.qualifiedName() + " WHERE PARENT_ID = ?",
                                            parentRecord.get("ID"));
                                        return rows.isEmpty() ? null : rows.get(0);
                                    }
                                },
                TestRootHierarchicalBackref.class,
                TestRootHierarchicalBackref.class,
                List.of("child"))
            .withRecordProvider(new RecordProvider<JdbcRecord, JdbcRecord>() {
                                    @Override
                                    public JdbcRecord provide(JdbcRecord parentRecord) {
                                        Object parentId = parentRecord.get("PARENT_ID");
                                        if (parentId != null) {
                                            var rows = selectMany(table,
                                                "SELECT * FROM " + table.qualifiedName() + " WHERE ID = ?",
                                                parentId);
                                            if (!rows.isEmpty()) {
                                                return rows.get(0);
                                            }
                                        }
                                        return null;
                                    }
                                },
                TestRootHierarchicalBackref.class,
                TestRootHierarchicalBackref.class,
                List.of("parent"))
            .fetchDeep(inserted.getId()).resultValue();
        Assertions.assertThat(result).isPresent();
        assertInsertedWithResult(inserted, result.get());
    }

    @Test
    public void testFetcherComplexEmpty() {
        JdbcAggregateFetcher<TestRoot, TestRootId> jdbcEntityFetcher =
            new JdbcAggregateFetcher<>(TestRoot.class, persistenceConfiguration.domainPersistenceProvider);

        Optional<TestRoot> result = jdbcEntityFetcher.fetchDeep(new TestRootId(1L)).resultValue();
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void testFetcherManyToManyEmpty() {
        JdbcAggregateFetcher<TestRootManyToMany, TestRootManyToManyId> jdbcEntityFetcher =
            new JdbcAggregateFetcher<>(TestRootManyToMany.class, persistenceConfiguration.domainPersistenceProvider);

        Optional<TestRootManyToMany> result = jdbcEntityFetcher.fetchDeep(new TestRootManyToManyId(1L)).resultValue();
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void testFetcherManyToManyComplete() {
        JdbcAggregateFetcher<TestRootManyToMany, TestRootManyToManyId> jdbcEntityFetcher =
            new JdbcAggregateFetcher<>(TestRootManyToMany.class, persistenceConfiguration.domainPersistenceProvider);

        TestRootManyToMany inserted = manyToManyAggregateRootRepository.insert(
            TestDataGenerator.buildManyToManyComplete());
        Optional<TestRootManyToMany> result = jdbcEntityFetcher.fetchDeep(inserted.getId()).resultValue();
        Assertions.assertThat(result).isPresent();
        assertInsertedWithResult(inserted, result.get());
    }

    @Test
    public void testFetcherIdEnumListEmpty() {
        JdbcAggregateFetcher<RootIdEnumList, RootIdEnumListId> jdbcEntityFetcher =
            new JdbcAggregateFetcher<>(RootIdEnumList.class, persistenceConfiguration.domainPersistenceProvider);

        Optional<RootIdEnumList> result = jdbcEntityFetcher.fetchDeep(new RootIdEnumListId(1L)).resultValue();
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void testFetcherIdEnumListComplete() {
        JdbcAggregateFetcher<RootIdEnumList, RootIdEnumListId> jdbcEntityFetcher =
            new JdbcAggregateFetcher<>(RootIdEnumList.class, persistenceConfiguration.domainPersistenceProvider);

        RootIdEnumList inserted = rootIdEnumListRepository.insert(TestDataGenerator.buildRootIdEnumListComplete());
        Optional<RootIdEnumList> result = jdbcEntityFetcher.fetchDeep(inserted.getId()).resultValue();
        Assertions.assertThat(result).isPresent();
        assertThat(result.get().getEnumList()).hasSize(2);
        assertThat(result.get().getIdList()).hasSize(2);
        assertThat(result.get().getEntity().getEnumList()).hasSize(1);
        assertThat(result.get().getEntity().getIdList()).hasSize(1);
        assertInsertedWithResult(inserted, result.get());
    }

    @Test
    public void testFetcherIdEnumListWithDuplicateValues() {
        JdbcAggregateFetcher<RootIdEnumList, RootIdEnumListId> jdbcEntityFetcher =
            new JdbcAggregateFetcher<>(RootIdEnumList.class, persistenceConfiguration.domainPersistenceProvider);

        //two equal MyEnum.ONE and two equal MyId(7) - a fetcher that (incorrectly) deduplicated by value would
        //come back with only 2 elements per list instead of 3
        RootIdEnumList inserted = rootIdEnumListRepository.insert(
            TestDataGenerator.buildRootIdEnumListWithDuplicates());
        Optional<RootIdEnumList> result = jdbcEntityFetcher.fetchDeep(inserted.getId()).resultValue();
        Assertions.assertThat(result).isPresent();
        assertThat(result.get().getEnumList()).hasSize(3);
        assertThat(result.get().getIdList()).hasSize(3);
        assertInsertedWithResult(inserted, result.get());
    }

    @Test
    public void testFetcherIdEnumListWithUuidIds() {
        JdbcAggregateFetcher<RootIdEnumList, RootIdEnumListId> jdbcEntityFetcher =
            new JdbcAggregateFetcher<>(RootIdEnumList.class, persistenceConfiguration.domainPersistenceProvider);

        //an Identity value type other than Long (UUID, stored as VARCHAR2(36)) - proves the write-side
        //UUID->String conversion (via the auto-discovered DefaultUuidToStringConverter) and the read-side
        //String->UUID coercion (via DefaultIdentityFactory) both apply to scalar list elements exactly as
        //they already do for a single (non-list) Identity field
        RootIdEnumList inserted = rootIdEnumListRepository.insert(
            TestDataGenerator.buildRootIdEnumListWithUuidIds());
        Optional<RootIdEnumList> result = jdbcEntityFetcher.fetchDeep(inserted.getId()).resultValue();
        Assertions.assertThat(result).isPresent();
        assertThat(result.get().getUuidIdList()).hasSize(2);
        assertInsertedWithResult(inserted, result.get());
    }

    @Test
    public void testFetcherIdEnumListWithValueWithListsList() {
        JdbcAggregateFetcher<RootIdEnumList, RootIdEnumListId> jdbcEntityFetcher =
            new JdbcAggregateFetcher<>(RootIdEnumList.class, persistenceConfiguration.domainPersistenceProvider);

        //a List<ValueWithLists>: a value object used as a to-many element, itself holding its own
        //List<MyEnum>/List<MyId> fields - a three-level fetch (root -> VO row -> VO's own scalar list rows)
        RootIdEnumList inserted = rootIdEnumListRepository.insert(
            TestDataGenerator.buildRootIdEnumListWithValueWithListsList());
        Optional<RootIdEnumList> result = jdbcEntityFetcher.fetchDeep(inserted.getId()).resultValue();
        Assertions.assertThat(result).isPresent();
        assertThat(result.get().getValueWithListsList()).hasSize(2);
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

    private JdbcRecord selectOne(TableMetadata table, String sql, Object... params) {
        List<JdbcRecord> rows = selectMany(table, sql, params);
        return rows.isEmpty() ? null : rows.get(0);
    }

    private List<JdbcRecord> selectMany(TableMetadata table, String sql, Object... params) {
        return JdbcRecordMapper.selectWithSql(persistenceConfiguration.connectionProvider, table, sql, params);
    }
}
