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

package nitrox.dlc.jooq.persistence.mapper.complex;

import nitrox.dlc.builder.DomainObjectBuilder;
import nitrox.dlc.builder.innerclass.InnerClassDomainObjectBuilder;
import nitrox.dlc.persistence.mapping.AbstractRecordMapper;
import nitrox.dlc.test.Tables;
import nitrox.dlc.test.tables.records.TestEntity_2Record;
import tests.shared.persistence.domain.complex.TestEntity2;
import tests.shared.persistence.domain.complex.TestEntity2Id;
import tests.shared.persistence.domain.complex.TestRoot;
import tests.shared.persistence.domain.complex.TestRootId;


/**
 * Mapping JOOQ Testroot records.
 */
public class Test2JooqRecordMapper extends AbstractRecordMapper<TestEntity_2Record, TestEntity2, TestRoot> {

    @Override
    public DomainObjectBuilder<TestEntity2> recordToDomainObjectBuilder(TestEntity_2Record record) {
        if (record == null) {
            return null;
        }
        TestEntity_2Record testEntity_2Record = record.into(Tables.TEST_ENTITY_2);
        return new InnerClassDomainObjectBuilder<TestEntity2>(TestEntity2.builder()
            .setId(new TestEntity2Id(testEntity_2Record.getId()))
            .setTestRootId(new TestRootId(testEntity_2Record.getTestRootId()))
            .setName(testEntity_2Record.getName())
            .setConcurrencyVersion(testEntity_2Record.getConcurrencyVersion()));
    }

    @Override
    public TestEntity_2Record from(TestEntity2 testEntity2, TestRoot root) {
        TestEntity_2Record testEntity_2Record = new TestEntity_2Record();
        testEntity_2Record.setTestRootId(testEntity2.getTestRootId().value());
        testEntity_2Record.setId(testEntity2.getId().value());
        testEntity_2Record.setName(testEntity2.getName());
        testEntity_2Record.setConcurrencyVersion(testEntity2.concurrencyVersion());
        return testEntity_2Record;
    }

    @Override
    public Class<TestEntity2> domainObjectType() {
        return TestEntity2.class;
    }

    @Override
    public Class<TestEntity_2Record> recordType() {
        return TestEntity_2Record.class;
    }
}