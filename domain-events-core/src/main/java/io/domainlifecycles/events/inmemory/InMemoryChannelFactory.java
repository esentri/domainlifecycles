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

package io.domainlifecycles.events.inmemory;

import io.domainlifecycles.events.api.ChannelFactory;
import io.domainlifecycles.events.api.ConsumingOnlyChannel;
import io.domainlifecycles.events.api.ProcessingChannel;
import io.domainlifecycles.events.api.PublishingOnlyChannel;
import io.domainlifecycles.events.consume.DomainEventConsumer;
import io.domainlifecycles.events.consume.execution.handler.HandlerExecutor;
import io.domainlifecycles.events.consume.execution.handler.ReflectiveHandlerExecutor;
import io.domainlifecycles.events.exception.DLCEventsException;
import io.domainlifecycles.events.publish.DomainEventPublisher;
import io.domainlifecycles.services.api.ServiceProvider;

/**
 * The InMemoryChannelFactory class is an implementation of the ChannelFactory interface that creates channels for in-memory processing of domain events.
 * It provides methods for creating channels processing domain events.
 *
 * @author Mario Herb
 */
public class InMemoryChannelFactory implements ChannelFactory {

    private final ServiceProvider serviceProvider;
    private final int executorThreads;

    /**
     * Depending on the {@code executorThreads} passed, a synchronous or asynchronous channel is created.
     *
     * @param serviceProvider The service provider used to retrieve instances of various types of services (event handlers).
     * @param executorThreads if 0 synchronous handling is activated else asynchronous with the given number of threads
     */
    public InMemoryChannelFactory(ServiceProvider serviceProvider, int executorThreads) {
        this.serviceProvider = serviceProvider;
        this.executorThreads = executorThreads;
    }

    /**
     * Creates an instance of InMemoryChannelFactory using synchronous event handling execution
     *
     * @param serviceProvider The service provider used to retrieve instances of various types of services (event handlers).
     */
    public InMemoryChannelFactory(ServiceProvider serviceProvider){
        this.serviceProvider = serviceProvider;
        this.executorThreads = 0;
    }

    /**
     * Not implemented for the in-memory channel.
     *
     * @param channelName The name of the channel to consume domain events from
     * @return nothing
     */
    @Override
    public ConsumingOnlyChannel consumeOnlyChannel(String channelName) {
        throw DLCEventsException.fail("A 'consumeOnlyChannel' makes no sense for in memory processing! " +
            "Only a 'processingChannel' is supported!");
    }

    /**
     * Not implemented for the in-memory channel.
     *
     * @param channelName The name of the channel to consume domain events from
     * @return nothing
     */
    @Override
    public PublishingOnlyChannel publishOnlyChannel(String channelName) {
        throw DLCEventsException.fail("A 'publishOnlyChannel' makes no sense for in memory processing! " +
            "Only a 'processingChannel' is supported!");
    }

    /**
     * Creates a new ProcessingChannel for processing domain events.
     *
     * @param channelName The name of the channel.
     * @return The created ProcessingChannel instance.
     */
    @Override
    public ProcessingChannel processingChannel(String channelName) {

        var consumingConfiguration = consumingConfiguration();
        var publishingConfiguration = publishingConfiguration(consumingConfiguration.domainEventConsumer());

        return new ProcessingChannel(channelName, publishingConfiguration , consumingConfiguration);
    }

    private InMemoryConsumingConfiguration consumingConfiguration(){
        InMemoryConsumingConfiguration consumingConfiguration;
        if(this.executorThreads == 0){
            consumingConfiguration = new InMemoryConsumingConfigurationFactory().consumeSync(
                this.serviceProvider,
                useHandlerExecutor()
            );
        }else{
            consumingConfiguration = new InMemoryConsumingConfigurationFactory().consumeAsync(
                this.serviceProvider,
                useHandlerExecutor(),
                this.executorThreads
            );
        }
        return consumingConfiguration;
    }

    private InMemoryPublishingConfiguration publishingConfiguration(DomainEventConsumer domainEventConsumer){
        return new InMemoryPublishingConfiguration(
            useDomainEventPublisher(domainEventConsumer)
        );
    }

    /**
     * Creates and returns a DomainEventPublisher using the provided DomainEventConsumer.
     *
     * @param domainEventConsumer The DomainEventConsumer to be associated with the DomainEventPublisher
     * @return A new instance of DomainEventPublisher
     */
    protected DomainEventPublisher useDomainEventPublisher(DomainEventConsumer domainEventConsumer){
        return new InMemoryDomainEventPublisher(domainEventConsumer);
    }

    /**
     * Returns a new instance of ReflectiveHandlerExecutor to be used as the HandlerExecutor.
     *
     * @return a new instance of ReflectiveHandlerExecutor
     */
    protected HandlerExecutor useHandlerExecutor(){
        return new ReflectiveHandlerExecutor();
    }
}
