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

package nitrox.dlc.jooq.persistence.mapper.hierarchical;

import nitrox.dlc.builder.DomainObjectBuilder;
import nitrox.dlc.builder.innerclass.InnerClassDomainObjectBuilder;
import nitrox.dlc.persistence.mapping.AbstractRecordMapper;
import nitrox.dlc.test.Tables;
import nitrox.dlc.test.tables.records.TestRootHierarchicalRecord;
import tests.shared.persistence.domain.hierarchical.TestRootHierarchical;
import tests.shared.persistence.domain.hierarchical.TestRootHierarchicalId;

/**
 * Mapping JOOQ TestRootHierarchical records.
 */
public class TestRootHierarchicalJooqRecordMapper extends AbstractRecordMapper<TestRootHierarchicalRecord, TestRootHierarchical, TestRootHierarchical> {

    @Override
    public DomainObjectBuilder<TestRootHierarchical> recordToDomainObjectBuilder(TestRootHierarchicalRecord record) {
        if (record == null) {
            return null;
        }
        TestRootHierarchicalRecord testRootHierarchicalRecord = record.into(Tables.TEST_ROOT_HIERARCHICAL);
        return new InnerClassDomainObjectBuilder<>(TestRootHierarchical.builder()
            .setId(new TestRootHierarchicalId(testRootHierarchicalRecord.getId()))
            .setName(testRootHierarchicalRecord.getName())
            .setParentId(testRootHierarchicalRecord.getParentId() == null ?
                null : new TestRootHierarchicalId(testRootHierarchicalRecord.getParentId()))
            .setConcurrencyVersion(testRootHierarchicalRecord.getConcurrencyVersion()));
    }

    @Override
    public TestRootHierarchicalRecord from(TestRootHierarchical testRootHierarchical, TestRootHierarchical root) {
        TestRootHierarchicalRecord testRootHierarchicalRecord = new TestRootHierarchicalRecord();
        testRootHierarchicalRecord.setId(testRootHierarchical.getId().value());
        testRootHierarchicalRecord.setName(testRootHierarchical.getName());
        testRootHierarchicalRecord.setParentId(testRootHierarchical.getParentId() == null ?
            null : testRootHierarchical.getParentId().value());
        testRootHierarchicalRecord.setConcurrencyVersion(testRootHierarchical.concurrencyVersion());
        return testRootHierarchicalRecord;
    }

    @Override
    public Class<TestRootHierarchical> domainObjectType() {
        return TestRootHierarchical.class;
    }

    @Override
    public Class<TestRootHierarchicalRecord> recordType() {
        return TestRootHierarchicalRecord.class;
    }
}
