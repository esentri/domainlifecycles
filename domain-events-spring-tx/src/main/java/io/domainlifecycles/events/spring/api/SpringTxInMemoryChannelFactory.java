package io.domainlifecycles.events.spring.api;

import io.domainlifecycles.events.consume.DomainEventConsumer;
import io.domainlifecycles.events.consume.execution.handler.HandlerExecutor;
import io.domainlifecycles.events.inmemory.InMemoryChannelFactory;
import io.domainlifecycles.events.publish.DomainEventPublisher;
import io.domainlifecycles.events.spring.publish.DirectSpringTransactionalDomainEventPublisher;
import io.domainlifecycles.events.spring.receive.execution.handler.SpringTransactionalHandlerExecutor;
import io.domainlifecycles.services.api.ServiceProvider;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Objects;

public class SpringTxInMemoryChannelFactory extends InMemoryChannelFactory {

    private final PlatformTransactionManager transactionManager;
    private final boolean publishAfterCommit;

    public SpringTxInMemoryChannelFactory(PlatformTransactionManager transactionManager, ServiceProvider serviceProvider, int executorThreads, boolean publishAfterCommit) {
        super(serviceProvider, executorThreads);
        this.transactionManager = Objects.requireNonNull(transactionManager, "A TransactionManager is required!");
        this.publishAfterCommit = publishAfterCommit;
    }

    public SpringTxInMemoryChannelFactory(PlatformTransactionManager transactionManager, ServiceProvider serviceProvider, boolean publishAfterCommit) {
        this(transactionManager, serviceProvider, 0, publishAfterCommit);
    }

    @Override
    protected DomainEventPublisher useDomainEventPublisher(DomainEventConsumer domainEventConsumer) {
        return new DirectSpringTransactionalDomainEventPublisher(domainEventConsumer, publishAfterCommit);
    }

    @Override
    protected HandlerExecutor useHandlerExecutor() {
        return new SpringTransactionalHandlerExecutor(transactionManager);
    }
}
