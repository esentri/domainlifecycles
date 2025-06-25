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
    //@ConditionalOnBean(PublishingChannel.class)
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
    //@ConditionalOnBean(PlatformTransactionManager.class)
    //@ConditionalOnMissingBean
    public PublishingChannel channelConfigurationWithPlatformTransactionManager(
        PlatformTransactionManager platformTransactionManager,
        ServiceProvider serviceProvider
    ){
        return new SpringTxInMemoryChannelFactory(platformTransactionManager, serviceProvider, true).processingChannel("default");
    }

    @Bean
    //@ConditionalOnMissingBean({PlatformTransactionManager.class, Channel.class})
    public PublishingChannel channelConfigurationWithoutPlatformTransactionManager(
        ServiceProvider serviceProvider){
        return new InMemoryChannelFactory(serviceProvider).processingChannel("default");
    }
}
