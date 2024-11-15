package io.domainlifecycles.jooq.persistence.mapper.oneToOneFollowingFK;


import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.test.Tables;
import io.domainlifecycles.test.tables.records.TestRootOneToOneFollowingRecord;
import tests.shared.persistence.domain.oneToOneFollowingFK.TestRootOneToOneFollowing;
import tests.shared.persistence.domain.oneToOneFollowingFK.TestRootOneToOneFollowingId;

/**
 * Mapping JOOQ TestRootOneToOneFollowing records.
 */
public class TestRootOneToOneFollowingJooqRecordMapper extends AbstractRecordMapper<TestRootOneToOneFollowingRecord,
    TestRootOneToOneFollowing, TestRootOneToOneFollowing> {

    @Override
    public DomainObjectBuilder<TestRootOneToOneFollowing> recordToDomainObjectBuilder(TestRootOneToOneFollowingRecord record) {
        if (record == null) {
            return null;
        }
        TestRootOneToOneFollowingRecord testRootOneToOneFollowingRecord = record.into(
            Tables.TEST_ROOT_ONE_TO_ONE_FOLLOWING);
        return new InnerClassDomainObjectBuilder<>(TestRootOneToOneFollowing.builder()
            .setId(new TestRootOneToOneFollowingId(testRootOneToOneFollowingRecord.getId()))
            .setName(testRootOneToOneFollowingRecord.getName())
            .setConcurrencyVersion(testRootOneToOneFollowingRecord.getConcurrencyVersion()));
    }

    @Override
    public TestRootOneToOneFollowingRecord from(TestRootOneToOneFollowing testRootOneToOneFollowing,
                                                TestRootOneToOneFollowing root) {
        TestRootOneToOneFollowingRecord testRootOneToOneFollowingRecord = new TestRootOneToOneFollowingRecord();
        testRootOneToOneFollowingRecord.setId(testRootOneToOneFollowing.getId().value());
        testRootOneToOneFollowingRecord.setName(testRootOneToOneFollowing.getName());
        testRootOneToOneFollowingRecord.setConcurrencyVersion(testRootOneToOneFollowing.concurrencyVersion());
        return testRootOneToOneFollowingRecord;
    }

    @Override
    public Class<TestRootOneToOneFollowing> domainObjectType() {
        return TestRootOneToOneFollowing.class;
    }

    @Override
    public Class<TestRootOneToOneFollowingRecord> recordType() {
        return TestRootOneToOneFollowingRecord.class;
    }
}
