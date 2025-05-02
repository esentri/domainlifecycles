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

package io.domainlifecycles.mirror.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.domainlifecycles.mirror.api.AccessLevel;
import io.domainlifecycles.mirror.api.AssertedContainableTypeMirror;
import io.domainlifecycles.mirror.api.ValueMirror;
import io.domainlifecycles.mirror.api.ValueReferenceMirror;
import io.domainlifecycles.mirror.exception.MirrorException;

/**
 * Model implementation of a {@link ValueReferenceMirror}.
 *
 * @author Mario Herb
 */
public class ValueReferenceModel extends FieldModel implements ValueReferenceMirror {


    /**
     * Constructs a new instance of {@code ValueReferenceModel}.
     *
     * @param name the name of the value reference
     * @param type the type of the value reference, represented as an {@link AssertedContainableTypeMirror}
     * @param accessLevel the access level of the value reference, as an {@link AccessLevel}
     * @param declaredByTypeName the name of the type that declares this value reference
     * @param modifiable a boolean indicating whether the value reference is modifiable
     * @param publicReadable a boolean indicating whether the value reference is publicly readable
     * @param publicWriteable a boolean indicating whether the value reference is publicly writable
     * @param isStatic a boolean indicating whether the value reference is static
     * @param hidden a boolean indicating whether the value reference is hidden
     */
    @JsonCreator
    public ValueReferenceModel(@JsonProperty("name") String name,
                               @JsonProperty("type") AssertedContainableTypeMirror type,
                               @JsonProperty("accessLevel") AccessLevel accessLevel,
                               @JsonProperty("declaredByTypeName") String declaredByTypeName,
                               @JsonProperty("modifiable") boolean modifiable,
                               @JsonProperty("publicReadable") boolean publicReadable,
                               @JsonProperty("publicWriteable") boolean publicWriteable,
                               @JsonProperty("static") boolean isStatic,
                               @JsonProperty("hidden") boolean hidden
    ) {
        super(name, type, accessLevel, declaredByTypeName, modifiable, publicReadable, publicWriteable, isStatic,
            hidden);
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public ValueMirror getValue() {
        return domainMirror.getDomainTypeMirror(getType().getTypeName())
            .map(e -> (ValueMirror) e)
            .orElseThrow(() -> MirrorException.fail("ValueMirror not found for '%s'", getType().getTypeName()));
    }
}
