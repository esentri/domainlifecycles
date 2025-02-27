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

package io.domainlifecycles.events.gruelbox.dispatch;

import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.consume.TargetExecutionContext;

/**
 * The {@code GruelboxDomainEventDispatcher} interface defines a contract for classes
 * that dispatch domain events to receiving domain event handlers for processing, after they were polled
 * from the outbox table.
 *
 * <p>A domain event dispatcher is responsible for routing domain events to the appropriate
 * receiving domain event handler based on the type of the event.
 *
 *
 * @see DomainEvent
 *
 * @author Mario Herb
 */
public interface GruelboxDomainEventDispatcher {

    /**
     * Dispatches the given domain event to the appropriate domain event handler.
     *
     * @param domainEvent the domain event to dispatch
     * @param targetExecutionContext the target execution context detected
     */
    void dispatch(DomainEvent domainEvent, TargetExecutionContext targetExecutionContext);
}
