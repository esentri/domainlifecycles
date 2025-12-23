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

package io.domainlifecycles.events.consume;

import java.util.Objects;

/**
 * The TargetExecutionContext is needed in cases where the consumer service should be known before
 * domain events are processed (Gruelbox).
 *
 * @param handlerTypeName   handler type name
 * @param handlerMethodName handler method name
 *
 * @author Mario Herb
 */
public record TargetExecutionContext(String handlerTypeName, String handlerMethodName) {

    /**
     * Constructor
     * @param handlerTypeName of the target event handler
     * @param handlerMethodName of the target event handler
     */
    public TargetExecutionContext(String handlerTypeName, String handlerMethodName) {
        this.handlerTypeName = Objects.requireNonNull(handlerTypeName, "A handler type name is required!");
        this.handlerMethodName = Objects.requireNonNull(handlerMethodName, "A handler method name is required!");
    }
}
