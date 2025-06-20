package io.domainlifecycles.autoconfig.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gruelbox.transactionoutbox.DefaultPersistor;
import com.gruelbox.transactionoutbox.Dialect;
import com.gruelbox.transactionoutbox.TransactionOutbox;
import com.gruelbox.transactionoutbox.TransactionOutboxListener;
import com.gruelbox.transactionoutbox.jackson.JacksonInvocationSerializer;
import com.gruelbox.transactionoutbox.spring.SpringTransactionManager;
import io.domainlifecycles.events.api.ChannelRoutingConfiguration;
import io.domainlifecycles.events.api.DomainEventTypeBasedRouter;
import io.domainlifecycles.events.api.PublishingChannel;
import io.domainlifecycles.events.consume.execution.handler.TransactionalHandlerExecutor;
import io.domainlifecycles.events.gruelbox.api.DomainEventsInstantiator;
import io.domainlifecycles.events.gruelbox.api.GruelboxChannelFactory;
import io.domainlifecycles.events.gruelbox.api.GruelboxProcessingChannel;
import io.domainlifecycles.events.gruelbox.api.PollerConfiguration;
import io.domainlifecycles.events.gruelbox.api.PublishingSchedulerConfiguration;
import io.domainlifecycles.events.gruelbox.idempotent.IdempotencyConfiguration;
import io.domainlifecycles.events.gruelbox.idempotent.IdempotencyConfigurationEntry;
import io.domainlifecycles.events.spring.receive.execution.handler.SpringTransactionalHandlerExecutor;
import io.domainlifecycles.services.api.ServiceProvider;
import java.time.Duration;
import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;

@AutoConfiguration
@Import({DlcDomainEventsAutoConfiguration.class, DlcJakartaJmsGruelboxAutoConfiguration.class})
public class DlcJakartaJmsGruelboxIdempotentAutoConfiguration {

    @Bean
    @ConditionalOnBean({ObjectMapper.class, TransactionOutboxListener.class})
    public TransactionOutbox transactionOutbox(
        SpringTransactionManager springTransactionManager,
        ObjectMapper objectMapper,
        TransactionOutboxListener transactionOutboxListener,
        DomainEventsInstantiator domainEventsInstantiator
    ) {
        return TransactionOutbox.builder()
            .instantiator(domainEventsInstantiator)
            .transactionManager(springTransactionManager)
            .blockAfterAttempts(3)
            .attemptFrequency(Duration.ofSeconds(1))
            .persistor(DefaultPersistor.builder()
                .serializer(JacksonInvocationSerializer.builder().mapper(objectMapper).build())
                .dialect(Dialect.H2)
                .build())
            .listener(transactionOutboxListener)
            .build();
    }

    @Bean(destroyMethod = "close")
    @ConditionalOnBean(IdempotencyConfiguration.class)
    @ConditionalOnMissingBean(GruelboxProcessingChannel.class)
    public GruelboxProcessingChannel gruelboxChannel(
        ServiceProvider serviceProvider,
        TransactionOutbox transactionOutbox,
        TransactionalHandlerExecutor transactionalHandlerExecutor,
        DomainEventsInstantiator domainEventsInstantiator,
        IdempotencyConfiguration idempotencyConfiguration
    ){
        return new GruelboxChannelFactory(
            serviceProvider,
            transactionOutbox,
            transactionalHandlerExecutor,
            domainEventsInstantiator,
            idempotencyConfiguration,
            new PollerConfiguration(5000,1000),
            new PublishingSchedulerConfiguration()
        ).processingChannel("c1");
    }

    @Bean
    @ConditionalOnBean(PlatformTransactionManager.class)
    public TransactionalHandlerExecutor transactionalHandlerExecutor(PlatformTransactionManager platformTransactionManager){
        return new SpringTransactionalHandlerExecutor(platformTransactionManager);
    }
}
