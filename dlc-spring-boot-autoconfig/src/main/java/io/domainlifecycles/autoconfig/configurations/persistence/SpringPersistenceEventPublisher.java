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

package io.domainlifecycles.autoconfig.configurations.persistence;

import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import io.domainlifecycles.persistence.repository.actions.PersistenceAction;
import org.springframework.context.ApplicationEventPublisher;

/**
 * An implementation of {@link PersistenceEventPublisher} that publishes {@link PersistenceAction}
 * events using Spring's {@link ApplicationEventPublisher}.
 *
 * This class serves as a bridge between the domain-specific persistence events represented
 * by {@link PersistenceAction} and the Spring event publishing infrastructure. It allows
 * persistence actions to be published and managed within the context of a Spring application.
 *
 * @author Mario Herb
 */
public class SpringPersistenceEventPublisher implements PersistenceEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    /**
     * Constructs a new instance of {@code SpringPersistenceEventPublisher}.
     *
     * @param eventPublisher the {@link ApplicationEventPublisher} used to publish persistence events;
     *                       must not be null
     */
    public SpringPersistenceEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    /**
     * Publishes a {@link PersistenceAction} using the configured {@link ApplicationEventPublisher}.
     *
     * @param action the {@link PersistenceAction} to be published; must not be null
     */
    @Override
    public void publish(PersistenceAction<?> action) {
        eventPublisher.publishEvent(action);
    }
}
