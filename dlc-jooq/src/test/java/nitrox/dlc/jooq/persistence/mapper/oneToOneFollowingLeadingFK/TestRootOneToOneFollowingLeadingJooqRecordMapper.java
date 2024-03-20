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

package nitrox.dlc.jooq.persistence.mapper.oneToOneFollowingLeadingFK;

import nitrox.dlc.builder.DomainObjectBuilder;
import nitrox.dlc.builder.innerclass.InnerClassDomainObjectBuilder;
import nitrox.dlc.persistence.mapping.AbstractRecordMapper;
import nitrox.dlc.test.tables.records.TestRootOneToOneFollowingLeadingRecord;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestRootOneToOneFollowingLeading;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestRootOneToOneFollowingLeadingId;

import nitrox.dlc.test.Tables;

/**
 * Mapping JOOQ TestRootOneToOneFollowing records.
 */
public class TestRootOneToOneFollowingLeadingJooqRecordMapper extends AbstractRecordMapper<TestRootOneToOneFollowingLeadingRecord, TestRootOneToOneFollowingLeading, TestRootOneToOneFollowingLeading> {

    @Override
    public DomainObjectBuilder<TestRootOneToOneFollowingLeading> recordToDomainObjectBuilder(TestRootOneToOneFollowingLeadingRecord record) {
        if (record == null) {
            return null;
        }
        TestRootOneToOneFollowingLeadingRecord testRootOneToOneFollowingLeadingRecord = record.into(Tables.TEST_ROOT_ONE_TO_ONE_FOLLOWING_LEADING);
        return new InnerClassDomainObjectBuilder<>(TestRootOneToOneFollowingLeading.builder()
            .setId(new TestRootOneToOneFollowingLeadingId(testRootOneToOneFollowingLeadingRecord.getId()))
            .setName(testRootOneToOneFollowingLeadingRecord.getName())
            .setConcurrencyVersion(testRootOneToOneFollowingLeadingRecord.getConcurrencyVersion()));
    }

    @Override
    public TestRootOneToOneFollowingLeadingRecord from(TestRootOneToOneFollowingLeading testRootOneToOneFollowingLeading, TestRootOneToOneFollowingLeading root) {
        TestRootOneToOneFollowingLeadingRecord testRootOneToOneFollowingLeadingRecord = new TestRootOneToOneFollowingLeadingRecord();
        testRootOneToOneFollowingLeadingRecord.setId(testRootOneToOneFollowingLeading.getId().value());
        testRootOneToOneFollowingLeadingRecord.setTestEntityId(testRootOneToOneFollowingLeading.getTestEntityBOneToOneFollowingLeading() == null ?
            null : testRootOneToOneFollowingLeading.getTestEntityBOneToOneFollowingLeading().getId().value());
        testRootOneToOneFollowingLeadingRecord.setName(testRootOneToOneFollowingLeading.getName());
        testRootOneToOneFollowingLeadingRecord.setConcurrencyVersion(testRootOneToOneFollowingLeading.concurrencyVersion());
        return testRootOneToOneFollowingLeadingRecord;
    }

    @Override
    public Class<TestRootOneToOneFollowingLeading> domainObjectType() {
        return TestRootOneToOneFollowingLeading.class;
    }

    @Override
    public Class<TestRootOneToOneFollowingLeadingRecord> recordType() {
        return TestRootOneToOneFollowingLeadingRecord.class;
    }
}
