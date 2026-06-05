package io.domainlifecycles.jooq.persistence.mapper.valueobjects;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.test.jooq.Tables;
import io.domainlifecycles.test.jooq.tables.records.SimpleVoOneToMany_3Record;
import tests.shared.persistence.domain.valueobjects.SimpleVoOneToMany3;
import tests.shared.persistence.domain.valueobjects.VoAggregateRoot;

/**
 * Mapping JOOQ TestRootSimpleIgnoring records.
 */
public class SimpleVoOneToMany3JooqRecordMapper extends AbstractRecordMapper<SimpleVoOneToMany_3Record,
    SimpleVoOneToMany3, VoAggregateRoot> {

    @Override
    public DomainObjectBuilder<SimpleVoOneToMany3> recordToDomainObjectBuilder(SimpleVoOneToMany_3Record record) {
        if (record == null) {
            return null;
        }
        SimpleVoOneToMany_3Record simpleVoOneToManyRecord = record.into(Tables.SIMPLE_VO_ONE_TO_MANY_3);
        return new InnerClassDomainObjectBuilder<>(SimpleVoOneToMany3.builder()
            .setValue(simpleVoOneToManyRecord.getValue()));
    }

    @Override
    public SimpleVoOneToMany_3Record from(SimpleVoOneToMany3 simpleVoOneToMany, VoAggregateRoot root) {
        SimpleVoOneToMany_3Record simpleVoOneToManyRecord = new SimpleVoOneToMany_3Record();
        simpleVoOneToManyRecord.setValue(simpleVoOneToMany.getValue());
        return simpleVoOneToManyRecord;
    }

    @Override
    public Class<SimpleVoOneToMany3> domainObjectType() {
        return SimpleVoOneToMany3.class;
    }

    @Override
    public Class<SimpleVoOneToMany_3Record> recordType() {
        return SimpleVoOneToMany_3Record.class;
    }
}
