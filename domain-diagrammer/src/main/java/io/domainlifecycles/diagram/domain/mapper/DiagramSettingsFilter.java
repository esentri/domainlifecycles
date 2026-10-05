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
import io.domainlifecycles.mirror.api.NonDomainTypeMirror;
import io.domainlifecycles.mirror.api.QueryHandlerMirror;
import io.domainlifecycles.mirror.api.ReadModelMirror;
import io.domainlifecycles.mirror.api.RepositoryMirror;
import io.domainlifecycles.mirror.api.ServiceKindMirror;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
    private final Set<DomainTypeMirror> includedDomainTypesByConnections;
    private final DomainMirror domainMirror;
    private final DomainFlowFilter domainFlowFilter;
    private Set<String> nonDomainTypeNamesReferencedByServiceKinds;

    /**
     * Constructs a new instance of DiagramSettingsFilter with the specified parameters.
     *
     * @param domainMirror The DomainMirror instance representing the domain structure
     *                     used for filtering. Must not be null.
     * @param diagramTrimSettings Configuration settings that determine how the diagram should be trimmed
     *                           and which elements should be included or excluded
     * @param generalVisualSettings  Configuration for general visual settings
     * @param domainFlowFilter       Restriction to the types taking part in a flow, use
     *                               {@link DomainFlowFilter#INACTIVE} to not restrict by flow
     */
    public DiagramSettingsFilter(DomainMirror domainMirror,
                                 DiagramTrimSettings diagramTrimSettings,
                                 GeneralVisualSettings generalVisualSettings,
                                 DomainFlowFilter domainFlowFilter) {
        this.trimSettings = Objects.requireNonNull(diagramTrimSettings, "TrimSettings must be provided!");
        this.domainMirror = Objects.requireNonNull(domainMirror, "A DomainMirror must be provided!");
        this.generalVisualSettings = Objects.requireNonNull(generalVisualSettings, "GeneralVisualSettings must be provided!");
        this.domainFlowFilter = Objects.requireNonNull(domainFlowFilter, "A DomainFlowFilter must be provided!");

        this.includedDomainTypesByConnections = new HashSet<>();
        this.includedDomainTypesByConnections.addAll(calculateConnectedIngoing(diagramTrimSettings.getIncludeConnectedToIngoing()));
        this.includedDomainTypesByConnections.addAll(calculateConnectedOutgoing(diagramTrimSettings.getIncludeConnectedToOutgoing()));
        this.includedDomainTypesByConnections.addAll(calculateConnected(diagramTrimSettings.getIncludeConnectedTo()));
        if( this.trimSettings.getIncludeConnectedTo().isEmpty() &&
            this.trimSettings.getIncludeConnectedToIngoing().isEmpty() &&
            this.trimSettings.getIncludeConnectedToOutgoing().isEmpty()
        ){
            this.includedDomainTypesByConnections.addAll(domainMirror.getAllDomainTypeMirrors());
        }
        this.includedDomainTypesByConnections.removeAll(calculateConnectedIngoing(diagramTrimSettings.getExcludeConnectedToIngoing()));
        this.includedDomainTypesByConnections.removeAll(calculateConnectedOutgoing(diagramTrimSettings.getExcludeConnectedToOutgoing()));
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
                case DOMAIN_SERVICE, REPOSITORY, SERVICE_KIND, APPLICATION_SERVICE, QUERY_HANDLER, OUTBOUND_SERVICE, FACTORY -> {
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
                    // without query handler, the service kinds and non-domain classes returning it provide it
                    if (!DomainMapperUtils.isProvidedByAQueryHandler(readModelMirror, domainMirror)) {
                        domainMirror.getAllDomainTypeMirrors().stream()
                            .filter(provider -> DomainMapperUtils
                                .readModelsProvidedWithoutQueryHandler(provider, domainMirror)
                                .contains(readModelMirror))
                            .forEach(ingoing::add);
                    }
                }
                case AGGREGATE_ROOT -> {
                    var aggregateRootMirror = (AggregateRootMirror) dtm;
                    // the service kinds creating it, and the aggregates whose root, entities or value objects do
                    domainMirror.getAllServiceKindMirrors().stream()
                        .filter(creator -> DomainMapperUtils.createdAggregateRoots(creator, domainMirror)
                            .contains(aggregateRootMirror))
                        .forEach(ingoing::add);
                    domainMirror.getAllAggregateRootMirrors().stream()
                        .filter(creator -> DomainMapperUtils.aggregateRootsCreatedByAggregate(creator, domainMirror)
                            .contains(aggregateRootMirror))
                        .forEach(ingoing::add);
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
                case DOMAIN_SERVICE, REPOSITORY, SERVICE_KIND, APPLICATION_SERVICE, QUERY_HANDLER, OUTBOUND_SERVICE, FACTORY -> {
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
                    outgoing.addAll(DomainMapperUtils.readModelsProvidedWithoutQueryHandler(dtm, domainMirror));
                    outgoing.addAll(DomainMapperUtils.createdAggregateRoots(dtm, domainMirror));
                }
                case AGGREGATE_ROOT -> {
                    var aggregateRootMirror = (AggregateRootMirror) dtm;
                    outgoing.addAll(aggregateRootMirror.publishedDomainEvents());
                    outgoing.addAll(DomainMapperUtils.aggregateRootsCreatedByAggregate(aggregateRootMirror, domainMirror));
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
        return isIncluded(dtm, false);
    }

    private boolean isIncluded(DomainTypeMirror dtm, boolean standingIn) {
        if (DomainMapperUtils.isNeverShown(dtm)) {
            return false;
        }
        boolean contained = !dtm.getTypeName().startsWith("io.domainlifecycles") && isIncludedByGeneralVisualSettings(dtm, standingIn);
        if(!contained){
            return false;
        }
        if(trimSettings.hasIncludedConnectedTypeSettings() || trimSettings.hasExcludedConnectedTypeSettings()){
            contained = this.includedDomainTypesByConnections.contains(dtm);
        }
        contained = contained && isIncludedByPackageAndBlacklist(dtm);
        // last, because restricting to a flow may only narrow what the other settings allowed,
        // never widen it
        contained = contained && domainFlowFilter.contains(dtm);
        return contained;
    }

    /**
     * Whether a domain type may be shown as part of another shown one - like a ReadModel contained in a shown
     * ReadModel. A part is shown together with the type containing it, so neither the connections nor the flows the
     * diagram is restricted to apply to it, only the general visual settings, the packages and the blacklist.
     *
     * @param dtm the {@link DomainTypeMirror} to be evaluated
     * @return {@code true} if it may be shown as part of another shown domain type
     */
    public boolean filterAsContainedPart(DomainTypeMirror dtm) {
        return !DomainMapperUtils.isNeverShown(dtm)
            && !dtm.getTypeName().startsWith("io.domainlifecycles")
            && isIncludedByGeneralVisualSettings(dtm)
            && isIncludedByPackageAndBlacklist(dtm);
    }

    private boolean isIncludedByPackageAndBlacklist(DomainTypeMirror dtm) {
        boolean contained = true;
        if(!this.trimSettings.getExplicitlyIncludedPackageNames().isEmpty()) {
            contained = this.trimSettings.getExplicitlyIncludedPackageNames().stream().anyMatch(
                p -> dtm.getTypeName().startsWith(p)
            );
        }
        return contained && !trimSettings.getClassesBlacklist().contains(dtm.getTypeName())
            && dtm.getAllInterfaceTypeNames().stream().noneMatch(
                it -> trimSettings.getClassesBlacklist().contains(it)
        );
    }

    /**
     * Filters all given domain types. An abstract type standing in for its implementations (see
     * {@link #standsInForItsImplementations(DomainTypeMirror)}) is only shown if none of its implementations is shown -
     * after all other settings, the connections and flows included. That way an interface whose only implementation is
     * left out, e.g. because the flow the diagram is restricted to reaches the interface only, is still shown.
     *
     * @param domainTypeMirrors the domain types to filter
     * @return the domain types to show
     */
    public Set<DomainTypeMirror> filterAll(Collection<? extends DomainTypeMirror> domainTypeMirrors) {
        Set<DomainTypeMirror> included = domainTypeMirrors.stream()
            .filter(this::filter)
            .collect(Collectors.toCollection(HashSet::new));
        List<DomainTypeMirror> standingIn = domainTypeMirrors.stream()
            .filter(this::standsInForItsImplementations)
            .filter(dtm -> isIncluded(dtm, true))
            .filter(dtm -> noImplementationShown(dtm, included))
            .collect(Collectors.toList());
        included.addAll(standingIn);
        return included;
    }

    /**
     * Whether an abstract type is shown in place of its implementations rather than besides them: when the
     * inheritance structures of its kind are not shown (neither {@code showAllInheritanceStructures} nor the setting of
     * its kind, e.g. {@code showInheritanceStructuresForReadModels}). With the inheritance structures shown, abstract
     * and concrete types are shown both.
     */
    private boolean standsInForItsImplementations(DomainTypeMirror dtm) {
        return dtm.isAbstract() && !isShownByInheritanceSettings(dtm) && hasInheritanceSetting(dtm);
    }

    private boolean hasInheritanceSetting(DomainTypeMirror dtm) {
        return switch (dtm.getDomainType()) {
            case SERVICE_KIND, QUERY_HANDLER, OUTBOUND_SERVICE, FACTORY, DOMAIN_SERVICE, REPOSITORY, APPLICATION_SERVICE,
                 AGGREGATE_ROOT, ENTITY, VALUE_OBJECT, READ_MODEL, DOMAIN_COMMAND, DOMAIN_EVENT -> true;
            default -> false;
        };
    }

    private boolean isShownByInheritanceSettings(DomainTypeMirror dtm) {
        if (generalVisualSettings.isShowAllInheritanceStructures()) {
            return true;
        }
        return switch (dtm.getDomainType()) {
            case SERVICE_KIND, QUERY_HANDLER, OUTBOUND_SERVICE, FACTORY, DOMAIN_SERVICE, REPOSITORY,
                 APPLICATION_SERVICE -> generalVisualSettings.isShowInheritanceStructuresForServiceKinds();
            case AGGREGATE_ROOT, ENTITY, VALUE_OBJECT -> generalVisualSettings.isShowInheritanceStructuresInAggregates();
            case READ_MODEL -> generalVisualSettings.isShowInheritanceStructuresForReadModels();
            case DOMAIN_COMMAND -> generalVisualSettings.isShowInheritanceStructuresForDomainCommands();
            case DOMAIN_EVENT -> generalVisualSettings.isShowInheritanceStructuresForDomainEvents();
            default -> false;
        };
    }

    private boolean isIncludedByGeneralVisualSettings(DomainTypeMirror dtm) {
        return isIncludedByGeneralVisualSettings(dtm, false);
    }

    /**
     * @param standingIn whether the type is checked as abstract type standing in for its implementations, which
     *                   {@link #filterAll(Collection)} decides on separately
     */
    private boolean isIncludedByGeneralVisualSettings(DomainTypeMirror dtm, boolean standingIn) {
        boolean included = !dtm.isAbstract() || isShownByInheritanceSettings(dtm) || standingIn;
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
            case FACTORY -> included = included && generalVisualSettings.isShowFactories();
            case DOMAIN_COMMAND -> {
                included = included && generalVisualSettings.isShowDomainCommands();
            }
            case SERVICE_KIND -> included = included && generalVisualSettings.isShowUnspecifiedServiceKinds();
            case NON_DOMAIN -> included = included
                && generalVisualSettings.isShowNonDomainClasses()
                && (isReferencedByServiceKind(dtm) || referencesAServiceKind(dtm));
        }
        return included;
    }

    /**
     * A non-domain class is shown when a service kind depends on it (e.g. a mapper or helper class
     * a domain service holds a field for).
     */
    private boolean isReferencedByServiceKind(DomainTypeMirror dtm) {
        if (nonDomainTypeNamesReferencedByServiceKinds == null) {
            // resolved once per filter instead of once per checked type: resolving the references of
            // all service kinds for every non-domain class is quadratic and dominated the diagram
            // generation of large models
            nonDomainTypeNamesReferencedByServiceKinds = domainMirror.getAllServiceKindMirrors()
                .stream()
                .flatMap(sk -> sk.getReferencedNonDomainTypes().stream())
                .map(DomainTypeMirror::getTypeName)
                .collect(Collectors.toUnmodifiableSet());
        }
        return nonDomainTypeNamesReferencedByServiceKinds.contains(dtm.getTypeName());
    }

    /**
     * A non-domain class is also shown when it depends on a service kind itself (e.g. a controller
     * or a message listener calling into an application service) - the inverse relationship of
     * {@link #isReferencedByServiceKind(DomainTypeMirror)}.
     */
    private boolean referencesAServiceKind(DomainTypeMirror dtm) {
        return dtm instanceof NonDomainTypeMirror nonDomainTypeMirror
            && !nonDomainTypeMirror.getReferencedServiceKinds().isEmpty();
    }

    /**
     * Whether no implementation of an abstract type - directly or via abstract types in between - is shown.
     */
    private boolean noImplementationShown(DomainTypeMirror dtm, Set<DomainTypeMirror> shown) {
        if(!dtm.isAbstract()){
            return false;
        }
        var typesOfSameDomainType = shown.stream()
            .filter(incl -> incl.getDomainType().equals(dtm.getDomainType()))
            .filter(incl -> !incl.getTypeName().equals(dtm.getTypeName()))
            .collect(Collectors.toSet());
        // the abstract types in between need not be shown themselves
        var abstractTypesOfSameDomainType = domainMirror.getAllDomainTypeMirrors()
            .stream()
            .filter(incl -> incl.getDomainType().equals(dtm.getDomainType()))
            .filter(incl -> !incl.getTypeName().equals(dtm.getTypeName()))
            .filter(incl -> incl.isAbstract())
            .collect(Collectors.toSet());
        var abstractSubTypes = new HashSet<DomainTypeMirror>();
        abstractSubTypes.add(dtm);
        var size = -1;
        while (size != abstractSubTypes.size()){
            size = abstractSubTypes.size();
            for(DomainTypeMirror abstractSub : abstractTypesOfSameDomainType){
                if(abstractSub.implementsInterface(dtm.getTypeName())
                    || abstractSub.isSubClassOf(dtm.getTypeName())
                            ){
                    abstractSubTypes.add(abstractSub);
                }

            }
        }
        var ret = typesOfSameDomainType
            .stream()
            .filter(incl -> !incl.isAbstract())
            .noneMatch(concrete ->
                abstractSubTypes.stream().anyMatch(sub -> concrete.isSubClassOf(sub.getTypeName())
                    || concrete.implementsInterface(sub.getTypeName())));
        return ret;
    }


}
