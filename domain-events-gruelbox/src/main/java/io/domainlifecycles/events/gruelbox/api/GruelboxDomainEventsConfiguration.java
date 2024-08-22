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

package io.domainlifecycles.events.gruelbox.api;

import com.gruelbox.transactionoutbox.TransactionOutbox;
import io.domainlifecycles.events.api.DomainEventsConfiguration;
import io.domainlifecycles.events.api.NonTransactionDefaultDomainEventsConfiguration;
import io.domainlifecycles.events.gruelbox.dispatch.DirectGruelboxDomainEventDispatcher;
import io.domainlifecycles.events.gruelbox.dispatch.GruelboxDomainEventDispatcher;
import io.domainlifecycles.events.gruelbox.poll.GruelboxPoller;
import io.domainlifecycles.events.gruelbox.publish.GruelboxDomainEventPublisher;
import io.domainlifecycles.events.receive.execution.GeneralReceivingDomainEventHandler;
import io.domainlifecycles.events.receive.execution.handler.TransactionalHandlerExecutor;
import io.domainlifecycles.events.receive.execution.processor.SimpleExecutionContextProcessor;
import io.domainlifecycles.services.api.ServiceProvider;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

/**
 * The GruelboxDomainEventsConfiguration class represents the configuration for the Gruelbox domain events framework.
 * It provides various options for configuring the behavior of the framework.
 *
 * @author Mario Herb
 */
public final class GruelboxDomainEventsConfiguration {

    public final static Duration schedulingDelayDefault = Duration.ZERO;
    public final static boolean orderedByDomainEventTypeDefault = false;
    public final static boolean useInternalPollerDefault = true;
    public final static long internalPollerDelayMsDefault = 3000;
    public final static long internalPollerPeriodMsDefault = 1000;

    private final TransactionOutbox transactionOutbox;
    private final DomainEventsConfiguration domainEventsConfiguration;
    private final Duration schedulingDelay;
    private final boolean orderedByDomainEventType;
    private final boolean useInternalPoller;
    private final long internalPollerDelayMs;
    private final long internalPollerPeriodMs;
    private final GruelboxDomainEventDispatcher gruelboxDomainEventDispatcher;
    private final GruelboxPoller internalGruelboxPoller;

    private static GruelboxDomainEventDispatcher configuredDispatcherInstance;


    /**
     * GruelboxDomainEventsConfiguration represents the configuration for the DLC Gruelbox domain events integration.
     * This constructor uses several configuration parameters to initialize the configuration object.
     *
     * @param serviceProvider The service provider used to retrieve instances of various types of services.
     * @param transactionOutbox The transaction outbox used for flushing domain events.
     * @param transactionalHandlerExecutor The executor for transactional handling of domain event handlers.
     * @param schedulingDelay The delay between scheduling domain events.
     * @param orderedByDomainEventType Flag indicating whether the domain events should be ordered by type.
     * @param useInternalPoller Flag indicating whether the internal poller should be used.
     * @param internalPollerDelayMs The delay in milliseconds for the internal poller.
     * @param internalPollerPeriodMs The period in milliseconds for the internal poller.
     */
    public GruelboxDomainEventsConfiguration(
            ServiceProvider serviceProvider,
            TransactionOutbox transactionOutbox,
            TransactionalHandlerExecutor transactionalHandlerExecutor,
            Duration schedulingDelay,
            boolean orderedByDomainEventType,
            boolean useInternalPoller,
            long internalPollerDelayMs,
            long internalPollerPeriodMs
    ){
        this.internalPollerDelayMs = internalPollerDelayMs;
        this.internalPollerPeriodMs = internalPollerPeriodMs;
        Objects.requireNonNull(serviceProvider, "A ServiceProvider is required!");
        this.transactionOutbox = Objects.requireNonNull(transactionOutbox, "A TransactionOutbox is required!");
        this.schedulingDelay = Objects.requireNonNull(schedulingDelay, "A schedulingDelay is required!");
        Objects.requireNonNull(transactionalHandlerExecutor, "A TransactionalHandlerExecutor is required!");
        this.orderedByDomainEventType = orderedByDomainEventType;
        this.useInternalPoller = useInternalPoller;
        var defaultConfig = new NonTransactionDefaultDomainEventsConfiguration(serviceProvider).getDomainEventsConfiguration();
        var processor = new SimpleExecutionContextProcessor(transactionalHandlerExecutor);
        var receivingDomainEventHandler = new GeneralReceivingDomainEventHandler(defaultConfig.getExecutionContextDetector(), processor);
        this.gruelboxDomainEventDispatcher = new DirectGruelboxDomainEventDispatcher(receivingDomainEventHandler);
        var domainEventPublisher = new GruelboxDomainEventPublisher(
            transactionOutbox,
            gruelboxDomainEventDispatcher,
            orderedByDomainEventType,
            schedulingDelay
        );
        this.domainEventsConfiguration = defaultConfig.toBuilder()
            .withHandlerExecutor(transactionalHandlerExecutor)
            .withExecutionContextProcessor(processor)
            .withReceivingDomainEventHandler(receivingDomainEventHandler)
            .withDomainEventPublisher(domainEventPublisher)
        .build();

        if(useInternalPoller){
            this.internalGruelboxPoller = new GruelboxPoller(transactionOutbox, this.internalPollerDelayMs, this.internalPollerPeriodMs);
        }else{
            this.internalGruelboxPoller = null;
        }
        configuredDispatcherInstance = this.gruelboxDomainEventDispatcher;
    }

    /**
     * The GruelboxDomainEventsConfiguration class represents the configuration for the DLC Gruelbox domain events integration.
     * It provides a constructor to initialize the configuration object with various parameters.
     *
     * @param serviceProvider The service provider used to retrieve instances of various types of services.
     * @param transactionOutbox The transaction outbox used for flushing domain events.
     * @param transactionalHandlerExecutor The executor for transactional handling of domain event handlers.
     */
    public GruelboxDomainEventsConfiguration(
        ServiceProvider serviceProvider,
        TransactionOutbox transactionOutbox,
        TransactionalHandlerExecutor transactionalHandlerExecutor){
        this(serviceProvider,
            transactionOutbox,
            transactionalHandlerExecutor,
            schedulingDelayDefault,
            orderedByDomainEventTypeDefault,
            useInternalPollerDefault,
            internalPollerDelayMsDefault,
            internalPollerPeriodMsDefault
        );
    }


    /**
     * Retrieves the transaction outbox used for flushing domain events.
     *
     * @return The transaction outbox.
     */
    public TransactionOutbox getTransactionOutbox() {
        return transactionOutbox;
    }

    /**
     * Retrieves the domain events configuration.
     *
     * @return The domain events configuration.
     */
    public DomainEventsConfiguration getDomainEventsConfiguration() {
        return domainEventsConfiguration;
    }

    /**
     * Retrieves the scheduling delay for domain events.
     *
     * @return The scheduling delay as a {@link Duration} object.
     */
    public Duration getSchedulingDelay() {
        return schedulingDelay;
    }

    /**
     * Checks if the ordering of domain events is enabled.
     *
     * @return true if the ordering of domain events is enabled, false otherwise.
     */
    public boolean isOrderedByDomainEventType() {
        return orderedByDomainEventType;
    }

    /**
     * Retrieve the GruelboxDomainEventDispatcher.
     *
     * @return The GruelboxDomainEventDispatcher instance.
     */
    public GruelboxDomainEventDispatcher getGruelboxDomainEventDispatcher() {
        return gruelboxDomainEventDispatcher;
    }

    /**
     * Returns whether the internal poller is being used.
     *
     * @return {@code true} if the internal poller is being used, {@code false} otherwise.
     */
    public boolean isUseInternalPoller() {
        return useInternalPoller;
    }

    /**
     * Returns the delay in milliseconds for the internal poller.
     *
     * @return The delay in milliseconds for the internal poller.
     */
    public long getInternalPollerDelayMs() {
        return internalPollerDelayMs;
    }

    /**
     * Retrieves the period in milliseconds for the internal poller.
     *
     * @return The period in milliseconds for the internal poller.
     */
    public long getInternalPollerPeriodMs() {
        return internalPollerPeriodMs;
    }

    /**
     * Retrieves the internal GruelboxPoller instance if it exists.
     *
     * @return an Optional object containing the internal GruelboxPoller instance, or an empty Optional if it does not exist.
     */
    public Optional<GruelboxPoller> getInternalGruelboxPoller() {
        return Optional.ofNullable(internalGruelboxPoller);
    }

    /**
     * Returns the configured instance of the GruelboxDomainEventDispatcher.
     *
     * @return The configured GruelboxDomainEventDispatcher instance.
     */
    public static GruelboxDomainEventDispatcher configuredDispatcher(){
        return configuredDispatcherInstance;
    }
}
