package io.domainlifecycles.jooq.persistence.mapper.valueobjects;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.test.tables.records.SimpleVoOneToMany_2Record;
import tests.shared.persistence.domain.valueobjects.SimpleVoOneToMany2;
import tests.shared.persistence.domain.valueobjects.VoAggregateRoot;

import io.domainlifecycles.test.Tables;

/**
 * Mapping JOOQ TestRootSimpleIgnoring records.
 */
public class SimpleVoOneToMany2JooqRecordMapper extends AbstractRecordMapper<SimpleVoOneToMany_2Record,
    SimpleVoOneToMany2, VoAggregateRoot> {

    @Override
    public DomainObjectBuilder<SimpleVoOneToMany2> recordToDomainObjectBuilder(SimpleVoOneToMany_2Record record) {
        if (record == null) {
            return null;
        }
        SimpleVoOneToMany_2Record simpleVoOneToManyRecord = record.into(Tables.SIMPLE_VO_ONE_TO_MANY_2);
        return new InnerClassDomainObjectBuilder<>(SimpleVoOneToMany2.builder()
            .setValue(simpleVoOneToManyRecord.getValue()));
    }

    @Override
    public SimpleVoOneToMany_2Record from(SimpleVoOneToMany2 simpleVoOneToMany, VoAggregateRoot root) {
        SimpleVoOneToMany_2Record simpleVoOneToManyRecord = new SimpleVoOneToMany_2Record();
        simpleVoOneToManyRecord.setValue(simpleVoOneToMany.getValue());
        return simpleVoOneToManyRecord;
    }

    @Override
    public Class<SimpleVoOneToMany2> domainObjectType() {
        return SimpleVoOneToMany2.class;
    }

    @Override
    public Class<SimpleVoOneToMany_2Record> recordType() {
        return SimpleVoOneToMany_2Record.class;
    }
}
