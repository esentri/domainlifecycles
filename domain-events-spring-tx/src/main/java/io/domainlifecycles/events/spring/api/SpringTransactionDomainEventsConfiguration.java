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

package io.domainlifecycles.events.spring.api;

import io.domainlifecycles.events.api.DomainEventsConfiguration;
import io.domainlifecycles.events.api.NonTransactionDefaultDomainEventsConfiguration;
import io.domainlifecycles.events.spring.publish.DirectSpringTransactionalDomainEventPublisher;
import io.domainlifecycles.events.spring.receive.execution.handler.SpringTransactionalHandlerExecutor;
import io.domainlifecycles.services.api.ServiceProvider;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Objects;

/**
 * SpringTransactionDomainEventsConfiguration class provides a static method
 * to configure and create a DomainEventsConfiguration instance with Spring transaction support.
 *
 * The configuration method takes a {@link PlatformTransactionManager}, {@link ServiceProvider},
 * and a boolean flag indicating whether to publish events after the transaction is committed.
 *
 * @author Mario Herb
 */
public final class SpringTransactionDomainEventsConfiguration {

    private final PlatformTransactionManager platformTransactionManager;
    private final boolean publishAfterCommit;
    private final DomainEventsConfiguration domainEventsConfiguration;

    /**
     * Default DomainEventsConfiguration for Spring transaction handling.
     * @param transactionManager
     * @param serviceProvider
     * @param publishAfterCommit
     * @return
     */
    public SpringTransactionDomainEventsConfiguration(
        PlatformTransactionManager transactionManager,
        ServiceProvider serviceProvider,
        boolean publishAfterCommit
    ){
        this.platformTransactionManager = Objects.requireNonNull(transactionManager, "A PlatformTransactionManager is required!");
        this.publishAfterCommit = publishAfterCommit;
        var baseConfig = new NonTransactionDefaultDomainEventsConfiguration(serviceProvider).getDomainEventsConfiguration();
        var builder = baseConfig.toBuilder();
        var publisher = new DirectSpringTransactionalDomainEventPublisher(baseConfig.getReceivingDomainEventHandler(), publishAfterCommit);
        this.domainEventsConfiguration = builder
            .withDomainEventPublisher(publisher)
            .withHandlerExecutor(new SpringTransactionalHandlerExecutor(transactionManager))
            .build();
    }

    public PlatformTransactionManager getPlatformTransactionManager() {
        return platformTransactionManager;
    }

    public boolean isPublishAfterCommit() {
        return publishAfterCommit;
    }

    public DomainEventsConfiguration getDomainEventsConfiguration() {
        return domainEventsConfiguration;
    }
}
