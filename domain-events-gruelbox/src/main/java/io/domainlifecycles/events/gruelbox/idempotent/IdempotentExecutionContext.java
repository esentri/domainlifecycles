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

package io.domainlifecycles.events.gruelbox.idempotent;

import io.domainlifecycles.domain.types.DomainEvent;

import java.util.Objects;

/**
 * Represents the context for executing idempotent operations.
 * The context includes the class of the handler, the method name of the handler,
 * and the domain event associated with the operation.
 *
 * @param handlerClass The class of the handler for the operation.
 * @param handlerMethod The method name of the handler.
 * @param domainEvent The domain event associated with the operation.
 *
 * @author Mario Herb
 */
public record IdempotentExecutionContext(Class<?> handlerClass,
                                         String handlerMethod,
                                         DomainEvent domainEvent) {

    /**
     * Constructs a new IdempotentExecutionContext with the provided handler class, handler method, and domain event.
     *
     * @param handlerClass The class of the handler.
     * @param handlerMethod The method name of the handler.
     * @param domainEvent The domain event associated with the operation.
     */
    public IdempotentExecutionContext(Class<?> handlerClass, String handlerMethod, DomainEvent domainEvent) {
        this.handlerClass = Objects.requireNonNull(handlerClass, "handlerClass required!");
        this.handlerMethod = Objects.requireNonNull(handlerMethod, "handlerMethod required!");
        this.domainEvent = Objects.requireNonNull(domainEvent, "DomainEvent required!");

    }


}
