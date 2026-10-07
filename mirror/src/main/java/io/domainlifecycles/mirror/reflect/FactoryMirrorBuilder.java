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

package io.domainlifecycles.mirror.reflect;

import io.domainlifecycles.domain.types.Factory;
import io.domainlifecycles.mirror.api.AccessLevel;
import io.domainlifecycles.mirror.api.FactoryMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.model.FactoryModel;
import io.domainlifecycles.mirror.resolver.GenericTypeResolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Builder to create {@link FactoryMirror}. Uses Java reflection.
 *
 * @author Mario Herb
 */
public class FactoryMirrorBuilder extends ServiceKindMirrorBuilder<FactoryMirror> {

    private static final Logger log = LoggerFactory.getLogger(FactoryMirrorBuilder.class);

    private final Class<? extends Factory> factoryClass;

    /**
     * Constructor
     *
     * @param factoryClass class being mirrored
     * @param genericTypeResolver type Resolver implementation, that resolves generics and type arguments
     * @param domainTypeDetector domain type detector implementation, that detects domain types
     */
    public FactoryMirrorBuilder(
        Class<? extends Factory> factoryClass,
        GenericTypeResolver genericTypeResolver,
        DomainTypeDetector domainTypeDetector
    ) {
        super(factoryClass, genericTypeResolver, domainTypeDetector);
        this.factoryClass = factoryClass;
    }

    /**
     * Creates a new {@link FactoryMirror}.
     *
     * @return new instance of FactoryMirror
     */
    @Override
    public FactoryMirror build() {
        var methods = buildMethods();
        var notCreating = methods.stream()
            .filter(method -> AccessLevel.PUBLIC.equals(method.getAccessLevel())
                && getTypeName().equals(method.getDeclaredByTypeName())
                && !method.isFactoryMethod())
            .map(MethodMirror::getName)
            .toList();
        if (!notCreating.isEmpty()) {
            log.warn("The factory {} has public methods creating no domain object, i.e. responsibilities beyond "
                + "creating: {}", getTypeName(), notCreating);
        }
        return new FactoryModel(
            getTypeName(),
            isAbstract(),
            buildFields(),
            methods,
            factoryInterfaceTypeNames(),
            buildInheritanceHierarchy(),
            buildInterfaceTypes()
        );
    }

    /**
     * {@inheritDoc}
     *
     * @return {@code true}: a factory only creates domain objects
     */
    @Override
    protected boolean createsDomainObjectsOnly() {
        return true;
    }

    private List<String> factoryInterfaceTypeNames() {
        return Arrays.stream(factoryClass.getInterfaces())
            .filter(
                i -> Factory.class.isAssignableFrom(i) && !i.getName().equals(Factory.class.getName()))
            .map(Class::getName)
            .toList();
    }
}
