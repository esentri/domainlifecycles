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

import io.domainlifecycles.domain.types.DomainService;
import io.domainlifecycles.mirror.api.DomainServiceMirror;
import io.domainlifecycles.mirror.model.DomainServiceModel;
import io.domainlifecycles.mirror.resolver.GenericTypeResolver;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Builder to create {@link DomainServiceMirror}. Uses Java reflection.
 *
 * @author Mario Herb
 */
public class DomainServiceMirrorBuilder extends ServiceKindMirrorBuilder<DomainServiceMirror> {

    private final Class<? extends DomainService> domainServiceClass;

    /**
     * Constructor
     *
     * @param domainServiceClass class being mirrored
     * @param genericTypeResolver type Resolver implementation, that resolves generics and type arguments
     */
    public DomainServiceMirrorBuilder(
        Class<? extends DomainService> domainServiceClass,
        GenericTypeResolver genericTypeResolver
    ) {
        super(domainServiceClass, genericTypeResolver);
        this.domainServiceClass = domainServiceClass;
    }

    /**
     * Creates a new {@link DomainServiceMirror}.
     *
     * @return new instance of DomainServiceMirror
     */
    @Override
    public DomainServiceMirror build() {
        return new DomainServiceModel(
                getTypeName(),
                isAbstract(),
                buildFields(),
                buildMethods(),
                domainServiceInterfaceTypeNames(),
                buildInheritanceHierarchy(),
                buildInterfaceTypes()
            );
    }

    private List<String> domainServiceInterfaceTypeNames() {
        return Arrays.stream(domainServiceClass.getInterfaces())
            .filter(i -> DomainService.class.isAssignableFrom(i) && !i.getName().equals(DomainService.class.getName()))
            .map(Class::getName)
            .collect(Collectors.toList());
    }
}
