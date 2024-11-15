package io.domainlifecycles.jooq.persistence.mapper.simpleUuid;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.test.Tables;
import io.domainlifecycles.test.tables.records.TestRootSimpleUuidRecord;
import tests.shared.persistence.domain.simpleUuid.TestRootSimpleUuid;
import tests.shared.persistence.domain.simpleUuid.TestRootSimpleUuidId;

import java.util.UUID;

/**
 * Mapping JOOQ TestRootSimpleIgnoring records.
 */
public class TestRootSimpleUuidJooqRecordMapper extends AbstractRecordMapper<TestRootSimpleUuidRecord,
    TestRootSimpleUuid, TestRootSimpleUuid> {

    @Override
    public DomainObjectBuilder<TestRootSimpleUuid> recordToDomainObjectBuilder(TestRootSimpleUuidRecord record) {
        if (record == null) {
            return null;
        }
        TestRootSimpleUuidRecord testRootSimpleRecord = record.into(Tables.TEST_ROOT_SIMPLE_UUID);
        return new InnerClassDomainObjectBuilder<>(TestRootSimpleUuid.builder()
            .setId(new TestRootSimpleUuidId(UUID.fromString(testRootSimpleRecord.getId())))
            .setName(testRootSimpleRecord.getName())
            .setConcurrencyVersion(testRootSimpleRecord.getConcurrencyVersion()));
    }

    @Override
    public TestRootSimpleUuidRecord from(TestRootSimpleUuid testRootSimpleUuid, TestRootSimpleUuid root) {
        TestRootSimpleUuidRecord testRootSimpleRecord = new TestRootSimpleUuidRecord();
        testRootSimpleRecord.setId(testRootSimpleUuid.getId().value().toString());
        testRootSimpleRecord.setName(testRootSimpleUuid.getName());
        testRootSimpleRecord.setConcurrencyVersion(testRootSimpleUuid.concurrencyVersion());
        return testRootSimpleRecord;
    }

    @Override
    public Class<TestRootSimpleUuid> domainObjectType() {
        return TestRootSimpleUuid.class;
    }

    @Override
    public Class<TestRootSimpleUuidRecord> recordType() {
        return TestRootSimpleUuidRecord.class;
    }
}
