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

package io.domainlifecycles.mirror.reflect;

import io.domainlifecycles.mirror.api.NonDomainTypeMirror;
import io.domainlifecycles.mirror.model.NonDomainTypeModel;
import io.domainlifecycles.mirror.resolver.GenericTypeResolver;

/**
 * Builder to create {@link NonDomainTypeMirror}
 *
 * @author Mario Herb
 */
public class NonDomainTypeMirrorBuilder extends DomainTypeMirrorBuilder<NonDomainTypeMirror> {

    /**
     * Constructor
     *
     * @param nonDomainClass class being mirrored
     * @param genericTypeResolver type Resolver implementation, that resolves generics and type arguments
     * @param domainTypeDetector type detector implementation, that detects domain types
     */
    public NonDomainTypeMirrorBuilder(Class<?> nonDomainClass,
                                      GenericTypeResolver genericTypeResolver,
                                      DomainTypeDetector domainTypeDetector) {
        super(nonDomainClass, genericTypeResolver, domainTypeDetector);
    }

    /**
     * Creates a new {@link NonDomainTypeMirror}.
     *
     * @return new instance of NonDomainTypeMirror
     */
    @Override
    public NonDomainTypeMirror build() {
        return new NonDomainTypeModel(
            getTypeName(),
            isAbstract(),
            buildFields(),
            buildMethods(),
            buildInheritanceHierarchy(),
            buildInterfaceTypes()
        );
    }
}
