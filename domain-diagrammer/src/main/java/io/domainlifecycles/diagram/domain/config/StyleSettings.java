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
 *  Copyright 2019-2025 the original author or authors.
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

package io.domainlifecycles.diagram.domain.config;

/**
 * Represents a set of style configuration settings for various components
 * in a system. Each style setting is customizable and can be used to define
 * visual or styling properties for corresponding domain-specific elements.
 * Default values are provided for all style settings, but they can be overridden
 * using the builder.
 * <p>
 * This class is immutable and uses a builder to construct instances with
 * specified customizations or default values for undefined properties.
 * <p>
 * Components represented by this style settings include:
 * - Aggregate Roots
 * - Frames around Aggregates
 * - Entities
 * - Value Objects
 * - Enumerations
 * - Identity components
 * - Domain Events
 * - Domain Commands
 * - Application Services
 * - Domain Services
 * - Repositories
 * - Read Models
 * - Query Handlers
 * - Outbound Services
 * - Services of unspecified kind
 * <p>
 * Additionally, this configuration supports styles for font and background color.
 * <p>
 * The builder pattern provides type-safe and property-specific methods for customization.
 *
 * @author Mario Herb
 */
public class StyleSettings {

    private static final String DEFAULT_NOTE_STYLE = "fill=#ffffff italic";
    private static final String DEFAULT_AGGREGATE_ROOT_STYLE = "fill=#8f8f bold";
    private static final String DEFAULT_AGGREGATE_FRAME_STYLE = "visual=frame align=left";
    private static final String DEFAULT_ENTITY_STYLE = "fill=#88AAFF bold";
    private static final String DEFAULT_VALUE_OBJECT_STYLE = "fill=#FFFFCC bold";
    private static final String DEFAULT_ENUM_STYLE = "fill=#FFFFCC bold";
    private static final String DEFAULT_IDENTITY_STYLE = "fill=#FFFFCC bold";
    private static final String DEFAULT_DOMAIN_EVENT_STYLE = "fill=#CCFFFF bold";
    private static final String DEFAULT_DOMAIN_COMMAND_STYLE = "fill=#FFB266 bold";
    private static final String DEFAULT_APPLICATION_SERVICE_STYLE = "bold";
    private static final String DEFAULT_DOMAIN_SERVICE_STYLE = "fill=#E0E0E0 bold";
    private static final String DEFAULT_REPOSITORY_STYLE = "fill=#C0C0C0 bold";
    private static final String DEFAULT_READ_MODEL_STYLE = "fill=#FFCCE5 bold";
    private static final String DEFAULT_QUERY_HANDLER_STYLE = "fill=#C0C0C0 bold";
    private static final String DEFAULT_OUTBOUND_SERVICE_STYLE = "fill=#C0C0C0 bold";
    private static final String DEFAULT_UNSPECIFIED_SERVICE_KIND_STYLE = "fill=#C0C0C0 bold";
    private static final String DEFAULT_FONT = "Helvetica";
    private static final String DEFAULT_BACKGROUND_COLOR = "transparent";

    private final String noteStyle;
    private final String aggregateRootStyle;
    private final String aggregateFrameStyle;
    private final String entityStyle;
    private final String valueObjectStyle;
    private final String enumStyle;
    private final String identityStyle;
    private final String domainEventStyle;
    private final String domainCommandStyle;
    private final String applicationServiceStyle;
    private final String domainServiceStyle;
    private final String repositoryStyle;
    private final String readModelStyle;
    private final String queryHandlerStyle;
    private final String outboundServiceStyle;
    private final String unspecifiedServiceKindStyle;
    private final String font;
    private final String backgroundColor;

    private StyleSettings(
        String noteStyle,
        String aggregateRootStyle,
        String aggregateFrameStyle,
        String entityStyle,
        String valueObjectStyle,
        String enumStyle,
        String identityStyle,
        String domainEventStyle,
        String domainCommandStyle,
        String applicationServiceStyle,
        String domainServiceStyle,
        String repositoryStyle,
        String readModelStyle,
        String queryHandlerStyle,
        String outboundServiceStyle,
        String unspecifiedServiceKindStyle,
        String font,
        String backgroundColor
    ) {
        this.noteStyle = noteStyle;
        this.aggregateRootStyle = aggregateRootStyle;
        this.aggregateFrameStyle = aggregateFrameStyle;
        this.entityStyle = entityStyle;
        this.valueObjectStyle = valueObjectStyle;
        this.enumStyle = enumStyle;
        this.identityStyle = identityStyle;
        this.domainEventStyle = domainEventStyle;
        this.domainCommandStyle = domainCommandStyle;
        this.applicationServiceStyle = applicationServiceStyle;
        this.domainServiceStyle = domainServiceStyle;
        this.repositoryStyle = repositoryStyle;
        this.readModelStyle = readModelStyle;
        this.queryHandlerStyle = queryHandlerStyle;
        this.outboundServiceStyle = outboundServiceStyle;
        this.unspecifiedServiceKindStyle = unspecifiedServiceKindStyle;
        this.font = font;
        this.backgroundColor = backgroundColor;
    }

    /**
     * Gets the style configuration for note elements.
     *
     * @return the style string for notes
     */
    public String getNoteStyle() {
        return noteStyle;
    }

    /**
     * Gets the style configuration for aggregate root elements.
     *
     * @return the style string for aggregate roots
     */
    public String getAggregateRootStyle() {
        return aggregateRootStyle;
    }

    /**
     * Gets the style configuration for aggregate frame elements.
     *
     * @return the style string for aggregate frames
     */
    public String getAggregateFrameStyle() {
        return aggregateFrameStyle;
    }

    /**
     * Gets the style configuration for entity elements.
     *
     * @return the style string for entities
     */
    public String getEntityStyle() {
        return entityStyle;
    }

    /**
     * Gets the style configuration for value object elements.
     *
     * @return the style string for value objects
     */
    public String getValueObjectStyle() {
        return valueObjectStyle;
    }

    /**
     * Gets the style configuration for enum elements.
     *
     * @return the style string for enums
     */
    public String getEnumStyle() {
        return enumStyle;
    }

    /**
     * Gets the style configuration for identity elements.
     *
     * @return the style string for identity elements
     */
    public String getIdentityStyle() {
        return identityStyle;
    }

    /**
     * Gets the style configuration for domain event elements.
     *
     * @return the style string for domain events
     */
    public String getDomainEventStyle() {
        return domainEventStyle;
    }

    /**
     * Gets the style configuration for domain command elements.
     *
     * @return the style string for domain commands
     */
    public String getDomainCommandStyle() {
        return domainCommandStyle;
    }

    /**
     * Gets the style configuration for application service elements.
     *
     * @return the style string for application services
     */
    public String getApplicationServiceStyle() {
        return applicationServiceStyle;
    }

    /**
     * Gets the style configuration for domain service elements.
     *
     * @return the style string for domain services
     */
    public String getDomainServiceStyle() {
        return domainServiceStyle;
    }

    /**
     * Gets the style configuration for repository elements.
     *
     * @return the style string for repositories
     */
    public String getRepositoryStyle() {
        return repositoryStyle;
    }

    /**
     * Gets the style configuration for read model elements.
     *
     * @return the style string for read models
     */
    public String getReadModelStyle() {
        return readModelStyle;
    }

    /**
     * Gets the style configuration for query handler elements.
     *
     * @return the style string for query handlers
     */
    public String getQueryHandlerStyle() {
        return queryHandlerStyle;
    }

    /**
     * Gets the style configuration for outbound service elements.
     *
     * @return the style string for outbound services
     */
    public String getOutboundServiceStyle() {
        return outboundServiceStyle;
    }

    /**
     * Gets the style configuration for unspecified service kind elements.
     *
     * @return the style string for unspecified service kinds
     */
    public String getUnspecifiedServiceKindStyle() {
        return unspecifiedServiceKindStyle;
    }

    /**
     * Gets the font configuration.
     *
     * @return the font string
     */
    public String getFont() {
        return font;
    }

    /**
     * Gets the background color configuration.
     *
     * @return the background color string
     */
    public String getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Creates a new builder instance for constructing {@code StyleSettings} objects.
     *
     * @return a new instance of {@code StyleSettingsBuilder} to configure and build {@code StyleSettings}.
     */
    public static StyleSettingsBuilder builder() {
        return new StyleSettingsBuilder();
    }

    /**
     * A builder class for creating instances of {@code StyleSettings} with customizable styles
     * for various elements in the system.
     * <p>
     * This builder allows step-by-step configuration of the visual or stylistic properties
     * for components, such as aggregate roots, entities, value objects, and more. If a style is
     * not explicitly set, default values are used when building the resulting {@code StyleSettings}
     * instance.
     */
    public static class StyleSettingsBuilder {
        private String noteStyle$value;
        private String aggregateRootStyle$value;
        private String aggregateFrameStyle$value;
        private String entityStyle$value;
        private String valueObjectStyle$value;
        private String enumStyle$value;
        private String identityStyle$value;
        private String domainEventStyle$value;
        private String domainCommandStyle$value;
        private String applicationServiceStyle$value;
        private String domainServiceStyle$value;
        private String repositoryStyle$value;
        private String readModelStyle$value;
        private String queryHandlerStyle$value;
        private String outboundServiceStyle$value;
        private String unspecifiedServiceKindStyle$value;
        private String font$value;
        private String backgroundColor$value;

        /**
         * Sets the style configuration for a note element in the domain diagram.
         *
         * @param value the style to be applied to the note
         * @return the current instance of {@code StyleSettingsBuilder} for method chaining
         */
        public StyleSettingsBuilder withNoteStyle(String value) {
            this.noteStyle$value = value;
            return this;
        }

        /**
         * Sets the style configuration for an aggregate root element in the domain diagram.
         *
         * @param value the style to be applied to the aggregate root
         * @return the current instance of {@code StyleSettingsBuilder} for method chaining
         */
        public StyleSettingsBuilder withAggregateRootStyle(String value) {
            this.aggregateRootStyle$value = value;
            return this;
        }

        /**
         * Sets the style configuration for an aggregate frame element in the domain diagram.
         *
         * @param value the style to be applied to the aggregate frame
         * @return the current instance of {@code StyleSettingsBuilder} for method chaining
         */
        public StyleSettingsBuilder withAggregateFrameStyle(String value) {
            this.aggregateFrameStyle$value = value;
            return this;
        }

        /**
         * Sets the style configuration for an entity element in the domain diagram.
         *
         * @param value the style to be applied to the entity
         * @return the current instance of {@code StyleSettingsBuilder} for method chaining
         */
        public StyleSettingsBuilder withEntityStyle(String value) {
            this.entityStyle$value = value;
            return this;
        }

        /**
         * Sets the style configuration for a value object element in the domain diagram.
         *
         * @param value the style to be applied to the value object
         * @return the current instance of {@code StyleSettingsBuilder} for method chaining
         */
        public StyleSettingsBuilder withValueObjectStyle(String value) {
            this.valueObjectStyle$value = value;
            return this;
        }

        /**
         * Sets the style configuration for an enum element in the domain diagram.
         *
         * @param value the style to be applied to the enum
         * @return the current instance of {@code StyleSettingsBuilder} for method chaining
         */
        public StyleSettingsBuilder withEnumStyle(String value) {
            this.enumStyle$value = value;
            return this;
        }

        /**
         * Sets the style configuration for an identity element in the domain diagram.
         *
         * @param value the style to be applied to the identity element
         * @return the current instance of {@code StyleSettingsBuilder} for method chaining
         */
        public StyleSettingsBuilder withIdentityStyle(String value) {
            this.identityStyle$value = value;
            return this;
        }

        /**
         * Sets the style configuration for a domain event element in the domain diagram.
         *
         * @param value the style to be applied to the domain event
         * @return the current instance of {@code StyleSettingsBuilder} for method chaining
         */
        public StyleSettingsBuilder withDomainEventStyle(String value) {
            this.domainEventStyle$value = value;
            return this;
        }

        /**
         * Sets the style configuration for a domain command element in the domain diagram.
         *
         * @param value the style to be applied to the domain command
         * @return the current instance of {@code StyleSettingsBuilder} for method chaining
         */
        public StyleSettingsBuilder withDomainCommandStyle(String value) {
            this.domainCommandStyle$value = value;
            return this;
        }

        /**
         * Sets the style configuration for an application service element in the domain diagram.
         *
         * @param value the style to be applied to the application service
         * @return the current instance of {@code StyleSettingsBuilder} for method chaining
         */
        public StyleSettingsBuilder withApplicationServiceStyle(String value) {
            this.applicationServiceStyle$value = value;
            return this;
        }

        /**
         * Sets the style configuration for a domain service element in the domain diagram.
         *
         * @param value the style to be applied to the domain service
         * @return the current instance of {@code StyleSettingsBuilder} for method chaining
         */
        public StyleSettingsBuilder withDomainServiceStyle(String value) {
            this.domainServiceStyle$value = value;
            return this;
        }

        /**
         * Sets the style configuration for a repository element in the domain diagram.
         *
         * @param value the style to be applied to the repository
         * @return the current instance of {@code StyleSettingsBuilder} for method chaining
         */
        public StyleSettingsBuilder withRepositoryStyle(String value) {
            this.repositoryStyle$value = value;
            return this;
        }

        /**
         * Sets the style configuration for a read model element in the domain diagram.
         *
         * @param value the style to be applied to the read model
         * @return the current instance of {@code StyleSettingsBuilder} for method chaining
         */
        public StyleSettingsBuilder withReadModelStyle(String value) {
            this.readModelStyle$value = value;
            return this;
        }

        /**
         * Sets the style configuration for a query handler element in the domain diagram.
         *
         * @param value the style to be applied to the query handler
         * @return the current instance of {@code StyleSettingsBuilder} for method chaining
         */
        public StyleSettingsBuilder withQueryHandlerStyle(String value) {
            this.queryHandlerStyle$value = value;
            return this;
        }

        /**
         * Sets the style configuration for an outbound service element in the domain diagram.
         *
         * @param value the style to be applied to the outbound service
         * @return the current instance of {@code StyleSettingsBuilder} for method chaining
         */
        public StyleSettingsBuilder withOutboundServiceStyle(String value) {
            this.outboundServiceStyle$value = value;
            return this;
        }

        /**
         * Sets the style configuration for services with unspecified kind in the domain diagram.
         *
         * @param value the style to be applied to unspecified service kinds
         * @return the current instance of {@code StyleSettingsBuilder} for method chaining
         */
        public StyleSettingsBuilder withUnspecifiedServiceKindStyle(String value) {
            this.unspecifiedServiceKindStyle$value = value;
            return this;
        }

        /**
         * Sets the font configuration for elements in the domain diagram.
         *
         * @param value the font to be used
         * @return the current instance of {@code StyleSettingsBuilder} for method chaining
         */
        public StyleSettingsBuilder withFont(String value) {
            this.font$value = value;
            return this;
        }

        /**
         * Sets the background color configuration for the domain diagram.
         *
         * @param value the background color to be applied
         * @return the current instance of {@code StyleSettingsBuilder} for method chaining
         */
        public StyleSettingsBuilder withBackgroundColor(String value) {
            this.backgroundColor$value = value;
            return this;
        }

        /**
         * Builds and returns a new {@code StyleSettings} instance with the configured values.
         * For any style setting that was not explicitly set, the default value will be used.
         *
         * @return a new {@code StyleSettings} instance with the configured values
         */
        public StyleSettings build() {
            return new StyleSettings(
                noteStyle$value == null ? DEFAULT_NOTE_STYLE : noteStyle$value,
                aggregateRootStyle$value == null ? DEFAULT_AGGREGATE_ROOT_STYLE : aggregateRootStyle$value,
                aggregateFrameStyle$value == null ? DEFAULT_AGGREGATE_FRAME_STYLE : aggregateFrameStyle$value,
                entityStyle$value == null ? DEFAULT_ENTITY_STYLE : entityStyle$value,
                valueObjectStyle$value == null ? DEFAULT_VALUE_OBJECT_STYLE : valueObjectStyle$value,
                enumStyle$value == null ? DEFAULT_ENUM_STYLE : enumStyle$value,
                identityStyle$value == null ? DEFAULT_IDENTITY_STYLE : identityStyle$value,
                domainEventStyle$value == null ? DEFAULT_DOMAIN_EVENT_STYLE : domainEventStyle$value,
                domainCommandStyle$value == null ? DEFAULT_DOMAIN_COMMAND_STYLE : domainCommandStyle$value,
                applicationServiceStyle$value == null ? DEFAULT_APPLICATION_SERVICE_STYLE : applicationServiceStyle$value,
                domainServiceStyle$value == null ? DEFAULT_DOMAIN_SERVICE_STYLE : domainServiceStyle$value,
                repositoryStyle$value == null ? DEFAULT_REPOSITORY_STYLE : repositoryStyle$value,
                readModelStyle$value == null ? DEFAULT_READ_MODEL_STYLE : readModelStyle$value,
                queryHandlerStyle$value == null ? DEFAULT_QUERY_HANDLER_STYLE : queryHandlerStyle$value,
                outboundServiceStyle$value == null ? DEFAULT_OUTBOUND_SERVICE_STYLE : outboundServiceStyle$value,
                unspecifiedServiceKindStyle$value == null ? DEFAULT_UNSPECIFIED_SERVICE_KIND_STYLE : unspecifiedServiceKindStyle$value,
                font$value == null ? DEFAULT_FONT : font$value,
                backgroundColor$value == null ? DEFAULT_BACKGROUND_COLOR : backgroundColor$value
            );
        }
    }
}
