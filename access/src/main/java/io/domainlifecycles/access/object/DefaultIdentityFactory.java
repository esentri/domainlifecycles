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

package io.domainlifecycles.access.object;

import io.domainlifecycles.access.classes.ClassProvider;
import io.domainlifecycles.access.exception.DLCAccessException;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.reflect.JavaReflect;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * Default implementation of {@link IdentityFactory}.
 *
 * @author Mario Herb
 */
public class DefaultIdentityFactory implements IdentityFactory{

    private final ClassProvider classProvider;

    public DefaultIdentityFactory(ClassProvider classProvider) {
        this.classProvider = classProvider;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <V, I extends Identity<V>> I newInstance(V value, String identityTypeName) {
        Objects.requireNonNull(identityTypeName);
        Class<I> identityClass = (Class<I>) classProvider.getClassForName(identityTypeName);
        return JavaReflect
            .findConstructor(identityClass, value.getClass())
            .map(constructor -> {
                try {
                    return (I)constructor.newInstance(value);
                } catch (InstantiationException | InvocationTargetException |
                         IllegalAccessException ex) {
                    throw DLCAccessException.fail("Couldn't instantiate Identity (Identity class: %s) for: '%s'.", ex, identityTypeName, value);
                }
            })
            ///
            .orElseThrow(() -> DLCAccessException.fail("Failed to instantiate Identity '%s'.", identityTypeName));
    }
}
