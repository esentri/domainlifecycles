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

import com.gruelbox.transactionoutbox.TransactionOutbox;
import com.gruelbox.transactionoutbox.spring.SpringTransactionManager;
import io.domainlifecycles.domain.types.ServiceKind;
import io.domainlifecycles.events.api.ChannelRoutingConfiguration;
import io.domainlifecycles.events.api.DomainEventTypeBasedRouter;
import io.domainlifecycles.events.api.PublishingChannel;
import io.domainlifecycles.events.api.PublishingRouter;
import io.domainlifecycles.events.consume.execution.handler.TransactionalHandlerExecutor;
import io.domainlifecycles.events.gruelbox.api.DomainEventsInstantiator;
import io.domainlifecycles.events.gruelbox.api.GruelboxChannelFactory;
import io.domainlifecycles.events.gruelbox.api.GruelboxProcessingChannel;
import io.domainlifecycles.events.gruelbox.api.PollerConfiguration;
import io.domainlifecycles.events.gruelbox.api.PublishingSchedulerConfiguration;
import io.domainlifecycles.events.gruelbox.idempotent.IdempotencyConfiguration;
import io.domainlifecycles.events.spring.receive.execution.handler.SpringTransactionalHandlerExecutor;
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.services.Services;
import io.domainlifecycles.services.api.ServiceProvider;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

/**
 * Autoconfiguration class for integrating Gruelbox Transaction Outbox with DLC domain events.
 * This configuration enables seamless processing of domain events using the Gruelbox transaction outbox
 * and sets up the necessary beans for instantiation and transaction management.
 *
 * The configuration activates after DlcDomainEventsAutoConfiguration and requires the presence
 * of com.gruelbox.transactionoutbox.TransactionOutbox class on the classpath.
 *
 * It also enables scheduling to facilitate periodic polling of the transaction outbox for processing.
 *
 * @author Mario Herb
 */
@AutoConfiguration(
    afterName = {
        "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration",
        "org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration",
    },
    after = {
        DlcDomainAutoConfiguration.class,
        DlcBuilderAutoConfiguration.class,
        DlcSpringTxInMemoryDomainEventsAutoConfiguration.class,
        DlcNoTxInMemoryDomainEventsAutoConfiguration.class,
        DlcSpringBusDomainEventsAutoConfiguration.class
    }
)
@ConditionalOnProperty(
    prefix = "dlc.events.gruelbox",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = false
)
public class DlcGruelboxDomainEventsAutoConfiguration {

    /**
     * The {@code GruelboxConfiguration} class provides bean definitions and configuration
     * for the integration of Gruelbox's transaction outbox library with a Spring application
     * context. This class ensures proper configuration and management of components related
     * to domain events, transactional handlers, publishing channels, and event routing.
     *
     * This configuration class is conditionally loaded based on the availability of the
     * {@code com.gruelbox.transactionoutbox.TransactionOutbox} class in the classpath.
     * It also leverages several conditional annotations to ensure the beans are only created
     * when required and not already present in the Spring context.
     *
     * The configuration is responsible for:
     * - Defining service providers and connecting domain services.
     * - Configuring transactional handling components for proper transaction management.
     * - Setting up routing of domain events to appropriate publishing channels.
     * - Creating processing channels to handle domain events.
     * - Managing optional configurations for idempotency, polling, and event publishing.
     */
    @Import(SpringTransactionManager.class)
    @EnableScheduling
    @ConditionalOnClass(name={"com.gruelbox.transactionoutbox.TransactionOutbox", "org.springframework.transaction.PlatformTransactionManager"})
    @AutoConfiguration
    static class GruelboxConfiguration {
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
        public ServiceProvider serviceProvider(List<ServiceKind> serviceKinds, DomainMirror domainMirror) {
            return new Services(serviceKinds);
        }

        /**
         * Creates and provides an instance of {@link GruelboxChannelFactory}. This bean is only initialized
         * if a {@link GruelboxChannelFactory} bean is not already present in the Spring context and
         * there is a single candidate for {@link TransactionOutbox}.
         *
         * @param serviceProvider The {@link ServiceProvider} instance responsible for providing services.
         * @param transactionOutbox The {@link TransactionOutbox} used for transaction management and event outboxing.
         * @param transactionalHandlerExecutor The {@link TransactionalHandlerExecutor} used to execute transactional handlers.
         * @param domainEventsInstantiator The {@link DomainEventsInstantiator} responsible for instantiating domain events.
         * @param idempotencyConfiguration Optional {@link IdempotencyConfiguration} for configuring idempotency behavior.
         * @param pollerConfiguration Optional {@link PollerConfiguration} for configuring polling behavior in the outbox.
         * @param publishingSchedulerConfiguration Optional {@link PublishingSchedulerConfiguration} for configuring
         *                                         publishing scheduling behavior.
         * @return An instance of {@link GruelboxChannelFactory} configured with the provided dependencies and configurations.
         */
        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnSingleCandidate(TransactionOutbox.class)
        public GruelboxChannelFactory gruelboxChannelFactory(
            ServiceProvider serviceProvider,
            TransactionOutbox transactionOutbox,
            TransactionalHandlerExecutor transactionalHandlerExecutor,
            DomainEventsInstantiator domainEventsInstantiator,
            @Nullable IdempotencyConfiguration idempotencyConfiguration,
            @Nullable PollerConfiguration pollerConfiguration,
            @Nullable PublishingSchedulerConfiguration publishingSchedulerConfiguration
        ) {
            return new GruelboxChannelFactory(
                serviceProvider,
                transactionOutbox,
                transactionalHandlerExecutor,
                domainEventsInstantiator,
                idempotencyConfiguration,
                pollerConfiguration,
                publishingSchedulerConfiguration
            );
        }

        /**
         * Creates and configures an instance of {@link GruelboxProcessingChannel} using the provided
         * {@link GruelboxChannelFactory}. The channel is used for processing domain events with the
         * default configuration.
         *
         * @param gruelboxChannelFactory The {@link GruelboxChannelFactory} used to create and configure
         *                               the {@link GruelboxProcessingChannel} instance.
         * @return An instance of {@link GruelboxProcessingChannel} configured with the default settings.
         */
        @Bean(destroyMethod = "close")
        @ConditionalOnBean(GruelboxChannelFactory.class)
        public GruelboxProcessingChannel gruelboxChannel(
            GruelboxChannelFactory gruelboxChannelFactory
        ) {
            return gruelboxChannelFactory.processingChannel("default");
        }

        /**
         * Creates and configures a {@link DomainEventTypeBasedRouter} bean if no other {@link PublishingRouter} bean is defined
         * in the Spring application context. The {@link DomainEventTypeBasedRouter} is responsible for routing domain events
         * to the appropriate publishing channels based on their event type.
         *
         * @param publishingChannels A list of {@link PublishingChannel} instances to be used for routing domain events.
         *                           Each channel represents a target destination for published domain events.
         * @return An instance of {@link DomainEventTypeBasedRouter} configured with the provided publishing channels,
         * with a default channel named "default" defined.
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
         * Creates and provides an instance of {@link DomainEventsInstantiator}.
         * This bean is only initialized if a {@link DomainEventsInstantiator} bean
         * is not already present in the Spring context.
         *
         * @return An instance of {@link DomainEventsInstantiator} used for instantiating
         * and managing domain event processing components within the Gruelbox
         * event processing framework.
         */
        @ConditionalOnMissingBean
        @Bean
        public DomainEventsInstantiator domainEventsInstantiator() {
            return new DomainEventsInstantiator();
        }

        /**
         * Creates and provides an instance of {@link TransactionalHandlerExecutor}.
         * This bean is only initialized if a {@link TransactionalHandlerExecutor} bean
         * is not already present in the Spring context and there is a single candidate
         * for {@link PlatformTransactionManager}.
         * <p>
         * The created {@link SpringTransactionalHandlerExecutor} uses the provided
         * {@link PlatformTransactionManager} to manage transactional behavior.
         *
         * @param platformTransactionManager The {@link PlatformTransactionManager} used
         *                                   for managing transactions.
         * @return An instance of {@link TransactionalHandlerExecutor}, specifically
         * {@link SpringTransactionalHandlerExecutor}, configured with the provided
         * {@link PlatformTransactionManager}.
         */
        @Bean
        @ConditionalOnMissingBean(TransactionalHandlerExecutor.class)
        public TransactionalHandlerExecutor transactionalHandlerExecutor(PlatformTransactionManager platformTransactionManager) {
            return new SpringTransactionalHandlerExecutor(platformTransactionManager);
        }

    }

}
