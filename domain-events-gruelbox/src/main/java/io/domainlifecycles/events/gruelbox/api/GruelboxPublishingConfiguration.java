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

package io.domainlifecycles.events.gruelbox.api;

import com.gruelbox.transactionoutbox.TransactionOutbox;
import io.domainlifecycles.events.api.PublishingConfiguration;
import io.domainlifecycles.events.gruelbox.publish.GruelboxDomainEventPublisher;
import io.domainlifecycles.events.publish.DomainEventPublisher;
import java.util.Objects;

/**
 * Represents the configuration for Gruelbox based publishing of Domain Events, responsible for handling the publishing of domain events.
 * It implements the PublishingConfiguration interface.
 *
 * @author Mario Herb
 */
class GruelboxPublishingConfiguration implements PublishingConfiguration {

    private final TransactionOutbox transactionOutbox;
    private final GruelboxDomainEventPublisher gruelboxDomainEventPublisher;

    /**
     * Creates a new GruelboxPublishingConfiguration
     *
     * @param transactionOutbox
     * @param publishingSchedulerConfiguration
     */
    GruelboxPublishingConfiguration(
        TransactionOutbox transactionOutbox,
        PublishingSchedulerConfiguration publishingSchedulerConfiguration
    ) {
        this.transactionOutbox = Objects.requireNonNull(transactionOutbox, "A TransactionOutbox is required!");
        this.gruelboxDomainEventPublisher = new GruelboxDomainEventPublisher(
            this.transactionOutbox,
            publishingSchedulerConfiguration
        );
    }

    /**
     * Retrieves the DomainEventPublisher associated with this PublishingConfiguration.
     * The DomainEventPublisher is used to publish domain events with the underlying technical event bus
     * (Gruelbox transaction outbox).
     *
     * @return The DomainEventPublisher for this configuration.
     */
    @Override
    public DomainEventPublisher domainEventPublisher() {
        return gruelboxDomainEventPublisher;
    }

    /**
     * Retrieves the TransactionOutbox associated with this PublishingConfiguration.
     *
     * @return The TransactionOutbox for this configuration.
     */
    public TransactionOutbox getTransactionOutbox() {
        return transactionOutbox;
    }
}
