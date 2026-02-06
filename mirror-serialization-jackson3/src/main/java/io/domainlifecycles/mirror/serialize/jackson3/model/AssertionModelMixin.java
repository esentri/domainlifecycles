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

package io.domainlifecycles.mirror.serialize.jackson3.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.domainlifecycles.mirror.model.AssertionType;

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.AssertionModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
public abstract class AssertionModelMixin {

    /**
     * Constructor for the {@code AssertionModelMixin} class, used for configuring
     * deserialization of assertion models via Jackson annotations.
     *
     * @param assertionType The type of assertion being represented. Determines the
     *                       behavior or validation logic associated with the assertion.
     * @param param1         The first parameter for the assertion. The specific
     *                       usage of this parameter depends on the assertion type.
     * @param param2         The second parameter for the assertion. The specific
     *                       usage of this parameter depends on the assertion type.
     * @param message        The message associated with the assertion. Typically
     *                       used to convey information or error details.
     */
    @JsonCreator
    public AssertionModelMixin(
        @JsonProperty("assertionType") AssertionType assertionType,
        @JsonProperty("param1") String param1,
        @JsonProperty("param2") String param2,
        @JsonProperty("message") String message
    ) {}
}
