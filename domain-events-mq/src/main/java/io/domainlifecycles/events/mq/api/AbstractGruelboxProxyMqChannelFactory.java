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

package io.domainlifecycles.events.mq.api;

import com.gruelbox.transactionoutbox.TransactionOutbox;
import io.domainlifecycles.access.classes.ClassProvider;
import io.domainlifecycles.events.consume.execution.detector.MirrorBasedExecutionContextDetector;
import io.domainlifecycles.events.consume.execution.handler.HandlerExecutor;
import io.domainlifecycles.events.consume.execution.handler.TransactionalHandlerExecutor;
import io.domainlifecycles.events.consume.execution.processor.SimpleExecutionContextProcessor;
import io.domainlifecycles.events.exception.DLCEventsException;
import io.domainlifecycles.events.gruelbox.api.DomainEventsInstantiator;
import io.domainlifecycles.events.gruelbox.api.PollerConfiguration;
import io.domainlifecycles.events.gruelbox.api.PublishingSchedulerConfiguration;
import io.domainlifecycles.events.gruelbox.idempotent.IdempotentExecutor;
import io.domainlifecycles.events.gruelbox.poll.GruelboxPoller;
import io.domainlifecycles.events.mq.consume.TransactionalIdempotencyAwareHandlerExecutorProxy;
import io.domainlifecycles.events.serialize.DomainEventSerializer;
import io.domainlifecycles.services.api.ServiceProvider;

import java.util.Objects;

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
    private final GruelboxPoller poller;
    private final PublishingSchedulerConfiguration publishingSchedulerConfiguration;
    private final DomainEventsInstantiator domainEventsInstantiator;
    private final TransactionalIdempotencyAwareHandlerExecutorProxy transactionalIdempotencyAwareHandlerExecutorProxy;

    /**
     * Constructs a new AbstractGruelboxProxyMqChannelFactory with the provided parameters.
     *
     * @param serviceProvider The service provider for retrieving instances of various services.
     * @param classProvider The provider of Class instances based on full qualified class names.
     * @param handlerExecutor The executor for handling domain event listeners.
     * @param domainEventSerializer for serialization and deserialization.
     * @param transactionOutbox The transaction outbox for managing message transactions when sending Domain Events.
     * @param domainEventsInstantiator The instantiator used in the outbox sending process .
     */
    public AbstractGruelboxProxyMqChannelFactory(
        ServiceProvider serviceProvider,
        ClassProvider classProvider,
        HandlerExecutor handlerExecutor,
        DomainEventSerializer domainEventSerializer,
        TransactionOutbox transactionOutbox,
        DomainEventsInstantiator domainEventsInstantiator
    ) {
        super(serviceProvider, classProvider, handlerExecutor, domainEventSerializer);
        this.transactionOutbox = transactionOutbox;
        this.domainEventsInstantiator = domainEventsInstantiator;
        this.pollerConfiguration = new PollerConfiguration();
        this.poller = new GruelboxPoller(transactionOutbox, pollerConfiguration);
        this.publishingSchedulerConfiguration = new PublishingSchedulerConfiguration();
        this.transactionalIdempotencyAwareHandlerExecutorProxy = null;
    }

    /**
     * Constructs a new AbstractGruelboxProxyMqChannelFactory with the provided parameters.
     *
     * @param serviceProvider The service provider for retrieving instances of various services.
     * @param classProvider The provider of Class instances based on full qualified class names.
     * @param handlerExecutor The executor for handling domain event listeners.
     * @param domainEventSerializer for serialization and deserialization.
     * @param transactionOutbox The transaction outbox for managing message transactions when sending Domain Events.
     * @param domainEventsInstantiator The instantiator used in the outbox sending process.
     * @param idempotencyAwareHandlerExecutorProxy The handler executor proxy needed for idempotency handling
     */
    public AbstractGruelboxProxyMqChannelFactory(
        ServiceProvider serviceProvider,
        ClassProvider classProvider,
        HandlerExecutor handlerExecutor,
        DomainEventSerializer domainEventSerializer,
        TransactionOutbox transactionOutbox,
        DomainEventsInstantiator domainEventsInstantiator,
        TransactionalIdempotencyAwareHandlerExecutorProxy idempotencyAwareHandlerExecutorProxy
    ) {
        super(serviceProvider, classProvider, handlerExecutor, domainEventSerializer);
        this.transactionOutbox = transactionOutbox;
        this.domainEventsInstantiator = domainEventsInstantiator;
        this.pollerConfiguration = new PollerConfiguration();
        this.poller = new GruelboxPoller(transactionOutbox, pollerConfiguration);
        this.publishingSchedulerConfiguration = new PublishingSchedulerConfiguration();
        this.transactionalIdempotencyAwareHandlerExecutorProxy = idempotencyAwareHandlerExecutorProxy;
    }

    /**
     * Constructs a new AbstractGruelboxProxyMqChannelFactory with the provided parameters.
     *
     * @param serviceProvider The service provider for retrieving instances of various services.
     * @param classProvider The provider of Class instances based on full qualified class names.
     * @param handlerExecutor The executor for handling domain event listeners.
     * @param domainEventSerializer for serialization and deserialization.
     * @param transactionOutbox The transaction outbox for managing message transactions when sending Domain Events.
     * @param domainEventsInstantiator The instantiator used in the outbox sending process.
     * @param pollerConfiguration The configuration for a polling mechanism on the outbox.
     * @param publishingSchedulerConfiguration The configuration for a publishing scheduler regarding the outbox send process.
     */
    public AbstractGruelboxProxyMqChannelFactory(
        ServiceProvider serviceProvider,
        ClassProvider classProvider,
        HandlerExecutor handlerExecutor,
        DomainEventSerializer domainEventSerializer,
        TransactionOutbox transactionOutbox,
        DomainEventsInstantiator domainEventsInstantiator,
        PollerConfiguration pollerConfiguration,
        PublishingSchedulerConfiguration publishingSchedulerConfiguration) {
        super(serviceProvider, classProvider, handlerExecutor, domainEventSerializer);
        this.transactionOutbox = transactionOutbox;
        this.domainEventsInstantiator = domainEventsInstantiator;
        this.pollerConfiguration = pollerConfiguration;
        this.poller = new GruelboxPoller(transactionOutbox, pollerConfiguration);
        this.publishingSchedulerConfiguration = publishingSchedulerConfiguration;
        this.transactionalIdempotencyAwareHandlerExecutorProxy = null;
    }

    /**
     * Constructs a new AbstractGruelboxProxyMqChannelFactory with the provided parameters.
     *
     * @param serviceProvider The service provider for retrieving instances of various services.
     * @param classProvider The provider of Class instances based on full qualified class names.
     * @param handlerExecutor The executor for handling domain event listeners.
     * @param domainEventSerializer for serialization and deserialization.
     * @param transactionOutbox The transaction outbox for managing message transactions when sending Domain Events.
     * @param domainEventsInstantiator The instantiator used in the outbox sending process.
     * @param pollerConfiguration The configuration for a polling mechanism on the outbox.
     * @param publishingSchedulerConfiguration The configuration for a publishing scheduler regarding the outbox send process.
     * @param idempotencyAwareHandlerExecutorProxy The handler executor proxy needed for idempotency handling
     */
    public AbstractGruelboxProxyMqChannelFactory(
        ServiceProvider serviceProvider,
        ClassProvider classProvider,
        HandlerExecutor handlerExecutor,
        DomainEventSerializer domainEventSerializer,
        TransactionOutbox transactionOutbox,
        DomainEventsInstantiator domainEventsInstantiator,
        PollerConfiguration pollerConfiguration,
        PublishingSchedulerConfiguration publishingSchedulerConfiguration,
        TransactionalIdempotencyAwareHandlerExecutorProxy idempotencyAwareHandlerExecutorProxy) {
        super(serviceProvider, classProvider, handlerExecutor, domainEventSerializer);
        this.transactionOutbox = transactionOutbox;
        this.domainEventsInstantiator = domainEventsInstantiator;
        this.pollerConfiguration = pollerConfiguration;
        this.poller = new GruelboxPoller(transactionOutbox, pollerConfiguration);
        this.publishingSchedulerConfiguration = publishingSchedulerConfiguration;
        this.transactionalIdempotencyAwareHandlerExecutorProxy = idempotencyAwareHandlerExecutorProxy;
    }

    /**
     * Constructs a new AbstractGruelboxProxyMqChannelFactory with the provided parameters.
     * This constructor is only sufficient for consume only channels without deduplication/idempotency protection.
     *
     * @param serviceProvider The service provider for retrieving instances of various services.
     * @param classProvider The provider of Class instances based on full qualified class names.
     * @param handlerExecutor The executor for handling domain event listeners.
     * @param domainEventSerializer for serialization and deserialization.
     */
    public AbstractGruelboxProxyMqChannelFactory(
        ServiceProvider serviceProvider,
        ClassProvider classProvider,
        HandlerExecutor handlerExecutor,
        DomainEventSerializer domainEventSerializer) {
        super(serviceProvider, classProvider, handlerExecutor, domainEventSerializer);
        this.transactionOutbox = null;
        this.domainEventsInstantiator = null;
        this.pollerConfiguration = null;
        this.poller = null;
        this.publishingSchedulerConfiguration = null;
        this.transactionalIdempotencyAwareHandlerExecutorProxy = null;
    }

    /**
     * Initializes a new instance of AbstractGruelboxProxyMqChannelFactory with the provided parameters.
     *
     * @param domainEventSerializer for serialization and deserialization.
     * @param transactionOutbox The transaction outbox for managing message transactions when sending Domain Events.
     * @param domainEventsInstantiator The instantiator used in the outbox sending process.
     */
    public AbstractGruelboxProxyMqChannelFactory(
        DomainEventSerializer domainEventSerializer,
        TransactionOutbox transactionOutbox,
        DomainEventsInstantiator domainEventsInstantiator
    ) {
        super(domainEventSerializer);
        this.transactionOutbox = transactionOutbox;
        this.domainEventsInstantiator = domainEventsInstantiator;
        this.pollerConfiguration = new PollerConfiguration();
        this.poller = new GruelboxPoller(transactionOutbox, pollerConfiguration);
        this.publishingSchedulerConfiguration = new PublishingSchedulerConfiguration();
        this.transactionalIdempotencyAwareHandlerExecutorProxy = null;
    }

    /**
     * Initializes a new instance of AbstractGruelboxProxyMqChannelFactory with the provided parameters.
     *
     * @param domainEventSerializer for serialization and deserialization.
     * @param transactionOutbox The transaction outbox for managing message transactions when sending Domain Events.
     * @param domainEventsInstantiator The instantiator used in the outbox sending process.
     * @param pollerConfiguration The configuration for a polling mechanism on the outbox.
     * @param publishingSchedulerConfiguration The configuration for a publishing scheduler regarding the outbox send process.
     */
    public AbstractGruelboxProxyMqChannelFactory(DomainEventSerializer domainEventSerializer,
                                                 TransactionOutbox transactionOutbox,
                                                 DomainEventsInstantiator domainEventsInstantiator,
                                                 PollerConfiguration pollerConfiguration,
                                                 PublishingSchedulerConfiguration publishingSchedulerConfiguration) {
        super(domainEventSerializer);
        this.transactionOutbox = transactionOutbox;
        this.domainEventsInstantiator = domainEventsInstantiator;
        this.pollerConfiguration = pollerConfiguration;
        this.poller = new GruelboxPoller(transactionOutbox, pollerConfiguration);
        this.publishingSchedulerConfiguration = publishingSchedulerConfiguration;
        this.transactionalIdempotencyAwareHandlerExecutorProxy = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MqPublishingChannel publishOnlyChannel(String channelName) {
        return new MqPublishingChannel(channelName, publishingConfiguration());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MqProcessingChannel processingChannel(String channelName) {
        return new MqProcessingChannel(channelName, consumingConfiguration(), publishingConfiguration());
    }

    /**
     * Constructs a new GruelboxProxyMqPublishingConfiguration instance with the provided parameters.
     *
     * @return A new instance of GruelboxProxyMqPublishingConfiguration with necessary configurations.
     */
    @Override
    protected GruelboxProxyMqPublishingConfiguration publishingConfiguration(){
        return new GruelboxProxyMqPublishingConfiguration(
            provideMqDomainEventPublisher(super.domainEventSerializer),
            this.transactionOutbox,
            this.poller,
            this.publishingSchedulerConfiguration,
            this.domainEventsInstantiator
        );
    }

    @Override
    protected GruelboxProxyMqConsumingConfiguration consumingConfiguration(){
        if(this.transactionalIdempotencyAwareHandlerExecutorProxy != null){
            if(!(this.handlerExecutor instanceof TransactionalHandlerExecutor)){
                throw DLCEventsException.fail("A TransactionalHandlerExecutor is required for idempotency protection!");
            }
            return idempotentConsumingConfiguration((TransactionalHandlerExecutor) this.handlerExecutor, this.transactionalIdempotencyAwareHandlerExecutorProxy);
        }else{
            return consumingConfiguration(this.handlerExecutor);
        }
    }

    /**
     * Creates a GruelboxProxyMqConsumingConfiguration with the given parameters.
     *
     * @return A new GruelboxProxyMqConsumingConfiguration instance configured with the provided HandlerExecutor.
     */
    GruelboxProxyMqConsumingConfiguration consumingConfiguration(HandlerExecutor handlerExecutor){
        Objects.requireNonNull(serviceProvider, "A ServiceProvider is required!");
        var executionContextDetector = new MirrorBasedExecutionContextDetector(serviceProvider);
        var executionContextProcessor = new SimpleExecutionContextProcessor(Objects.requireNonNull(handlerExecutor,"A HandlerExecutor is required!"));
        return new GruelboxProxyMqConsumingConfiguration(
            provideMqDomainEventConsumer(
                this.domainEventSerializer,
                executionContextDetector,
                executionContextProcessor,
                Objects.requireNonNull(this.classProvider, "A ClassProvider is required!")
            ),
            handlerExecutor
        );
    }

    /**
     * Configures a GruelboxConsumingConfiguration with the provided IdempotencyConfiguration
     * and TransactionalHandlerExecutor.
     *

     * @return A GruelboxProxyMqConsumingConfiguration instance configured with the specified parameters.
     */
    GruelboxProxyMqConsumingConfiguration idempotentConsumingConfiguration(TransactionalHandlerExecutor transactionalHandlerExecutor, TransactionalIdempotencyAwareHandlerExecutorProxy idempotencyAwareHandlerExecutorProxy){
        Objects.requireNonNull(domainEventsInstantiator, "A DomainEventsInstantiator is required for idempotency protection!");
        domainEventsInstantiator.registerIdempotentExecutor(new IdempotentExecutor(serviceProvider, transactionalHandlerExecutor));
        return consumingConfiguration(idempotencyAwareHandlerExecutorProxy);
    }





}
