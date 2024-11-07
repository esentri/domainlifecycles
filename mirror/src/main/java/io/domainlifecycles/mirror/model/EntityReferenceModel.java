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
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.EntityMirror;
import io.domainlifecycles.mirror.api.EntityReferenceMirror;
import io.domainlifecycles.mirror.exception.MirrorException;

/**
 * Model implementation of a {@link EntityReferenceMirror}.
 *
 * @author Mario Herb
 */
public class EntityReferenceModel extends FieldModel implements EntityReferenceMirror {

    @JsonCreator
    public EntityReferenceModel(@JsonProperty("name") String name,
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
    public EntityMirror getEntity() {
        return Domain.typeMirror(getType().getTypeName())
            .map(e -> (EntityMirror) e)
            .orElseThrow(() -> MirrorException.fail("EntityMirror not found for '%s'", getType().getTypeName()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "EntityReferenceModel{} " + super.toString();
    }

}
