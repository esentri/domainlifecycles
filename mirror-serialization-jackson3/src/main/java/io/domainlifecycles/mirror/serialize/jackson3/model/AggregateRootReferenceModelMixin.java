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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.domainlifecycles.mirror.api.AccessLevel;
import io.domainlifecycles.mirror.api.AggregateRootMirror;
import io.domainlifecycles.mirror.api.AssertedContainableTypeMirror;

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.AggregateRootReferenceModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
public abstract class AggregateRootReferenceModelMixin extends FieldModelMixin {

    /**
     * Constructor for the AggregateRootReferenceModelMixin class, which serves as a Jackson Mixin
     * for controlling deserialization of the AggregateRootReferenceModel class.
     *
     * @param name the name of the aggregate root reference
     * @param type the type of the aggregate root reference, represented as an AssertedContainableTypeMirror
     * @param accessLevel the access level of the aggregate root reference, represented as an AccessLevel
     * @param declaredByTypeName the name of the type declaring the aggregate root reference
     * @param modifiable indicates whether the aggregate root reference is modifiable
     * @param publicReadable indicates whether the aggregate root reference is readable by the public
     * @param publicWriteable indicates whether the aggregate root reference is writable by the public
     * @param isStatic indicates whether the aggregate root reference is static
     * @param hidden indicates whether the aggregate root reference is hidden
     */
    @JsonCreator
    public AggregateRootReferenceModelMixin(
        @JsonProperty("name") String name,
        @JsonProperty("type") AssertedContainableTypeMirror type,
        @JsonProperty("accessLevel") AccessLevel accessLevel,
        @JsonProperty("declaredByTypeName") String declaredByTypeName,
        @JsonProperty("modifiable") boolean modifiable,
        @JsonProperty("publicReadable") boolean publicReadable,
        @JsonProperty("publicWriteable") boolean publicWriteable,
        @JsonProperty("static") boolean isStatic,
        @JsonProperty("hidden") boolean hidden
    ) {
        super(name, type, accessLevel, declaredByTypeName, modifiable, publicReadable, publicWriteable, isStatic, hidden);
    }

    /**
     * Retrieves the aggregate root associated with this model.
     * This method is ignored during JSON serialization.
     *
     * @return the {@code AggregateRootMirror} representing the aggregate root of the current model
     */
    @JsonIgnore
    public abstract AggregateRootMirror getAggregateRoot();
}
