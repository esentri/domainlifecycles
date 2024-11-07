package io.domainlifecycles.jooq.persistence.mapper.valueobjects;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.test.Tables;
import io.domainlifecycles.test.tables.records.SimpleVoOneToManyRecord;
import tests.shared.persistence.domain.valueobjects.SimpleVoOneToMany;
import tests.shared.persistence.domain.valueobjects.VoAggregateRoot;

/**
 * Mapping JOOQ TestRootSimpleIgnoring records.
 */
public class SimpleVoOneToManyJooqRecordMapper extends AbstractRecordMapper<SimpleVoOneToManyRecord,
    SimpleVoOneToMany, VoAggregateRoot> {

    @Override
    public DomainObjectBuilder<SimpleVoOneToMany> recordToDomainObjectBuilder(SimpleVoOneToManyRecord record) {
        if (record == null) {
            return null;
        }
        SimpleVoOneToManyRecord simpleVoOneToManyRecord = record.into(Tables.SIMPLE_VO_ONE_TO_MANY);
        return new InnerClassDomainObjectBuilder<>(SimpleVoOneToMany.builder()
            .setValue(simpleVoOneToManyRecord.getValue()));
    }

    @Override
    public SimpleVoOneToManyRecord from(SimpleVoOneToMany simpleVoOneToMany, VoAggregateRoot root) {
        SimpleVoOneToManyRecord simpleVoOneToManyRecord = new SimpleVoOneToManyRecord();
        simpleVoOneToManyRecord.setValue(simpleVoOneToMany.getValue());
        return simpleVoOneToManyRecord;
    }

    @Override
    public Class<SimpleVoOneToMany> domainObjectType() {
        return SimpleVoOneToMany.class;
    }

    @Override
    public Class<SimpleVoOneToManyRecord> recordType() {
        return SimpleVoOneToManyRecord.class;
    }
}
