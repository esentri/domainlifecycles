package io.domainlifecycles.jdbc.persistence.mapper.complex;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import tests.shared.persistence.domain.complex.TestEntity1;
import tests.shared.persistence.domain.complex.TestEntity1Id;
import tests.shared.persistence.domain.complex.TestRoot;
import tests.shared.persistence.domain.complex.TestRootId;

/**
 * TEST_ENTITY_1 has two foreign keys to the same table (TEST_ENTITY_2_ID_A/_B), which the "strip the last
 * two characters off the FK column name" auto-mapping convention cannot resolve - hence a custom mapper.
 * {@code testEntity2A}/{@code testEntity2B} are entity references, not plain Identity fields: like the jOOQ
 * mapper this is ported from, this mapper only maps this record's own columns; the fetcher populates the
 * entity reference fields separately, after the builder below is built.
 */
public class Test1JdbcRecordMapper extends AbstractRecordMapper<JdbcRecord, TestEntity1, TestRoot> {

    @Override
    public DomainObjectBuilder<TestEntity1> recordToDomainObjectBuilder(JdbcRecord record) {
        if (record == null) {
            return null;
        }
        return new InnerClassDomainObjectBuilder<>(TestEntity1.builder()
            .setId(new TestEntity1Id((Long) record.get("ID")))
            .setTestRootId(new TestRootId((Long) record.get("TEST_ROOT_ID")))
            .setName((String) record.get("NAME"))
            .setConcurrencyVersion((Long) record.get("CONCURRENCY_VERSION")));
    }

    @Override
    public JdbcRecord from(TestEntity1 testEntity1, TestRoot root) {
        JdbcRecord record = new JdbcRecord("TEST_ENTITY_1");
        record.set("ID", testEntity1.getId().value());
        record.set("TEST_ROOT_ID", testEntity1.getTestRootId().value());
        record.set("NAME", testEntity1.getName());
        record.set("TEST_ENTITY_2_ID_A",
            testEntity1.getTestEntity2A() != null ? testEntity1.getTestEntity2A().getId().value() : null);
        record.set("TEST_ENTITY_2_ID_B",
            testEntity1.getTestEntity2B() != null ? testEntity1.getTestEntity2B().getId().value() : null);
        record.set("CONCURRENCY_VERSION", testEntity1.concurrencyVersion());
        return record;
    }

    @Override
    public Class<TestEntity1> domainObjectType() {
        return TestEntity1.class;
    }

    @Override
    public Class<JdbcRecord> recordType() {
        return JdbcRecord.class;
    }
}
