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

package io.domainlifecycles.mirror.api;

import io.domainlifecycles.domain.types.AggregateRoot;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.internal.ConcurrencySafe;

import java.util.List;
import java.util.Optional;

/**
 * An EntityMirror mirrors all {@link Entity} instances.
 *
 * @author Mario Herb
 */
public interface EntityMirror extends DomainObjectMirror, DomainCommandProcessingMirror, DomainEventProcessingMirror {

    /**
     * @return the mirror for the field containing the {@link Identity} value for the mirrored Entity.
     * If no unambiguous Identity could be found, then the returned Optional is empty.
     */
    Optional<FieldMirror> getIdentityField();

    /**
     * @return the mirror for the field containing the concurrency version for the mirrored Entity.
     * If no unambiguous field annotated with {@link ConcurrencySafe.ConcurrencyVersion} could be found, then the
     * returned Optional is empty.
     */
    Optional<FieldMirror> getConcurrencyVersionField();

    /**
     * @return a list mirrors for {@link Entity} references of the mirrored Entity.
     */
    List<EntityReferenceMirror> getEntityReferences();

    /**
     * @param name the name of the reference
     * @return an {@link EntityReferenceMirror} for the reference with the given name.
     */
    EntityReferenceMirror entityReferenceByName(String name);

    /**
     * @return a list mirrors for {@link AggregateRoot} references of the mirrored Entity.
     * Should normally only exist for back references to the root.
     */
    List<AggregateRootReferenceMirror> getAggregateRootReferences();

    /**
     * @param name the name of the AggregateRoot
     * @return an {@link AggregateRootReferenceMirror} for the reference with the given name.
     */
    AggregateRootReferenceMirror aggregateRootReferenceByName(String name);

}
