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

package io.domainlifecycles.events.mq.api;

import com.gruelbox.transactionoutbox.TransactionOutbox;
import io.domainlifecycles.events.gruelbox.api.DomainEventsInstantiator;
import io.domainlifecycles.events.gruelbox.api.PollerConfiguration;
import io.domainlifecycles.events.gruelbox.api.PublishingSchedulerConfiguration;
import io.domainlifecycles.events.gruelbox.poll.GruelboxPoller;
import io.domainlifecycles.events.gruelbox.publish.GruelboxDomainEventPublisher;
import io.domainlifecycles.events.mq.gruelbox.MqGruelboxDomainEventDispatcher;
import io.domainlifecycles.events.mq.publish.MqDomainEventPublisher;
import io.domainlifecycles.events.publish.DomainEventPublisher;

import java.util.Objects;

/**
 * Represents a configuration for a proxy publishing mechanism that utilizes Gruelbox components for publishing domain events via a message broker
 * and having a Gruelbox as a proxy.
 *
 * Domain Events are written to the outbox first. Then the outbox is polled using the GruelboxPoller.
 * Events polled from the outbox are finally sent to the broker.
 *
 * @author Mario Herb
 */
public class AbstractGruelboxProxyMqPublishingConfiguration extends AbstractMqPublishingConfiguration {

    private final TransactionOutbox transactionOutbox;
    private final GruelboxPoller poller;
    private final PublishingSchedulerConfiguration publishingSchedulerConfiguration;
    private final GruelboxDomainEventPublisher gruelboxDomainEventPublisher;
    private final DomainEventsInstantiator domainEventsInstantiator;
    private final MqGruelboxDomainEventDispatcher dispatcher;

    /**
     * Constructor for configuring a proxy publishing mechanism using a message queue.
     *
     * @param mqDomainEventPublisher The publisher for domain events using a message queue.
     * @param transactionOutbox The transaction outbox used for publishing domain events.
     * @param poller The Poller controlling polling operations on the outbox.
     * @param publishingSchedulerConfiguration The configuration for the publishing scheduler.
     * @param domainEventsInstantiator The instantiator for domain events.
     */
    AbstractGruelboxProxyMqPublishingConfiguration(MqDomainEventPublisher mqDomainEventPublisher,
                                                   TransactionOutbox transactionOutbox,
                                                   GruelboxPoller poller,
                                                   PublishingSchedulerConfiguration publishingSchedulerConfiguration,
                                                   DomainEventsInstantiator domainEventsInstantiator) {
        super(mqDomainEventPublisher);
        this.transactionOutbox = Objects.requireNonNull(transactionOutbox, "We need a TransactionOutbox for our AbstractGruelboxProxyMqPublishingConfiguration!");
        this.poller = Objects.requireNonNull(poller, "We need a GruelboxPoller for our AbstractGruelboxProxyMqPublishingConfiguration!");
        this.domainEventsInstantiator = Objects.requireNonNull(domainEventsInstantiator, "A DomainEventsInstantiator is required!");

        this.publishingSchedulerConfiguration = Objects.requireNonNull(publishingSchedulerConfiguration, "We need a TransactionOutbox for our AbstractGruelboxProxyMqPublishingConfiguration!");
        this.gruelboxDomainEventPublisher = new GruelboxDomainEventPublisher(transactionOutbox, publishingSchedulerConfiguration);
        this.dispatcher = new MqGruelboxDomainEventDispatcher(Objects.requireNonNull(mqDomainEventPublisher, "A MqDomainEventPublisher is required!"));
        domainEventsInstantiator.registerGruelboxDomainEventDispatcher(this.dispatcher);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomainEventPublisher domainEventPublisher() {
        return gruelboxDomainEventPublisher;
    }

    /**
     * Retrieves the TransactionOutbox object associated with this configuration.
     *
     * @return The TransactionOutbox object used for publishing domain events.
     */
    public TransactionOutbox getTransactionOutbox() {
        return transactionOutbox;
    }

    /**
     * Retrieves the GruelboxPoller associated with the configuration.
     *
     * @return The GruelboxPoller object representing the Poller associated with the configuration.
     */
    public GruelboxPoller getPoller() {
        return poller;
    }

    /**
     * Retrieves the PublishingSchedulerConfiguration object associated with this configuration.
     *
     * @return The PublishingSchedulerConfiguration representing the configuration for the publishing scheduler.
     */
    public PublishingSchedulerConfiguration getPublishingSchedulerConfiguration() {
        return publishingSchedulerConfiguration;
    }

    /**
     * Retrieves the GruelboxDomainEventPublisher associated with this configuration.
     *
     * @return The GruelboxDomainEventPublisher responsible for publishing domain events to a transaction outbox.
     */
    public GruelboxDomainEventPublisher getGruelboxDomainEventPublisher() {
        return gruelboxDomainEventPublisher;
    }

    /**
     * Retrieves the DomainEventsInstantiator associated with this configuration.
     *
     * @return The DomainEventsInstantiator responsible for instantiating instances of GruelboxDomainEventDispatcher or IdempotentExecutor.
     */
    public DomainEventsInstantiator getDomainEventsInstantiator() {
        return domainEventsInstantiator;
    }

    /**
     * Retrieves the MqGruelboxDomainEventDispatcher associated with this configuration.
     *
     * @return The MqGruelboxDomainEventDispatcher responsible for dispatching domain events to a topic of a message broker.
     */
    public MqGruelboxDomainEventDispatcher getDispatcher() {
        return dispatcher;
    }
}
