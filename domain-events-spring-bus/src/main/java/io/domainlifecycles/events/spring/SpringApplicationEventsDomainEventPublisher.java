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
 *  Copyright 2019-2026 the original author or authors.
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

package io.domainlifecycles.events.spring;

import io.domainlifecycles.domain.types.DomainEvent;

import io.domainlifecycles.events.publish.DomainEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Objects;

/**
 * DomainEventPublisher implementation that publishes domain events using Spring's ApplicationEventPublisher.
 *
 * @author Mario Herb
 */
public class SpringApplicationEventsDomainEventPublisher implements DomainEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(SpringApplicationEventsDomainEventPublisher.class);

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * Constructs a new instance of SpringApplicationEventsDomainEventPublisher.
     *
     * @param applicationEventPublisher the ApplicationEventPublisher used to publish domain events;
     *                                   must not be null
     * @throws NullPointerException if applicationEventPublisher is null
     */
    public SpringApplicationEventsDomainEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = Objects.requireNonNull(applicationEventPublisher, "An ApplicationEventPublisher is required!");
    }

    /**
     * Publishes the given domain event using Spring's ApplicationEventPublisher.
     *
     * @param domainEvent the domain event to be published; must not be null
     * @throws NullPointerException if the given domain event is null
     */
    @Override
    public void publish(DomainEvent domainEvent) {
        log.debug("Publishing DomainEvent {} through Spring ApplicationEventPublisher", domainEvent);
        applicationEventPublisher.publishEvent(domainEvent);
    }
}
