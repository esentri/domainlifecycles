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

package io.domainlifecycles.events.spring.outbox.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.domainlifecycles.events.api.ChannelFactory;
import io.domainlifecycles.events.api.ConsumingOnlyChannel;
import io.domainlifecycles.events.api.ProcessingChannel;
import io.domainlifecycles.events.api.PublishingOnlyChannel;
import io.domainlifecycles.events.consume.GeneralDomainEventConsumer;
import io.domainlifecycles.events.consume.execution.detector.MirrorBasedExecutionContextDetector;
import io.domainlifecycles.events.consume.execution.processor.SimpleExecutionContextProcessor;
import io.domainlifecycles.events.spring.outbox.SpringOutboxDomainEventPublisher;
import io.domainlifecycles.events.spring.outbox.impl.SpringJdbcOutbox;
import io.domainlifecycles.events.spring.outbox.poll.AbstractOutboxPoller;
import io.domainlifecycles.events.spring.outbox.poll.DirectOutboxPoller;
import io.domainlifecycles.events.spring.receive.execution.handler.SpringTransactionalHandlerExecutor;
import io.domainlifecycles.services.api.ServiceProvider;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Objects;

/**
 * The SpringTransactionalOutboxChannelFactory class represents a factory for creating channels for publishing and consuming domain events
 * in a SpringOutbox context.
 *
 * @author Mario Herb
 */
public class SpringTransactionalOutboxChannelFactory implements ChannelFactory {

    private final PlatformTransactionManager platformTransactionManager;
    private final ObjectMapper objectMapper;
    private final DataSource dataSource;
    private final ServiceProvider serviceProvider;

    private final TransactionalOutbox transactionalOutbox;
    private final AbstractOutboxPoller poller;

    /**
     * Constructs a new SpringTransactionalOutboxChannelFactory with the provided dependencies.
     *
     * @param platformTransactionManager The platform transaction manager to be used.
     * @param objectMapper The object mapper for serialization and deserialization.
     * @param dataSource The data source for database interaction.
     * @param serviceProvider The service provider for retrieving various service instances.
     */
    public SpringTransactionalOutboxChannelFactory(
        PlatformTransactionManager platformTransactionManager,
        ObjectMapper objectMapper,
        DataSource dataSource,
        ServiceProvider serviceProvider
    ) {
        this(
            platformTransactionManager,
            objectMapper,
            dataSource,
            serviceProvider,
            new SpringJdbcOutbox(dataSource, objectMapper, platformTransactionManager),
            null
        );
    }

    /**
     * Constructs a new SpringTransactionalOutboxChannelFactory with the provided dependencies.
     *
     * @param platformTransactionManager The platform transaction manager to be used.
     * @param objectMapper The object mapper for serialization and deserialization.
     * @param dataSource The data source for database interaction.
     * @param serviceProvider The service provider for retrieving various service instances.
     * @param outbox The transactional outbox for storing domain events before sending them.
     * @param poller The poller used for polling the outbox for events.
     */
    public SpringTransactionalOutboxChannelFactory(
        PlatformTransactionManager platformTransactionManager,
        ObjectMapper objectMapper,
        DataSource dataSource,
        ServiceProvider serviceProvider,
        TransactionalOutbox outbox,
        AbstractOutboxPoller poller) {
        this.dataSource = Objects.requireNonNull(dataSource, "A DataSource is required!");
        this.platformTransactionManager = Objects.requireNonNull(platformTransactionManager, "A PlatformTransactionManager is required!");
        this.objectMapper = Objects.requireNonNull(objectMapper, "An ObjectMapper is required!");
        this.serviceProvider = serviceProvider;
        this.transactionalOutbox = Objects.requireNonNull(outbox, "A TransactionalOutbox is required!");
        this.poller = poller;
    }

    /**
     * Constructs a new SpringTransactionalOutboxChannelFactory with the provided dependencies.
     *
     * @param platformTransactionManager The platform transaction manager to be used.
     * @param objectMapper The object mapper for serialization and deserialization.
     * @param dataSource The data source for database interaction.
     */
    public SpringTransactionalOutboxChannelFactory(PlatformTransactionManager platformTransactionManager, ObjectMapper objectMapper, DataSource dataSource) {
        this(platformTransactionManager, objectMapper, dataSource, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConsumingOnlyChannel consumeOnlyChannel(String channelName) {
        return new ConsumingOnlyChannel(channelName, consumingConfiguration());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PublishingOnlyChannel publishOnlyChannel(String channelName) {
        return new PublishingOnlyChannel(channelName, publishingConfiguration());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProcessingChannel processingChannel(String channelName) {
        return new ProcessingChannel(channelName, publishingConfiguration(), consumingConfiguration());
    }

    SpringOutboxConsumingConfiguration consumingConfiguration(){
        var handlerExecutor = new SpringTransactionalHandlerExecutor(this.platformTransactionManager);
        var sp = Objects.requireNonNull(this.serviceProvider,"A ServiceProvider is required!");
        var executionContextDetector = new MirrorBasedExecutionContextDetector(sp);
        var executionContextProcessor = new SimpleExecutionContextProcessor(handlerExecutor);
        var domainEventConsumer = new GeneralDomainEventConsumer(executionContextDetector, executionContextProcessor);
        AbstractOutboxPoller poller = new DirectOutboxPoller(this.transactionalOutbox, domainEventConsumer);
        if(this.poller != null){
            poller = this.poller;
        }
        return new SpringOutboxConsumingConfiguration(poller);
    }

    SpringOutboxPublishingConfiguration publishingConfiguration(){
        return new SpringOutboxPublishingConfiguration(
            new SpringOutboxDomainEventPublisher(
                this.transactionalOutbox,
                this.platformTransactionManager
            )
        );
    }

    /**
     * Retrieves the transactional outbox instance associated with this channel factory.
     *
     * @return the transactional outbox instance
     */
    public TransactionalOutbox getTransactionalOutbox() {
        return transactionalOutbox;
    }
}
