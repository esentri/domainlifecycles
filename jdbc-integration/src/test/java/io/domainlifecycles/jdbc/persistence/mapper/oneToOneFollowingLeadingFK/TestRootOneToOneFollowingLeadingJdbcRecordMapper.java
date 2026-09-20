package io.domainlifecycles.jdbc.persistence.mapper.oneToOneFollowingLeadingFK;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.jdbc.persistence.PhysicalNames;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestRootOneToOneFollowingLeading;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestRootOneToOneFollowingLeadingId;

/**
 * TEST_ROOT_ONE_TO_ONE_FOLLOWING_LEADING's foreign key column is the abbreviated {@code TEST_ENTITY_ID}, not
 * the convention-compliant {@code TEST_ENTITY_B_ONE_TO_ONE_FOLLOWING_LEADING_ID} - hence a custom mapper.
 * Both {@code testEntityAOneToOneFollowingLeading} and {@code testEntityBOneToOneFollowingLeading} are
 * entity references, populated by the fetcher.
 */
public class TestRootOneToOneFollowingLeadingJdbcRecordMapper extends AbstractRecordMapper<JdbcRecord,
    TestRootOneToOneFollowingLeading, TestRootOneToOneFollowingLeading> {

    @Override
    public DomainObjectBuilder<TestRootOneToOneFollowingLeading> recordToDomainObjectBuilder(JdbcRecord record) {
        if (record == null) {
            return null;
        }
        return new InnerClassDomainObjectBuilder<>(TestRootOneToOneFollowingLeading.builder()
            .setId(new TestRootOneToOneFollowingLeadingId((Long) record.get(PhysicalNames.name("ID"))))
            .setName((String) record.get(PhysicalNames.name("NAME")))
            .setConcurrencyVersion((Long) record.get(PhysicalNames.name("CONCURRENCY_VERSION"))));
    }

    @Override
    public JdbcRecord from(TestRootOneToOneFollowingLeading testRootOneToOneFollowingLeading,
                            TestRootOneToOneFollowingLeading root) {
        JdbcRecord record = new JdbcRecord(PhysicalNames.name("TEST_ROOT_ONE_TO_ONE_FOLLOWING_LEADING"));
        record.set(PhysicalNames.name("ID"), testRootOneToOneFollowingLeading.getId().value());
        record.set(PhysicalNames.name("TEST_ENTITY_ID"), testRootOneToOneFollowingLeading.getTestEntityBOneToOneFollowingLeading()
            == null ? null : testRootOneToOneFollowingLeading.getTestEntityBOneToOneFollowingLeading()
            .getId().value());
        record.set(PhysicalNames.name("NAME"), testRootOneToOneFollowingLeading.getName());
        record.set(PhysicalNames.name("CONCURRENCY_VERSION"), testRootOneToOneFollowingLeading.concurrencyVersion());
        return record;
    }

    @Override
    public Class<TestRootOneToOneFollowingLeading> domainObjectType() {
        return TestRootOneToOneFollowingLeading.class;
    }

    @Override
    public Class<JdbcRecord> recordType() {
        return JdbcRecord.class;
    }
}
