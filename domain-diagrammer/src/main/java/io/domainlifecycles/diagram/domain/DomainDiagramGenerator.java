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

package io.domainlifecycles.diagram.domain;

import io.domainlifecycles.diagram.Diagram;
import io.domainlifecycles.diagram.domain.config.DomainDiagramConfig;
import io.domainlifecycles.diagram.domain.mapper.DomainMapper;

/**
 * The DomainDiagramGenerator generates the Nomnoml diagram text
 * for a complete bounded contexts and the configuration specified by a given
 * {@link DomainDiagramConfig}.
 *
 * @author Mario Herb
 */
public class DomainDiagramGenerator implements Diagram {
    private final DomainDiagramConfig diagramConfig;

    private final DomainMapper domainMapper;

    public static final String AGGREGATE_ROOT_STYLE_TAG = "AggregateRoot";

    public static final String AGGREGATE_FRAME_STYLE_TAG = "AggregateFrame";

    public static final String ENTITY_STYLE_TAG = "Entity";

    public static final String VALUE_OBJECT_STYLE_TAG = "ValueObject";

    public static final String ENUM_STYLE_TAG = "Enum";

    public static final String DOMAIN_SERVICE_STYLE_TAG = "DomainService";

    public static final String APPLICATION_SERVICE_STYLE_TAG = "ApplicationService";

    public static final String DOMAIN_EVENT_STYLE_TAG = "DomainEvent";

    public static final String REPOSITORY_STYLE_TAG = "Repository";

    public static final String DOMAIN_COMMAND_STYLE_TAG = "DomainCommand";

    public static final String READ_MODEL_STYLE_TAG = "ReadModel";

    public static final String QUERY_CLIENT_STYLE_TAG = "QueryClient";
    public static final String OUTBOUND_SERVICE_STYLE_TAG = "OutboundService";

    public static final String IDENTITY_STYLE_TAG = "Identity";

    /**
     * Initializes the DomainDiagramGenerator with a given {@link DomainDiagramConfig}
     * @param diagramConfig diagram configuration
     */
    public DomainDiagramGenerator(final DomainDiagramConfig diagramConfig) {
        this.diagramConfig = diagramConfig;
        this.domainMapper = new DomainMapper(diagramConfig);
    }

    /**
     * Generates to complete diagram text for a Domain Diagram of a Bounded Context.
     *
     * @return generated diagram text
     */
    @Override
    public String generateDiagramText() {
        var builder = new StringBuilder();
        builder.append(aggregateFrameStyleDeclaration());
        builder.append(aggregateRootStyleDeclaration());
        builder.append(entityStyleDeclaration());
        builder.append(valueObjectStyleDeclaration());
        builder.append(enumStyleDeclaration());
        builder.append(identityStyleDeclaration());
        builder.append(domainCommandStyleDeclaration());
        builder.append(domainEventStyleDeclaration());
        builder.append(applicationServiceStyleDeclaration());
        builder.append(domainServiceStyleDeclaration());
        builder.append(repositoryStyleDeclaration());
        builder.append(outboundServiceStyleDeclaration());
        builder.append(queryClientStyleDeclaration());
        builder.append(readModelStyleDeclaration());
        builder.append(fontStyleDeclaration());
        builder.append(directionStyleDeclaration());
        builder.append(acyclerStyleDeclaration());
        builder.append(rankerStyleDeclaration());

        builder.append(System.lineSeparator());
        if(diagramConfig.isShowDomainCommands()) {
            domainMapper.getDomainCommands().forEach(f -> builder.append(f.getDiagramText()));
        }
        if(diagramConfig.isShowApplicationServices()) {
            domainMapper.getApplicationServices().forEach(f -> builder.append(f.getDiagramText()));
        }
        if(diagramConfig.isShowDomainServices()) {
            domainMapper.getDomainServices().forEach(f -> builder.append(f.getDiagramText()));
        }
        if(diagramConfig.isShowDomainEvents()) {
            domainMapper.getDomainEvents().forEach(f -> builder.append(f.getDiagramText()));
        }
        domainMapper.getAggregateFrames().forEach(f -> builder.append(f.getDiagramText()));
        domainMapper.getDomainRelationshipMapper()
            .mapAllAggregateFrameRelationships()
            .forEach(f -> builder.append(f.getDiagramText()));
        if(diagramConfig.isShowRepositories()) {
            domainMapper.getRepositories().forEach(f -> builder.append(f.getDiagramText()));
        }
        if(diagramConfig.isShowReadModels()) {
            domainMapper.getReadModels().forEach(f -> builder.append(f.getDiagramText()));
        }
        if(diagramConfig.isShowQueryClients()) {
            domainMapper.getQueryClients().forEach(f -> builder.append(f.getDiagramText()));
        }
        if(diagramConfig.isShowOutboundServices()) {
            domainMapper.getOutboundServices().forEach(f -> builder.append(f.getDiagramText()));
        }

        domainMapper.getDomainRelationshipMapper().mapAllDomainCommandRelationships()
            .forEach(f -> builder.append(f.getDiagramText()));
        domainMapper.getDomainRelationshipMapper().mapAllDomainEventRelationships()
            .forEach(f -> builder.append(f.getDiagramText()));
        domainMapper.getDomainRelationshipMapper().mapAllApplicationServiceServiceDomainServiceRelationships()
            .forEach(f -> builder.append(f.getDiagramText()));
        domainMapper.getDomainRelationshipMapper().mapAllApplicationServiceServiceRepositoryRelationships()
            .forEach(f -> builder.append(f.getDiagramText()));
        domainMapper.getDomainRelationshipMapper().mapAllApplicationServiceOutboundServiceRelationships()
            .forEach(f -> builder.append(f.getDiagramText()));
        domainMapper.getDomainRelationshipMapper().mapAllApplicationServiceQueryClientRelationships()
            .forEach(f -> builder.append(f.getDiagramText()));
        domainMapper.getDomainRelationshipMapper().mapAllDomainServiceRepositoryRelationships()
            .forEach(f -> builder.append(f.getDiagramText()));
        domainMapper.getDomainRelationshipMapper().mapAllDomainServiceQueryClientRelationships()
            .forEach(f -> builder.append(f.getDiagramText()));
        domainMapper.getDomainRelationshipMapper().mapAllDomainServiceOutboundServiceRelationships()
            .forEach(f -> builder.append(f.getDiagramText()));

        domainMapper.getDomainRelationshipMapper().mapAllAggregateRepositoryRelationships()
            .forEach(f -> builder.append(f.getDiagramText()));
        domainMapper.getDomainRelationshipMapper().mapAllQueryClientReadModelRelationships()
            .forEach(f -> builder.append(f.getDiagramText()));



        return builder.toString();
    }

    private String aggregateFrameStyleDeclaration(){
        if(diagramConfig.getAggregateFrameStyle()!= null){
            return styleDeclaration(diagramConfig.getAggregateFrameStyle(), AGGREGATE_FRAME_STYLE_TAG);
        }
        return "";
    }
    private String aggregateRootStyleDeclaration(){
        if(diagramConfig.getAggregateRootStyle()!= null){
            return styleDeclaration(diagramConfig.getAggregateRootStyle(), AGGREGATE_ROOT_STYLE_TAG);
        }
        return "";
    }

    private String entityStyleDeclaration(){
        if(diagramConfig.getEntityStyle()!= null){
            return styleDeclaration(diagramConfig.getEntityStyle(), ENTITY_STYLE_TAG);
        }
        return "";
    }

    private String valueObjectStyleDeclaration(){
        if(diagramConfig.getValueObjectStyle()!= null){
            return styleDeclaration(diagramConfig.getValueObjectStyle(), VALUE_OBJECT_STYLE_TAG);
        }
        return "";
    }

    private String enumStyleDeclaration(){
        if(diagramConfig.getEnumStyle()!= null){
            return styleDeclaration(diagramConfig.getEnumStyle(), ENUM_STYLE_TAG);
        }
        return "";
    }
    private String identityStyleDeclaration(){
        if(diagramConfig.getIdentityStyle()!= null){
            return styleDeclaration(diagramConfig.getIdentityStyle(), IDENTITY_STYLE_TAG);
        }
        return "";
    }

    private String domainCommandStyleDeclaration(){
        if(diagramConfig.getDomainCommandStyle()!= null){
            return styleDeclaration(diagramConfig.getDomainCommandStyle(), DOMAIN_COMMAND_STYLE_TAG);
        }
        return "";
    }

    private String domainEventStyleDeclaration(){
        if(diagramConfig.getDomainEventStyle()!= null){
            return styleDeclaration(diagramConfig.getDomainEventStyle(), DOMAIN_EVENT_STYLE_TAG);
        }
        return "";
    }

    private String applicationServiceStyleDeclaration(){
        if(diagramConfig.getApplicationServiceStyle()!= null){
            return styleDeclaration(diagramConfig.getApplicationServiceStyle(), APPLICATION_SERVICE_STYLE_TAG);
        }
        return "";
    }

    private String domainServiceStyleDeclaration(){
        if(diagramConfig.getDomainServiceStyle()!= null){
            return styleDeclaration(diagramConfig.getDomainServiceStyle(), DOMAIN_SERVICE_STYLE_TAG);
        }
        return "";
    }

    private String repositoryStyleDeclaration(){
        if(diagramConfig.getRepositoryStyle()!= null){
            return styleDeclaration(diagramConfig.getRepositoryStyle(), REPOSITORY_STYLE_TAG);
        }
        return "";
    }

    private String outboundServiceStyleDeclaration(){
        if(diagramConfig.getOutboundServiceStyle()!= null){
            return styleDeclaration(diagramConfig.getOutboundServiceStyle(), OUTBOUND_SERVICE_STYLE_TAG);
        }
        return "";
    }

    private String queryClientStyleDeclaration(){
        if(diagramConfig.getQueryClientStyle()!= null){
            return styleDeclaration(diagramConfig.getQueryClientStyle(), QUERY_CLIENT_STYLE_TAG);
        }
        return "";
    }

    private String readModelStyleDeclaration(){
        if(diagramConfig.getReadModelStyle()!= null){
            return styleDeclaration(diagramConfig.getReadModelStyle(), READ_MODEL_STYLE_TAG);
        }
        return "";
    }

    private String styleDeclaration(String declaration, String tagName){
        return "#." + tagName + ":" + declaration + System.lineSeparator();
    }

    private String fontStyleDeclaration(){
        if(diagramConfig.getFont()!= null){
            return "#font:"+diagramConfig.getFont() + System.lineSeparator();
        }
        return "";
    }

    private String directionStyleDeclaration(){
        if(diagramConfig.getDirection()!= null){
            return "#direction:"+diagramConfig.getDirection() + System.lineSeparator();
        }
        return "";
    }

    private String acyclerStyleDeclaration(){
        if(diagramConfig.getDirection()!= null){
            return "#acycler: "+diagramConfig.getAcycler() + System.lineSeparator();
        }
        return "";
    }

    private String rankerStyleDeclaration(){
        if(diagramConfig.getDirection()!= null){
            return "#ranker: "+diagramConfig.getRanker() + System.lineSeparator();
        }
        return "";
    }
}
