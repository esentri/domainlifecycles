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

package io.domainlifecycles.jooq.persistence.mapper.oneToMany;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.test.Tables;
import io.domainlifecycles.test.tables.records.TestEntityOneToManyRecord;
import tests.shared.persistence.domain.oneToMany.TestEntityOneToMany;
import tests.shared.persistence.domain.oneToMany.TestEntityOneToManyId;
import tests.shared.persistence.domain.oneToMany.TestRootOneToMany;
import tests.shared.persistence.domain.oneToMany.TestRootOneToManyId;

/**
 * Mapping JOOQ TestEntityOneToOneFollowing records.
 */
public class TestOneToManyJooqRecordMapper extends AbstractRecordMapper<TestEntityOneToManyRecord, TestEntityOneToMany, TestRootOneToMany> {

    @Override
    public DomainObjectBuilder<TestEntityOneToMany> recordToDomainObjectBuilder(TestEntityOneToManyRecord record) {
        if (record == null) {
            return null;
        }
        TestEntityOneToManyRecord testEntityOneToManyRecord = record.into(Tables.TEST_ENTITY_ONE_TO_MANY);
        return new InnerClassDomainObjectBuilder<>(TestEntityOneToMany.builder()
            .setId(new TestEntityOneToManyId(testEntityOneToManyRecord.getId()))
            .setName(testEntityOneToManyRecord.getName())
            .setTestRootId(testEntityOneToManyRecord.getTestRootId() == null ?
                null : new TestRootOneToManyId(testEntityOneToManyRecord.getTestRootId()))
            .setConcurrencyVersion(testEntityOneToManyRecord.getConcurrencyVersion()));
    }

    @Override
    public TestEntityOneToManyRecord from(TestEntityOneToMany testEntityOneToMany, TestRootOneToMany root) {
        TestEntityOneToManyRecord testEntityOneToManyRecord = new TestEntityOneToManyRecord();
        testEntityOneToManyRecord.setId(testEntityOneToMany.getId().value());
        testEntityOneToManyRecord.setName(testEntityOneToMany.getName());
        testEntityOneToManyRecord.setTestRootId(testEntityOneToMany.getTestRootId() == null ?
            null : testEntityOneToMany.getTestRootId().value());
        testEntityOneToManyRecord.setConcurrencyVersion(testEntityOneToMany.concurrencyVersion());
        return testEntityOneToManyRecord;
    }

    @Override
    public Class<TestEntityOneToMany> domainObjectType() {
        return TestEntityOneToMany.class;
    }

    @Override
    public Class<TestEntityOneToManyRecord> recordType() {
        return TestEntityOneToManyRecord.class;
    }
}
