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
import io.domainlifecycles.events.api.Channel;
import io.domainlifecycles.events.api.ChannelRoutingConfiguration;
import io.domainlifecycles.events.api.DomainEventTypeBasedRouter;
import io.domainlifecycles.events.api.PublishingChannel;
import io.domainlifecycles.events.api.PublishingRouter;
import io.domainlifecycles.events.consume.execution.handler.TransactionalHandlerExecutor;
import io.domainlifecycles.events.inmemory.InMemoryChannelFactory;
import io.domainlifecycles.events.spring.api.SpringTxInMemoryChannelFactory;
import io.domainlifecycles.events.spring.receive.execution.handler.SpringTransactionalHandlerExecutor;
import io.domainlifecycles.services.Services;
import io.domainlifecycles.services.api.ServiceProvider;
import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

@AutoConfiguration
@AutoConfigureAfter(DlcDomainAutoConfiguration.class)
public class DlcDomainEventsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ServiceProvider serviceProvider(List<ServiceKind> serviceKinds){
        return new Services(serviceKinds);
    }

    @Bean
    @ConditionalOnBean(PlatformTransactionManager.class)
    @ConditionalOnMissingBean
    public TransactionalHandlerExecutor transactionalHandlerExecutor(PlatformTransactionManager platformTransactionManager){
        return new SpringTransactionalHandlerExecutor(platformTransactionManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public ClassProvider classProvider(){
        return new DefaultClassProvider();
    }

    @Bean
    public DomainEventTypeBasedRouter router(List<PublishingChannel> publishingChannels){
        var router = new DomainEventTypeBasedRouter(publishingChannels);
        router.defineDefaultChannel("default");
        return router;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(PublishingRouter.class)
    public ChannelRoutingConfiguration channelConfiguration(PublishingRouter publishingRouter){
        return new ChannelRoutingConfiguration(publishingRouter);
    }

    @Bean
    @ConditionalOnBean(PlatformTransactionManager.class)
    @ConditionalOnMissingBean
    public PublishingChannel channelConfigurationWithPlatformTransactionManager(
        PlatformTransactionManager platformTransactionManager,
        ServiceProvider serviceProvider
    ){
        return new SpringTxInMemoryChannelFactory(platformTransactionManager, serviceProvider, true).processingChannel("default");
    }

    @Bean
    @ConditionalOnMissingBean({PlatformTransactionManager.class, Channel.class})
    public PublishingChannel channelConfigurationWithoutPlatformTransactionManager(
        ServiceProvider serviceProvider){
        return new InMemoryChannelFactory(serviceProvider).processingChannel("default");
    }
}
