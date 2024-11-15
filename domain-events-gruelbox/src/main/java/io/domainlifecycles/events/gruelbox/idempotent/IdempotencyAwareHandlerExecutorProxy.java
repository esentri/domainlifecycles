/*
 *     ___
 *     │   ╲                 _
 *     │    ╲ ___ _ __  __ _(_)_ _
 *     |     ╲ _ ╲ '  ╲╱ _` │ │ ' ╲
 *     |_____╱___╱_│_│_╲__,_│_│_||_|
 *     │ │  (_)╱ _│___ __ _  _ __│ |___ ___
 *     │ │__│ │  _╱ -_) _│ ││ ╱ _│ ╱ -_|_-<
 *     │____│_│_│ ╲___╲__│╲_, ╲__│_╲___╱__╱
 *                      |__╱
 *
 *  Copyright 2019-2024 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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

/**
 * IdempotencyAwareHandlerExecutorProxy is a class that acts as a proxy for executing handlers with idempotency protection.
 * It utilizes an underlying TransactionalHandlerExecutor to execute the handlers guaranteeing that each Domain Event
 * is processed only once for handler which was passed to the idempotency configuration.
 *
 * @author Mario Herb
 */
public class IdempotencyAwareHandlerExecutorProxy implements HandlerExecutor {

    private static final Logger log = LoggerFactory.getLogger(IdempotencyAwareHandlerExecutorProxy.class);

    private final TransactionalHandlerExecutor handlerExecutorDelegate;
    private final IdempotencyConfiguration idempotencyConfiguration;
    private final TransactionOutbox outbox;

    /**
     * Constructor for IdempotencyAwareHandlerExecutorProxy.
     *
     * @param handlerExecutorDelegate The TransactionalHandlerExecutor to delegate handler executions
     * @param idempotencyConfiguration The IdempotencyConfiguration for idempotency protection settings
     * @param outbox The TransactionOutbox for handling transactions
     */
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
        return schedule(executionContext, config.get());
    }

    /**
     * Schedules an idempotent operation based on the provided ExecutionContext and IdempotencyConfigurationEntry.
     *
     * @param executionContext The execution context for the domain event handler.
     * @param config The configuration entry for idempotency protection.
     * @return True if scheduling is successful, false otherwise.
     */
    protected boolean schedule(ExecutionContext executionContext,
                               IdempotencyConfigurationEntry config){
        log.debug("Idempotency configuration detected for {}", executionContext);
        var scheduleBuilder = outbox.with();
        if(idempotencyConfiguration.isIdempotencyOrderedByDomainEventType()){
            scheduleBuilder.ordered(executionContext.domainEvent().getClass().getName());
        }
        try {

            scheduleBuilder.delayForAtLeast(idempotencyConfiguration.getIdempotencySchedulingDelay())
                .uniqueRequestId(
                    config.idempotencyFunction().uniqueIdentifier(executionContext.domainEvent()) +
                        "-" + config.handlerClass().getName() +
                        "-" + config.methodName())
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
                return false;
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
