package io.domainlifecycles.events.jta.api;

import io.domainlifecycles.events.consume.DomainEventConsumer;
import io.domainlifecycles.events.consume.execution.handler.HandlerExecutor;
import io.domainlifecycles.events.inmemory.InMemoryChannelFactory;
import io.domainlifecycles.events.jta.publish.DirectJtaTransactionalDomainEventPublisher;
import io.domainlifecycles.events.jta.receive.execution.handler.JtaTransactionalHandlerExecutor;
import io.domainlifecycles.events.publish.DomainEventPublisher;
import io.domainlifecycles.services.api.ServiceProvider;
import jakarta.transaction.TransactionManager;

import java.util.Objects;

public class JtaInMemoryChannelFactory extends InMemoryChannelFactory {

    private final TransactionManager transactionManager;
    private final boolean publishAfterCommit;

    public JtaInMemoryChannelFactory(TransactionManager transactionManager, ServiceProvider serviceProvider, int executorThreads, boolean publishAfterCommit) {
        super(serviceProvider, executorThreads);
        this.transactionManager = Objects.requireNonNull(transactionManager, "A TransactionManager is required!");
        this.publishAfterCommit = publishAfterCommit;
    }

    public JtaInMemoryChannelFactory(TransactionManager transactionManager, ServiceProvider serviceProvider, boolean publishAfterCommit) {
        this(transactionManager, serviceProvider, 0, publishAfterCommit);
    }

    @Override
    protected DomainEventPublisher useDomainEventPublisher(DomainEventConsumer domainEventConsumer) {
        return new DirectJtaTransactionalDomainEventPublisher(domainEventConsumer, transactionManager, publishAfterCommit);
    }

    @Override
    protected HandlerExecutor useHandlerExecutor() {
        return new JtaTransactionalHandlerExecutor(transactionManager);
    }
}
