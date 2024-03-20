/*
 *
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

package nitrox.dlc.jooq.configuration.def;


import nitrox.dlc.jooq.mirror.JooqEntityRecordMirrorImpl;
import nitrox.dlc.jooq.mirror.JooqValueObjectRecordMirrorImpl;
import nitrox.dlc.persistence.mapping.RecordMapper;
import nitrox.dlc.persistence.mirror.api.EntityRecordMirror;
import nitrox.dlc.persistence.mirror.api.ValueObjectRecordMirror;
import nitrox.dlc.persistence.records.NewRecordInstanceProvider;
import nitrox.dlc.persistence.records.RecordMirrorInstanceProvider;
import org.jooq.ForeignKey;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * jOOQ specific implementation of a {@link RecordMirrorInstanceProvider}.
 *
 * @author Mario Herb
 */
public class JooqRecordMirrorInstanceProvider
    implements RecordMirrorInstanceProvider<UpdatableRecord<?>> {

    private final NewRecordInstanceProvider newRecordInstanceProvider;

    public JooqRecordMirrorInstanceProvider(final NewRecordInstanceProvider newRecordInstanceProvider) {
        this.newRecordInstanceProvider = newRecordInstanceProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityRecordMirror<UpdatableRecord<?>> provideEntityRecordMirror(String recordTypeName,
                                                        String entityTypeName,
                                                        RecordMapper<UpdatableRecord<?>,?,?> mapper,
                                                        List<ValueObjectRecordMirror<UpdatableRecord<?>>> valueObjectRecordMirrors,
                                                        Map<String, List<String>> recordCanonicalNameToDomainObjectTypeMap) {
        var recordInstance = (TableRecord<?>)newRecordInstanceProvider.provideNewRecord(recordTypeName);
        var databaseReferences = recordInstance
            .getTable()
            .getReferences()
            .stream()
            .flatMap(fk ->
                recordCanonicalNameToDomainObjectTypeMap.get((((ForeignKey) fk).getKey()).getTable().getRecordType().getName()).stream()
            )
            .distinct()
            .collect(Collectors.toList());

        return new JooqEntityRecordMirrorImpl(
            recordTypeName,
            entityTypeName,
            mapper,
            databaseReferences,
            valueObjectRecordMirrors
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JooqValueObjectRecordMirrorImpl provideValueObjectRecordMirror(String containingEntityTypeName,
                                                                          String containedValueObjectTypeName,
                                                                          Class<? extends UpdatableRecord<?>> valueObjectRecordType,
                                                                          List<String> pathFromEntityToValueObject,
                                                                          RecordMapper<UpdatableRecord<?>, ?, ?> mapper,
                                                                          Map<String, List<String>> recordToDomainObjectTypeMap) {
        var recordInstance = (TableRecord<?>)newRecordInstanceProvider.provideNewRecord(valueObjectRecordType.getName());
        var databaseReferences = recordInstance.getTable()
            .getReferences()
            .stream()
            .flatMap(fk -> recordToDomainObjectTypeMap.get(((ForeignKey) fk).getKey().getTable().getRecordType().getName()).stream())
            .distinct()
            .collect(Collectors.toList());

        return new JooqValueObjectRecordMirrorImpl(
            containingEntityTypeName,
            containedValueObjectTypeName,
            valueObjectRecordType,
            pathFromEntityToValueObject,
            mapper,
            databaseReferences);
    }
}
