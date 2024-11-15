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

package io.domainlifecycles.events.gruelbox.dispatch;

import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.consume.DomainEventConsumer;

import java.util.Objects;

/**
 * The DirectGruelboxDomainEventDispatcher class is an implementation of the GruelboxDomainEventDispatcher interface.
 * It dispatches a domain event to a receiving domain event handler for processing, after the event was polled
 * from the outbox table.
 *
 * In other cases the outbox might be used as a proxy for a message queue. Then the dispatcher would
 * pass the domain events to the queue.
 *
 * @author Mario Herb
 */
public final class DirectGruelboxDomainEventDispatcher implements GruelboxDomainEventDispatcher{

    private final DomainEventConsumer domainEventConsumer;

    /**
     * Creates a DirectGruelboxDomainEventDispatcher object.
     *
     * @param domainEventConsumer The receiving domain event handler that will handle the dispatched domain events.
     *                                   It must implement the DomainEventConsumer interface.
     * @throws NullPointerException if domainEventConsumer is null.
     */
    public DirectGruelboxDomainEventDispatcher(DomainEventConsumer domainEventConsumer) {
        this.domainEventConsumer = Objects.requireNonNull(domainEventConsumer, "A DomainEventConsumer is required!");
    }

    /**
     * Dispatches a domain event to a receiving domain event handler for processing.
     *
     * @param domainEvent The domain event to be dispatched
     */
    @Override
    public void dispatch(DomainEvent domainEvent) {
        domainEventConsumer.consume(domainEvent);
    }
}
