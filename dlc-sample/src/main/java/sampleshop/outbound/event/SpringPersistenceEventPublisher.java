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

package sampleshop.outbound.event;

import nitrox.dlc.persistence.repository.PersistenceEventPublisher;
import nitrox.dlc.persistence.repository.actions.PersistenceAction;
import org.springframework.context.ApplicationEventPublisher;


/**
 * The event publisher separates event dispatch mechanics
 * from the client logic that triggers a persistence event.
 *
 * @author Mario Herb
 */
public final class SpringPersistenceEventPublisher implements PersistenceEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public SpringPersistenceEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * Publishes NitroX DLC persistence actions on the Spring application event bus.
     * For every DML operation applied an action is emitted.
     *
     * @param pa the {@link PersistenceAction}
     */
    public void publish(PersistenceAction<?> pa) {
        applicationEventPublisher.publishEvent(pa);
    }

}

