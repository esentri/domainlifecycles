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
import io.domainlifecycles.test.tables.records.SimpleVoOneToMany_3Record;
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
