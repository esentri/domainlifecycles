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

import lombok.RequiredArgsConstructor;
import nitrox.dlc.builder.DomainObjectBuilder;
import nitrox.dlc.builder.innerclass.InnerClassDomainObjectBuilder;
import nitrox.dlc.persistence.mapping.AbstractRecordMapper;
import nitrox.dlc.test.Tables;
import nitrox.dlc.test.tables.records.TestEntity_1Record;
import tests.shared.persistence.domain.complex.TestEntity1;
import tests.shared.persistence.domain.complex.TestEntity1Id;
import tests.shared.persistence.domain.complex.TestRoot;
import tests.shared.persistence.domain.complex.TestRootId;

/**
 * Mapping JOOQ Testroot records.
 */
@RequiredArgsConstructor
public class Test1JooqRecordMapper extends AbstractRecordMapper<TestEntity_1Record, TestEntity1, TestRoot> {

    @Override
    public DomainObjectBuilder<TestEntity1> recordToDomainObjectBuilder(TestEntity_1Record record) {
        if (record == null) {
            return null;
        }
        TestEntity_1Record testEntity_1Record = record.into(Tables.TEST_ENTITY_1);
        return new InnerClassDomainObjectBuilder<TestEntity1>(TestEntity1.builder()
            .setId(new TestEntity1Id(testEntity_1Record.getId()))
            .setTestRootId(new TestRootId(testEntity_1Record.getTestRootId()))
            .setName(testEntity_1Record.getName())
            .setConcurrencyVersion(testEntity_1Record.getConcurrencyVersion()));
    }

    @Override
    public TestEntity_1Record from(TestEntity1 testEntity1, TestRoot root) {
        TestEntity_1Record testEntity_1Record = new TestEntity_1Record();
        testEntity_1Record.setTestRootId(testEntity1.getTestRootId().value());
        testEntity_1Record.setId(testEntity1.getId().value());
        testEntity_1Record.setName(testEntity1.getName());
        testEntity_1Record.setTestEntity_2IdA(testEntity1.getTestEntity2A() != null ? testEntity1.getTestEntity2A().getId().value() : null);
        testEntity_1Record.setTestEntity_2IdB(testEntity1.getTestEntity2B() != null ? testEntity1.getTestEntity2B().getId().value() : null);
        testEntity_1Record.setConcurrencyVersion(testEntity1.concurrencyVersion());
        return testEntity_1Record;
    }

    @Override
    public Class<TestEntity1> domainObjectType() {
        return TestEntity1.class;
    }

    @Override
    public Class<TestEntity_1Record> recordType() {
        return TestEntity_1Record.class;
    }
}
