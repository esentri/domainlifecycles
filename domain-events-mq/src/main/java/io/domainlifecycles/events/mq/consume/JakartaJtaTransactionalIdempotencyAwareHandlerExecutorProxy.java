package io.domainlifecycles.events.mq.consume;

import com.gruelbox.transactionoutbox.TransactionOutbox;
import io.domainlifecycles.events.consume.execution.detector.ExecutionContext;
import io.domainlifecycles.events.consume.execution.handler.TransactionalHandlerExecutor;
import io.domainlifecycles.events.gruelbox.idempotent.IdempotencyAwareHandlerExecutorProxy;
import io.domainlifecycles.events.gruelbox.idempotent.IdempotencyConfiguration;
import io.domainlifecycles.events.gruelbox.idempotent.IdempotencyConfigurationEntry;
import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.TransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Objects;

/**
 * JakartaJtaTransactionalIdempotencyAwareHandlerExecutorProxy is a class that acts as a proxy for executing handlers with idempotency protection.
 * It utilizes an underlying TransactionalHandlerExecutor to execute the handlers guaranteeing that each Domain Event
 * is processed only once for handler which was passed to the idempotency configuration.
 *
 * In an MQ consumer case a transaction muss be provided by this proxy.
 *
 * @author Mario Herb
 */
public class JakartaJtaTransactionalIdempotencyAwareHandlerExecutorProxy extends IdempotencyAwareHandlerExecutorProxy implements TransactionalIdempotencyAwareHandlerExecutorProxy{

    private final TransactionManager transactionManager;
    private static final Logger log = LoggerFactory.getLogger(JakartaJtaTransactionalIdempotencyAwareHandlerExecutorProxy.class);

    public JakartaJtaTransactionalIdempotencyAwareHandlerExecutorProxy(
        TransactionalHandlerExecutor handlerExecutorDelegate,
        IdempotencyConfiguration idempotencyConfiguration,
        TransactionOutbox outbox,
        TransactionManager platformTransactionManager) {
        super(handlerExecutorDelegate, idempotencyConfiguration, outbox);
        this.transactionManager = Objects.requireNonNull(platformTransactionManager, "TransactionManager required!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean schedule(ExecutionContext executionContext, IdempotencyConfigurationEntry config) {
        try {
            transactionManager.begin();
            var managedTransaction = transactionManager.getTransaction();
            log.debug("Started new DLC managed transaction!");
            boolean returnValue = super.schedule(executionContext, config);
            managedTransaction.commit();
            log.debug("DLC managed transaction committed!");
            return returnValue;
        } catch (SystemException e) {
            log.error("Could access TransactionManager to get transaction", e);
            return false;
        } catch (NotSupportedException e) {
            log.error("Could not start new transaction", e);
            return false;
        } catch (HeuristicRollbackException | HeuristicMixedException |RollbackException e) {
            log.error("Could not schedule", e);
            return false;
        }
    }




}
