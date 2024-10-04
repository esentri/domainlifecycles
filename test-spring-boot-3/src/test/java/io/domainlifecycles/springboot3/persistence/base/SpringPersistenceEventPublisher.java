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

package io.domainlifecycles.springboot3.persistence.base;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import io.domainlifecycles.persistence.repository.actions.PersistenceAction;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import tests.shared.events.PersistenceEvent;


/**
 * The event publisher separates event dispatch mechanics
 * from the client logic that triggers a persistence event.
 */
@Slf4j
@Component
public final class SpringPersistenceEventPublisher implements PersistenceEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public SpringPersistenceEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publish(@NonNull PersistenceAction pa) {
        final PersistenceEvent event = assembleEvent(pa);
        log.debug("Publish persistence event: {}", event);
        applicationEventPublisher.publishEvent(event);
    }

    /**
     * Assembles a persistence event object for the given domain entity, tagged with the
     * given event type that refers to the kind of CUD operation that was applied on
     * the entity. The remaining event parameters are taken from the current activation
     * context.
     *
     * @return a new persistence event.
     */
    private PersistenceEvent assembleEvent(final PersistenceAction persistenceAction) {
        PersistenceEvent.PersistenceEventType eventType = PersistenceEvent.PersistenceEventType.INSERTED;
        if (persistenceAction.actionType.equals(
            PersistenceAction.ActionType.UPDATE) || persistenceAction.actionType.equals(
            PersistenceAction.ActionType.DELETE_UPDATE)) {
            eventType = PersistenceEvent.PersistenceEventType.UPDATED;
        } else if (persistenceAction.actionType.equals(PersistenceAction.ActionType.DELETE)) {
            eventType = PersistenceEvent.PersistenceEventType.DELETED;
        }

        return new PersistenceEvent(persistenceAction.instanceAccessModel, eventType);
    }
}
