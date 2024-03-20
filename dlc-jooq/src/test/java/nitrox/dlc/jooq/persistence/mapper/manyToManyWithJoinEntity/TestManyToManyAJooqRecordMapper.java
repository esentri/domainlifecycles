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

package nitrox.dlc.jooq.persistence.mapper.manyToManyWithJoinEntity;


import nitrox.dlc.builder.DomainObjectBuilder;
import nitrox.dlc.builder.innerclass.InnerClassDomainObjectBuilder;
import nitrox.dlc.persistence.mapping.AbstractRecordMapper;
import nitrox.dlc.test.tables.records.TestEntityManyToManyARecord;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestEntityManyToManyA;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestEntityManyToManyAId;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestRootManyToMany;

import nitrox.dlc.test.Tables;


/**
 * Mapping JOOQ TestEntityManyToManyA records.
 */
public class TestManyToManyAJooqRecordMapper extends AbstractRecordMapper<TestEntityManyToManyARecord, TestEntityManyToManyA, TestRootManyToMany> {

    @Override
    public DomainObjectBuilder<TestEntityManyToManyA> recordToDomainObjectBuilder(TestEntityManyToManyARecord record) {
        if (record == null) {
            return null;
        }
        TestEntityManyToManyARecord testEntityManyToManyARecord = record.into(Tables.TEST_ENTITY_MANY_TO_MANY_A);
        return new InnerClassDomainObjectBuilder<>(TestEntityManyToManyA.builder()
            .setId(new TestEntityManyToManyAId(testEntityManyToManyARecord.getId()))
            .setName(testEntityManyToManyARecord.getName())
            .setConcurrencyVersion(testEntityManyToManyARecord.getConcurrencyVersion()));
    }

    @Override
    public TestEntityManyToManyARecord from(TestEntityManyToManyA testEntityManyToManyA, TestRootManyToMany root) {
        TestEntityManyToManyARecord testEntityManyToManyARecord = new TestEntityManyToManyARecord();
        testEntityManyToManyARecord.setId(testEntityManyToManyA.getId().value());
        testEntityManyToManyARecord.setTestRootId(testEntityManyToManyA.getTestRootManyToMany().getId().value());
        testEntityManyToManyARecord.setName(testEntityManyToManyA.getName());
        testEntityManyToManyARecord.setConcurrencyVersion(testEntityManyToManyA.concurrencyVersion());
        return testEntityManyToManyARecord;
    }

    @Override
    public Class<TestEntityManyToManyA> domainObjectType() {
        return TestEntityManyToManyA.class;
    }

    @Override
    public Class<TestEntityManyToManyARecord> recordType() {
        return TestEntityManyToManyARecord.class;
    }
}
