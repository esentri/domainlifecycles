package io.domainlifecycles.jdbc.persistence.mapper.manyToManyWithJoinEntity;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestEntityManyToManyA;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestEntityManyToManyAId;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestRootManyToMany;

/**
 * TEST_ENTITY_MANY_TO_MANY_A's foreign key to its aggregate root is named {@code TEST_ROOT_ID}, not the
 * convention-compliant {@code TEST_ROOT_MANY_TO_MANY_ID} - hence a custom mapper. {@code testRootManyToMany}
 * is an entity reference, populated by the fetcher, so this mapper (like its jOOQ counterpart) only maps
 * this record's own scalar columns.
 */
public class TestManyToManyAJdbcRecordMapper extends AbstractRecordMapper<JdbcRecord, TestEntityManyToManyA,
    TestRootManyToMany> {

    @Override
    public DomainObjectBuilder<TestEntityManyToManyA> recordToDomainObjectBuilder(JdbcRecord record) {
        if (record == null) {
            return null;
        }
        return new InnerClassDomainObjectBuilder<>(TestEntityManyToManyA.builder()
            .setId(new TestEntityManyToManyAId((Long) record.get("ID")))
            .setName((String) record.get("NAME"))
            .setConcurrencyVersion((Long) record.get("CONCURRENCY_VERSION")));
    }

    @Override
    public JdbcRecord from(TestEntityManyToManyA testEntityManyToManyA, TestRootManyToMany root) {
        JdbcRecord record = new JdbcRecord("TEST_ENTITY_MANY_TO_MANY_A");
        record.set("ID", testEntityManyToManyA.getId().value());
        record.set("TEST_ROOT_ID", testEntityManyToManyA.getTestRootManyToMany().getId().value());
        record.set("NAME", testEntityManyToManyA.getName());
        record.set("CONCURRENCY_VERSION", testEntityManyToManyA.concurrencyVersion());
        return record;
    }

    @Override
    public Class<TestEntityManyToManyA> domainObjectType() {
        return TestEntityManyToManyA.class;
    }

    @Override
    public Class<JdbcRecord> recordType() {
        return JdbcRecord.class;
    }
}
