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
import nitrox.dlc.test.tables.records.TestEntity_4Record;
import tests.shared.persistence.domain.complex.TestEntity3Id;
import tests.shared.persistence.domain.complex.TestEntity4;
import tests.shared.persistence.domain.complex.TestEntity4Id;
import tests.shared.persistence.domain.complex.TestRoot;


/**
 * Mapping JOOQ Testroot records.
 */
public class Test4JooqRecordMapper extends AbstractRecordMapper<TestEntity_4Record, TestEntity4, TestRoot> {

    @Override
    public DomainObjectBuilder<TestEntity4> recordToDomainObjectBuilder(TestEntity_4Record record) {
        if (record == null) {
            return null;
        }
        TestEntity_4Record testEntity_4Record = record.into(Tables.TEST_ENTITY_4);
        return new InnerClassDomainObjectBuilder<>(TestEntity4.builder()
            .setId(new TestEntity4Id(testEntity_4Record.getId()))
            .setTestEntity3Id(new TestEntity3Id(testEntity_4Record.getTestEntity_3Id()))
            .setName(testEntity_4Record.getName())
            .setConcurrencyVersion(testEntity_4Record.getConcurrencyVersion()));

    }

    @Override
    public TestEntity_4Record from(TestEntity4 testEntity4, TestRoot root) {
        TestEntity_4Record testEntity_4Record = new TestEntity_4Record();
        testEntity_4Record.setId(testEntity4.getId().value());
        testEntity_4Record.setName(testEntity4.getName());
        testEntity_4Record.setTestEntity_3Id(testEntity4.getTestEntity3Id().value());
        testEntity_4Record.setConcurrencyVersion(testEntity4.concurrencyVersion());
        return testEntity_4Record;
    }

    @Override
    public Class<TestEntity4> domainObjectType() {
        return TestEntity4.class;
    }

    @Override
    public Class<TestEntity_4Record> recordType() {
        return TestEntity_4Record.class;
    }
}
