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
import io.domainlifecycles.events.consume.GeneralDomainEventConsumer;
import io.domainlifecycles.events.consume.execution.detector.MirrorBasedExecutionContextDetector;
import io.domainlifecycles.events.consume.execution.handler.HandlerExecutor;
import io.domainlifecycles.events.consume.execution.handler.ReflectiveHandlerExecutor;
import io.domainlifecycles.events.consume.execution.handler.TransactionalHandlerExecutor;
import io.domainlifecycles.events.consume.execution.processor.SimpleExecutionContextProcessor;
import io.domainlifecycles.events.gruelbox.dispatch.DirectGruelboxDomainEventDispatcher;
import io.domainlifecycles.events.gruelbox.idempotent.IdempotencyAwareHandlerExecutorProxy;
import io.domainlifecycles.events.gruelbox.idempotent.IdempotencyConfiguration;
import io.domainlifecycles.events.gruelbox.idempotent.IdempotentExecutor;
import io.domainlifecycles.events.gruelbox.poll.GruelboxPoller;
import io.domainlifecycles.services.api.ServiceProvider;

import java.util.Objects;

/**
 * The GruelboxConsumingConfigurationFactory class is responsible for creating consuming configurations for Gruelbox.
 * It provides methods to create standard consuming configurations and idempotent consuming configurations.
 *
 * @author Mario Herb
 */
class GruelboxConsumingConfigurationFactory {

    private final ServiceProvider serviceProvider;
    private final TransactionOutbox transactionOutbox;
    private final PollerConfiguration pollerConfiguration;
    private final DomainEventsInstantiator domainEventsInstantiator;

    /**
     * Creates a GruelboxConsumingConfigurationFactory with the given parameters.
     *
     * @param serviceProvider The ServiceProvider instance to be set. Cannot be null.
     * @param transactionOutbox The TransactionOutbox instance to be set. Cannot be null.
     * @param pollerConfiguration The PollerConfiguration instance to be set. Cannot be null.
     * @param domainEventsInstantiator The DomainEventsInstantiator instance to be set. Cannot be null.
     */
    GruelboxConsumingConfigurationFactory(
        ServiceProvider serviceProvider,
        TransactionOutbox transactionOutbox,
        PollerConfiguration pollerConfiguration,
        DomainEventsInstantiator domainEventsInstantiator
        ) {
        this.serviceProvider = Objects.requireNonNull(serviceProvider, "A ServiceProvider is required!");
        this.transactionOutbox = Objects.requireNonNull(transactionOutbox, "A TransactionOutbox is required!");
        this.domainEventsInstantiator = Objects.requireNonNull(domainEventsInstantiator, "A DomainEventsInstantiator is required!");
        this.pollerConfiguration = Objects.requireNonNull(pollerConfiguration, "A PollerConfiguration is required!");
    }

    /**
     * Creates a GruelboxConsumingConfigurationFactory with the given parameters.
     *
     * @param handlerExecutor The HandlerExecutor instance to be used for executing domain event listeners. Cannot be null.
     * @return A new GruelboxConsumingConfiguration instance configured with the provided HandlerExecutor.
     */
    GruelboxConsumingConfiguration consumingConfiguration(HandlerExecutor handlerExecutor){
        Objects.requireNonNull(handlerExecutor, "HandlerExecutor is required!");
        var poller = new GruelboxPoller(transactionOutbox, pollerConfiguration);
        var executionContextDetector = new MirrorBasedExecutionContextDetector(this.serviceProvider);
        var executionContextProcessor = new SimpleExecutionContextProcessor(handlerExecutor);
        var domainEventConsumer = new GeneralDomainEventConsumer(executionContextDetector, executionContextProcessor);
        var gruelboxDomainEventDispatcher = new DirectGruelboxDomainEventDispatcher(domainEventConsumer);
        domainEventsInstantiator.registerGruelboxDomainEventDispatcher(gruelboxDomainEventDispatcher);
        return new GruelboxConsumingConfiguration(poller, gruelboxDomainEventDispatcher, handlerExecutor);
    }

    /**
     * Configures a GruelboxConsumingConfiguration with the provided IdempotencyConfiguration
     * and TransactionalHandlerExecutor.
     *
     * @param idempotencyConfiguration The IdempotencyConfiguration to be used for idempotent consuming. Must not be null.
     * @param transactionalHandlerExecutor The TransactionalHandlerExecutor to handle transactional execution. Must not be null.
     * @return A GruelboxConsumingConfiguration instance configured with the specified parameters.
     */
    GruelboxConsumingConfiguration idempotentConsumingConfiguration(IdempotencyConfiguration idempotencyConfiguration,
                                                                    TransactionalHandlerExecutor transactionalHandlerExecutor){
        Objects.requireNonNull(idempotencyConfiguration, "IdempotencyConfiguration is required!");
        Objects.requireNonNull(transactionalHandlerExecutor, "TransactionalHandlerExecutor is required!");
        var usedHandlerExecutor = new IdempotencyAwareHandlerExecutorProxy(
            transactionalHandlerExecutor,
            idempotencyConfiguration,
            transactionOutbox
        );
        domainEventsInstantiator.registerIdempotentExecutor(new IdempotentExecutor(serviceProvider, new ReflectiveHandlerExecutor()));
        return consumingConfiguration(usedHandlerExecutor);
    }

}
