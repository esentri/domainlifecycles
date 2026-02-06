package io.domainlifecycles.jooq.persistence.mapper.oneToMany;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.test.jooq.Tables;
import io.domainlifecycles.test.jooq.tables.records.TestEntityOneToManyRecord;
import tests.shared.persistence.domain.oneToMany.TestEntityOneToMany;
import tests.shared.persistence.domain.oneToMany.TestEntityOneToManyId;
import tests.shared.persistence.domain.oneToMany.TestRootOneToMany;
import tests.shared.persistence.domain.oneToMany.TestRootOneToManyId;

/**
 * Mapping JOOQ TestEntityOneToOneFollowing records.
 */
public class TestOneToManyJooqRecordMapper extends AbstractRecordMapper<TestEntityOneToManyRecord,
    TestEntityOneToMany, TestRootOneToMany> {

    @Override
    public DomainObjectBuilder<TestEntityOneToMany> recordToDomainObjectBuilder(TestEntityOneToManyRecord record) {
        if (record == null) {
            return null;
        }
        TestEntityOneToManyRecord testEntityOneToManyRecord = record.into(Tables.TEST_ENTITY_ONE_TO_MANY);
        return new InnerClassDomainObjectBuilder<>(TestEntityOneToMany.builder()
            .setId(new TestEntityOneToManyId(testEntityOneToManyRecord.getId()))
            .setName(testEntityOneToManyRecord.getName())
            .setTestRootId(testEntityOneToManyRecord.getTestRootId() == null ?
                null : new TestRootOneToManyId(testEntityOneToManyRecord.getTestRootId()))
            .setConcurrencyVersion(testEntityOneToManyRecord.getConcurrencyVersion()));
    }

    @Override
    public TestEntityOneToManyRecord from(TestEntityOneToMany testEntityOneToMany, TestRootOneToMany root) {
        TestEntityOneToManyRecord testEntityOneToManyRecord = new TestEntityOneToManyRecord();
        testEntityOneToManyRecord.setId(testEntityOneToMany.getId().value());
        testEntityOneToManyRecord.setName(testEntityOneToMany.getName());
        testEntityOneToManyRecord.setTestRootId(testEntityOneToMany.getTestRootId() == null ?
            null : testEntityOneToMany.getTestRootId().value());
        testEntityOneToManyRecord.setConcurrencyVersion(testEntityOneToMany.concurrencyVersion());
        return testEntityOneToManyRecord;
    }

    @Override
    public Class<TestEntityOneToMany> domainObjectType() {
        return TestEntityOneToMany.class;
    }

    @Override
    public Class<TestEntityOneToManyRecord> recordType() {
        return TestEntityOneToManyRecord.class;
    }
}
