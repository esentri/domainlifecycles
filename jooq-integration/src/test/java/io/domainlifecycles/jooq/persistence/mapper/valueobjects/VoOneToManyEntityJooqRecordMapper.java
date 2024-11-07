package io.domainlifecycles.jooq.persistence.mapper.valueobjects;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.test.Tables;
import io.domainlifecycles.test.tables.records.VoOneToManyEntityRecord;
import tests.shared.persistence.domain.valueobjects.VoAggregateRoot;
import tests.shared.persistence.domain.valueobjects.VoOneToManyEntity;

/**
 * Mapping JOOQ TestRootSimpleIgnoring records.
 */
public class VoOneToManyEntityJooqRecordMapper extends AbstractRecordMapper<VoOneToManyEntityRecord,
    VoOneToManyEntity, VoAggregateRoot> {

    @Override
    public DomainObjectBuilder<VoOneToManyEntity> recordToDomainObjectBuilder(VoOneToManyEntityRecord record) {
        if (record == null) {
            return null;
        }
        VoOneToManyEntityRecord voOneToManyEntityRecord = record.into(Tables.VO_ONE_TO_MANY_ENTITY);
        return new InnerClassDomainObjectBuilder<>(VoOneToManyEntity.builder()
            .setValue(voOneToManyEntityRecord.getValue()));
    }

    @Override
    public VoOneToManyEntityRecord from(VoOneToManyEntity voOneToManyEntity, VoAggregateRoot root) {
        VoOneToManyEntityRecord voOneToManyEntityRecord = new VoOneToManyEntityRecord();
        voOneToManyEntityRecord.setValue(voOneToManyEntity.getValue());
        return voOneToManyEntityRecord;
    }

    @Override
    public Class<VoOneToManyEntity> domainObjectType() {
        return VoOneToManyEntity.class;
    }

    @Override
    public Class<VoOneToManyEntityRecord> recordType() {
        return VoOneToManyEntityRecord.class;
    }
}
