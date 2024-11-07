/*
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

package io.domainlifecycles.jooq.persistence.mapper.hierarchicalBackRef;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.test.tables.records.TestRootHierarchicalBackrefRecord;
import tests.shared.persistence.domain.hierarchicalBackRef.TestRootHierarchicalBackref;
import tests.shared.persistence.domain.hierarchicalBackRef.TestRootHierarchicalBackrefId;

import io.domainlifecycles.test.Tables;

/**
 * Mapping JOOQ TestRootHierarchical records.
 */
public class TestRootHierarchicalBackrefJooqRecordMapper extends AbstractRecordMapper<TestRootHierarchicalBackrefRecord, TestRootHierarchicalBackref, TestRootHierarchicalBackref> {

    @Override
    public DomainObjectBuilder<TestRootHierarchicalBackref> recordToDomainObjectBuilder(TestRootHierarchicalBackrefRecord record) {
        if (record == null) {
            return null;
        }
        TestRootHierarchicalBackrefRecord testRootHierarchicalRecord = record.into(
            Tables.TEST_ROOT_HIERARCHICAL_BACKREF);
        return new InnerClassDomainObjectBuilder<TestRootHierarchicalBackref>(TestRootHierarchicalBackref.builder()
            .setId(new TestRootHierarchicalBackrefId(testRootHierarchicalRecord.getId()))
            .setName(testRootHierarchicalRecord.getName())
            .setConcurrencyVersion(testRootHierarchicalRecord.getConcurrencyVersion()));
    }

    @Override
    public TestRootHierarchicalBackrefRecord from(TestRootHierarchicalBackref testRootHierarchicalBackref,
                                                  TestRootHierarchicalBackref root) {
        TestRootHierarchicalBackrefRecord testRootHierarchicalRecord = new TestRootHierarchicalBackrefRecord();
        testRootHierarchicalRecord.setId(testRootHierarchicalBackref.getId().value());
        testRootHierarchicalRecord.setName(testRootHierarchicalBackref.getName());
        testRootHierarchicalRecord.setParentId(testRootHierarchicalBackref.getParent() == null ?
            null : testRootHierarchicalBackref.getParent().getId().value());
        testRootHierarchicalRecord.setConcurrencyVersion(testRootHierarchicalBackref.concurrencyVersion());
        return testRootHierarchicalRecord;
    }

    @Override
    public Class<TestRootHierarchicalBackref> domainObjectType() {
        return TestRootHierarchicalBackref.class;
    }

    @Override
    public Class<TestRootHierarchicalBackrefRecord> recordType() {
        return TestRootHierarchicalBackrefRecord.class;
    }
}
