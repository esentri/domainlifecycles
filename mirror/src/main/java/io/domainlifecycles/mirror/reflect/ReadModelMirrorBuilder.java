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

import io.domainlifecycles.domain.types.ReadModel;
import io.domainlifecycles.mirror.api.ReadModelMirror;
import io.domainlifecycles.mirror.model.ReadModelModel;
import io.domainlifecycles.mirror.resolver.GenericTypeResolver;

/**
 * Builder to create {@link ReadModelMirror}. Uses Java reflection.
 *
 * @author Mario Herb
 */
public class ReadModelMirrorBuilder extends DomainTypeMirrorBuilder<ReadModelMirror> {

    /**
     * Constructor
     *
     * @param readModelClass class being mirrored
     * @param genericTypeResolver type Resolver implementation, that resolves generics and type arguments
     */
    public ReadModelMirrorBuilder(
        Class<? extends ReadModel> readModelClass,
        GenericTypeResolver genericTypeResolver
    ) {
        super(readModelClass, genericTypeResolver);
    }

    /**
     * Creates a new {@link ReadModelMirror}.
     *
     * @return new instance of ReadModelMirror
     */
    @Override
    public ReadModelMirror build() {
        return new ReadModelModel(
            getTypeName(),
            isAbstract(),
            buildFields(),
            buildMethods(),
            buildInheritanceHierarchy(),
            buildInterfaceTypes()
        );
    }
}
