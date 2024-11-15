package io.domainlifecycles.jooq.persistence.mapper.manyToManyWithJoinEntity;


import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.test.Tables;
import io.domainlifecycles.test.tables.records.TestEntityManyToManyJoinRecord;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestEntityManyToManyAId;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestEntityManyToManyJoin;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestEntityManyToManyJoinId;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestRootManyToMany;


/**
 * Mapping JOOQ TestEntityManyToManyA records.
 */
public class TestManyToManyJoinJooqRecordMapper extends AbstractRecordMapper<TestEntityManyToManyJoinRecord,
    TestEntityManyToManyJoin, TestRootManyToMany> {

    @Override
    public DomainObjectBuilder<TestEntityManyToManyJoin> recordToDomainObjectBuilder(TestEntityManyToManyJoinRecord record) {
        if (record == null) {
            return null;
        }
        TestEntityManyToManyJoinRecord testEntityManyToManyARecord = record.into(Tables.TEST_ENTITY_MANY_TO_MANY_JOIN);
        return new InnerClassDomainObjectBuilder<>(TestEntityManyToManyJoin.builder()
            .setId(new TestEntityManyToManyJoinId(testEntityManyToManyARecord.getId()))
            .setTestEntityManyToManyAId(new TestEntityManyToManyAId(testEntityManyToManyARecord.getTestEntityAId()))
            .setConcurrencyVersion(testEntityManyToManyARecord.getConcurrencyVersion()));
    }

    @Override
    public TestEntityManyToManyJoinRecord from(TestEntityManyToManyJoin testEntityManyToManyJoin,
                                               TestRootManyToMany root) {
        TestEntityManyToManyJoinRecord testEntityManyToManyJoinRecord = new TestEntityManyToManyJoinRecord();
        testEntityManyToManyJoinRecord.setId(testEntityManyToManyJoin.getId().value());
        testEntityManyToManyJoinRecord.setTestEntityAId(testEntityManyToManyJoin.getTestEntityManyToManyAId().value());
        testEntityManyToManyJoinRecord.setTestEntityBId(
            testEntityManyToManyJoin.getTestEntityManyToManyB().getId().value());
        testEntityManyToManyJoinRecord.setConcurrencyVersion(testEntityManyToManyJoin.concurrencyVersion());
        return testEntityManyToManyJoinRecord;
    }

    @Override
    public Class<TestEntityManyToManyJoin> domainObjectType() {
        return TestEntityManyToManyJoin.class;
    }

    @Override
    public Class<TestEntityManyToManyJoinRecord> recordType() {
        return TestEntityManyToManyJoinRecord.class;
    }
}
