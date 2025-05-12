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

package io.domainlifecycles.mirror.api;

import java.util.List;

/**
 * Represents a mirror interface that aggregates various functionalities for service kinds in a domain architecture context.
 * It extends multiple mirror interfaces to combine the ability to mirror domain types, process domain commands,
 * and handle domain events.
 *
 * @author Mario Herb
 */
public interface ServiceKindMirror extends DomainTypeMirror, DomainCommandProcessingMirror, DomainEventProcessingMirror {

    /**
     * @return the list of referenced {@link ServiceKindMirror} instances.
     */
    List<ServiceKindMirror> getReferencedServiceKinds();

    /**
     * @return the list of referenced {@link RepositoryMirror} instances.
     */
    List<RepositoryMirror> getReferencedRepositories();

    /**
     * @return the list of referenced {@link DomainServiceMirror} instances.
     */
    List<DomainServiceMirror> getReferencedDomainServices();

    /**
     * @return the list of referenced {@link OutboundServiceMirror} instances.
     */
    List<OutboundServiceMirror> getReferencedOutboundServices();

    /**
     * @return the list of referenced {@link QueryHandlerMirror} instances.
     */
    List<QueryHandlerMirror> getReferencedQueryHandlers();

    /**
     * @return the list of referenced {@link ApplicationServiceMirror} instances.
     */
    List<ApplicationServiceMirror> getReferencedApplicationServices();

}
