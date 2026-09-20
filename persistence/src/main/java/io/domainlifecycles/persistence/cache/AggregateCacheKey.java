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

package io.domainlifecycles.persistence.cache;

import io.domainlifecycles.domain.types.Identity;

import java.util.Objects;

/**
 * Identifies a single aggregate root instance within the {@link TransactionCache}.
 * <p>
 * The aggregate root type is carried alongside the {@link Identity} rather than relying on the identity
 * value alone, because nothing guarantees that two different aggregate root types never use the same
 * underlying identity value (e.g. both a {@code Long} based {@code OrderId} and a {@code Long} based
 * {@code CustomerId} could hold the value {@code 1}). The root type is represented by its fully qualified
 * class name (rather than a {@code Class} reference), because that is what can be derived both from an
 * already fetched aggregate instance and, without one at hand, from an {@link Identity} alone via
 * {@code Domain.entityMirrorForIdentityTypeName(...)} - the same lookup {@code JooqAggregateFetcher} and
 * {@code JdbcAggregateFetcher} already use to resolve a root's table/record by id.
 *
 * @param rootTypeName the fully qualified class name of the aggregate root type
 * @param id           the identity of the aggregate root instance
 * @author Mario Herb
 */
public record AggregateCacheKey(String rootTypeName, Identity<?> id) {

    /**
     * Creates a new AggregateCacheKey.
     *
     * @param rootTypeName the fully qualified class name of the aggregate root type
     * @param id           the identity of the aggregate root instance
     */
    public AggregateCacheKey {
        Objects.requireNonNull(rootTypeName);
        Objects.requireNonNull(id);
    }
}
