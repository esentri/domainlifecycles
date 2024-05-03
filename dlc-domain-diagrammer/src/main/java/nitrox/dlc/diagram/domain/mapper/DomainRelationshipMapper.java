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

import nitrox.dlc.diagram.domain.DomainDiagramGenerator;
import nitrox.dlc.diagram.domain.config.DomainDiagramConfig;
import nitrox.dlc.diagram.nomnoml.NomnomlRelationship;
import nitrox.dlc.mirror.api.AggregateRootMirror;
import nitrox.dlc.mirror.api.ApplicationServiceMirror;
import nitrox.dlc.mirror.api.Domain;
import nitrox.dlc.mirror.api.DomainCommandMirror;
import nitrox.dlc.mirror.api.DomainCommandProcessingMirror;
import nitrox.dlc.mirror.api.DomainEventMirror;
import nitrox.dlc.mirror.api.DomainServiceMirror;
import nitrox.dlc.mirror.api.DomainType;
import nitrox.dlc.mirror.api.DomainTypeMirror;
import nitrox.dlc.mirror.api.EntityMirror;
import nitrox.dlc.mirror.api.EntityReferenceMirror;
import nitrox.dlc.mirror.api.FieldMirror;
import nitrox.dlc.mirror.api.OutboundServiceMirror;
import nitrox.dlc.mirror.api.QueryClientMirror;
import nitrox.dlc.mirror.api.ReadModelMirror;
import nitrox.dlc.mirror.api.RepositoryMirror;
import nitrox.dlc.mirror.api.ValueReferenceMirror;
import nitrox.dlc.mirror.model.AssertionType;
import nitrox.dlc.mirror.visitor.ContextDomainObjectVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Derives the {@link NomnomlRelationship} representations from the given domain structures delivered as mirrors.
 *
 * @author Mario Herb
 */
public class DomainRelationshipMapper {

    private final DomainDiagramConfig diagramConfig;

    private final FilteredDomainClasses filteredDomainClasses;


    /**
     * Initializes the DomainRelationshipMapper with a given {@link DomainDiagramConfig} and the {@link FilteredDomainClasses}
     */
    public DomainRelationshipMapper(DomainDiagramConfig diagramConfig, FilteredDomainClasses filteredDomainClasses) {
        this.diagramConfig = diagramConfig;
        this.filteredDomainClasses = filteredDomainClasses;
    }

    /**
     * Derives a {@link NomnomlRelationship} for all ApplicationServices that use a Repository.
     */
    public List<NomnomlRelationship> mapAllApplicationServiceServiceRepositoryRelationships(){
        var relationShips = new ArrayList<NomnomlRelationship>();
        if(diagramConfig.isShowRepositories() && diagramConfig.isShowApplicationServices()) {
            filteredDomainClasses
                .getApplicationServices()
                .forEach(as -> as.getReferencedRepositories().forEach(
                    r -> relationShips.add(mapApplicationServiceRepositoryRelationship(as, r))
                ));
        }
        return relationShips;
    }

    /**
     * Derives a {@link NomnomlRelationship} for all ApplicationServices that use a Domain Service.
     */
    public List<NomnomlRelationship> mapAllApplicationServiceServiceDomainServiceRelationships(){
        var relationShips = new ArrayList<NomnomlRelationship>();
        if(diagramConfig.isShowApplicationServices() && diagramConfig.isShowDomainServices()) {
            filteredDomainClasses.getApplicationServices()
                .forEach(as -> as.getReferencedDomainServices().forEach(
                    ds -> relationShips.add(mapApplicationServiceDomainServiceRelationship(as, ds))
                ));
        }
        return relationShips;
    }

    /**
     * Derives a {@link NomnomlRelationship} for all ApplicationServices that use a QueryClient.
     */
    public List<NomnomlRelationship> mapAllApplicationServiceQueryClientRelationships(){
        var relationShips = new ArrayList<NomnomlRelationship>();
        if(diagramConfig.isShowApplicationServices() && diagramConfig.isShowQueryClients()) {
            filteredDomainClasses.getApplicationServices()
                .forEach(as -> as.getReferencedQueryClients().forEach(
                    ds -> relationShips.add(mapApplicationServiceQueryClientRelationship(as, ds))
                ));
        }
        return relationShips;
    }

    /**
     * Derives a {@link NomnomlRelationship} for all ApplicationServices that use an OutboundService.
     */
    public List<NomnomlRelationship> mapAllApplicationServiceOutboundServiceRelationships(){
        var relationShips = new ArrayList<NomnomlRelationship>();
        if(diagramConfig.isShowApplicationServices() && diagramConfig.isShowOutboundServices()) {
            filteredDomainClasses.getApplicationServices()
                .forEach(as -> as.getReferencedOutboundServices().forEach(
                    ds -> relationShips.add(mapApplicationServiceOutboundServiceRelationship(as, ds))
                ));
        }
        return relationShips;
    }

    /**
     * Derives a {@link NomnomlRelationship} for all DomainServices that use a Repository.
     */
    public List<NomnomlRelationship> mapAllDomainServiceRepositoryRelationships(){
        var relationShips = new ArrayList<NomnomlRelationship>();
        if(diagramConfig.isShowRepositories() && diagramConfig.isShowDomainServices()) {
            filteredDomainClasses.getDomainServices()
                .forEach(ds -> ds.getReferencedRepositories().forEach(
                    r -> relationShips.add(mapDomainServiceRepositoryRelationship(ds, r))
                ));
        }
        return relationShips;
    }

    /**
     * Derives a {@link NomnomlRelationship} for all DomainServices that use a QueryClient.
     */
    public List<NomnomlRelationship> mapAllDomainServiceQueryClientRelationships(){
        var relationShips = new ArrayList<NomnomlRelationship>();
        if(diagramConfig.isShowRepositories() && diagramConfig.isShowQueryClients()) {
            filteredDomainClasses.getDomainServices()
                .forEach(ds -> ds.getReferencedQueryClients().forEach(
                    r -> relationShips.add(mapDomainServiceQueryClientRelationship(ds, r))
                ));
        }
        return relationShips;
    }

    /**
     * Derives a {@link NomnomlRelationship} for all DomainServices that use an OutboundService.
     */
    public List<NomnomlRelationship> mapAllDomainServiceOutboundServiceRelationships(){
        var relationShips = new ArrayList<NomnomlRelationship>();
        if(diagramConfig.isShowRepositories() && diagramConfig.isShowOutboundServices()) {
            filteredDomainClasses.getDomainServices()
                .forEach(ds -> ds.getReferencedOutboundServices().forEach(
                    r -> relationShips.add(mapDomainServiceOutboundServiceRelationship(ds, r))
                ));
        }
        return relationShips;
    }

    /**
     * Derives a {@link NomnomlRelationship} for all Aggregates to their Repository.
     */
    public List<NomnomlRelationship> mapAllAggregateRepositoryRelationships(){
        var relationShips = new ArrayList<NomnomlRelationship>();
        if(diagramConfig.isShowRepositories()) {
            filteredDomainClasses.getRepositories()
                .forEach(r -> relationShips.add(mapAggregateRepositoryRelationship(r)));
        }
        return relationShips;
    }

    /**
     * Derives a {@link NomnomlRelationship} for all QueryClients to their ReadModels.
     */
    public List<NomnomlRelationship> mapAllQueryClientReadModelRelationships(){
        var relationShips = new ArrayList<NomnomlRelationship>();
        if(diagramConfig.isShowQueryClients()&&diagramConfig.isShowReadModels()) {
            filteredDomainClasses.getQueryClients()
                .forEach(r -> relationShips.add(mapQueryClientReadModelRelationship(r)));
        }
        return relationShips;
    }

    /**
     * Derives a {@link NomnomlRelationship} for all Aggregates pointing to another Aggregate by an Id Reference.
     */
    public List<NomnomlRelationship> mapAllAggregateFrameRelationships(){
        var relationShips = new ArrayList<NomnomlRelationship>();
        filteredDomainClasses.getAggregateRoots()
            .forEach(ar -> {
                var visitor = new ContextDomainObjectVisitor(ar){
                    @Override
                    public void visitValueReference(ValueReferenceMirror valueReferenceMirror) {
                        if(valueReferenceMirror.getValue().isIdentity() && !valueReferenceMirror.isIdentityField()){
                            relationShips.addAll(mapIdReferences(valueReferenceMirror));
                        }
                    }
                };
                visitor.start();
            });
        for (NomnomlRelationship rel : relationShips){
            var startIndex = relationShips.indexOf(rel);
            for(var i=startIndex; i<relationShips.size(); i++){
                var compared = relationShips.get(i);
                if(compared.getFromName().equals(rel.getToName()) && compared.getToName().equals(rel.getFromName())){
                    compared.transpose();
                }
            }
        }

        return relationShips;
    }

    /**
     * Derives a {@link NomnomlRelationship} for all Aggregates
     * or services processing Domain Commands.
     */
    public List<NomnomlRelationship> mapAllDomainCommandRelationships(){
        var relationShips = new ArrayList<NomnomlRelationship>();
        if(diagramConfig.isShowDomainCommands()) {
            filteredDomainClasses.getDomainCommands()
                .forEach(c -> {
                    filteredDomainClasses.getAggregateRoots()
                        .forEach(
                        ar -> {
                            if (ar.processes(c)) {
                                if(!diagramConfig.isShowOnlyTopLevelDomainCommandRelations() || isTopLevelConsumerForCommand(ar, c)){
                                    relationShips.add(mapDomainCommandAggregateRelationship(c, ar));
                                }
                            }
                        }
                    );

                    if (diagramConfig.isShowDomainServices()) {
                        filteredDomainClasses.getDomainServices()
                            .forEach(
                                ds -> {
                                    if (ds.processes(c)) {
                                        if(!diagramConfig.isShowOnlyTopLevelDomainCommandRelations() || isTopLevelConsumerForCommand(ds, c)){
                                            relationShips.add(mapDomainCommandDomainServiceRelationship(c, ds));
                                        }
                                    }
                                }
                            );
                    }

                    if (diagramConfig.isShowApplicationServices()) {
                        filteredDomainClasses.getApplicationServices()
                            .forEach(
                                as -> {
                                    if (as.processes(c)) {
                                        if(!diagramConfig.isShowOnlyTopLevelDomainCommandRelations() || isTopLevelConsumerForCommand(as, c)){
                                            relationShips.add(mapDomainCommandApplicationServiceRelationship(c, as));
                                        }
                                    }
                                }
                            );
                    }
                    if (diagramConfig.isShowQueryClients()) {
                        filteredDomainClasses.getQueryClients()
                            .forEach(
                                as -> {
                                    if (as.processes(c)) {
                                        if(!diagramConfig.isShowOnlyTopLevelDomainCommandRelations() || isTopLevelConsumerForCommand(as, c)){
                                            relationShips.add(mapDomainCommandQueryClientRelationship(c, as));
                                        }
                                    }
                                }
                            );
                    }
                    if (diagramConfig.isShowOutboundServices()) {
                        filteredDomainClasses.getOutboundServices()
                            .forEach(
                                as -> {
                                    if (as.processes(c)) {
                                        if(!diagramConfig.isShowOnlyTopLevelDomainCommandRelations() || isTopLevelConsumerForCommand(as, c)){
                                            relationShips.add(mapDomainCommandOutboundServiceRelationship(c, as));
                                        }
                                    }
                                }
                            );
                    }

                });
        }
        return relationShips;
    }

    private boolean isTopLevelConsumerForCommand(DomainTypeMirror domainTypeMirror, DomainCommandMirror domainCommandMirror){
        var typesReferencing = Domain.getInitializedDomain().allTypeMirrors().values()
            .stream()
            .filter(dt -> {
                return dt.getAllFields()
                        .stream()
                        .anyMatch(fm ->{
                            return fm.getType().getTypeName().equals(domainTypeMirror.getTypeName())
                                || domainTypeMirror.getAllInterfaceTypeNames().stream().anyMatch(it -> fm.getType().getTypeName().equals(it));
                        });
                }

            ).toList();
        if(domainTypeMirror.getDomainType().equals(DomainType.AGGREGATE_ROOT)){
            return Domain.getInitializedDomain().allTypeMirrors()
                .values()
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
        for( var referencingType : typesReferencing){
            switch (referencingType.getDomainType()){
                case AGGREGATE_ROOT, ENTITY -> {
                    return !((EntityMirror) referencingType).processes(domainCommandMirror);
                }
                case REPOSITORY -> {
                    return !((RepositoryMirror) referencingType).processes(domainCommandMirror);
                }
                case DOMAIN_SERVICE -> {
                    return !((DomainServiceMirror) referencingType).processes(domainCommandMirror);
                }
                case APPLICATION_SERVICE -> {
                    return !((ApplicationServiceMirror) referencingType).processes(domainCommandMirror);
                }
                case OUTBOUND_SERVICE -> {
                    return !((OutboundServiceMirror) referencingType).processes(domainCommandMirror);
                }
                case QUERY_CLIENT -> {
                    return !((QueryClientMirror) referencingType).processes(domainCommandMirror);
                }
                default -> {
                    return true;
                }
            }
        }
        return true;
    }

    /**
     * Derives a {@link NomnomlRelationship} for all Aggregates
     * or Domain Services or Application Services or Repositories publishing or listening to Domain Events.
     */
    public List<NomnomlRelationship> mapAllDomainEventRelationships(){
        var relationShips = new ArrayList<NomnomlRelationship>();
        if(diagramConfig.isShowDomainEvents()) {
            filteredDomainClasses.getDomainEvents()
                .forEach(de -> {
                if(diagramConfig.isShowApplicationServices()) {
                    filteredDomainClasses.getApplicationServices()
                        .forEach(as -> {
                        if (as.publishes(de)) {
                            relationShips.add(mapPublishesDomainEvent(as, de));
                        }
                        if (as.listensTo(de)) {
                            relationShips.add(mapListensToDomainEvent(as, de));
                        }
                    });
                }
                if(diagramConfig.isShowRepositories()) {
                    filteredDomainClasses.getRepositories()
                        .forEach(r -> {
                        if (r.publishes(de)) {
                            relationShips.add(mapPublishesDomainEvent(r, de));
                        }
                        if (r.listensTo(de)) {
                            relationShips.add(mapListensToDomainEvent(r, de));
                        }

                    });
                }
                if(diagramConfig.isShowDomainServices()) {
                    filteredDomainClasses
                        .getDomainServices()
                        .forEach(ds -> {
                        if (ds.publishes(de)) {
                            relationShips.add(mapPublishesDomainEvent(ds, de));
                        }
                        if (ds.listensTo(de)) {
                            relationShips.add(mapListensToDomainEvent(ds, de));
                        }
                    });
                }
                if(diagramConfig.isShowOutboundServices()) {
                    filteredDomainClasses
                        .getOutboundServices()
                        .forEach(ds -> {
                            if (ds.publishes(de)) {
                                relationShips.add(mapPublishesDomainEvent(ds, de));
                            }
                            if (ds.listensTo(de)) {
                                relationShips.add(mapListensToDomainEvent(ds, de));
                            }
                        });
                }
                if(diagramConfig.isShowQueryClients()) {
                    filteredDomainClasses
                        .getQueryClients()
                        .forEach(ds -> {
                            if (ds.publishes(de)) {
                                relationShips.add(mapPublishesDomainEvent(ds, de));
                            }
                            if (ds.listensTo(de)) {
                                relationShips.add(mapListensToDomainEvent(ds, de));
                            }
                        });
                }
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
        }
        return relationShips;
    }

    private NomnomlRelationship mapAggregateRepositoryRelationship(RepositoryMirror repositoryMirror){
        return NomnomlRelationship
            .builder()
            .fromName(relationConnectorName(repositoryMirror))
            .fromMultiplicity("")
            .fromStyleClassifier(DomainMapperUtils.styleClassifier(repositoryMirror.getTypeName()))
            .label("")
            .relationshiptype(NomnomlRelationship.RelationshipType.ASSOCIATION)
            .toStyleClassifier("<"+DomainDiagramGenerator.AGGREGATE_FRAME_STYLE_TAG+"> ")
            .toMultiplicity("")
            .toName(DomainMapperUtils.mapTypeName(repositoryMirror.getManagedAggregate().map(AggregateRootMirror::getTypeName).orElse("java.lang.Object"), diagramConfig) + " <<Aggregate>>")
            .build();
    }

    private NomnomlRelationship mapQueryClientReadModelRelationship(QueryClientMirror queryClientMirror){
        return NomnomlRelationship
            .builder()
            .fromName(relationConnectorName(queryClientMirror))
            .fromMultiplicity("")
            .fromStyleClassifier(DomainMapperUtils.styleClassifier(queryClientMirror.getTypeName()))
            .label("")
            .relationshiptype(NomnomlRelationship.RelationshipType.ASSOCIATION)
            .toStyleClassifier(DomainMapperUtils.styleClassifier(queryClientMirror.getProvidedReadModel().map(ReadModelMirror::getTypeName).orElse("java.lang.Object")))
            .toMultiplicity("")
            .toName(relationConnectorName(queryClientMirror.getProvidedReadModel().map(ReadModelMirror::getTypeName).orElse("java.lang.Object")))
            .build();
    }

    private NomnomlRelationship mapDomainServiceRepositoryRelationship(
        DomainServiceMirror domainServiceMirror,
        RepositoryMirror repositoryMirror
    ){
        return NomnomlRelationship
            .builder()
            .fromName(relationConnectorName(domainServiceMirror))
            .fromMultiplicity("")
            .fromStyleClassifier(DomainMapperUtils.styleClassifier(domainServiceMirror.getTypeName()))
            .label("")
            .relationshiptype(NomnomlRelationship.RelationshipType.ASSOCIATION)
            .toName(relationConnectorName(repositoryMirror))
            .toMultiplicity("")
            .toStyleClassifier(DomainMapperUtils.styleClassifier(repositoryMirror.getTypeName()))
            .build();
    }

    private NomnomlRelationship mapDomainServiceQueryClientRelationship(
        DomainServiceMirror domainServiceMirror,
        QueryClientMirror queryClientMirror
    ){
        return NomnomlRelationship
            .builder()
            .fromName(relationConnectorName(domainServiceMirror))
            .fromMultiplicity("")
            .fromStyleClassifier(DomainMapperUtils.styleClassifier(domainServiceMirror.getTypeName()))
            .label("")
            .relationshiptype(NomnomlRelationship.RelationshipType.ASSOCIATION)
            .toName(relationConnectorName(queryClientMirror))
            .toMultiplicity("")
            .toStyleClassifier(DomainMapperUtils.styleClassifier(queryClientMirror.getTypeName()))
            .build();
    }

    private NomnomlRelationship mapDomainServiceOutboundServiceRelationship(
        DomainServiceMirror domainServiceMirror,
        OutboundServiceMirror outboundServiceMirror
    ){
        return NomnomlRelationship
            .builder()
            .fromName(relationConnectorName(domainServiceMirror))
            .fromMultiplicity("")
            .fromStyleClassifier(DomainMapperUtils.styleClassifier(domainServiceMirror.getTypeName()))
            .label("")
            .relationshiptype(NomnomlRelationship.RelationshipType.ASSOCIATION)
            .toName(relationConnectorName(outboundServiceMirror))
            .toMultiplicity("")
            .toStyleClassifier(DomainMapperUtils.styleClassifier(outboundServiceMirror.getTypeName()))
            .build();
    }

    private NomnomlRelationship mapApplicationServiceRepositoryRelationship(
        ApplicationServiceMirror applicationServiceMirror,
        RepositoryMirror repositoryMirror
    ){
        return NomnomlRelationship
            .builder()
            .fromName(relationConnectorName(applicationServiceMirror))
            .fromMultiplicity("")
            .fromStyleClassifier(DomainMapperUtils.styleClassifier(applicationServiceMirror.getTypeName()))
            .label("")
            .relationshiptype(NomnomlRelationship.RelationshipType.ASSOCIATION)
            .toName(relationConnectorName(repositoryMirror))
            .toMultiplicity("")
            .toStyleClassifier(DomainMapperUtils.styleClassifier(repositoryMirror.getTypeName()))
            .build();
    }

    private NomnomlRelationship mapApplicationServiceDomainServiceRelationship(
        ApplicationServiceMirror applicationServiceMirror,
        DomainServiceMirror domainServiceMirror
    ){
        return NomnomlRelationship
            .builder()
            .fromName(relationConnectorName(applicationServiceMirror))
            .fromMultiplicity("")
            .fromStyleClassifier(DomainMapperUtils.styleClassifier(applicationServiceMirror.getTypeName()))
            .label("")
            .relationshiptype(NomnomlRelationship.RelationshipType.ASSOCIATION)
            .toName(relationConnectorName(domainServiceMirror))
            .toMultiplicity("")
            .toStyleClassifier(DomainMapperUtils.styleClassifier(domainServiceMirror.getTypeName()))
            .build();
    }

    private NomnomlRelationship mapApplicationServiceQueryClientRelationship(
        ApplicationServiceMirror applicationServiceMirror,
        QueryClientMirror queryClientMirror
    ){
        return NomnomlRelationship
            .builder()
            .fromName(relationConnectorName(applicationServiceMirror))
            .fromMultiplicity("")
            .fromStyleClassifier(DomainMapperUtils.styleClassifier(applicationServiceMirror.getTypeName()))
            .label("")
            .relationshiptype(NomnomlRelationship.RelationshipType.ASSOCIATION)
            .toName(relationConnectorName(queryClientMirror))
            .toMultiplicity("")
            .toStyleClassifier(DomainMapperUtils.styleClassifier(queryClientMirror.getTypeName()))
            .build();
    }

    private NomnomlRelationship mapApplicationServiceOutboundServiceRelationship(
        ApplicationServiceMirror applicationServiceMirror,
        OutboundServiceMirror outboundServiceMirror
    ){
        return NomnomlRelationship
            .builder()
            .fromName(relationConnectorName(applicationServiceMirror))
            .fromMultiplicity("")
            .fromStyleClassifier(DomainMapperUtils.styleClassifier(applicationServiceMirror.getTypeName()))
            .label("")
            .relationshiptype(NomnomlRelationship.RelationshipType.ASSOCIATION)
            .toName(relationConnectorName(outboundServiceMirror))
            .toMultiplicity("")
            .toStyleClassifier(DomainMapperUtils.styleClassifier(outboundServiceMirror.getTypeName()))
            .build();
    }

    private NomnomlRelationship mapListensToDomainEvent(DomainTypeMirror domainTypeMirror, DomainEventMirror domainEventMirror){
        return NomnomlRelationship
            .builder()
            .fromName(relationConnectorName(domainEventMirror))
            .fromMultiplicity("")
            .fromStyleClassifier(DomainMapperUtils.styleClassifier(domainEventMirror.getTypeName()))
            .label("")
            .relationshiptype(NomnomlRelationship.RelationshipType.DIRECTED_ASSOCIATION)
            .toName(relationConnectorName(domainTypeMirror))
            .toMultiplicity("")
            .toStyleClassifier(DomainMapperUtils.styleClassifier(domainTypeMirror.getTypeName()))
            .build();
    }

    private NomnomlRelationship mapAggregateListensToDomainEvent(AggregateRootMirror aggregateRootMirror, DomainEventMirror domainEventMirror){
        return NomnomlRelationship
            .builder()
            .fromName(relationConnectorName(domainEventMirror))
            .fromMultiplicity("")
            .fromStyleClassifier(DomainMapperUtils.styleClassifier(domainEventMirror.getTypeName()))
            .label("")
            .relationshiptype(NomnomlRelationship.RelationshipType.DIRECTED_ASSOCIATION)
            .toName(DomainMapperUtils.mapTypeName(aggregateRootMirror.getTypeName(), diagramConfig) + " <<Aggregate>>")
            .toMultiplicity("")
            .toStyleClassifier(DomainMapperUtils.styleClassifier("<"+DomainDiagramGenerator.AGGREGATE_FRAME_STYLE_TAG+"> "))
            .build();
    }

    private NomnomlRelationship mapPublishesDomainEvent(DomainTypeMirror domainTypeMirror, DomainEventMirror domainEventMirror){
        return NomnomlRelationship
            .builder()
            .fromName(relationConnectorName(domainTypeMirror))
            .fromMultiplicity("")
            .fromStyleClassifier(DomainMapperUtils.styleClassifier(domainTypeMirror.getTypeName()))
            .label("")
            .relationshiptype(NomnomlRelationship.RelationshipType.DIRECTED_ASSOCIATION)
            .toName(relationConnectorName(domainEventMirror))
            .toMultiplicity("")
            .toStyleClassifier(DomainMapperUtils.styleClassifier(domainEventMirror.getTypeName()))
            .build();
    }

    private NomnomlRelationship mapAggregatePublishesDomainEvent(AggregateRootMirror aggregateRootMirror, DomainEventMirror domainEventMirror){
        return NomnomlRelationship
            .builder()
            .fromName(DomainMapperUtils.mapTypeName(aggregateRootMirror.getTypeName(), diagramConfig) + " <<Aggregate>>")
            .fromMultiplicity("")
            .fromStyleClassifier("<"+DomainDiagramGenerator.AGGREGATE_FRAME_STYLE_TAG+"> ")
            .label("")
            .relationshiptype(NomnomlRelationship.RelationshipType.DIRECTED_ASSOCIATION)
            .toName(relationConnectorName(domainEventMirror))
            .toMultiplicity("")
            .toStyleClassifier(DomainMapperUtils.styleClassifier(domainEventMirror.getTypeName()))
            .build();
    }

    private NomnomlRelationship mapDomainCommandDomainServiceRelationship(DomainCommandMirror domainCommandMirror, DomainServiceMirror domainServiceMirror){
        return NomnomlRelationship
            .builder()
            .fromName(relationConnectorName(domainCommandMirror))
            .fromMultiplicity("")
            .fromStyleClassifier(DomainMapperUtils.styleClassifier(domainCommandMirror.getTypeName()))
            .label("")
            .relationshiptype(NomnomlRelationship.RelationshipType.DIRECTED_ASSOCIATION)
            .toName(relationConnectorName(domainServiceMirror))
            .toMultiplicity("")
            .toStyleClassifier(DomainMapperUtils.styleClassifier(domainServiceMirror.getTypeName()))
            .build();
    }

    private NomnomlRelationship mapDomainCommandAggregateRelationship(DomainCommandMirror domainCommandMirror, AggregateRootMirror aggregateRootMirror){
        return NomnomlRelationship
            .builder()
            .fromName(relationConnectorName(domainCommandMirror))
            .fromMultiplicity("")
            .fromStyleClassifier(DomainMapperUtils.styleClassifier(domainCommandMirror.getTypeName()))
            .label("")
            .relationshiptype(NomnomlRelationship.RelationshipType.DIRECTED_ASSOCIATION)
            .toStyleClassifier("<"+DomainDiagramGenerator.AGGREGATE_FRAME_STYLE_TAG+"> ")
            .toMultiplicity("")
            .toName(DomainMapperUtils.mapTypeName(aggregateRootMirror.getTypeName(), diagramConfig) + " <<Aggregate>>")
            .build();
    }

    private NomnomlRelationship mapDomainCommandApplicationServiceRelationship(DomainCommandMirror domainCommandMirror, ApplicationServiceMirror applicationServiceMirror){
        return NomnomlRelationship
            .builder()
            .fromName(relationConnectorName(domainCommandMirror))
            .fromMultiplicity("")
            .fromStyleClassifier(DomainMapperUtils.styleClassifier(domainCommandMirror.getTypeName()))
            .label("")
            .relationshiptype(NomnomlRelationship.RelationshipType.DIRECTED_ASSOCIATION)
            .toName(relationConnectorName(applicationServiceMirror))
            .toMultiplicity("")
            .toStyleClassifier(DomainMapperUtils.styleClassifier(applicationServiceMirror.getTypeName()))
            .build();
    }

    private NomnomlRelationship mapDomainCommandQueryClientRelationship(DomainCommandMirror domainCommandMirror, QueryClientMirror queryClientMirror){
        return NomnomlRelationship
            .builder()
            .fromName(relationConnectorName(domainCommandMirror))
            .fromMultiplicity("")
            .fromStyleClassifier(DomainMapperUtils.styleClassifier(domainCommandMirror.getTypeName()))
            .label("")
            .relationshiptype(NomnomlRelationship.RelationshipType.DIRECTED_ASSOCIATION)
            .toName(relationConnectorName(queryClientMirror))
            .toMultiplicity("")
            .toStyleClassifier(DomainMapperUtils.styleClassifier(queryClientMirror.getTypeName()))
            .build();
    }

    private NomnomlRelationship mapDomainCommandOutboundServiceRelationship(DomainCommandMirror domainCommandMirror, OutboundServiceMirror outboundServiceMirror){
        return NomnomlRelationship
            .builder()
            .fromName(relationConnectorName(domainCommandMirror))
            .fromMultiplicity("")
            .fromStyleClassifier(DomainMapperUtils.styleClassifier(domainCommandMirror.getTypeName()))
            .label("")
            .relationshiptype(NomnomlRelationship.RelationshipType.DIRECTED_ASSOCIATION)
            .toName(relationConnectorName(outboundServiceMirror))
            .toMultiplicity("")
            .toStyleClassifier(DomainMapperUtils.styleClassifier(outboundServiceMirror.getTypeName()))
            .build();
    }

    public List<NomnomlRelationship> mapAllAggregateRelationships(AggregateRootMirror aggregateRootMirror){
        var relationShips = new ArrayList<NomnomlRelationship>();
        var visitor = new ContextDomainObjectVisitor(aggregateRootMirror){

            @Override
            public void visitEntityReference(EntityReferenceMirror entityReferenceMirror) {
                relationShips.add(mapEntityReference(entityReferenceMirror));
            }

            @Override
            public void visitValueReference(ValueReferenceMirror valueReferenceMirror) {
                if(!DomainMapperUtils.showPropertyInline(valueReferenceMirror, aggregateRootMirror)){
                    relationShips.add(mapValueReference(valueReferenceMirror));
                }
            }

            @Override
            public void visitEnterAnyDomainType(DomainTypeMirror domainTypeMirror) {
                mapInheritance(domainTypeMirror).ifPresent(relationShips::add);
            }
        };
        visitor.start();

        return relationShips;
    }

    private Optional<NomnomlRelationship> mapInheritance(DomainTypeMirror domainTypeMirror){
        if(domainTypeMirror.getInheritanceHierarchyTypeNames().get(0).startsWith(diagramConfig.getContextPackageName())){
            var superClassName = domainTypeMirror.getInheritanceHierarchyTypeNames().get(0);
            return Optional.of(
                NomnomlRelationship
                    .builder()
                    .fromName(relationConnectorName(superClassName))
                    .fromStyleClassifier(DomainMapperUtils.styleClassifier(superClassName))
                    .fromMultiplicity("")
                    .label("")
                    .toName(relationConnectorName(domainTypeMirror))
                    .toStyleClassifier(DomainMapperUtils.styleClassifier(domainTypeMirror.getTypeName()))
                    .toMultiplicity("")
                    .relationshiptype(NomnomlRelationship.RelationshipType.INHERITANCE)
                    .build()
            );
        }
        return Optional.empty();
    }


    private NomnomlRelationship mapEntityReference(EntityReferenceMirror entityReferenceMirror){

        var toMultiplicity = toMultiplicity(entityReferenceMirror);
        var label = entityReferenceMirror.getName();
        if(diagramConfig.isMultiplicityInLabel()){
            label = label + " " + toMultiplicity;
            toMultiplicity = "";
        }

        return NomnomlRelationship
            .builder()
            .fromName(relationConnectorName(entityReferenceMirror.getDeclaredByTypeName()))
            .fromMultiplicity("")
            .fromStyleClassifier(DomainMapperUtils.styleClassifier(entityReferenceMirror.getDeclaredByTypeName()))
            .label(label)
            .toStyleClassifier(DomainMapperUtils.styleClassifier(entityReferenceMirror.getType().getTypeName()))
            .toMultiplicity(toMultiplicity)
            .toName(relationConnectorName(entityReferenceMirror.getType().getTypeName()))
            .relationshiptype(NomnomlRelationship.RelationshipType.COMPOSITION)
            .build();
    }

    private NomnomlRelationship mapValueReference(ValueReferenceMirror valueReferenceMirror){

        var toMultiplicity = toMultiplicity(valueReferenceMirror);
        var label = valueReferenceMirror.getName();
        if(diagramConfig.isMultiplicityInLabel()){
            label = label + " " + toMultiplicity;
            toMultiplicity = "";
        }

        return NomnomlRelationship
            .builder()
            .fromName(relationConnectorName(valueReferenceMirror.getDeclaredByTypeName()))
            .fromMultiplicity("")
            .fromStyleClassifier(DomainMapperUtils.styleClassifier(valueReferenceMirror.getDeclaredByTypeName()))
            .label(label)
            .toStyleClassifier(DomainMapperUtils.styleClassifier(valueReferenceMirror.getType().getTypeName()))
            .toMultiplicity(toMultiplicity)
            .toName(relationConnectorName(valueReferenceMirror.getType().getTypeName()))
            .relationshiptype(NomnomlRelationship.RelationshipType.AGGREGATION)
            .build();
    }

    private List<NomnomlRelationship> mapIdReferences(ValueReferenceMirror idReferenceMirror){
        var declaringAggregates = filteredDomainClasses
            .getAggregateRoots()
            .stream()
            .filter(aggregateRootMirror -> {
                final var contained = new AtomicBoolean(false);
                var visitor = new ContextDomainObjectVisitor(aggregateRootMirror){
                    @Override
                    public void visitValueReference(ValueReferenceMirror valueReferenceMirror) {
                        if(idReferenceMirror.equals(valueReferenceMirror)){
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
                if(identity.isPresent()){
                    return identity.get().getType().getTypeName().equals(idReferenceMirror.getValue().getTypeName());
                }
                return false;
            })
            .findFirst();
        if(!declaringAggregates.isEmpty() && targetAggregate.isPresent()){
            return declaringAggregates
                .stream()
                .map(da -> NomnomlRelationship
                    .builder()
                    .fromName(DomainMapperUtils.mapTypeName(da.getTypeName(), diagramConfig) + " <<Aggregate>>")
                    .fromMultiplicity("")
                    .fromStyleClassifier("<"+DomainDiagramGenerator.AGGREGATE_FRAME_STYLE_TAG+"> ")
                    .label(DomainMapperUtils.mapTypeName(idReferenceMirror.getDeclaredByTypeName(), diagramConfig) + "." + idReferenceMirror.getName())
                    .toStyleClassifier("<"+DomainDiagramGenerator.AGGREGATE_FRAME_STYLE_TAG+"> ")
                    .toMultiplicity("")
                    .toName(DomainMapperUtils.mapTypeName(targetAggregate.get().getTypeName(), diagramConfig) + " <<Aggregate>>")
                    .relationshiptype(NomnomlRelationship.RelationshipType.DIRECTED_ASSOCIATION)
                    .build())
                .toList();
        }
        return Collections.emptyList();
    }

    private String relationConnectorName(DomainTypeMirror domainTypeMirror){
        return DomainMapperUtils.domainTypeName(domainTypeMirror, diagramConfig) + connectorStereotype(domainTypeMirror.getTypeName());

    }

    private String relationConnectorName(String typeName){
        return DomainMapperUtils.mapTypeName(typeName, diagramConfig) + connectorStereotype(typeName);
    }

    private String connectorStereotype(String typeName){
        var domainTypeMirror = Domain.typeMirror(typeName);
        if(domainTypeMirror.isPresent()){
            var stereotype = DomainMapperUtils.stereotype(domainTypeMirror.get(), diagramConfig);
            if(!"".equals(stereotype)){
                return " <<" + stereotype + ">>";
            }
        }
        return "";
    }

    private String toMultiplicity(FieldMirror propertyMirror){
        if(propertyMirror.getType().hasOptionalContainer()){
            return "0..1";
        }else if(propertyMirror.getType().hasCollectionContainer()){
            var maxBySizeAssertions = propertyMirror
                .getType()
                .getContainerAssertions()
                .stream()
                .filter(a -> AssertionType.hasSize.equals(a.getAssertionType()))
                .map(a -> {
                    try {
                        return Integer.valueOf(a.getParam2());
                    }catch (Throwable t){
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
                    }catch (Throwable t){
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
                    }catch (Throwable t){
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
                    }catch (Throwable t){
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

            if(minByNotEmptyAssertion.isPresent()){
                min = 1;
            }
            if(minByMinAssertions.isPresent()){
                if(minByMinAssertions.get()>min){
                    min = minByMinAssertions.get();
                }
            }
            if(minBySizeAssertions.isPresent()){
                if(minBySizeAssertions.get()>min){
                    min = minBySizeAssertions.get();
                }
            }
            if(maxByMaxAssertions.isPresent()){
                if(maxByMaxAssertions.get()<max){
                    max = maxByMaxAssertions.get();
                }
            }
            if(maxBySizeAssertions.isPresent()){
                if(maxBySizeAssertions.get()<max){
                    max = maxBySizeAssertions.get();
                }
            }
            return min + ".." + (max == Integer.MAX_VALUE ? "*" : max);
        }else{
            var notNullAssertion = propertyMirror
                .getType()
                .getAssertions()
                .stream()
                .filter(a -> AssertionType.isNotNull.equals(a.getAssertionType()))
                .findAny();
            if(notNullAssertion.isPresent()){
                return "1";
            }
            return "0..1";
        }
    }



}

