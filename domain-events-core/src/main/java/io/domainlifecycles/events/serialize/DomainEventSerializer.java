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
 *  Copyright 2019-2025 the original author or authors.
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

package io.domainlifecycles.events.serialize;

import io.domainlifecycles.domain.types.DomainEvent;

/**
 * Interface for serializing and deserializing {@link DomainEvent} objects.
 * <p>
 * Implementations of this interface are responsible for converting {@code DomainEvent}
 * instances to a serialized String representation and vice versa.
 *
 * @author Mario Herb
 */
public interface DomainEventSerializer {

    /**
     * Serializes a given {@link DomainEvent} into its String representation.
     * The serialized String can be used for persistence or communication purposes.
     *
     * @param event the {@code DomainEvent} to be serialized; must not be null
     * @return a String representation of the provided {@code DomainEvent}
     */
    String serialize(DomainEvent event);

    /**
     * Deserializes the provided serialized representation of a {@link DomainEvent} into an instance
     * of the specified domain event type.
     *
     * @param serializedEvent the serialized string representation of a {@code DomainEvent}; must not be null
     * @param eventType the target class type of the domain event to deserialize into; must not be null
     * @return an instance of the deserialized {@code DomainEvent} of the specified type
     */
    DomainEvent deserialize(String serializedEvent, Class<? extends DomainEvent> eventType);
}
