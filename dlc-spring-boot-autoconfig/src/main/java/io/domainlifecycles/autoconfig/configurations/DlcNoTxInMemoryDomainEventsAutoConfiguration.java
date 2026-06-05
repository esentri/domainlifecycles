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

package io.domainlifecycles.autoconfig.configurations;

import io.domainlifecycles.domain.types.ServiceKind;
import io.domainlifecycles.events.api.ChannelRoutingConfiguration;
import io.domainlifecycles.events.api.DomainEventTypeBasedRouter;
import io.domainlifecycles.events.api.ProcessingChannel;
import io.domainlifecycles.events.api.PublishingChannel;
import io.domainlifecycles.events.api.PublishingRouter;
import io.domainlifecycles.events.inmemory.InMemoryChannelFactory;
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.services.Services;
import io.domainlifecycles.services.api.ServiceProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * Autoconfiguration class for enabling in-memory domain event processing in the DLC framework
 * without transactional support. This configuration is activated only when the property
 * `dlc.events.inmemory.enabled` is set to `true`.
 *
 * @author Mario Herb
 */
@AutoConfiguration(
    after = {
        DlcDomainAutoConfiguration.class,
        DlcBuilderAutoConfiguration.class,
        DlcSpringBusDomainEventsAutoConfiguration.class,
    }
)
@ConditionalOnProperty(prefix = "dlc.features.events.inmemory", name = "enabled", havingValue = "true", matchIfMissing = false)
public class DlcNoTxInMemoryDomainEventsAutoConfiguration {

    /**
     * Defines a Bean for creating an instance of {@link InMemoryChannelFactory}
     * when no other bean of its type is available in the application context.
     * The factory facilitates the creation of in-memory channels for processing domain events.
     *
     * @param serviceProvider The service provider used to retrieve instances of various
     *                        types of services, such as event handlers.
     * @return A new instance of {@link InMemoryChannelFactory}, configured with the provided
     *         {@link ServiceProvider}.
     */
    @Bean
    @ConditionalOnMissingBean
    @Primary
    public InMemoryChannelFactory inMemoryChannelFactory(
        ServiceProvider serviceProvider){
        return new InMemoryChannelFactory(serviceProvider);
    }

    /**
     * Creates a ServiceProvider bean for managing service kinds.
     *
     * @param serviceKinds List of service kinds to be managed
     * @param domainMirror the current Domain Mirror bean
     * @return A new ServiceProvider instance
     */
    @Bean
    @ConditionalOnMissingBean(ServiceProvider.class)
    @ConditionalOnBean(DomainMirror.class)
    public ServiceProvider serviceProvider(List<ServiceKind> serviceKinds, DomainMirror domainMirror){
        return new Services(serviceKinds);
    }

    /**
     * Configures and provides a {@link ProcessingChannel} instance for in-memory processing of domain events.
     * This method ensures the creation of a default processing channel when no transaction-based channel configuration is required.
     *
     * @param channelFactory The factory responsible for creating in-memory channels for domain event processing.
     * @return A new instance of {@link ProcessingChannel} with a default configuration.
     */
    @Bean
    @ConditionalOnBean(InMemoryChannelFactory.class)
    @ConditionalOnMissingBean
    public ProcessingChannel channelNoTx(
        InMemoryChannelFactory channelFactory){
        return channelFactory.processingChannel("default");
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
}
