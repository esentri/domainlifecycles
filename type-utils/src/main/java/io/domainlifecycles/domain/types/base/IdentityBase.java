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

import io.domainlifecycles.domain.types.Identity;

import java.util.Objects;

/**
 * Abstract skeleton implementation of an {@link Identity}.
 *
 * @param <V> type of identity value
 *
 * @author Mario Herb
 */
public abstract class IdentityBase<V> implements Identity<V> {

    /**
     * The value that identifies a thread of continuity.
     */
    public final V value;

    /**
     * Constructs a skeleton {@code Identity} with the given {@code value}.
     *
     * @param value fixed to this user-defined identity,
     */
    protected IdentityBase(final V value) {
        this.value = Objects.requireNonNull(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final V value() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final var that = (Identity<?>) o;
        return value.equals(that.value());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return value.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return Identity.toString(this);
    }
}
