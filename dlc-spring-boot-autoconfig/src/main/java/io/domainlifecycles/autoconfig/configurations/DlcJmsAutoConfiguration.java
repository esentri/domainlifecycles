package io.domainlifecycles.autoconfig.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.domainlifecycles.access.classes.ClassProvider;
import io.domainlifecycles.events.api.ChannelRoutingConfiguration;
import io.domainlifecycles.events.api.DomainEventTypeBasedRouter;
import io.domainlifecycles.events.api.PublishingChannel;
import io.domainlifecycles.events.consume.execution.handler.TransactionalHandlerExecutor;
import io.domainlifecycles.events.jakarta.jms.api.SpringTransactionJakartaJmsChannelFactory;
import io.domainlifecycles.events.mq.api.MqProcessingChannel;
import io.domainlifecycles.services.api.ServiceProvider;
import java.util.List;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.jms.annotation.EnableJms;

@AutoConfiguration
@EnableJms
@Import(DlcDomainEventsAutoConfiguration.class)
public class DlcJmsAutoConfiguration {

    @Bean
    @DependsOn("initializedDomain")
    @ConditionalOnBean({ActiveMQConnectionFactory.class, ObjectMapper.class})
    @ConditionalOnMissingBean(SpringTransactionJakartaJmsChannelFactory.class)
    public SpringTransactionJakartaJmsChannelFactory springtransactionJakartaJmsChannelFactory(
        ServiceProvider serviceProvider,
        ClassProvider classProvider,
        TransactionalHandlerExecutor transactionalHandlerExecutor,
        ActiveMQConnectionFactory jmsConnectionFactory,
        ObjectMapper objectMapper
    ){
        return new SpringTransactionJakartaJmsChannelFactory(
            jmsConnectionFactory,
            serviceProvider,
            classProvider,
            transactionalHandlerExecutor,
            objectMapper
        );
    }
}
