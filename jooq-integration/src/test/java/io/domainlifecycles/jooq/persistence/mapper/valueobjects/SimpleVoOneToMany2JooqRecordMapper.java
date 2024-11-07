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
