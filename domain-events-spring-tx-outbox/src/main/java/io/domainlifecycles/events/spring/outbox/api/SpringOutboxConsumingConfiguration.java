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

package io.domainlifecycles.events.spring.outbox.api;

import io.domainlifecycles.events.api.ConsumingConfiguration;
import io.domainlifecycles.events.spring.outbox.poll.AbstractOutboxPoller;

/**
 * The SpringOutboxConsumingConfiguration class implements the ConsumingConfiguration interface and represents a configuration for consuming domain events using Spring.
 * It contains an AbstractOutboxPoller instance to handle the polling of the outbox and sending domain events to consumers.
 *
 * This class provides a constructor to initialize the SpringOutboxConsumingConfiguration with an AbstractOutboxPoller instance.
 * It also exposes a method to retrieve the underlying AbstractOutboxPoller for further customization if needed.
 *
 * @author Mario Herb
 */
@Deprecated
public class SpringOutboxConsumingConfiguration implements ConsumingConfiguration {

    private final AbstractOutboxPoller outboxPoller;

    /**
     * Sets the AbstractOutboxPoller for the SpringOutboxConsumingConfiguration.
     *
     * @param outboxPoller the AbstractOutboxPoller instance for handling outbox polling and sending domain events
     */
    SpringOutboxConsumingConfiguration(AbstractOutboxPoller outboxPoller) {
        this.outboxPoller = outboxPoller;
    }

    /**
     * Returns the AbstractOutboxPoller instance associated with this configuration.
     *
     * @return the AbstractOutboxPoller instance for handling outbox polling and sending domain events
     */
    public AbstractOutboxPoller getOutboxPoller() {
        return outboxPoller;
    }
}
