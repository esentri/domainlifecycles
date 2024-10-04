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

package io.domainlifecycles.events.mq.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gruelbox.transactionoutbox.TransactionOutbox;
import io.domainlifecycles.access.classes.ClassProvider;
import io.domainlifecycles.events.consume.execution.handler.HandlerExecutor;
import io.domainlifecycles.events.gruelbox.api.DomainEventsInstantiator;
import io.domainlifecycles.events.gruelbox.api.PollerConfiguration;
import io.domainlifecycles.events.gruelbox.api.PublishingSchedulerConfiguration;
import io.domainlifecycles.services.api.ServiceProvider;

/**
 * Represents an abstract class for creating Message Queue (MQ) channels that are proxied by a TransactionalOutbox (Gruelbox).
 * Domain Events published on this channel get written to a TransactionOutbox before being sent to an external message broker.
 * That way, it is ensured no ghost events occur and any event loss is avoided. Event sending is transactionally consistent.
 *
 * @author Mario Herb
 */
public abstract class AbstractGruelboxProxyMqChannelFactory extends AbstractMqChannelFactory{

    private final TransactionOutbox transactionOutbox;
    private final PollerConfiguration pollerConfiguration;
    private final PublishingSchedulerConfiguration publishingSchedulerConfiguration;
    private final DomainEventsInstantiator domainEventsInstantiator;

    /**
     * Constructs a new AbstractGruelboxProxyMqChannelFactory with the provided parameters.
     *
     * @param serviceProvider The service provider for retrieving instances of various services.
     * @param classProvider The provider of Class instances based on full qualified class names.
     * @param handlerExecutor The executor for handling domain event listeners.
     * @param objectMapper The object mapper for serialization and deserialization.
     * @param transactionOutbox The transaction outbox for managing message transactions when sending Domain Events.
     * @param domainEventsInstantiator The instantiator used in the outbox sending process .
     */
    public AbstractGruelboxProxyMqChannelFactory(
        ServiceProvider serviceProvider,
        ClassProvider classProvider,
        HandlerExecutor handlerExecutor,
        ObjectMapper objectMapper,
        TransactionOutbox transactionOutbox,
        DomainEventsInstantiator domainEventsInstantiator
    ) {
        super(serviceProvider, classProvider, handlerExecutor, objectMapper);
        this.transactionOutbox = transactionOutbox;
        this.domainEventsInstantiator = domainEventsInstantiator;
        this.pollerConfiguration = new PollerConfiguration();
        this.publishingSchedulerConfiguration = new PublishingSchedulerConfiguration();
    }

    /**
     * Constructs a new AbstractGruelboxProxyMqChannelFactory with the provided parameters.
     *
     * @param serviceProvider The service provider for retrieving instances of various services.
     * @param classProvider The provider of Class instances based on full qualified class names.
     * @param handlerExecutor The executor for handling domain event listeners.
     * @param objectMapper The object mapper for serialization and deserialization.
     * @param transactionOutbox The transaction outbox for managing message transactions when sending Domain Events.
     * @param domainEventsInstantiator The instantiator used in the outbox sending process.
     * @param pollerConfiguration The configuration for a polling mechanism on the outbox.
     * @param publishingSchedulerConfiguration The configuration for a publishing scheduler regarding the outbox send process.
     */
    public AbstractGruelboxProxyMqChannelFactory(
        ServiceProvider serviceProvider,
        ClassProvider classProvider,
        HandlerExecutor handlerExecutor,
        ObjectMapper objectMapper,
        TransactionOutbox transactionOutbox,
        DomainEventsInstantiator domainEventsInstantiator,
        PollerConfiguration pollerConfiguration,
        PublishingSchedulerConfiguration publishingSchedulerConfiguration) {
        super(serviceProvider, classProvider, handlerExecutor, objectMapper);
        this.transactionOutbox = transactionOutbox;
        this.domainEventsInstantiator = domainEventsInstantiator;
        this.pollerConfiguration = pollerConfiguration;
        this.publishingSchedulerConfiguration = publishingSchedulerConfiguration;
    }

    /**
     * Constructs a new AbstractGruelboxProxyMqChannelFactory with the provided parameters.
     * This constructor is only sufficient for consume only channels
     *
     * @param serviceProvider The service provider for retrieving instances of various services.
     * @param classProvider The provider of Class instances based on full qualified class names.
     * @param handlerExecutor The executor for handling domain event listeners.
     * @param objectMapper The object mapper for serialization and deserialization.
     */
    public AbstractGruelboxProxyMqChannelFactory(
        ServiceProvider serviceProvider,
        ClassProvider classProvider,
        HandlerExecutor handlerExecutor,
        ObjectMapper objectMapper) {
        super(serviceProvider, classProvider, handlerExecutor, objectMapper);
        this.transactionOutbox = null;
        this.domainEventsInstantiator = null;
        this.pollerConfiguration = null;
        this.publishingSchedulerConfiguration = null;
    }

    /**
     * Initializes a new instance of AbstractGruelboxProxyMqChannelFactory with the provided parameters.
     *
     * @param objectMapper The object mapper for serialization and deserialization.
     * @param transactionOutbox The transaction outbox for managing message transactions when sending Domain Events.
     * @param domainEventsInstantiator The instantiator used in the outbox sending process.
     */
    public AbstractGruelboxProxyMqChannelFactory(
        ObjectMapper objectMapper,
        TransactionOutbox transactionOutbox,
        DomainEventsInstantiator domainEventsInstantiator
    ) {
        super(objectMapper);
        this.transactionOutbox = transactionOutbox;
        this.domainEventsInstantiator = domainEventsInstantiator;
        this.pollerConfiguration = new PollerConfiguration();
        this.publishingSchedulerConfiguration = new PublishingSchedulerConfiguration();
    }

    /**
     * Initializes a new instance of AbstractGruelboxProxyMqChannelFactory with the provided parameters.
     *
     * @param objectMapper The object mapper for serialization and deserialization.
     * @param transactionOutbox The transaction outbox for managing message transactions when sending Domain Events.
     * @param domainEventsInstantiator The instantiator used in the outbox sending process.
     * @param pollerConfiguration The configuration for a polling mechanism on the outbox.
     * @param publishingSchedulerConfiguration The configuration for a publishing scheduler regarding the outbox send process.
     */
    public AbstractGruelboxProxyMqChannelFactory(ObjectMapper objectMapper,
                                                 TransactionOutbox transactionOutbox,
                                                 DomainEventsInstantiator domainEventsInstantiator,
                                                 PollerConfiguration pollerConfiguration,
                                                 PublishingSchedulerConfiguration publishingSchedulerConfiguration) {
        super(objectMapper);
        this.transactionOutbox = transactionOutbox;
        this.domainEventsInstantiator = domainEventsInstantiator;
        this.pollerConfiguration = pollerConfiguration;
        this.publishingSchedulerConfiguration = publishingSchedulerConfiguration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractMqPublishingChannel publishOnlyChannel(String channelName) {
        return new AbstractMqPublishingChannel(channelName, publishingConfiguration());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractMqProcessingChannel processingChannel(String channelName) {
        return new AbstractMqProcessingChannel(channelName, consumingConfiguration(), publishingConfiguration());
    }

    /**
     * Constructs a new AbstractGruelboxProxyMqPublishingConfiguration instance with the provided parameters.
     *
     * @return A new instance of AbstractGruelboxProxyMqPublishingConfiguration with necessary configurations.
     */
    protected AbstractGruelboxProxyMqPublishingConfiguration publishingConfiguration(){
        return new AbstractGruelboxProxyMqPublishingConfiguration(
            provideMqDomainEventPublisher(super.objectMapper),
            this.transactionOutbox,
            this.pollerConfiguration,
            this.publishingSchedulerConfiguration,
            this.domainEventsInstantiator
        );
    }


}
