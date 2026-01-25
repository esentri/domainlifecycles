package io.domainlifecycles.jooq.persistence.mapper.complex;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.test.jooq.Tables;
import io.domainlifecycles.test.jooq.tables.records.TestEntity_2Record;
import tests.shared.persistence.domain.complex.TestEntity2;
import tests.shared.persistence.domain.complex.TestEntity2Id;
import tests.shared.persistence.domain.complex.TestRoot;
import tests.shared.persistence.domain.complex.TestRootId;


/**
 * Mapping JOOQ Testroot records.
 */
public class Test2JooqRecordMapper extends AbstractRecordMapper<TestEntity_2Record, TestEntity2, TestRoot> {

    @Override
    public DomainObjectBuilder<TestEntity2> recordToDomainObjectBuilder(TestEntity_2Record record) {
        if (record == null) {
            return null;
        }
        TestEntity_2Record testEntity_2Record = record.into(Tables.TEST_ENTITY_2);
        return new InnerClassDomainObjectBuilder<TestEntity2>(TestEntity2.builder()
            .setId(new TestEntity2Id(testEntity_2Record.getId()))
            .setTestRootId(new TestRootId(testEntity_2Record.getTestRootId()))
            .setName(testEntity_2Record.getName())
            .setConcurrencyVersion(testEntity_2Record.getConcurrencyVersion()));
    }

    @Override
    public TestEntity_2Record from(TestEntity2 testEntity2, TestRoot root) {
        TestEntity_2Record testEntity_2Record = new TestEntity_2Record();
        testEntity_2Record.setTestRootId(testEntity2.getTestRootId().value());
        testEntity_2Record.setId(testEntity2.getId().value());
        testEntity_2Record.setName(testEntity2.getName());
        testEntity_2Record.setConcurrencyVersion(testEntity2.concurrencyVersion());
        return testEntity_2Record;
    }

    @Override
    public Class<TestEntity2> domainObjectType() {
        return TestEntity2.class;
    }

    @Override
    public Class<TestEntity_2Record> recordType() {
        return TestEntity_2Record.class;
    }
}
