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

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.FieldModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
@Deprecated
public abstract class FieldModelMixin {

    /**
     * Constructor for the FieldModelMixin, used to configure Jackson serialization
     * and deserialization behavior for the FieldModel class.
     *
     * @param name the name of the field being modeled.
     * @param type the type mirror representing the type of the field.
     * @param accessLevel the access level of the field (e.g., public, private, etc.).
     * @param declaredByTypeName the name of the type declaring this field.
     * @param modifiable indicates whether the field is modifiable after initialization.
     * @param publicReadable indicates whether the field is readable outside its defining class.
     * @param publicWriteable indicates whether the field is writable outside its defining class.
     * @param isStatic indicates whether the field is declared as static.
     * @param hidden indicates whether the field should be hidden from serialization.
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
     * Mixin method declaration. Ignored for serialization purposes.
     *
     * @return true if the field is an identity field, false otherwise
     */
    @JsonIgnore
    public abstract boolean isIdentityField();
}
