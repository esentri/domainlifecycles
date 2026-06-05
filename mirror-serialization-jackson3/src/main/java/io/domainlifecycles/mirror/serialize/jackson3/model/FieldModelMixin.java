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

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.FieldModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
public abstract class FieldModelMixin {

    /**
     * Constructor for the FieldModelMixin class, which is used to define a Jackson Mixin
     * for controlling deserialization of the FieldModel.
     *
     * @param name the name of the field
     * @param type the type of the field, represented as an AssertedContainableTypeMirror
     * @param accessLevel the access level of the field, represented as an AccessLevel
     * @param declaredByTypeName the type name of the class where the field is declared
     * @param modifiable indicates whether the field is modifiable
     * @param publicReadable indicates whether the field is readable by the public
     * @param publicWriteable indicates whether the field is writable by the public
     * @param isStatic indicates whether the field is static
     * @param hidden indicates whether the field is hidden
     */
    @JsonCreator
    public FieldModelMixin(
        @JsonProperty("name") String name,
        @JsonProperty("type") io.domainlifecycles.mirror.api.AssertedContainableTypeMirror type,
        @JsonProperty("accessLevel") io.domainlifecycles.mirror.api.AccessLevel accessLevel,
        @JsonProperty("declaredByTypeName") String declaredByTypeName,
        @JsonProperty("modifiable") boolean modifiable,
        @JsonProperty("publicReadable") boolean publicReadable,
        @JsonProperty("publicWriteable") boolean publicWriteable,
        @JsonProperty("static") boolean isStatic,
        @JsonProperty("hidden") boolean hidden
    ) {}

    /**
     * Determines if the field is an identity field. An identity field typically
     * represents a unique identifier or primary key for the entity or record
     * to which the field belongs. Ignored during serialization.
     *
     * @return {@code true} if the field is an identity field, {@code false} otherwise.
     */
    @JsonIgnore
    public abstract boolean isIdentityField();
}
