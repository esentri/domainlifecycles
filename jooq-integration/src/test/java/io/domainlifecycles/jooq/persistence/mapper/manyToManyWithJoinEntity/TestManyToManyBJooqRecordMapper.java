package io.domainlifecycles.jooq.persistence.mapper.manyToManyWithJoinEntity;


import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.test.Tables;
import io.domainlifecycles.test.tables.records.TestEntityManyToManyBRecord;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestEntityManyToManyB;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestEntityManyToManyBId;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestRootManyToMany;


/**
 * Mapping JOOQ TestEntityManyToManyA records.
 */
public class TestManyToManyBJooqRecordMapper extends AbstractRecordMapper<TestEntityManyToManyBRecord,
    TestEntityManyToManyB, TestRootManyToMany> {

    @Override
    public DomainObjectBuilder<TestEntityManyToManyB> recordToDomainObjectBuilder(TestEntityManyToManyBRecord record) {
        if (record == null) {
            return null;
        }
        TestEntityManyToManyBRecord testEntityManyToManyBRecord = record.into(Tables.TEST_ENTITY_MANY_TO_MANY_B);
        return new InnerClassDomainObjectBuilder<TestEntityManyToManyB>(TestEntityManyToManyB.builder()
            .setId(new TestEntityManyToManyBId(testEntityManyToManyBRecord.getId()))
            .setName(testEntityManyToManyBRecord.getName())
            .setConcurrencyVersion(testEntityManyToManyBRecord.getConcurrencyVersion()));
    }

    @Override
    public TestEntityManyToManyBRecord from(TestEntityManyToManyB testEntityManyToManyB, TestRootManyToMany root) {
        TestEntityManyToManyBRecord testEntityManyToManyBRecord = new TestEntityManyToManyBRecord();
        testEntityManyToManyBRecord.setId(testEntityManyToManyB.getId().value());
        testEntityManyToManyBRecord.setName(testEntityManyToManyB.getName());
        testEntityManyToManyBRecord.setConcurrencyVersion(testEntityManyToManyB.concurrencyVersion());
        return testEntityManyToManyBRecord;
    }

    @Override
    public Class<TestEntityManyToManyB> domainObjectType() {
        return TestEntityManyToManyB.class;
    }

    @Override
    public Class<TestEntityManyToManyBRecord> recordType() {
        return TestEntityManyToManyBRecord.class;
    }
}
