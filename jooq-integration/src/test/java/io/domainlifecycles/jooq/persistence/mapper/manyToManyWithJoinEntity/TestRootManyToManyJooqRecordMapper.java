package io.domainlifecycles.jooq.persistence.mapper.manyToManyWithJoinEntity;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.test.tables.records.TestRootManyToManyRecord;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestRootManyToMany;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestRootManyToManyId;

import io.domainlifecycles.test.Tables;

/**
 * Mapping JOOQ TestRootManyToMany records.
 */
public class TestRootManyToManyJooqRecordMapper extends AbstractRecordMapper<TestRootManyToManyRecord,
    TestRootManyToMany, TestRootManyToMany> {

    @Override
    public DomainObjectBuilder<TestRootManyToMany> recordToDomainObjectBuilder(TestRootManyToManyRecord record) {
        if (record == null) {
            return null;
        }
        TestRootManyToManyRecord testRootManyToManyRecord = record.into(Tables.TEST_ROOT_MANY_TO_MANY);
        return new InnerClassDomainObjectBuilder<>(TestRootManyToMany.builder()
            .setId(new TestRootManyToManyId(testRootManyToManyRecord.getId()))
            .setName(testRootManyToManyRecord.getName())
            .setConcurrencyVersion(testRootManyToManyRecord.getConcurrencyVersion()));
    }

    @Override
    public TestRootManyToManyRecord from(TestRootManyToMany testRootManyToMany, TestRootManyToMany root) {
        TestRootManyToManyRecord testRootManyToManyRecord = new TestRootManyToManyRecord();
        testRootManyToManyRecord.setId(testRootManyToMany.getId().value());
        testRootManyToManyRecord.setName(testRootManyToMany.getName());
        testRootManyToManyRecord.setConcurrencyVersion(testRootManyToMany.concurrencyVersion());
        return testRootManyToManyRecord;
    }

    @Override
    public Class<TestRootManyToMany> domainObjectType() {
        return TestRootManyToMany.class;
    }

    @Override
    public Class<TestRootManyToManyRecord> recordType() {
        return TestRootManyToManyRecord.class;
    }
}
