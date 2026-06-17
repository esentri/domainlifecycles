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

package io.domainlifecycles.mirrorjmolecules.reflect;

import io.domainlifecycles.mirror.api.DomainServiceMirror;
import io.domainlifecycles.mirror.model.DomainServiceModel;
import io.domainlifecycles.mirror.reflect.DomainTypeDetector;
import io.domainlifecycles.mirror.reflect.DomainTypeMirrorBuilder;
import io.domainlifecycles.mirror.resolver.GenericTypeResolver;

import java.util.Collections;
import java.util.List;

/**
 * Builder to create {@link DomainServiceMirror} for JMolecules marked Domain Services. Uses Java reflection.
 *
 * @author Mario Herb
 */
public class DomainServiceMirrorBuilder extends DomainTypeMirrorBuilder<DomainServiceMirror> {

    private final Class<?> domainServiceClass;

    /**
     * Constructor
     *
     * @param domainServiceClass class being mirrored
     * @param genericTypeResolver type Resolver implementation, that resolves generics and type arguments
     * @param domainTypeDetector type detector implementation, that detects domain types
     */
    public DomainServiceMirrorBuilder(
        Class<?> domainServiceClass,
        GenericTypeResolver genericTypeResolver,
        DomainTypeDetector domainTypeDetector
    ) {
        super(domainServiceClass, genericTypeResolver, domainTypeDetector);
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
        return Collections.emptyList();
    }
}
