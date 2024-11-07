/*
 *     ___
 *     │   ╲                 _
 *     │    ╲ ___ _ __  __ _(_)_ _
 *     |     ╲ _ ╲ '  ╲╱ _` │ │ ' ╲
 *     |_____╱___╱_│_│_╲__,_│_│_||_|
 *     │ │  (_)╱ _│___ __ _  _ __│ |___ ___
 *     │ │__│ │  _╱ -_) _│ ││ ╱ _│ ╱ -_|_-<
 *     │____│_│_│ ╲___╲__│╲_, ╲__│_╲___╱__╱
 *                      |__╱
 *
 *  Copyright 2019-2024 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
