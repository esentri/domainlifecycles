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

package io.domainlifecycles.events.spring.receive.execution.handler;

import io.domainlifecycles.events.consume.execution.detector.ExecutionContext;
import io.domainlifecycles.events.consume.execution.handler.ReflectiveHandlerExecutor;
import io.domainlifecycles.events.consume.execution.handler.TransactionalHandlerExecutor;
import io.domainlifecycles.events.exception.DLCEventsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The SpringTransactionalHandlerExecutor class is an implementation of the TransactionalHandlerExecutor interface.
 * It performs the execution of handlers by wrapping them a new independent transaction
 *
 * @author Mario Herb
 */
@Deprecated
public final class SpringTransactionalHandlerExecutor extends ReflectiveHandlerExecutor implements TransactionalHandlerExecutor {

    private static final Logger log = LoggerFactory.getLogger(SpringTransactionalHandlerExecutor.class);
    private final PlatformTransactionManager transactionManager;
    private final Map<ExecutionContext, TransactionStatus> managedTransactions;

    /**
     * Constructor for SpringTransactionalHandlerExecutor.
     *
     * @param transactionManager The PlatformTransactionManager to be used.
     */
    public SpringTransactionalHandlerExecutor(PlatformTransactionManager transactionManager) {
        this.transactionManager = Objects.requireNonNull(transactionManager, "A PlatformTransactionManager is required!");
        managedTransactions = new ConcurrentHashMap<>();
    }

    /**
     * Executes the before execution logic for the given execution context.
     * If createNewTransactionForHandling returns true for the execution context, it starts a new transaction using
     * the given transaction manager
     * and adds the transaction status to the managedTransactions map.
     *
     * @param executionContext the execution context
     */
    @Override
    public void beforeExecution(ExecutionContext executionContext) {
        if (createNewTransactionForHandling(executionContext)) {
            DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
            definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            var status = transactionManager.getTransaction(definition);
            log.debug("Started new DLC managed transaction!");
            managedTransactions.put(executionContext, status);
        }
    }

    /**
     * Executes the after execution logic for the given execution context.
     * If the execution context has an associated managed transaction and it is not completed,
     * the method either commits or rolls back the transaction based on the success flag.
     * It then removes the execution context from the managedTransactions map.
     *
     * @param executionContext the execution context executed
     * @param success          A boolean flag indicating whether the listener method execution was successful or not.
     * @throws DLCEventsException if there is an error handling the managed transaction
     */
    @Override
    public void afterExecution(ExecutionContext executionContext, boolean success) {
        TransactionStatus status = managedTransactions.get(executionContext);
        if (status != null && !status.isCompleted()) {
            try {
                log.debug("DLC managed transaction recognized!");
                if (success) {
                    transactionManager.commit(status);
                    log.debug("DLC managed transaction committed!");
                } else {
                    transactionManager.rollback(status);
                    log.debug("DLC managed transaction rolled back!");
                }
            } catch (Throwable t) {
                throw DLCEventsException.fail("Failed to handle DLC managed transaction");
            } finally {
                managedTransactions.remove(executionContext);
                log.debug("DLC managed transaction removed!");
            }
        }
    }

}
