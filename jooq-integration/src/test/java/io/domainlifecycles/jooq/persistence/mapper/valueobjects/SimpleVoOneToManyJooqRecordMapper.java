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
