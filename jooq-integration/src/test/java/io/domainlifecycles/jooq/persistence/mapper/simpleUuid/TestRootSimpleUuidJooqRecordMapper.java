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

package io.domainlifecycles.jooq.persistence.mapper.simpleUuid;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.test.Tables;
import io.domainlifecycles.test.tables.records.TestRootSimpleUuidRecord;
import tests.shared.persistence.domain.simpleUuid.TestRootSimpleUuid;
import tests.shared.persistence.domain.simpleUuid.TestRootSimpleUuidId;

import java.util.UUID;

/**
 * Mapping JOOQ TestRootSimpleIgnoring records.
 */
public class TestRootSimpleUuidJooqRecordMapper extends AbstractRecordMapper<TestRootSimpleUuidRecord, TestRootSimpleUuid, TestRootSimpleUuid> {

    @Override
    public DomainObjectBuilder<TestRootSimpleUuid> recordToDomainObjectBuilder(TestRootSimpleUuidRecord record) {
        if (record == null) {
            return null;
        }
        TestRootSimpleUuidRecord testRootSimpleRecord = record.into(Tables.TEST_ROOT_SIMPLE_UUID);
        return new InnerClassDomainObjectBuilder<>(TestRootSimpleUuid.builder()
            .setId(new TestRootSimpleUuidId(UUID.fromString(testRootSimpleRecord.getId())))
            .setName(testRootSimpleRecord.getName())
            .setConcurrencyVersion(testRootSimpleRecord.getConcurrencyVersion()));
    }

    @Override
    public TestRootSimpleUuidRecord from(TestRootSimpleUuid testRootSimpleUuid, TestRootSimpleUuid root) {
        TestRootSimpleUuidRecord testRootSimpleRecord = new TestRootSimpleUuidRecord();
        testRootSimpleRecord.setId(testRootSimpleUuid.getId().value().toString());
        testRootSimpleRecord.setName(testRootSimpleUuid.getName());
        testRootSimpleRecord.setConcurrencyVersion(testRootSimpleUuid.concurrencyVersion());
        return testRootSimpleRecord;
    }

    @Override
    public Class<TestRootSimpleUuid> domainObjectType() {
        return TestRootSimpleUuid.class;
    }

    @Override
    public Class<TestRootSimpleUuidRecord> recordType() {
        return TestRootSimpleUuidRecord.class;
    }
}
