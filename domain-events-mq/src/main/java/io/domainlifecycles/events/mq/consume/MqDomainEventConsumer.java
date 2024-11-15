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

package io.domainlifecycles.events.mq.consume;

/**
 * An interface representing a consumer for handling domain events from a message broker.
 *
 * @author Mario Herb
 */
public interface MqDomainEventConsumer {

    /**
     * Closes all resources or connections to a message broker held by the implementing class.
     */
    void closeAll();

    /**
     * Pauses handling of a specific domain event by a given handler method in the specified class.
     * This method allows pausing the execution of a specific handler method for a particular domain event type.
     *
     * @param handlerClassName the class name of the handler method to be paused
     * @param handlerMethodName the name of the handler method to be paused
     * @param domainEventTypeName the type of domain event for which the handler should be paused
     */
    void pauseHandler(String handlerClassName, String handlerMethodName, String domainEventTypeName);

    /**
     * Resumes handling of a specific domain event by a given handler method in the specified class.
     *
     * @param handlerClassName the class name of the handler method to be resumed
     * @param handlerMethodName the name of the handler method to be resumed
     * @param domainEventTypeName the type of domain event for which the handler should be resumed
     */
    void resumeHandler(String handlerClassName, String handlerMethodName, String domainEventTypeName);

    /**
     * Checks if the handler method for a specific domain event in a given class is currently paused.
     *
     * @param handlerClassName the class name of the handler method to check
     * @param handlerMethodName the name of the handler method to check
     * @param domainEventTypeName the type of domain event to check for handler pause status
     * @return true if the handler method is currently paused, false otherwise
     */
    public boolean isHandlerPaused(String handlerClassName, String handlerMethodName, String domainEventTypeName);

    /**
     * Initializes the consumer by establishing necessary connections and configuration.
     */
    void initialize();
}
