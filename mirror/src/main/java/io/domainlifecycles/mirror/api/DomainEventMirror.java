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


import io.domainlifecycles.domain.types.AggregateRoot;
import io.domainlifecycles.domain.types.ApplicationService;
import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.domain.types.DomainService;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.ListensTo;
import io.domainlifecycles.domain.types.OutboundService;
import io.domainlifecycles.domain.types.Publishes;
import io.domainlifecycles.domain.types.Repository;
import io.domainlifecycles.domain.types.ValueObject;
import io.domainlifecycles.domain.types.QueryClient;

import java.util.List;
/**
 * A DomainCommandMirror mirrors a {@link DomainEvent}.
 *
 * @author Mario Herb
 */
public interface DomainEventMirror extends DomainTypeMirror{
    /**
     * @return a list mirrored fields of the mirrored Command (all fields that are not in the category of other kinds of references).
     */
    List<FieldMirror> getBasicFields();
    /**
     * @return a list mirrors for value references (references of {@link ValueObject}, Enums or {@link Identity}) of the mirrored Command.
     */
    List<ValueReferenceMirror> getValueReferences();
    /**
     * @return a list mirrors for {@link Entity} references  of the mirrored Command.
     */
    List<EntityReferenceMirror> getEntityReferences();
    /**
     * @return a list mirrors for {@link AggregateRoot} references of the mirrored Command.
     */
    List<AggregateRootReferenceMirror> getAggregateRootReferences();

    /**
     * @return a list mirrors for the {@link AggregateRoot} instances representing the Aggregates publishing the mirrored event.
     * Therefor the AggregateRoot or any contained DomainObject must have a method being annotated with {@link Publishes}.
     */
    List<AggregateRootMirror> getPublishingAggregates();
    /**
     * @return a list mirrors for the {@link DomainService} instances publishing the mirrored event.
     * Therefor the DomainService must have a method being annotated with {@link Publishes}.
     */
    List<DomainServiceMirror> getPublishingDomainServices();
    /**
     * @return a list mirrors for the {@link Repository} instances publishing the mirrored event.
     * Therefor the Repository must have a method being annotated with {@link Publishes}.
     */
    List<RepositoryMirror> getPublishingRepositories();
    /**
     * @return a list mirrors for the {@link AggregateRoot} instances representing the Aggregates listening to the mirrored event.
     * Therefor the AggregateRoot or nay contained DomainObject must have a method being annotated with {@link ListensTo}.
     */
    List<AggregateRootMirror> getListeningAggregates();
    /**
     * @return a list mirrors for the {@link DomainService} instances listening to the mirrored event.
     * Therefor the DomainService must have a method being annotated with {@link ListensTo}.
     */
    List<DomainServiceMirror> getListeningDomainServices();

    /**
     * @return a list mirrors for the {@link Repository} instances listening to the mirrored event.
     * Therefor the Repository must have a method being annotated with {@link ListensTo}.
     */
    List<RepositoryMirror> getListeningRepositories();

    /**
     * @return a list mirrors for the {@link ApplicationService} instances listening to the mirrored event.
     * Therefor the ApplicationService must have a method being annotated with {@link ListensTo}.
     */
    List<ApplicationServiceMirror> getListeningApplicationServices();

    /**
     * @return a list mirrors for the {@link OutboundService} instances listening to the mirrored event.
     * Therefor the OutboundService must have a method being annotated with {@link ListensTo}.
     */
    List<OutboundServiceMirror> getListeningOutboundServices();

    /**
     * @return a list mirrors for the {@link QueryClient} instances listening to the mirrored event.
     * Therefor the QueryClient must have a method being annotated with {@link ListensTo}.
     */
    List<QueryClientMirror> getListeningQueryClients();

}
