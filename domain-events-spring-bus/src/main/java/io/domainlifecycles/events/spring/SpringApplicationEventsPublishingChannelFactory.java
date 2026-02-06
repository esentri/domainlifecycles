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
 *  Copyright 2019-2026 the original author or authors.
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

package io.domainlifecycles.events.spring;

import io.domainlifecycles.events.api.ChannelFactory;
import io.domainlifecycles.events.api.ConsumingOnlyChannel;
import io.domainlifecycles.events.api.ProcessingChannel;
import io.domainlifecycles.events.api.PublishingOnlyChannel;
import io.domainlifecycles.events.exception.DLCEventsException;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Objects;

/**
 * A factory for creating channels that support publishing domain events
 * using the Spring application event mechanism.
 *
 * This implementation of {@code ChannelFactory} restricts channel creation
 * to only support publishing operations. Attempts to create channels for
 * consuming or processing domain events will result in exceptions.
 *
 * This allows DomainEvents being published using DLC's {@code DomainEvents.publih(...)}.
 *
 * @author Mario Herb
 */
public class SpringApplicationEventsPublishingChannelFactory implements ChannelFactory {

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * Constructs an instance of SpringApplicationEventsPublishingChannelFactory.
     *
     * This factory is designed to create channels specifically for publishing domain events
     * using the Spring ApplicationEventPublisher mechanism. It does not support the creation
     * of consuming or processing channels.
     *
     * @param applicationEventPublisher the ApplicationEventPublisher used to publish domain
     *                                  events; must not be null
     * @throws NullPointerException if applicationEventPublisher is null
     */
    public SpringApplicationEventsPublishingChannelFactory(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = Objects.requireNonNull(applicationEventPublisher, "An ApplicationEventPublisher is required!");
    }

    /**
     * Creates a channel for consuming domain events. This implementation does not support
     * consuming channel creation and will throw an exception if invoked.
     *
     * @param channelName the name of the channel to be created; must not be null
     * @return this method does not return a value as it always throws an exception
     * @throws DLCEventsException always thrown to indicate that consuming channels are not supported
     */
    @Override
    public ConsumingOnlyChannel consumeOnlyChannel(String channelName) {
        throw DLCEventsException.fail("A 'consumeOnlyChannel' makes no sense for in SpringApplicationEventPublisher processing! " +
            "Only a 'publishOnlyChannel' is supported!");
    }

    /**
     * Creates and returns a {@link PublishingOnlyChannel} configured for publishing domain events
     * via Spring's application event mechanism. The created channel supports only publishing operations.
     * DomainEvent routing within this channel is handled by Spring's application event mechanism.
     *
     * @param channelName the name of the channel to be created; must not be null
     * @return a {@link PublishingOnlyChannel} configured with the given channel name
     * @throws NullPointerException if channelName is null
     */
    @Override
    public PublishingOnlyChannel publishOnlyChannel(String channelName) {
        var conf = new SpringApplicationEventsPublishingConfiguration(
            new SpringApplicationEventsDomainEventPublisher(applicationEventPublisher)
        );
        return new PublishingOnlyChannel(channelName, conf);
    }

    /**
     * Creates a {@link ProcessingChannel} for publishing and consuming domain events.
     * This implementation does not support processing channels and throws an exception if invoked.
     *
     * @param channelName the name of the channel to be created; must not be null
     * @return this method does not return a value as it always throws an exception
     * @throws DLCEventsException always thrown to indicate that ProcessingChannel creation is not supported
     */
    @Override
    public ProcessingChannel processingChannel(String channelName) {
        throw DLCEventsException.fail("A 'processingChannel' makes no sense for in SpringApplicationEventPublisher processing! " +
            "Only a 'publishOnlyChannel' is supported!");
    }
}
