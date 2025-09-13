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


import io.domainlifecycles.access.classes.ClassProvider;
import io.domainlifecycles.access.classes.DefaultClassProvider;
import io.domainlifecycles.domain.types.ServiceKind;
import io.domainlifecycles.events.api.ChannelRoutingConfiguration;
import io.domainlifecycles.events.api.DomainEventTypeBasedRouter;
import io.domainlifecycles.events.api.PublishingChannel;
import io.domainlifecycles.events.api.PublishingRouter;
import io.domainlifecycles.events.consume.execution.handler.TransactionalHandlerExecutor;
import io.domainlifecycles.events.inmemory.InMemoryChannelFactory;
import io.domainlifecycles.events.spring.api.SpringTxInMemoryChannelFactory;
import io.domainlifecycles.events.spring.receive.execution.handler.SpringTransactionalHandlerExecutor;
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.services.Services;
import io.domainlifecycles.services.api.ServiceProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;

import java.util.List;

/**
 * Autoconfiguration class for Domain Events functionality in the DLC framework.
 * This configuration sets up the necessary beans for event handling, routing, and processing.
 * It provides both transactional and non-transactional event processing capabilities.
 *
 *  @author Mario Herb
 *  @author Leon Völlinger
 */
@AutoConfiguration(after = {
    DlcDomainAutoConfiguration.class,
    DataSourceAutoConfiguration.class,
    DataSourceTransactionManagerAutoConfiguration.class,
    TransactionAutoConfiguration.class
})
public class DlcDomainEventsAutoConfiguration {

    /**
     * Creates a ServiceProvider bean for managing service kinds.
     *
     * @param serviceKinds List of service kinds to be managed
     * @return A new ServiceProvider instance
     */
    @Bean
    @ConditionalOnMissingBean(ServiceProvider.class)
    @ConditionalOnBean(DomainMirror.class)
    public ServiceProvider serviceProvider(List<ServiceKind> serviceKinds, DomainMirror domainMirror){
        return new Services(serviceKinds);
    }

    /**
     * Creates a TransactionalHandlerExecutor for managing transactional event handling.
     *
     * @param platformTransactionManager The Spring transaction manager to use
     * @return A new TransactionalHandlerExecutor instance
     */
    @Bean
    @ConditionalOnClass(TransactionManager.class)
    @ConditionalOnBean(PlatformTransactionManager.class)
    @ConditionalOnMissingBean(TransactionalHandlerExecutor.class)
    public TransactionalHandlerExecutor transactionalHandlerExecutor(PlatformTransactionManager platformTransactionManager){
        return new SpringTransactionalHandlerExecutor(platformTransactionManager);
    }

    /**
     * Provides a ClassProvider bean for class resolution and loading.
     *
     * @return A new ClassProvider instance
     */
    @Bean
    @ConditionalOnMissingBean(ClassProvider.class)
    public ClassProvider classProvider(){
        return new DefaultClassProvider();
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
    public ChannelRoutingConfiguration channelConfiguration(PublishingRouter publishingRouter){
        return new ChannelRoutingConfiguration(publishingRouter);
    }

    /**
     * Creates a transactional publishing channel when a PlatformTransactionManager is available.
     *
     * @param platformTransactionManager The transaction manager to use
     * @param serviceProvider The service provider for channel configuration
     * @return A new transactional PublishingChannel
     */
    @Bean
    @ConditionalOnClass(PlatformTransactionManager.class)
    @ConditionalOnBean(PlatformTransactionManager.class)
    public PublishingChannel channelConfigurationWithPlatformTransactionManager(
        PlatformTransactionManager platformTransactionManager, ServiceProvider serviceProvider) {
        return new SpringTxInMemoryChannelFactory(platformTransactionManager, serviceProvider, true).processingChannel("default");
    }

    /**
     * Creates a non-transactional publishing channel when no PlatformTransactionManager is available.
     *
     * @param serviceProvider The service provider for channel configuration
     * @return A new non-transactional PublishingChannel
     */
    @Bean
    @ConditionalOnMissingBean({PublishingChannel.class, PlatformTransactionManager.class})
    public PublishingChannel channelConfigurationWithoutPlatformTransactionManager(
        ServiceProvider serviceProvider){
        return new InMemoryChannelFactory(serviceProvider).processingChannel("default");
    }

}
