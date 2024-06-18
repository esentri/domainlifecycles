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

package io.domainlifecycles.jooq.persistence.mapper.simple;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.test.Tables;
import io.domainlifecycles.test.tables.records.TestRootSimpleRecord;
import tests.shared.persistence.domain.simple.TestRootSimple;
import tests.shared.persistence.domain.simple.TestRootSimpleId;

/**
 * Mapping JOOQ TestRootSimpleIgnoring records.
 */
public class TestRootSimpleJooqRecordMapper extends AbstractRecordMapper<TestRootSimpleRecord, TestRootSimple, TestRootSimple> {

    @Override
    public DomainObjectBuilder<TestRootSimple> recordToDomainObjectBuilder(TestRootSimpleRecord record) {
        if (record == null) {
            return null;
        }
        TestRootSimpleRecord testRootSimpleRecord = record.into(Tables.TEST_ROOT_SIMPLE);
        return new InnerClassDomainObjectBuilder<>(TestRootSimple.builder()
            .setId(new TestRootSimpleId(testRootSimpleRecord.getId()))
            .setName(testRootSimpleRecord.getName())
            .setConcurrencyVersion(testRootSimpleRecord.getConcurrencyVersion()));
    }

    @Override
    public TestRootSimpleRecord from(TestRootSimple testRootSimple, TestRootSimple root) {
        TestRootSimpleRecord testRootSimpleRecord = new TestRootSimpleRecord();
        testRootSimpleRecord.setId(testRootSimple.getId().value());
        testRootSimpleRecord.setName(testRootSimple.getName());
        testRootSimpleRecord.setConcurrencyVersion(testRootSimple.concurrencyVersion());
        return testRootSimpleRecord;
    }

    @Override
    public Class<TestRootSimple> domainObjectType() {
        return TestRootSimple.class;
    }

    @Override
    public Class<TestRootSimpleRecord> recordType() {
        return TestRootSimpleRecord.class;
    }
}
