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

import io.domainlifecycles.diagram.domain.DomainDiagramGenerator;
import io.domainlifecycles.diagram.domain.config.DomainDiagramConfig;
import io.domainlifecycles.diagram.nomnoml.NomnomlRelationship;
import io.domainlifecycles.mirror.api.AccessLevel;
import io.domainlifecycles.mirror.api.AggregateRootMirror;
import io.domainlifecycles.mirror.api.ApplicationServiceMirror;
import io.domainlifecycles.mirror.api.DomainCommandMirror;
import io.domainlifecycles.mirror.api.DomainCommandProcessingMirror;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.api.DomainServiceMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.api.EntityMirror;
import io.domainlifecycles.mirror.api.EntityReferenceMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.api.NonDomainTypeMirror;
import io.domainlifecycles.mirror.api.OutboundServiceMirror;
import io.domainlifecycles.mirror.api.QueryHandlerMirror;
import io.domainlifecycles.mirror.api.RepositoryMirror;
import io.domainlifecycles.mirror.api.ServiceKindMirror;
import io.domainlifecycles.mirror.api.ValueReferenceMirror;
import io.domainlifecycles.mirror.model.AssertionType;
import io.domainlifecycles.mirror.visitor.ContextDomainObjectVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Derives the {@link NomnomlRelationship} representations from the given domain structures delivered as mirrors.
 *
 * @author Mario Herb
 */
public class DomainRelationshipMapper {

    private final DomainDiagramConfig diagramConfig;
    private final DomainMirror domainMirror;

    private final FilteredDomainClasses filteredDomainClasses;
    private final NodeNames nodeNames;


    /**
     * Initializes the DomainRelationshipMapper with a given {@link DomainDiagramConfig} and the
     * {@link FilteredDomainClasses}
     *
     * @param diagramConfig         diagram configuration
     * @param domainMirror           the domain model
     * @param filteredDomainClasses filtered domain classes
     */
    public DomainRelationshipMapper(DomainDiagramConfig diagramConfig, DomainMirror domainMirror, FilteredDomainClasses filteredDomainClasses) {
        this(diagramConfig, domainMirror, filteredDomainClasses, NodeNames.withoutPackageHints(diagramConfig));
    }

    /**
     * Initializes the DomainRelationshipMapper with a given {@link DomainDiagramConfig}, the
     * {@link FilteredDomainClasses} and the names of the diagram's nodes.
     *
     * @param diagramConfig         diagram configuration
     * @param domainMirror           the domain model
     * @param filteredDomainClasses filtered domain classes
     * @param nodeNames             the names of the diagram's nodes, which the relationships connect
     */
    public DomainRelationshipMapper(DomainDiagramConfig diagramConfig, DomainMirror domainMirror,
                                    FilteredDomainClasses filteredDomainClasses, NodeNames nodeNames) {
        this.diagramConfig = diagramConfig;
        this.domainMirror = domainMirror;
        this.filteredDomainClasses = filteredDomainClasses;
        this.nodeNames = nodeNames;
    }

    /**
     * Derives a {@link NomnomlRelationship} for all ApplicationServices that use a Repository.
     *
     * @return mapped application service - service repository relationships
     */
    public List<NomnomlRelationship> mapAllServiceKindRelationships() {
        var relationShips = new ArrayList<NomnomlRelationship>();
        filteredDomainClasses
            .getServiceKinds()
            .forEach(s -> s.getReferencedServiceKinds()
                .stream()
                .filter(filteredDomainClasses::contains)
                .forEach(t -> relationShips.add(mapServiceKindRelationship(s, t)))
            );
        if(diagramConfig.getGeneralVisualSettings().isShowAllInheritanceStructures()
            || diagramConfig.getGeneralVisualSettings().isShowInheritanceStructuresForServiceKinds()) {
            filteredDomainClasses
                .getServiceKinds().forEach(s -> {
                    relationShips.addAll(mapImplementsInterface(s));
                    mapInheritance(s).ifPresent(relationShips::add);
                });
        }
        return relationShips;
    }

    /**
     * Derives a {@link NomnomlRelationship} for all ServiceKinds that reference a non-domain class,
     * as well as for all non-domain classes that reference a ServiceKind themselves (e.g. a
     * controller or a message listener calling into an application service).
     *
     * @return mapped service kind - non-domain class relationships
     */
    public List<NomnomlRelationship> mapAllNonDomainRelationships() {
        var relationShips = new ArrayList<NomnomlRelationship>();
        filteredDomainClasses
            .getServiceKinds()
            .forEach(s -> s.getReferencedNonDomainTypes()
                .stream()
                .filter(filteredDomainClasses::contains)
                .forEach(t -> relationShips.add(mapServiceKindToNonDomainRelationship(s, t)))
            );
        filteredDomainClasses
            .getNonDomainClasses()
            .forEach(nd -> nd.getReferencedServiceKinds()
                .stream()
                .filter(filteredDomainClasses::contains)
                .forEach(s -> relationShips.add(mapNonDomainToServiceKindRelationship(nd, s)))
            );
        return relationShips;
    }

    /**
     * Derives a {@link NomnomlRelationship} for all Aggregates to their Repository.
     *
     * @return mapped aggregate repository relationships
     */
    public List<NomnomlRelationship> mapAllAggregateRepositoryRelationships() {
        var relationShips = new ArrayList<NomnomlRelationship>();
        filteredDomainClasses.getRepositories()
            .stream()
            .filter(r -> {
                    if(r.getManagedAggregate().isPresent()){
                        return filteredDomainClasses.contains(r.getManagedAggregate().get());
                    }
                    return false;
                }
            )
            .forEach(r -> relationShips.add(mapAggregateRepositoryRelationship(r)));
        return relationShips;
    }

    /**
     * Derives a {@link NomnomlRelationship} for all QueryHandlers to their ReadModels.
     *
     * @return mapped query handler - read model relationships
     */
    public List<NomnomlRelationship> mapAllQueryHandlerReadModelRelationships() {
        var relationShips = new ArrayList<NomnomlRelationship>();
        if (diagramConfig.getGeneralVisualSettings().isShowQueryHandlers() && diagramConfig.getGeneralVisualSettings().isShowReadModels()) {
            filteredDomainClasses.getQueryHandlers().stream()
                .filter(r -> {
                        if(r.getProvidedReadModel().isPresent()){
                            return filteredDomainClasses.contains(r.getProvidedReadModel().get());
                        }
                        return false;
                    }
                )
                .forEach(r -> relationShips.add(mapQueryHandlerReadModelRelationship(r)));
        }
        return relationShips;
    }

    /**
     * Derives a {@link NomnomlRelationship} for all Aggregates pointing to another Aggregate by an Id Reference.
     *
     * @return mapped aggregate frame relationships
     */
    public List<NomnomlRelationship> mapAllAggregateFrameRelationships() {
        var relationShips = new ArrayList<NomnomlRelationship>();
        filteredDomainClasses.getAggregateRoots()
            .forEach(ar -> {
                var visitor = new ContextDomainObjectVisitor(ar) {
                    @Override
                    public void visitValueReference(ValueReferenceMirror valueReferenceMirror) {
                        if (valueReferenceMirror.getValue().isIdentity() && !valueReferenceMirror.isIdentityField()) {
                            relationShips.addAll(mapFrameIdReferences(valueReferenceMirror));
                        }
                    }
                };
                visitor.start();
            });
        for (NomnomlRelationship rel : relationShips) {
            var startIndex = relationShips.indexOf(rel);
            for (var i = startIndex; i < relationShips.size(); i++) {
                var compared = relationShips.get(i);
                if (compared.getFromName().equals(rel.getToName()) && compared.getToName().equals(rel.getFromName())) {
                    compared.transpose();
                }
            }
        }

        return relationShips;
    }

    /**
     * Derives a {@link NomnomlRelationship} for all Aggregates, services
     * or non-domain classes processing Domain Commands.
     *
     * @return mapped domain command relationships
     */
    public List<NomnomlRelationship> mapAllDomainCommandRelationships() {
        var relationShips = new ArrayList<NomnomlRelationship>();
        if (diagramConfig.getGeneralVisualSettings().isShowDomainCommands()) {
            if(diagramConfig.getGeneralVisualSettings().isShowAllInheritanceStructures()
                || diagramConfig.getGeneralVisualSettings().isShowInheritanceStructuresForDomainCommands()) {
                filteredDomainClasses
                    .getDomainCommands().forEach(s -> {
                        relationShips.addAll(mapImplementsInterface(s));
                        mapInheritance(s).ifPresent(relationShips::add);
                    });
            }
            filteredDomainClasses.getDomainCommands()
                .forEach(c -> {
                    filteredDomainClasses.getAggregateRoots()
                        .forEach(
                            ar -> {
                                if (ar.processes(c)) {
                                    if (!diagramConfig.getGeneralVisualSettings().isShowOnlyTopLevelDomainCommandRelations() || isTopLevelConsumerForCommand(
                                        ar, c)) {
                                        relationShips.add(mapDomainCommandAggregateRelationship(c, ar));
                                    }
                                }
                            }
                        );

                    filteredDomainClasses.getServiceKinds()
                        .forEach(
                            ds -> {
                                if (ds.processes(c)) {
                                    if (!diagramConfig.getGeneralVisualSettings().isShowOnlyTopLevelDomainCommandRelations() || isTopLevelConsumerForCommand(
                                        ds, c)) {
                                        relationShips.add(mapDomainCommandProcessorRelationship(c, ds));
                                    }
                                }
                            }
                        );

                    // e.g. a controller or message listener receiving the command, like an ApplicationService
                    filteredDomainClasses.getNonDomainClasses()
                        .forEach(
                            nd -> {
                                if (processes(nd, c)) {
                                    if (!diagramConfig.getGeneralVisualSettings().isShowOnlyTopLevelDomainCommandRelations() || isTopLevelConsumerForCommand(
                                        nd, c)) {
                                        relationShips.add(mapDomainCommandProcessorRelationship(c, nd));
                                    }
                                }
                            }
                        );
                });
        }
        return relationShips;
    }

    /**
     * Derives a {@link NomnomlRelationship} for all ReadModels containing another ReadModel - like a ValueObject
     * containing another one - and, if shown, their inheritance structures.
     *
     * @return mapped read model relationships
     */
    public List<NomnomlRelationship> mapAllReadModelRelationships() {
        var relationShips = new ArrayList<NomnomlRelationship>();
        if (diagramConfig.getGeneralVisualSettings().isShowReadModels()) {
            boolean inheritanceShown = diagramConfig.getGeneralVisualSettings().isShowAllInheritanceStructures()
                || diagramConfig.getGeneralVisualSettings().isShowInheritanceStructuresForReadModels();
            if(inheritanceShown) {
                filteredDomainClasses
                    .getReadModels().forEach(s -> {
                        relationShips.addAll(mapImplementsInterface(s));
                        mapInheritance(s).ifPresent(relationShips::add);
                    });
            }
            filteredDomainClasses.getReadModels()
                .forEach(readModel -> readModel.getAllFields().stream()
                    .filter(field -> !field.isStatic() && !field.isHidden())
                    // with the inheritance shown, an inherited field is drawn from the super type already
                    .filter(field -> !inheritanceShown || field.getDeclaredByTypeName().equals(readModel.getTypeName()))
                    .filter(field -> DomainType.READ_MODEL.equals(field.getType().getDomainType()))
                    .filter(field -> filteredDomainClasses.getContained(field.getType().getTypeName()).isPresent())
                    .forEach(field -> relationShips.add(mapContainedReadModel(readModel, field))));
        }
        return relationShips;
    }

    private boolean isTopLevelConsumerForCommand(DomainTypeMirror domainTypeMirror,
                                                 DomainCommandMirror domainCommandMirror) {
        var typesReferencing = domainMirror.getAllDomainTypeMirrors()
            .stream()
            .filter(dt -> {
                    return dt.getAllFields()
                        .stream()
                        .anyMatch(fm -> {
                            return fm.getType().getTypeName().equals(domainTypeMirror.getTypeName())
                                || domainTypeMirror.getAllInterfaceTypeNames().stream().anyMatch(
                                it -> fm.getType().getTypeName().equals(it));
                        });
                }

            ).toList();
        if (domainTypeMirror.getDomainType().equals(DomainType.AGGREGATE_ROOT)) {
            return domainMirror.getAllDomainTypeMirrors()
                .stream()
                .filter(dtm -> dtm instanceof DomainCommandProcessingMirror)
                .filter(dtm ->
                    dtm.getDomainType().equals(DomainType.REPOSITORY)
                        || dtm.getDomainType().equals(DomainType.DOMAIN_SERVICE)
                        || dtm.getDomainType().equals(DomainType.APPLICATION_SERVICE)
                )
                .map(dtm -> (DomainCommandProcessingMirror) dtm)
                .noneMatch(d -> d.processes(domainCommandMirror));
        }
        // domainTypeMirror is top-level unless SOME referencing type also processes the very same
        // command - a referencing type kept around for an unrelated purpose (e.g. a field of the
        // same service used just to call one of its other methods) must not by itself suppress the
        // relationship, so every referencing type is checked, not just the first one encountered.
        // A non-domain class receiving the command counts only if it is shown: otherwise nothing in the diagram would be
        // connected to the command.
        return typesReferencing.stream()
            .filter(referencingType -> referencingType instanceof DomainCommandProcessingMirror
                || referencingType instanceof NonDomainTypeMirror && filteredDomainClasses.contains(referencingType))
            .noneMatch(referencingType -> processes(referencingType, domainCommandMirror));
    }

    /**
     * Whether a domain type processes a command: for a non-domain class, whether one of its methods takes it.
     */
    private static boolean processes(DomainTypeMirror domainTypeMirror, DomainCommandMirror domainCommandMirror) {
        if (domainTypeMirror instanceof DomainCommandProcessingMirror processingMirror) {
            return processingMirror.processes(domainCommandMirror);
        }
        return domainTypeMirror.getMethods().stream().anyMatch(method -> method.processes(domainCommandMirror));
    }

    /**
     * Derives a {@link NomnomlRelationship} for all Aggregates
     * or Domain Services or Application Services or Repositories publishing or listening to Domain Events.
     *
     * @return mapped domain event relationships
     */
    public List<NomnomlRelationship> mapAllDomainEventRelationships() {
        var relationShips = new ArrayList<NomnomlRelationship>();
        if(diagramConfig.getGeneralVisualSettings().isShowAllInheritanceStructures()
            || diagramConfig.getGeneralVisualSettings().isShowInheritanceStructuresForDomainEvents()) {
            filteredDomainClasses
                .getDomainEvents().forEach(s -> {
                    relationShips.addAll(mapImplementsInterface(s));
                    mapInheritance(s).ifPresent(relationShips::add);
                });
        }
            filteredDomainClasses.getDomainEvents()
                .forEach(de -> {

                    filteredDomainClasses.getServiceKinds()
                        .forEach(s -> {
                                if (s.publishes(de)) {
                                    relationShips.add(mapPublishesDomainEvent(s, de));
                                }
                                if (s.listensTo(de)) {
                                    relationShips.add(mapListensToDomainEvent(s, de));
                                }
                            }
                        );

                    filteredDomainClasses.getAggregateRoots()
                        .forEach(a -> {
                            if (a.publishes(de)) {
                                relationShips.add(mapAggregatePublishesDomainEvent(a, de));
                            }
                            if (a.listensTo(de)) {
                                relationShips.add(mapAggregateListensToDomainEvent(a, de));
                            }
                        });
                });

        return relationShips;
    }

    private NomnomlRelationship mapAggregateRepositoryRelationship(RepositoryMirror repositoryMirror) {
        return NomnomlRelationship
            .builder()
            .fromName(relationConnectorName(repositoryMirror))
            .fromMultiplicity("")
            .fromStyleClassifier(DomainMapperUtils.styleClassifier(repositoryMirror))
            .label("")
            .relationshiptype(NomnomlRelationship.RelationshipType.DIRECTED_DEPENDENCY)
            .toStyleClassifier("<" + DomainDiagramGenerator.AGGREGATE_FRAME_STYLE_TAG + "> ")
            .toMultiplicity("")
            .toName(aggregateFrameName(
                repositoryMirror.getManagedAggregate().map(AggregateRootMirror::getTypeName).orElse("java.lang.Object")))
            .showLabel(this.diagramConfig.getGeneralVisualSettings().isShowRelationshipLabels())
            .showStereotype(this.diagramConfig.getGeneralVisualSettings().isShowRelationshipStereotypes())
            .build();
    }

    private NomnomlRelationship mapQueryHandlerReadModelRelationship(QueryHandlerMirror queryHandlerMirror) {
        return NomnomlRelationship
            .builder()
            .fromName(relationConnectorName(queryHandlerMirror))
            .fromMultiplicity("")
            .fromStyleClassifier(DomainMapperUtils.styleClassifier(queryHandlerMirror))
            .label("")
            .relationshiptype(NomnomlRelationship.RelationshipType.DIRECTED_DEPENDENCY)
            .toStyleClassifier(DomainMapperUtils.styleClassifier(
                queryHandlerMirror.getProvidedReadModel().orElse(null)))
            .toMultiplicity("")
            .toName(relationConnectorName(
                queryHandlerMirror.getProvidedReadModel().orElse(null))
            )
            .showLabel(this.diagramConfig.getGeneralVisualSettings().isShowRelationshipLabels())
            .showStereotype(this.diagramConfig.getGeneralVisualSettings().isShowRelationshipStereotypes())
            .build();
    }

    private NomnomlRelationship mapServiceKindRelationship(
        ServiceKindMirror serviceKindMirrorFrom,
        ServiceKindMirror serviceKindMirrorTo
    ) {
        return NomnomlRelationship
            .builder()
            .fromName(relationConnectorName(serviceKindMirrorFrom))
            .fromMultiplicity("")
            .fromStyleClassifier(DomainMapperUtils.styleClassifier(serviceKindMirrorFrom))
            .label("")
            .relationshiptype(NomnomlRelationship.RelationshipType.DIRECTED_ASSOCIATION)
            .toName(relationConnectorName(serviceKindMirrorTo))
            .toMultiplicity("")
            .toStyleClassifier(DomainMapperUtils.styleClassifier(serviceKindMirrorTo))
            .showLabel(this.diagramConfig.getGeneralVisualSettings().isShowRelationshipLabels())
            .showStereotype(this.diagramConfig.getGeneralVisualSettings().isShowRelationshipStereotypes())
            .build();
    }

    private NomnomlRelationship mapServiceKindToNonDomainRelationship(
        ServiceKindMirror serviceKindMirrorFrom,
        NonDomainTypeMirror nonDomainTypeMirrorTo
    ) {
        return NomnomlRelationship
            .builder()
            .fromName(relationConnectorName(serviceKindMirrorFrom))
            .fromMultiplicity("")
            .fromStyleClassifier(DomainMapperUtils.styleClassifier(serviceKindMirrorFrom))
            .label("")
            .relationshiptype(NomnomlRelationship.RelationshipType.DIRECTED_ASSOCIATION)
            .toName(relationConnectorName(nonDomainTypeMirrorTo))
            .toMultiplicity("")
            .toStyleClassifier(DomainMapperUtils.styleClassifier(nonDomainTypeMirrorTo))
            .showLabel(this.diagramConfig.getGeneralVisualSettings().isShowRelationshipLabels())
            .showStereotype(this.diagramConfig.getGeneralVisualSettings().isShowRelationshipStereotypes())
            .build();
    }

    private NomnomlRelationship mapNonDomainToServiceKindRelationship(
        NonDomainTypeMirror nonDomainTypeMirrorFrom,
        ServiceKindMirror serviceKindMirrorTo
    ) {
        return NomnomlRelationship
            .builder()
            .fromName(relationConnectorName(nonDomainTypeMirrorFrom))
            .fromMultiplicity("")
            .fromStyleClassifier(DomainMapperUtils.styleClassifier(nonDomainTypeMirrorFrom))
            .label("")
            .relationshiptype(NomnomlRelationship.RelationshipType.DIRECTED_ASSOCIATION)
            .toName(relationConnectorName(serviceKindMirrorTo))
            .toMultiplicity("")
            .toStyleClassifier(DomainMapperUtils.styleClassifier(serviceKindMirrorTo))
            .showLabel(this.diagramConfig.getGeneralVisualSettings().isShowRelationshipLabels())
            .showStereotype(this.diagramConfig.getGeneralVisualSettings().isShowRelationshipStereotypes())
            .build();
    }

    private NomnomlRelationship mapListensToDomainEvent(DomainTypeMirror domainTypeMirror,
                                                        DomainEventMirror domainEventMirror) {
        return NomnomlRelationship
            .builder()
            .fromName(relationConnectorName(domainEventMirror))
            .fromMultiplicity("")
            .fromStyleClassifier(DomainMapperUtils.styleClassifier(domainEventMirror))
            .label(notifiesLabel(domainTypeMirror, domainEventMirror))
            .stereotype("notifies")
            .relationshiptype(NomnomlRelationship.RelationshipType.DIRECTED_DEPENDENCY)
            .toName(relationConnectorName(domainTypeMirror))
            .toMultiplicity("")
            .toStyleClassifier(DomainMapperUtils.styleClassifier(domainTypeMirror))
            .showLabel(this.diagramConfig.getGeneralVisualSettings().isShowRelationshipLabels())
            .showStereotype(this.diagramConfig.getGeneralVisualSettings().isShowRelationshipStereotypes())
            .build();
    }

    private NomnomlRelationship mapAggregateListensToDomainEvent(AggregateRootMirror aggregateRootMirror,
                                                                 DomainEventMirror domainEventMirror) {
        return NomnomlRelationship
            .builder()
            .fromName(relationConnectorName(domainEventMirror))
            .fromMultiplicity("")
            .fromStyleClassifier(DomainMapperUtils.styleClassifier(domainEventMirror))
            .label(notifiesLabel(aggregateRootMirror, domainEventMirror))
            .stereotype("notifies")
            .relationshiptype(NomnomlRelationship.RelationshipType.DIRECTED_DEPENDENCY)
            .toName(aggregateFrameName(aggregateRootMirror.getTypeName()))
            .toMultiplicity("")
            .toStyleClassifier(DomainMapperUtils.styleClassifier(null))
            .showLabel(this.diagramConfig.getGeneralVisualSettings().isShowRelationshipLabels())
            .showStereotype(this.diagramConfig.getGeneralVisualSettings().isShowRelationshipStereotypes())
            .build();
    }

    private String notifiesLabel(DomainTypeMirror domainTypeMirror, DomainEventMirror domainEventMirror){
        return domainTypeMirror.getMethods()
            .stream()
            .filter(m->
                m.listensTo(domainEventMirror)
            )
            .map(m ->
                DomainMapperUtils.mapTypeName(domainTypeMirror.getTypeName(), diagramConfig)
                    + "." + m.getName())
            .distinct()
            .collect(Collectors.joining(", "));
    }

    private NomnomlRelationship mapPublishesDomainEvent(DomainTypeMirror domainTypeMirror,
                                                        DomainEventMirror domainEventMirror) {
        return NomnomlRelationship
            .builder()
            .fromName(relationConnectorName(domainTypeMirror))
            .fromMultiplicity("")
            .fromStyleClassifier(DomainMapperUtils.styleClassifier(domainTypeMirror))
            .label(publishesLabel(domainTypeMirror, domainEventMirror))
            .relationshiptype(NomnomlRelationship.RelationshipType.DIRECTED_DEPENDENCY)
            .stereotype("publishes")
            .toName(relationConnectorName(domainEventMirror))
            .toMultiplicity("")
            .toStyleClassifier(DomainMapperUtils.styleClassifier(domainEventMirror))
            .showLabel(this.diagramConfig.getGeneralVisualSettings().isShowRelationshipLabels())
            .showStereotype(this.diagramConfig.getGeneralVisualSettings().isShowRelationshipStereotypes())
            .build();
    }

    private NomnomlRelationship mapAggregatePublishesDomainEvent(AggregateRootMirror aggregateRootMirror,
                                                                 DomainEventMirror domainEventMirror) {
        return NomnomlRelationship
            .builder()
            .fromName(
                aggregateFrameName(aggregateRootMirror.getTypeName()))
            .fromMultiplicity("")
            .fromStyleClassifier("<" + DomainDiagramGenerator.AGGREGATE_FRAME_STYLE_TAG + "> ")
            .label(publishesLabel(aggregateRootMirror, domainEventMirror))
            .stereotype("publishes")
            .relationshiptype(NomnomlRelationship.RelationshipType.DIRECTED_DEPENDENCY)
            .toName(relationConnectorName(domainEventMirror))
            .toMultiplicity("")
            .toStyleClassifier(DomainMapperUtils.styleClassifier(domainEventMirror))
            .showLabel(this.diagramConfig.getGeneralVisualSettings().isShowRelationshipLabels())
            .showStereotype(this.diagramConfig.getGeneralVisualSettings().isShowRelationshipStereotypes())
            .build();
    }

    private String publishesLabel(DomainTypeMirror domainTypeMirror, DomainEventMirror domainEventMirror){
        return domainTypeMirror.getMethods()
            .stream()
            .filter(m->
                m.publishes(domainEventMirror)
            )
            .map(m ->
                DomainMapperUtils.mapTypeName(domainTypeMirror.getTypeName(), diagramConfig)
                    + "." + m.getName())
            .distinct()
            .collect(Collectors.joining(", "));
    }

    private NomnomlRelationship mapDomainCommandProcessorRelationship(DomainCommandMirror domainCommandMirror,
                                                                      DomainTypeMirror processorMirror) {
        return NomnomlRelationship
            .builder()
            .fromName(relationConnectorName(domainCommandMirror))
            .fromMultiplicity("")
            .fromStyleClassifier(DomainMapperUtils.styleClassifier(domainCommandMirror))
            .label(processorLabel(processorMirror, domainCommandMirror))
            .stereotype("is processed by")
            .relationshiptype(NomnomlRelationship.RelationshipType.DIRECTED_DEPENDENCY)
            .toName(relationConnectorName(processorMirror))
            .toMultiplicity("")
            .toStyleClassifier(DomainMapperUtils.styleClassifier(processorMirror))
            .showLabel(this.diagramConfig.getGeneralVisualSettings().isShowRelationshipLabels())
            .showStereotype(this.diagramConfig.getGeneralVisualSettings().isShowRelationshipStereotypes())
            .build();
    }

    private NomnomlRelationship mapDomainCommandAggregateRelationship(DomainCommandMirror domainCommandMirror,
                                                                      AggregateRootMirror aggregateRootMirror) {
        return NomnomlRelationship
            .builder()
            .fromName(relationConnectorName(domainCommandMirror))
            .fromMultiplicity("")
            .fromStyleClassifier(DomainMapperUtils.styleClassifier(domainCommandMirror))
            .label(processorLabel(aggregateRootMirror, domainCommandMirror))
            .stereotype("is processed by")
            .relationshiptype(NomnomlRelationship.RelationshipType.DIRECTED_DEPENDENCY)
            .toStyleClassifier("<" + DomainDiagramGenerator.AGGREGATE_FRAME_STYLE_TAG + "> ")
            .toMultiplicity("")
            .toName(aggregateFrameName(aggregateRootMirror.getTypeName()))
            .showLabel(this.diagramConfig.getGeneralVisualSettings().isShowRelationshipLabels())
            .showStereotype(this.diagramConfig.getGeneralVisualSettings().isShowRelationshipStereotypes())
            .build();
    }

    private String processorLabel(DomainTypeMirror domainTypeMirror, DomainCommandMirror domainCommandMirror){
        return domainTypeMirror.getMethods()
            .stream()
            .filter(m->
                m.processes(domainCommandMirror)
                && AccessLevel.PUBLIC.equals(m.getAccessLevel())
            )
            .map(m ->
                DomainMapperUtils.mapTypeName(domainTypeMirror.getTypeName(), diagramConfig)
                    + "." + m.getName()
            )
            .distinct()
            .collect(Collectors.joining(", "));
    }

    /**
     * Derives a list of {@link NomnomlRelationship} objects for all aggregate relationships
     * defined within the given {@code AggregateRootMirror}.
     *
     * @param aggregateRootMirror the aggregate root that provides the domain references to map relationships from
     * @return a list of {@link NomnomlRelationship} representing the relationships derived from the aggregate
     */
    public List<NomnomlRelationship> mapAllAggregateRelationships(AggregateRootMirror aggregateRootMirror) {
        var relationShips = new ArrayList<NomnomlRelationship>();
        var visitor = new ContextDomainObjectVisitor(aggregateRootMirror) {

            /**
             * {inheritDoc}
             */
            @Override
            public void visitEntityReference(EntityReferenceMirror entityReferenceMirror) {
                relationShips.add(mapEntityReference(entityReferenceMirror));
            }

            /**
             * {inheritDoc}
             */
            @Override
            public void visitValueReference(ValueReferenceMirror valueReferenceMirror) {
                if (!DomainMapperUtils.showPropertyInline(valueReferenceMirror, aggregateRootMirror, diagramConfig)) {
                    relationShips.add(mapValueReference(valueReferenceMirror));
                }
            }

            /**
             * {inheritDoc}
             */
            @Override
            public void visitEnterAnyDomainType(DomainTypeMirror domainTypeMirror) {
                if(diagramConfig.getGeneralVisualSettings().isShowInheritanceStructuresInAggregates()
                    || diagramConfig.getGeneralVisualSettings().isShowAllInheritanceStructures()) {
                    mapInheritance(domainTypeMirror).ifPresent(relationShips::add);
                    relationShips.addAll(mapImplementsInterface(domainTypeMirror));
                }
            }
        };
        visitor.start();

        return relationShips;
    }

    private Optional<NomnomlRelationship> mapInheritance(DomainTypeMirror domainTypeMirror) {
            if(domainTypeMirror.getInheritanceHierarchyTypeNames() != null && !domainTypeMirror.getInheritanceHierarchyTypeNames().isEmpty()){
                var superClassName = domainTypeMirror.getInheritanceHierarchyTypeNames().get(0);
                if(!superClassName.startsWith("io.domainlifecycles")) {
                    var superType = domainMirror.getDomainTypeMirror(superClassName);
                    if (superType.isPresent() && filteredDomainClasses.contains(superType.get())) {
                        return Optional.of(
                            NomnomlRelationship
                                .builder()
                                .fromName(relationConnectorName(superClassName))
                                .fromStyleClassifier(DomainMapperUtils.styleClassifier(domainMirror.getDomainTypeMirror(superClassName).orElse(null)))
                                .fromMultiplicity("")
                                .label("")
                                .toName(relationConnectorName(domainTypeMirror))
                                .toStyleClassifier(DomainMapperUtils.styleClassifier(domainTypeMirror))
                                .toMultiplicity("")
                                .relationshiptype(NomnomlRelationship.RelationshipType.INHERITANCE)
                                .showLabel(this.diagramConfig.getGeneralVisualSettings().isShowRelationshipLabels())
                                .showStereotype(this.diagramConfig.getGeneralVisualSettings().isShowRelationshipStereotypes())
                                .build()
                        );
                    }
                }
            }
        return Optional.empty();
    }

    private List<NomnomlRelationship> mapImplementsInterface(DomainTypeMirror domainTypeMirror) {
        if(domainTypeMirror.getAllInterfaceTypeNames() != null){
            return domainTypeMirror.getAllInterfaceTypeNames()
                .stream()
                .filter(interfaceName -> !interfaceName.startsWith("io.domainlifecycles"))
                .map(domainMirror::getDomainTypeMirror)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(filteredDomainClasses::contains)
                .map(i -> NomnomlRelationship
                                .builder()
                                .fromName(relationConnectorName(i))
                                .fromStyleClassifier(DomainMapperUtils.styleClassifier(i))
                                .fromMultiplicity("")
                                .label("")
                                .toName(relationConnectorName(domainTypeMirror))
                                .toStyleClassifier(DomainMapperUtils.styleClassifier(domainTypeMirror))
                                .toMultiplicity("")
                                .relationshiptype(NomnomlRelationship.RelationshipType.INHERITANCE)
                                .showLabel(this.diagramConfig.getGeneralVisualSettings().isShowRelationshipLabels())
                                .showStereotype(this.diagramConfig.getGeneralVisualSettings().isShowRelationshipStereotypes())
                                .build()
                        )
                .toList();
        }
        return Collections.emptyList();
    }

    private NomnomlRelationship mapEntityReference(EntityReferenceMirror entityReferenceMirror) {

        var toMultiplicity = toMultiplicity(entityReferenceMirror);
        var label = entityReferenceMirror.getName();
        if (diagramConfig.getGeneralVisualSettings().isMultiplicityInLabel()) {
            label = label + " " + toMultiplicity;
            toMultiplicity = "";
        }

        return NomnomlRelationship
            .builder()
            .fromName(relationConnectorName(entityReferenceMirror.getDeclaredByTypeName()))
            .fromMultiplicity("")
            .fromStyleClassifier(DomainMapperUtils.styleClassifier(domainMirror.getDomainTypeMirror(entityReferenceMirror.getDeclaredByTypeName()).orElse(null)))
            .label(label)
            .toStyleClassifier(DomainMapperUtils.styleClassifier(domainMirror.getDomainTypeMirror(entityReferenceMirror.getType().getTypeName()).orElse(null)))
            .toMultiplicity(toMultiplicity)
            .toName(relationConnectorName(entityReferenceMirror.getType().getTypeName()))
            .relationshiptype(NomnomlRelationship.RelationshipType.COMPOSITION)
            .showLabel(this.diagramConfig.getGeneralVisualSettings().isShowRelationshipLabels())
            .showStereotype(this.diagramConfig.getGeneralVisualSettings().isShowRelationshipStereotypes())
            .build();
    }

    private NomnomlRelationship mapValueReference(ValueReferenceMirror valueReferenceMirror) {

        var toMultiplicity = toMultiplicity(valueReferenceMirror);
        var label = valueReferenceMirror.getName();
        if (diagramConfig.getGeneralVisualSettings().isMultiplicityInLabel()) {
            label = label + " " + toMultiplicity;
            toMultiplicity = "";
        }

        return NomnomlRelationship
            .builder()
            .fromName(relationConnectorName(valueReferenceMirror.getDeclaredByTypeName()))
            .fromMultiplicity("")
            .fromStyleClassifier(DomainMapperUtils.styleClassifier(domainMirror.getDomainTypeMirror(valueReferenceMirror.getDeclaredByTypeName()).orElse(null)))
            .label(label)
            .toStyleClassifier(DomainMapperUtils.styleClassifier(domainMirror.getDomainTypeMirror(valueReferenceMirror.getType().getTypeName()).orElse(null)))
            .toMultiplicity(toMultiplicity)
            .toName(relationConnectorName(valueReferenceMirror.getType().getTypeName()))
            .relationshiptype(NomnomlRelationship.RelationshipType.COMPOSITION)
            .showLabel(this.diagramConfig.getGeneralVisualSettings().isShowRelationshipLabels())
            .showStereotype(this.diagramConfig.getGeneralVisualSettings().isShowRelationshipStereotypes())
            .build();
    }

    private NomnomlRelationship mapContainedReadModel(DomainTypeMirror readModelMirror, FieldMirror fieldMirror) {

        var toMultiplicity = toMultiplicity(fieldMirror);
        var label = fieldMirror.getName();
        if (diagramConfig.getGeneralVisualSettings().isMultiplicityInLabel()) {
            label = label + " " + toMultiplicity;
            toMultiplicity = "";
        }

        return NomnomlRelationship
            .builder()
            .fromName(relationConnectorName(readModelMirror))
            .fromMultiplicity("")
            .fromStyleClassifier(DomainMapperUtils.styleClassifier(readModelMirror))
            .label(label)
            .toStyleClassifier(DomainMapperUtils.styleClassifier(domainMirror.getDomainTypeMirror(fieldMirror.getType().getTypeName()).orElse(null)))
            .toMultiplicity(toMultiplicity)
            .toName(relationConnectorName(fieldMirror.getType().getTypeName()))
            .relationshiptype(NomnomlRelationship.RelationshipType.COMPOSITION)
            .showLabel(this.diagramConfig.getGeneralVisualSettings().isShowRelationshipLabels())
            .showStereotype(this.diagramConfig.getGeneralVisualSettings().isShowRelationshipStereotypes())
            .build();
    }

    private List<NomnomlRelationship> mapFrameIdReferences(ValueReferenceMirror idReferenceMirror) {
        var declaringAggregates = filteredDomainClasses
            .getAggregateRoots()
            .stream()
            .filter(aggregateRootMirror -> {
                final var contained = new AtomicBoolean(false);
                var visitor = new ContextDomainObjectVisitor(aggregateRootMirror) {
                    @Override
                    public void visitValueReference(ValueReferenceMirror valueReferenceMirror) {
                        if (idReferenceMirror.equals(valueReferenceMirror)) {
                            contained.set(true);
                        }
                    }
                };
                visitor.start();
                return contained.get();
            })
            .toList();
        var targetAggregate = filteredDomainClasses
            .getAggregateRoots()
            .stream()
            .filter(aggregateRootMirror -> {
                var identity = aggregateRootMirror.getIdentityField();
                return identity.map(fieldMirror -> fieldMirror.getType().getTypeName().equals(idReferenceMirror.getValue().getTypeName())).orElse(false);
            })
            .findFirst();
        if (!declaringAggregates.isEmpty() && targetAggregate.isPresent()) {
            return declaringAggregates
                .stream()
                .filter(decl -> !decl.getTypeName().equals(targetAggregate.get().getTypeName()))
                .map(da -> NomnomlRelationship
                    .builder()
                    .fromName(aggregateFrameName(da.getTypeName()))
                    .fromMultiplicity("")
                    .fromStyleClassifier("<" + DomainDiagramGenerator.AGGREGATE_FRAME_STYLE_TAG + "> ")
                    .label(DomainMapperUtils.mapTypeName(idReferenceMirror.getDeclaredByTypeName(),
                        diagramConfig) + "." + idReferenceMirror.getName())
                    .stereotype("id-ref")
                    .toStyleClassifier("<" + DomainDiagramGenerator.AGGREGATE_FRAME_STYLE_TAG + "> ")
                    .toMultiplicity("")
                    .toName(aggregateFrameName(targetAggregate.get().getTypeName()))
                    .relationshiptype(NomnomlRelationship.RelationshipType.DIRECTED_DEPENDENCY)
                    .showLabel(this.diagramConfig.getGeneralVisualSettings().isShowRelationshipLabels())
                    .showStereotype(this.diagramConfig.getGeneralVisualSettings().isShowRelationshipStereotypes())
                    .build())
                .toList();
        }
        return Collections.emptyList();
    }

    private String relationConnectorName(DomainTypeMirror domainTypeMirror) {
        return nodeNames.name(domainTypeMirror) + connectorStereotype(domainTypeMirror.getTypeName());

    }

    private String relationConnectorName(String typeName) {
        return nodeNames.name(typeName) + connectorStereotype(typeName);
    }

    private String aggregateFrameName(String aggregateRootTypeName) {
        return nodeNames.name(aggregateRootTypeName) + " <<Aggregate>>";
    }

    private String connectorStereotype(String typeName) {
        var domainTypeMirror = domainMirror.getDomainTypeMirror(typeName);
        if (domainTypeMirror.isPresent()) {
            var stereotype = DomainMapperUtils.stereotype(domainTypeMirror.get(), diagramConfig);
            if (!"".equals(stereotype)) {
                return " <<" + stereotype + ">>";
            }
        }
        return "";
    }

    private String toMultiplicity(FieldMirror propertyMirror) {
        if (propertyMirror.getType().hasOptionalContainer()) {
            return "0..1";
        } else if (propertyMirror.getType().hasCollectionContainer()) {
            var maxBySizeAssertions = propertyMirror
                .getType()
                .getContainerAssertions()
                .stream()
                .filter(a -> AssertionType.hasSize.equals(a.getAssertionType()))
                .map(a -> {
                    try {
                        return Integer.valueOf(a.getParam2());
                    } catch (Throwable t) {
                        return Integer.MAX_VALUE;
                    }
                })
                .min(Comparator.naturalOrder());

            var minBySizeAssertions = propertyMirror
                .getType()
                .getContainerAssertions()
                .stream()
                .filter(a -> AssertionType.hasSize.equals(a.getAssertionType()))
                .map(a -> {
                    try {
                        return Integer.valueOf(a.getParam2());
                    } catch (Throwable t) {
                        return 0;
                    }
                })
                .min(Comparator.naturalOrder());

            var minByMinAssertions = propertyMirror
                .getType()
                .getContainerAssertions()
                .stream()
                .filter(a -> AssertionType.hasSizeMin.equals(a.getAssertionType()))
                .map(a -> {
                    try {
                        return Integer.valueOf(a.getParam1());
                    } catch (Throwable t) {
                        return 0;
                    }
                })
                .max(Comparator.naturalOrder());

            var maxByMaxAssertions = propertyMirror
                .getType()
                .getContainerAssertions()
                .stream()
                .filter(a -> AssertionType.hasSizeMax.equals(a.getAssertionType()))
                .map(a -> {
                    try {
                        return Integer.valueOf(a.getParam2());
                    } catch (Throwable t) {
                        return Integer.MAX_VALUE;
                    }
                })
                .min(Comparator.naturalOrder());

            var minByNotEmptyAssertion = propertyMirror
                .getType()
                .getContainerAssertions()
                .stream()
                .filter(a -> AssertionType.isNotEmptyIterable.equals(a.getAssertionType()))
                .findAny()
                .map(a -> 1);

            int min = 0;
            int max = Integer.MAX_VALUE;

            if (minByNotEmptyAssertion.isPresent()) {
                min = 1;
            }
            if (minByMinAssertions.isPresent()) {
                if (minByMinAssertions.get() > min) {
                    min = minByMinAssertions.get();
                }
            }
            if (minBySizeAssertions.isPresent()) {
                if (minBySizeAssertions.get() > min) {
                    min = minBySizeAssertions.get();
                }
            }
            if (maxByMaxAssertions.isPresent()) {
                if (maxByMaxAssertions.get() < max) {
                    max = maxByMaxAssertions.get();
                }
            }
            if (maxBySizeAssertions.isPresent()) {
                if (maxBySizeAssertions.get() < max) {
                    max = maxBySizeAssertions.get();
                }
            }
            return min + ".." + (max == Integer.MAX_VALUE ? "*" : max);
        } else {
            var notNullAssertion = propertyMirror
                .getType()
                .getAssertions()
                .stream()
                .filter(a -> AssertionType.isNotNull.equals(a.getAssertionType()))
                .findAny();
            if (notNullAssertion.isPresent()) {
                return "1";
            }
            return "0..1";
        }
    }


}

