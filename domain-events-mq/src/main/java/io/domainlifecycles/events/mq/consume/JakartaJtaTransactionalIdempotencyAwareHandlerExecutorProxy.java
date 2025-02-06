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

    /**
     * Creates a new JakartaJtaTransactionalIdempotencyAwareHandlerExecutorProxy instance.
     *
     * @param handlerExecutorDelegate The TransactionalHandlerExecutor to delegate handler executions
     * @param idempotencyConfiguration The IdempotencyConfiguration for idempotency protection settings
     * @param outbox The TransactionOutbox for handling transactions
     * @param platformTransactionManager The TransactionManager for managing transactions
     */
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
