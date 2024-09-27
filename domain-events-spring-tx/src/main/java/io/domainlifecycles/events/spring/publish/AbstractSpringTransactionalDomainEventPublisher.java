package io.domainlifecycles.events.spring.publish;

import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.exception.DLCEventsException;
import io.domainlifecycles.events.publish.AbstractTransactionalDomainEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Collection;


public abstract class AbstractSpringTransactionalDomainEventPublisher extends AbstractTransactionalDomainEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(AbstractSpringTransactionalDomainEventPublisher.class);
    private final boolean afterCommit;

    public AbstractSpringTransactionalDomainEventPublisher(
        boolean afterCommit
    ) {
        this.afterCommit = afterCommit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void publish(DomainEvent domainEvent) {
        log.debug("Received DomainEvent {} for publishing", domainEvent);
        if(!TransactionSynchronizationManager.isActualTransactionActive()) {
            var msg = String.format("No transaction active, but active transaction is required! Event dispatching failed for %s", domainEvent);
            log.error(msg);
            throw DLCEventsException.fail(msg);
        }else{
            if(afterCommit){
                TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            log.debug("Publisher transaction committed. Passing DomainEvent {}!", domainEvent);
                            send(domainEvent);
                        }
                    }
                );
            }else{
                TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void beforeCommit(boolean readOnly) {
                            log.debug("Publisher transaction about to commit. Passing DomainEvent {} to DomainEventConsumer!", domainEvent);
                            send(domainEvent);
                        }
                    }
                );
            }
        }
    }
}
