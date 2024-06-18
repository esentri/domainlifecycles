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

package io.domainlifecycles.jooq.persistence.mapper.complex;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.test.Tables;
import io.domainlifecycles.test.tables.records.TestEntity_6Record;
import tests.shared.persistence.domain.complex.TestEntity6;
import tests.shared.persistence.domain.complex.TestEntity6Id;
import tests.shared.persistence.domain.complex.TestRoot;


/**
 * Mapping JOOQ Testroot records.
 */
public class Test6JooqRecordMapper extends AbstractRecordMapper<TestEntity_6Record, TestEntity6, TestRoot> {

    @Override
    public DomainObjectBuilder<TestEntity6> recordToDomainObjectBuilder(TestEntity_6Record record) {
        if (record == null) {
            return null;
        }
        TestEntity_6Record testEntity_6Record = record.into(Tables.TEST_ENTITY_6);
        return new InnerClassDomainObjectBuilder<>(TestEntity6.builder()
            .setId(new TestEntity6Id(testEntity_6Record.getId()))
            .setName(testEntity_6Record.getName())
            .setConcurrencyVersion(testEntity_6Record.getConcurrencyVersion()));
    }

    @Override
    public TestEntity_6Record from(TestEntity6 testEntity6, TestRoot root) {
        TestEntity_6Record testEntity_6Record = new TestEntity_6Record();
        testEntity_6Record.setId(testEntity6.getId().value());
        testEntity_6Record.setName(testEntity6.getName());
        testEntity_6Record.setConcurrencyVersion(testEntity6.concurrencyVersion());
        return testEntity_6Record;
    }

    @Override
    public Class<TestEntity6> domainObjectType() {
        return TestEntity6.class;
    }

    @Override
    public Class<TestEntity_6Record> recordType() {
        return TestEntity_6Record.class;
    }
}
