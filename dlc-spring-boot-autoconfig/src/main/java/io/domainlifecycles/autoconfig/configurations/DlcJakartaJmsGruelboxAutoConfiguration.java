package io.domainlifecycles.autoconfig.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gruelbox.transactionoutbox.TransactionOutbox;
import io.domainlifecycles.access.classes.ClassProvider;
import io.domainlifecycles.events.api.ChannelRoutingConfiguration;
import io.domainlifecycles.events.api.DomainEventTypeBasedRouter;
import io.domainlifecycles.events.api.PublishingChannel;
import io.domainlifecycles.events.consume.execution.handler.TransactionalHandlerExecutor;
import io.domainlifecycles.events.gruelbox.api.DomainEventsInstantiator;
import io.domainlifecycles.events.jakarta.jms.api.GruelboxProxyJakartaJmsChannelFactory;
import io.domainlifecycles.events.mq.api.MqProcessingChannel;
import io.domainlifecycles.services.api.ServiceProvider;
import jakarta.jms.ConnectionFactory;
import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import({DlcDomainEventsAutoConfiguration.class, DlcGruelboxAutoConfiguration.class})
public class DlcJakartaJmsGruelboxAutoConfiguration {

    @Bean
    public DomainEventsInstantiator domainEventsInstantiator(){
        return new DomainEventsInstantiator();
    }

    @Bean
    @ConditionalOnBean({ConnectionFactory.class, ObjectMapper.class})
    @ConditionalOnMissingBean(GruelboxProxyJakartaJmsChannelFactory.class)
    public GruelboxProxyJakartaJmsChannelFactory gruelboxProxyActiveMqChannelFactory(
        ServiceProvider serviceProvider,
        ClassProvider classProvider,
        TransactionalHandlerExecutor transactionalHandlerExecutor,
        ConnectionFactory jmsConnectionFactory,
        ObjectMapper objectMapper,
        TransactionOutbox transactionOutbox,
        DomainEventsInstantiator domainEventsInstantiator
    ){
        return new GruelboxProxyJakartaJmsChannelFactory(
            serviceProvider,
            classProvider,
            transactionalHandlerExecutor,
            objectMapper,
            transactionOutbox,
            domainEventsInstantiator,
            jmsConnectionFactory
        );
    }
}
