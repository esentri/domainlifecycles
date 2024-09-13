package io.domainlifecycles.events.jta.publish;

import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.exception.DLCEventsException;
import io.domainlifecycles.events.publish.AbstractTransactionalDomainEventPublisher;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.TransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Objects;

public abstract class AbstractJtaTransactionalDomainEventPublisher extends AbstractTransactionalDomainEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(AbstractJtaTransactionalDomainEventPublisher.class);

    private final JtaDomainEventSender sender;

    private final TransactionManager transactionManager;
    private final boolean afterCommit;
    private Collection<Class<? extends DomainEvent>> passThroughEventTypes;


    public AbstractJtaTransactionalDomainEventPublisher(
        JtaDomainEventSender sender,
        TransactionManager transactionManager,
        boolean afterCommit
    ) {
        this.sender = Objects.requireNonNull(sender, "A JtaDomainEventSender is required!");
        this.transactionManager = Objects.requireNonNull(transactionManager, "A TransactionManager is required!");
        this.afterCommit = afterCommit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void publish(DomainEvent domainEvent) {
        log.debug("Received DomainEvent {} for publishing", domainEvent);
        try {
            final var transaction = transactionManager.getTransaction();

            if(transaction == null || (passThroughEventTypes != null && passThroughEventTypes.contains(domainEvent.getClass()))) {
                log.debug("Passing DomainEvent {} to DomainEventDispatcher passing through to ReceivingDomainEventHandler directly", domainEvent);
                sender.send(domainEvent);
            }else{
                if(this.afterCommit){
                    transaction.registerSynchronization(new AfterCommitSynchronization(sender, domainEvent));
                }else{
                    transaction.registerSynchronization(new BeforeCommitSynchronization(sender, domainEvent));
                }
            }
        } catch (SystemException | RollbackException e) {
            throw DLCEventsException.fail("Couldn't get transaction! Event dispatching failed for %s", domainEvent, e);
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
