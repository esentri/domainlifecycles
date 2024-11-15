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

package io.domainlifecycles.events.api;

import java.util.Objects;

/**
 * Represents a channel that consumes domain events and provides a name.
 *
 * @author Mario Herb
 */
public class ConsumingOnlyChannel implements ConsumingChannel, NamedChannel{

    private final String name;
    private final ConsumingConfiguration consumingConfiguration;

    /**
     * Initializes a new ConsumingOnlyChannel with the provided name and consuming configuration.
     *
     * @param name The name of the consuming channel
     * @param consumingConfiguration The configuration for consuming domain events
     */
    public ConsumingOnlyChannel(String name, ConsumingConfiguration consumingConfiguration) {
        this.name = Objects.requireNonNull(name, "A name is required!");
        this.consumingConfiguration = Objects.requireNonNull(consumingConfiguration, "A ConsumingConfiguration is required!");

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConsumingConfiguration getConsumingConfiguration() {
        return consumingConfiguration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }
}
