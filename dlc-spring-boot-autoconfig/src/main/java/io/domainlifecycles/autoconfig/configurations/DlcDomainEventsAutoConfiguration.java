package io.domainlifecycles.autoconfig.configurations;

import io.domainlifecycles.domain.types.ServiceKind;
import io.domainlifecycles.events.api.ChannelRoutingConfiguration;
import io.domainlifecycles.events.api.DomainEventTypeBasedRouter;
import io.domainlifecycles.events.api.PublishingChannel;
import io.domainlifecycles.events.api.PublishingRouter;
import io.domainlifecycles.events.inmemory.InMemoryChannelFactory;
import io.domainlifecycles.events.spring.api.SpringTxInMemoryChannelFactory;
import io.domainlifecycles.services.Services;
import io.domainlifecycles.services.api.ServiceProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@AutoConfiguration
public class DlcDomainEventsAutoConfiguration {

    /**
     * This method creates and configures a ServiceProvider instance, which is responsible for providing instances of
     * various types of services.
     * It takes three parameters: repositories, applicationServices, and domainServices, which are lists of
     * Repository, ApplicationService, and DomainService instances respectively
     */
    @ConditionalOnMissingBean(ServiceProvider.class)
    @Bean
    public ServiceProvider serviceProvider(List<ServiceKind> serviceKinds){
        return  new Services(serviceKinds);
    }

    @Bean
    @ConditionalOnMissingBean(PublishingRouter.class)
    public DomainEventTypeBasedRouter router(List<PublishingChannel> publishingChannels){
        var router = new DomainEventTypeBasedRouter(publishingChannels);
        router.defineDefaultChannel("default");
        return router;
    }
    @Bean
    @ConditionalOnMissingBean(ChannelRoutingConfiguration.class)
    @ConditionalOnBean(PublishingRouter.class)
    public ChannelRoutingConfiguration channelConfiguration(PublishingRouter publishingRouter){
        return new ChannelRoutingConfiguration(publishingRouter);
    }

    @Bean
    @ConditionalOnBean(PlatformTransactionManager.class)
    @ConditionalOnMissingBean(ChannelRoutingConfiguration.class)
    public ChannelRoutingConfiguration channelConfiguration(
        PlatformTransactionManager platformTransactionManager,
        ServiceProvider serviceProvider,
        PublishingRouter router
    ){
        var channel = new SpringTxInMemoryChannelFactory(platformTransactionManager, serviceProvider, true).processingChannel("default");
        return new ChannelRoutingConfiguration(router);
    }

    @Bean
    @ConditionalOnMissingBean({PlatformTransactionManager.class, ChannelRoutingConfiguration.class})
    public ChannelRoutingConfiguration channelConfiguration(ServiceProvider serviceProvider){
        var channel = new InMemoryChannelFactory(serviceProvider).processingChannel("default");
        var router = new DomainEventTypeBasedRouter(List.of(channel));
        router.defineDefaultChannel("default");
        return new ChannelRoutingConfiguration(router);
    }
}
