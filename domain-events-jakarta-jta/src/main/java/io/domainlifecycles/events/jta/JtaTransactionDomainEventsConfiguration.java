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

package io.domainlifecycles.events.jta;

import io.domainlifecycles.events.api.DomainEventsConfiguration;
import io.domainlifecycles.events.api.NonTransactionDefaultDomainEventsConfiguration;
import io.domainlifecycles.events.jta.publish.DirectJtaTransactionalDomainEventPublisher;
import io.domainlifecycles.events.jta.receive.execution.handler.JtaTransactionalHandlerExecutor;
import io.domainlifecycles.services.api.ServiceProvider;
import jakarta.transaction.TransactionManager;

/**
 * JtaTransactionDomainEventsConfiguration class provides a static method
 * to configure and create a DomainEventsConfiguration instance with JTA transaction support.
 *
 * The configuration method takes a {@link TransactionManager}, {@link ServiceProvider},
 * and a boolean flag indicating whether to publish events after the transaction is committed.
 *
 * @author Mario Herb
 */
public class JtaTransactionDomainEventsConfiguration {

    /**
     * Default DomainEventsConfiguration for JTA transaction handling.
     *
     * @param transactionManager
     * @param serviceProvider
     * @param publishAfterCommit
     * @return
     */
    static DomainEventsConfiguration configuration(
        TransactionManager transactionManager,
        ServiceProvider serviceProvider,
        boolean publishAfterCommit
    ){
        var baseConfig = NonTransactionDefaultDomainEventsConfiguration.configuration(serviceProvider);
        var builder = baseConfig.toBuilder();
        var publisher = new DirectJtaTransactionalDomainEventPublisher(baseConfig.getReceivingDomainEventHandler(), transactionManager, publishAfterCommit);
        return builder
            .withDomainEventPublisher(publisher)
            .withHandlerExecutor(new JtaTransactionalHandlerExecutor(transactionManager))
            .build();
    }

}
