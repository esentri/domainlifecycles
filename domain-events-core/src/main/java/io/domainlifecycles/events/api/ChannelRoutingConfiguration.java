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

import io.domainlifecycles.domain.types.DomainEvent;

import java.util.Objects;

/**
 * The ChannelRoutingConfiguration class represents the configuration for all {@link PublishingChannel}s  in the system.
 * It is responsible for routing domain events to the appropriate channel when they are published.
 *
 * @author Mario Herb
 */
public final class ChannelRoutingConfiguration {

    private final PublishingRouter publishingRouter;

    /**
     * This configuration uses a {@link PublishingRouter} instance and wires itself
     * to the central {@link DomainEvents} publishing interface
     *
     * @param publishingRouter the PublishingRouter implementation to be used
     */
    public ChannelRoutingConfiguration(PublishingRouter publishingRouter) {
        this.publishingRouter = Objects.requireNonNull(publishingRouter, "A PublishingRouter is required!");
        DomainEvents.registerChannelConfiguration(this);
    }

    /**
     * Passes a domain event to the appropriate channel for publishing.
     *
     * @param domainEvent The domain event to be passed to the channel.
     */
    void passToChannel(DomainEvent domainEvent) {
        var channel = publishingRouter.route(domainEvent);
        channel.getPublishingConfiguration().domainEventPublisher().publish(domainEvent);
    }
}
