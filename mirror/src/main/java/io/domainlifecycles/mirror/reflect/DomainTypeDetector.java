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

import io.domainlifecycles.mirror.api.DomainType;

import java.lang.reflect.Type;

/**
 * Interface for detecting the {@link DomainType} of a given type.
 * This is typically used to classify types in domain-driven design (DDD)
 * contexts into categories such as entities, value objects, aggregate roots,
 * and various service types.
 *
 * The {@code detectDomainType} method should return the corresponding
 * {@link DomainType} for the provided type. If the type does not map to a
 * recognized domain type, the method should return {@code DomainType.NON_DOMAIN}.
 *
 * @author Mario Herb
 */
public interface DomainTypeDetector {

    /**
     * Detects the {@link DomainType} of the provided type. This method is used to classify the given type
     * into a category based on domain-driven design (DDD) principles, such as entity, value object, aggregate root,
     * or other domain-related classifications. If the type does not correspond to any known domain type,
     * {@link DomainType#NON_DOMAIN} is returned.
     *
     * @param type the type to be analyzed and classified into a specific {@link DomainType}.
     *             This can represent any class or interface within the application context.
     * @return the detected {@link DomainType}, representing the classification of the provided type.
     *         Returns {@link DomainType#NON_DOMAIN} if the type does not belong to a recognized domain classification.
     */
    DomainType detectDomainType(Type type);
}
