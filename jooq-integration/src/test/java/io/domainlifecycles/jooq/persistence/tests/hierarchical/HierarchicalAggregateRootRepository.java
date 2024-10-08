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

package io.domainlifecycles.jooq.persistence.tests.hierarchical;


import lombok.extern.slf4j.Slf4j;
import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.internal.DomainObject;
import io.domainlifecycles.jooq.imp.JooqPersister;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceActionPublishingRepository;
import io.domainlifecycles.persistence.fetcher.AggregateFetcher;
import io.domainlifecycles.persistence.fetcher.FetcherResult;
import io.domainlifecycles.persistence.fetcher.RecordProvider;
import io.domainlifecycles.persistence.fetcher.simple.SimpleAggregateFetcher;
import io.domainlifecycles.persistence.fetcher.simple.SimpleFetcherContext;
import io.domainlifecycles.persistence.mapping.RecordMapper;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import io.domainlifecycles.test.Tables;
import io.domainlifecycles.test.tables.records.TestRootHierarchicalRecord;
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
