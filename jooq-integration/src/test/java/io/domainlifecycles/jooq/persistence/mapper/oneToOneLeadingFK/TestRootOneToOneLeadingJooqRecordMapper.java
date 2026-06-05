package io.domainlifecycles.jooq.persistence.mapper.oneToOneLeadingFK;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.test.jooq.Tables;
import io.domainlifecycles.test.jooq.tables.records.TestRootOneToOneLeadingRecord;
import tests.shared.persistence.domain.oneToOneLeadingFK.TestRootOneToOneLeading;
import tests.shared.persistence.domain.oneToOneLeadingFK.TestRootOneToOneLeadingId;

/**
 * Mapping JOOQ TestRootOneToOneLeading records.
 */
public class TestRootOneToOneLeadingJooqRecordMapper extends AbstractRecordMapper<TestRootOneToOneLeadingRecord,
    TestRootOneToOneLeading, TestRootOneToOneLeading> {

    @Override
    public DomainObjectBuilder<TestRootOneToOneLeading> recordToDomainObjectBuilder(TestRootOneToOneLeadingRecord record) {
        if (record == null) {
            return null;
        }
        TestRootOneToOneLeadingRecord testRootOneToOneLeadingRecord = record.into(Tables.TEST_ROOT_ONE_TO_ONE_LEADING);
        return new InnerClassDomainObjectBuilder<>(TestRootOneToOneLeading.builder()
            .setId(new TestRootOneToOneLeadingId(testRootOneToOneLeadingRecord.getId()))
            .setName(testRootOneToOneLeadingRecord.getName())
            .setConcurrencyVersion(testRootOneToOneLeadingRecord.getConcurrencyVersion()));
    }

    @Override
    public TestRootOneToOneLeadingRecord from(TestRootOneToOneLeading testRootOneToOneLeading,
                                              TestRootOneToOneLeading root) {
        TestRootOneToOneLeadingRecord testRootOneToOneLeadingRecord = new TestRootOneToOneLeadingRecord();
        testRootOneToOneLeadingRecord.setId(testRootOneToOneLeading.getId().value());
        testRootOneToOneLeadingRecord.setTestEntityId(testRootOneToOneLeading.getTestEntityOneToOneLeading() == null
            ? null : testRootOneToOneLeading.getTestEntityOneToOneLeading().getId().value());
        testRootOneToOneLeadingRecord.setName(testRootOneToOneLeading.getName());
        testRootOneToOneLeadingRecord.setConcurrencyVersion(testRootOneToOneLeading.concurrencyVersion());
        return testRootOneToOneLeadingRecord;
    }

    @Override
    public Class<TestRootOneToOneLeading> domainObjectType() {
        return TestRootOneToOneLeading.class;
    }

    @Override
    public Class<TestRootOneToOneLeadingRecord> recordType() {
        return TestRootOneToOneLeadingRecord.class;
    }
}
