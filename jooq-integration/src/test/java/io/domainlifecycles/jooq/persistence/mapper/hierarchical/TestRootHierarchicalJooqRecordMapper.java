package io.domainlifecycles.jooq.persistence.mapper.hierarchical;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.test.jooq.Tables;
import io.domainlifecycles.test.jooq.tables.records.TestRootHierarchicalRecord;
import tests.shared.persistence.domain.hierarchical.TestRootHierarchical;
import tests.shared.persistence.domain.hierarchical.TestRootHierarchicalId;

/**
 * Mapping JOOQ TestRootHierarchical records.
 */
public class TestRootHierarchicalJooqRecordMapper extends AbstractRecordMapper<TestRootHierarchicalRecord,
    TestRootHierarchical, TestRootHierarchical> {

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
