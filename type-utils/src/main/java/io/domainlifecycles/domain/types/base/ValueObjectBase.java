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

package io.domainlifecycles.domain.types.base;

import io.domainlifecycles.domain.types.ValueObject;
import io.domainlifecycles.domain.types.companions.ValueObjects;

/**
 * Abstract skeleton implementation of a value object that obeys the general
 * value object contract as defined in {@link ValueObject}.
 *
 * @author Mario Herb
 */
public abstract class ValueObjectBase implements ValueObject {

    /**
     * Constructs a skeleton {@code ValueObject}.
     */
    protected ValueObjectBase() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object o) {
        return o instanceof ValueObject that && ValueObjects.equals(this, that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return ValueObjects.hashCode(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return ValueObjects.toString(this);
    }
}
