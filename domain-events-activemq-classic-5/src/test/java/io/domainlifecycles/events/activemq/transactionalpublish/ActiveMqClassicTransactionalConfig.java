package io.domainlifecycles.events.activemq.transactionalpublish;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.domainlifecycles.access.classes.ClassProvider;
import io.domainlifecycles.access.classes.DefaultClassProvider;
import io.domainlifecycles.events.activemq.api.SpringTransactionalActiveMqDomainEventsConfiguration;
import io.domainlifecycles.events.receive.execution.handler.TransactionalHandlerExecutor;
import io.domainlifecycles.events.spring.receive.execution.handler.SpringTransactionalHandlerExecutor;
import io.domainlifecycles.services.api.ServiceProvider;
import jakarta.jms.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
@EnableJms
public class ActiveMqClassicTransactionalConfig {

    @Bean
    public BrokerService broker() throws Exception {

        BrokerService broker = new BrokerService();
        broker.setPersistent(false);
        broker.setUseJmx(true);
        broker.addConnector("tcp://localhost:62222");
        return broker;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory("tcp://localhost:62222");
    }


    @Bean(destroyMethod = "shutDown")
    @DependsOn("initializedDomain")
    public SpringTransactionalActiveMqDomainEventsConfiguration springTransactionalActiveMqDomainEventsConfiguration(
        ServiceProvider serviceProvider,
        ClassProvider classProvider,
        TransactionalHandlerExecutor transactionalHandlerExecutor,
        ActiveMQConnectionFactory jmsConnectionFactory,
        ObjectMapper objectMapper
    ){
        return new SpringTransactionalActiveMqDomainEventsConfiguration(
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
