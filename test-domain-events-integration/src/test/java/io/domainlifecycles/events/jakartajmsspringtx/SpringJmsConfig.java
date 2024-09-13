package io.domainlifecycles.events.jakartajmsspringtx;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gruelbox.transactionoutbox.spring.SpringTransactionManager;
import io.domainlifecycles.access.classes.ClassProvider;
import io.domainlifecycles.access.classes.DefaultClassProvider;
import io.domainlifecycles.events.jakarta.jms.api.JakartaJmsDomainEventsConfiguration;
import io.domainlifecycles.events.jakarta.jms.api.SpringTransactionJakartaJmsDomainEventsConfiguration;
import io.domainlifecycles.events.receive.execution.handler.TransactionalHandlerExecutor;
import io.domainlifecycles.events.spring.receive.execution.handler.SpringTransactionalHandlerExecutor;
import io.domainlifecycles.services.api.ServiceProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
@EnableJms
public class SpringJmsConfig {

    @Bean(destroyMethod = "shutDown")
    @DependsOn("initializedDomain")
    public SpringTransactionJakartaJmsDomainEventsConfiguration springTransactionJakartaJmsDomainEventsConfiguration(
        ServiceProvider serviceProvider,
        ClassProvider classProvider,
        TransactionalHandlerExecutor transactionalHandlerExecutor,
        ActiveMQConnectionFactory jmsConnectionFactory,
        ObjectMapper objectMapper
    ){
        return new SpringTransactionJakartaJmsDomainEventsConfiguration(
            jmsConnectionFactory,
            serviceProvider,
            classProvider,
            transactionalHandlerExecutor,
            objectMapper
        );
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
