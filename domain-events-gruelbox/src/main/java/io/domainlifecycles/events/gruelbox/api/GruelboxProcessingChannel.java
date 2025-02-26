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

package io.domainlifecycles.events.gruelbox.api;

import io.domainlifecycles.events.api.CloseableChannel;
import io.domainlifecycles.events.api.ConsumingConfiguration;
import io.domainlifecycles.events.api.ProcessingChannel;
import io.domainlifecycles.events.api.PublishingConfiguration;

/**
 *
 */
public class GruelboxProcessingChannel extends ProcessingChannel implements CloseableChannel {

    /**
     * Represents a channel for publishing and consuming domain events.
     *
     * @param name                    The name of the channel.
     * @param publishingConfiguration The publishing configuration for the channel.
     * @param consumingConfiguration  The consuming configuration for the channel.
     */
    public GruelboxProcessingChannel(String name, PublishingConfiguration publishingConfiguration, ConsumingConfiguration consumingConfiguration) {
        super(name, publishingConfiguration, consumingConfiguration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        var config = (GruelboxConsumingConfiguration)getConsumingConfiguration();
        config.getGruelboxPoller().getScheduler().shutdown();
    }
}
