package io.domainlifecycles.events.jta.publish;

import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.exception.DLCEventsException;
import io.domainlifecycles.events.publish.AbstractTransactionalDomainEventPublisher;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.TransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Objects;

public abstract class AbstractJtaTransactionalDomainEventPublisher extends AbstractTransactionalDomainEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(AbstractJtaTransactionalDomainEventPublisher.class);

    private final JtaDomainEventSender sender;
    private final TransactionManager transactionManager;
    private final boolean afterCommit;

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
            if(transaction == null) {
                var msg = String.format("No transaction active, but active transaction is required! Event dispatching failed for %s", domainEvent);
                log.error(msg);
                throw DLCEventsException.fail(msg);
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





}
