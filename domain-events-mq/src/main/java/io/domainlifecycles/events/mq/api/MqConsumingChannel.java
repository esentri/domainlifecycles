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
import io.domainlifecycles.events.api.ConsumingOnlyChannel;

/**
 * MqConsumingChannel extends ConsumingOnlyChannel and implements CloseableChannel.
 * It represents a channel for consuming domain events using Message Queue (MQ) Domain Event Consumer.
 *
 * @author Mario Herb
 */
public class MqConsumingChannel extends ConsumingOnlyChannel implements CloseableChannel {

    private final MqConsumingConfiguration consumingConfiguration;

    /**
     * Initializes a new MqConsumingChannel with the specified name and consuming configuration.
     *
     * @param name The name of the consuming channel
     * @param consumingConfiguration The configuration for consuming domain events
     */
    public MqConsumingChannel(String name, MqConsumingConfiguration consumingConfiguration) {
        super(name, consumingConfiguration);
        this.consumingConfiguration = consumingConfiguration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        consumingConfiguration.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MqConsumingConfiguration getConsumingConfiguration() {
        return consumingConfiguration;
    }
}
