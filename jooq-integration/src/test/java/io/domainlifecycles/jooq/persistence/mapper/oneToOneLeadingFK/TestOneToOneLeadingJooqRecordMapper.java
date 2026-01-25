package io.domainlifecycles.jooq.persistence.mapper.oneToOneLeadingFK;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.test.jooq.Tables;
import io.domainlifecycles.test.jooq.tables.records.TestEntityOneToOneLeadingRecord;
import tests.shared.persistence.domain.oneToOneLeadingFK.TestEntityOneToOneLeading;
import tests.shared.persistence.domain.oneToOneLeadingFK.TestEntityOneToOneLeadingId;
import tests.shared.persistence.domain.oneToOneLeadingFK.TestRootOneToOneLeading;

/**
 * Mapping JOOQ TestEntityOneToOneLeading records.
 */
public class TestOneToOneLeadingJooqRecordMapper extends AbstractRecordMapper<TestEntityOneToOneLeadingRecord,
    TestEntityOneToOneLeading, TestRootOneToOneLeading> {

    @Override
    public DomainObjectBuilder<TestEntityOneToOneLeading> recordToDomainObjectBuilder(TestEntityOneToOneLeadingRecord record) {
        if (record == null) {
            return null;
        }
        TestEntityOneToOneLeadingRecord testEntityOneToOneLeadingRecord = record.into(
            Tables.TEST_ENTITY_ONE_TO_ONE_LEADING);
        return new InnerClassDomainObjectBuilder<>(TestEntityOneToOneLeading.builder()
            .setId(new TestEntityOneToOneLeadingId(testEntityOneToOneLeadingRecord.getId()))
            .setName(testEntityOneToOneLeadingRecord.getName())
            .setConcurrencyVersion(testEntityOneToOneLeadingRecord.getConcurrencyVersion()));
    }

    @Override
    public TestEntityOneToOneLeadingRecord from(TestEntityOneToOneLeading testEntityOneToOneLeading,
                                                TestRootOneToOneLeading root) {
        TestEntityOneToOneLeadingRecord testEntityOneToOneLeadingRecord = new TestEntityOneToOneLeadingRecord();
        testEntityOneToOneLeadingRecord.setId(testEntityOneToOneLeading.getId().value());
        testEntityOneToOneLeadingRecord.setName(testEntityOneToOneLeading.getName());
        testEntityOneToOneLeadingRecord.setConcurrencyVersion(testEntityOneToOneLeading.concurrencyVersion());
        return testEntityOneToOneLeadingRecord;
    }

    @Override
    public Class<TestEntityOneToOneLeading> domainObjectType() {
        return TestEntityOneToOneLeading.class;
    }

    @Override
    public Class<TestEntityOneToOneLeadingRecord> recordType() {
        return TestEntityOneToOneLeadingRecord.class;
    }
}
