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

package nitrox.dlc.jooq.persistence.mapper.oneToOneLeadingFK;

import nitrox.dlc.builder.DomainObjectBuilder;
import nitrox.dlc.builder.innerclass.InnerClassDomainObjectBuilder;
import nitrox.dlc.persistence.mapping.AbstractRecordMapper;
import nitrox.dlc.test.Tables;
import nitrox.dlc.test.tables.records.TestRootOneToOneLeadingRecord;
import tests.shared.persistence.domain.oneToOneLeadingFK.TestRootOneToOneLeading;
import tests.shared.persistence.domain.oneToOneLeadingFK.TestRootOneToOneLeadingId;

/**
 * Mapping JOOQ TestRootOneToOneLeading records.
 */
public class TestRootOneToOneLeadingJooqRecordMapper extends AbstractRecordMapper<TestRootOneToOneLeadingRecord, TestRootOneToOneLeading, TestRootOneToOneLeading> {

    @Override
    public DomainObjectBuilder<TestRootOneToOneLeading> recordToDomainObjectBuilder(TestRootOneToOneLeadingRecord record) {
        if (record == null) {
            return null;
        }
        TestRootOneToOneLeadingRecord testRootOneToOneLeadingRecord = record.into(Tables.TEST_ROOT_ONE_TO_ONE_LEADING);
        return new InnerClassDomainObjectBuilder<>(TestRootOneToOneLeading.builder()
            .setId(new TestRootOneToOneLeadingId(testRootOneToOneLeadingRecord.getId()))
            .setName(testRootOneToOneLeadingRecord.getName())
            .setConcurrencyVersion(testRootOneToOneLeadingRecord.getConcurrencyVersion()));
    }

    @Override
    public TestRootOneToOneLeadingRecord from(TestRootOneToOneLeading testRootOneToOneLeading, TestRootOneToOneLeading root) {
        TestRootOneToOneLeadingRecord testRootOneToOneLeadingRecord = new TestRootOneToOneLeadingRecord();
        testRootOneToOneLeadingRecord.setId(testRootOneToOneLeading.getId().value());
        testRootOneToOneLeadingRecord.setTestEntityId(testRootOneToOneLeading.getTestEntityOneToOneLeading() == null
            ? null : testRootOneToOneLeading.getTestEntityOneToOneLeading().getId().value());
        testRootOneToOneLeadingRecord.setName(testRootOneToOneLeading.getName());
        testRootOneToOneLeadingRecord.setConcurrencyVersion(testRootOneToOneLeading.concurrencyVersion());
        return testRootOneToOneLeadingRecord;
    }

    @Override
    public Class<TestRootOneToOneLeading> domainObjectType() {
        return TestRootOneToOneLeading.class;
    }

    @Override
    public Class<TestRootOneToOneLeadingRecord> recordType() {
        return TestRootOneToOneLeadingRecord.class;
    }
}
