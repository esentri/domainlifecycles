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

package io.domainlifecycles.mirror.reflect;

import io.domainlifecycles.mirror.model.IdentityModel;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.mirror.api.IdentityMirror;

import java.util.Optional;

/**
 * Builder to create {@link IdentityMirror}. Uses Java reflection.
 *
 * @author Mario Herb
 */
public class IdentityMirrorBuilder extends DomainTypeMirrorBuilder {
    private final Class<? extends Identity<?>> identityClass;

    public IdentityMirrorBuilder(Class<? extends Identity<?>> identityClass) {
        super(identityClass);
        this.identityClass = identityClass;
    }

    /**
     * Creates a new {@link IdentityMirror}.
     *
     * @return new instance of IdentityMirror
     */
    public IdentityMirror build() {
        return new IdentityModel(
            getTypeName(),
            isAbstract(),
            buildFields(),
            buildMethods(),
            idValueType(),
            buildInheritanceHierarchy(),
            buildInterfaceTypes()
        );
    }

    private Optional<String> idValueType() {
        return getValueType(identityClass)
            .map(Class::getName);
    }

    private static Optional<Class<?>> getValueType(Class<? extends Identity<?>> c) {
        var resolver = new GenericInterfaceTypeResolver(c);
        var resolved = resolver.resolveFor(Identity.class, 0);
        return Optional.ofNullable(resolved);
    }

}
