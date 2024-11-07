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
import io.domainlifecycles.test.tables.records.TestEntity_5Record;
import tests.shared.persistence.domain.complex.TestEntity4Id;
import tests.shared.persistence.domain.complex.TestEntity5;
import tests.shared.persistence.domain.complex.TestEntity5Id;
import tests.shared.persistence.domain.complex.TestRoot;
import tests.shared.persistence.domain.complex.TestRootId;


/**
 * Mapping JOOQ Testroot records.
 */
public class Test5JooqRecordMapper extends AbstractRecordMapper<TestEntity_5Record, TestEntity5, TestRoot> {

    @Override
    public DomainObjectBuilder<TestEntity5> recordToDomainObjectBuilder(TestEntity_5Record record) {
        if (record == null) {
            return null;
        }
        TestEntity_5Record testEntity_5Record = record.into(Tables.TEST_ENTITY_5);
        return new InnerClassDomainObjectBuilder<TestEntity5>(TestEntity5.builder()
            .setId(new TestEntity5Id(testEntity_5Record.getId()))
            .setTestEntity4Id(new TestEntity4Id(testEntity_5Record.getTestEntity_4Id()))
            .setTestRootId(new TestRootId(testEntity_5Record.getTestRootId()))
            .setName(testEntity_5Record.getName())
            .setConcurrencyVersion(testEntity_5Record.getConcurrencyVersion()));
    }

    @Override
    public TestEntity_5Record from(TestEntity5 testEntity5, TestRoot root) {
        TestEntity_5Record testEntity_5Record = new TestEntity_5Record();
        testEntity_5Record.setId(testEntity5.getId().value());
        testEntity_5Record.setName(testEntity5.getName());
        testEntity_5Record.setTestEntity_4Id(testEntity5.getTestEntity4Id().value());
        testEntity_5Record.setTestRootId(testEntity5.getTestRootId().value());
        testEntity_5Record.setTestEntity_6Id(
            testEntity5.getTestEntity6() != null ? testEntity5.getTestEntity6().getId().value() : null);
        testEntity_5Record.setConcurrencyVersion(testEntity5.concurrencyVersion());
        return testEntity_5Record;
    }

    @Override
    public Class<TestEntity5> domainObjectType() {
        return TestEntity5.class;
    }

    @Override
    public Class<TestEntity_5Record> recordType() {
        return TestEntity_5Record.class;
    }
}
