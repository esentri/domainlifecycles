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

package io.domainlifecycles.mirror.api;

import io.domainlifecycles.domain.types.AggregateRoot;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.ReadModel;
import io.domainlifecycles.domain.types.ValueObject;

import java.util.List;

/**
 * A ReadModelMirror mirrors {@link ReadModel} instances.
 *
 * @author Mario Herb
 */
public interface ReadModelMirror extends DomainTypeMirror {

    /**
     * @return a list mirrored fields of the mirrored ReadModel (all fields that are not in the category of other
     * kinds of references).
     */
    List<FieldMirror> getBasicFields();

    /**
     * @return a list mirrors for value references (references of {@link ValueObject}, Enums or {@link Identity}) of
     * the mirrored ReadModel.
     */
    List<ValueReferenceMirror> getValueReferences();

    /**
     * @return a list mirrors for {@link Entity} references of the mirrored ReadModel.
     */
    List<EntityReferenceMirror> getEntityReferences();

    /**
     * @return a list mirrors for {@link AggregateRoot} references of the mirrored ReadModel.
     */
    List<AggregateRootReferenceMirror> getAggregateRootReferences();

}
