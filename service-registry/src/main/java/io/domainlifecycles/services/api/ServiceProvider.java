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

package io.domainlifecycles.services.api;

import io.domainlifecycles.domain.types.ServiceKind;

/**
 * The ServiceProvider interface represents a service provider that is responsible for providing instances of various
 * types of services.
 * It allows retrieving instances of ApplicationService, Repository, and DomainService based on their type name.
 *
 * @author Mario Herb
 */
public interface ServiceProvider {

    /**
     * Retrieves an instance of {@link ServiceKind} based on the given type name.
     *
     * @param typeName The full qualified name of the type representing the OutboundService.
     * @return An instance of ServiceKind based on the given type name.
     */
    <S extends ServiceKind> S getServiceKindInstance(String typeName);
}
