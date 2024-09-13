package io.domainlifecycles.events.spring.publish;

import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.publish.AbstractTransactionalDomainEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Collection;


public abstract class AbstractSpringTransactionalDomainEventPublisher extends AbstractTransactionalDomainEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(AbstractSpringTransactionalDomainEventPublisher.class);
    private Collection<Class<? extends DomainEvent>> passThroughEventTypes;
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
        if(!TransactionSynchronizationManager.isActualTransactionActive() || (passThroughEventTypes != null && passThroughEventTypes.contains(domainEvent.getClass()))) {
            log.debug("Passing DomainEvent {} to DomainEventDispatcher passing through directly", domainEvent);
            send(domainEvent);
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
                            log.debug("Publisher transaction about to commit. Passing DomainEvent {} to ReceivingDomainEventHandler!", domainEvent);
                            send(domainEvent);
                        }
                    }
                );
            }
        }
    }

    /**
     * Sets the pass-through event types.
     * Pass-through event types are the types of events that should bypass the transaction handling and be immediately dispatched.
     * If a transaction is active and the domain event is not a pass-through event, it will register a synchronization with the transaction
     * to dispatch the event after the transaction is successfully committed.
     *
     * @param passThroughEventTypes the collection of pass-through event types
     */
    public void setPassThroughEventTypes(Collection<Class<? extends DomainEvent>> passThroughEventTypes){
        this.passThroughEventTypes = passThroughEventTypes;
    }
}
