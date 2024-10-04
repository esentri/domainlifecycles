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

import io.domainlifecycles.events.api.ConsumingConfiguration;
import io.domainlifecycles.events.consume.execution.handler.HandlerExecutor;
import io.domainlifecycles.events.gruelbox.dispatch.GruelboxDomainEventDispatcher;
import io.domainlifecycles.events.gruelbox.poll.GruelboxPoller;

/**
 * The GruelboxConsumingConfiguration class represents a configuration for consuming domain events using the Gruelbox
 * transaction outbox framework.
 * It defines the necessary components and settings required for consuming domain events efficiently.
 *
 * @author Mario Herb
 */
class GruelboxConsumingConfiguration implements ConsumingConfiguration {

    private final GruelboxPoller gruelboxPoller;
    private final GruelboxDomainEventDispatcher gruelboxDomainEventDispatcher;
    private final HandlerExecutor usedHandlerExecutor;

    /**
     * Constructs a new GruelboxConsumingConfiguration with the specified components.
     *
     * @param gruelboxPoller The GruelboxPoller instance to be associated with this configuration.
     * @param gruelboxDomainEventDispatcher The GruelboxDomainEventDispatcher instance to be associated with this configuration.
     * @param usedHandlerExecutor The HandlerExecutor instance to be used for executing domain event listeners.
     */
    public GruelboxConsumingConfiguration(GruelboxPoller gruelboxPoller, GruelboxDomainEventDispatcher gruelboxDomainEventDispatcher, HandlerExecutor usedHandlerExecutor) {
        this.gruelboxPoller = gruelboxPoller;
        this.gruelboxDomainEventDispatcher = gruelboxDomainEventDispatcher;
        this.usedHandlerExecutor = usedHandlerExecutor;
    }

    /**
     * Retrieves the {@link GruelboxPoller} instance associated with this consuming configuration.
     *
     * @return The {@link GruelboxPoller} instance.
     */
    public GruelboxPoller getGruelboxPoller() {
        return gruelboxPoller;
    }

    /**
     * Retrieves the GruelboxDomainEventDispatcher instance associated with this consuming configuration.
     *
     * @return The GruelboxDomainEventDispatcher instance.
     */
    public GruelboxDomainEventDispatcher getGruelboxDomainEventDispatcher() {
        return gruelboxDomainEventDispatcher;
    }

    /**
     * Retrieves the HandlerExecutor instance that is currently being used for executing domain event listeners.
     * This HandlerExecutor is responsible for handling listener methods within the provided execution context.
     *
     * @return The HandlerExecutor instance used for executing domain event listeners.
     */
    public HandlerExecutor getUsedHandlerExecutor() {
        return usedHandlerExecutor;
    }
}
