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

package io.domainlifecycles.events.api;

import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.exception.DLCEventsException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@code DomainEventTypeBasedRouter} class is an implementation of the {@link PublishingRouter} interface
 * that routes domain events to the appropriate channel based on their event type.
 *
 * @author Mario Herb
 */
public class DomainEventTypeBasedRouter implements PublishingRouter{

    private final Map<String, PublishingChannel> channelMap = new HashMap<>();
    private final Map<String , String> eventTypeNameToChannelName = new HashMap<>();
    private String defaultChannelName;

    /**
     * The {@code DomainEventTypeBasedRouter} class is an implementation of the {@link PublishingRouter} interface
     * that routes domain events to the appropriate channel based on their event type.
     *
     * @param channels the channel for which routing is applied
     */
    public DomainEventTypeBasedRouter(List<PublishingChannel> channels) {
        channels.forEach(c -> channelMap.put(c.getName(), c));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PublishingChannel route(DomainEvent domainEvent) {
        var routedChannelName = eventTypeNameToChannelName.get(domainEvent.getClass().getName());
        if(routedChannelName != null){
            return channelMap.get(routedChannelName);
        }else{
            if(defaultChannelName != null){
                return channelMap.get(defaultChannelName);
            }
        }
        throw DLCEventsException.fail("No default channel defined and no routing configured for DomainEvent '%s'", domainEvent);
    }

    /**
     * Defines an explicit route for a specific domain event type to a channel.
     *
     * @param domainEventType The class representing the domain event type.
     * @param channelName The name of the channel to route the domain event to.
     */
    public void defineExplicitRoute(Class<? extends DomainEvent> domainEventType, String channelName){
        eventTypeNameToChannelName.put(domainEventType.getName(), channelName);
    }

    /**
     * Defines the default channel for routing domain events.
     *
     * @param channelName The name of the default channel.
     *                    The specified channel will be used when no explicit route is defined for a domain event type.
     */
    public void defineDefaultChannel(String channelName){
        this.defaultChannelName = channelName;
    }

    /**
     * Retrieves the default channel name for routing domain events.
     *
     * @return The name of the default channel.
     */
    public String getDefaultChannelName() {
        return defaultChannelName;
    }

    /**
     * Returns the routed channel name for the given domain event type.
     *
     * @param domainEventType The class representing the domain event type.
     * @return The name of the channel to which the domain event will be routed.
     */
    public String getRoutedChannelName(Class<? extends DomainEvent> domainEventType){
        return eventTypeNameToChannelName.get(domainEventType.getName());
    }
}
