package io.domainlifecycles.jdbc.persistence.mapper.manyToManyWithJoinEntity;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.jdbc.persistence.PhysicalNames;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestEntityManyToManyAId;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestEntityManyToManyJoin;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestEntityManyToManyJoinId;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestRootManyToMany;

/**
 * TEST_ENTITY_MANY_TO_MANY_JOIN's foreign key columns are abbreviated ({@code TEST_ENTITY_A_ID}/{@code
 * TEST_ENTITY_B_ID} rather than the convention-compliant {@code TEST_ENTITY_MANY_TO_MANY_A_ID}/{@code
 * ..._B_ID}) - hence a custom mapper. {@code testEntityManyToManyAId} is a plain Identity field (mapped
 * here); {@code testEntityManyToManyB} is an entity reference, populated by the fetcher.
 */
public class TestManyToManyJoinJdbcRecordMapper extends AbstractRecordMapper<JdbcRecord,
    TestEntityManyToManyJoin, TestRootManyToMany> {

    @Override
    public DomainObjectBuilder<TestEntityManyToManyJoin> recordToDomainObjectBuilder(JdbcRecord record) {
        if (record == null) {
            return null;
        }
        return new InnerClassDomainObjectBuilder<>(TestEntityManyToManyJoin.builder()
            .setId(new TestEntityManyToManyJoinId((Long) record.get(PhysicalNames.name("ID"))))
            .setTestEntityManyToManyAId(new TestEntityManyToManyAId((Long) record.get(PhysicalNames.name("TEST_ENTITY_A_ID"))))
            .setConcurrencyVersion((Long) record.get(PhysicalNames.name("CONCURRENCY_VERSION"))));
    }

    @Override
    public JdbcRecord from(TestEntityManyToManyJoin testEntityManyToManyJoin, TestRootManyToMany root) {
        JdbcRecord record = new JdbcRecord(PhysicalNames.name("TEST_ENTITY_MANY_TO_MANY_JOIN"));
        record.set(PhysicalNames.name("ID"), testEntityManyToManyJoin.getId().value());
        record.set(PhysicalNames.name("TEST_ENTITY_A_ID"), testEntityManyToManyJoin.getTestEntityManyToManyAId().value());
        record.set(PhysicalNames.name("TEST_ENTITY_B_ID"), testEntityManyToManyJoin.getTestEntityManyToManyB().getId().value());
        record.set(PhysicalNames.name("CONCURRENCY_VERSION"), testEntityManyToManyJoin.concurrencyVersion());
        return record;
    }

    @Override
    public Class<TestEntityManyToManyJoin> domainObjectType() {
        return TestEntityManyToManyJoin.class;
    }

    @Override
    public Class<JdbcRecord> recordType() {
        return JdbcRecord.class;
    }
}
