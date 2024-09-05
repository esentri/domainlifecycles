package io.domainlifecycles.events.springgruelboxintegrationidempotency;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gruelbox.transactionoutbox.DefaultPersistor;
import com.gruelbox.transactionoutbox.Dialect;
import com.gruelbox.transactionoutbox.TransactionOutbox;
import com.gruelbox.transactionoutbox.TransactionOutboxListener;
import com.gruelbox.transactionoutbox.jackson.JacksonInvocationSerializer;
import com.gruelbox.transactionoutbox.spring.SpringTransactionManager;
import io.domainlifecycles.events.IdemProtectedDomainEvent;
import io.domainlifecycles.events.IdemProtectedListener;
import io.domainlifecycles.events.MyTransactionOutboxListener;
import io.domainlifecycles.events.gruelbox.api.GruelboxDomainEventsConfiguration;
import io.domainlifecycles.events.gruelbox.dispatch.DomainEventsInstantiator;
import io.domainlifecycles.events.gruelbox.idempotent.IdempotencyConfiguration;
import io.domainlifecycles.events.gruelbox.idempotent.IdempotencyConfigurationEntry;
import io.domainlifecycles.events.receive.execution.handler.TransactionalHandlerExecutor;
import io.domainlifecycles.events.spring.receive.execution.handler.SpringTransactionalHandlerExecutor;
import io.domainlifecycles.services.api.ServiceProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Import({SpringTransactionManager.class})
@Slf4j
public class GruelboxIntegrationIdempotencyConfig {

    @Bean
    @Lazy
    public TransactionOutbox transactionOutbox(
        SpringTransactionManager springTransactionManager,
        ObjectMapper objectMapper,
        TransactionOutboxListener transactionOutboxListener
    ) {
        return TransactionOutbox.builder()
            .instantiator(new DomainEventsInstantiator())
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
        TransactionalHandlerExecutor transactionalHandlerExecutor,
        IdempotencyConfiguration idempotencyConfiguration
    ){
        return new GruelboxDomainEventsConfiguration(serviceProvider, transactionOutbox, transactionalHandlerExecutor, idempotencyConfiguration);
    }

    @Bean
    public TransactionalHandlerExecutor transactionalHandlerExecutor(PlatformTransactionManager platformTransactionManager){
        return new SpringTransactionalHandlerExecutor(platformTransactionManager);
    }

    @Bean
    public MyTransactionOutboxListener transactionOutboxListener(){
        return new MyTransactionOutboxListener();
    }


    @Bean
    public IdempotencyConfiguration idempotencyConfiguration(){
        var config = new IdempotencyConfiguration();
        config.addConfigurationEntry(new IdempotencyConfigurationEntry(IdemProtectedListener.class, "handle", IdemProtectedDomainEvent.class, (e)-> ((IdemProtectedDomainEvent)e).id()));
        return config;
    }


}
