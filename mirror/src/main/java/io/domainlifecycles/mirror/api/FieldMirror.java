/*
 *
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

package io.domainlifecycles.mirror.api;

import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.Identity;

/**
 * A FieldMirror mirrors fields of a mirrored type.
 *
 * @author Mario Herb
 */
public interface FieldMirror {

    /**
     * Returns the name of the mirrored field.
     */
    String getName();

    /**
     * Returns the type of the mirrored field as {@link AssertedContainableTypeMirror}
     */
    AssertedContainableTypeMirror getType();

    /**
     * Returns the full qualified name of the class, that declared this field.
     */
    String getDeclaredByTypeName();

    /**
     * Returns true, if this field is modifiable (in Java non final).
     */
    boolean isModifiable();

    /**
     * Returns the {@link AccessLevel} of the mirrored field.
     */
    AccessLevel getAccessLevel();

    /**
     * Returns true, if this field is readable with public access constraints.
     * E.g. the field is public or there is a public getter.
     */
    boolean isPublicReadable();

    /**
     * Returns true, if this field is writeable with public access constraints.
     * E.g. the field is public or there is a public setter.
     */
    boolean isPublicWriteable();

    /**
     * Returns true, if this field contains the {@link Identity}, in case it is defined by an {@link Entity}.
     */
    boolean isIdentityField();

    /**
     * Returns true, if this field is {@code static}.
     */
    boolean isStatic();

    /**
     * Returns true, if this field is hidden.
     *
     * @return true if the field is hidden, otherwise false
     */
    boolean isHidden();
}
