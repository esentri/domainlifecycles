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

package nitrox.dlc.diagram.domain.mapper;

import lombok.Getter;
import nitrox.dlc.diagram.domain.config.DomainDiagramConfig;
import nitrox.dlc.mirror.api.AggregateRootMirror;
import nitrox.dlc.mirror.api.ApplicationServiceMirror;
import nitrox.dlc.mirror.api.BoundedContextMirror;
import nitrox.dlc.mirror.api.Domain;
import nitrox.dlc.mirror.api.DomainCommandMirror;
import nitrox.dlc.mirror.api.DomainEventMirror;
import nitrox.dlc.mirror.api.DomainServiceMirror;
import nitrox.dlc.mirror.api.OutboundServiceMirror;
import nitrox.dlc.mirror.api.ReadModelMirror;
import nitrox.dlc.mirror.api.ReadModelProviderMirror;
import nitrox.dlc.mirror.api.RepositoryMirror;
import java.util.List;

/**
 * The FilteredDomainClasses class represents a collection of filtered domain classes based on certain criteria
 * (based on the given diagram configuration).
 * It provides separate lists for various types of domain classes like application services, domain commands, domain events,
 * domain services, repositories, aggregate roots, and read models.
 *
 * @author Mario Herb
 */
public class FilteredDomainClasses {

    private final DomainDiagramConfig domainDiagramConfig;

    private final BoundedContextMirror boundedContextMirror;

    @Getter
    private final List<ApplicationServiceMirror> applicationServices;

    @Getter
    private final List<DomainServiceMirror> domainServices;

    @Getter
    private final List<RepositoryMirror> repositories;

    @Getter
    private final List<DomainCommandMirror> domainCommands;

    @Getter
    private final List<DomainEventMirror> domainEvents;

    @Getter
    private final List<AggregateRootMirror> aggregateRoots;

    @Getter
    private final List<ReadModelMirror> readModels;

    @Getter
    private final List<ReadModelProviderMirror> readModelProviders;

    @Getter
    private final List<OutboundServiceMirror> outboundServices;

    private final TransitiveDomainTypeFilter transitiveDomainTypeFilter;

    public FilteredDomainClasses(DomainDiagramConfig domainDiagramConfig) {
        this.domainDiagramConfig = domainDiagramConfig;
        this.boundedContextMirror = Domain.getBoundedContexts()
            .stream()
            .filter(a -> a.getPackageName().equals(domainDiagramConfig.getContextPackageName()))
            .findFirst().orElseThrow(()-> new IllegalStateException(
                String.format("Bounded Context '%s' not found!", domainDiagramConfig.getContextPackageName())
            ));
        transitiveDomainTypeFilter = new TransitiveDomainTypeFilter(
            domainDiagramConfig.getTransitiveFilterSeedDomainServiceTypeNames(),
            boundedContextMirror
        );
        this.applicationServices = initFilteredApplicationServices();
        this.domainCommands = initFilteredDomainCommands();
        this.domainEvents = initFilteredDomainEvents();
        this.aggregateRoots = initFilteredAggregateRoots();
        this.domainServices = initFilteredDomainServices();
        this.repositories = initFilteredRepositories();
        this.readModels = initFilteredReadModels();
        this.readModelProviders = initFilteredReadModelProviders();
        this.outboundServices = initFilteredOutboundServices();

    }

    private List<ApplicationServiceMirror> initFilteredApplicationServices(){
        return boundedContextMirror
            .getApplicationServices()
            .stream()
            .filter(as -> !as.isAbstract())
            .filter(transitiveDomainTypeFilter::filter)
            .filter(as -> !domainDiagramConfig.getClassesBlacklist().contains(as.getTypeName()))
            .toList();
    }

    private List<DomainCommandMirror> initFilteredDomainCommands(){
        return boundedContextMirror
            .getDomainCommands()
            .stream()
            .filter(dc -> !dc.isAbstract())
            .filter(transitiveDomainTypeFilter::filter)
            .filter(dc -> !domainDiagramConfig.getClassesBlacklist().contains(dc.getTypeName()))
            .toList();
    }

    private List<DomainEventMirror> initFilteredDomainEvents(){
        return boundedContextMirror
            .getDomainEvents()
            .stream()
            .filter(de -> !de.isAbstract())
            .filter(transitiveDomainTypeFilter::filter)
            .filter(de -> !domainDiagramConfig.getClassesBlacklist().contains(de.getTypeName()))
            .toList();
    }

    private List<DomainServiceMirror> initFilteredDomainServices(){
        return boundedContextMirror
            .getDomainServices()
            .stream()
            .filter(ds -> !ds.isAbstract())
            .filter(transitiveDomainTypeFilter::filter)
            .filter(ds -> !domainDiagramConfig.getClassesBlacklist().contains(ds.getTypeName()))
            .toList();
    }

    private List<RepositoryMirror> initFilteredRepositories(){
        return boundedContextMirror
            .getRepositories()
            .stream()
            .filter(r -> !r.isAbstract())
            .filter(transitiveDomainTypeFilter::filter)
            .filter(r -> !domainDiagramConfig.getClassesBlacklist().contains(r.getTypeName()))
            .toList();
    }

    private List<ReadModelMirror> initFilteredReadModels(){
        return boundedContextMirror
            .getReadModels()
            .stream()
            .filter(r -> !r.isAbstract())
            .filter(transitiveDomainTypeFilter::filter)
            .filter(r -> !domainDiagramConfig.getClassesBlacklist().contains(r.getTypeName()))
            .toList();
    }

    private List<AggregateRootMirror> initFilteredAggregateRoots(){
        return boundedContextMirror
            .getAggregateRoots()
            .stream()
            .filter(ar -> ! ar.isAbstract())
            .filter(transitiveDomainTypeFilter::filter)
            .filter(ar -> !domainDiagramConfig.getClassesBlacklist().contains(ar.getTypeName()))
            .toList();
    }

    private List<ReadModelProviderMirror> initFilteredReadModelProviders(){
        return boundedContextMirror
            .getReadModelProviders()
            .stream()
            .filter(r -> !r.isAbstract())
            .filter(transitiveDomainTypeFilter::filter)
            .filter(r -> !domainDiagramConfig.getClassesBlacklist().contains(r.getTypeName()))
            .toList();
    }

    private List<OutboundServiceMirror> initFilteredOutboundServices(){
        return boundedContextMirror
            .getOutboundServices()
            .stream()
            .filter(r -> !r.isAbstract())
            .filter(transitiveDomainTypeFilter::filter)
            .filter(r -> !domainDiagramConfig.getClassesBlacklist().contains(r.getTypeName()))
            .toList();
    }
}

