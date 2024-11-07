package io.domainlifecycles.jooq.persistence.mapper.simple;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.test.Tables;
import io.domainlifecycles.test.tables.records.TestRootSimpleRecord;
import tests.shared.persistence.domain.simple.TestRootSimple;
import tests.shared.persistence.domain.simple.TestRootSimpleId;

/**
 * Mapping JOOQ TestRootSimpleIgnoring records.
 */
public class TestRootSimpleJooqRecordMapper extends AbstractRecordMapper<TestRootSimpleRecord, TestRootSimple,
    TestRootSimple> {

    @Override
    public DomainObjectBuilder<TestRootSimple> recordToDomainObjectBuilder(TestRootSimpleRecord record) {
        if (record == null) {
            return null;
        }
        TestRootSimpleRecord testRootSimpleRecord = record.into(Tables.TEST_ROOT_SIMPLE);
        return new InnerClassDomainObjectBuilder<>(TestRootSimple.builder()
            .setId(new TestRootSimpleId(testRootSimpleRecord.getId()))
            .setName(testRootSimpleRecord.getName())
            .setConcurrencyVersion(testRootSimpleRecord.getConcurrencyVersion()));
    }

    @Override
    public TestRootSimpleRecord from(TestRootSimple testRootSimple, TestRootSimple root) {
        TestRootSimpleRecord testRootSimpleRecord = new TestRootSimpleRecord();
        testRootSimpleRecord.setId(testRootSimple.getId().value());
        testRootSimpleRecord.setName(testRootSimple.getName());
        testRootSimpleRecord.setConcurrencyVersion(testRootSimple.concurrencyVersion());
        return testRootSimpleRecord;
    }

    @Override
    public Class<TestRootSimple> domainObjectType() {
        return TestRootSimple.class;
    }

    @Override
    public Class<TestRootSimpleRecord> recordType() {
        return TestRootSimpleRecord.class;
    }
}
