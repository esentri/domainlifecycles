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

package io.domainlifecycles.events.receive.execution.detector;

import io.domainlifecycles.domain.types.ApplicationService;
import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.domain.types.DomainService;
import io.domainlifecycles.domain.types.OutboundService;
import io.domainlifecycles.domain.types.Repository;
import io.domainlifecycles.domain.types.QueryClient;

import java.util.Objects;

/**
 * Represents the execution context for a domain event handler within a service
 * ({@link DomainService}, {@link ApplicationService}, {@link OutboundService},
 * {@link QueryClient} or {@link Repository}).
 *
 * It holds the handler object (the service instance), handler method name, and the domain event being dispatched.
 *
 * @param handler handler instance
 * @param handlerMethodName handler method name
 * @param domainEvent domain event
 * @author Mario Herb
 */
public record ServiceExecutionContext(Object handler, String handlerMethodName, DomainEvent domainEvent) implements ExecutionContext {
    public ServiceExecutionContext {
        Objects.requireNonNull(handler, "A ServiceExecutionContext needs a non null handler object!");
        Objects.requireNonNull(handlerMethodName, "A ServiceExecutionContext needs a non null handlerMethodName!");
        Objects.requireNonNull(domainEvent, "A ServiceExecutionContext needs a non null domain event!");
    }
}
