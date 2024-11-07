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

package io.domainlifecycles.events.api;

import jakarta.transaction.TransactionManager;
import io.domainlifecycles.events.exception.DLCEventsException;
import io.domainlifecycles.events.publish.outbox.poll.AbstractOutboxPoller;
import io.domainlifecycles.events.publish.outbox.poll.DirectOutboxPoller;
import io.domainlifecycles.events.publish.outbox.JtaOutboxDomainEventPublisher;
import io.domainlifecycles.events.publish.outbox.SpringOutboxDomainEventPublisher;
import io.domainlifecycles.events.publish.outbox.api.TransactionalOutbox;
import io.domainlifecycles.events.receive.execution.GeneralReceivingDomainEventHandler;
import io.domainlifecycles.events.receive.execution.ReceivingDomainEventHandler;
import io.domainlifecycles.events.publish.DomainEventPublisher;
import io.domainlifecycles.events.publish.direct.DirectPassThroughDomainEventPublisher;
import io.domainlifecycles.events.publish.direct.DirectJtaTransactionalDomainEventPublisher;
import io.domainlifecycles.events.publish.direct.DirectSpringTransactionalDomainEventPublisher;
import io.domainlifecycles.events.receive.execution.detector.ExecutionContextDetector;
import io.domainlifecycles.events.receive.execution.detector.MirrorBasedExecutionContextDetector;
import io.domainlifecycles.events.receive.execution.handler.HandlerExecutor;
import io.domainlifecycles.events.receive.execution.handler.JtaTransactionalHandlerExecutor;
import io.domainlifecycles.events.receive.execution.handler.ReflectiveHandlerExecutor;
import io.domainlifecycles.events.receive.execution.handler.SpringTransactionalHandlerExecutor;
import io.domainlifecycles.events.receive.execution.processor.AsyncExecutionContextProcessor;
import io.domainlifecycles.events.receive.execution.processor.ExecutionContextProcessor;
import io.domainlifecycles.services.api.ServiceProvider;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Objects;

/**
 * The DomainEventsConfiguration class represents the configuration
 * for handling and dispatching domain events in the application.
 *
 * @author Mario Herb
 */
public class DomainEventsConfiguration {


    public final DomainEventPublisher domainEventPublisher;
    public final ServiceProvider serviceProvider;
    public final ReceivingDomainEventHandler receivingDomainEventHandler;
    public final ExecutionContextDetector executionContextDetector;
    public final HandlerExecutor handlerExecutor;
    public final ExecutionContextProcessor executionContextProcessor;
    public final TransactionalOutbox transactionalOutbox;
    public final PlatformTransactionManager springPlatformTransactionManager;
    public final TransactionManager jtaTransactionManager;
    public final AbstractOutboxPoller outboxPoller;


    private DomainEventsConfiguration(
        DomainEventPublisher domainEventPublisher,
        ServiceProvider serviceProvider,
        ReceivingDomainEventHandler receivingDomainEventHandler,
        ExecutionContextDetector executionContextDetector,
        HandlerExecutor handlerExecutor,
        ExecutionContextProcessor executionContextProcessor,
        TransactionalOutbox transactionalOutbox,
        PlatformTransactionManager springPlatformTransactionManager,
        TransactionManager jtaTransactionManager,
        AbstractOutboxPoller outboxPoller
    ) {
        this.domainEventPublisher = domainEventPublisher;
        this.serviceProvider = serviceProvider;
        this.receivingDomainEventHandler = receivingDomainEventHandler;
        this.executionContextDetector = executionContextDetector;
        this.handlerExecutor = handlerExecutor;
        this.executionContextProcessor = executionContextProcessor;
        this.transactionalOutbox = transactionalOutbox;
        this.springPlatformTransactionManager = springPlatformTransactionManager;
        this.jtaTransactionManager = jtaTransactionManager;
        this.outboxPoller = outboxPoller;
    }

    /**
     * The DomainEventsConfigurationBuilder class is used to build a configuration for publishing and handling
     * domain events in the application.
     */
    public static class DomainEventsConfigurationBuilder {

        private DomainEventPublisher domainEventPublisher;
        private ServiceProvider serviceProvider;
        private ReceivingDomainEventHandler receivingDomainEventHandler;
        private ExecutionContextDetector executionContextDetector;
        private HandlerExecutor handlerExecutor;
        private ExecutionContextProcessor executionContextProcessor;
        private TransactionalOutbox transactionalOutbox;
        private PlatformTransactionManager springPlatformTransactionManager;
        private TransactionManager jtaTransactionManager;

        private AbstractOutboxPoller outboxPoller;


        public DomainEventsConfigurationBuilder withDomainEventPublisher(DomainEventPublisher domainEventPublisher) {
            this.domainEventPublisher = domainEventPublisher;
            return this;
        }

        public DomainEventsConfigurationBuilder withServiceProvider(ServiceProvider serviceProvider) {
            this.serviceProvider = serviceProvider;
            return this;
        }

        public DomainEventsConfigurationBuilder withReceivingDomainEventHandler(ReceivingDomainEventHandler receivingDomainEventHandler) {
            this.receivingDomainEventHandler = receivingDomainEventHandler;
            return this;
        }

        public DomainEventsConfigurationBuilder withExecutionContextDetector(ExecutionContextDetector executionContextDetector) {
            this.executionContextDetector = executionContextDetector;
            return this;
        }

        public DomainEventsConfigurationBuilder withHandlerExecutor(HandlerExecutor handlerExecutor) {
            this.handlerExecutor = handlerExecutor;
            return this;
        }

        public DomainEventsConfigurationBuilder withExecutionContextProcessor(ExecutionContextProcessor executionContextProcessor) {
            this.executionContextProcessor = executionContextProcessor;
            return this;
        }

        public DomainEventsConfigurationBuilder withTransactionalOutbox(TransactionalOutbox transactionalOutbox) {
            this.transactionalOutbox = transactionalOutbox;
            return this;
        }

        public DomainEventsConfigurationBuilder withSpringPlatformTransactionManager(PlatformTransactionManager springPlatformTransactionManager) {
            this.springPlatformTransactionManager = springPlatformTransactionManager;
            return this;
        }

        public DomainEventsConfigurationBuilder withJtaTransactionManager(TransactionManager jtaTransactionManager) {
            this.jtaTransactionManager = jtaTransactionManager;
            return this;
        }

        public DomainEventsConfigurationBuilder withOutboxPoller(AbstractOutboxPoller outboxPoller) {
            this.outboxPoller = outboxPoller;
            return this;
        }


        public DomainEventsConfiguration make() {
            Objects.requireNonNull(
                this.serviceProvider,
                "A ServiceProvider is needed to be able to dispatch events properly!"
            );

            if (this.springPlatformTransactionManager != null && this.jtaTransactionManager != null) {
                throw DLCEventsException.fail(
                    "Either Spring transaction management or JTA transaction management can be activated!");
            }

            if (this.executionContextDetector == null) {
                this.executionContextDetector = new MirrorBasedExecutionContextDetector(serviceProvider);
            }

            if (this.springPlatformTransactionManager != null) {
                if (this.handlerExecutor == null) {
                    this.handlerExecutor = new SpringTransactionalHandlerExecutor(
                        this.springPlatformTransactionManager);
                }
                if (this.executionContextProcessor == null) {
                    this.executionContextProcessor = new AsyncExecutionContextProcessor(this.handlerExecutor);
                }
                if (this.receivingDomainEventHandler == null) {
                    this.receivingDomainEventHandler = new GeneralReceivingDomainEventHandler(
                        this.executionContextDetector, this.executionContextProcessor);
                }
                if (this.transactionalOutbox != null) {
                    this.domainEventPublisher = new SpringOutboxDomainEventPublisher(this.transactionalOutbox,
                        this.springPlatformTransactionManager);
                    if (this.outboxPoller == null) {
                        this.outboxPoller = new DirectOutboxPoller(this.transactionalOutbox,
                            receivingDomainEventHandler);
                    }
                } else {
                    this.domainEventPublisher = new DirectSpringTransactionalDomainEventPublisher(
                        this.receivingDomainEventHandler);
                }
            } else if (this.jtaTransactionManager != null) {
                if (this.handlerExecutor == null) {
                    this.handlerExecutor = new JtaTransactionalHandlerExecutor(this.jtaTransactionManager);
                }
                if (this.executionContextProcessor == null) {
                    this.executionContextProcessor = new AsyncExecutionContextProcessor(this.handlerExecutor);
                }
                if (this.receivingDomainEventHandler == null) {
                    this.receivingDomainEventHandler = new GeneralReceivingDomainEventHandler(
                        this.executionContextDetector, this.executionContextProcessor);
                }
                if (this.transactionalOutbox != null) {
                    this.domainEventPublisher = new JtaOutboxDomainEventPublisher(this.transactionalOutbox,
                        this.jtaTransactionManager);
                    if (this.outboxPoller == null) {
                        this.outboxPoller = new DirectOutboxPoller(this.transactionalOutbox,
                            receivingDomainEventHandler);
                    }
                } else {
                    this.domainEventPublisher = new DirectJtaTransactionalDomainEventPublisher(
                        this.receivingDomainEventHandler, this.jtaTransactionManager);
                }
            } else {
                if (this.handlerExecutor == null) {
                    this.handlerExecutor = new ReflectiveHandlerExecutor();
                }
                if (this.executionContextProcessor == null) {
                    this.executionContextProcessor = new AsyncExecutionContextProcessor(this.handlerExecutor);
                }
                if (this.receivingDomainEventHandler == null) {
                    this.receivingDomainEventHandler = new GeneralReceivingDomainEventHandler(
                        this.executionContextDetector, this.executionContextProcessor);
                }
                if (this.domainEventPublisher == null) {
                    this.domainEventPublisher = new DirectPassThroughDomainEventPublisher(
                        this.receivingDomainEventHandler);
                }
            }

            var config = new DomainEventsConfiguration(
                this.domainEventPublisher,
                this.serviceProvider,
                this.receivingDomainEventHandler,
                this.executionContextDetector,
                this.handlerExecutor,
                this.executionContextProcessor,
                this.transactionalOutbox,
                this.springPlatformTransactionManager,
                this.jtaTransactionManager,
                this.outboxPoller
            );
            DomainEvents.registerConfiguration(config);
            return config;
        }
    }
}
