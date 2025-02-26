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
import io.domainlifecycles.events.api.PublishingOnlyChannel;

/**
 * Represents a channel for publishing domain events via a dedicated message broker infrastructure.
 *
 * @author Mario Herb
 */
public class MqPublishingChannel extends PublishingOnlyChannel implements CloseableChannel {

    private final MqPublishingConfiguration mqPublishingConfiguration;

    /**
     * Constructs an MqPublishingChannel with the provided name and MqPublishingConfiguration.
     *
     * @param name The name of the channel
     * @param mqPublishingConfiguration The MqPublishingConfiguration associated with the channel
     */
    public MqPublishingChannel(String name, MqPublishingConfiguration mqPublishingConfiguration) {
        super(name, mqPublishingConfiguration);
        this.mqPublishingConfiguration = mqPublishingConfiguration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        mqPublishingConfiguration.close();
    }
}
