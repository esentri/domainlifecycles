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

import io.domainlifecycles.domain.types.ServiceKind;
import io.domainlifecycles.events.api.Channel;
import io.domainlifecycles.events.api.ChannelRoutingConfiguration;
import io.domainlifecycles.events.api.DomainEventTypeBasedRouter;
import io.domainlifecycles.events.api.ProcessingChannel;
import io.domainlifecycles.events.api.PublishingChannel;
import io.domainlifecycles.events.api.PublishingRouter;
import io.domainlifecycles.events.consume.execution.handler.TransactionalHandlerExecutor;
import io.domainlifecycles.events.spring.api.SpringTxInMemoryChannelFactory;
import io.domainlifecycles.events.spring.receive.execution.handler.SpringTransactionalHandlerExecutor;
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.services.Services;
import io.domainlifecycles.services.api.ServiceProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

/**
 * Auto-configuration for in-memory domain events with Spring Transactional support.
 *
 * @author Mario Herb
 */
@AutoConfiguration(
    afterName = {
        "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration",
        "org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration"
    },
    after = {
        DlcDomainAutoConfiguration.class,
        DlcBuilderAutoConfiguration.class,
        DlcSpringBusDomainEventsAutoConfiguration.class,
        DlcNoTxInMemoryDomainEventsAutoConfiguration.class
    }
)
public class DlcSpringTxInMemoryDomainEventsAutoConfiguration {

    /**
     * Inner guarded configuration: only loaded if Spring TX is on the classpath
     * and the property dlc.events.springtx.enabled=true
     */
    @AutoConfiguration
    @ConditionalOnClass(name = "org.springframework.transaction.PlatformTransactionManager")
    @ConditionalOnProperty(
        prefix = "dlc.events.springtx",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = false
    )
    static class SpringTxConfiguration {

        /**
         * Configures and creates an instance of {@link SpringTxInMemoryChannelFactory}. This bean is only initialized
         * if no other bean of type {@link SpringTxInMemoryChannelFactory} is present in the Spring context.
         * The factory is configured to enable in-memory channel operations with transactional support.
         *
         * @param transactionManager The {@link PlatformTransactionManager} used for
         *                           managing transactions during in-memory channel operations.
         * @param serviceProvider The {@link ServiceProvider} responsible for providing instances of domain services
         *                        and repositories required for channel operations.
         * @return An instance of {@link SpringTxInMemoryChannelFactory}, configured with transactional supports and
         *         the provided {@link ServiceProvider}.
         */
        @ConditionalOnMissingBean
        @Bean
        public SpringTxInMemoryChannelFactory springTxInMemoryChannelFactory(
            org.springframework.transaction.PlatformTransactionManager transactionManager,
            ServiceProvider serviceProvider
        ) {
            return new SpringTxInMemoryChannelFactory(transactionManager, serviceProvider, true);
        }
        /**
         * Creates and provides an instance of {@link ServiceProvider}. This bean is only initialized if a {@link ServiceProvider}
         * bean is not already present in the Spring context. The method takes a list of {@link ServiceKind} implementations
         * and a {@link DomainMirror} to configure the {@link ServiceProvider}.
         *
         * @param serviceKinds A list of {@link ServiceKind} implementations that represent various service-like types.
         * @param domainMirror An instance of {@link DomainMirror} used for domain reflection-related operations.
         * @return An instance of {@link ServiceProvider}, initialized with the provided {@link ServiceKind} implementations.
         */
        @Bean
        @ConditionalOnMissingBean(ServiceProvider.class)
        public ServiceProvider serviceProvider(
            List<ServiceKind> serviceKinds,
            DomainMirror domainMirror
        ) {
            return new Services(serviceKinds);
        }

        /**
         * Configures and provides an instance of {@link Channel} using the specified
         * {@link SpringTxInMemoryChannelFactory}. This bean is only initialized if no
         * other bean of type {@link Channel} is already present in the Spring application context.
         *
         * @param channelFactory The {@link SpringTxInMemoryChannelFactory} used to create
         *                       the instance of {@link Channel}. The factory is
         *                       responsible for providing a processing channel with
         *                       transactional support.
         * @return An instance of {@link Channel} configured with a default processing channel
         *         created by the specified {@link SpringTxInMemoryChannelFactory}.
         */
        @Bean
        @ConditionalOnMissingBean(Channel.class)
        public ProcessingChannel channelConfigurationWithPlatformTransactionManager(
            SpringTxInMemoryChannelFactory channelFactory
        ) {
            return channelFactory
                .processingChannel("default");
        }

        /**
         * Configures and provides an instance of {@link DomainEventTypeBasedRouter}. This bean is only initialized if a
         * {@link PublishingRouter} bean is not already present in the Spring application context. The router is created
         * using the provided list of {@link PublishingChannel} instances and assigns a default channel for routing.
         *
         * @param publishingChannels A list of {@link PublishingChannel} instances to be used by the router
         *                           for routing domain events based on their type.
         * @return An instance of {@link DomainEventTypeBasedRouter}, initialized with the specified
         *         {@link PublishingChannel} instances and a default channel configuration.
         */
        @Bean
        @ConditionalOnMissingBean(PublishingRouter.class)
        public DomainEventTypeBasedRouter router(
            List<PublishingChannel> publishingChannels
        ) {
            var router = new DomainEventTypeBasedRouter(publishingChannels);
            router.defineDefaultChannel("default");
            return router;
        }

        /**
         * Creates and provides an instance of {@link ChannelRoutingConfiguration}. This bean is only initialized
         * if a {@link ChannelRoutingConfiguration} bean is not already present in the Spring context and a
         * {@link PublishingRouter} bean is available. The configuration is responsible for setting up
         * channel routing for domain event processing.
         *
         * @param publishingRouter The {@link PublishingRouter} implementation to be used for routing domain events
         *                         to the appropriate {@link PublishingChannel}.
         * @return An instance of {@link ChannelRoutingConfiguration} configured with the provided {@link PublishingRouter}.
         */
        @Bean
        @ConditionalOnMissingBean(ChannelRoutingConfiguration.class)
        @ConditionalOnBean(PublishingRouter.class)
        public ChannelRoutingConfiguration channelRoutingConfiguration(
            PublishingRouter publishingRouter
        ) {
            return new ChannelRoutingConfiguration(publishingRouter);
        }

        /**
         * Configures and provides an instance of {@link TransactionalHandlerExecutor}.
         * This bean is only initialized if a {@link TransactionalHandlerExecutor}
         * bean is not already present in the Spring context. It utilizes the provided
         * {@link PlatformTransactionManager} to enable
         * transactional execution of handlers.
         *
         * @param transactionManager The {@link PlatformTransactionManager}
         *                           used to manage transactions within the handler execution
         *                           process.
         * @return An instance of {@link TransactionalHandlerExecutor} configured with the
         *         supplied {@link PlatformTransactionManager}.
         */
        @Bean
        @ConditionalOnMissingBean(TransactionalHandlerExecutor.class)
        public TransactionalHandlerExecutor transactionalHandlerExecutor(
            org.springframework.transaction.PlatformTransactionManager transactionManager
        ) {
            return new SpringTransactionalHandlerExecutor(transactionManager);
        }
    }
}
