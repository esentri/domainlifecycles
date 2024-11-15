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

    public static final String AGGREGATE_ROOT_STYLE_TAG = "AR";

    public static final String AGGREGATE_FRAME_STYLE_TAG = "AF";

    public static final String ENTITY_STYLE_TAG = "E";

    public static final String VALUE_OBJECT_STYLE_TAG = "VO";

    public static final String ENUM_STYLE_TAG = "V";

    public static final String DOMAIN_SERVICE_STYLE_TAG = "DS";

    public static final String SERVICE_KIND_STYLE_TAG = "SK";

    public static final String APPLICATION_SERVICE_STYLE_TAG = "AS";

    public static final String DOMAIN_EVENT_STYLE_TAG = "DE";

    public static final String REPOSITORY_STYLE_TAG = "R";

    public static final String DOMAIN_COMMAND_STYLE_TAG = "DC";

    public static final String READ_MODEL_STYLE_TAG = "RM";

    public static final String QUERY_HANDLER_STYLE_TAG = "QH";
    public static final String OUTBOUND_SERVICE_STYLE_TAG = "OS";

    public static final String IDENTITY_STYLE_TAG = "I";

    /**
     * Initializes the DomainDiagramGenerator with a given {@link DomainDiagramConfig}
     *
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
        builder.append(unspecifiedServiceKindStyleDeclaration());
        builder.append(queryHandlerStyleDeclaration());
        builder.append(readModelStyleDeclaration());
        builder.append(fontStyleDeclaration());
        builder.append(directionStyleDeclaration());
        builder.append(acyclerStyleDeclaration());
        builder.append(rankerStyleDeclaration());

        builder.append(System.lineSeparator());

        domainMapper.getDomainCommands().forEach(f -> builder.append(f.getDiagramText()));

        domainMapper.getApplicationServices().forEach(f -> builder.append(f.getDiagramText()));

        domainMapper.getDomainServices().forEach(f -> builder.append(f.getDiagramText()));

        domainMapper.getDomainEvents().forEach(f -> builder.append(f.getDiagramText()));

        domainMapper.getAggregateFrames().forEach(f -> builder.append(f.getDiagramText()));
        domainMapper.getDomainRelationshipMapper()
            .mapAllAggregateFrameRelationships()
            .forEach(f -> builder.append(f.getDiagramText()));

        domainMapper.getRepositories().forEach(f -> builder.append(f.getDiagramText()));

        domainMapper.getReadModels().forEach(f -> builder.append(f.getDiagramText()));

        domainMapper.getQueryHandlers().forEach(f -> builder.append(f.getDiagramText()));

        domainMapper.getOutboundServices().forEach(f -> builder.append(f.getDiagramText()));

        domainMapper.getUnspecifiedServiceKinds().forEach(f -> builder.append(f.getDiagramText()));

        domainMapper.getDomainRelationshipMapper().mapAllDomainCommandRelationships()
            .forEach(f -> builder.append(f.getDiagramText()));
        domainMapper.getDomainRelationshipMapper().mapAllDomainEventRelationships()
            .forEach(f -> builder.append(f.getDiagramText()));
        domainMapper.getDomainRelationshipMapper().mapAllServiceKindRelationships()
            .forEach(f -> builder.append(f.getDiagramText()));

        domainMapper.getDomainRelationshipMapper().mapAllAggregateRepositoryRelationships()
            .forEach(f -> builder.append(f.getDiagramText()));
        domainMapper.getDomainRelationshipMapper().mapAllQueryHandlerReadModelRelationships()
            .forEach(f -> builder.append(f.getDiagramText()));

        return builder.toString();
    }

    private String aggregateFrameStyleDeclaration() {
        if (diagramConfig.getAggregateFrameStyle() != null) {
            return completeStyleDeclaration(diagramConfig.getAggregateFrameStyle(), AGGREGATE_FRAME_STYLE_TAG);
        }
        return "";
    }

    private String aggregateRootStyleDeclaration() {
        if (diagramConfig.getAggregateRootStyle() != null) {
            return completeStyleDeclaration(diagramConfig.getAggregateRootStyle(), AGGREGATE_ROOT_STYLE_TAG);
        }
        return "";
    }

    private String entityStyleDeclaration() {
        if (diagramConfig.getEntityStyle() != null) {
            return completeStyleDeclaration(diagramConfig.getEntityStyle(), ENTITY_STYLE_TAG);
        }
        return "";
    }

    private String valueObjectStyleDeclaration() {
        if (diagramConfig.getValueObjectStyle() != null) {
            return completeStyleDeclaration(diagramConfig.getValueObjectStyle(), VALUE_OBJECT_STYLE_TAG);
        }
        return "";
    }

    private String enumStyleDeclaration() {
        if (diagramConfig.getEnumStyle() != null) {
            return completeStyleDeclaration(diagramConfig.getEnumStyle(), ENUM_STYLE_TAG);
        }
        return "";
    }

    private String identityStyleDeclaration() {
        if (diagramConfig.getIdentityStyle() != null) {
            return completeStyleDeclaration(diagramConfig.getIdentityStyle(), IDENTITY_STYLE_TAG);
        }
        return "";
    }

    private String domainCommandStyleDeclaration() {
        if (diagramConfig.getDomainCommandStyle() != null) {
            return completeStyleDeclaration(diagramConfig.getDomainCommandStyle(), DOMAIN_COMMAND_STYLE_TAG);
        }
        return "";
    }

    private String domainEventStyleDeclaration() {
        if (diagramConfig.getDomainEventStyle() != null) {
            return completeStyleDeclaration(diagramConfig.getDomainEventStyle(), DOMAIN_EVENT_STYLE_TAG);
        }
        return "";
    }

    private String applicationServiceStyleDeclaration() {
        if (diagramConfig.getApplicationServiceStyle() != null) {
            return completeStyleDeclaration(diagramConfig.getApplicationServiceStyle(), APPLICATION_SERVICE_STYLE_TAG);
        }
        return "";
    }

    private String domainServiceStyleDeclaration() {
        if (diagramConfig.getDomainServiceStyle() != null) {
            return styleDeclaration(diagramConfig.getDomainServiceStyle(), DOMAIN_SERVICE_STYLE_TAG);
        }
        return "";
    }

    private String repositoryStyleDeclaration() {
        if (diagramConfig.getRepositoryStyle() != null) {
            return completeStyleDeclaration(diagramConfig.getRepositoryStyle(), REPOSITORY_STYLE_TAG);
        }
        return "";
    }

    private String outboundServiceStyleDeclaration() {
        if (diagramConfig.getOutboundServiceStyle() != null) {
            return completeStyleDeclaration(diagramConfig.getOutboundServiceStyle(), OUTBOUND_SERVICE_STYLE_TAG);
        }
        return "";
    }

    private String queryHandlerStyleDeclaration() {
        if (diagramConfig.getQueryHandlerStyle() != null) {
            return completeStyleDeclaration(diagramConfig.getQueryHandlerStyle(), QUERY_HANDLER_STYLE_TAG);
        }
        return "";
    }

    private String unspecifiedServiceKindStyleDeclaration() {
        if (diagramConfig.getUnspecifiedServiceKindStyle() != null) {
            return completeStyleDeclaration(diagramConfig.getUnspecifiedServiceKindStyle(), SERVICE_KIND_STYLE_TAG);
        }
        return "";
    }

    private String readModelStyleDeclaration() {
        if (diagramConfig.getReadModelStyle() != null) {
            return completeStyleDeclaration(diagramConfig.getReadModelStyle(), READ_MODEL_STYLE_TAG);
        }
        return "";
    }

    private String completeStyleDeclaration(String declaration, String tagName){
        return styleDeclaration(declaration, tagName)
            + removedStyleDeclaration(declaration, tagName)
            + addedStyleDeclaration(declaration, tagName);
    }

    private String styleDeclaration(String declaration, String tagName) {
        return "#." + tagName + ":" + declaration + System.lineSeparator();
    }

    private String removedStyleDeclaration(String declaration, String tagName) {
        return "#." + tagName + "_R:" + declaration + ";stroke=#FF0000" + System.lineSeparator();
    }

    private String addedStyleDeclaration(String declaration, String tagName) {
        return "#." + tagName + "_N:" + declaration + ";stroke=#108738" + System.lineSeparator();
    }

    private String fontStyleDeclaration() {
        if (diagramConfig.getFont() != null) {
            return "#font:" + diagramConfig.getFont() + System.lineSeparator();
        }
        return "";
    }

    private String directionStyleDeclaration() {
        if (diagramConfig.getDirection() != null) {
            return "#direction:" + diagramConfig.getDirection() + System.lineSeparator();
        }
        return "";
    }

    private String acyclerStyleDeclaration() {
        if (diagramConfig.getDirection() != null) {
            return "#acycler: " + diagramConfig.getAcycler() + System.lineSeparator();
        }
        return "";
    }

    private String rankerStyleDeclaration() {
        if (diagramConfig.getDirection() != null) {
            return "#ranker: " + diagramConfig.getRanker() + System.lineSeparator();
        }
        return "";
    }
}
