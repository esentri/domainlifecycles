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

import io.domainlifecycles.mirror.api.AccessLevel;
import io.domainlifecycles.mirror.api.AssertedContainableTypeMirror;
import io.domainlifecycles.mirror.api.EntityMirror;
import io.domainlifecycles.mirror.api.EntityReferenceMirror;
import io.domainlifecycles.mirror.exception.MirrorException;

/**
 * Model implementation of a {@link EntityReferenceMirror}.
 *
 * @author Mario Herb
 */
public class EntityReferenceModel extends FieldModel implements EntityReferenceMirror {

    /**
     * Creates an instance of EntityReferenceModel.
     *
     * @param name the name of the entity reference; must not be null.
     * @param type the type of the entity reference, represented by an AssertedContainableTypeMirror; must not be null.
     * @param accessLevel the access level of the entity reference, as specified by the AccessLevel enum; must not be null.
     * @param declaredByTypeName the fully qualified name of the type declaring the entity reference; must not be null.
     * @param modifiable a boolean indicating whether the entity reference is modifiable.
     * @param publicReadable a boolean indicating whether the entity reference is publicly readable.
     * @param publicWriteable a boolean indicating whether the entity reference is publicly writable.
     * @param isStatic a boolean indicating whether the entity reference is static.
     * @param hidden a boolean indicating whether the entity reference is hidden.
     */
    public EntityReferenceModel(String name,
                                AssertedContainableTypeMirror type,
                                AccessLevel accessLevel,
                                String declaredByTypeName,
                                boolean modifiable,
                                boolean publicReadable,
                                boolean publicWriteable,
                                boolean isStatic,
                                boolean hidden
    ) {
        super(name, type, accessLevel, declaredByTypeName, modifiable, publicReadable, publicWriteable, isStatic,
            hidden);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityMirror getEntity() {
        return domainMirror.getDomainTypeMirror(getType().getTypeName())
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