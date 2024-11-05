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

package io.domainlifecycles.events.gruelbox.idempotent;

import io.domainlifecycles.domain.types.DomainEvent;

import java.util.Objects;

/**
 * Represents an entry in the configuration for idempotency protection.
 *
 * @param handlerClass The class implementing the domain event handler which should be protected
 * @param methodName The name of the method in the handlerClass that processes the domain event in an idempotent manner
 * @param domainEventClass The class object representing the domain event type
 * @param idempotencyFunction The function to generate a unique identifier for the given domain event
 *                            This function is important to detect Domain Event Duplicates (it might be should deliver a unique identifier of the domain event)
 *
 * @author Mario Herb
 */
public record IdempotencyConfigurationEntry(Class<?> handlerClass,
                                            String methodName,
                                            Class<? extends DomainEvent> domainEventClass,
                                            IdempotencyFunction idempotencyFunction)
{
    /**
     * Creates a new IdempotencyConfigurationEntry.
     *
     * @param handlerClass The class implementing the domain event handler which should be protected
     * @param methodName The name of the method in the handlerClass that processes the domain event in an idempotent manner
     * @param domainEventClass The class object representing the domain event type
     * @param idempotencyFunction The function to generate a unique identifier for the given domain event
     */
    public IdempotencyConfigurationEntry(Class<?> handlerClass, String methodName, Class<? extends DomainEvent> domainEventClass, IdempotencyFunction idempotencyFunction) {
        this.handlerClass = Objects.requireNonNull(handlerClass, "A handlerClass is required!");
        this.methodName = Objects.requireNonNull(methodName, "A handlerMethod is required!");
        this.domainEventClass = Objects.requireNonNull(domainEventClass, "A Domain Event class is required!");
        this.idempotencyFunction = Objects.requireNonNull(idempotencyFunction, "An idempotency function must be defined!");
    }
}
