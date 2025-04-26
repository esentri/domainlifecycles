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

package io.domainlifecycles.persistence.mirror;

import io.domainlifecycles.domain.types.AggregateRoot;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.mapping.RecordMapper;
import io.domainlifecycles.persistence.mirror.api.EntityRecordMirror;
import io.domainlifecycles.persistence.mirror.api.PersistenceMirror;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a persistence model.
 *
 * @param <BASE_RECORD_TYPE> the type of record
 * @author Mario Herb
 */
public class PersistenceModel<BASE_RECORD_TYPE> implements PersistenceMirror<BASE_RECORD_TYPE> {

    private final Map<String, EntityRecordMirror<BASE_RECORD_TYPE>> entityRecordMap;

    /**
     * Constructs a new PersistenceModel and initializes the entity record map using the provided
     * array of EntityRecordMirror instances.
     *
     * @param entityRecordMirrors an array of EntityRecordMirror instances that map domain object
     *                            type names to their corresponding record mirrors. These are
     *                            used to populate the entity record map within the persistence model.
     */
    public PersistenceModel(EntityRecordMirror<BASE_RECORD_TYPE>[] entityRecordMirrors) {
        this.entityRecordMap = new HashMap<>();
        Arrays.stream(entityRecordMirrors).forEach(erm -> entityRecordMap.put(erm.domainObjectTypeName(), erm));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityRecordMirror<BASE_RECORD_TYPE> getEntityRecordMirror(String entityClassName) {
        var erm = entityRecordMap
            .get(entityClassName);
        if (erm != null) {
            return erm;
        }
        throw DLCPersistenceException.fail("Couldn't find EntityRecordMirror for '%s'.", entityClassName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RecordMapper<BASE_RECORD_TYPE, ? extends Entity<?>, ? extends AggregateRoot<?>> getEntityRecordMapper(String entityClassName) {
        var erm = entityRecordMap
            .get(entityClassName);
        if (erm != null) {
            return (RecordMapper<BASE_RECORD_TYPE, ? extends Entity<?>, ? extends AggregateRoot<?>>) erm.recordMapper();
        }
        //try to find mapper in type hierarchy
        erm = entityRecordMap
            .values()
            .stream()
            .filter(rm -> Domain.entityMirrorFor(rm.domainObjectTypeName()).isSubClassOf(entityClassName))
            .findFirst()
            .orElseThrow(() -> DLCPersistenceException.fail("Couldn't find record mapper for '%s'.", entityClassName));
        return (RecordMapper<BASE_RECORD_TYPE, ? extends Entity<?>, ? extends AggregateRoot<?>>) erm.recordMapper();
    }
}
