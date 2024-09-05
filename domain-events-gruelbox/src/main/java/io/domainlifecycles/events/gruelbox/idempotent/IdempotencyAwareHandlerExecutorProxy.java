package io.domainlifecycles.events.gruelbox.idempotent;

import com.gruelbox.transactionoutbox.TransactionOutbox;
import io.domainlifecycles.events.gruelbox.dispatch.GruelboxDomainEventDispatcher;
import io.domainlifecycles.events.receive.execution.detector.ExecutionContext;
import io.domainlifecycles.events.receive.execution.handler.HandlerExecutor;
import io.domainlifecycles.events.receive.execution.processor.ExecutionContextProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class IdempotencyAwareHandlerExecutorProxy implements HandlerExecutor {

    private static final Logger log = LoggerFactory.getLogger(IdempotencyAwareHandlerExecutorProxy.class);

    private final HandlerExecutor handlerExecutorDelegate;
    private final IdempotencyConfiguration idempotencyConfiguration;
    private final TransactionOutbox outbox;
    private final boolean orderedByDomainEventType;
    private final Duration schedulingDelay;

    public IdempotencyAwareHandlerExecutorProxy(HandlerExecutor handlerExecutorDelegate, IdempotencyConfiguration idempotencyConfiguration, TransactionOutbox outbox, boolean orderedByDomainEventType, Duration schedulingDelay) {
        this.handlerExecutorDelegate = Objects.requireNonNull(handlerExecutorDelegate, "A handlerExecutorDelegate is required!");
        this.idempotencyConfiguration = Objects.requireNonNull(idempotencyConfiguration, "An IdempotencyConfiguration is required!");
        this.outbox = Objects.requireNonNull(outbox, "A TransactionOutbox is required!");
        this.orderedByDomainEventType = orderedByDomainEventType;
        this.schedulingDelay = schedulingDelay;
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
        if(orderedByDomainEventType){
            scheduleBuilder.ordered(executionContext.domainEvent().getClass().getName());
        }
        try {
            scheduleBuilder.delayForAtLeast(schedulingDelay)
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
