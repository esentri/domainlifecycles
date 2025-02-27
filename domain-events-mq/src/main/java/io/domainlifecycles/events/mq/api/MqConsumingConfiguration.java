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

import io.domainlifecycles.events.api.ConsumingConfiguration;
import io.domainlifecycles.events.mq.consume.MqDomainEventConsumer;

/**
 * The MqConsumingConfiguration class implements the ConsumingConfiguration interface
 * and represents a configuration for consuming domain events using a Message Queue (MQ) Domain Event Consumer.
 *
 * @author Mario Herb
 */
public class MqConsumingConfiguration implements ConsumingConfiguration {

    private final MqDomainEventConsumer mqDomainEventConsumer;

    /**
     * Constructs a new MqConsumingConfiguration with the provided MqDomainEventConsumer.
     *
     * @param mqDomainEventConsumer the MqDomainEventConsumer used for consuming domain events
     */
    MqConsumingConfiguration(MqDomainEventConsumer mqDomainEventConsumer) {
        this.mqDomainEventConsumer = mqDomainEventConsumer;
    }

    /**
     * Closes the Message Queue Domain Event Consumer associated with this configuration.
     */
    void close(){
        mqDomainEventConsumer.closeAll();
    }

    /**
     * Retrieves the MqDomainEventConsumer associated with this configuration.
     *
     * @return the MqDomainEventConsumer used for consuming domain events
     */
    public MqDomainEventConsumer getMqDomainEventConsumer() {
        return mqDomainEventConsumer;
    }
}
