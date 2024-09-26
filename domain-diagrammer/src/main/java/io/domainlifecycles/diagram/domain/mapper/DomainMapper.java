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

package io.domainlifecycles.diagram.domain.mapper;

import io.domainlifecycles.diagram.DiagramElement;
import io.domainlifecycles.diagram.domain.DomainDiagramGenerator;
import io.domainlifecycles.diagram.domain.config.DomainDiagramConfig;
import io.domainlifecycles.diagram.nomnoml.NomnomlClass;
import io.domainlifecycles.diagram.nomnoml.NomnomlFrame;
import io.domainlifecycles.mirror.api.AggregateRootMirror;

import java.util.ArrayList;
import java.util.List;

/**
 * Central mapping entry point providing all methods to map mirror structures of a bounded context to
 * a domain diagramm.
 *
 * @author Mario Herb
 */
public class DomainMapper {

    private final DomainDiagramConfig domainDiagramConfig;
    private final DomainClassMapper domainClassMapper;

    private final DomainRelationshipMapper domainRelationshipMapper;
    private final FilteredDomainClasses filteredDomainClasses;


    /**
     * Initializes the Domain Mapper with a given {@link DomainDiagramConfig}.
     * @param domainDiagramConfig diagram configuration
     */
    public DomainMapper(DomainDiagramConfig domainDiagramConfig) {
        this.domainDiagramConfig = domainDiagramConfig;
        this.filteredDomainClasses = new FilteredDomainClasses(domainDiagramConfig);

        this.domainClassMapper = new DomainClassMapper(domainDiagramConfig);
        this.domainRelationshipMapper = new DomainRelationshipMapper(domainDiagramConfig, filteredDomainClasses);

    }

    /**
     * @return all ApplicationsServices connected to the Bounded Context as {@link NomnomlClass}.
     */
    public List<NomnomlClass> getApplicationServices(){
        return filteredDomainClasses.getApplicationServices().stream()
            .map(domainClassMapper::mapApplicationServiceClass)
            .toList();
    }

    /**
     * @return all DomainCommands of the Bounded Context as {@link NomnomlClass}.
     */
    public List<NomnomlClass> getDomainCommands(){
        return filteredDomainClasses.getDomainCommands().stream()
            .map(domainClassMapper::mapDomainCommandClass)
            .toList();
    }

    /**
     * @return all DomainEvents of the Bounded Context as {@link NomnomlClass}.
     */
    public List<NomnomlClass> getDomainEvents(){
        return filteredDomainClasses.getDomainEvents().stream()
            .map(domainClassMapper::mapDomainEventClass)
            .toList();
    }

    /**
     * @return all DomainServices of the Bounded Context as {@link NomnomlClass}.
     */
    public List<NomnomlClass> getDomainServices(){
       return filteredDomainClasses.getDomainServices().stream()
            .map(domainClassMapper::mapDomainServiceClass)
            .toList();
    }

    /**
     * @return all Repositories of the Bounded Context as {@link NomnomlClass}.
     */
    public List<NomnomlClass> getRepositories(){
        return filteredDomainClasses.getRepositories().stream()
            .map(domainClassMapper::mapRepositoryClass)
            .toList();
    }

    /**
     * @return all ReadModels connected to the Bounded Context as {@link NomnomlClass}.
     */
    public List<NomnomlClass> getReadModels(){
        return filteredDomainClasses.getReadModels().stream()
            .map(domainClassMapper::mapReadModelClass)
            .toList();
    }

    /**
     * @return all QueryClients connected to the Bounded Context as {@link NomnomlClass}.
     */
    public List<NomnomlClass> getQueryClients(){
        return filteredDomainClasses.getQueryClients().stream()
            .map(domainClassMapper::mapQueryClientClass)
            .toList();
    }

    /**
     * @return all OutboundServices connected to the Bounded Context as {@link NomnomlClass}.
     */
    public List<NomnomlClass> getOutboundServices(){
        return filteredDomainClasses.getOutboundServices().stream()
            .map(domainClassMapper::mapOutboundServiceClass)
            .toList();
    }

    /**
     * @return all Aggregates of the Bounded Context as {@link NomnomlFrame}.
     */
    public List<NomnomlFrame> getAggregateFrames(){
        return filteredDomainClasses.getAggregateRoots().stream()
            .map(this::getAggregateFrame)
            .toList();
    }

    private NomnomlFrame getAggregateFrame(AggregateRootMirror aggregateRootMirror){
        var aggregateClasses = domainClassMapper.mapAllAggregateClasses(aggregateRootMirror);
        var aggregateRelationShips = domainRelationshipMapper.mapAllAggregateRelationships(aggregateRootMirror);
        var allElements = new ArrayList<DiagramElement>(aggregateClasses);
        allElements.addAll(aggregateRelationShips);
        return NomnomlFrame
            .builder()
            .name(DomainMapperUtils.mapTypeName(aggregateRootMirror.getTypeName(), domainDiagramConfig))
            .comment("!!! {Frame} "+ aggregateRootMirror.getTypeName() +" !!!")
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
