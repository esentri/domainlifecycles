package io.domainlifecycles.events.jakartajmsspringtxseparate;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.domainlifecycles.access.classes.ClassProvider;
import io.domainlifecycles.access.classes.DefaultClassProvider;
import io.domainlifecycles.events.api.ChannelRoutingConfiguration;
import io.domainlifecycles.events.api.DomainEventTypeBasedRouter;
import io.domainlifecycles.events.api.PublishingChannel;
import io.domainlifecycles.events.consume.execution.handler.TransactionalHandlerExecutor;
import io.domainlifecycles.events.jakarta.jms.api.SpringtransactionJakartaJmsChannelFactory;
import io.domainlifecycles.events.mq.api.AbstractMqConsumingChannel;
import io.domainlifecycles.events.mq.api.AbstractMqPublishingChannel;
import io.domainlifecycles.events.spring.receive.execution.handler.SpringTransactionalHandlerExecutor;
import io.domainlifecycles.services.api.ServiceProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@Slf4j
@EnableJms
public class SpringJmsConfigSeparate {

    @Bean
    @DependsOn("initializedDomain")
    public SpringtransactionJakartaJmsChannelFactory springtransactionJakartaJmsChannelFactory(
        ServiceProvider serviceProvider,
        ClassProvider classProvider,
        TransactionalHandlerExecutor transactionalHandlerExecutor,
        ActiveMQConnectionFactory jmsConnectionFactory,
        ObjectMapper objectMapper
    ){
        return new SpringtransactionJakartaJmsChannelFactory(
            jmsConnectionFactory,
            serviceProvider,
            classProvider,
            transactionalHandlerExecutor,
            objectMapper
        );
    }

    @Bean(destroyMethod = "close")
    public AbstractMqPublishingChannel channelPub(SpringtransactionJakartaJmsChannelFactory factory){
        return factory.publishOnlyChannel("jms2Pub");
    }

    @Bean(destroyMethod = "close")
    public AbstractMqConsumingChannel channelCons(SpringtransactionJakartaJmsChannelFactory factory){
        return factory.consumeOnlyChannel("jms2Cons");
    }

    @Bean
    public ChannelRoutingConfiguration channelConfiguration(List<PublishingChannel> publishingChannels){
        var router = new DomainEventTypeBasedRouter(publishingChannels);
        router.defineDefaultChannel("jms2Pub");
        return new ChannelRoutingConfiguration(router);
    }

    @Bean
    public TransactionalHandlerExecutor transactionalHandlerExecutor(PlatformTransactionManager platformTransactionManager){
        return new SpringTransactionalHandlerExecutor(platformTransactionManager);
    }

    @Bean
    public ClassProvider classProvider(){
        return new DefaultClassProvider();
    }

}
