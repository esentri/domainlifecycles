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

package io.domainlifecycles.events.gruelbox.dispatch;

import io.domainlifecycles.domain.types.DomainEvent;

/**
 * The {@code GruelboxDomainEventDispatcher} interface defines a contract for classes
 * that dispatch domain events to receiving domain event handlers for processing.
 *
 * <p>A domain event dispatcher is responsible for routing domain events to the appropriate
 * receiving domain event handler based on the type of the event.
 *
 * <p>Each implementation of this interface should provide an implementation for the
 * {@link #dispatch(DomainEvent)} method, which takes a domain event and dispatches it
 * to the domain event handler.
 *
 * <p><b>Note:</b> It is important to ensure that a non-null domainEvent is provided to the
 * {@link #dispatch(DomainEvent)} method.
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
     */
    void dispatch(DomainEvent domainEvent);
}
