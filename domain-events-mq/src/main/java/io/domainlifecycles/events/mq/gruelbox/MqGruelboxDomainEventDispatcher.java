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

package io.domainlifecycles.events.mq.gruelbox;

import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.consume.TargetExecutionContext;
import io.domainlifecycles.events.gruelbox.dispatch.GruelboxDomainEventDispatcher;
import io.domainlifecycles.events.mq.publish.MqDomainEventPublisher;

/**
 * The {@code MqGruelboxDomainEventDispatcher} class is an implementation of the {@link GruelboxDomainEventDispatcher}
 * interface. It is responsible for dispatching domain events to a topic of a message broker.
 * In this case the Gruelbox acts as an outbox proxy for DomainEvents that are distributed via a message broker.
 *
 * Gruelbox ensures that the events are transactionally consistent,
 * that means no ghost events occur or events cannot be lost.
 *
 * {@inheritDoc}
 *
 * @author Mario Herb
 */
public class MqGruelboxDomainEventDispatcher implements GruelboxDomainEventDispatcher {

    private final MqDomainEventPublisher mqDomainEventPublisher;

    /**
     * Creates a new MqGruelboxDomainEventDispatcher
     * @param mqDomainEventPublisher the MqDomainEventPublisher used to publish domain events
     */
    public MqGruelboxDomainEventDispatcher(MqDomainEventPublisher mqDomainEventPublisher) {
        this.mqDomainEventPublisher = mqDomainEventPublisher;
    }

    /**
     * This method is responsible for dispatching a domain event.
     * It takes a domain event as a parameter and publishes it using the MqDomainEventPublisher.
     *
     * @param domainEvent the domain event to dispatch
     * @param targetExecutionContext Target Context not relevant
     */
    @Override
    public void dispatch(DomainEvent domainEvent, TargetExecutionContext targetExecutionContext) {
        mqDomainEventPublisher.publish(domainEvent);
    }
}
