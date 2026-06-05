package io.domainlifecycles.jooq.persistence.mapper.complex;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.test.jooq.Tables;
import io.domainlifecycles.test.jooq.tables.records.TestRootRecord;
import tests.shared.persistence.domain.complex.TestRoot;
import tests.shared.persistence.domain.complex.TestRootId;

/**
 * Mapping JOOQ Testroot records.
 */
public class TestRootJooqRecordMapper extends AbstractRecordMapper<TestRootRecord, TestRoot, TestRoot> {

    @Override
    public DomainObjectBuilder<TestRoot> recordToDomainObjectBuilder(TestRootRecord record) {
        if (record == null) {
            return null;
        }
        TestRootRecord testRootRecord = record.into(Tables.TEST_ROOT);
        return new InnerClassDomainObjectBuilder<TestRoot>(TestRoot.builder()
            .setId(new TestRootId(testRootRecord.getId()))
            .setName(testRootRecord.getName())
            .setConcurrencyVersion(testRootRecord.getConcurrencyVersion()));
    }

    @Override
    public TestRootRecord from(TestRoot testRoot, TestRoot root) {
        TestRootRecord testRootRecord = new TestRootRecord();
        testRootRecord.setId(testRoot.getId().value());
        testRootRecord.setName(testRoot.getName());
        testRootRecord.setConcurrencyVersion(testRoot.concurrencyVersion());
        return testRootRecord;
    }

    @Override
    public Class<TestRoot> domainObjectType() {
        return TestRoot.class;
    }

    @Override
    public Class<TestRootRecord> recordType() {
        return TestRootRecord.class;
    }
}
