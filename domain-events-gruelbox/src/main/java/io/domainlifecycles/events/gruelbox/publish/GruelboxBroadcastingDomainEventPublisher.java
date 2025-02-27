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

package io.domainlifecycles.events.gruelbox.publish;

import com.gruelbox.transactionoutbox.TransactionOutbox;
import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.consume.execution.detector.ExecutionContextDetector;
import io.domainlifecycles.events.gruelbox.api.PublishingSchedulerConfiguration;
import io.domainlifecycles.events.gruelbox.dispatch.GruelboxDomainEventDispatcher;
import io.domainlifecycles.events.consume.TargetExecutionContext;
import io.domainlifecycles.events.publish.DomainEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * The GruelboxBroadcastingDomainEventPublisher class is responsible for publishing domain events to a transaction outbox.
 * The outbox schedules calls on a GruelboxDomainEventDispatcher, that dispatches the events later on when the
 * outbox entries are processed (the outbox is flushed).
 *
 * The GruelboxBroadcastingDomainEventPublisher schedules separate calls on the GruelboxDomainEventDispatcher for each
 * target execution context detected. That way the event processing for each consumer of a DomainEvent could be retried
 * corresponding to Gruelbox retry features when a consumer execution failes.
 *
 * @author Mario Herb
 */
public final class GruelboxBroadcastingDomainEventPublisher implements DomainEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(GruelboxBroadcastingDomainEventPublisher.class);
    private final TransactionOutbox outbox;
    private final PublishingSchedulerConfiguration publishingSchedulerConfiguration;
    private final ExecutionContextDetector executionContextDetector;

    /**
     * The {@code GruelboxBroadcastingDomainEventPublisher} class is responsible for publishing domain events to a transaction outbox.
     * The outbox schedules calls on a GruelboxDomainEventDispatcher, that dispatches the events later on when the outbox entries are processed.
     *
     * @param outbox the outbox receiving the events
     * @param publishingSchedulerConfiguration the scheduler configuration for the scheduled processing calls
     * @param executionContextDetector responsible for detecting the target execution context
     */
    public GruelboxBroadcastingDomainEventPublisher(TransactionOutbox outbox,
                                                    PublishingSchedulerConfiguration publishingSchedulerConfiguration,
                                                    ExecutionContextDetector executionContextDetector
    ) {
        this.outbox = Objects.requireNonNull(outbox, "A TransactionOutbox is required!");
        this.publishingSchedulerConfiguration = Objects.requireNonNull(publishingSchedulerConfiguration, "A PublishingSchedulerConfiguration is required!");
        this.executionContextDetector = Objects.requireNonNull(executionContextDetector, "An ExecutionContextDetector is required!");
    }

    /**
     * Publishes a domain event to the transaction outbox for later dispatching.
     * The event is logged for debugging purposes.
     * If orderedByDomainEventType is true, the event is scheduled in an ordered manner based on its class name.
     * The event is scheduled with a delay of at least schedulingDelay and dispatched using the GruelboxDomainEventDispatcher.
     *
     * @param domainEvent the domain event to be published
     */
    @Override
    public void publish(DomainEvent domainEvent) {
        log.debug("Received DomainEvent {} for publishing", domainEvent);
        var scheduleBuilder = outbox.with();
        if(publishingSchedulerConfiguration.isOrderedByDomainEventType()){
            scheduleBuilder.ordered(domainEvent.getClass().getName());
        }
        executionContextDetector.detectExecutionContexts(domainEvent).forEach(executionContext ->
            {
                scheduleBuilder.delayForAtLeast(publishingSchedulerConfiguration.getSchedulingDelay())
                    .schedule(GruelboxDomainEventDispatcher.class)
                    .dispatch(domainEvent, new TargetExecutionContext(executionContext.handlerTypeName(), executionContext.handlerMethodName()));
            }
        );

    }

    /**
     * Retrieves the configuration settings for the publishing scheduler.
     *
     * @return the publishing scheduler configuration
     */
    public PublishingSchedulerConfiguration getPublishingSchedulerConfiguration() {
        return publishingSchedulerConfiguration;
    }
}
