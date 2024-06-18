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

package io.domainlifecycles.jooq.persistence.mapper.oneToOneFollowingLeadingFK;


import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.test.tables.records.TestEntityAOneToOneFollowingLeadingRecord;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestEntityAOneToOneFollowingLeading;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestEntityAOneToOneFollowingLeadingId;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestRootOneToOneFollowingLeading;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestRootOneToOneFollowingLeadingId;

import io.domainlifecycles.test.Tables;

/**
 * Mapping JOOQ TestEntityOneToOneFollowing records.
 */
public class TestAOneToOneFollowingLeadingJooqRecordMapper extends AbstractRecordMapper<TestEntityAOneToOneFollowingLeadingRecord, TestEntityAOneToOneFollowingLeading, TestRootOneToOneFollowingLeading> {

    @Override
    public DomainObjectBuilder<TestEntityAOneToOneFollowingLeading> recordToDomainObjectBuilder(TestEntityAOneToOneFollowingLeadingRecord record) {
        if (record == null) {
            return null;
        }
        TestEntityAOneToOneFollowingLeadingRecord testEntityAOneToOneFollowingLeadingRecord = record.into(Tables.TEST_ENTITY_A_ONE_TO_ONE_FOLLOWING_LEADING);
        return new InnerClassDomainObjectBuilder<>(TestEntityAOneToOneFollowingLeading.builder()
            .setId(new TestEntityAOneToOneFollowingLeadingId(testEntityAOneToOneFollowingLeadingRecord.getId()))
            .setName(testEntityAOneToOneFollowingLeadingRecord.getName())
            .setTestRootId(testEntityAOneToOneFollowingLeadingRecord.getTestRootId() == null ?
                null : new TestRootOneToOneFollowingLeadingId(testEntityAOneToOneFollowingLeadingRecord.getTestRootId()))
            .setConcurrencyVersion(testEntityAOneToOneFollowingLeadingRecord.getConcurrencyVersion()));
    }

    @Override
    public TestEntityAOneToOneFollowingLeadingRecord from(TestEntityAOneToOneFollowingLeading testEntityAOneToOneFollowingLeading, TestRootOneToOneFollowingLeading root) {
        TestEntityAOneToOneFollowingLeadingRecord testEntityAOneToOneFollowingLeadingRecord = new TestEntityAOneToOneFollowingLeadingRecord();
        testEntityAOneToOneFollowingLeadingRecord.setId(testEntityAOneToOneFollowingLeading.getId().value());
        testEntityAOneToOneFollowingLeadingRecord.setName(testEntityAOneToOneFollowingLeading.getName());
        testEntityAOneToOneFollowingLeadingRecord.setTestRootId(testEntityAOneToOneFollowingLeading.getTestRootId() == null ?
            null : testEntityAOneToOneFollowingLeading.getTestRootId().value());
        testEntityAOneToOneFollowingLeadingRecord.setConcurrencyVersion(testEntityAOneToOneFollowingLeading.concurrencyVersion());
        return testEntityAOneToOneFollowingLeadingRecord;
    }

    @Override
    public Class<TestEntityAOneToOneFollowingLeading> domainObjectType() {
        return TestEntityAOneToOneFollowingLeading.class;
    }

    @Override
    public Class<TestEntityAOneToOneFollowingLeadingRecord> recordType() {
        return TestEntityAOneToOneFollowingLeadingRecord.class;
    }
}
