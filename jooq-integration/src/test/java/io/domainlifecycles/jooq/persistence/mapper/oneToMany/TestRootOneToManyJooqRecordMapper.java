package io.domainlifecycles.jooq.persistence.mapper.oneToMany;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.test.Tables;
import io.domainlifecycles.test.tables.records.TestRootOneToManyRecord;
import tests.shared.persistence.domain.oneToMany.TestRootOneToMany;
import tests.shared.persistence.domain.oneToMany.TestRootOneToManyId;

/**
 * Mapping JOOQ TestRootOneToOneFollowing records.
 */
public class TestRootOneToManyJooqRecordMapper extends AbstractRecordMapper<TestRootOneToManyRecord,
    TestRootOneToMany, TestRootOneToMany> {

    @Override
    public DomainObjectBuilder<TestRootOneToMany> recordToDomainObjectBuilder(TestRootOneToManyRecord record) {
        if (record == null) {
            return null;
        }
        TestRootOneToManyRecord testRootOneToManyRecord = record.into(Tables.TEST_ROOT_ONE_TO_MANY);
        return new InnerClassDomainObjectBuilder<>(TestRootOneToMany.builder()
            .setId(new TestRootOneToManyId(testRootOneToManyRecord.getId()))
            .setName(testRootOneToManyRecord.getName())
            .setConcurrencyVersion(testRootOneToManyRecord.getConcurrencyVersion()));
    }

    @Override
    public TestRootOneToManyRecord from(TestRootOneToMany testRootOneToMany, TestRootOneToMany root) {
        TestRootOneToManyRecord testRootOneToManyRecord = new TestRootOneToManyRecord();
        testRootOneToManyRecord.setId(testRootOneToMany.getId().value());
        testRootOneToManyRecord.setName(testRootOneToMany.getName());
        testRootOneToManyRecord.setConcurrencyVersion(testRootOneToMany.concurrencyVersion());
        return testRootOneToManyRecord;
    }

    @Override
    public Class<TestRootOneToMany> domainObjectType() {
        return TestRootOneToMany.class;
    }

    @Override
    public Class<TestRootOneToManyRecord> recordType() {
        return TestRootOneToManyRecord.class;
    }
}
