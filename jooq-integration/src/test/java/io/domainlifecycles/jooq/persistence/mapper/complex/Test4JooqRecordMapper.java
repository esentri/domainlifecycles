package io.domainlifecycles.jooq.persistence.mapper.complex;


import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.test.jooq.Tables;
import io.domainlifecycles.test.jooq.tables.records.TestEntity_4Record;
import tests.shared.persistence.domain.complex.TestEntity3Id;
import tests.shared.persistence.domain.complex.TestEntity4;
import tests.shared.persistence.domain.complex.TestEntity4Id;
import tests.shared.persistence.domain.complex.TestRoot;


/**
 * Mapping JOOQ Testroot records.
 */
public class Test4JooqRecordMapper extends AbstractRecordMapper<TestEntity_4Record, TestEntity4, TestRoot> {

    @Override
    public DomainObjectBuilder<TestEntity4> recordToDomainObjectBuilder(TestEntity_4Record record) {
        if (record == null) {
            return null;
        }
        TestEntity_4Record testEntity_4Record = record.into(Tables.TEST_ENTITY_4);
        return new InnerClassDomainObjectBuilder<>(TestEntity4.builder()
            .setId(new TestEntity4Id(testEntity_4Record.getId()))
            .setTestEntity3Id(new TestEntity3Id(testEntity_4Record.getTestEntity_3Id()))
            .setName(testEntity_4Record.getName())
            .setConcurrencyVersion(testEntity_4Record.getConcurrencyVersion()));

    }

    @Override
    public TestEntity_4Record from(TestEntity4 testEntity4, TestRoot root) {
        TestEntity_4Record testEntity_4Record = new TestEntity_4Record();
        testEntity_4Record.setId(testEntity4.getId().value());
        testEntity_4Record.setName(testEntity4.getName());
        testEntity_4Record.setTestEntity_3Id(testEntity4.getTestEntity3Id().value());
        testEntity_4Record.setConcurrencyVersion(testEntity4.concurrencyVersion());
        return testEntity_4Record;
    }

    @Override
    public Class<TestEntity4> domainObjectType() {
        return TestEntity4.class;
    }

    @Override
    public Class<TestEntity_4Record> recordType() {
        return TestEntity_4Record.class;
    }
}
