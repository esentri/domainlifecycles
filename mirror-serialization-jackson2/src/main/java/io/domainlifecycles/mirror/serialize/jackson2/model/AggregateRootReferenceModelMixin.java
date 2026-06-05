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

package io.domainlifecycles.mirror.serialize.jackson2.model;

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
@Deprecated
public abstract class AggregateRootReferenceModelMixin extends FieldModelMixin {

    /**
     * Constructor for the AggregateRootReferenceModelMixin. Controls deserialization without
     * modifying the actual model class.
     *
     * @param name name of the field
     * @param type type of the field
     * @param accessLevel access level of the field
     * @param declaredByTypeName name of the class that declared this field
     * @param modifiable whether the field can be modified
     * @param publicReadable whether the field is readable from outside the class
     * @param publicWriteable whether the field is writable from outside the class
     * @param isStatic whether the field is static
     * @param hidden whether the field should be hidden from serialization
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
     * Mixin method declaration. Ignored for serialization purposes.
     *
     * @return the {@link AggregateRootMirror}
     */
    @JsonIgnore
    public abstract AggregateRootMirror getAggregateRoot();
}
