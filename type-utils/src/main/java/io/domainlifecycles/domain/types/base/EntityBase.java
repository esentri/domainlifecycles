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

package io.domainlifecycles.domain.types.base;


import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.companions.Entities;

/**
 * Abstract skeleton implementation of an {@link Entity}.
 * <p>
 * Typically, used to implement standard entities, i.e. when class inheritance
 * is not required to represent domain-specific entity hierarchies.
 *
 * @param <ID> type of entity identifier
 * @author Mario Herb
 */
public abstract class EntityBase<ID extends Identity<?>> implements Entity<ID> {

    /**
     * The "version" of this entity according to the underlying
     * optimistic concurrency model.
     */
    @ConcurrencyVersion
    long concurrencyVersion;

    /**
     * Constructs a skeleton {@code Entity} with the
     * default {@code concurrencyVersion} set to zero.
     */
    protected EntityBase() {
        this(0L);
    }

    /**
     * Constructs a skeleton {@code Entity} with the
     * given {@code concurrencyVersion}.
     *
     * @param concurrencyVersion used initially.
     */
    protected EntityBase(final long concurrencyVersion) {
        this.concurrencyVersion = concurrencyVersion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public final ID id() {
        return (ID) Entities.id(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long concurrencyVersion() {
        return this.concurrencyVersion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object o) {
        return o instanceof Entity<?> that && this.id().equals(that.id());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Entities.hashCode(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return Entities.toString(this);
    }
}
