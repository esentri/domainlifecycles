package io.domainlifecycles.autoconfig.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gruelbox.transactionoutbox.DefaultPersistor;
import com.gruelbox.transactionoutbox.Dialect;
import com.gruelbox.transactionoutbox.TransactionOutbox;
import com.gruelbox.transactionoutbox.jackson.JacksonInvocationSerializer;
import com.gruelbox.transactionoutbox.spring.SpringInstantiator;
import com.gruelbox.transactionoutbox.spring.SpringTransactionManager;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;

@AutoConfiguration
@Import({DlcDomainEventsAutoConfiguration.class, SpringInstantiator.class, SpringTransactionManager.class})
@EnableScheduling
public class DlcGruelboxAutoConfiguration {

    @Bean
    @Lazy
    @ConditionalOnBean(ObjectMapper.class)
    public TransactionOutbox transactionOutbox(
        SpringTransactionManager springTransactionManager,
        SpringInstantiator springInstantiator,
        ObjectMapper objectMapper
    ) {
        return TransactionOutbox.builder()
            .instantiator(springInstantiator)
            .transactionManager(springTransactionManager)
            .blockAfterAttempts(3)
            .persistor(DefaultPersistor.builder()
                .serializer(JacksonInvocationSerializer.builder().mapper(objectMapper).build())
                .dialect(Dialect.H2)
                .build())
            .build();
    }
}
