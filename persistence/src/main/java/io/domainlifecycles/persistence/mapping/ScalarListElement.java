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
 *  Copyright 2019-2026 the original author or authors.
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

package io.domainlifecycles.persistence.mapping;

import io.domainlifecycles.domain.types.ValueObject;

/**
 * Internal, framework-only carrier used to persist a single element of a {@code List<Identity>} or
 * {@code List<Enum>} field (a "scalar list") through the very same access-model / insert / update /
 * delete / diff machinery that already persists {@code List<ValueObject>} fields.
 * <p>
 * A raw {@link io.domainlifecycles.domain.types.Identity} or {@code Enum} value is not a
 * {@link io.domainlifecycles.domain.types.internal.DomainObject} and can therefore not flow through
 * {@code StructuralPosition}/{@code DomainObjectInstanceAccessModel} on its own. Wrapping it in this
 * {@link ValueObject} (equals/hashCode delegate to the record components, i.e. value-based equality,
 * exactly what the existing multiset insert/delete diff in {@code PersistenceContext} already relies
 * on for value object lists) lets every existing VO-list code path recognize and handle it without any
 * change, since those paths dispatch purely on {@code instanceof ValueObject}/{@code instanceof
 * Entity}.
 * <p>
 * This type is never part of a user's domain model and is never returned from a repository; it exists
 * only transiently while an aggregate is being saved or loaded, and is unwrapped back to the raw
 * {@code value} before it is attached to the real {@code List<Identity>}/{@code List<Enum>} field of a
 * rebuilt domain object.
 *
 * @param value           the wrapped raw {@code Identity} or {@code Enum} element
 * @param elementTypeName the full qualified type name of the wrapped element's declared domain type
 *                        (the {@code Identity} or {@code Enum} implementation), used to pick the
 *                        correct child record mapping when a field path could in theory be
 *                        polymorphic
 * @author Mario Herb
 */
public record ScalarListElement<V>(V value, String elementTypeName) implements ValueObject {
}
