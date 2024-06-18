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

package io.domainlifecycles.jooq.persistence.tests.complex;


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
import io.domainlifecycles.persistence.provider.DomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import io.domainlifecycles.test.Tables;
import io.domainlifecycles.test.tables.records.TestEntity_1Record;
import io.domainlifecycles.test.tables.records.TestEntity_2Record;
import io.domainlifecycles.test.tables.records.TestEntity_3Record;
import io.domainlifecycles.test.tables.records.TestEntity_4Record;
import io.domainlifecycles.test.tables.records.TestEntity_5Record;
import io.domainlifecycles.test.tables.records.TestEntity_6Record;
import io.domainlifecycles.test.tables.records.TestRootRecord;
import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


public class ComplexAggregateRootRepository extends PersistenceActionPublishingRepository<TestRootId, TestRoot, UpdatableRecord<?>> {

    private static final Logger log = LoggerFactory.getLogger(ComplexAggregateRootRepository.class);
    private final DSLContext dslContext;

    private final SimpleAggregateFetcher<Long, TestRoot, TestRootId, UpdatableRecord<?>> simpleAggregateFetcher;

    private final DomainPersistenceProvider domainPersistenceProvider;

    public ComplexAggregateRootRepository(DSLContext dslContext,
                                          PersistenceEventPublisher persistenceEventPublisher,
                                          JooqDomainPersistenceProvider domainPersistenceProvider) {
        super(
            new JooqPersister(dslContext, domainPersistenceProvider),
            domainPersistenceProvider,
            persistenceEventPublisher
        );
        this.domainPersistenceProvider = domainPersistenceProvider;
        this.dslContext = dslContext;
        this.simpleAggregateFetcher = provideFetcher();

    }

    private SimpleAggregateFetcher<Long, TestRoot, TestRootId, UpdatableRecord<?>> provideFetcher() {
        return new SimpleAggregateFetcher<>() {
            @Override
            public AggregateFetcher<TestRoot, TestRootId, UpdatableRecord<?>> withRecordProvider(RecordProvider<? extends UpdatableRecord<?>, ? extends UpdatableRecord<?>> recordProvider,
                                                                                                 Class<? extends Entity<?>> containingEntityClass,
                                                                                                 Class<? extends DomainObject> propertyClass,
                                                                                                 List<String> propertyPath) {
                throw new IllegalStateException("Not implemented!");
            }

            @Override
            public TestRoot fetchBasicByIdValue(Long aLong, SimpleFetcherContext<UpdatableRecord<?>> fetcherContext) {
                return findByIdCustom(aLong);
            }

            @Override
            public TestRoot fetchBasicByRecord(UpdatableRecord<?> aggregateRecord, SimpleFetcherContext<UpdatableRecord<?>> fetcherContext) {
                throw new IllegalStateException("Not implemented!");
            }

        };
    }


    private TestRoot findByIdCustom(Long id) {
        Optional<TestRootRecord> testRootOptional = dslContext.fetchOptional(Tables.TEST_ROOT, Tables.TEST_ROOT.ID.eq(id));
        TestEntity1 te1 = null;
        if (testRootOptional.isPresent()) {
            Optional<TestEntity_1Record> testEntity1Optional = dslContext
                .fetchOptional(Tables.TEST_ENTITY_1, Tables.TEST_ENTITY_1.TEST_ROOT_ID.equal(id));

            if (testEntity1Optional.isPresent()) {
                TestEntity2 testEntity2A = fetchSubTreeEntity2(testEntity1Optional.get().getTestEntity_2IdA());
                TestEntity2 testEntity2B = fetchSubTreeEntity2(testEntity1Optional.get().getTestEntity_2IdB());
                RecordMapper<TestEntity_1Record, TestEntity1, ?> mapper1 = (RecordMapper<TestEntity_1Record, TestEntity1, ?>) domainPersistenceProvider
                    .persistenceMirror
                        .getEntityRecordMapper(TestEntity1.class.getName());

                DomainObjectBuilder<TestEntity1> b = testEntity1Optional.map(r -> mapper1.recordToDomainObjectBuilder(r)).get();
                b.setFieldValue(testEntity2A, "testEntity2A");
                b.setFieldValue(testEntity2B, "testEntity2B");
                te1 = b.build();
            }
            RecordMapper<TestRootRecord, TestRoot, ?> mapper = (RecordMapper<TestRootRecord, TestRoot, ?>) domainPersistenceProvider
                .persistenceMirror
                .getEntityRecordMapper(TestRoot.class.getName());
            DomainObjectBuilder<TestRoot> b2 = testRootOptional
                .map(r ->
                    mapper.recordToDomainObjectBuilder(r)).get();
            b2.setFieldValue(te1, "testEntity1");
            return b2.build();
        }
        return null;
    }

    private TestEntity2 fetchSubTreeEntity2(Long testEntit2Id) {
        Optional<TestEntity_2Record> testEntity2 = dslContext.fetchOptional(Tables.TEST_ENTITY_2,
            Tables.TEST_ENTITY_2.ID.equal(testEntit2Id));
        if (testEntity2.isPresent()) {
            RecordMapper<TestEntity_2Record, TestEntity2, ?> mapper = (RecordMapper<TestEntity_2Record, TestEntity2, ?>) domainPersistenceProvider
                .persistenceMirror
                    .getEntityRecordMapper(TestEntity2.class.getName());
            DomainObjectBuilder<TestEntity2> b = testEntity2
                .map(r -> mapper.recordToDomainObjectBuilder(r))
                .get();

            List<TestEntity_3Record> testEntity3List = dslContext
                .fetch(Tables.TEST_ENTITY_3, Tables.TEST_ENTITY_3.TEST_ENTITY_2_ID.equal(testEntit2Id));

            for (TestEntity_3Record te3 : testEntity3List) {
                TestEntity3 te3Entity = fetchSubTreeEntity3(te3);
                b.addValueToCollection(te3Entity, "testEntity3List");
            }
            TestEntity2 te2 = b.build();
            return te2;
        }
        return null;
    }

    private TestEntity3 fetchSubTreeEntity3(TestEntity_3Record te3) {
        List<TestEntity_4Record> testEntity4List = dslContext.fetch(Tables.TEST_ENTITY_4,
            Tables.TEST_ENTITY_4.TEST_ENTITY_3_ID.equal(te3.getId()));
        RecordMapper<TestEntity_3Record, TestEntity3, ?> mapper = (RecordMapper<TestEntity_3Record, TestEntity3, ?>) domainPersistenceProvider
            .persistenceMirror
            .getEntityRecordMapper(TestEntity3.class.getName());
        DomainObjectBuilder<TestEntity3> b = mapper.recordToDomainObjectBuilder(te3);
        for (TestEntity_4Record te4 : testEntity4List) {
            TestEntity4 te4Entity = fetchSubTreeEntity4(te4);
            b.addValueToCollection(te4Entity, "testEntity4List");
        }
        TestEntity3 te3Entity = b.build();


        return te3Entity;
    }

    private TestEntity4 fetchSubTreeEntity4(TestEntity_4Record te4) {
        List<TestEntity_5Record> testEntity5List = dslContext.fetch(Tables.TEST_ENTITY_5,
            Tables.TEST_ENTITY_5.TEST_ENTITY_4_ID.equal(te4.getId()));
        RecordMapper<TestEntity_4Record, TestEntity4, ?> mapper = (RecordMapper<TestEntity_4Record, TestEntity4, ?>) domainPersistenceProvider
            .persistenceMirror
            .getEntityRecordMapper(TestEntity4.class.getName());
        DomainObjectBuilder<TestEntity4> b = mapper.recordToDomainObjectBuilder(te4);

        for (TestEntity_5Record te5 : testEntity5List) {
            TestEntity5 te5Entity = fetchSubTreeEntity5(te5);
            b.addValueToCollection(te5Entity, "testEntity5List");
        }
        TestEntity4 te4Entity = b.build();

        return te4Entity;
    }

    private TestEntity5 fetchSubTreeEntity5(TestEntity_5Record te5) {
        Optional<TestEntity_6Record> testEntity_6RecordOptional = dslContext.fetchOptional(Tables.TEST_ENTITY_6,
            Tables.TEST_ENTITY_6.ID.equal(te5.getTestEntity_6Id()));
        RecordMapper<TestEntity_5Record, TestEntity5, ?> mapper5 = (RecordMapper<TestEntity_5Record, TestEntity5, ?>) domainPersistenceProvider
            .persistenceMirror
            .getEntityRecordMapper(TestEntity5.class.getName());
        DomainObjectBuilder<TestEntity5> b = mapper5.recordToDomainObjectBuilder(te5);
        if (testEntity_6RecordOptional.isPresent()) {
            RecordMapper<TestEntity_6Record, TestEntity6, ?> mapper6 = (RecordMapper<TestEntity_6Record, TestEntity6, ?>) domainPersistenceProvider
                .persistenceMirror
                .getEntityRecordMapper(TestEntity6.class.getName());

            b.setFieldValue(testEntity_6RecordOptional.map(
                r -> mapper6.recordToDomainObjectBuilder(r).build()).get(), "testEntity6");
        }
        return b.build();
    }

    @Override
    public FetcherResult<TestRoot, UpdatableRecord<?>> findResultById(TestRootId rootId) {
        return simpleAggregateFetcher.fetchDeep(rootId);
    }

}
