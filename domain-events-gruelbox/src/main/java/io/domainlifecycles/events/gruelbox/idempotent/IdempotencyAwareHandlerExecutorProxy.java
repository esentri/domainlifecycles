package io.domainlifecycles.events.gruelbox.idempotent;

import com.gruelbox.transactionoutbox.TransactionOutbox;
import io.domainlifecycles.events.consume.execution.detector.ExecutionContext;
import io.domainlifecycles.events.consume.execution.handler.HandlerExecutor;
import io.domainlifecycles.events.consume.execution.handler.TransactionalHandlerExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.Optional;

public final class IdempotencyAwareHandlerExecutorProxy implements HandlerExecutor {

    private static final Logger log = LoggerFactory.getLogger(IdempotencyAwareHandlerExecutorProxy.class);

    private final TransactionalHandlerExecutor handlerExecutorDelegate;
    private final IdempotencyConfiguration idempotencyConfiguration;
    private final TransactionOutbox outbox;


    public IdempotencyAwareHandlerExecutorProxy(TransactionalHandlerExecutor handlerExecutorDelegate,
                                                IdempotencyConfiguration idempotencyConfiguration,
                                                TransactionOutbox outbox) {
        this.handlerExecutorDelegate = Objects.requireNonNull(handlerExecutorDelegate, "A handlerExecutorDelegate is required!");
        this.idempotencyConfiguration = Objects.requireNonNull(idempotencyConfiguration, "An IdempotencyConfiguration is required!");
        this.outbox = Objects.requireNonNull(outbox, "A TransactionOutbox is required!");
        log.info("IdempotencyAwareHandlerExecutorProxy created!");
    }

    @Override
    public boolean execute(ExecutionContext executionContext) {
        var config = idempotencyProtectionConfiguration(executionContext);
        if(config.isEmpty()){
            log.debug("No idempotency configuration detected for {}", executionContext);
            return handlerExecutorDelegate.execute(executionContext);
        }
        log.debug("Idempotency configuration detected for {}", executionContext);
        var scheduleBuilder = outbox.with();
        if(idempotencyConfiguration.isIdempotencyOrderedByDomainEventType()){
            scheduleBuilder.ordered(executionContext.domainEvent().getClass().getName());
        }
        try {
            scheduleBuilder.delayForAtLeast(idempotencyConfiguration.getIdempotencySchedulingDelay())
                .uniqueRequestId(config.get().idempotencyFunction().uniqueIdentifier(executionContext.domainEvent()))
                .schedule(IdempotentExecutor.class)
                .execute(
                    new IdempotentExecutionContext(
                        executionContext.handler().getClass(),
                        executionContext.handlerMethodName(),
                        executionContext.domainEvent()
                    )
                );
        }catch (Throwable t){
            if(t.getCause() instanceof InvocationTargetException ite){
                log.info("Idempotent scheduling failed! {}",ite.getTargetException().getMessage(), t);
            }else{
                log.error("Unknown scheduling error!", t);
                throw t;
            }
        }
        return true;
    }

    @Override
    public void beforeExecution(ExecutionContext executionContext) {
        if(idempotencyProtectionConfiguration(executionContext).isPresent()){
            handlerExecutorDelegate.beforeExecution(executionContext);
        }
    }

    @Override
    public void afterExecution(ExecutionContext executionContext, boolean success) {
        if(idempotencyProtectionConfiguration(executionContext).isPresent()){
            handlerExecutorDelegate.afterExecution(executionContext, success);
        }
    }

    private Optional<IdempotencyConfigurationEntry> idempotencyProtectionConfiguration(ExecutionContext executionContext){
        return idempotencyConfiguration.idempotencyProtectionConfiguration(executionContext);
    }

}
