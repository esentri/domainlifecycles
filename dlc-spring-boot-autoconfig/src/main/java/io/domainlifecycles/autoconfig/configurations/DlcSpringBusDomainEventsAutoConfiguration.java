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

package io.domainlifecycles.autoconfig.configurations;

import io.domainlifecycles.events.api.Channel;
import io.domainlifecycles.events.api.ChannelRoutingConfiguration;
import io.domainlifecycles.events.api.DomainEventTypeBasedRouter;
import io.domainlifecycles.events.api.PublishingChannel;
import io.domainlifecycles.events.api.PublishingRouter;
import io.domainlifecycles.events.spring.SpringApplicationEventsPublishingChannelFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * Auto-configuration for integrating domain events with Spring's event bus.
 * This configuration is enabled automatically when the `dlc.events.springbus.enabled` property
 * is set to `true` or is missing. It ensures that domain events can be routed and published
 * using Spring's application event system.
 *
 * This configuration loads after `DlcDomainAutoConfiguration` to ensure that
 * the core domain configuration is initialized before event handling is set up.
 *
 * The configuration provides beans for creating publishing channels, routing domain events,
 * and defining default channels for event publication. It is designed to be extensible
 * and only loads beans if the necessary types are not already present in the application context.
 *
 * @author Mario Herb
 */
@AutoConfiguration(
    after = {
        DlcDomainAutoConfiguration.class
    }
)
@ConditionalOnProperty(
    prefix = "dlc.events.springbus",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true
)
public class DlcSpringBusDomainEventsAutoConfiguration {

    /**
     * Creates a factory for publishing application events.
     *
     * @param applicationEventPublisher Application event publisher
     * @return SpringApplicationEventsPublishingChannelFactory
     */
    @Bean
    @ConditionalOnMissingBean
    public SpringApplicationEventsPublishingChannelFactory springApplicationEventsPublishingChannelFactory(
        ApplicationEventPublisher applicationEventPublisher
    ){
        return new SpringApplicationEventsPublishingChannelFactory(applicationEventPublisher);
    }
    /**
     * Creates a router for domain events based on their types.
     *
     * @param publishingChannels List of available publishing channels
     * @return A configured DomainEventTypeBasedRouter
     */
    @Bean
    @ConditionalOnMissingBean(PublishingRouter.class)
    public DomainEventTypeBasedRouter router(List<PublishingChannel> publishingChannels){
        var router = new DomainEventTypeBasedRouter(publishingChannels);
        router.defineDefaultChannel("default");
        return router;
    }

    /**
     * Configures channel routing when a PublishingRouter is available.
     *
     * @param publishingRouter The router to use for channel configuration
     * @return A new ChannelRoutingConfiguration instance
     */
    @Bean
    @ConditionalOnMissingBean(ChannelRoutingConfiguration.class)
    @ConditionalOnBean(PublishingRouter.class)
    public ChannelRoutingConfiguration channelRoutingConfiguration(PublishingRouter publishingRouter){
        return new ChannelRoutingConfiguration(publishingRouter);
    }

    /**
     * Configures a default channel for publishing events.
     *
     * @param channelFactory The factory to create the channel
     * @return A default publishing channel
     */
    @Bean
    @ConditionalOnMissingBean(Channel.class)
    public PublishingChannel channel(SpringApplicationEventsPublishingChannelFactory channelFactory){
        return channelFactory.publishOnlyChannel("default");
    }

}
