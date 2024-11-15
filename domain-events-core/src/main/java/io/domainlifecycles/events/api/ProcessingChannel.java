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
 * Represents a channel for publishing and consuming domain events.
 *
 * @author Mario Herb
 */
public class ProcessingChannel implements ConsumingChannel, PublishingChannel, NamedChannel{

    private final String name;
    private final PublishingConfiguration publishingConfiguration;
    private final ConsumingConfiguration consumingConfiguration;

    /**
     * Represents a channel for publishing and consuming domain events.
     *
     * @param name The name of the channel.
     * @param publishingConfiguration The publishing configuration for the channel.
     * @param consumingConfiguration The consuming configuration for the channel.
     */
    public ProcessingChannel(String name,
                             PublishingConfiguration publishingConfiguration,
                             ConsumingConfiguration consumingConfiguration) {
        this.name = Objects.requireNonNull(name, "A name is required!");
        this.publishingConfiguration = Objects.requireNonNull(publishingConfiguration, "A PublishingConfiguration is required!");
        this.consumingConfiguration = Objects.requireNonNull(consumingConfiguration, "A ConsumingConfiguration is required!");
    }

    /**
     * Returns the name of the channel.
     *
     * @return The name of the channel.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Retrieves the ConsumingConfiguration associated with this channel.
     *
     * @return the ConsumingConfiguration for consuming domain events
     */
    @Override
    public ConsumingConfiguration getConsumingConfiguration() {
        return this.consumingConfiguration;
    }

    /**
     * Retrieves the publishing configuration associated with this channel.
     *
     * @return the PublishingConfiguration for publishing domain events
     */
    @Override
    public PublishingConfiguration getPublishingConfiguration() {
        return this.publishingConfiguration;
    }
}
