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

package io.domainlifecycles.mirror.api;

import java.util.List;

/**
 * A BoundedContextMirror mirrors a BoundedContext.
 * Here a Bounded Context is supposed to be implemented within a single dedicated (Java) package.
 *
 * @author Mario Herb
 */
public interface BoundedContextMirror {

    /**
     * Returns the full qualified package name of this mirrored BoundedContext
     */
    String getPackageName();

    /**
     * Returns the list of {@link AggregateRootMirror} instances, contained in the BoundedContext.
     */
    List<AggregateRootMirror> getAggregateRoots();

    /**
     * Returns the list of {@link DomainServiceMirror} instances, contained in the BoundedContext.
     */
    List<DomainServiceMirror> getDomainServices();

    /**
     * Returns the list of {@link ApplicationServiceMirror} instances, associated with the BoundedContext.
     */
    List<ApplicationServiceMirror> getApplicationServices();

    /**
     * Returns the list of {@link RepositoryMirror} instances, contained in the BoundedContext.
     */
    List<RepositoryMirror> getRepositories();

    /**
     * Returns the list of {@link ReadModelMirror} instances, associated with the BoundedContext.
     */
    List<ReadModelMirror> getReadModels();

    /**
     * Returns the list of {@link QueryClientMirror} instances, associated with the BoundedContext.
     */
    List<QueryClientMirror> getQueryClients();

    /**
     * Returns the list of {@link OutboundServiceMirror} instances, associated with the BoundedContext.
     */
    List<OutboundServiceMirror> getOutboundServices();

    /**
     * Returns the list of {@link DomainCommandMirror} instances of DomainCommands processed within the BoundedContext.
     */
    List<DomainCommandMirror> getDomainCommands();

    /**
     * Returns the list of {@link DomainEventMirror} instances of DomainEvents processed within the BoundedContext.
     */
    List<DomainEventMirror> getDomainEvents();

}
