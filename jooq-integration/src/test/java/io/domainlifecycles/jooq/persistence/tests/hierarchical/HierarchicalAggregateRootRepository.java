package io.domainlifecycles.jooq.persistence.tests.hierarchical;


import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.internal.DomainObject;
import io.domainlifecycles.jooq.imp.JooqPersister;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.fetcher.AggregateFetcher;
import io.domainlifecycles.persistence.fetcher.FetcherResult;
import io.domainlifecycles.persistence.fetcher.RecordProvider;
import io.domainlifecycles.persistence.fetcher.simple.SimpleAggregateFetcher;
import io.domainlifecycles.persistence.fetcher.simple.SimpleFetcherContext;
import io.domainlifecycles.persistence.mapping.RecordMapper;
import io.domainlifecycles.persistence.repository.PersistenceActionPublishingRepository;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import io.domainlifecycles.test.jooq.Tables;
import io.domainlifecycles.test.jooq.tables.records.TestRootHierarchicalRecord;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;
import tests.shared.persistence.domain.hierarchical.TestRootHierarchical;
import tests.shared.persistence.domain.hierarchical.TestRootHierarchicalId;

import java.util.List;
import java.util.Optional;

@Slf4j
public class HierarchicalAggregateRootRepository extends PersistenceActionPublishingRepository<TestRootHierarchicalId
    , TestRootHierarchical, UpdatableRecord<?>> {

    private final DSLContext dslContext;

    private final JooqDomainPersistenceProvider jooqDomainPersistenceProvider;

    private final SimpleAggregateFetcher<Long, TestRootHierarchical, TestRootHierarchicalId, UpdatableRecord<?>> simpleAggregateFetcher;


    public HierarchicalAggregateRootRepository(DSLContext dslContext,
                                               PersistenceEventPublisher persistenceEventPublisher,
                                               JooqDomainPersistenceProvider jooqDomainPersistenceProvider) {
        super(
            new JooqPersister(dslContext, jooqDomainPersistenceProvider),
            jooqDomainPersistenceProvider,
            persistenceEventPublisher
        );
        this.jooqDomainPersistenceProvider = jooqDomainPersistenceProvider;
        this.dslContext = dslContext;
        this.simpleAggregateFetcher = provideFetcher();

    }

    private SimpleAggregateFetcher<Long, TestRootHierarchical, TestRootHierarchicalId, UpdatableRecord<?>> provideFetcher() {
        return new SimpleAggregateFetcher<>() {
            @Override
            public AggregateFetcher<TestRootHierarchical, TestRootHierarchicalId, UpdatableRecord<?>> withRecordProvider(RecordProvider<? extends UpdatableRecord<?>, ? extends UpdatableRecord<?>> recordProvider,
                                                                                                                         Class<? extends Entity<?>> containingEntityClass,
                                                                                                                         Class<? extends DomainObject> propertyClass,
                                                                                                                         List<String> propertyPath) {
                throw new IllegalStateException("Not implemented!");
            }


            @Override
            public TestRootHierarchical fetchBasicByIdValue(Long aLong,
                                                            SimpleFetcherContext<UpdatableRecord<?>> fetcherContext) {
                return findByIdCustom(aLong);
            }

            @Override
            public TestRootHierarchical fetchBasicByRecord(UpdatableRecord<?> aggregateRecord,
                                                           SimpleFetcherContext<UpdatableRecord<?>> fetcherContext) {
                throw new IllegalStateException("Not implemented!");
            }

        };
    }

    public TestRootHierarchical findByIdCustom(Long testRootHierarchicalId) {
        Optional<TestRootHierarchicalRecord> testRootHierarchicalRecordOptional = dslContext
            .fetchOptional(Tables.TEST_ROOT_HIERARCHICAL, Tables.TEST_ROOT_HIERARCHICAL.ID.eq(testRootHierarchicalId));

        if (testRootHierarchicalRecordOptional.isPresent()) {

            DomainObjectBuilder<TestRootHierarchical> b = testRootHierarchicalRecordOptional
                .map(r -> ((RecordMapper<TestRootHierarchicalRecord, TestRootHierarchical, TestRootHierarchical>)
                    jooqDomainPersistenceProvider
                        .persistenceMirror
                        .getEntityRecordMapper(TestRootHierarchical.class.getName()))
                    .recordToDomainObjectBuilder(r))
                .get();

            Optional<TestRootHierarchicalRecord> testRootHierarchicalChildRecordOptional = dslContext
                .fetchOptional(Tables.TEST_ROOT_HIERARCHICAL,
                    Tables.TEST_ROOT_HIERARCHICAL.PARENT_ID.eq(testRootHierarchicalId));

            if (testRootHierarchicalChildRecordOptional.isPresent()) {
                TestRootHierarchical child = findByIdCustom(testRootHierarchicalChildRecordOptional.get().getId());
                b.setFieldValue(child, "child");
            }

            return b.build();
        }
        return null;
    }

    @Override
    public FetcherResult<TestRootHierarchical, UpdatableRecord<?>> findResultById(TestRootHierarchicalId rootId) {
        return simpleAggregateFetcher.fetchDeep(rootId);
    }


}
