package io.domainlifecycles.jooq.persistence.mapper.complex;

import lombok.RequiredArgsConstructor;
import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.test.Tables;
import io.domainlifecycles.test.tables.records.TestEntity_1Record;
import tests.shared.persistence.domain.complex.TestEntity1;
import tests.shared.persistence.domain.complex.TestEntity1Id;
import tests.shared.persistence.domain.complex.TestRoot;
import tests.shared.persistence.domain.complex.TestRootId;

/**
 * Mapping JOOQ Testroot records.
 */
@RequiredArgsConstructor
public class Test1JooqRecordMapper extends AbstractRecordMapper<TestEntity_1Record, TestEntity1, TestRoot> {

    @Override
    public DomainObjectBuilder<TestEntity1> recordToDomainObjectBuilder(TestEntity_1Record record) {
        if (record == null) {
            return null;
        }
        TestEntity_1Record testEntity_1Record = record.into(Tables.TEST_ENTITY_1);
        return new InnerClassDomainObjectBuilder<TestEntity1>(TestEntity1.builder()
            .setId(new TestEntity1Id(testEntity_1Record.getId()))
            .setTestRootId(new TestRootId(testEntity_1Record.getTestRootId()))
            .setName(testEntity_1Record.getName())
            .setConcurrencyVersion(testEntity_1Record.getConcurrencyVersion()));
    }

    @Override
    public TestEntity_1Record from(TestEntity1 testEntity1, TestRoot root) {
        TestEntity_1Record testEntity_1Record = new TestEntity_1Record();
        testEntity_1Record.setTestRootId(testEntity1.getTestRootId().value());
        testEntity_1Record.setId(testEntity1.getId().value());
        testEntity_1Record.setName(testEntity1.getName());
        testEntity_1Record.setTestEntity_2IdA(
            testEntity1.getTestEntity2A() != null ? testEntity1.getTestEntity2A().getId().value() : null);
        testEntity_1Record.setTestEntity_2IdB(
            testEntity1.getTestEntity2B() != null ? testEntity1.getTestEntity2B().getId().value() : null);
        testEntity_1Record.setConcurrencyVersion(testEntity1.concurrencyVersion());
        return testEntity_1Record;
    }

    @Override
    public Class<TestEntity1> domainObjectType() {
        return TestEntity1.class;
    }

    @Override
    public Class<TestEntity_1Record> recordType() {
        return TestEntity_1Record.class;
    }
}
