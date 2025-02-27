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
 *  Copyright 2019-2025 the original author or authors.
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

package io.domainlifecycles.events.mq.api;

import io.domainlifecycles.events.api.CloseableChannel;
import io.domainlifecycles.events.api.ProcessingChannel;

/**
 * Represents an abstract Message Queue Processing Channel.
 *
 * @author Mario Herb
 */
public class MqProcessingChannel extends ProcessingChannel implements CloseableChannel {

    private final MqConsumingConfiguration consumingConfiguration;
    private final MqPublishingConfiguration publishingConfiguration;

    /**
     * Initializes a new Abstract MQ (Message Queue) Processing Channel with the given name, consuming configuration,
     * and publishing configuration.
     *
     * @param name The name of the channel.
     * @param consumingConfiguration The consuming configuration for the channel.
     * @param publishingConfiguration The publishing configuration for the channel.
     */
    public MqProcessingChannel(
        String name,
        MqConsumingConfiguration consumingConfiguration,
        MqPublishingConfiguration publishingConfiguration
    ) {
        super(name, publishingConfiguration, consumingConfiguration);
        this.consumingConfiguration = consumingConfiguration;
        this.publishingConfiguration = publishingConfiguration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        consumingConfiguration.close();
        publishingConfiguration.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MqConsumingConfiguration getConsumingConfiguration() {
        return consumingConfiguration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MqPublishingConfiguration getPublishingConfiguration() {
        return publishingConfiguration;
    }
}
