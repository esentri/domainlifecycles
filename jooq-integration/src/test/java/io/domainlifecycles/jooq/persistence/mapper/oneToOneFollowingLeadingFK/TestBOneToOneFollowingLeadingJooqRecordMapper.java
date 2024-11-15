package io.domainlifecycles.jooq.persistence.mapper.oneToOneFollowingLeadingFK;


import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.test.Tables;
import io.domainlifecycles.test.tables.records.TestEntityBOneToOneFollowingLeadingRecord;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestEntityBOneToOneFollowingLeading;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestEntityBOneToOneFollowingLeadingId;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestRootOneToOneFollowingLeading;

/**
 * Mapping JOOQ TestEntityOneToOneFollowing records.
 */
public class TestBOneToOneFollowingLeadingJooqRecordMapper extends AbstractRecordMapper<TestEntityBOneToOneFollowingLeadingRecord, TestEntityBOneToOneFollowingLeading, TestRootOneToOneFollowingLeading> {

    @Override
    public DomainObjectBuilder<TestEntityBOneToOneFollowingLeading> recordToDomainObjectBuilder(TestEntityBOneToOneFollowingLeadingRecord record) {
        if (record == null) {
            return null;
        }
        TestEntityBOneToOneFollowingLeadingRecord testEntityBOneToOneFollowingLeadingRecord = record.into(
            Tables.TEST_ENTITY_B_ONE_TO_ONE_FOLLOWING_LEADING);
        return new InnerClassDomainObjectBuilder<>(TestEntityBOneToOneFollowingLeading.builder()
            .setId(new TestEntityBOneToOneFollowingLeadingId(testEntityBOneToOneFollowingLeadingRecord.getId()))
            .setName(testEntityBOneToOneFollowingLeadingRecord.getName())
            .setConcurrencyVersion(testEntityBOneToOneFollowingLeadingRecord.getConcurrencyVersion()));
    }

    @Override
    public TestEntityBOneToOneFollowingLeadingRecord from(TestEntityBOneToOneFollowingLeading testEntityBOneToOneFollowingLeading, TestRootOneToOneFollowingLeading root) {
        TestEntityBOneToOneFollowingLeadingRecord testEntityBOneToOneFollowingLeadingRecord =
            new TestEntityBOneToOneFollowingLeadingRecord();
        testEntityBOneToOneFollowingLeadingRecord.setId(testEntityBOneToOneFollowingLeading.getId().value());
        testEntityBOneToOneFollowingLeadingRecord.setName(testEntityBOneToOneFollowingLeading.getName());
        testEntityBOneToOneFollowingLeadingRecord.setConcurrencyVersion(
            testEntityBOneToOneFollowingLeading.concurrencyVersion());
        return testEntityBOneToOneFollowingLeadingRecord;
    }

    @Override
    public Class<TestEntityBOneToOneFollowingLeading> domainObjectType() {
        return TestEntityBOneToOneFollowingLeading.class;
    }

    @Override
    public Class<TestEntityBOneToOneFollowingLeadingRecord> recordType() {
        return TestEntityBOneToOneFollowingLeadingRecord.class;
    }
}
