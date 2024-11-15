package io.domainlifecycles.jooq.persistence.mapper.manyToManyWithJoinEntity;


import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.test.Tables;
import io.domainlifecycles.test.tables.records.TestEntityManyToManyARecord;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestEntityManyToManyA;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestEntityManyToManyAId;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestRootManyToMany;


/**
 * Mapping JOOQ TestEntityManyToManyA records.
 */
public class TestManyToManyAJooqRecordMapper extends AbstractRecordMapper<TestEntityManyToManyARecord,
    TestEntityManyToManyA, TestRootManyToMany> {

    @Override
    public DomainObjectBuilder<TestEntityManyToManyA> recordToDomainObjectBuilder(TestEntityManyToManyARecord record) {
        if (record == null) {
            return null;
        }
        TestEntityManyToManyARecord testEntityManyToManyARecord = record.into(Tables.TEST_ENTITY_MANY_TO_MANY_A);
        return new InnerClassDomainObjectBuilder<>(TestEntityManyToManyA.builder()
            .setId(new TestEntityManyToManyAId(testEntityManyToManyARecord.getId()))
            .setName(testEntityManyToManyARecord.getName())
            .setConcurrencyVersion(testEntityManyToManyARecord.getConcurrencyVersion()));
    }

    @Override
    public TestEntityManyToManyARecord from(TestEntityManyToManyA testEntityManyToManyA, TestRootManyToMany root) {
        TestEntityManyToManyARecord testEntityManyToManyARecord = new TestEntityManyToManyARecord();
        testEntityManyToManyARecord.setId(testEntityManyToManyA.getId().value());
        testEntityManyToManyARecord.setTestRootId(testEntityManyToManyA.getTestRootManyToMany().getId().value());
        testEntityManyToManyARecord.setName(testEntityManyToManyA.getName());
        testEntityManyToManyARecord.setConcurrencyVersion(testEntityManyToManyA.concurrencyVersion());
        return testEntityManyToManyARecord;
    }

    @Override
    public Class<TestEntityManyToManyA> domainObjectType() {
        return TestEntityManyToManyA.class;
    }

    @Override
    public Class<TestEntityManyToManyARecord> recordType() {
        return TestEntityManyToManyARecord.class;
    }
}
