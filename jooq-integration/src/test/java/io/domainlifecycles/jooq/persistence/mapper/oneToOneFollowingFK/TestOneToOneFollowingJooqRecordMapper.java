package io.domainlifecycles.jooq.persistence.mapper.oneToOneFollowingFK;


import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.test.jooq.Tables;
import io.domainlifecycles.test.jooq.tables.records.TestEntityOneToOneFollowingRecord;
import tests.shared.persistence.domain.oneToOneFollowingFK.TestEntityOneToOneFollowing;
import tests.shared.persistence.domain.oneToOneFollowingFK.TestEntityOneToOneFollowingId;
import tests.shared.persistence.domain.oneToOneFollowingFK.TestRootOneToOneFollowing;
import tests.shared.persistence.domain.oneToOneFollowingFK.TestRootOneToOneFollowingId;


/**
 * Mapping JOOQ TestEntityOneToOneFollowing records.
 */
public class TestOneToOneFollowingJooqRecordMapper extends AbstractRecordMapper<TestEntityOneToOneFollowingRecord,
    TestEntityOneToOneFollowing, TestRootOneToOneFollowing> {

    @Override
    public DomainObjectBuilder<TestEntityOneToOneFollowing> recordToDomainObjectBuilder(TestEntityOneToOneFollowingRecord record) {
        if (record == null) {
            return null;
        }
        TestEntityOneToOneFollowingRecord testEntityOneToOneFollowingRecord = record.into(
            Tables.TEST_ENTITY_ONE_TO_ONE_FOLLOWING);
        return new InnerClassDomainObjectBuilder<>(TestEntityOneToOneFollowing.builder()
            .setId(new TestEntityOneToOneFollowingId(testEntityOneToOneFollowingRecord.getId()))
            .setName(testEntityOneToOneFollowingRecord.getName())
            .setTestRootId(testEntityOneToOneFollowingRecord.getTestRootId() == null ?
                null : new TestRootOneToOneFollowingId(testEntityOneToOneFollowingRecord.getTestRootId()))
            .setConcurrencyVersion(testEntityOneToOneFollowingRecord.getConcurrencyVersion()));
    }

    @Override
    public TestEntityOneToOneFollowingRecord from(TestEntityOneToOneFollowing testEntityOneToOneFollowing,
                                                  TestRootOneToOneFollowing root) {
        TestEntityOneToOneFollowingRecord testEntityOneToOneFollowingRecord = new TestEntityOneToOneFollowingRecord();
        testEntityOneToOneFollowingRecord.setId(testEntityOneToOneFollowing.getId().value());
        testEntityOneToOneFollowingRecord.setName(testEntityOneToOneFollowing.getName());
        testEntityOneToOneFollowingRecord.setTestRootId(testEntityOneToOneFollowing.getTestRootId() == null ?
            null : testEntityOneToOneFollowing.getTestRootId().value());
        testEntityOneToOneFollowingRecord.setConcurrencyVersion(testEntityOneToOneFollowing.concurrencyVersion());
        return testEntityOneToOneFollowingRecord;
    }

    @Override
    public Class<TestEntityOneToOneFollowing> domainObjectType() {
        return TestEntityOneToOneFollowing.class;
    }

    @Override
    public Class<TestEntityOneToOneFollowingRecord> recordType() {
        return TestEntityOneToOneFollowingRecord.class;
    }
}
