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

package io.domainlifecycles.events.gruelbox.api;

import java.time.Duration;

/**
 * Represents the configuration for the publishing scheduler,
 * providing settings for scheduling delay and ordering of domain events (if the processing is ordered by the given type).
 *
 * @author Mario Herb
 */
public class PublishingSchedulerConfiguration {

    private static final Duration PUBLISHING_SCHEDULING_DELAY_DEFAULT = Duration.ZERO;
    private static final boolean PUBLISHING_ORDERED_BY_DOMAIN_EVENT_TYPE_DEFAULT = false;

    private final Duration schedulingDelay;
    private final boolean orderedByDomainEventType;

    /**
     * Initializes a new instance of PublishingSchedulerConfiguration with the specified scheduling delay and ordering setting.
     *
     * @param schedulingDelay the duration of the scheduling delay
     * @param orderedByDomainEventType true if events should be ordered by domain event type, false otherwise
     */
    public PublishingSchedulerConfiguration(Duration schedulingDelay, boolean orderedByDomainEventType) {
        this.schedulingDelay = schedulingDelay;
        this.orderedByDomainEventType = orderedByDomainEventType;
    }

    /**
     * Represents the configuration for the publishing scheduler,
     * providing default values for scheduling delay and ordering of domain events.
     */
    public PublishingSchedulerConfiguration(){
        this(PUBLISHING_SCHEDULING_DELAY_DEFAULT, PUBLISHING_ORDERED_BY_DOMAIN_EVENT_TYPE_DEFAULT);
    }

    /**
     * Retrieves the scheduling delay for the publishing scheduler configuration.
     *
     * @return the duration representing the scheduling delay
     */
    public Duration getSchedulingDelay() {
        return schedulingDelay;
    }

    /**
     * Retrieves the current setting indicating if events should be ordered by domain event type.
     *
     * @return true, if event processing is ordered by domain event type, false otherwise
     */
    public boolean isOrderedByDomainEventType() {
        return orderedByDomainEventType;
    }
}
