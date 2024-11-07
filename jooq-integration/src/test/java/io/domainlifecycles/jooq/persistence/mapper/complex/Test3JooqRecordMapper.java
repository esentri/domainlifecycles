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

package io.domainlifecycles.jooq.persistence.mapper.complex;


import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.test.Tables;
import io.domainlifecycles.test.tables.records.TestEntity_3Record;
import tests.shared.persistence.domain.complex.TestEntity2Id;
import tests.shared.persistence.domain.complex.TestEntity3;
import tests.shared.persistence.domain.complex.TestEntity3Id;
import tests.shared.persistence.domain.complex.TestRoot;
import tests.shared.persistence.domain.complex.TestRootId;


/**
 * Mapping JOOQ Testroot records.
 */
public class Test3JooqRecordMapper extends AbstractRecordMapper<TestEntity_3Record, TestEntity3, TestRoot> {

    @Override
    public DomainObjectBuilder<TestEntity3> recordToDomainObjectBuilder(TestEntity_3Record record) {
        if (record == null) {
            return null;
        }
        TestEntity_3Record testEntity_3Record = record.into(Tables.TEST_ENTITY_3);
        return new InnerClassDomainObjectBuilder<>(TestEntity3.builder()
            .setId(new TestEntity3Id(testEntity_3Record.getId()))
            .setTestRootId(new TestRootId(testEntity_3Record.getTestRootId()))
            .setTestEntity2Id(new TestEntity2Id(testEntity_3Record.getTestEntity_2Id()))
            .setName(testEntity_3Record.getName())
            .setConcurrencyVersion(testEntity_3Record.getConcurrencyVersion()));
    }

    @Override
    public TestEntity_3Record from(TestEntity3 testEntity3, TestRoot root) {
        TestEntity_3Record testEntity_3Record = new TestEntity_3Record();
        testEntity_3Record.setTestRootId(testEntity3.getTestRootId().value());
        testEntity_3Record.setId(testEntity3.getId().value());
        testEntity_3Record.setName(testEntity3.getName());
        testEntity_3Record.setTestEntity_2Id(
            testEntity3.getTestEntity2Id() == null ? null : testEntity3.getTestEntity2Id().value());
        testEntity_3Record.setConcurrencyVersion(testEntity3.concurrencyVersion());
        return testEntity_3Record;
    }

    @Override
    public Class<TestEntity3> domainObjectType() {
        return TestEntity3.class;
    }

    @Override
    public Class<TestEntity_3Record> recordType() {
        return TestEntity_3Record.class;
    }
}
