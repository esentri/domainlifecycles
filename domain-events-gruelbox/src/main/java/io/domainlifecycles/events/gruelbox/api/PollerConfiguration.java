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

/**
 * Represents the configuration for a Poller polling the transaction outbox, to process the provided domain events.
 *
 * @author Mario Herb
 */
public class PollerConfiguration {

    private static final long CONSUMING_POLLER_DELAY_MS_DEFAULT = 3000;
    private static final long CONSUMING_POLLER_PERIOD_MS_DEFAULT = 1000;

    private final long pollerDelayMs;
    private final long pollerPeriodMs;

    /**
     * Represents the configuration for a Poller controlling the time delay and period for polling.
     *
     * @param pollerDelayMs The delay before the Poller starts polling, in milliseconds.
     * @param pollerPeriodMs The interval between each poll operation, in milliseconds.
     */
    public PollerConfiguration(
        long pollerDelayMs,
        long pollerPeriodMs
    ) {
        this.pollerDelayMs = pollerDelayMs;
        this.pollerPeriodMs = pollerPeriodMs;
    }

    /**
     * Represents the configuration for a Poller controlling the time delay and period for polling.
     */
    public PollerConfiguration(){
        this(CONSUMING_POLLER_DELAY_MS_DEFAULT, CONSUMING_POLLER_PERIOD_MS_DEFAULT);
    }

    /**
     * Retrieves the delay (in milliseconds) configured for the Poller before it starts polling.
     *
     * @return The delay before the Poller starts polling, in milliseconds.
     */
    public long getPollerDelayMs() {
        return pollerDelayMs;
    }

    /**
     * Retrieves the interval between each poll operation in milliseconds.
     *
     * @return The interval between each poll operation, in milliseconds.
     */
    public long getPollerPeriodMs() {
        return pollerPeriodMs;
    }
}
