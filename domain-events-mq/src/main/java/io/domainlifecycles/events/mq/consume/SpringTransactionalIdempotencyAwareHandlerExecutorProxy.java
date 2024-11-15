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

    /**
     * Creates a new SpringTransactionalIdempotencyAwareHandlerExecutorProxy instance.
     *
     * @param handlerExecutorDelegate The TransactionalHandlerExecutor used to execute the handlers transactionally
     * @param idempotencyConfiguration The IdempotencyConfiguration defining settings for idempotency protection
     * @param outbox The TransactionOutbox for handling transactional outbox operations
     * @param platformTransactionManager The PlatformTransactionManager for managing transactions
     */
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
