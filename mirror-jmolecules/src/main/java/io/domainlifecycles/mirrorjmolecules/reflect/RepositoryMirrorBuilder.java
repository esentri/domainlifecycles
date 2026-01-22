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

package io.domainlifecycles.mirrorjmolecules.reflect;

import io.domainlifecycles.domain.types.Repository;
import io.domainlifecycles.mirror.api.RepositoryMirror;
import io.domainlifecycles.mirror.model.RepositoryModel;
import io.domainlifecycles.mirror.reflect.GenericInterfaceTypeResolver;
import io.domainlifecycles.mirror.resolver.GenericTypeResolver;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Builder to create {@link RepositoryMirror}. Uses Java reflection.
 *
 * @author Mario Herb
 */
public class RepositoryMirrorBuilder extends ServiceMirrorBuilder {
    private final Class<?> repositoryClass;

    /**
     * Constructor
     *
     * @param repositoryClass class being mirrored
     * @param genericTypeResolver type Resolver implementation, that resolves generics and type arguments
     */
    public RepositoryMirrorBuilder(
        Class<?> repositoryClass,
        GenericTypeResolver genericTypeResolver
    ) {
        super(repositoryClass, genericTypeResolver);
        this.repositoryClass = repositoryClass;
    }

    /**
     * Creates a new {@link RepositoryMirror}.
     *
     * @return new instance of RepositoryMirror
     */
    @Override
    public RepositoryMirror build() {
        return new RepositoryModel(
            getTypeName(),
            isAbstract(),
            buildFields(),
            buildMethods(),
            getManagedAggregateType(repositoryClass).map(Class::getName).orElse(Object.class.getName()),
            repositoryInterfaceTypeNames(),
            buildInheritanceHierarchy(),
            buildInterfaceTypes()
        );
    }

    private static Optional<Class<?>> getManagedAggregateType(Class<?> c) {
        var resolver = new GenericInterfaceTypeResolver(c);
        var resolved = resolver.resolveFor(Repository.class, 1);
        return Optional.ofNullable(resolved);
    }

    private List<String> repositoryInterfaceTypeNames() {
        return Arrays.stream(repositoryClass.getInterfaces())
            .filter(i -> Repository.class.isAssignableFrom(i) && !i.getName().equals(Repository.class.getName()))
            .map(Class::getName)
            .toList();
    }
}
