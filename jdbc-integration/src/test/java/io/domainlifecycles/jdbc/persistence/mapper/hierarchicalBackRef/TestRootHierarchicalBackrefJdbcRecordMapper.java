package io.domainlifecycles.jdbc.persistence.mapper.hierarchicalBackRef;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.jdbc.persistence.PhysicalNames;
import tests.shared.persistence.domain.hierarchicalBackRef.TestRootHierarchicalBackref;
import tests.shared.persistence.domain.hierarchicalBackRef.TestRootHierarchicalBackrefId;

/**
 * {@code TestRootHierarchicalBackref} has both a {@code parent} and a {@code child} field of its own
 * aggregate root type (self-references in both directions). Same reasoning as {@code
 * TestRootHierarchicalJdbcRecordMapper}: a manually written mapper is required to avoid {@code
 * AutoRecordMapper}'s unguarded recursion into self-referencing aggregate root fields. {@code parent}/{@code
 * child} are populated by the fetcher, not by this mapper.
 */
public class TestRootHierarchicalBackrefJdbcRecordMapper extends AbstractRecordMapper<JdbcRecord,
    TestRootHierarchicalBackref, TestRootHierarchicalBackref> {

    @Override
    public DomainObjectBuilder<TestRootHierarchicalBackref> recordToDomainObjectBuilder(JdbcRecord record) {
        if (record == null) {
            return null;
        }
        return new InnerClassDomainObjectBuilder<>(TestRootHierarchicalBackref.builder()
            .setId(new TestRootHierarchicalBackrefId((Long) record.get(PhysicalNames.name("ID"))))
            .setName((String) record.get(PhysicalNames.name("NAME")))
            .setConcurrencyVersion((Long) record.get(PhysicalNames.name("CONCURRENCY_VERSION"))));
    }

    @Override
    public JdbcRecord from(TestRootHierarchicalBackref testRootHierarchicalBackref,
                           TestRootHierarchicalBackref root) {
        JdbcRecord record = new JdbcRecord(PhysicalNames.name("TEST_ROOT_HIERARCHICAL_BACKREF"));
        record.set(PhysicalNames.name("ID"), testRootHierarchicalBackref.getId().value());
        record.set(PhysicalNames.name("NAME"), testRootHierarchicalBackref.getName());
        record.set(PhysicalNames.name("PARENT_ID"), testRootHierarchicalBackref.getParent() == null
            ? null : testRootHierarchicalBackref.getParent().getId().value());
        record.set(PhysicalNames.name("CONCURRENCY_VERSION"), testRootHierarchicalBackref.concurrencyVersion());
        return record;
    }

    @Override
    public Class<TestRootHierarchicalBackref> domainObjectType() {
        return TestRootHierarchicalBackref.class;
    }

    @Override
    public Class<JdbcRecord> recordType() {
        return JdbcRecord.class;
    }
}
