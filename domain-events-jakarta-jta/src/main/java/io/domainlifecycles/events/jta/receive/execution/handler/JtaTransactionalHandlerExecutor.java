/*
 *
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

package io.domainlifecycles.events.jta.receive.execution.handler;

import io.domainlifecycles.events.receive.execution.handler.ReflectiveHandlerExecutor;
import io.domainlifecycles.events.receive.execution.handler.TransactionalHandlerExecutor;
import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.InvalidTransactionException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.Status;
import jakarta.transaction.SystemException;
import jakarta.transaction.Transaction;
import jakarta.transaction.TransactionManager;
import io.domainlifecycles.events.exception.DLCEventsException;
import io.domainlifecycles.events.receive.execution.detector.ExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JtaTransactionalHandlerExecutor is a concrete implementation of the TransactionalHandlerExecutor interface.
 * It executes domain event listeners in a transactional manner using JTA (Java Transaction API).
 *
 * @author Mario Herb
 */
public class JtaTransactionalHandlerExecutor extends ReflectiveHandlerExecutor implements TransactionalHandlerExecutor {
    private static final Logger log = LoggerFactory.getLogger(JtaTransactionalHandlerExecutor.class);

    private final TransactionManager transactionManager;
    private final Map<ExecutionContext, Transaction> managedTransactions;
    private final Map<ExecutionContext, Transaction> suspendedTransactions;

    public JtaTransactionalHandlerExecutor(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
        managedTransactions = new ConcurrentHashMap<>();
        suspendedTransactions = new ConcurrentHashMap<>();
    }

    /**
     * Executes before the listener method is executed. Starts a new independent transaction for each handler.
     *
     * @param executionContext context of domain event handler execution
     * @throws DLCEventsException if there is an error accessing the TransactionManager or starting a new transaction.
     */
    @Override
    public void beforeExecution(ExecutionContext executionContext) {
        if(createNewTransactionForHandling(executionContext)) {
            try {
                var transaction = transactionManager.getTransaction();
                if(transaction != null){
                    suspendedTransactions.put(executionContext, transactionManager.suspend());
                    log.debug("Suspended externally managed transaction!");
                }
                transactionManager.begin();
                log.debug("Started new DLC managed transaction!");
                var managedTransaction = transactionManager.getTransaction();
                managedTransactions.put(executionContext, managedTransaction);
            } catch (SystemException e) {
                throw DLCEventsException.fail("Could access TransactionManager to get transaction", e);
            } catch (NotSupportedException e) {
                throw DLCEventsException.fail("Could not start new transaction", e);
            }
        }
    }

    /**
     * Executes after the listener method is executed.
     * Handles DLC managed transactions as well as suspended transactions, which are resumed after handler execution.
     *
     * @param executionContext context of domain event handler execution
     * @param success    A boolean flag indicating whether the listener method execution was successful or not.
     * @throws DLCEventsException if there is an error committing or rolling back the transaction.
     * @throws DLCEventsException if the managed transaction cannot be removed.
     * @throws DLCEventsException if there is an error suspending an already running transaction.
     */
    @Override
    public void afterExecution(ExecutionContext executionContext, boolean success) {
        try {
            var transaction = transactionManager.getTransaction();
            try {
                if (managedTransactions.get(executionContext).equals(transaction) && Status.STATUS_ACTIVE == transaction.getStatus()) {
                    log.debug("DLC managed transaction recognized!");
                    if(success){
                        transaction.commit();
                        log.debug("DLC managed transaction committed!");
                    } else{
                        transaction.rollback();
                        log.debug("DLC managed transaction rolled back!");
                    }
                }
            } catch (HeuristicRollbackException | HeuristicMixedException | RollbackException e) {
                throw DLCEventsException.fail("Failed to commit DLC managed transaction");
            } finally {
                if (managedTransactions.containsKey(executionContext)) {
                    managedTransactions.remove(executionContext);
                    log.debug("DLC managed transaction removed!");
                }
            }
            if(suspendedTransactions.containsKey(executionContext)){
                var suspended = suspendedTransactions.get(executionContext);
                try {
                    transactionManager.resume(suspended);
                    log.debug("Suspended transaction resumed!");
                    suspendedTransactions.remove(executionContext);
                } catch (InvalidTransactionException e) {
                    throw DLCEventsException.fail("Failed to resume suspended transaction");
                }
            }
        } catch (SystemException e) {
            throw DLCEventsException.fail("Could access TransactionManager to get transaction", e);
        }
    }

}
