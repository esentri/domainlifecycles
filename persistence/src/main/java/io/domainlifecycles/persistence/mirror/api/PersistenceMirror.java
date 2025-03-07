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

package io.domainlifecycles.persistence.mirror.api;

import io.domainlifecycles.domain.types.AggregateRoot;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.persistence.mapping.RecordMapper;

/**
 * Top-level (root node) persistence mirror
 *
 * @param <BASE_RECORD_TYPE> the type of base record
 * @author Mario Herb
 */
public interface PersistenceMirror<BASE_RECORD_TYPE> {

    /**
     * Returns {@link EntityRecordMirror} for Entity by
     * the full qualified Entity type name.
     *
     * @param entityTypeName the full qualified entity type
     * @return the EntityRecordMirror for the provided EntityTypeName
     */
    EntityRecordMirror<BASE_RECORD_TYPE> getEntityRecordMirror(String entityTypeName);

    /**
     * Returns {@link RecordMapper} for Entity by
     * the full qualified Entity type name.
     *
     * @param entityTypeName the full qualified entity type
     * @return the RecordMapper for the provided Entity
     */
    RecordMapper<? extends BASE_RECORD_TYPE, ? extends Entity<?>, ? extends AggregateRoot<?>> getEntityRecordMapper(String entityTypeName);
}


