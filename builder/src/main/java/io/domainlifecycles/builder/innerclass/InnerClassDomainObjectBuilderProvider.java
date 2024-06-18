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

package io.domainlifecycles.builder.innerclass;

import io.domainlifecycles.access.DlcAccess;
import io.domainlifecycles.builder.DomainBuilderConfiguration;
import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.builder.exception.DLCBuilderException;
import io.domainlifecycles.domain.types.internal.DomainObject;

import java.lang.reflect.InvocationTargetException;

/**
 * Provider for inner class domain object builders. Provides instances of DomainObjectBuilder for a given class, given
 * a inner class builder. Can be configured by using {@code DomainBuilderConfiguration}
 *
 * @see DomainObjectBuilderProvider
 *
 * @author Dominik Galler
 */
public final class InnerClassDomainObjectBuilderProvider extends DomainObjectBuilderProvider {

    /**
     * Constructor for default configuration.
     *
     */
    public InnerClassDomainObjectBuilderProvider() {
        this(new InnerClassDefaultDomainBuilderConfiguration());
    }

    /**
     * Constructor to provide custom configuration.
     *
     * @param domainBuilderConfiguration the configuration to use within the {@code CommonDomainObjectBuilder}
     */
    public InnerClassDomainObjectBuilderProvider(
        DomainBuilderConfiguration domainBuilderConfiguration
    ) {
        super(domainBuilderConfiguration);
    }

    /**
     * Provides a new {@link DomainObjectBuilder} instance for the given class.
     *
     * @param domainObjectTypeName The full qualified class name to provide a DomainObjectBuilder for
     * @return a new builder instance
     * @param <T> the DomainObject type for which a new DomianObjectBuilder is provided.
     */
    @Override
    public <T extends DomainObject> DomainObjectBuilder<T> provide(String domainObjectTypeName) {
        Object builderInstance;
        Class<?> clazz = DlcAccess.getClassForName(domainObjectTypeName);
        try {

            var builderMethod = clazz.getDeclaredMethod(getBuilderConfiguration().builderMethodName());
            builderInstance = builderMethod.invoke(null);
        } catch (NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException e) {
            throw DLCBuilderException.fail(
                "Couldn't provide Builder instance for Entity class: '%s'.",
                e,
                domainObjectTypeName,
                e
            );
        }
        return new InnerClassDomainObjectBuilder<>(builderInstance, getBuilderConfiguration());
    }
}
