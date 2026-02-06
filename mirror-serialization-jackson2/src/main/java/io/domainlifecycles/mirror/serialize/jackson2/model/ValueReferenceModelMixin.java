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
import io.domainlifecycles.mirror.api.AssertedContainableTypeMirror;
import io.domainlifecycles.mirror.api.ValueMirror;

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.ValueReferenceModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
@Deprecated
public abstract class ValueReferenceModelMixin extends FieldModelMixin {

    /**
     * Constructor for the ValueReferenceModelMixin class, utilized to configure
     * deserialization behavior for the {@code ValueReferenceModel} class.
     * This constructor is annotated with {@link JsonCreator} to indicate that it should
     * be used for JSON deserialization.
     *
     * @param name the name of the value reference.
     * @param type an instance of {@link AssertedContainableTypeMirror}, representing the type of the value reference.
     * @param accessLevel the {@link AccessLevel} of the value reference, specifying its visibility constraints.
     * @param declaredByTypeName the name of the type that declares this value reference.
     * @param modifiable a {@code boolean} indicating if the value reference is modifiable.
     * @param publicReadable a {@code boolean} indicating if the value reference is publicly readable.
     * @param publicWriteable a {@code boolean} indicating if the value reference is publicly writable.
     * @param isStatic a {@code boolean} indicating if the value reference is static.
     * @param hidden a {@code boolean} indicating if the value reference is hidden.
     */
    @JsonCreator
    public ValueReferenceModelMixin(
        @JsonProperty("name") String name,
        @JsonProperty("type") AssertedContainableTypeMirror type,
        @JsonProperty("accessLevel") AccessLevel accessLevel,
        @JsonProperty("declaredByTypeName") String declaredByTypeName,
        @JsonProperty("modifiable") boolean modifiable,
        @JsonProperty("publicReadable") boolean publicReadable,
        @JsonProperty("publicWriteable") boolean publicWriteable,
        @JsonProperty("static") boolean isStatic,
        @JsonProperty("hidden") boolean hidden
    ){
        super(name, type, accessLevel, declaredByTypeName, modifiable, publicReadable, publicWriteable, isStatic, hidden);
    }

    /**
     * Retrieves the {@link ValueMirror} associated with this instance.
     * This method is annotated with {@link JsonIgnore}, and therefore will not
     * be included in the JSON serialization or deserialization process.
     *
     * @return the {@link ValueMirror} instance, which may represent an EnumMirror,
     *         ValueObjectMirror, or IdentityMirror.
     */
    @JsonIgnore
    public abstract ValueMirror getValue();
}
