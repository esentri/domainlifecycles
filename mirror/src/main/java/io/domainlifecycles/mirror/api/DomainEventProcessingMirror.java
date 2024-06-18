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

package io.domainlifecycles.mirror.api;

import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.domain.types.ListensTo;
import io.domainlifecycles.domain.types.Publishes;

/**
 * All domain types mirrored, that can process {@link DomainEvent} instances, implement this interface.
 *
 * @author Mario Herb
 */
public interface DomainEventProcessingMirror {

    /**
     * Query, if a certain DomainEvent for the corresponding {@link DomainEventMirror} is published.
     * To be more precise, if the mirrored type contains a method, which is annotated to do so {@link Publishes}.
     * @return true, if the underlying mirrored instance publishes the corresponding DomainEvent
     */
    boolean publishes(DomainEventMirror domainEvent);

    /**
     * Query, if a certain DomainEvent for the corresponding {@link DomainEventMirror} is listened to.
     * To be more precise, if the mirrored type contains a method, which is annotated to do so {@link ListensTo}.
     * @return true, if the underlying mirrored instance listens to the corresponding DomainEvent
     */
    boolean listensTo(DomainEventMirror domainEvent);
}
