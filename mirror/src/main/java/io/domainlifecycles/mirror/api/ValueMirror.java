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

package io.domainlifecycles.mirror.api;

import java.util.Optional;

/**
 * A ValueMirror is either an {@link EnumMirror}, a {@link ValueObjectMirror} or an {@link IdentityMirror}.
 *
 * @author Mario Herb
 */
public interface ValueMirror extends DomainTypeMirror {

    /**
     * @return true, if the ValueMirror is an EnumMirror.
     */
    default boolean isEnum() {
        return this instanceof EnumMirror;
    }

    /**
     * @return true, if the ValueMirror is a ValueObjectMirror.
     */
    default boolean isValueObject() {
        return this instanceof ValueObjectMirror;
    }

    /**
     * @return true, if the ValueMirror is an IdentityMirror.
     */
    default boolean isIdentity() {
        return this instanceof IdentityMirror;
    }

    /**
     * @return the full qualified type name being mirrored.
     */
    String getTypeName();

    /**
     * @return true, if the Value contains only exactly one field, that contains the associated value.
     * Identities or Enums are singleValued by definition.
     */
    boolean isSingledValued();

    /**
     * @return the field mirror for the only field that might contain a value. If the ValueO is not singleValued
     * the returned Optional is empty.
     */
    Optional<FieldMirror> singledValuedField();
}
