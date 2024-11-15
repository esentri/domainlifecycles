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

package io.domainlifecycles.events.gruelbox.api;

import com.gruelbox.transactionoutbox.TransactionOutbox;
import io.domainlifecycles.events.api.ConsumingOnlyChannel;
import io.domainlifecycles.events.api.ProcessingChannel;
import io.domainlifecycles.events.api.ChannelFactory;
import io.domainlifecycles.events.api.PublishingOnlyChannel;
import io.domainlifecycles.events.consume.execution.handler.TransactionalHandlerExecutor;
import io.domainlifecycles.events.gruelbox.idempotent.IdempotencyConfiguration;
import io.domainlifecycles.services.api.ServiceProvider;
import java.util.Objects;

/**
 * The GruelboxChannelFactory class implements the ChannelFactory interface and provides
 * methods for creating ConsumingOnlyChannel, PublishingOnlyChannel,
 * and ProcessingChannel objects for handling domain events.
 *
 * It uses thr Gruelbox transaction outbox implementation as messaging infrastructure.
 *
 * @author Mario Herb
 */
public class GruelboxChannelFactory implements ChannelFactory {

    private final ServiceProvider serviceProvider;
    private final TransactionOutbox transactionOutbox;
    private final TransactionalHandlerExecutor transactionalHandlerExecutor;
    private final DomainEventsInstantiator domainEventsInstantiator;
    private final IdempotencyConfiguration idempotencyConfiguration;
    private final PollerConfiguration pollerConfiguration;
    private final PublishingSchedulerConfiguration publishingSchedulerConfiguration;

    /**
     * Constructs a new GruelboxChannelFactory with the provided dependencies.
     *
     * @param serviceProvider The service provider responsible for providing various types of services.
     * @param transactionOutbox The transaction outbox required for handling reliable domain event publishing and processing.
     * @param transactionalHandlerExecutor The executor for domain event handlers in a transactional manner.
     * @param domainEventsInstantiator The instantiator for domain event handlers.
     * @param idempotencyConfiguration The configuration for idempotency protection of event handlers.
     * @param pollerConfiguration The configuration for polling.
     * @param publishingSchedulerConfiguration The configuration for publishing scheduler.
     */
    public GruelboxChannelFactory(ServiceProvider serviceProvider,
                                  TransactionOutbox transactionOutbox,
                                  TransactionalHandlerExecutor transactionalHandlerExecutor,
                                  DomainEventsInstantiator domainEventsInstantiator,
                                  IdempotencyConfiguration idempotencyConfiguration,
                                  PollerConfiguration pollerConfiguration,
                                  PublishingSchedulerConfiguration publishingSchedulerConfiguration) {
        this.serviceProvider = serviceProvider;
        this.transactionOutbox = Objects.requireNonNull(transactionOutbox, "A TransactionOutbox is required!");
        this.transactionalHandlerExecutor = transactionalHandlerExecutor;
        this.domainEventsInstantiator = Objects.requireNonNull(domainEventsInstantiator, "A DomainEventsInstantiator is required!");
        this.idempotencyConfiguration = idempotencyConfiguration;
        this.pollerConfiguration = (pollerConfiguration == null ?
            new PollerConfiguration() : pollerConfiguration);
        this.publishingSchedulerConfiguration = (publishingSchedulerConfiguration == null ?
            new PublishingSchedulerConfiguration() : publishingSchedulerConfiguration);
    }

    /**
     * Constructs a new GruelboxChannelFactory with the provided dependencies.
     *
     * @param serviceProvider The service provider responsible for providing various types of services.
     * @param transactionOutbox The transaction outbox required for handling reliable domain event publishing and processing.
     * @param transactionalHandlerExecutor The executor for domain event handlers in a transactional manner.
     * @param domainEventsInstantiator The instantiator for domain event handlers.
     * @param idempotencyConfiguration The configuration for idempotency protection of event handlers.
     */
    public GruelboxChannelFactory(ServiceProvider serviceProvider,
                                  TransactionOutbox transactionOutbox,
                                  TransactionalHandlerExecutor transactionalHandlerExecutor,
                                  DomainEventsInstantiator domainEventsInstantiator,
                                  IdempotencyConfiguration idempotencyConfiguration) {
        this(
            serviceProvider,
            transactionOutbox,
            transactionalHandlerExecutor,
            domainEventsInstantiator,
            idempotencyConfiguration,
            null,
            null
        );
    }

    /**
     * Constructs a new GruelboxChannelFactory with the provided dependencies.
     *
     * @param serviceProvider The service provider responsible for providing various types of services.
     * @param transactionOutbox The transaction outbox required for handling reliable domain event publishing and processing.
     * @param transactionalHandlerExecutor The executor for domain event handlers in a transactional manner.
     * @param domainEventsInstantiator The instantiator for domain event handlers.
     */
    public GruelboxChannelFactory(ServiceProvider serviceProvider,
                                  TransactionOutbox transactionOutbox,
                                  TransactionalHandlerExecutor transactionalHandlerExecutor,
                                  DomainEventsInstantiator domainEventsInstantiator) {
        this(
            serviceProvider,
            transactionOutbox,
            transactionalHandlerExecutor,
            domainEventsInstantiator,
            null,
            null,
            null
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConsumingOnlyChannel consumeOnlyChannel(String channelName) {
        return new ConsumingOnlyChannel(channelName, createGruelboxConsumingConfiguration());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PublishingOnlyChannel publishOnlyChannel(String channelName) {
        return new PublishingOnlyChannel(channelName, createGruelboxPublishingConfiguration());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProcessingChannel processingChannel(String channelName) {
        return new ProcessingChannel(channelName, createGruelboxPublishingConfiguration(), createGruelboxConsumingConfiguration());
    }

    private GruelboxConsumingConfiguration createGruelboxConsumingConfiguration(){
        var factory = new GruelboxConsumingConfigurationFactory(
            this.serviceProvider,
            this.transactionOutbox,
            this.pollerConfiguration,
            this.domainEventsInstantiator
        );
        if(idempotencyConfiguration != null){
            return factory.idempotentConsumingConfiguration(idempotencyConfiguration, transactionalHandlerExecutor);
        }else{
            return factory.consumingConfiguration(transactionalHandlerExecutor);
        }
    }

    private GruelboxPublishingConfiguration createGruelboxPublishingConfiguration(){
        return new GruelboxPublishingConfiguration(transactionOutbox, publishingSchedulerConfiguration);
    }

}
