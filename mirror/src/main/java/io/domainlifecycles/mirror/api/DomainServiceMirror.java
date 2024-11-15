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

import io.domainlifecycles.domain.types.DomainService;
import io.domainlifecycles.domain.types.Repository;

import java.util.List;

/**
 * A DomainServiceMirror mirrors {@link DomainService} instances.
 *
 * @author Mario Herb
 */
public interface DomainServiceMirror extends ServiceKindMirror {


    /**
     * @return the list of mirrors for the {@link Repository} instances referenced by the DomainService.
     */
    List<RepositoryMirror> getReferencedRepositories();

    /**
     * @return the list of mirrors for the {@link DomainService} instances referenced by the DomainService.
     */
    List<DomainServiceMirror> getReferencedDomainServices();

    /**
     * @return the interface type names (full qualified class names) that the mirrored DomainService implements.
     * The interfaces therefore must extend {@link DomainService}.
     * If separation of concerns is respected, we have at most only one interface, which fulfills that condition.
     */
    List<String> getDomainServiceInterfaceTypeNames();

    /**
     * @return the list of referenced {@link OutboundServiceMirror} instances.
     */
    List<OutboundServiceMirror> getReferencedOutboundServices();

    /**
     * @return the list of referenced {@link QueryClientMirror} instances.
     */
    List<QueryClientMirror> getReferencedQueryClients();

}
