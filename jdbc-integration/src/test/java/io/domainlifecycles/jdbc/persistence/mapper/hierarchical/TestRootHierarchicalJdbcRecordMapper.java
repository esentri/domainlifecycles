package io.domainlifecycles.jdbc.persistence.mapper.hierarchical;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.jdbc.persistence.PhysicalNames;
import tests.shared.persistence.domain.hierarchical.TestRootHierarchical;
import tests.shared.persistence.domain.hierarchical.TestRootHierarchicalId;

/**
 * {@code TestRootHierarchical} has a {@code child} field of its own aggregate root type (a direct
 * self-reference). {@link io.domainlifecycles.persistence.mapping.AutoRecordMapper}'s internal traversal
 * (used to discover value-object-record paths) has no cycle guard for a self-referencing aggregate root
 * field and recurses into it indefinitely - so, exactly like the jOOQ mapper this is ported from, this type
 * needs a manually written mapper rather than auto-mapping, even though the column mapping itself (id, name,
 * parentId, concurrencyVersion) is convention-compliant. {@code child} is populated by the fetcher, not by
 * this mapper.
 */
public class TestRootHierarchicalJdbcRecordMapper extends AbstractRecordMapper<JdbcRecord, TestRootHierarchical,
    TestRootHierarchical> {

    @Override
    public DomainObjectBuilder<TestRootHierarchical> recordToDomainObjectBuilder(JdbcRecord record) {
        if (record == null) {
            return null;
        }
        Long parentId = (Long) record.get(PhysicalNames.name("PARENT_ID"));
        return new InnerClassDomainObjectBuilder<>(TestRootHierarchical.builder()
            .setId(new TestRootHierarchicalId((Long) record.get(PhysicalNames.name("ID"))))
            .setName((String) record.get(PhysicalNames.name("NAME")))
            .setParentId(parentId == null ? null : new TestRootHierarchicalId(parentId))
            .setConcurrencyVersion((Long) record.get(PhysicalNames.name("CONCURRENCY_VERSION"))));
    }

    @Override
    public JdbcRecord from(TestRootHierarchical testRootHierarchical, TestRootHierarchical root) {
        JdbcRecord record = new JdbcRecord(PhysicalNames.name("TEST_ROOT_HIERARCHICAL"));
        record.set(PhysicalNames.name("ID"), testRootHierarchical.getId().value());
        record.set(PhysicalNames.name("NAME"), testRootHierarchical.getName());
        record.set(PhysicalNames.name("PARENT_ID"), testRootHierarchical.getParentId() == null
            ? null : testRootHierarchical.getParentId().value());
        record.set(PhysicalNames.name("CONCURRENCY_VERSION"), testRootHierarchical.concurrencyVersion());
        return record;
    }

    @Override
    public Class<TestRootHierarchical> domainObjectType() {
        return TestRootHierarchical.class;
    }

    @Override
    public Class<JdbcRecord> recordType() {
        return JdbcRecord.class;
    }
}
