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

import nitrox.dlc.mirror.api.AggregateRootMirror;
import nitrox.dlc.mirror.api.ApplicationServiceMirror;
import nitrox.dlc.mirror.api.BoundedContextMirror;
import nitrox.dlc.mirror.api.DomainCommandMirror;
import nitrox.dlc.mirror.api.DomainCommandProcessingMirror;
import nitrox.dlc.mirror.api.DomainEventMirror;
import nitrox.dlc.mirror.api.DomainEventProcessingMirror;
import nitrox.dlc.mirror.api.DomainServiceMirror;
import nitrox.dlc.mirror.api.DomainTypeMirror;
import nitrox.dlc.mirror.api.OutboundServiceMirror;
import nitrox.dlc.mirror.api.ReadModelMirror;
import nitrox.dlc.mirror.api.QueryClientMirror;
import nitrox.dlc.mirror.api.RepositoryMirror;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The TransitiveDomainTypeFilter class is used to filter domain types based on a list of seed type names and a BoundedContextMirror.
 * It filters various types of domain objects such as ApplicationServices, DomainServices, Repositories, DomainCommands, DomainEvents, and AggregateRoots.
 *
 * The primary use case is, that we don't want to show all classes in the Domain diagram. Instead we want to focus
 * on a specific part, e.g. the use cases encapsulated in a driver / application service. Initializing the seed with the drivers
 * (full qualified) class name, will result in a diagram, that shows everything below call flow initiated by the driver down to the
 * involved repositories. But all the components involved in other use cases will be excluded.
 *
 * @author Mario Herb
 */
public class TransitiveDomainTypeFilter {

    private final List<String> seedTypeNames;

    private final Set<ApplicationServiceMirror> filteredApplicationServices;

    private final Set<DomainServiceMirror> filteredDomainServices;

    private final Set<RepositoryMirror> filteredRepositories;

    private final Set<DomainCommandMirror> filteredDomainCommands;

    private final Set<DomainEventMirror> filteredDomainEvents;

    private final Set<AggregateRootMirror> filteredAggregates;

    private final Set<ReadModelMirror> filteredReadModels;

    private final Set<QueryClientMirror> filteredQueryClients;

    private final Set<OutboundServiceMirror> filteredOutboundServices;

    private final BoundedContextMirror boundedContextMirror;

    public TransitiveDomainTypeFilter(List<String> seedTypeNames, BoundedContextMirror boundedContextMirror) {
        this.filteredApplicationServices = new HashSet<>();
        this.filteredDomainServices = new HashSet<>();
        this.filteredRepositories = new HashSet<>();
        this.filteredDomainCommands = new HashSet<>();
        this.filteredDomainEvents = new HashSet<>();
        this.filteredAggregates = new HashSet<>();
        this.filteredQueryClients = new HashSet<>();
        this.filteredOutboundServices = new HashSet<>();
        this.filteredReadModels = new HashSet<>();
        this.boundedContextMirror = boundedContextMirror;
        if(seedTypeNames == null){
            this.seedTypeNames = new ArrayList<>();
        }else {
            this.seedTypeNames = new ArrayList<>(seedTypeNames);
            prepareFiltersFromSeed();
        }
    }

    private void prepareFiltersFromSeed(){
        this.filteredApplicationServices.addAll(boundedContextMirror.getApplicationServices()
            .stream()
            .filter(as ->
                seedTypeNames.contains(as.getTypeName())
                || seedTypeNames.stream().anyMatch(st -> as.getAllInterfaceTypeNames().contains(st))
            )
            .toList());
        this.filteredDomainServices.addAll(boundedContextMirror.getDomainServices()
            .stream()
            .filter(ds ->
                seedTypeNames.contains(ds.getTypeName())
                || seedTypeNames.stream().anyMatch(st -> ds.getAllInterfaceTypeNames().contains(st))
            )
            .toList());
        this.filteredRepositories.addAll(boundedContextMirror.getRepositories()
            .stream()
            .filter(rs ->
                seedTypeNames.contains(rs.getTypeName())
                || seedTypeNames.stream().anyMatch(st -> rs.getAllInterfaceTypeNames().contains(st))
            )
            .toList());
        this.filteredAggregates.addAll(boundedContextMirror.getAggregateRoots()
            .stream()
            .filter(as -> seedTypeNames.contains(as.getTypeName()))
            .toList());
        this.filteredReadModels.addAll(boundedContextMirror.getReadModels()
            .stream()
            .filter(as -> seedTypeNames.contains(as.getTypeName()))
            .toList());
        this.filteredOutboundServices.addAll(boundedContextMirror.getOutboundServices()
            .stream()
            .filter(as -> seedTypeNames.contains(as.getTypeName()))
            .toList());
        this.filteredQueryClients.addAll(boundedContextMirror.getQueryClients()
            .stream()
            .filter(as -> seedTypeNames.contains(as.getTypeName()))
            .toList());

        //add outgoing references from the seed classes
        filteredApplicationServices.forEach(
            as -> {
                addEventsFor(as);
                addCommandsFor(as);
                addReferencedDomainServices(as.getReferencedDomainServices());
                addReferencedRepositories(as.getReferencedRepositories());
                addReferencedQueryClients(as.getReferencedQueryClients());
                addReferencedOutboundServices(as.getReferencedOutboundServices());
            }
        );

        var filteredDomainServicesCnt = 0;
        while (filteredDomainServicesCnt != filteredDomainServices.size()){
            filteredDomainServicesCnt = filteredDomainServices.size();
            var newServices = filteredDomainServices.stream().flatMap(ds -> ds.getReferencedDomainServices().stream()).toList();
            addReferencedDomainServices(newServices);
        }

        filteredDomainServices.forEach(
            ds -> {
                addEventsFor(ds);
                addCommandsFor(ds);
                addReferencedRepositories(ds.getReferencedRepositories());
                addReferencedQueryClients(ds.getReferencedQueryClients());
                addReferencedOutboundServices(ds.getReferencedOutboundServices());

            }
        );

        filteredRepositories.forEach(
            r -> {
                addEventsFor(r);
                addCommandsFor(r);
                r.getManagedAggregate().ifPresent(filteredAggregates::add);
            }
        );

        filteredOutboundServices.forEach(
            os -> {
                addEventsFor(os);
                addCommandsFor(os);
            }
        );

        filteredQueryClients.forEach(
            r -> {
                addEventsFor(r);
                addCommandsFor(r);
                if(r.getProvidedReadModel().isPresent()){
                    filteredReadModels.add(r.getProvidedReadModel().get());
                }
            }
        );

    }

    private void addReferencedDomainServices(List<DomainServiceMirror> ref){
        filteredDomainServices.addAll(ref);
        ref.stream().filter(DomainTypeMirror::isAbstract)
            .forEach(abstractDs -> {
                boundedContextMirror.getDomainServices().stream()
                    .filter(ds -> ds.getAllInterfaceTypeNames().contains(abstractDs.getTypeName()))
                    .forEach(filteredDomainServices::add);
            });
    }

    private void addReferencedRepositories(List<RepositoryMirror> ref){
        filteredRepositories.addAll(ref);
        ref.stream().filter(DomainTypeMirror::isAbstract)
            .forEach(abstractDs -> {
                boundedContextMirror.getRepositories().stream()
                    .filter(ds -> ds.getAllInterfaceTypeNames().contains(abstractDs.getTypeName()))
                    .forEach(filteredRepositories::add);
            });
    }

    private void addReferencedQueryClients(List<QueryClientMirror> ref){
        filteredQueryClients.addAll(ref);
        ref.stream().filter(DomainTypeMirror::isAbstract)
            .forEach(abstractDs -> {
                boundedContextMirror.getQueryClients().stream()
                    .filter(ds -> ds.getAllInterfaceTypeNames().contains(abstractDs.getTypeName()))
                    .forEach(filteredQueryClients::add);
            });
    }

    private void addReferencedOutboundServices(List<OutboundServiceMirror> ref){
        filteredOutboundServices.addAll(ref);
        ref.stream().filter(DomainTypeMirror::isAbstract)
            .forEach(abstractDs -> {
                boundedContextMirror.getOutboundServices().stream()
                    .filter(ds -> ds.getAllInterfaceTypeNames().contains(abstractDs.getTypeName()))
                    .forEach(filteredOutboundServices::add);
            });
    }

    private void addEventsFor(DomainEventProcessingMirror depm){
        filteredDomainEvents.addAll(boundedContextMirror.getDomainEvents()
            .stream()
            .filter(de -> depm.listensTo(de)||depm.publishes(de))
            .toList());
    }

    private void addCommandsFor(DomainCommandProcessingMirror dcpm){
        filteredDomainCommands.addAll(boundedContextMirror.getDomainCommands()
            .stream()
            .filter(dcpm::processes)
            .toList());
    }

    /**
     * Filters the given DomainTypeMirror based on the configured seed type names and filter conditions.
     *
     * @param dtm the DomainTypeMirror to be filtered
     * @return true if the DomainTypeMirror is accepted by the filter, false otherwise
     */
    public boolean filter(DomainTypeMirror dtm){
        if(!this.seedTypeNames.isEmpty()){
            return switch (dtm.getDomainType()){
                case AGGREGATE_ROOT -> filteredAggregates.contains(dtm);
                case REPOSITORY ->  filteredRepositories.contains(dtm);
                case DOMAIN_SERVICE -> filteredDomainServices.contains(dtm);
                case APPLICATION_SERVICE -> filteredApplicationServices.contains(dtm);
                case DOMAIN_EVENT -> filteredDomainEvents.contains(dtm);
                case DOMAIN_COMMAND -> filteredDomainCommands.contains(dtm);
                case READ_MODEL -> filteredReadModels.contains(dtm);
                case QUERY_CLIENT -> filteredQueryClients.contains(dtm);
                case OUTBOUND_SERVICE -> filteredOutboundServices.contains(dtm);
                default -> false;
            };
        }
        return true;
    }




}
