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

package nitrox.dlc.mirror.api;


import nitrox.dlc.domain.types.QueryClient;

import java.util.List;
/**
 * A DomainCommandMirror mirrors a {@link nitrox.dlc.domain.types.DomainEvent}.
 *
 * @author Mario Herb
 */
public interface DomainEventMirror extends DomainTypeMirror{
    /**
     * Returns a list mirrored fields of the mirrored Command (all fields that are not in the category of other kinds of references).
     */
    List<FieldMirror> getBasicFields();
    /**
     * Returns a list mirrors for value references (references of {@link nitrox.dlc.domain.types.ValueObject}, Enums or {@link nitrox.dlc.domain.types.Identity}) of the mirrored Command.
     */
    List<ValueReferenceMirror> getValueReferences();
    /**
     * Returns a list mirrors for {@link nitrox.dlc.domain.types.Entity} references  of the mirrored Command.
     */
    List<EntityReferenceMirror> getEntityReferences();
    /**
     * Returns a list mirrors for {@link nitrox.dlc.domain.types.AggregateRoot} references of the mirrored Command.
     */
    List<AggregateRootReferenceMirror> getAggregateRootReferences();

    /**
     * Returns a list mirrors for the {@link nitrox.dlc.domain.types.AggregateRoot} instances representing the Aggregates publishing the mirrored event.
     * Therefor the AggregateRoot or any contained DomainObject must have a method being annotated with {@link nitrox.dlc.domain.types.Publishes}.
     */
    List<AggregateRootMirror> getPublishingAggregates();
    /**
     * Returns a list mirrors for the {@link nitrox.dlc.domain.types.DomainService} instances publishing the mirrored event.
     * Therefor the DomainService must have a method being annotated with {@link nitrox.dlc.domain.types.Publishes}.
     */
    List<DomainServiceMirror> getPublishingDomainServices();
    /**
     * Returns a list mirrors for the {@link nitrox.dlc.domain.types.Repository} instances publishing the mirrored event.
     * Therefor the Repository must have a method being annotated with {@link nitrox.dlc.domain.types.Publishes}.
     */
    List<RepositoryMirror> getPublishingRepositories();
    /**
     * Returns a list mirrors for the {@link nitrox.dlc.domain.types.AggregateRoot} instances representing the Aggregates listening to the mirrored event.
     * Therefor the AggregateRoot or nay contained DomainObject must have a method being annotated with {@link nitrox.dlc.domain.types.ListensTo}.
     */
    List<AggregateRootMirror> getListeningAggregates();
    /**
     * Returns a list mirrors for the {@link nitrox.dlc.domain.types.DomainService} instances listening to the mirrored event.
     * Therefor the DomainService must have a method being annotated with {@link nitrox.dlc.domain.types.ListensTo}.
     */
    List<DomainServiceMirror> getListeningDomainServices();

    /**
     * Returns a list mirrors for the {@link nitrox.dlc.domain.types.Repository} instances listening to the mirrored event.
     * Therefor the Repository must have a method being annotated with {@link nitrox.dlc.domain.types.ListensTo}.
     */
    List<RepositoryMirror> getListeningRepositories();

    /**
     * Returns a list mirrors for the {@link nitrox.dlc.domain.types.ApplicationService} instances listening to the mirrored event.
     * Therefor the ApplicationService must have a method being annotated with {@link nitrox.dlc.domain.types.ListensTo}.
     */
    List<ApplicationServiceMirror> getListeningApplicationServices();

    /**
     * Returns a list mirrors for the {@link nitrox.dlc.domain.types.OutboundService} instances listening to the mirrored event.
     * Therefor the OutboundService must have a method being annotated with {@link nitrox.dlc.domain.types.ListensTo}.
     */
    List<OutboundServiceMirror> getListeningOutboundServices();

    /**
     * Returns a list mirrors for the {@link QueryClient} instances listening to the mirrored event.
     * Therefor the QueryClient must have a method being annotated with {@link nitrox.dlc.domain.types.ListensTo}.
     */
    List<QueryClientMirror> getListeningQueryClients();

}
