/*
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

package io.domainlifecycles.jooq.persistence.tests.hierarchicalBackRef;

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
import io.domainlifecycles.test.tables.records.TestRootHierarchicalBackrefRecord;
import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;
import org.slf4j.Logger;
import tests.shared.persistence.domain.hierarchicalBackRef.TestRootHierarchicalBackref;
import tests.shared.persistence.domain.hierarchicalBackRef.TestRootHierarchicalBackrefId;

import java.util.List;
import java.util.Optional;

public class HierarchicalAggregateRootBackrefRepository extends PersistenceActionPublishingRepository<TestRootHierarchicalBackrefId, TestRootHierarchicalBackref, UpdatableRecord<?>> {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(
        HierarchicalAggregateRootBackrefRepository.class);
    private final DSLContext dslContext;
    private final SimpleAggregateFetcher<Long, TestRootHierarchicalBackref, TestRootHierarchicalBackrefId,
        UpdatableRecord<?>> simpleAggregateFetcher;
    private final JooqDomainPersistenceProvider jooqDomainPersistenceProvider;

    public HierarchicalAggregateRootBackrefRepository(DSLContext dslContext,
                                                      PersistenceEventPublisher persistenceEventPublisher,
                                                      JooqDomainPersistenceProvider jooqDomainPersistenceProvider) {
        super(
            new JooqPersister(dslContext, jooqDomainPersistenceProvider),
            jooqDomainPersistenceProvider,
            persistenceEventPublisher);
        this.jooqDomainPersistenceProvider = jooqDomainPersistenceProvider;
        this.dslContext = dslContext;
        this.simpleAggregateFetcher = provideFetcher();
    }

    private SimpleAggregateFetcher<Long, TestRootHierarchicalBackref, TestRootHierarchicalBackrefId, UpdatableRecord<
        ?>> provideFetcher() {
        return new SimpleAggregateFetcher<>() {
            @Override
            public AggregateFetcher<TestRootHierarchicalBackref, TestRootHierarchicalBackrefId, UpdatableRecord<?>> withRecordProvider(
                RecordProvider<? extends UpdatableRecord<?>, ? extends UpdatableRecord<?>> recordProvider,
                Class<? extends Entity<?>> containingEntityClass,
                Class<? extends DomainObject> propertyClass,
                List<String> propertyPath
            ) {
                throw new IllegalStateException("Not implemented!");
            }

            @Override
            public TestRootHierarchicalBackref fetchBasicByIdValue(Long aLong,
                                                                   SimpleFetcherContext<UpdatableRecord<?>> fetcherContext) {
                return findByIdCustom(aLong);
            }

            @Override
            public TestRootHierarchicalBackref fetchBasicByRecord(UpdatableRecord<?> aggregateRecord,
                                                                  SimpleFetcherContext<UpdatableRecord<?>> fetcherContext) {
                throw new IllegalStateException("Not implemented!");
            }

        };
    }

    public TestRootHierarchicalBackref findByIdCustom(Long testRootHierarchicalBackrefId) {
        Optional<TestRootHierarchicalBackrefRecord> testRootHierarchicalRecordOptional = dslContext
            .fetchOptional(Tables.TEST_ROOT_HIERARCHICAL_BACKREF,
                Tables.TEST_ROOT_HIERARCHICAL_BACKREF.ID.eq(testRootHierarchicalBackrefId));

        if (testRootHierarchicalRecordOptional.isPresent()) {

            DomainObjectBuilder<TestRootHierarchicalBackref> b = testRootHierarchicalRecordOptional
                .map(
                    r -> ((RecordMapper<TestRootHierarchicalBackrefRecord, TestRootHierarchicalBackref,
                        TestRootHierarchicalBackref>)
                        jooqDomainPersistenceProvider
                            .persistenceMirror
                            .getEntityRecordMapper(TestRootHierarchicalBackref.class.getName()))
                        .recordToDomainObjectBuilder(r)).get();

            Optional<TestRootHierarchicalBackrefRecord> testRootHierarchicalChildRecordOptional = dslContext
                .fetchOptional(Tables.TEST_ROOT_HIERARCHICAL_BACKREF,
                    Tables.TEST_ROOT_HIERARCHICAL_BACKREF.PARENT_ID.eq(testRootHierarchicalBackrefId));

            if (testRootHierarchicalChildRecordOptional.isPresent()) {
                TestRootHierarchicalBackref child = findByIdCustom(
                    testRootHierarchicalChildRecordOptional.get().getId());
                b.setFieldValue(child, "child");
            }

            TestRootHierarchicalBackref tr = b.build();
            if (tr.getChild() != null) {
                tr.getChild().setParent(tr);
            }

            return tr;
        } else {
            return null;
        }
    }

    @Override
    public FetcherResult<TestRootHierarchicalBackref, UpdatableRecord<?>> findResultById(TestRootHierarchicalBackrefId rootId) {
        return simpleAggregateFetcher.fetchDeep(rootId);
    }

}
