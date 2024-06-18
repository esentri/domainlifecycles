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

package io.domainlifecycles.persistence.repository.order;

import io.domainlifecycles.domain.types.internal.DomainObject;

import java.util.Objects;

/**
 * A DatabaseDependencyEdge expresses any kind of order relation that is implied to these {@link DomainObject}s (=entities, value objects) by its corresponding
 * database relation (Dependencies implied by Foreign Keys).
 * Those dependencies have an impact on the order of operations applied to an entity.
 *
 * @param sourceClassName the source of the edge (the entity that has to be persisted before the target entity)
 * @param targetClassName the target of the edge (the entity that has to be persisted after the source entity)
 *
 * @author Mario Herb
 */
public record DatabaseDependencyEdge(String sourceClassName,
                                     String targetClassName) {

    public DatabaseDependencyEdge {
        Objects.requireNonNull(sourceClassName);
        Objects.requireNonNull(targetClassName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DatabaseDependencyEdge that)) return false;
        return sourceClassName.equals(that.sourceClassName) &&
            targetClassName.equals(that.targetClassName);
    }

}
