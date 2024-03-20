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
import nitrox.dlc.test.tables.records.TestEntityOneToOneLeadingRecord;
import tests.shared.persistence.domain.oneToOneLeadingFK.TestEntityOneToOneLeading;
import tests.shared.persistence.domain.oneToOneLeadingFK.TestEntityOneToOneLeadingId;
import tests.shared.persistence.domain.oneToOneLeadingFK.TestRootOneToOneLeading;

/**
 * Mapping JOOQ TestEntityOneToOneLeading records.
 */
public class TestOneToOneLeadingJooqRecordMapper extends AbstractRecordMapper<TestEntityOneToOneLeadingRecord, TestEntityOneToOneLeading, TestRootOneToOneLeading> {

    @Override
    public DomainObjectBuilder<TestEntityOneToOneLeading> recordToDomainObjectBuilder(TestEntityOneToOneLeadingRecord record) {
        if (record == null) {
            return null;
        }
        TestEntityOneToOneLeadingRecord testEntityOneToOneLeadingRecord = record.into(Tables.TEST_ENTITY_ONE_TO_ONE_LEADING);
        return new InnerClassDomainObjectBuilder<>(TestEntityOneToOneLeading.builder()
            .setId(new TestEntityOneToOneLeadingId(testEntityOneToOneLeadingRecord.getId()))
            .setName(testEntityOneToOneLeadingRecord.getName())
            .setConcurrencyVersion(testEntityOneToOneLeadingRecord.getConcurrencyVersion()));
    }

    @Override
    public TestEntityOneToOneLeadingRecord from(TestEntityOneToOneLeading testEntityOneToOneLeading, TestRootOneToOneLeading root) {
        TestEntityOneToOneLeadingRecord testEntityOneToOneLeadingRecord = new TestEntityOneToOneLeadingRecord();
        testEntityOneToOneLeadingRecord.setId(testEntityOneToOneLeading.getId().value());
        testEntityOneToOneLeadingRecord.setName(testEntityOneToOneLeading.getName());
        testEntityOneToOneLeadingRecord.setConcurrencyVersion(testEntityOneToOneLeading.concurrencyVersion());
        return testEntityOneToOneLeadingRecord;
    }

    @Override
    public Class<TestEntityOneToOneLeading> domainObjectType() {
        return TestEntityOneToOneLeading.class;
    }

    @Override
    public Class<TestEntityOneToOneLeadingRecord> recordType() {
        return TestEntityOneToOneLeadingRecord.class;
    }
}
