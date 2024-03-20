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
import nitrox.dlc.test.tables.records.TestEntityManyToManyBRecord;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestEntityManyToManyB;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestEntityManyToManyBId;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestRootManyToMany;

import nitrox.dlc.test.Tables;


/**
 * Mapping JOOQ TestEntityManyToManyA records.
 */
public class TestManyToManyBJooqRecordMapper extends AbstractRecordMapper<TestEntityManyToManyBRecord, TestEntityManyToManyB, TestRootManyToMany> {

    @Override
    public DomainObjectBuilder<TestEntityManyToManyB> recordToDomainObjectBuilder(TestEntityManyToManyBRecord record) {
        if (record == null) {
            return null;
        }
        TestEntityManyToManyBRecord testEntityManyToManyBRecord = record.into(Tables.TEST_ENTITY_MANY_TO_MANY_B);
        return new InnerClassDomainObjectBuilder<TestEntityManyToManyB>(TestEntityManyToManyB.builder()
            .setId(new TestEntityManyToManyBId(testEntityManyToManyBRecord.getId()))
            .setName(testEntityManyToManyBRecord.getName())
            .setConcurrencyVersion(testEntityManyToManyBRecord.getConcurrencyVersion()));
    }

    @Override
    public TestEntityManyToManyBRecord from(TestEntityManyToManyB testEntityManyToManyB, TestRootManyToMany root) {
        TestEntityManyToManyBRecord testEntityManyToManyBRecord = new TestEntityManyToManyBRecord();
        testEntityManyToManyBRecord.setId(testEntityManyToManyB.getId().value());
        testEntityManyToManyBRecord.setName(testEntityManyToManyB.getName());
        testEntityManyToManyBRecord.setConcurrencyVersion(testEntityManyToManyB.concurrencyVersion());
        return testEntityManyToManyBRecord;
    }

    @Override
    public Class<TestEntityManyToManyB> domainObjectType() {
        return TestEntityManyToManyB.class;
    }

    @Override
    public Class<TestEntityManyToManyBRecord> recordType() {
        return TestEntityManyToManyBRecord.class;
    }
}
