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

package io.domainlifecycles.diagram.domain.mapper;

import io.domainlifecycles.diagram.domain.config.DomainDiagramConfig;
import io.domainlifecycles.mirror.api.AggregateRootMirror;
import io.domainlifecycles.mirror.api.ApplicationServiceMirror;
import io.domainlifecycles.mirror.api.DomainCommandMirror;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.api.DomainServiceMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.OutboundServiceMirror;
import io.domainlifecycles.mirror.api.QueryHandlerMirror;
import io.domainlifecycles.mirror.api.ReadModelMirror;
import io.domainlifecycles.mirror.api.RepositoryMirror;
import io.domainlifecycles.mirror.api.ServiceKindMirror;

import java.util.List;

/**
 * The FilteredDomainClasses class represents a collection of filtered domain classes based on certain criteria
 * (based on the given diagram configuration).
 * It provides separate lists for various types of domain classes like application services, domain commands, domain
 * events,
 * domain services, repositories, aggregate roots, query handlers and read models.
 *
 * @author Mario Herb
 */
public class FilteredDomainClasses {

    private final DomainMirror domainMirror;

    private final DomainDiagramConfig domainDiagramConfig;

    private final List<DomainCommandMirror> domainCommands;

    private final List<DomainEventMirror> domainEvents;

    private final List<AggregateRootMirror> aggregateRoots;

    private final List<ReadModelMirror> readModels;

    private final List<ServiceKindMirror> serviceKinds;

    private final TransitiveDomainTypeAnPackageFilter transitiveDomainTypeAnPackageFilter;

    /**
     * Constructs a new instance of FilteredDomainClasses with the given configuration and domain mirror.
     *
     * @param domainDiagramConfig the configuration defining filters for domain diagrams
     * @param domainMirror the representation of the domain used to extract and filter domain elements
     */
    public FilteredDomainClasses(DomainDiagramConfig domainDiagramConfig, DomainMirror domainMirror) {
        this.domainDiagramConfig = domainDiagramConfig;
        this.domainMirror = domainMirror;

        transitiveDomainTypeAnPackageFilter = new TransitiveDomainTypeAnPackageFilter(
            domainMirror,
            domainDiagramConfig.getDiagramTrimSettings().getExplicitlyIncludedPackageNames(),
            domainDiagramConfig.getDiagramTrimSettings().getTransitiveFilterSeedDomainServiceTypeNames()
        );
        this.domainCommands = initFilteredDomainCommands();
        this.domainEvents = initFilteredDomainEvents();
        this.aggregateRoots = initFilteredAggregateRoots();
        this.readModels = initFilteredReadModels();
        this.serviceKinds = initFilteredServiceKinds();

    }

    private List<ServiceKindMirror> initFilteredServiceKinds() {
        return domainMirror
            .getAllServiceKindMirrors()
            .stream()
            .filter(s -> !s.isAbstract() || domainDiagramConfig.getGeneralVisualSettings().isShowAbstractTypes())
            .filter(s ->
                (s.getDomainType().equals(DomainType.REPOSITORY) && domainDiagramConfig.getGeneralVisualSettings().isShowRepositories() && !s.getTypeName().equals("io.domainlifecycles.jooq.imp.JooqAggregateRepository"))
                || (s.getDomainType().equals(DomainType.APPLICATION_SERVICE) && domainDiagramConfig.getGeneralVisualSettings().isShowApplicationServices())
                || (s.getDomainType().equals(DomainType.DOMAIN_SERVICE) && domainDiagramConfig.getGeneralVisualSettings().isShowDomainServices())
                || (s.getDomainType().equals(DomainType.OUTBOUND_SERVICE) && domainDiagramConfig.getGeneralVisualSettings().isShowOutboundServices())
                || (s.getDomainType().equals(DomainType.QUERY_HANDLER) && domainDiagramConfig.getGeneralVisualSettings().isShowQueryHandlers())
                || (s.getDomainType().equals(DomainType.SERVICE_KIND) && domainDiagramConfig.getGeneralVisualSettings().isShowUnspecifiedServiceKinds())
            )
            .filter(transitiveDomainTypeAnPackageFilter::filter)
            .filter(s ->
                !domainDiagramConfig.getDiagramTrimSettings().getClassesBlacklist().contains(s.getTypeName())
                && s.getAllInterfaceTypeNames().stream().noneMatch(it -> domainDiagramConfig.getDiagramTrimSettings().getClassesBlacklist().contains(it)))
            .toList();
    }

    /**
     * Checks whether the current list of service kinds contains a specific {@code ServiceKindMirror}.
     * The comparison is based on the type name or the interface type names of the {@code ServiceKindMirror}.
     *
     * @param serviceKindMirror the {@code ServiceKindMirror} to check for existence in the current list
     * @return {@code true} if the current list contains the specified {@code ServiceKindMirror} based on the type name
     *         or interface type names; {@code false} otherwise
     */
    public boolean contains(ServiceKindMirror serviceKindMirror){
        return this.serviceKinds.stream().anyMatch(
            s -> s.getTypeName().equals(serviceKindMirror.getTypeName())
            || s.getAllInterfaceTypeNames().contains(serviceKindMirror.getTypeName())
        );
    }

    /**
     * Checks whether the given {@code AggregateRootMirror} exists within the current collection
     * of aggregate roots.
     *
     * The method iterates through the collection of aggregate roots and determines if any match
     * the specified {@code AggregateRootMirror} based on equality.
     *
     * @param aggregateRootMirror the {@code AggregateRootMirror} to check for existence in the collection
     * @return {@code true} if the specified {@code AggregateRootMirror} is present in the collection;
     *         {@code false} otherwise
     */
    public boolean contains(AggregateRootMirror aggregateRootMirror){
        return this.aggregateRoots.stream().anyMatch(a -> a.equals(aggregateRootMirror));
    }

    /**
     * Checks whether the current collection of read models contains the specified {@code ReadModelMirror}.
     *
     * This method iterates through the list of read models and determines if any match the given
     * {@code ReadModelMirror} based on equality.
     *
     * @param readModelMirror the {@code ReadModelMirror} to check for existence in the collection
     * @return {@code true} if the specified {@code ReadModelMirror} is present in the collection;
     *         {@code false} otherwise
     */
    public boolean contains(ReadModelMirror readModelMirror){
        return this.readModels.stream().anyMatch(r -> r.equals(readModelMirror));
    }

    private List<DomainCommandMirror> initFilteredDomainCommands() {
        return domainMirror
            .getAllDomainCommandMirrors()
            .stream()
            .filter(dc -> !dc.isAbstract() || domainDiagramConfig.getGeneralVisualSettings().isShowAbstractTypes())
            .filter(dc -> domainDiagramConfig.getGeneralVisualSettings().isShowDomainCommands())
            .filter(transitiveDomainTypeAnPackageFilter::filter)
            .filter(dc -> !domainDiagramConfig.getDiagramTrimSettings().getClassesBlacklist().contains(dc.getTypeName()))
            .toList();
    }

    private List<DomainEventMirror> initFilteredDomainEvents() {
        return domainMirror
            .getAllDomainEventMirrors()
            .stream()
            .filter(de -> !de.isAbstract() || domainDiagramConfig.getGeneralVisualSettings().isShowAbstractTypes())
            .filter(dc -> domainDiagramConfig.getGeneralVisualSettings().isShowDomainEvents())
            .filter(transitiveDomainTypeAnPackageFilter::filter)
            .filter(de -> !domainDiagramConfig.getDiagramTrimSettings().getClassesBlacklist().contains(de.getTypeName()))
            .toList();
    }

    private List<ReadModelMirror> initFilteredReadModels() {
        return domainMirror
            .getAllReadModelMirrors()
            .stream()
            .filter(r -> !r.isAbstract() || domainDiagramConfig.getGeneralVisualSettings().isShowAbstractTypes())
            .filter(r -> domainDiagramConfig.getGeneralVisualSettings().isShowReadModels())
            .filter(transitiveDomainTypeAnPackageFilter::filter)
            .filter(r -> !domainDiagramConfig.getDiagramTrimSettings().getClassesBlacklist().contains(r.getTypeName()))
            .toList();
    }

    private List<AggregateRootMirror> initFilteredAggregateRoots() {
        return domainMirror
            .getAllAggregateRootMirrors()
            .stream()
            .filter(ar -> !ar.isAbstract() || domainDiagramConfig.getGeneralVisualSettings().isShowAbstractTypes())
            .filter(r -> domainDiagramConfig.getGeneralVisualSettings().isShowAggregates())
            .filter(transitiveDomainTypeAnPackageFilter::filter)
            .filter(ar -> !domainDiagramConfig.getDiagramTrimSettings().getClassesBlacklist().contains(ar.getTypeName()))
            .toList();
    }

    /**
     * Retrieves the list of filtered {@link DomainCommandMirror} instances associated with the domain.
     *
     * These instances represent domain commands identified and filtered based on the domain mirror
     * and filter configuration. Domain commands are used to model intent within the system and represent
     * the actions to be performed.
     *
     * @return a list of {@link DomainCommandMirror} instances representing the filtered domain commands.
     */
    public List<DomainCommandMirror> getDomainCommands() {
        return domainCommands;
    }

    /**
     * Retrieves the list of filtered {@link DomainEventMirror} instances associated with the domain.
     *
     * These mirrors represent the domain events within the system that are identified
     * and filtered based on the domain mirror and filter configuration.
     *
     * @return a list of {@link DomainEventMirror} instances representing the filtered domain events.
     */
    public List<DomainEventMirror> getDomainEvents() {
        return domainEvents;
    }

    /**
     * Retrieves the list of filtered {@link AggregateRootMirror} instances associated with the domain.
     *
     * These mirrors represent the aggregate roots within the system, identified and filtered
     * based on the domain mirror and filter configuration. Aggregate roots are the primary entities
     * responsible for maintaining the consistency boundary in the domain model.
     *
     * @return a list of {@link AggregateRootMirror} instances representing the filtered aggregate roots.
     */
    public List<AggregateRootMirror> getAggregateRoots() {
        return aggregateRoots;
    }

    /**
     * Retrieves the list of filtered {@link ReadModelMirror} instances associated with the domain.
     *
     * These mirrors represent the read models within the system, identified and filtered based on the
     * domain mirror and filter configuration. A read model is typically used for querying and presenting
     * data in the domain.
     *
     * @return a list of {@link ReadModelMirror} instances representing the filtered read models.
     */
    public List<ReadModelMirror> getReadModels() {
        return readModels;
    }

    /**
     * Retrieves the list of filtered {@link ServiceKindMirror} instances associated with the domain.
     *
     * These instances represent various service kinds identified and filtered based on the domain
     * mirror and filter configuration. Service kinds may include application services, domain services,
     * repositories, or other categorized service types defined in the domain.
     *
     * @return a list of {@link ServiceKindMirror} instances representing the filtered service kinds.
     */
    public List<ServiceKindMirror> getServiceKinds() {
        return serviceKinds;
    }

    /**
     * Retrieves the list of filtered {@link ApplicationServiceMirror} instances associated with the domain.
     *
     * These instances represent application services identified and filtered based on the domain mirror
     * and filter configuration. Application services typically contain application-specific business logic
     * and orchestrate domain operations.
     *
     * @return a list of {@link ApplicationServiceMirror} instances representing the filtered application services.
     */
    public List<ApplicationServiceMirror>getApplicationServices() {
        return serviceKinds
            .stream()
            .filter(s -> s.getDomainType().equals(DomainType.APPLICATION_SERVICE))
            .map(s -> (ApplicationServiceMirror) s)
            .toList();
    }

    /**
     * Retrieves the list of filtered {@link DomainServiceMirror} instances from the available service kinds.
     *
     * The method filters service kinds to include only those with a domain type of {@link DomainType#DOMAIN_SERVICE}.
     * These filtered instances are then cast to {@link DomainServiceMirror}.
     *
     * @return a list of {@link DomainServiceMirror} instances representing the filtered domain services.
     */
    public List<DomainServiceMirror> getDomainServices() {
        return serviceKinds
            .stream()
            .filter(s -> s.getDomainType().equals(DomainType.DOMAIN_SERVICE))
            .map(s -> (DomainServiceMirror) s)
            .toList();
    }

    /**
     * Retrieves the list of filtered {@link RepositoryMirror} instances from the available service kinds.
     *
     * The method filters service kinds to include only those with a domain type of {@link DomainType#REPOSITORY}.
     * These filtered instances are then cast to {@link RepositoryMirror}.
     *
     * @return a list of {@link RepositoryMirror} instances representing the filtered repositories.
     */
    public List<RepositoryMirror> getRepositories() {
        return serviceKinds
            .stream()
            .filter(s -> s.getDomainType().equals(DomainType.REPOSITORY))
            .map(s -> (RepositoryMirror) s)
            .toList();
    }

    /**
     * Retrieves the list of filtered {@link QueryHandlerMirror} instances from the available service kinds.
     *
     * The method filters service kinds to include only those with a domain type of {@link DomainType#QUERY_HANDLER}.
     * These filtered instances are then cast to {@link QueryHandlerMirror}.
     *
     * @return a list of {@link QueryHandlerMirror} instances representing the filtered query handlers.
     */
    public List<QueryHandlerMirror> getQueryHandlers() {
        return serviceKinds
            .stream()
            .filter(s -> s.getDomainType().equals(DomainType.QUERY_HANDLER))
            .map(s -> (QueryHandlerMirror) s)
            .toList();
    }

    /**
     * Retrieves the list of filtered {@link OutboundServiceMirror} instances.
     *
     * This method filters the collection of service kinds by selecting only those with a
     * domain type of {@link DomainType#OUTBOUND_SERVICE}. The filtered service kinds are
     * then cast to {@link OutboundServiceMirror}.
     *
     * @return a list of {@link OutboundServiceMirror} instances representing the filtered outbound services.
     */
    public List<OutboundServiceMirror> getOutboundServices() {
        return serviceKinds
            .stream()
            .filter(s -> s.getDomainType().equals(DomainType.OUTBOUND_SERVICE))
            .map(s -> (OutboundServiceMirror) s)
            .toList();
    }

    /**
     * Retrieves the list of unspecified {@link ServiceKindMirror} instances from the available service kinds.
     *
     * This method filters the collection of service kinds to include only those with a domain type of
     * {@link DomainType#SERVICE_KIND}. These filtered instances represent service kinds that are not categorized
     * into a more specific domain type.
     *
     * @return a list of {@link ServiceKindMirror} instances where the domain type is {@link DomainType#SERVICE_KIND},
     *         representing unspecified service kinds.
     */
    public List<ServiceKindMirror> getUnspecifiedServiceKinds() {
        return serviceKinds
            .stream()
            .filter(s -> s.getDomainType().equals(DomainType.SERVICE_KIND))
            .toList();
    }

}

