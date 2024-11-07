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
