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

package io.domainlifecycles.jooq.persistence.mapper.manyToManyWithJoinEntity;


import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.test.tables.records.TestEntityManyToManyJoinRecord;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestEntityManyToManyAId;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestEntityManyToManyJoin;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestEntityManyToManyJoinId;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestRootManyToMany;

import io.domainlifecycles.test.Tables;


/**
 * Mapping JOOQ TestEntityManyToManyA records.
 */
public class TestManyToManyJoinJooqRecordMapper extends AbstractRecordMapper<TestEntityManyToManyJoinRecord, TestEntityManyToManyJoin, TestRootManyToMany> {

    @Override
    public DomainObjectBuilder<TestEntityManyToManyJoin> recordToDomainObjectBuilder(TestEntityManyToManyJoinRecord record) {
        if (record == null) {
            return null;
        }
        TestEntityManyToManyJoinRecord testEntityManyToManyARecord = record.into(Tables.TEST_ENTITY_MANY_TO_MANY_JOIN);
        return new InnerClassDomainObjectBuilder<>(TestEntityManyToManyJoin.builder()
            .setId(new TestEntityManyToManyJoinId(testEntityManyToManyARecord.getId()))
            .setTestEntityManyToManyAId(new TestEntityManyToManyAId(testEntityManyToManyARecord.getTestEntityAId()))
            .setConcurrencyVersion(testEntityManyToManyARecord.getConcurrencyVersion()));
    }

    @Override
    public TestEntityManyToManyJoinRecord from(TestEntityManyToManyJoin testEntityManyToManyJoin, TestRootManyToMany root) {
        TestEntityManyToManyJoinRecord testEntityManyToManyJoinRecord = new TestEntityManyToManyJoinRecord();
        testEntityManyToManyJoinRecord.setId(testEntityManyToManyJoin.getId().value());
        testEntityManyToManyJoinRecord.setTestEntityAId(testEntityManyToManyJoin.getTestEntityManyToManyAId().value());
        testEntityManyToManyJoinRecord.setTestEntityBId(testEntityManyToManyJoin.getTestEntityManyToManyB().getId().value());
        testEntityManyToManyJoinRecord.setConcurrencyVersion(testEntityManyToManyJoin.concurrencyVersion());
        return testEntityManyToManyJoinRecord;
    }

    @Override
    public Class<TestEntityManyToManyJoin> domainObjectType() {
        return TestEntityManyToManyJoin.class;
    }

    @Override
    public Class<TestEntityManyToManyJoinRecord> recordType() {
        return TestEntityManyToManyJoinRecord.class;
    }
}
