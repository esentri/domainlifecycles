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

package nitrox.dlc.jooq.persistence.mapper.oneToOneFollowingFK;


import nitrox.dlc.builder.DomainObjectBuilder;
import nitrox.dlc.builder.innerclass.InnerClassDomainObjectBuilder;
import nitrox.dlc.persistence.mapping.AbstractRecordMapper;
import nitrox.dlc.test.Tables;
import nitrox.dlc.test.tables.records.TestEntityOneToOneFollowingRecord;
import tests.shared.persistence.domain.oneToOneFollowingFK.TestEntityOneToOneFollowing;
import tests.shared.persistence.domain.oneToOneFollowingFK.TestEntityOneToOneFollowingId;
import tests.shared.persistence.domain.oneToOneFollowingFK.TestRootOneToOneFollowing;
import tests.shared.persistence.domain.oneToOneFollowingFK.TestRootOneToOneFollowingId;


/**
 * Mapping JOOQ TestEntityOneToOneFollowing records.
 */
public class TestOneToOneFollowingJooqRecordMapper extends AbstractRecordMapper<TestEntityOneToOneFollowingRecord, TestEntityOneToOneFollowing, TestRootOneToOneFollowing> {

    @Override
    public DomainObjectBuilder<TestEntityOneToOneFollowing> recordToDomainObjectBuilder(TestEntityOneToOneFollowingRecord record) {
        if (record == null) {
            return null;
        }
        TestEntityOneToOneFollowingRecord testEntityOneToOneFollowingRecord = record.into(Tables.TEST_ENTITY_ONE_TO_ONE_FOLLOWING);
        return new InnerClassDomainObjectBuilder<>(TestEntityOneToOneFollowing.builder()
            .setId(new TestEntityOneToOneFollowingId(testEntityOneToOneFollowingRecord.getId()))
            .setName(testEntityOneToOneFollowingRecord.getName())
            .setTestRootId(testEntityOneToOneFollowingRecord.getTestRootId() == null ?
                null : new TestRootOneToOneFollowingId(testEntityOneToOneFollowingRecord.getTestRootId()))
            .setConcurrencyVersion(testEntityOneToOneFollowingRecord.getConcurrencyVersion()));
    }

    @Override
    public TestEntityOneToOneFollowingRecord from(TestEntityOneToOneFollowing testEntityOneToOneFollowing, TestRootOneToOneFollowing root) {
        TestEntityOneToOneFollowingRecord testEntityOneToOneFollowingRecord = new TestEntityOneToOneFollowingRecord();
        testEntityOneToOneFollowingRecord.setId(testEntityOneToOneFollowing.getId().value());
        testEntityOneToOneFollowingRecord.setName(testEntityOneToOneFollowing.getName());
        testEntityOneToOneFollowingRecord.setTestRootId(testEntityOneToOneFollowing.getTestRootId() == null ?
            null : testEntityOneToOneFollowing.getTestRootId().value());
        testEntityOneToOneFollowingRecord.setConcurrencyVersion(testEntityOneToOneFollowing.concurrencyVersion());
        return testEntityOneToOneFollowingRecord;
    }

    @Override
    public Class<TestEntityOneToOneFollowing> domainObjectType() {
        return TestEntityOneToOneFollowing.class;
    }

    @Override
    public Class<TestEntityOneToOneFollowingRecord> recordType() {
        return TestEntityOneToOneFollowingRecord.class;
    }
}
