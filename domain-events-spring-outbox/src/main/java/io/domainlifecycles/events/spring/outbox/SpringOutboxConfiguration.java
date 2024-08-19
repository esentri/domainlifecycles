package io.domainlifecycles.events.spring.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.domainlifecycles.events.api.DomainEventsConfiguration;
import io.domainlifecycles.events.spring.SpringTransactionDomainEventsConfiguration;
import io.domainlifecycles.events.spring.outbox.api.TransactionalOutbox;
import io.domainlifecycles.events.spring.outbox.impl.SpringJdbcOutbox;
import io.domainlifecycles.events.spring.outbox.poll.AbstractOutboxPoller;
import io.domainlifecycles.events.spring.outbox.poll.DirectOutboxPoller;
import io.domainlifecycles.services.api.ServiceProvider;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;


public class SpringOutboxConfiguration {

    private final DomainEventsConfiguration domainEventsConfiguration;
    private final TransactionalOutbox transactionalOutbox;
    private final AbstractOutboxPoller outboxPoller;

    public SpringOutboxConfiguration(DataSource dataSource,
                                     PlatformTransactionManager platformTransactionManager,
                                     ObjectMapper objectMapper,
                                     ServiceProvider serviceProvider
                                     ) {
        var transactionalOutbox = new SpringJdbcOutbox(dataSource, objectMapper, platformTransactionManager);
        var baseConfiguration = SpringTransactionDomainEventsConfiguration.configuration(platformTransactionManager, serviceProvider, false);
        var builder = baseConfiguration.toBuilder();
        var domainEventsConfiguration = builder
            .withDomainEventPublisher(
                new SpringOutboxDomainEventPublisher(
                    transactionalOutbox,
                    platformTransactionManager
                )
            )
            .build();

        var outboxPoller = new DirectOutboxPoller(transactionalOutbox, domainEventsConfiguration.getReceivingDomainEventHandler());
        this.outboxPoller = outboxPoller;
        this.transactionalOutbox = transactionalOutbox;
        this.domainEventsConfiguration = domainEventsConfiguration;
    }

    public SpringOutboxConfiguration(
        TransactionalOutbox transactionalOutbox,
        DomainEventsConfiguration domainEventsConfiguration,
        AbstractOutboxPoller outboxPoller
    ) {
        this.transactionalOutbox = transactionalOutbox;
        this.domainEventsConfiguration = domainEventsConfiguration;
        this.outboxPoller = outboxPoller;
    }

    public DomainEventsConfiguration getDomainEventsConfiguration() {
        return domainEventsConfiguration;
    }

    public TransactionalOutbox getTransactionalOutbox() {
        return transactionalOutbox;
    }

    public AbstractOutboxPoller getOutboxPoller() {
        return outboxPoller;
    }
}
