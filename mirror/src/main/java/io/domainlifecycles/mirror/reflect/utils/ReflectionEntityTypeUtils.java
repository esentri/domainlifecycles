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
 *  Copyright 2019-2025 the original author or authors.
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

package io.domainlifecycles.mirror.reflect.utils;

import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.mirror.reflect.GenericInterfaceTypeResolver;
import io.domainlifecycles.reflect.JavaReflect;
import io.domainlifecycles.reflect.MemberSelect;

import java.lang.reflect.Field;
import java.util.Optional;

/**
 * Utility class that contains general reflection cases used for analyzing
 * {@link Entity} classes.
 *
 * @author Mario Herb
 */
public class ReflectionEntityTypeUtils {

    /**
     * Returning the field containing the entity id
     *
     * @param entityClass to be analyzed
     * @return the field containing the entity id or empty Optional, if not found
     */
    public static Optional<Field> identityField(Class<? extends Entity<? extends Identity<?>>> entityClass) {
        var resolver = new GenericInterfaceTypeResolver(entityClass);
        var identityType = getIdentityType(resolver);

        if (identityType.isPresent()) {
            var idPropertyFieldCandidates = JavaReflect.fields(entityClass, MemberSelect.HIERARCHY)
                .stream()
                .filter(
                    f -> identityType.get().isAssignableFrom(f.getType()) || Identity.class.getName().equals(
                        f.getType().getName())
                )
                .toList();
            Optional<Field> idProperty;
            if (idPropertyFieldCandidates.size() > 1) {
                idProperty = idPropertyFieldCandidates
                    .stream()
                    .filter(f -> f.isAnnotationPresent(Entity.Id.class))
                    .findFirst();
            } else {
                idProperty = idPropertyFieldCandidates
                    .stream()
                    .findFirst();
            }
            return idProperty;
        }
        return Optional.empty();
    }

    private static Optional<Class<? extends Identity<?>>> getIdentityType(
        GenericInterfaceTypeResolver  resolver
    ) {
        var resolved = resolver.resolveFor(Entity.class, 0);
        return Optional.ofNullable((Class<? extends Identity<?>>) resolved);
    }
}
