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
import io.domainlifecycles.mirror.api.ApplicationServiceMirror;
import io.domainlifecycles.mirror.api.DomainCommandMirror;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.api.DomainServiceMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.api.FactoryMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.api.NonDomainTypeMirror;
import io.domainlifecycles.mirror.api.OutboundServiceMirror;
import io.domainlifecycles.mirror.api.QueryHandlerMirror;
import io.domainlifecycles.mirror.api.ReadModelMirror;
import io.domainlifecycles.mirror.api.RepositoryMirror;
import io.domainlifecycles.mirror.api.ServiceKindMirror;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The FilteredDomainClasses class represents a collection of filtered domain classes based on certain criteria
 * (based on the given diagram configuration).
 * It provides separate lists for various types of domain classes like application services, domain commands, domain
 * events, domain services, repositories, aggregate roots, query handlers and read models.
 *
 * @author Mario Herb
 */
public class FilteredDomainClasses {

    private final Set<DomainTypeMirror> includedDomainTypes;
    private final GeneralVisualSettings generalVisualSettings;
    private final DomainFlowFilter domainFlowFilter;

    /**
     * Constructs a new instance of FilteredDomainClasses with the given configuration and domain mirror.
     *
     * @param diagramTrimSettings the configuration defining trim settings for the diagram
     * @param generalVisualSettings the configuration defining visual settings for the diagram
     * @param domainMirror the representation of the domain used to extract and filter domain elements
     * @param domainFlowFilter the restriction to the domain types taking part in a flow
     */
    public FilteredDomainClasses(
        DiagramTrimSettings diagramTrimSettings,
        GeneralVisualSettings generalVisualSettings,
        DomainMirror domainMirror,
        DomainFlowFilter domainFlowFilter) {

        Objects.requireNonNull(diagramTrimSettings,"diagramTrimSettings must not be null");
        this.generalVisualSettings = Objects.requireNonNull(generalVisualSettings,"generalVisualSettings must not be null");
        Objects.requireNonNull(domainMirror,"domainMirror must not be null");
        this.domainFlowFilter = Objects.requireNonNull(domainFlowFilter,"domainFlowFilter must not be null");

        var diagramSettingsFilter = new DiagramSettingsFilter(
            domainMirror,
            diagramTrimSettings,
            generalVisualSettings,
            domainFlowFilter
        );

        includedDomainTypes = diagramSettingsFilter.filterAll(domainMirror.getAllDomainTypeMirrors());
        includeContainedReadModels(domainMirror, diagramSettingsFilter);
    }

    /**
     * A ReadModel contained in a shown ReadModel is shown with it - like the parts of an Aggregate - even if the
     * connections or flows the diagram is restricted to do not reach it themselves.
     */
    private void includeContainedReadModels(DomainMirror domainMirror, DiagramSettingsFilter diagramSettingsFilter) {
        Deque<DomainTypeMirror> pending = includedDomainTypes.stream()
            .filter(dtm -> DomainType.READ_MODEL.equals(dtm.getDomainType()))
            .collect(Collectors.toCollection(ArrayDeque::new));
        while (!pending.isEmpty()) {
            pending.poll().getAllFields().stream()
                .filter(field -> DomainType.READ_MODEL.equals(field.getType().getDomainType()))
                .map(field -> domainMirror.getDomainTypeMirror(field.getType().getTypeName()))
                .flatMap(Optional::stream)
                .filter(diagramSettingsFilter::filterAsContainedPart)
                .filter(includedDomainTypes::add)
                .forEach(pending::add);
        }
    }

    /**
     * Determines whether the specified domain type is contained within the filtered domain types.
     * The method checks for the inclusion of the given domain type based on its type name, inheritance,
     * and implemented interfaces, considering the general visual settings.
     *
     * @param domainTypeMirror the {@link DomainTypeMirror} instance to check for containment
     * @return true if the specified domain type is contained in the filtered domain types, false otherwise
     */
    public boolean contains(DomainTypeMirror domainTypeMirror){
        if( !generalVisualSettings.isShowAllInheritanceStructures()){
            if(!generalVisualSettings.isShowInheritanceStructuresForServiceKinds()
                && (
                    DomainType.DOMAIN_SERVICE.equals(domainTypeMirror.getDomainType())
                    || DomainType.REPOSITORY.equals(domainTypeMirror.getDomainType())
                    || DomainType.OUTBOUND_SERVICE.equals(domainTypeMirror.getDomainType())
                    || DomainType.FACTORY.equals(domainTypeMirror.getDomainType())
                    || DomainType.APPLICATION_SERVICE.equals(domainTypeMirror.getDomainType())
                    || DomainType.QUERY_HANDLER.equals(domainTypeMirror.getDomainType())
                )
            ){
                return this.includedDomainTypes.stream().anyMatch(
                    s -> s.getTypeName().equals(domainTypeMirror.getTypeName())
                        || s.getAllInterfaceTypeNames().contains(domainTypeMirror.getTypeName())
                        || s.getInheritanceHierarchyTypeNames().contains(domainTypeMirror.getTypeName())
                );
            }else if(!generalVisualSettings.isShowInheritanceStructuresInAggregates()
                && (
                DomainType.AGGREGATE_ROOT.equals(domainTypeMirror.getDomainType())
                    || DomainType.ENTITY.equals(domainTypeMirror.getDomainType())
                    || DomainType.VALUE_OBJECT.equals(domainTypeMirror.getDomainType())
            )
            ){
                return this.includedDomainTypes.stream().anyMatch(
                    s -> s.getTypeName().equals(domainTypeMirror.getTypeName())
                        || s.getAllInterfaceTypeNames().contains(domainTypeMirror.getTypeName())
                        || s.getInheritanceHierarchyTypeNames().contains(domainTypeMirror.getTypeName())
                );
            }else if(!generalVisualSettings.isShowInheritanceStructuresForDomainCommands()
                && DomainType.DOMAIN_COMMAND.equals(domainTypeMirror.getDomainType())
            ){
                return this.includedDomainTypes.stream().anyMatch(
                    s -> s.getTypeName().equals(domainTypeMirror.getTypeName())
                        || s.getAllInterfaceTypeNames().contains(domainTypeMirror.getTypeName())
                        || s.getInheritanceHierarchyTypeNames().contains(domainTypeMirror.getTypeName())
                );
            }else if(!generalVisualSettings.isShowInheritanceStructuresForDomainEvents()
                && DomainType.DOMAIN_EVENT.equals(domainTypeMirror.getDomainType())
            ){
                return this.includedDomainTypes.stream().anyMatch(
                    s -> s.getTypeName().equals(domainTypeMirror.getTypeName())
                        || s.getAllInterfaceTypeNames().contains(domainTypeMirror.getTypeName())
                        || s.getInheritanceHierarchyTypeNames().contains(domainTypeMirror.getTypeName())
                );
            }else if(!generalVisualSettings.isShowInheritanceStructuresForReadModels()
                && DomainType.READ_MODEL.equals(domainTypeMirror.getDomainType())
            ){
                return this.includedDomainTypes.stream().anyMatch(
                    s -> s.getTypeName().equals(domainTypeMirror.getTypeName())
                        || s.getAllInterfaceTypeNames().contains(domainTypeMirror.getTypeName())
                        || s.getInheritanceHierarchyTypeNames().contains(domainTypeMirror.getTypeName())
                );
            }
        }
        return this.includedDomainTypes.contains(domainTypeMirror);
    }

    /**
     * @return the calls between the types of the flows the diagram is restricted to, see
     *     {@link DomainFlowFilter#calls()}
     */
    public Map<String, Map<String, Set<String>>> getFlowCalls() {
        return domainFlowFilter.calls();
    }

    /**
     * Whether a method is shown in the class of a domain type, as far as the flows are concerned: with
     * {@code showOnlyFlowMethods}, a type taking part in a flow shows only the methods called in it. A type shown for
     * another reason - e.g. as part of an aggregate or read model - and a diagram without flow show all methods the
     * other settings allow.
     *
     * @param domainTypeMirror the type the method is shown in
     * @param methodMirror     the method
     * @return {@code false} if the method is hidden because it is not called in the flows
     */
    public boolean isShownRegardingFlows(DomainTypeMirror domainTypeMirror, MethodMirror methodMirror) {
        return !generalVisualSettings.isShowOnlyFlowMethods()
            || !domainFlowFilter.isReachedByFlow(domainTypeMirror)
            || domainFlowFilter.isCalledInFlow(domainTypeMirror, methodMirror);
    }

    /**
     * Query contained domain type mirror
     * @param fullQualifiedTypeName of a domaintype
     * @return Optional of the contained DomainType Mirror, empty Optional if not contained
     */
    public Optional<DomainTypeMirror> getContained(String fullQualifiedTypeName){
        return this.includedDomainTypes.stream()
            .filter(t -> t.getTypeName().equals(fullQualifiedTypeName))
            .findFirst();
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
        return this.includedDomainTypes.stream()
            .filter(dtm -> dtm.getDomainType().equals(DomainType.DOMAIN_COMMAND))
            .map(dtm -> (DomainCommandMirror) dtm)
            .sorted(Comparator.comparing(DomainTypeMirror::getTypeName))
            .toList();
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
        return this.includedDomainTypes.stream()
            .filter(dtm -> dtm.getDomainType().equals(DomainType.DOMAIN_EVENT))
            .map(dtm -> (DomainEventMirror) dtm)
            .sorted(Comparator.comparing(DomainTypeMirror::getTypeName))
            .toList();
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
        return this.includedDomainTypes.stream()
            .filter(dtm -> dtm.getDomainType().equals(DomainType.AGGREGATE_ROOT))
            .map(dtm -> (AggregateRootMirror) dtm)
            .sorted(Comparator.comparing(DomainTypeMirror::getTypeName))
            .toList();
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
        return this.includedDomainTypes.stream()
            .filter(dtm -> dtm.getDomainType().equals(DomainType.READ_MODEL))
            .map(dtm -> (ReadModelMirror) dtm)
            .sorted(Comparator.comparing(DomainTypeMirror::getTypeName))
            .toList();
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
        return this.includedDomainTypes.stream()
            .filter(dtm ->
                dtm.getDomainType().equals(DomainType.SERVICE_KIND) ||
                dtm.getDomainType().equals(DomainType.APPLICATION_SERVICE) ||
                dtm.getDomainType().equals(DomainType.DOMAIN_SERVICE) ||
                dtm.getDomainType().equals(DomainType.REPOSITORY) ||
                dtm.getDomainType().equals(DomainType.OUTBOUND_SERVICE) ||
                dtm.getDomainType().equals(DomainType.FACTORY) ||
                dtm.getDomainType().equals(DomainType.QUERY_HANDLER)
            )
            .map(dtm -> (ServiceKindMirror) dtm)
            .sorted(Comparator.comparing(DomainTypeMirror::getTypeName))
            .toList();
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
        return this.includedDomainTypes.stream()
            .filter(dtm -> dtm.getDomainType().equals(DomainType.APPLICATION_SERVICE))
            .map(dtm -> (ApplicationServiceMirror) dtm)
            .sorted(Comparator.comparing(DomainTypeMirror::getTypeName))
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
        return this.includedDomainTypes.stream()
            .filter(dtm -> dtm.getDomainType().equals(DomainType.DOMAIN_SERVICE))
            .map(dtm -> (DomainServiceMirror) dtm)
            .sorted(Comparator.comparing(DomainTypeMirror::getTypeName))
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
        return this.includedDomainTypes.stream()
            .filter(dtm -> dtm.getDomainType().equals(DomainType.REPOSITORY))
            .map(dtm -> (RepositoryMirror) dtm)
            .sorted(Comparator.comparing(DomainTypeMirror::getTypeName))
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
        return this.includedDomainTypes.stream()
            .filter(dtm -> dtm.getDomainType().equals(DomainType.QUERY_HANDLER))
            .map(dtm -> (QueryHandlerMirror) dtm)
            .sorted(Comparator.comparing(DomainTypeMirror::getTypeName))
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
        return this.includedDomainTypes.stream()
            .filter(dtm -> dtm.getDomainType().equals(DomainType.OUTBOUND_SERVICE))
            .map(dtm -> (OutboundServiceMirror) dtm)
            .sorted(Comparator.comparing(DomainTypeMirror::getTypeName))
            .toList();
    }

    /**
     * Retrieves the list of filtered {@link FactoryMirror} instances.
     *
     * @return a list of {@link FactoryMirror} instances representing the filtered factories.
     */
    public List<FactoryMirror> getFactories() {
        return this.includedDomainTypes.stream()
            .filter(dtm -> dtm.getDomainType().equals(DomainType.FACTORY))
            .map(dtm -> (FactoryMirror) dtm)
            .sorted(Comparator.comparing(DomainTypeMirror::getTypeName))
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
        return this.includedDomainTypes.stream()
            .filter(dtm -> dtm.getDomainType().equals(DomainType.SERVICE_KIND))
            .map(dtm -> (ServiceKindMirror) dtm)
            .sorted(Comparator.comparing(DomainTypeMirror::getTypeName))
            .toList();
    }

    /**
     * Retrieves the list of filtered {@link NonDomainTypeMirror} instances associated with the domain.
     *
     * These instances represent classes not classified as any recognized {@link DomainType}, identified
     * and filtered based on the domain mirror and filter configuration. Only non-domain classes that are
     * referenced by a service kind are ever included (see {@link DiagramSettingsFilter}).
     *
     * @return a list of {@link NonDomainTypeMirror} instances representing the filtered non-domain classes.
     */
    public List<NonDomainTypeMirror> getNonDomainClasses() {
        return this.includedDomainTypes.stream()
            .filter(dtm -> dtm.getDomainType().equals(DomainType.NON_DOMAIN))
            .map(dtm -> (NonDomainTypeMirror) dtm)
            .sorted(Comparator.comparing(DomainTypeMirror::getTypeName))
            .toList();
    }

}

