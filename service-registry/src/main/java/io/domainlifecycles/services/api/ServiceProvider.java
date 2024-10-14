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

import io.domainlifecycles.domain.types.AggregateRoot;
import io.domainlifecycles.domain.types.ApplicationService;
import io.domainlifecycles.domain.types.DomainService;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.OutboundService;
import io.domainlifecycles.domain.types.QueryClient;
import io.domainlifecycles.domain.types.Repository;
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
     * Retrieves an instance of {@link ApplicationService} based on the given type name.
     *
     * @param typeName The full qualified name of the type representing the ApplicationService.
     * @return An instance of ApplicationService based on the given type name.
     */
    ApplicationService getApplicationServiceInstance(String typeName);

    /**
     * Retrieves an instance of {@link Repository} based on the given type name.
     *
     * @param <A>      type of AggregateRoot
     * @param <ID>     type of AggregateRoot's Identity
     * @param typeName The full qualified name of the type representing the Repository.
     * @return An instance of Repository based on the given type name.
     */
    <ID extends Identity<?>, A extends AggregateRoot<ID>> Repository<ID, A> getRepositoryInstance(String typeName);

    /**
     * Retrieves an instance of {@link DomainService} based on the given type name.
     *
     * @param typeName The full qualified name of the type representing the DomainService.
     * @return An instance of DomainService based on the given type name.
     */
    DomainService getDomainServiceInstance(String typeName);

    /**
     * Retrieves an instance of {@link QueryClient} based on the given type name.
     *
     * @param typeName The full qualified name of the type representing the QueryClient.
     * @return An instance of QueryClient based on the given type name.
     */
    QueryClient getQueryClientInstance(String typeName);

    /**
     * Retrieves an instance of {@link OutboundService} based on the given type name.
     *
     * @param typeName The full qualified name of the type representing the OutboundService.
     * @return An instance of QueryClient based on the given type name.
     */
    OutboundService getOutboundServiceInstance(String typeName);

    /**
     * Retrieves an instance of {@link ServiceKind} based on the given type name.
     *
     * @param typeName The full qualified name of the type representing the OutboundService.
     * @return An instance of ServiceKind based on the given type name.
     */
    ServiceKind getServiceKindInstance(String typeName);
}
