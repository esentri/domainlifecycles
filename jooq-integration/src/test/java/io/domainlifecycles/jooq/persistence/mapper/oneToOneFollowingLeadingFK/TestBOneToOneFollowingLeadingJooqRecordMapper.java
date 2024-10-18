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
import io.domainlifecycles.test.tables.records.TestEntityBOneToOneFollowingLeadingRecord;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestEntityBOneToOneFollowingLeading;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestEntityBOneToOneFollowingLeadingId;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestRootOneToOneFollowingLeading;

import io.domainlifecycles.test.Tables;

/**
 * Mapping JOOQ TestEntityOneToOneFollowing records.
 */
public class TestBOneToOneFollowingLeadingJooqRecordMapper extends AbstractRecordMapper<TestEntityBOneToOneFollowingLeadingRecord, TestEntityBOneToOneFollowingLeading, TestRootOneToOneFollowingLeading> {

    @Override
    public DomainObjectBuilder<TestEntityBOneToOneFollowingLeading> recordToDomainObjectBuilder(TestEntityBOneToOneFollowingLeadingRecord record) {
        if (record == null) {
            return null;
        }
        TestEntityBOneToOneFollowingLeadingRecord testEntityBOneToOneFollowingLeadingRecord = record.into(
            Tables.TEST_ENTITY_B_ONE_TO_ONE_FOLLOWING_LEADING);
        return new InnerClassDomainObjectBuilder<>(TestEntityBOneToOneFollowingLeading.builder()
            .setId(new TestEntityBOneToOneFollowingLeadingId(testEntityBOneToOneFollowingLeadingRecord.getId()))
            .setName(testEntityBOneToOneFollowingLeadingRecord.getName())
            .setConcurrencyVersion(testEntityBOneToOneFollowingLeadingRecord.getConcurrencyVersion()));
    }

    @Override
    public TestEntityBOneToOneFollowingLeadingRecord from(TestEntityBOneToOneFollowingLeading testEntityBOneToOneFollowingLeading, TestRootOneToOneFollowingLeading root) {
        TestEntityBOneToOneFollowingLeadingRecord testEntityBOneToOneFollowingLeadingRecord =
            new TestEntityBOneToOneFollowingLeadingRecord();
        testEntityBOneToOneFollowingLeadingRecord.setId(testEntityBOneToOneFollowingLeading.getId().value());
        testEntityBOneToOneFollowingLeadingRecord.setName(testEntityBOneToOneFollowingLeading.getName());
        testEntityBOneToOneFollowingLeadingRecord.setConcurrencyVersion(
            testEntityBOneToOneFollowingLeading.concurrencyVersion());
        return testEntityBOneToOneFollowingLeadingRecord;
    }

    @Override
    public Class<TestEntityBOneToOneFollowingLeading> domainObjectType() {
        return TestEntityBOneToOneFollowingLeading.class;
    }

    @Override
    public Class<TestEntityBOneToOneFollowingLeadingRecord> recordType() {
        return TestEntityBOneToOneFollowingLeadingRecord.class;
    }
}
