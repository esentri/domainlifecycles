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

package io.domainlifecycles.events.spring.outbox.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.domainlifecycles.events.api.DomainEventsConfiguration;
import io.domainlifecycles.events.spring.api.SpringTransactionDomainEventsConfiguration;
import io.domainlifecycles.events.spring.outbox.SpringOutboxDomainEventPublisher;
import io.domainlifecycles.events.spring.outbox.api.TransactionalOutbox;
import io.domainlifecycles.events.spring.outbox.impl.SpringJdbcOutbox;
import io.domainlifecycles.events.spring.outbox.poll.AbstractOutboxPoller;
import io.domainlifecycles.events.spring.outbox.poll.DirectOutboxPoller;
import io.domainlifecycles.services.api.ServiceProvider;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Optional;


public final class SpringOutboxConfiguration {

    private final DomainEventsConfiguration domainEventsConfiguration;
    private final TransactionalOutbox transactionalOutbox;
    private final AbstractOutboxPoller outboxPoller;
    private final PlatformTransactionManager platformTransactionManager;
    private final ObjectMapper objectMapper;
    private final DataSource dataSource;


    public SpringOutboxConfiguration(DataSource dataSource,
                                     PlatformTransactionManager platformTransactionManager,
                                     ObjectMapper objectMapper,
                                     ServiceProvider serviceProvider
                                     ) {
        this.dataSource = Objects.requireNonNull(dataSource, "A DataSource is required!");
        this. platformTransactionManager = Objects.requireNonNull(platformTransactionManager, "A PlatformTransactionManager is required!");
        this. objectMapper = Objects.requireNonNull(objectMapper, "An ObjectMapper is required!");
        Objects.requireNonNull(serviceProvider, "A ServiceProvider is required!");
        var transactionalOutbox = new SpringJdbcOutbox(dataSource, objectMapper, platformTransactionManager);
        var baseConfiguration = new SpringTransactionDomainEventsConfiguration(platformTransactionManager, serviceProvider, false).getDomainEventsConfiguration();
        var builder = baseConfiguration.toBuilder();
        var domainEventsConfiguration = builder
            .withDomainEventPublisher(
                new SpringOutboxDomainEventPublisher(
                    transactionalOutbox,
                    platformTransactionManager
                )
            )
            .build();

        this.outboxPoller = new DirectOutboxPoller(transactionalOutbox, domainEventsConfiguration.getReceivingDomainEventHandler());
        this.transactionalOutbox = transactionalOutbox;
        this.domainEventsConfiguration = domainEventsConfiguration;
    }

    public SpringOutboxConfiguration(
        TransactionalOutbox transactionalOutbox,
        DomainEventsConfiguration domainEventsConfiguration,
        AbstractOutboxPoller outboxPoller
    ) {
        this.objectMapper = null;
        this.platformTransactionManager = null;
        this.dataSource = null;
        this.transactionalOutbox = Objects.requireNonNull(transactionalOutbox, "A TransactionalOutbox is required!");
        this.domainEventsConfiguration = Objects.requireNonNull(domainEventsConfiguration, "A DomainEventsConfiguration is required!");
        this.outboxPoller = Objects.requireNonNull(outboxPoller, "An OutboxPoller is required!");
    }

    public DomainEventsConfiguration getDomainEventsConfiguration() {
        return domainEventsConfiguration;
    }

    public TransactionalOutbox getTransactionalOutbox() {
        return transactionalOutbox;
    }

    public AbstractOutboxPoller getOutboxPoller() {
        return outboxPoller;
    }

    public Optional<PlatformTransactionManager> getPlatformTransactionManager() {
        return Optional.ofNullable(platformTransactionManager);
    }

    public Optional<ObjectMapper> getObjectMapper() {
        return Optional.ofNullable(objectMapper);
    }

    public Optional<DataSource> getDataSource() {
        return Optional.ofNullable(dataSource);
    }
}
