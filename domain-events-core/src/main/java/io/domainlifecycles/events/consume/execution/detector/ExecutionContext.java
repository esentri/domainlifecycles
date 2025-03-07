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

package io.domainlifecycles.events.consume.execution.detector;

import io.domainlifecycles.domain.types.DomainEvent;

/**
 * The {@code ExecutionContext} interface represents the execution context for a domain event handler within a service or an aggregate.
 * Its implementations provide information about the handler object, handler method name, and the domain event being dispatched.
 *
 * @author Mario Herb
 */
public interface ExecutionContext {

    /**
     * Retrieves the Domain Event associated with the execution context.
     *
     * @return The domain event object.
     */
    DomainEvent domainEvent();

    /**
     * This method returns the Domain Event handler object.
     *
     * @return The handler object.
     */
    Object handler();

    /**
     * This method returns the mirrored handler type name.
     *
     * @return The handler object.
     */
    String handlerTypeName();

    /**
     * Retrieves the handler method name associated with the execution context.
     *
     * @return The handler method name.
     */
    String handlerMethodName();
}
