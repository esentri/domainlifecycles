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
import io.domainlifecycles.mirror.api.EntityMirror;

/**
 * Jackson Mixin for {@link io.domainlifecycles.mirror.model.EntityReferenceModel}.
 * Controls serialization without modifying the actual model class.
 *
 * @author leonvoellinger
 */
@Deprecated
public abstract class EntityReferenceModelMixin extends FieldModelMixin {

    /**
     * Constructor for the EntityReferenceModelMixin class.
     * This is used by Jackson for deserialization of the model.
     *
     * @param name the name of the entity reference
     * @param type the type of the entity reference, represented by an AssertedContainableTypeMirror
     * @param accessLevel the access level of the entity reference, represented by an AccessLevel
     * @param declaredByTypeName the name of the type declaring the entity reference
     * @param modifiable whether the entity reference is modifiable
     * @param publicReadable whether the entity reference is publicly readable
     * @param publicWriteable whether the entity reference is publicly writable
     * @param isStatic whether the entity reference is static
     * @param hidden whether the entity reference is hidden
     */
    @JsonCreator
    public EntityReferenceModelMixin(
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
     * @return the {@link EntityMirror} representing the mirrored Entity.
     *         May provide insights into the Entity's identity, concurrency version,
     *         references to other entities, and aggregate roots, among other details.
     */
    @JsonIgnore
    public abstract EntityMirror getEntity();
}
