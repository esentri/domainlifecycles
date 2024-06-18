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
import io.domainlifecycles.test.tables.records.TestRootManyToManyRecord;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestRootManyToMany;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestRootManyToManyId;

import io.domainlifecycles.test.Tables;

/**
 * Mapping JOOQ TestRootManyToMany records.
 */
public class TestRootManyToManyJooqRecordMapper extends AbstractRecordMapper<TestRootManyToManyRecord, TestRootManyToMany, TestRootManyToMany> {

    @Override
    public DomainObjectBuilder<TestRootManyToMany> recordToDomainObjectBuilder(TestRootManyToManyRecord record) {
        if (record == null) {
            return null;
        }
        TestRootManyToManyRecord testRootManyToManyRecord = record.into(Tables.TEST_ROOT_MANY_TO_MANY);
        return new InnerClassDomainObjectBuilder<>(TestRootManyToMany.builder()
            .setId(new TestRootManyToManyId(testRootManyToManyRecord.getId()))
            .setName(testRootManyToManyRecord.getName())
            .setConcurrencyVersion(testRootManyToManyRecord.getConcurrencyVersion()));
    }

    @Override
    public TestRootManyToManyRecord from(TestRootManyToMany testRootManyToMany, TestRootManyToMany root) {
        TestRootManyToManyRecord testRootManyToManyRecord = new TestRootManyToManyRecord();
        testRootManyToManyRecord.setId(testRootManyToMany.getId().value());
        testRootManyToManyRecord.setName(testRootManyToMany.getName());
        testRootManyToManyRecord.setConcurrencyVersion(testRootManyToMany.concurrencyVersion());
        return testRootManyToManyRecord;
    }

    @Override
    public Class<TestRootManyToMany> domainObjectType() {
        return TestRootManyToMany.class;
    }

    @Override
    public Class<TestRootManyToManyRecord> recordType() {
        return TestRootManyToManyRecord.class;
    }
}
