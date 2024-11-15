package io.domainlifecycles.jooq.persistence.mapper.oneToOneFollowingLeadingFK;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.test.Tables;
import io.domainlifecycles.test.tables.records.TestRootOneToOneFollowingLeadingRecord;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestRootOneToOneFollowingLeading;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestRootOneToOneFollowingLeadingId;

/**
 * Mapping JOOQ TestRootOneToOneFollowing records.
 */
public class TestRootOneToOneFollowingLeadingJooqRecordMapper extends AbstractRecordMapper<TestRootOneToOneFollowingLeadingRecord, TestRootOneToOneFollowingLeading, TestRootOneToOneFollowingLeading> {

    @Override
    public DomainObjectBuilder<TestRootOneToOneFollowingLeading> recordToDomainObjectBuilder(TestRootOneToOneFollowingLeadingRecord record) {
        if (record == null) {
            return null;
        }
        TestRootOneToOneFollowingLeadingRecord testRootOneToOneFollowingLeadingRecord = record.into(
            Tables.TEST_ROOT_ONE_TO_ONE_FOLLOWING_LEADING);
        return new InnerClassDomainObjectBuilder<>(TestRootOneToOneFollowingLeading.builder()
            .setId(new TestRootOneToOneFollowingLeadingId(testRootOneToOneFollowingLeadingRecord.getId()))
            .setName(testRootOneToOneFollowingLeadingRecord.getName())
            .setConcurrencyVersion(testRootOneToOneFollowingLeadingRecord.getConcurrencyVersion()));
    }

    @Override
    public TestRootOneToOneFollowingLeadingRecord from(TestRootOneToOneFollowingLeading testRootOneToOneFollowingLeading, TestRootOneToOneFollowingLeading root) {
        TestRootOneToOneFollowingLeadingRecord testRootOneToOneFollowingLeadingRecord =
            new TestRootOneToOneFollowingLeadingRecord();
        testRootOneToOneFollowingLeadingRecord.setId(testRootOneToOneFollowingLeading.getId().value());
        testRootOneToOneFollowingLeadingRecord.setTestEntityId(
            testRootOneToOneFollowingLeading.getTestEntityBOneToOneFollowingLeading() == null ?
                null : testRootOneToOneFollowingLeading.getTestEntityBOneToOneFollowingLeading().getId().value());
        testRootOneToOneFollowingLeadingRecord.setName(testRootOneToOneFollowingLeading.getName());
        testRootOneToOneFollowingLeadingRecord.setConcurrencyVersion(
            testRootOneToOneFollowingLeading.concurrencyVersion());
        return testRootOneToOneFollowingLeadingRecord;
    }

    @Override
    public Class<TestRootOneToOneFollowingLeading> domainObjectType() {
        return TestRootOneToOneFollowingLeading.class;
    }

    @Override
    public Class<TestRootOneToOneFollowingLeadingRecord> recordType() {
        return TestRootOneToOneFollowingLeadingRecord.class;
    }
}
