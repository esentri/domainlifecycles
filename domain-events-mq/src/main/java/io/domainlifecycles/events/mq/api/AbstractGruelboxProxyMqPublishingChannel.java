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

package io.domainlifecycles.events.mq.api;

import io.domainlifecycles.events.api.PublishingOnlyChannel;

/**
 * A specific type of PublishingChannel that represents a channel for publishing domain events using a message broker
 * and proxying the sending process with an outbox to avoid ghost events or event loss.
 *
 * @author Mario Herb
 */
public class AbstractGruelboxProxyMqPublishingChannel extends PublishingOnlyChannel implements CloseableChannel {

    private final AbstractMqPublishingConfiguration abstractMqPublishingConfiguration;

    /**
     * Creates a new AbstractGruelboxProxyMqPublishingChannel.
     *
     * @param name name of the channel
     * @param abstractMqPublishingConfiguration publishing configuration
     */
    public AbstractGruelboxProxyMqPublishingChannel(String name, AbstractMqPublishingConfiguration abstractMqPublishingConfiguration) {
        super(name, abstractMqPublishingConfiguration);
        this.abstractMqPublishingConfiguration = abstractMqPublishingConfiguration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        abstractMqPublishingConfiguration.close();
    }
}