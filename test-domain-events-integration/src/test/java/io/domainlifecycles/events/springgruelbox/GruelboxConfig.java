package io.domainlifecycles.events.springgruelbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gruelbox.transactionoutbox.DefaultPersistor;
import com.gruelbox.transactionoutbox.Dialect;
import com.gruelbox.transactionoutbox.TransactionOutbox;
import com.gruelbox.transactionoutbox.TransactionOutboxEntry;
import com.gruelbox.transactionoutbox.TransactionOutboxListener;
import com.gruelbox.transactionoutbox.jackson.JacksonInvocationSerializer;
import com.gruelbox.transactionoutbox.spring.SpringInstantiator;
import com.gruelbox.transactionoutbox.spring.SpringTransactionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@Import({SpringInstantiator.class, SpringTransactionManager.class})
@EnableScheduling
@Slf4j
public class GruelboxConfig {

    @Bean
    @Lazy
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
            .listener(new TransactionOutboxListener() {

                    @Override
                    public void success(TransactionOutboxEntry entry) {
                        log.info("Entry '{}' processed successfully!", entry);
                    }

                    @Override
                    public void failure(TransactionOutboxEntry entry, Throwable cause) {
                        log.error("Entry '{}' failed!", entry, cause);
                    }

                    @Override
                    public void blocked(TransactionOutboxEntry entry, Throwable cause) {
                        log.error("Entry '{}' blocked!", entry, cause);
                    }
                })
            .build();
    }

}
