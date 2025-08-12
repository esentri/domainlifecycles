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

import io.domainlifecycles.diagram.DiagramElement;
import io.domainlifecycles.diagram.domain.DomainDiagramGenerator;
import io.domainlifecycles.diagram.domain.config.DomainDiagramConfig;
import io.domainlifecycles.diagram.domain.notes.DomainClassNote;
import io.domainlifecycles.diagram.nomnoml.NomnomlClass;
import io.domainlifecycles.diagram.nomnoml.NomnomlFrame;
import io.domainlifecycles.diagram.nomnoml.NomnomlNote;
import io.domainlifecycles.diagram.nomnoml.NomnomlRelationship;
import io.domainlifecycles.diagram.nomnoml.NomnomlStereotype;
import io.domainlifecycles.mirror.api.AggregateRootMirror;
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.DomainTypeMirror;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Central mapping entry point providing all methods to map mirror structures of a bounded context to
 * a domain diagramm.
 *
 * @author Mario Herb
 */
public class DomainMapper {

    private final DomainDiagramConfig domainDiagramConfig;
    private final DomainClassMapper domainClassMapper;
    private final Collection<DomainClassNote> notes;

    private final DomainRelationshipMapper domainRelationshipMapper;
    private final FilteredDomainClasses filteredDomainClasses;


    /**
     * Initializes the Domain Mapper with a given {@link DomainDiagramConfig}.
     *
     * @param domainDiagramConfig diagram configuration
     * @param domainMirror mapped domain
     * @param notes a collection of externally provided notes to be attached to the shown classes
     */
    public DomainMapper(
        DomainDiagramConfig domainDiagramConfig,
        DomainMirror domainMirror,
        Collection<DomainClassNote> notes) {
        this.domainDiagramConfig = domainDiagramConfig;
        this.notes = notes;
        this.filteredDomainClasses = new FilteredDomainClasses(
            domainDiagramConfig.getDiagramTrimSettings(),
            domainDiagramConfig.getGeneralVisualSettings(),
            domainMirror);

        this.domainClassMapper = new DomainClassMapper(domainDiagramConfig);
        this.domainRelationshipMapper = new DomainRelationshipMapper(domainDiagramConfig, domainMirror, filteredDomainClasses);

    }

    /**
     * @return all ApplicationsServices in the diagram as {@link NomnomlClass}.
     */
    public List<NomnomlClass> getApplicationServices() {
        return filteredDomainClasses.getApplicationServices().stream()
            .map(domainClassMapper::mapApplicationServiceClass)
            .toList();
    }

    /**
     * @return all DomainCommands in the diagram as {@link NomnomlClass}.
     */
    public List<NomnomlClass> getDomainCommands() {
        return filteredDomainClasses.getDomainCommands().stream()
            .map(domainClassMapper::mapDomainCommandClass)
            .toList();
    }

    /**
     * @return all DomainEvents in the diagram as {@link NomnomlClass}.
     */
    public List<NomnomlClass> getDomainEvents() {
        return filteredDomainClasses.getDomainEvents().stream()
            .map(domainClassMapper::mapDomainEventClass)
            .toList();
    }

    /**
     * @return all DomainServices in the diagram as {@link NomnomlClass}.
     */
    public List<NomnomlClass> getDomainServices() {
        return filteredDomainClasses.getDomainServices().stream()
            .map(domainClassMapper::mapDomainServiceClass)
            .toList();
    }

    /**
     * @return all Repositories in the diagram as {@link NomnomlClass}.
     */
    public List<NomnomlClass> getRepositories() {
        return filteredDomainClasses.getRepositories().stream()
            .map(domainClassMapper::mapRepositoryClass)
            .toList();
    }

    /**
     * @return all ReadModels in the diagram as {@link NomnomlClass}.
     */
    public List<NomnomlClass> getReadModels() {
        return filteredDomainClasses.getReadModels().stream()
            .map(domainClassMapper::mapReadModelClass)
            .toList();
    }

    /**
     * @return all QueryHandlers in the diagram as {@link NomnomlClass}.
     */
    public List<NomnomlClass> getQueryHandlers() {
        return filteredDomainClasses.getQueryHandlers().stream()
            .map(domainClassMapper::mapQueryHandlerClass)
            .toList();
    }

    /**
     * @return all OutboundServices in the diagram as {@link NomnomlClass}.
     */
    public List<NomnomlClass> getOutboundServices() {
        return filteredDomainClasses.getOutboundServices().stream()
            .map(domainClassMapper::mapOutboundServiceClass)
            .toList();
    }

    /**
     * @return all unspecified ServiceKinds in the diagram as {@link NomnomlClass}.
     */
    public List<NomnomlClass> getUnspecifiedServiceKinds() {
        return filteredDomainClasses.getUnspecifiedServiceKinds().stream()
            .map(domainClassMapper::mapUnspecifiedServiceKindClass)
            .toList();
    }

    /**
     * @return all Aggregates of in the diagram as {@link NomnomlFrame}.
     */
    public List<NomnomlFrame> getAggregateFrames() {
        return filteredDomainClasses.getAggregateRoots().stream()
            .map(this::getAggregateFrame)
            .toList();
    }

    private record NotePair(DomainClassNote note, Optional<DomainTypeMirror> mirror) {};

    /**
     * @return all notes in the diagram as {@link NomnomlNote}.
     */
    private Stream<NotePair> getNotes() {
        if(notes == null || notes.isEmpty()) {
            return Stream.empty();
        }
        return notes.stream().map(
            n -> new NotePair(n, filteredDomainClasses.getContained(n.className())))
            .filter(p -> p.mirror.isPresent());
    }

    private NomnomlNote mapNotePair(NotePair notePair) {
        return new NomnomlNote(
            notePair.note.text(),
            DomainMapperUtils.mapTypeName(notePair.note.className(), domainDiagramConfig),
            DomainMapperUtils.styleClassifier(notePair.mirror.get()),
            List.of(new NomnomlStereotype(DomainMapperUtils.stereotype(notePair.mirror.get(), domainDiagramConfig)))
        );
    }

    /**
     * Returns all notes needed for a specific aggregate
     * @param aggregateClassNames all classes contained in the correspondig aggregate
     * @return aggregate specific notes
     */
    public List<NomnomlNote> getAggregateNotes(List<String> aggregateClassNames) {
        if(!domainDiagramConfig.getGeneralVisualSettings().isShowNotes()){
            return Collections.emptyList();
        }
        return getNotes()
            .filter(p -> aggregateClassNames.contains(p.note.className()))
            .map(this::mapNotePair)
            .toList();
    }

    /**
     * Returns all non aggregate specific notes
     * @return all notes not contained in an aggregates boundary
     */
    public List<NomnomlNote> getNonAggregateNotes() {
        if(!domainDiagramConfig.getGeneralVisualSettings().isShowNotes()){
            return Collections.emptyList();
        }
        return getNotes()
            .filter(p ->
                p.mirror.isPresent()
                &&!DomainType.ENTITY.equals(p.mirror.get().getDomainType())
                && !DomainType.VALUE_OBJECT.equals(p.mirror.get().getDomainType())
                && !DomainType.AGGREGATE_ROOT.equals(p.mirror.get().getDomainType())
            )
            .map(this::mapNotePair)
            .toList();
    }

    private NomnomlFrame getAggregateFrame(AggregateRootMirror aggregateRootMirror) {
        var mirrors = domainClassMapper.getAllAggregateMirrors(aggregateRootMirror);
        var aggregateRelationShips = domainRelationshipMapper.mapAllAggregateRelationships(aggregateRootMirror);
        var allElements = new ArrayList<DiagramElement>(
            mirrors
                .stream()
                .map(domainTypeMirror ->
                    domainClassMapper.mapToNomnomlClass(
                        domainTypeMirror,
                        domainDiagramConfig.getGeneralVisualSettings().isShowAggregateFields() && domainDiagramConfig.getGeneralVisualSettings().isShowFields(),
                        domainDiagramConfig.getGeneralVisualSettings().isShowAggregateMethods() && domainDiagramConfig.getGeneralVisualSettings().isShowMethods()
                    )
                ).toList()
        );
        allElements.addAll(aggregateRelationShips);
        allElements.addAll(
            getAggregateNotes(
                mirrors.stream().map(DomainTypeMirror::getTypeName).toList()
            )
        );
        return NomnomlFrame
            .builder()
            .name(DomainMapperUtils.mapTypeName(aggregateRootMirror.getTypeName(), domainDiagramConfig))
            .comment("!!! {Frame} " + aggregateRootMirror.getTypeName() + " !!!")
            .type("<<Aggregate>>")
            .styleClassifier(DomainDiagramGenerator.AGGREGATE_FRAME_STYLE_TAG)
            .innerElements(allElements)
            .build();
    }

    /**
     * @return the {@link DomainRelationshipMapper} providing all relationship
     * connections which have to be mapped.
     */
    public DomainRelationshipMapper getDomainRelationshipMapper() {
        return domainRelationshipMapper;
    }
}
