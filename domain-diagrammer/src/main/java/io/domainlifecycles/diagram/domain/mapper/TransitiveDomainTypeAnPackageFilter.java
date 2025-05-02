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

import io.domainlifecycles.mirror.api.AggregateRootMirror;
import io.domainlifecycles.mirror.api.DomainCommandMirror;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.api.QueryHandlerMirror;
import io.domainlifecycles.mirror.api.ReadModelMirror;
import io.domainlifecycles.mirror.api.RepositoryMirror;
import io.domainlifecycles.mirror.api.ServiceKindMirror;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * The TransitiveDomainTypeAnPackageFilter class is used to filter domain types based on a list of seed type names and a
 * BoundedContextMirror.
 * It filters various types of domain objects such as ApplicationServices, DomainServices, Repositories,
 * DomainCommands, DomainEvents, and AggregateRoots.
 * <p>
 * The primary use case is, that we don't want to show all classes in the Domain diagram. Instead we want to focus
 * on a specific part, e.g. the use cases encapsulated in a driver / application service. Initializing the seed with
 * the drivers
 * (full qualified) class name, will result in a diagram, that shows everything below call flow initiated by the
 * driver down to the
 * involved repositories. But all the components involved in other use cases will be excluded.
 *
 * @author Mario Herb
 */
public class TransitiveDomainTypeAnPackageFilter {

    private final List<String> seedTypeNames;

    private final Set<ServiceKindMirror> filteredServiceKinds;

    private final Set<DomainCommandMirror> filteredDomainCommands;

    private final Set<DomainEventMirror> filteredDomainEvents;

    private final Set<AggregateRootMirror> filteredAggregates;

    private final Set<ReadModelMirror> filteredReadModels;

    private final DomainMirror domainMirror;

    private final List<String> filteredPackageNames;

    /**
     * Constructs a new instance of TransitiveDomainTypeAnPackageFilter with the specified parameters.
     *
     * @param domainMirror The DomainMirror instance representing the domain structure
     *                     used for filtering. Must not be null.
     * @param filteredPackageNames A list of package names to be filtered. If null, an empty list is used.
     * @param seedTypeNames A list of seed type names used to populate filters. If null, an empty list is used.
     */
    public TransitiveDomainTypeAnPackageFilter(DomainMirror domainMirror, List<String> filteredPackageNames, List<String> seedTypeNames) {
        this.domainMirror = Objects.requireNonNull(domainMirror, "A DomainMirror must be provided!");
        this.filteredServiceKinds = new HashSet<>();
        this.filteredDomainCommands = new HashSet<>();
        this.filteredDomainEvents = new HashSet<>();
        this.filteredAggregates = new HashSet<>();
        this.filteredReadModels = new HashSet<>();
        if(filteredPackageNames == null) {
            this.filteredPackageNames = Collections.emptyList();
        }else{
            this.filteredPackageNames = filteredPackageNames;
        }
        if (seedTypeNames == null) {
            this.seedTypeNames = Collections.emptyList();
        } else {
            this.seedTypeNames = new ArrayList<>(seedTypeNames);
            prepareFiltersFromSeed();
        }

    }

    private void prepareFiltersFromSeed() {
        addReferencedServiceKinds(
            domainMirror.getAllServiceKindMirrors()
                .stream()
                .filter(s -> seedTypeNames.stream().anyMatch(s::isSubClassOf))
                .toList()
        );

        this.filteredAggregates.addAll(domainMirror.getAllAggregateRootMirrors()
            .stream()
            .filter(s -> seedTypeNames.stream().anyMatch(s::isSubClassOf))
            .toList());

        this.filteredDomainEvents.addAll(domainMirror.getAllDomainEventMirrors()
            .stream()
            .filter(s -> seedTypeNames.stream().anyMatch(s::isSubClassOf))
            .toList());

        this.filteredDomainCommands.addAll(domainMirror.getAllDomainCommandMirrors()
            .stream()
            .filter(s -> seedTypeNames.stream().anyMatch(s::isSubClassOf))
            .toList());

        this.filteredReadModels.addAll(domainMirror.getAllReadModelMirrors()
            .stream()
            .filter(s -> seedTypeNames.stream().anyMatch(s::isSubClassOf))
            .toList());

        var filteredServicesCnt = 0;
        var filteredDomainEventsCnt = 0;
        var filteredDomainCommandsCnt = 0;
        while (filteredServicesCnt != filteredServiceKinds.size()
            || filteredDomainEventsCnt != filteredDomainEvents.size()
            || filteredDomainCommandsCnt != filteredDomainCommands.size()
        ) {
            filteredServicesCnt = filteredServiceKinds.size();
            filteredDomainEventsCnt = filteredDomainEvents.size();
            filteredDomainCommandsCnt = filteredDomainCommands.size();

            var newServices = new ArrayList<ServiceKindMirror>();

            newServices.addAll(
                filteredServiceKinds
                    .stream()
                    .flatMap(ds -> ds.getReferencedServiceKinds().stream())
                    .toList()
            );

            newServices.addAll(
                filteredDomainCommands
                    .stream()
                    .flatMap(c -> c.getProcessingServiceKinds().stream())
                    .toList()
            );

            newServices.addAll(
                filteredDomainEvents
                    .stream()
                    .flatMap(de -> de.getListeningServiceKinds().stream())
                    .toList()
            );

            newServices.addAll(
                filteredDomainEvents
                    .stream()
                    .flatMap(de -> de.getPublishingServiceKinds().stream())
                    .toList()
            );

            var newEvents = new ArrayList<DomainEventMirror>();

            newEvents.addAll(filteredServiceKinds.stream().flatMap(
            ds -> ds.listenedDomainEvents().stream()).toList());

            newEvents.addAll(filteredServiceKinds.stream().flatMap(
                ds -> ds.publishedDomainEvents().stream()).toList());

            filteredDomainEvents.addAll(newEvents);

            var newCommands = new ArrayList<DomainCommandMirror>();
            newCommands.addAll(filteredServiceKinds.stream().flatMap(
                ds -> ds.processedDomainCommands().stream()).toList());

            filteredDomainCommands.addAll(newCommands);

            addReferencedServiceKinds(newServices);

            filteredAggregates.addAll(
                filteredDomainEvents.stream().flatMap(d -> d.getListeningAggregates().stream()).toList()
            );

            filteredAggregates.addAll(
                filteredDomainEvents.stream().flatMap(d -> d.getPublishingAggregates().stream()).toList()
            );
        }

        filteredServiceKinds
        .stream()
        .filter(s -> s.getDomainType().equals(DomainType.REPOSITORY))
        .map(s -> (RepositoryMirror) s)
        .forEach(
        r -> {
            r.getManagedAggregate().ifPresent(filteredAggregates::add);
        });

        filteredServiceKinds
        .stream()
        .filter(s -> s.getDomainType().equals(DomainType.QUERY_HANDLER))
        .map(s -> (QueryHandlerMirror) s).
        forEach(
            r -> {
                if (r.getProvidedReadModel().isPresent()) {
                    filteredReadModels.add(r.getProvidedReadModel().get());
                }
            }
        );

    }

    private void addReferencedServiceKinds(List<ServiceKindMirror> ref) {
        filteredServiceKinds.addAll(ref);
        ref.forEach(abstractDs -> {
                domainMirror.getAllServiceKindMirrors().stream()
                    .filter(ds -> ds.implementsInterface(abstractDs.getTypeName()))
                    .forEach(filteredServiceKinds::add);
            });
    }



    /**
     * Filters the provided {@link DomainTypeMirror} based on predefined criteria.
     * The filtering is determined by the domain type and package name of the
     * provided {@link DomainTypeMirror}.
     *
     * @param dtm the {@link DomainTypeMirror} to be evaluated against the filter criteria
     * @return {@code true} if the provided {@link DomainTypeMirror} satisfies the filter criteria,
     *         {@code false} otherwise
     */
    public boolean filter(DomainTypeMirror dtm) {
        boolean contained = true;
        if (!this.seedTypeNames.isEmpty()) {
            contained = switch (dtm.getDomainType()) {
                case AGGREGATE_ROOT -> filteredAggregates.contains(dtm);
                case DOMAIN_EVENT -> filteredDomainEvents.contains(dtm);
                case DOMAIN_COMMAND -> filteredDomainCommands.contains(dtm);
                case READ_MODEL -> filteredReadModels.contains(dtm);
                default ->  filteredServiceKinds.contains(dtm);
            };
        }
        if(!this.filteredPackageNames.isEmpty()) {
            contained = contained && this.filteredPackageNames.stream().anyMatch(p -> dtm.getTypeName().startsWith(p));
        }
        return contained;
    }


}
