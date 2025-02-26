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

package io.domainlifecycles.events.spring.outbox.api;

import io.domainlifecycles.events.api.PublishingConfiguration;
import io.domainlifecycles.events.publish.DomainEventPublisher;
import io.domainlifecycles.events.spring.outbox.SpringOutboxDomainEventPublisher;

import java.util.Objects;

/**
 * Represents a SpringOutbox configuration class for publishing domain events using an outbox pattern.
 *
 * @author Mario Herb
 */
@Deprecated
public class SpringOutboxPublishingConfiguration implements PublishingConfiguration {

    private final SpringOutboxDomainEventPublisher springOutboxDomainEventPublisher;

    /**
     * Constructs a SpringOutboxPublishingConfiguration with the provided SpringOutboxDomainEventPublisher.
     *
     * @param springOutboxDomainEventPublisher the SpringOutboxDomainEventPublisher to set for this configuration
     */
    SpringOutboxPublishingConfiguration(SpringOutboxDomainEventPublisher springOutboxDomainEventPublisher) {
        this.springOutboxDomainEventPublisher = Objects.requireNonNull(springOutboxDomainEventPublisher, "SpringOutboxDomainEventPublisher is required!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomainEventPublisher domainEventPublisher() {
        return springOutboxDomainEventPublisher;
    }
}
