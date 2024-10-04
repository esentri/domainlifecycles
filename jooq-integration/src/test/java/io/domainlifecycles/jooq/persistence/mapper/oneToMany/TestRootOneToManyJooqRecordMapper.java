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
import io.domainlifecycles.test.tables.records.TestRootOneToManyRecord;
import tests.shared.persistence.domain.oneToMany.TestRootOneToMany;
import tests.shared.persistence.domain.oneToMany.TestRootOneToManyId;

/**
 * Mapping JOOQ TestRootOneToOneFollowing records.
 */
public class TestRootOneToManyJooqRecordMapper extends AbstractRecordMapper<TestRootOneToManyRecord,
    TestRootOneToMany, TestRootOneToMany> {

    @Override
    public DomainObjectBuilder<TestRootOneToMany> recordToDomainObjectBuilder(TestRootOneToManyRecord record) {
        if (record == null) {
            return null;
        }
        TestRootOneToManyRecord testRootOneToManyRecord = record.into(Tables.TEST_ROOT_ONE_TO_MANY);
        return new InnerClassDomainObjectBuilder<>(TestRootOneToMany.builder()
            .setId(new TestRootOneToManyId(testRootOneToManyRecord.getId()))
            .setName(testRootOneToManyRecord.getName())
            .setConcurrencyVersion(testRootOneToManyRecord.getConcurrencyVersion()));
    }

    @Override
    public TestRootOneToManyRecord from(TestRootOneToMany testRootOneToMany, TestRootOneToMany root) {
        TestRootOneToManyRecord testRootOneToManyRecord = new TestRootOneToManyRecord();
        testRootOneToManyRecord.setId(testRootOneToMany.getId().value());
        testRootOneToManyRecord.setName(testRootOneToMany.getName());
        testRootOneToManyRecord.setConcurrencyVersion(testRootOneToMany.concurrencyVersion());
        return testRootOneToManyRecord;
    }

    @Override
    public Class<TestRootOneToMany> domainObjectType() {
        return TestRootOneToMany.class;
    }

    @Override
    public Class<TestRootOneToManyRecord> recordType() {
        return TestRootOneToManyRecord.class;
    }
}
