package io.domainlifecycles.jdbc.persistence.mapper.oneToOneLeadingFK;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import tests.shared.persistence.domain.oneToOneLeadingFK.TestRootOneToOneLeading;
import tests.shared.persistence.domain.oneToOneLeadingFK.TestRootOneToOneLeadingId;

/**
 * TEST_ROOT_ONE_TO_ONE_LEADING's foreign key column is the abbreviated {@code TEST_ENTITY_ID}, not the
 * convention-compliant {@code TEST_ENTITY_ONE_TO_ONE_LEADING_ID} - hence a custom mapper.
 * {@code testEntityOneToOneLeading} is an entity reference, populated by the fetcher.
 */
public class TestRootOneToOneLeadingJdbcRecordMapper extends AbstractRecordMapper<JdbcRecord,
    TestRootOneToOneLeading, TestRootOneToOneLeading> {

    @Override
    public DomainObjectBuilder<TestRootOneToOneLeading> recordToDomainObjectBuilder(JdbcRecord record) {
        if (record == null) {
            return null;
        }
        return new InnerClassDomainObjectBuilder<>(TestRootOneToOneLeading.builder()
            .setId(new TestRootOneToOneLeadingId((Long) record.get("ID")))
            .setName((String) record.get("NAME"))
            .setConcurrencyVersion((Long) record.get("CONCURRENCY_VERSION")));
    }

    @Override
    public JdbcRecord from(TestRootOneToOneLeading testRootOneToOneLeading, TestRootOneToOneLeading root) {
        JdbcRecord record = new JdbcRecord("TEST_ROOT_ONE_TO_ONE_LEADING");
        record.set("ID", testRootOneToOneLeading.getId().value());
        record.set("TEST_ENTITY_ID", testRootOneToOneLeading.getTestEntityOneToOneLeading() == null
            ? null : testRootOneToOneLeading.getTestEntityOneToOneLeading().getId().value());
        record.set("NAME", testRootOneToOneLeading.getName());
        record.set("CONCURRENCY_VERSION", testRootOneToOneLeading.concurrencyVersion());
        return record;
    }

    @Override
    public Class<TestRootOneToOneLeading> domainObjectType() {
        return TestRootOneToOneLeading.class;
    }

    @Override
    public Class<JdbcRecord> recordType() {
        return JdbcRecord.class;
    }
}
