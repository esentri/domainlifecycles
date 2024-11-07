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
import io.domainlifecycles.test.tables.records.VoOneToManyEntity_2Record;
import tests.shared.persistence.domain.valueobjects.VoAggregateRoot;
import tests.shared.persistence.domain.valueobjects.VoOneToManyEntity2;

/**
 * Mapping JOOQ TestRootSimpleIgnoring records.
 */
public class VoOneToManyEntity2JooqRecordMapper extends AbstractRecordMapper<VoOneToManyEntity_2Record,
    VoOneToManyEntity2, VoAggregateRoot> {

    @Override
    public DomainObjectBuilder<VoOneToManyEntity2> recordToDomainObjectBuilder(VoOneToManyEntity_2Record record) {
        if (record == null) {
            return null;
        }
        VoOneToManyEntity_2Record voOneToManyEntity_2Record = record.into(Tables.VO_ONE_TO_MANY_ENTITY_2);
        return new InnerClassDomainObjectBuilder<>(VoOneToManyEntity2.builder()
            .setValue(voOneToManyEntity_2Record.getValue()));
    }

    @Override
    public VoOneToManyEntity_2Record from(VoOneToManyEntity2 voOneToManyEntity2, VoAggregateRoot root) {
        VoOneToManyEntity_2Record voOneToManyEntity_2Record = new VoOneToManyEntity_2Record();
        voOneToManyEntity_2Record.setValue(voOneToManyEntity2.getValue());
        return voOneToManyEntity_2Record;
    }

    @Override
    public Class<VoOneToManyEntity2> domainObjectType() {
        return VoOneToManyEntity2.class;
    }

    @Override
    public Class<VoOneToManyEntity_2Record> recordType() {
        return VoOneToManyEntity_2Record.class;
    }
}
