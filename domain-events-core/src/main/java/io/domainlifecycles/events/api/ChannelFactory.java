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

/**
 * The ChannelFactory interface represents a factory for creating channels for publishing and consuming domain events.
 *
 * @author Mario Herb
 */
public interface ChannelFactory {

    /**
     * Returns a ProcessingChannel object for consuming domain events.
     * A consume only channel cannot be used to publish domain events.
     *
     * @param channelName The name of the channel to consume domain events from
     * @return A ProcessingChannel object for consuming domain events
     */
    ConsumingOnlyChannel consumeOnlyChannel(String channelName);

    /**
     * Returns a ProcessingChannel object for publishing domain events.
     * A consume only channel cannot be used to consume domain events.
     *
     * @param channelName The name of the channel to publish domain events to
     * @return A ProcessingChannel object for publishing domain events
     */
    PublishingOnlyChannel publishOnlyChannel(String channelName);

    /**
     * Returns a ProcessingChannel object for publishing and consuming domain events.
     *
     * @param channelName The name of the channel
     * @return A ProcessingChannel object for processing domain events
     */
    ProcessingChannel processingChannel(String channelName);
}
