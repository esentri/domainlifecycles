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

package nitrox.dlc.jooq.persistence.mapper.complex;

import nitrox.dlc.builder.DomainObjectBuilder;
import nitrox.dlc.builder.innerclass.InnerClassDomainObjectBuilder;
import nitrox.dlc.persistence.mapping.AbstractRecordMapper;
import nitrox.dlc.test.Tables;
import nitrox.dlc.test.tables.records.TestRootRecord;
import tests.shared.persistence.domain.complex.TestRoot;
import tests.shared.persistence.domain.complex.TestRootId;

/**
 * Mapping JOOQ Testroot records.
 */
public class TestRootJooqRecordMapper extends AbstractRecordMapper<TestRootRecord, TestRoot, TestRoot> {

    @Override
    public DomainObjectBuilder<TestRoot> recordToDomainObjectBuilder(TestRootRecord record) {
        if (record == null) {
            return null;
        }
        TestRootRecord testRootRecord = record.into(Tables.TEST_ROOT);
        return new InnerClassDomainObjectBuilder<TestRoot>(TestRoot.builder()
            .setId(new TestRootId(testRootRecord.getId()))
            .setName(testRootRecord.getName())
            .setConcurrencyVersion(testRootRecord.getConcurrencyVersion()));
    }

    @Override
    public TestRootRecord from(TestRoot testRoot, TestRoot root) {
        TestRootRecord testRootRecord = new TestRootRecord();
        testRootRecord.setId(testRoot.getId().value());
        testRootRecord.setName(testRoot.getName());
        testRootRecord.setConcurrencyVersion(testRoot.concurrencyVersion());
        return testRootRecord;
    }

    @Override
    public Class<TestRoot> domainObjectType() {
        return TestRoot.class;
    }

    @Override
    public Class<TestRootRecord> recordType() {
        return TestRootRecord.class;
    }
}
