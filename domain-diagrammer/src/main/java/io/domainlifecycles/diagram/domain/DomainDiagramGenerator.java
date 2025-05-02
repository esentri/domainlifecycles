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
import io.domainlifecycles.mirror.api.DomainMirror;

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

    /**
     * Represents the style tag identifier used for Aggregate Root elements
     * in the domain diagram generation process.
     *
     * This tag is utilized to apply specific style declarations and visual
     * distinctions to elements identified as Aggregate Roots during the
     * rendering of domain diagrams. The value of this constant is used to
     * associate a style definition with the Aggregate Root concept in the
     * generated output.
     */
    public static final String AGGREGATE_ROOT_STYLE_TAG = "AR";

    /**
     * Represents the style tag identifier for an aggregate frame in a domain diagram.
     * Used to apply specific styling configurations associated with aggregates in the generated diagram.
     */
    public static final String AGGREGATE_FRAME_STYLE_TAG = "AF";

    /**
     * A constant representing the style tag for entities in the domain diagram.
     * This tag is used to apply specific visual styles to entities within the generated diagram.
     */
    public static final String ENTITY_STYLE_TAG = "E";

    /**
     * Represents the style tag identifier for Value Object elements in the domain diagram.
     * This constant is used to tag and apply specific styling for elements classified as Value Objects
     * when generating a domain diagram.
     */
    public static final String VALUE_OBJECT_STYLE_TAG = "VO";

    /**
     * A constant representing the style tag used for enums in the domain diagram generation process.
     * This style tag is used as an identifier to apply specific visual styles to elements in the diagram
     * that are classified as enums.
     */
    public static final String ENUM_STYLE_TAG = "V";

    /**
     * Represents the style tag associated with domain services within the domain diagram.
     *
     * This constant is used to define and apply specific styling rules or visual representation
     * for domain services when generating a domain diagram. It ensures consistency and clarity
     * in the visual depiction of the domain model elements related to services.
     */
    public static final String DOMAIN_SERVICE_STYLE_TAG = "DS";

    /**
     * Represents the style tag used to define the visual presentation for a Service Kind
     * element in the domain diagram. This tag is utilized within the styling configuration
     * to apply specific formatting rules for Service Kind components when generating
     * the domain diagram.
     */
    public static final String SERVICE_KIND_STYLE_TAG = "SK";

    /**
     * Tag used to define the style for application services within the domain diagram.
     * This constant is utilized in the diagram generation process to apply specific
     * style configurations associated with application services.
     */
    public static final String APPLICATION_SERVICE_STYLE_TAG = "AS";

    /**
     * A constant representing the style tag used for domain events in the domain diagram generation process.
     * This tag is utilized to apply specific visual styles or configurations to domain event elements
     * within the generated domain diagrams.
     */
    public static final String DOMAIN_EVENT_STYLE_TAG = "DE";

    /**
     * Represents the style tag identifier for repositories in the domain diagram generation process.
     * This constant is used to associate specific styling rules or configurations
     * that are applied to repository elements when generating domain diagrams.
     */
    public static final String REPOSITORY_STYLE_TAG = "R";

    /**
     * Represents the style tag used for domain command elements in the domain diagram.
     * This tag is used to apply specific styling or configurations for domain commands
     * when generating a domain diagram.
     */
    public static final String DOMAIN_COMMAND_STYLE_TAG = "DC";

    /**
     * Represents the style tag used to indicate the visual representation for Read Models
     * in the domain diagram generated by the {@code DomainDiagramGenerator}.
     */
    public static final String READ_MODEL_STYLE_TAG = "RM";

    /**
     * A constant representing the style tag associated with a Query Handler in the domain diagram.
     * This tag is used to apply specific styling definitions to diagram elements categorized as Query Handlers.
     */
    public static final String QUERY_HANDLER_STYLE_TAG = "QH";

    /**
     * Represents the style tag identifier for outbound services in the domain diagram.
     * This tag is used to define visual styles specifically for outbound services
     * when generating domain diagrams within the system.
     */
    public static final String OUTBOUND_SERVICE_STYLE_TAG = "OS";

    /**
     * A constant representing the style tag for the identity model element in a domain diagram.
     * Used to apply specific styling rules for identity elements within the generated diagram.
     */
    public static final String IDENTITY_STYLE_TAG = "I";

    /**
     * Initializes the DomainDiagramGenerator with a given {@link DomainDiagramConfig}
     *
     * @param diagramConfig diagram configuration
     * @param domainMirror domain
     */
    public DomainDiagramGenerator(final DomainDiagramConfig diagramConfig, DomainMirror domainMirror) {
        this.diagramConfig = diagramConfig;
        this.domainMapper = new DomainMapper(diagramConfig, domainMirror);
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
        builder.append(backgroundColorStyleDeclaration());

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

    private String backgroundColorStyleDeclaration() {
        if (diagramConfig.getBackgroundColor() != null) {
            return "#background: " + diagramConfig.getBackgroundColor() + System.lineSeparator();
        }
        return "";
    }
}
