package io.domainlifecycles.jooq.persistence.mapper.oneToOneFollowingLeadingFK;


import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.test.jooq.Tables;
import io.domainlifecycles.test.jooq.tables.records.TestEntityAOneToOneFollowingLeadingRecord;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestEntityAOneToOneFollowingLeading;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestEntityAOneToOneFollowingLeadingId;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestRootOneToOneFollowingLeading;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestRootOneToOneFollowingLeadingId;

/**
 * Mapping JOOQ TestEntityOneToOneFollowing records.
 */
public class TestAOneToOneFollowingLeadingJooqRecordMapper extends AbstractRecordMapper<TestEntityAOneToOneFollowingLeadingRecord, TestEntityAOneToOneFollowingLeading, TestRootOneToOneFollowingLeading> {

    @Override
    public DomainObjectBuilder<TestEntityAOneToOneFollowingLeading> recordToDomainObjectBuilder(TestEntityAOneToOneFollowingLeadingRecord record) {
        if (record == null) {
            return null;
        }
        TestEntityAOneToOneFollowingLeadingRecord testEntityAOneToOneFollowingLeadingRecord = record.into(
            Tables.TEST_ENTITY_A_ONE_TO_ONE_FOLLOWING_LEADING);
        return new InnerClassDomainObjectBuilder<>(TestEntityAOneToOneFollowingLeading.builder()
            .setId(new TestEntityAOneToOneFollowingLeadingId(testEntityAOneToOneFollowingLeadingRecord.getId()))
            .setName(testEntityAOneToOneFollowingLeadingRecord.getName())
            .setTestRootId(testEntityAOneToOneFollowingLeadingRecord.getTestRootId() == null ?
                null : new TestRootOneToOneFollowingLeadingId(
                testEntityAOneToOneFollowingLeadingRecord.getTestRootId()))
            .setConcurrencyVersion(testEntityAOneToOneFollowingLeadingRecord.getConcurrencyVersion()));
    }

    @Override
    public TestEntityAOneToOneFollowingLeadingRecord from(TestEntityAOneToOneFollowingLeading testEntityAOneToOneFollowingLeading, TestRootOneToOneFollowingLeading root) {
        TestEntityAOneToOneFollowingLeadingRecord testEntityAOneToOneFollowingLeadingRecord =
            new TestEntityAOneToOneFollowingLeadingRecord();
        testEntityAOneToOneFollowingLeadingRecord.setId(testEntityAOneToOneFollowingLeading.getId().value());
        testEntityAOneToOneFollowingLeadingRecord.setName(testEntityAOneToOneFollowingLeading.getName());
        testEntityAOneToOneFollowingLeadingRecord.setTestRootId(
            testEntityAOneToOneFollowingLeading.getTestRootId() == null ?
                null : testEntityAOneToOneFollowingLeading.getTestRootId().value());
        testEntityAOneToOneFollowingLeadingRecord.setConcurrencyVersion(
            testEntityAOneToOneFollowingLeading.concurrencyVersion());
        return testEntityAOneToOneFollowingLeadingRecord;
    }

    @Override
    public Class<TestEntityAOneToOneFollowingLeading> domainObjectType() {
        return TestEntityAOneToOneFollowingLeading.class;
    }

    @Override
    public Class<TestEntityAOneToOneFollowingLeadingRecord> recordType() {
        return TestEntityAOneToOneFollowingLeadingRecord.class;
    }
}
