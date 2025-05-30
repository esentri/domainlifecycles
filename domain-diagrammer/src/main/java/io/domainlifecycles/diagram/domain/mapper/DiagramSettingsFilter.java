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

import io.domainlifecycles.diagram.domain.config.DiagramTrimSettings;
import io.domainlifecycles.diagram.domain.config.GeneralVisualSettings;
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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * The DiagramSettingsFilter class is used to filter domain types based on a list of seed type names and a
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
public class DiagramSettingsFilter {

    private final DiagramTrimSettings trimSettings;
    private final GeneralVisualSettings generalVisualSettings;
    private final Set<DomainTypeMirror> includedDomainTypesByConnctions;
    private final DomainMirror domainMirror;

    /**
     * Constructs a new instance of DiagramSettingsFilter with the specified parameters.
     *
     * @param domainMirror The DomainMirror instance representing the domain structure
     *                     used for filtering. Must not be null.
     * @param diagramTrimSettings Configuration settings that determine how the diagram should be trimmed
     *                           and which elements should be included or excluded
     * @param generalVisualSettings  Configuration for general visual settings
     */
    public DiagramSettingsFilter(DomainMirror domainMirror,
                                 DiagramTrimSettings diagramTrimSettings,
                                 GeneralVisualSettings generalVisualSettings) {
        this.trimSettings = Objects.requireNonNull(diagramTrimSettings, "TrimSettings must be provided!");
        this.domainMirror = Objects.requireNonNull(domainMirror, "A DomainMirror must be provided!");
        this.generalVisualSettings = Objects.requireNonNull(generalVisualSettings, "GeneralVisualSettings must be provided!");

        this.includedDomainTypesByConnctions = new HashSet<>();
        this.includedDomainTypesByConnctions.addAll(calculateConnectedIngoing(diagramTrimSettings.getIncludeConnectedToIngoing()));
        this.includedDomainTypesByConnctions.addAll(calculateConnectedOutgoing(diagramTrimSettings.getIncludeConnectedToOutgoing()));
        this.includedDomainTypesByConnctions.addAll(calculateConnected(diagramTrimSettings.getIncludeConnectedTo()));
        if( this.trimSettings.getIncludeConnectedTo().isEmpty() &&
            this.trimSettings.getIncludeConnectedToIngoing().isEmpty() &&
            this.trimSettings.getIncludeConnectedToOutgoing().isEmpty()
        ){
            this.includedDomainTypesByConnctions.addAll(domainMirror.getAllDomainTypeMirrors());
        }
        this.includedDomainTypesByConnctions.removeAll(calculateConnectedIngoing(diagramTrimSettings.getExcludeConnectedToIngoing()));
        this.includedDomainTypesByConnctions.removeAll(calculateConnectedOutgoing(diagramTrimSettings.getExcludeConnectedToOutgoing()));
    }

    private Set<DomainTypeMirror> calculateConnected(List<String> typeNames){
        var connectedTypes = new HashSet<>(getTypeMirrors(typeNames));
        var size = 0;
        while (connectedTypes.size() != size){
            size = connectedTypes.size();
            connectedTypes.addAll(getOutgoingTypeMirrors(connectedTypes));
            connectedTypes.addAll(getIngoingTypeMirrors(connectedTypes));
        }
        return connectedTypes;
    }

    private Set<DomainTypeMirror> calculateConnectedOutgoing(List<String> typeNames){
        var connectedTypes = new HashSet<>(getTypeMirrors(typeNames));
        var size = 0;
        while (connectedTypes.size() != size){
            size = connectedTypes.size();
            connectedTypes.addAll(getOutgoingTypeMirrors(connectedTypes));
        }
        return connectedTypes;
    }

    private Set<DomainTypeMirror> calculateConnectedIngoing(List<String> typeNames){
        var connectedTypes = new HashSet<>(getTypeMirrors(typeNames));
        var size = 0;
        while (connectedTypes.size() != size){
            size = connectedTypes.size();
            connectedTypes.addAll(getIngoingTypeMirrors(connectedTypes));
        }
        return connectedTypes;
    }

    private List<DomainTypeMirror> getTypeMirrors(List<String> seedTypeNames) {
        List<DomainTypeMirror> seedTypes = new ArrayList<>();

        seedTypes.addAll(domainMirror.getAllServiceKindMirrors()
            .stream()
            .filter(s -> seedTypeNames.stream().anyMatch(t -> s.isSubClassOf(t) || s.implementsInterface(t)))
            .toList());

        seedTypes.addAll(domainMirror.getAllAggregateRootMirrors()
            .stream()
            .filter(s -> seedTypeNames.stream().anyMatch(t -> s.isSubClassOf(t) || s.implementsInterface(t)))
            .toList());

        seedTypes.addAll(domainMirror.getAllDomainEventMirrors()
            .stream()
            .filter(s -> seedTypeNames.stream().anyMatch(t -> s.isSubClassOf(t) || s.implementsInterface(t)))
            .toList());

        seedTypes.addAll(domainMirror.getAllDomainCommandMirrors()
            .stream()
            .filter(s -> seedTypeNames.stream().anyMatch(t -> s.isSubClassOf(t) || s.implementsInterface(t)))
            .toList());

        seedTypes.addAll(domainMirror.getAllReadModelMirrors()
            .stream()
            .filter(s -> seedTypeNames.stream().anyMatch(t -> s.isSubClassOf(t) || s.implementsInterface(t)))
            .toList());
        return seedTypes;
    }

    private List<DomainTypeMirror> getIngoingTypeMirrors(Set<DomainTypeMirror> startingTypeMirrors) {
        List<DomainTypeMirror> ingoing = new ArrayList<>(startingTypeMirrors);
        startingTypeMirrors.forEach(dtm->{
            switch (dtm.getDomainType()) {
                case DOMAIN_EVENT ->  {
                    var domainEventMirror = (DomainEventMirror) dtm;
                    ingoing.addAll(domainEventMirror.getPublishingAggregates());
                    ingoing.addAll(addConcreteServiceKinds(domainEventMirror.getPublishingServiceKinds()));
                }
                case DOMAIN_SERVICE, REPOSITORY, SERVICE_KIND, APPLICATION_SERVICE, QUERY_HANDLER, OUTBOUND_SERVICE -> {
                    var serviceKindMirror = (ServiceKindMirror) dtm;
                    ingoing.addAll(
                        addConcreteServiceKinds(
                            domainMirror
                            .getAllServiceKindMirrors()
                            .stream()
                            .filter(sk -> sk.getReferencedServiceKinds().contains(serviceKindMirror))
                            .toList()
                        )
                    );
                    ingoing.addAll(serviceKindMirror.listenedDomainEvents());
                    ingoing.addAll(serviceKindMirror.processedDomainCommands());
                }
                case READ_MODEL -> {
                    var readModelMirror = (ReadModelMirror) dtm;
                    ingoing.addAll(
                        addConcreteServiceKinds(
                        domainMirror
                            .getAllQueryHandlerMirrors()
                            .stream()
                            .filter(qh ->
                                qh.getProvidedReadModel()
                                    .stream()
                                    .anyMatch(readModelMirror::equals))
                            .toList()
                        )
                    );
                }
                case AGGREGATE_ROOT -> {
                    var aggregateRootMirror = (AggregateRootMirror) dtm;
                    ingoing.addAll(aggregateRootMirror.listenedDomainEvents());
                    ingoing.addAll(aggregateRootMirror.processedDomainCommands());
                    ingoing.addAll(addConcreteServiceKinds(
                            domainMirror
                            .getAllRepositoryMirrors()
                            .stream()
                            .filter(qh ->
                                qh.getManagedAggregate()
                                    .stream()
                                    .anyMatch(aggregateRootMirror::equals))
                            .toList()
                        )
                    );
                }
            }

        });
        return ingoing;
    }

    private List<DomainTypeMirror> getOutgoingTypeMirrors(Set<DomainTypeMirror> startingTypeMirrors) {
        List<DomainTypeMirror> outgoing = new ArrayList<>(startingTypeMirrors);
        startingTypeMirrors.forEach(dtm->{
            switch (dtm.getDomainType()) {
                case DOMAIN_COMMAND -> {
                    var domainCommandMirror = (DomainCommandMirror) dtm;
                    outgoing.addAll(domainCommandMirror.getProcessingServiceKinds());
                }
                case DOMAIN_EVENT ->  {
                    var domainEventMirror = (DomainEventMirror) dtm;
                    outgoing.addAll(domainEventMirror.getListeningAggregates());
                    outgoing.addAll(
                        addConcreteServiceKinds(
                            domainEventMirror.getListeningServiceKinds()
                        )
                    );
                }
                case DOMAIN_SERVICE, REPOSITORY, SERVICE_KIND, APPLICATION_SERVICE, QUERY_HANDLER, OUTBOUND_SERVICE -> {
                    var serviceKindMirror = (ServiceKindMirror) dtm;
                    var ref = serviceKindMirror.getReferencedServiceKinds();
                    outgoing.addAll(addConcreteServiceKinds(ref));
                    outgoing.addAll(serviceKindMirror.publishedDomainEvents());
                    if(dtm.getDomainType().equals(DomainType.REPOSITORY)) {
                        var rep = (RepositoryMirror) dtm;
                        outgoing.addAll(rep.getManagedAggregate().stream().toList());
                    }
                    if(dtm.getDomainType().equals(DomainType.QUERY_HANDLER)) {
                        var rep = (QueryHandlerMirror) dtm;
                        outgoing.addAll(rep.getProvidedReadModel().stream().toList());
                    }
                }
                case AGGREGATE_ROOT -> {
                    var aggregateRootMirror = (AggregateRootMirror) dtm;
                    outgoing.addAll(aggregateRootMirror.publishedDomainEvents());
                }
            }

        });
        return outgoing;
    }

    private List<ServiceKindMirror> addConcreteServiceKinds(List<? extends ServiceKindMirror> ref) {
        var includedServiceKinds = new ArrayList<ServiceKindMirror>(ref);
        ref.forEach(sk -> {
                domainMirror.getAllServiceKindMirrors().stream()
                    .filter( concrete ->
                        concrete.implementsInterface(sk.getTypeName())
                        || sk.implementsInterface(concrete.getTypeName())
                        || concrete.isSubClassOf(sk.getTypeName())
                        || sk.isSubClassOf(concrete.getTypeName())

                    )
                    .forEach(includedServiceKinds::add);
            });
        return includedServiceKinds;
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
        boolean contained = !dtm.getTypeName().startsWith("io.domainlifecycles") && isIncludedByGeneralVisualSettings(dtm);
        if(!contained){
            return false;
        }
        if(trimSettings.hasIncludedConnectedTypeSettings() || trimSettings.hasExcludedConnectedTypeSettings()){
            contained = this.includedDomainTypesByConnctions.contains(dtm);
        }
        if(!this.trimSettings.getExplicitlyIncludedPackageNames().isEmpty()) {
            contained = contained && this.trimSettings.getExplicitlyIncludedPackageNames().stream().anyMatch(
                p -> dtm.getTypeName().startsWith(p)
            );
        }
        contained = contained && !trimSettings.getClassesBlacklist().contains(dtm.getTypeName())
            && dtm.getAllInterfaceTypeNames().stream().noneMatch(
                it -> trimSettings.getClassesBlacklist().contains(it)
        );
        return contained;
    }

    private boolean isIncludedByGeneralVisualSettings(DomainTypeMirror dtm) {
        boolean included = (!dtm.isAbstract() || generalVisualSettings.isShowAllInheritanceStructures());
        if (dtm.isAbstract()) {

            switch (dtm.getDomainType()) {
                case SERVICE_KIND, QUERY_HANDLER, OUTBOUND_SERVICE, DOMAIN_SERVICE, REPOSITORY, APPLICATION_SERVICE -> {
                    included = included
                        || generalVisualSettings.isShowAllInheritanceStructures()
                        || generalVisualSettings.isShowInheritanceStructuresForServiceKinds()
                        || noConcreteTypeExists(dtm);
                }
                case AGGREGATE_ROOT, ENTITY, VALUE_OBJECT -> {
                    included = included
                        || generalVisualSettings.isShowAllInheritanceStructures()
                        || generalVisualSettings.isShowInheritanceStructuresInAggregates()
                        || noConcreteTypeExists(dtm);
                }
                case READ_MODEL -> {
                    included = included
                        || generalVisualSettings.isShowAllInheritanceStructures()
                        || generalVisualSettings.isShowInheritanceStructuresForReadModels()
                        || noConcreteTypeExists(dtm);
                }
                case DOMAIN_COMMAND -> {
                    included = included
                        || generalVisualSettings.isShowAllInheritanceStructures()
                        || generalVisualSettings.isShowInheritanceStructuresForDomainCommands()
                        || noConcreteTypeExists(dtm);
                }
                case DOMAIN_EVENT -> {
                    included = included
                        || generalVisualSettings.isShowAllInheritanceStructures()
                        || generalVisualSettings.isShowInheritanceStructuresForDomainEvents()
                        || noConcreteTypeExists(dtm);
                }
            }
        }
        switch (dtm.getDomainType()) {
            case DOMAIN_EVENT -> included = included && generalVisualSettings.isShowDomainEvents();
            case DOMAIN_SERVICE -> included = included && generalVisualSettings.isShowDomainServices();
            case AGGREGATE_ROOT, ENTITY -> included = included && generalVisualSettings.isShowAggregates();
            case READ_MODEL -> included = included && generalVisualSettings.isShowReadModels();
            case QUERY_HANDLER -> included = included && generalVisualSettings.isShowQueryHandlers();
            case REPOSITORY -> included = included && generalVisualSettings.isShowRepositories()
                && !dtm.getTypeName().equals("io.domainlifecycles.jooq.imp.JooqAggregateRepository");
            case APPLICATION_SERVICE -> included = included && generalVisualSettings.isShowApplicationServices();
            case OUTBOUND_SERVICE -> included = included && generalVisualSettings.isShowOutboundServices();
            case DOMAIN_COMMAND -> included = included && generalVisualSettings.isShowDomainCommands();
            case SERVICE_KIND -> included = included && generalVisualSettings.isShowUnspecifiedServiceKinds();
        }
        return included;
    }

    private boolean noConcreteTypeExists(DomainTypeMirror dtm) {
        if(!dtm.isAbstract()){
            return false;
        }
        return this.includedDomainTypesByConnctions
            .stream()
            .noneMatch(incl ->
                incl.implementsInterface(dtm.getTypeName())
                    || incl.isSubClassOf(dtm.getTypeName())
            );
    }


}
