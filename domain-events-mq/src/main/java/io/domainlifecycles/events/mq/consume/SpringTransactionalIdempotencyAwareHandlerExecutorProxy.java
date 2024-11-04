package io.domainlifecycles.events.mq.consume;

import com.gruelbox.transactionoutbox.TransactionOutbox;
import io.domainlifecycles.events.consume.execution.detector.ExecutionContext;
import io.domainlifecycles.events.consume.execution.handler.TransactionalHandlerExecutor;
import io.domainlifecycles.events.gruelbox.idempotent.IdempotencyAwareHandlerExecutorProxy;
import io.domainlifecycles.events.gruelbox.idempotent.IdempotencyConfiguration;
import io.domainlifecycles.events.gruelbox.idempotent.IdempotencyConfigurationEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Objects;

/**
 * SpringTransactionalIdempotencyAwareHandlerExecutorProxy is a class that acts as a proxy for executing handlers with idempotency protection.
 * It utilizes an underlying TransactionalHandlerExecutor to execute the handlers guaranteeing that each Domain Event
 * is processed only once for handler which was passed to the idempotency configuration.
 *
 * In an MQ consumer case a transaction muss be provided by this proxy.
 *
 * @author Mario Herb
 */
public class SpringTransactionalIdempotencyAwareHandlerExecutorProxy extends IdempotencyAwareHandlerExecutorProxy implements TransactionalIdempotencyAwareHandlerExecutorProxy {

    private final PlatformTransactionManager platformTransactionManager;
    private static final Logger log = LoggerFactory.getLogger(SpringTransactionalIdempotencyAwareHandlerExecutorProxy.class);

    public SpringTransactionalIdempotencyAwareHandlerExecutorProxy(
        TransactionalHandlerExecutor handlerExecutorDelegate,
        IdempotencyConfiguration idempotencyConfiguration,
        TransactionOutbox outbox,
        PlatformTransactionManager platformTransactionManager
    ) {
        super(handlerExecutorDelegate, idempotencyConfiguration, outbox);
        this.platformTransactionManager = Objects.requireNonNull(platformTransactionManager, "PlatformTransactionManager required!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean schedule(ExecutionContext executionContext, IdempotencyConfigurationEntry config) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        var status = platformTransactionManager.getTransaction(definition);
        log.debug("Started new DLC managed transaction!");
        try {
            boolean returnValue = super.schedule(executionContext, config);
            platformTransactionManager.commit(status);
            log.debug("DLC managed transaction committed!");
            return returnValue;
        } catch (Throwable t) {
            platformTransactionManager.rollback(status);
            log.error("Failed to handle DLC managed transaction", t);
            return false;
        }
    }




}
