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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents a utility for publishing domain events.
 * It could be used to publish {@link DomainEvent} instances directly from Domain Services or Aggregates
 * and hides all the infrastructure and pure technical management of domain event publishing.
 *
 * @author Mario Herb
 */
public class DomainEvents {

    private static final Logger log = LoggerFactory.getLogger(DomainEvents.class);

    private static ChannelRoutingConfiguration channelConfiguration;

    protected static void registerChannelConfiguration(ChannelRoutingConfiguration channelConfig){
        channelConfiguration = channelConfig;
    }

    /**
     * Publishes a domain event using the configured channels.
     * The method throws a DLCEventsException, if the ChannelRoutingConfiguration is not initialized.
     *
     * @param domainEvent the domain event to be published
     * @throws DLCEventsException if the configuration is not initialized
     */
    public static void publish(DomainEvent domainEvent) {
        log.info("DomainEvent provided to be published {}", domainEvent);
        if(channelConfiguration == null){
            throw DLCEventsException.fail("No ChannelRoutingConfiguration initialized!");
        }
        channelConfiguration.passToChannel(domainEvent);
    }

}
