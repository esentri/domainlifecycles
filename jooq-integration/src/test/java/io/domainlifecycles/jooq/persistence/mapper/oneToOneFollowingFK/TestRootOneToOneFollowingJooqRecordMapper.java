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

package io.domainlifecycles.jooq.persistence.mapper.oneToOneFollowingFK;


import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.test.tables.records.TestRootOneToOneFollowingRecord;
import tests.shared.persistence.domain.oneToOneFollowingFK.TestRootOneToOneFollowing;
import tests.shared.persistence.domain.oneToOneFollowingFK.TestRootOneToOneFollowingId;

import io.domainlifecycles.test.Tables;

/**
 * Mapping JOOQ TestRootOneToOneFollowing records.
 */
public class TestRootOneToOneFollowingJooqRecordMapper extends AbstractRecordMapper<TestRootOneToOneFollowingRecord,
    TestRootOneToOneFollowing, TestRootOneToOneFollowing> {

    @Override
    public DomainObjectBuilder<TestRootOneToOneFollowing> recordToDomainObjectBuilder(TestRootOneToOneFollowingRecord record) {
        if (record == null) {
            return null;
        }
        TestRootOneToOneFollowingRecord testRootOneToOneFollowingRecord = record.into(
            Tables.TEST_ROOT_ONE_TO_ONE_FOLLOWING);
        return new InnerClassDomainObjectBuilder<>(TestRootOneToOneFollowing.builder()
            .setId(new TestRootOneToOneFollowingId(testRootOneToOneFollowingRecord.getId()))
            .setName(testRootOneToOneFollowingRecord.getName())
            .setConcurrencyVersion(testRootOneToOneFollowingRecord.getConcurrencyVersion()));
    }

    @Override
    public TestRootOneToOneFollowingRecord from(TestRootOneToOneFollowing testRootOneToOneFollowing,
                                                TestRootOneToOneFollowing root) {
        TestRootOneToOneFollowingRecord testRootOneToOneFollowingRecord = new TestRootOneToOneFollowingRecord();
        testRootOneToOneFollowingRecord.setId(testRootOneToOneFollowing.getId().value());
        testRootOneToOneFollowingRecord.setName(testRootOneToOneFollowing.getName());
        testRootOneToOneFollowingRecord.setConcurrencyVersion(testRootOneToOneFollowing.concurrencyVersion());
        return testRootOneToOneFollowingRecord;
    }

    @Override
    public Class<TestRootOneToOneFollowing> domainObjectType() {
        return TestRootOneToOneFollowing.class;
    }

    @Override
    public Class<TestRootOneToOneFollowingRecord> recordType() {
        return TestRootOneToOneFollowingRecord.class;
    }
}
