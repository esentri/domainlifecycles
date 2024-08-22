package io.domainlifecycles.events.springgruelboxintegration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gruelbox.transactionoutbox.DefaultPersistor;
import com.gruelbox.transactionoutbox.Dialect;
import com.gruelbox.transactionoutbox.TransactionOutbox;
import com.gruelbox.transactionoutbox.TransactionOutboxEntry;
import com.gruelbox.transactionoutbox.TransactionOutboxListener;
import com.gruelbox.transactionoutbox.jackson.JacksonInvocationSerializer;
import com.gruelbox.transactionoutbox.spring.SpringInstantiator;
import com.gruelbox.transactionoutbox.spring.SpringTransactionManager;
import io.domainlifecycles.events.gruelbox.api.GruelboxDomainEventsConfiguration;
import io.domainlifecycles.events.gruelbox.dispatch.GruelboxDispatcherInstantiator;
import io.domainlifecycles.events.receive.execution.handler.TransactionalHandlerExecutor;
import io.domainlifecycles.events.spring.receive.execution.handler.SpringTransactionalHandlerExecutor;
import io.domainlifecycles.services.api.ServiceProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Import({SpringTransactionManager.class})
@Slf4j
public class GruelboxIntegrationConfig {

    @Bean
    @Lazy
    public TransactionOutbox transactionOutbox(
        SpringTransactionManager springTransactionManager,
        ObjectMapper objectMapper,
        TransactionOutboxListener transactionOutboxListener
    ) {
        return TransactionOutbox.builder()
            .instantiator(new GruelboxDispatcherInstantiator())
            .transactionManager(springTransactionManager)
            .blockAfterAttempts(3)
            .persistor(DefaultPersistor.builder()
                           .serializer(JacksonInvocationSerializer.builder().mapper(objectMapper).build())
                           .dialect(Dialect.H2)
                           .build())
            .listener(transactionOutboxListener)
            .build();
    }

    @Bean
    @DependsOn("initializedDomain")
    public GruelboxDomainEventsConfiguration gruelboxDomainEventsConfiguration(
        ServiceProvider serviceProvider,
        TransactionOutbox transactionOutbox,
        TransactionalHandlerExecutor transactionalHandlerExecutor
    ){
        return new GruelboxDomainEventsConfiguration(serviceProvider, transactionOutbox, transactionalHandlerExecutor);
    }

    @Bean
    public TransactionalHandlerExecutor transactionalHandlerExecutor(PlatformTransactionManager platformTransactionManager){
        return new SpringTransactionalHandlerExecutor(platformTransactionManager);
    }

    @Bean
    public MyTransactionOutboxListener transactionOutboxListener(){
        return new MyTransactionOutboxListener();
    }




}
