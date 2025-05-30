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

package io.domainlifecycles.plugins.diagram;

import io.domainlifecycles.diagram.domain.config.DiagramTrimSettings;
import io.domainlifecycles.diagram.domain.config.DomainDiagramConfig;
import io.domainlifecycles.diagram.domain.config.DomainDiagramConfig.DomainDiagramConfigBuilder;
import io.domainlifecycles.diagram.domain.config.GeneralVisualSettings;
import io.domainlifecycles.diagram.domain.config.LayoutSettings;
import io.domainlifecycles.diagram.domain.config.StyleSettings;

import java.util.List;

/**
 * A configuration class for generating domain-level diagrams, providing a variety of options to
 * customize diagram appearance and content.
 *
 * This class contains a number of fields and methods to define properties for diagram generation
 * such as styles, visibility of specific elements, file export settings, and filters. DiagramConfig
 * ensures flexibility in defining how domain elements like aggregates, services, events, commands,
 * and more are visualized and organized.
 *
 * Key functionalities include:
 * - Setting diagram file type, name, and export options.
 * - Customizing styles for various domain elements.
 * - Configuring visibility of fields, methods, relationships, and specific domain concepts.
 * - Supporting filters for blacklisted classes, fields, and methods.
 * - Adjusting fonts, organization, background colors, and diagram direction.
 *
 * The configuration can be mapped to a corresponding DomainDiagramConfig object
 * for further operations.
 *
 * @author Leon Völlinger
 */
public class DiagramConfig {

    private FileType fileType;
    private String fileName;

    private String aggregateRootStyle;
    private String aggregateFrameStyle;
    private String entityStyle;
    private String valueObjectStyle;
    private String enumStyle;
    private String identityStyle;
    private String domainEventStyle;
    private String domainCommandStyle;
    private String applicationServiceStyle;
    private String domainServiceStyle;
    private String repositoryStyle;
    private String readModelStyle;
    private String queryHandlerStyle;
    private String outboundServiceStyle;
    private String font;
    private String direction;
    private String ranker;
    private String acycler;
    private String backgroundColor;
    private List<String> classesBlacklist;
    private Boolean showFields;
    private Boolean showFullQualifiedClassNames;
    private Boolean showAssertions;
    private Boolean showMethods;
    private Boolean showOnlyPublicMethods;
    private Boolean showAggregates;
    private Boolean showAggregateFields;
    private Boolean showAggregateMethods;
    private Boolean showDomainEvents;
    private Boolean showDomainEventFields;
    private Boolean showDomainEventMethods;
    private Boolean showDomainCommands;
    private Boolean showOnlyTopLevelDomainCommandRelations;
    private Boolean showDomainCommandFields;
    private Boolean showDomainCommandMethods;
    private Boolean showDomainServices;
    private Boolean showDomainServiceFields;
    private Boolean showDomainServiceMethods;
    private Boolean showApplicationServices;
    private Boolean showApplicationServiceFields;
    private Boolean showApplicationServiceMethods;
    private Boolean showRepositories;
    private Boolean showRepositoryFields;
    private Boolean showRepositoryMethods;
    private Boolean showReadModels;
    private Boolean showReadModelFields;
    private Boolean showReadModelMethods;
    private Boolean showQueryHandlers;
    private Boolean showQueryHandlerFields;
    private Boolean showQueryHandlerMethods;
    private Boolean showOutboundServices;
    private Boolean showOutboundServiceFields;
    private Boolean showOutboundServiceMethods;
    private Boolean showUnspecifiedServiceKinds;
    private Boolean showUnspecifiedServiceKindFields;
    private Boolean showUnspecifiedServiceKindMethods;
    private Boolean callApplicationServiceDriver;
    private List<String> fieldBlacklist;
    private List<String> methodBlacklist;
    private Boolean showInheritedMembersInClasses;
    private Boolean showObjectMembersInClasses;
    private Boolean multiplicityInLabel;
    private Boolean fieldStereotypes;
    private List<String> includeConnectedTo;
    private List<String> includeConnectedToIngoing;
    private List<String> includeConnectedToOutgoing;
    private List<String> excludeConnectedToIngoing;
    private List<String> excludeConnectedToOutgoing;
    private List<String> explicitlyIncludedPackageNames;
    private Boolean showAllInheritanceStructures;
    private Boolean showInheritanceStructuresInAggregates;
    private Boolean showInheritanceStructuresForServiceKinds;
    private Boolean showInheritanceStructuresForReadModels;
    private Boolean showInheritanceStructuresForDomainEvents;
    private Boolean showInheritanceStructuresForDomainCommands;

    /**
     * Gets the file type for the diagram output
     *
     * @return The file type
     */
    public FileType getFileType() {
        return fileType;
    }

    /**
     * Sets the file type for the diagram output
     *
     * @param fileType The file type to set
     */
    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    /**
     * Gets the output filename for the diagram
     *
     * @return The filename
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the output filename for the diagram
     *
     * @param fileName The filename to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Gets the style configuration for aggregate roots
     *
     * @return The aggregate root style configuration
     */
    public String getAggregateRootStyle() {
        return aggregateRootStyle;
    }

    /**
     * Sets the style configuration for aggregate roots
     *
     * @param aggregateRootStyle The style configuration to set
     */
    public void setAggregateRootStyle(String aggregateRootStyle) {
        this.aggregateRootStyle = aggregateRootStyle;
    }

    /**
     * Gets the style configuration for aggregate frames
     *
     * @return The aggregate frame style configuration
     */
    public String getAggregateFrameStyle() {
        return aggregateFrameStyle;
    }

    /**
     * Sets the style configuration for aggregate frames
     *
     * @param aggregateFrameStyle The style configuration to set
     */
    public void setAggregateFrameStyle(String aggregateFrameStyle) {
        this.aggregateFrameStyle = aggregateFrameStyle;
    }

    /**
     * Gets the style configuration for entities
     *
     * @return The entity style configuration
     */
    public String getEntityStyle() {
        return entityStyle;
    }

    /**
     * Sets the style configuration for entities
     *
     * @param entityStyle The style configuration to set
     */
    public void setEntityStyle(String entityStyle) {
        this.entityStyle = entityStyle;
    }

    /**
     * Gets the style configuration for value objects
     *
     * @return The value object style configuration
     */
    public String getValueObjectStyle() {
        return valueObjectStyle;
    }

    /**
     * Sets the style configuration for value objects
     *
     * @param valueObjectStyle The style configuration to set
     */
    public void setValueObjectStyle(String valueObjectStyle) {
        this.valueObjectStyle = valueObjectStyle;
    }

    /**
     * Gets the style configuration for enums
     *
     * @return The enum style configuration
     */
    public String getEnumStyle() {
        return enumStyle;
    }

    /**
     * Sets the style configuration for enums
     *
     * @param enumStyle The style configuration to set
     */
    public void setEnumStyle(String enumStyle) {
        this.enumStyle = enumStyle;
    }

    /**
     * Gets the style configuration for identity types
     *
     * @return The identity style configuration
     */
    public String getIdentityStyle() {
        return identityStyle;
    }

    /**
     * Sets the style configuration for identity types
     *
     * @param identityStyle The style configuration to set
     */
    public void setIdentityStyle(String identityStyle) {
        this.identityStyle = identityStyle;
    }

    /**
     * Gets the style configuration for domain events
     *
     * @return The domain event style configuration
     */
    public String getDomainEventStyle() {
        return domainEventStyle;
    }

    /**
     * Sets the style configuration for domain events
     *
     * @param domainEventStyle The style configuration to set
     */
    public void setDomainEventStyle(String domainEventStyle) {
        this.domainEventStyle = domainEventStyle;
    }

    /**
     * Gets the style configuration for domain commands
     *
     * @return The domain command style configuration
     */
    public String getDomainCommandStyle() {
        return domainCommandStyle;
    }

    /**
     * Sets the style configuration for domain commands
     *
     * @param domainCommandStyle The style configuration to set
     */
    public void setDomainCommandStyle(String domainCommandStyle) {
        this.domainCommandStyle = domainCommandStyle;
    }

    /**
     * Gets the style configuration for application services
     *
     * @return The application service style configuration
     */
    public String getApplicationServiceStyle() {
        return applicationServiceStyle;
    }

    /**
     * Sets the style configuration for application services
     *
     * @param applicationServiceStyle The style configuration to set
     */
    public void setApplicationServiceStyle(String applicationServiceStyle) {
        this.applicationServiceStyle = applicationServiceStyle;
    }

    /**
     * Gets the style configuration for domain services
     *
     * @return The domain service style configuration
     */
    public String getDomainServiceStyle() {
        return domainServiceStyle;
    }

    /**
     * Sets the style configuration for domain services
     *
     * @param domainServiceStyle The style configuration to set
     */
    public void setDomainServiceStyle(String domainServiceStyle) {
        this.domainServiceStyle = domainServiceStyle;
    }

    /**
     * Gets the style configuration for repositories
     *
     * @return The repository style configuration
     */
    public String getRepositoryStyle() {
        return repositoryStyle;
    }

    /**
     * Sets the style configuration for repositories
     *
     * @param repositoryStyle The style configuration to set
     */
    public void setRepositoryStyle(String repositoryStyle) {
        this.repositoryStyle = repositoryStyle;
    }

    /**
     * Gets the style configuration for read models
     *
     * @return The read model style configuration
     */
    public String getReadModelStyle() {
        return readModelStyle;
    }

    /**
     * Sets the style configuration for read models
     *
     * @param readModelStyle The style configuration to set
     */
    public void setReadModelStyle(String readModelStyle) {
        this.readModelStyle = readModelStyle;
    }

    /**
     * Gets the style configuration for query handlers
     *
     * @return The query handler style configuration
     */
    public String getQueryHandlerStyle() {
        return queryHandlerStyle;
    }

    /**
     * Sets the style configuration for query handlers
     *
     * @param queryHandlerStyle The style configuration to set
     */
    public void setQueryHandlerStyle(String queryHandlerStyle) {
        this.queryHandlerStyle = queryHandlerStyle;
    }

    /**
     * Gets the style configuration for outbound services
     *
     * @return The outbound service style configuration
     */
    public String getOutboundServiceStyle() {
        return outboundServiceStyle;
    }

    /**
     * Sets the style configuration for outbound services
     *
     * @param outboundServiceStyle The style configuration to set
     */
    public void setOutboundServiceStyle(String outboundServiceStyle) {
        this.outboundServiceStyle = outboundServiceStyle;
    }

    /**
     * Gets the font setting for the diagram
     *
     * @return The font name
     */
    public String getFont() {
        return font;
    }

    /**
     * Sets the font setting for the diagram
     *
     * @param font The font name to use
     */
    public void setFont(String font) {
        this.font = font;
    }

    /**
     * Gets the layout direction for the diagram
     *
     * @return The diagram direction
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Sets the layout direction for the diagram
     *
     * @param direction The diagram direction
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * Gets the ranker setting for layout
     *
     * @return The ranker setting
     */
    public String getRanker() {
        return ranker;
    }

    /**
     * Sets the ranker setting for layout
     *
     * @param ranker The ranker setting
     */
    public void setRanker(String ranker) {
        this.ranker = ranker;
    }

    /**
     * Gets the acycler setting for layout
     *
     * @return The acycler setting
     */
    public String getAcycler() {
        return acycler;
    }

    /**
     * Sets the acycler setting for layout
     *
     * @param acycler The acycler setting
     */
    public void setAcycler(String acycler) {
        this.acycler = acycler;
    }

    /**
     * Gets the background color for the diagram
     *
     * @return The background color
     */
    public String getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Sets the background color for the diagram
     *
     * @param backgroundColor The background color to set
     */
    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * Gets the list of class names to exclude from the diagram
     *
     * @return The blacklisted class names
     */
    public List<String> getClassesBlacklist() {
        return classesBlacklist;
    }

    /**
     * Sets the list of class names to exclude from the diagram
     *
     * @param classesBlacklist The class names to blacklist
     */
    public void setClassesBlacklist(List<String> classesBlacklist) {
        this.classesBlacklist = classesBlacklist;
    }

    /**
     * Gets whether to show fields in the diagram
     *
     * @return Whether fields should be shown
     */
    public Boolean getShowFields() {
        return showFields;
    }

    /**
     * Sets whether to show fields in the diagram
     *
     * @param showFields Whether fields should be shown
     */
    public void setShowFields(Boolean showFields) {
        this.showFields = showFields;
    }

    /**
     * Gets whether to show fully qualified class names
     *
     * @return Whether fully qualified names should be shown
     */
    public Boolean getShowFullQualifiedClassNames() {
        return showFullQualifiedClassNames;
    }

    /**
     * Sets whether to show fully qualified class names
     *
     * @param showFullQualifiedClassNames Whether fully qualified names should be shown
     */
    public void setShowFullQualifiedClassNames(Boolean showFullQualifiedClassNames) {
        this.showFullQualifiedClassNames = showFullQualifiedClassNames;
    }

    /**
     * Gets whether to show assertions in the diagram
     *
     * @return Whether assertions should be shown
     */
    public Boolean getShowAssertions() {
        return showAssertions;
    }

    /**
     * Sets whether to show assertions in the diagram
     *
     * @param showAssertions Whether assertions should be shown
     */
    public void setShowAssertions(Boolean showAssertions) {
        this.showAssertions = showAssertions;
    }

    /**
     * Gets whether to show methods in the diagram
     *
     * @return Whether methods should be shown
     */
    public Boolean getShowMethods() {
        return showMethods;
    }

    /**
     * Sets whether to show methods in the diagram
     *
     * @param showMethods Whether methods should be shown
     */
    public void setShowMethods(Boolean showMethods) {
        this.showMethods = showMethods;
    }

    /**
     * Gets whether to only show public methods
     *
     * @return Whether only public methods should be shown
     */
    public Boolean getShowOnlyPublicMethods() {
        return showOnlyPublicMethods;
    }

    /**
     * Sets whether to only show public methods
     *
     * @param showOnlyPublicMethods Whether only public methods should be shown
     */
    public void setShowOnlyPublicMethods(Boolean showOnlyPublicMethods) {
        this.showOnlyPublicMethods = showOnlyPublicMethods;
    }

    /**
     * Gets whether to show aggregates in the diagram
     *
     * @return Whether aggregates should be shown
     */
    public Boolean getShowAggregates() {
        return showAggregates;
    }

    /**
     * Sets whether to show aggregates in the diagram
     *
     * @param showAggregates Whether aggregates should be shown
     */
    public void setShowAggregates(Boolean showAggregates) {
        this.showAggregates = showAggregates;
    }

    /**
     * Gets whether to show aggregate fields
     *
     * @return Whether aggregate fields should be shown
     */
    public Boolean getShowAggregateFields() {
        return showAggregateFields;
    }

    /**
     * Sets whether to show aggregate fields
     *
     * @param showAggregateFields Whether aggregate fields should be shown
     */
    public void setShowAggregateFields(Boolean showAggregateFields) {
        this.showAggregateFields = showAggregateFields;
    }

    /**
     * Gets whether to show aggregate methods
     *
     * @return Whether aggregate methods should be shown
     */
    public Boolean getShowAggregateMethods() {
        return showAggregateMethods;
    }

    /**
     * Sets whether to show aggregate methods
     *
     * @param showAggregateMethods Whether aggregate methods should be shown
     */
    public void setShowAggregateMethods(Boolean showAggregateMethods) {
        this.showAggregateMethods = showAggregateMethods;
    }

    /**
     * Gets whether to show domain events in the diagram
     *
     * @return Whether domain events should be shown
     */
    public Boolean getShowDomainEvents() {
        return showDomainEvents;
    }

    /**
     * Sets whether to show domain events in the diagram
     *
     * @param showDomainEvents Whether domain events should be shown
     */
    public void setShowDomainEvents(Boolean showDomainEvents) {
        this.showDomainEvents = showDomainEvents;
    }

    /**
     * Gets whether to show domain event fields
     *
     * @return Whether domain event fields should be shown
     */
    public Boolean getShowDomainEventFields() {
        return showDomainEventFields;
    }

    /**
     * Sets whether to show domain event fields
     *
     * @param showDomainEventFields Whether domain event fields should be shown
     */
    public void setShowDomainEventFields(Boolean showDomainEventFields) {
        this.showDomainEventFields = showDomainEventFields;
    }

    /**
     * Gets whether to show domain event methods
     *
     * @return Whether domain event methods should be shown
     */
    public Boolean getShowDomainEventMethods() {
        return showDomainEventMethods;
    }

    /**
     * Sets whether to show domain event methods
     *
     * @param showDomainEventMethods Whether domain event methods should be shown
     */
    public void setShowDomainEventMethods(Boolean showDomainEventMethods) {
        this.showDomainEventMethods = showDomainEventMethods;
    }

    /**
     * Gets whether to show domain commands in the diagram
     *
     * @return Whether domain commands should be shown
     */
    public Boolean getShowDomainCommands() {
        return showDomainCommands;
    }

    /**
     * Sets whether to show domain commands in the diagram
     *
     * @param showDomainCommands Whether domain commands should be shown
     */
    public void setShowDomainCommands(Boolean showDomainCommands) {
        this.showDomainCommands = showDomainCommands;
    }

    /**
     * Gets whether to only show top level domain command relations
     *
     * @return Whether only top level relations should be shown
     */
    public Boolean getShowOnlyTopLevelDomainCommandRelations() {
        return showOnlyTopLevelDomainCommandRelations;
    }

    /**
     * Sets whether to only show top level domain command relations
     *
     * @param showOnlyTopLevelDomainCommandRelations Whether only top level relations should be shown
     */
    public void setShowOnlyTopLevelDomainCommandRelations(Boolean showOnlyTopLevelDomainCommandRelations) {
        this.showOnlyTopLevelDomainCommandRelations = showOnlyTopLevelDomainCommandRelations;
    }

    /**
     * Gets whether to show domain command fields
     *
     * @return Whether domain command fields should be shown
     */
    public Boolean getShowDomainCommandFields() {
        return showDomainCommandFields;
    }

    /**
     * Sets whether to show domain command fields
     *
     * @param showDomainCommandFields Whether domain command fields should be shown
     */
    public void setShowDomainCommandFields(Boolean showDomainCommandFields) {
        this.showDomainCommandFields = showDomainCommandFields;
    }

    /**
     * Gets whether to show domain command methods
     *
     * @return Whether domain command methods should be shown
     */
    public Boolean getShowDomainCommandMethods() {
        return showDomainCommandMethods;
    }

    /**
     * Sets whether to show domain command methods
     *
     * @param showDomainCommandMethods Whether domain command methods should be shown
     */
    public void setShowDomainCommandMethods(Boolean showDomainCommandMethods) {
        this.showDomainCommandMethods = showDomainCommandMethods;
    }

    /**
     * Gets whether to show domain services in the diagram
     *
     * @return Whether domain services should be shown
     */
    public Boolean getShowDomainServices() {
        return showDomainServices;
    }

    /**
     * Sets whether to show domain services in the diagram
     *
     * @param showDomainServices Whether domain services should be shown
     */
    public void setShowDomainServices(Boolean showDomainServices) {
        this.showDomainServices = showDomainServices;
    }

    /**
     * Gets whether to show domain service fields
     *
     * @return Whether domain service fields should be shown
     */
    public Boolean getShowDomainServiceFields() {
        return showDomainServiceFields;
    }

    /**
     * Sets whether to show domain service fields
     *
     * @param showDomainServiceFields Whether domain service fields should be shown
     */
    public void setShowDomainServiceFields(Boolean showDomainServiceFields) {
        this.showDomainServiceFields = showDomainServiceFields;
    }

    /**
     * Gets whether to show domain service methods
     *
     * @return Whether domain service methods should be shown
     */
    public Boolean getShowDomainServiceMethods() {
        return showDomainServiceMethods;
    }

    /**
     * Sets whether to show domain service methods
     *
     * @param showDomainServiceMethods Whether domain service methods should be shown
     */
    public void setShowDomainServiceMethods(Boolean showDomainServiceMethods) {
        this.showDomainServiceMethods = showDomainServiceMethods;
    }

    /**
     * Gets whether to show application services in the diagram
     *
     * @return Whether application services should be shown
     */
    public Boolean getShowApplicationServices() {
        return showApplicationServices;
    }

    /**
     * Sets whether to show application services in the diagram
     *
     * @param showApplicationServices Whether application services should be shown
     */
    public void setShowApplicationServices(Boolean showApplicationServices) {
        this.showApplicationServices = showApplicationServices;
    }

    /**
     * Gets whether to show application service fields
     *
     * @return Whether application service fields should be shown
     */
    public Boolean getShowApplicationServiceFields() {
        return showApplicationServiceFields;
    }

    /**
     * Sets whether to show application service fields
     *
     * @param showApplicationServiceFields Whether application service fields should be shown
     */
    public void setShowApplicationServiceFields(Boolean showApplicationServiceFields) {
        this.showApplicationServiceFields = showApplicationServiceFields;
    }

    /**
     * Gets whether to show application service methods
     *
     * @return Whether application service methods should be shown
     */
    public Boolean getShowApplicationServiceMethods() {
        return showApplicationServiceMethods;
    }

    /**
     * Sets whether to show application service methods
     *
     * @param showApplicationServiceMethods Whether application service methods should be shown
     */
    public void setShowApplicationServiceMethods(Boolean showApplicationServiceMethods) {
        this.showApplicationServiceMethods = showApplicationServiceMethods;
    }

    /**
     * Gets whether to show repositories in the diagram
     *
     * @return Whether repositories should be shown
     */
    public Boolean getShowRepositories() {
        return showRepositories;
    }

    /**
     * Sets whether to show repositories in the diagram
     *
     * @param showRepositories Whether repositories should be shown
     */
    public void setShowRepositories(Boolean showRepositories) {
        this.showRepositories = showRepositories;
    }

    /**
     * Gets whether to show repository fields
     *
     * @return Whether repository fields should be shown
     */
    public Boolean getShowRepositoryFields() {
        return showRepositoryFields;
    }

    /**
     * Sets whether to show repository fields
     *
     * @param showRepositoryFields Whether repository fields should be shown
     */
    public void setShowRepositoryFields(Boolean showRepositoryFields) {
        this.showRepositoryFields = showRepositoryFields;
    }

    /**
     * Gets whether to show repository methods
     *
     * @return Whether repository methods should be shown
     */
    public Boolean getShowRepositoryMethods() {
        return showRepositoryMethods;
    }

    /**
     * Sets whether to show repository methods
     *
     * @param showRepositoryMethods Whether repository methods should be shown
     */
    public void setShowRepositoryMethods(Boolean showRepositoryMethods) {
        this.showRepositoryMethods = showRepositoryMethods;
    }

    /**
     * Gets whether to show read models in the diagram
     *
     * @return Whether read models should be shown
     */
    public Boolean getShowReadModels() {
        return showReadModels;
    }

    /**
     * Sets whether to show read models in the diagram
     *
     * @param showReadModels Whether read models should be shown
     */
    public void setShowReadModels(Boolean showReadModels) {
        this.showReadModels = showReadModels;
    }

    /**
     * Gets whether to show read model fields
     *
     * @return Whether read model fields should be shown
     */
    public Boolean getShowReadModelFields() {
        return showReadModelFields;
    }

    /**
     * Sets whether to show read model fields
     *
     * @param showReadModelFields Whether read model fields should be shown
     */
    public void setShowReadModelFields(Boolean showReadModelFields) {
        this.showReadModelFields = showReadModelFields;
    }

    /**
     * Gets whether to show read model methods
     *
     * @return Whether read model methods should be shown
     */
    public Boolean getShowReadModelMethods() {
        return showReadModelMethods;
    }

    /**
     * Sets whether to show read model methods
     *
     * @param showReadModelMethods Whether read model methods should be shown
     */
    public void setShowReadModelMethods(Boolean showReadModelMethods) {
        this.showReadModelMethods = showReadModelMethods;
    }

    /**
     * Gets whether to show query handlers in the diagram
     *
     * @return Whether query handlers should be shown
     */
    public Boolean getShowQueryHandlers() {
        return showQueryHandlers;
    }

    /**
     * Sets whether to show query handlers in the diagram
     *
     * @param showQueryHandlers Whether query handlers should be shown
     */
    public void setShowQueryHandlers(Boolean showQueryHandlers) {
        this.showQueryHandlers = showQueryHandlers;
    }

    /**
     * Gets whether to show query handler fields
     *
     * @return Whether query handler fields should be shown
     */
    public Boolean getShowQueryHandlerFields() {
        return showQueryHandlerFields;
    }

    /**
     * Sets whether to show query handler fields
     *
     * @param showQueryHandlerFields Whether query handler fields should be shown
     */
    public void setShowQueryHandlerFields(Boolean showQueryHandlerFields) {
        this.showQueryHandlerFields = showQueryHandlerFields;
    }

    /**
     * Gets whether to show query handler methods
     *
     * @return Whether query handler methods should be shown
     */
    public Boolean getShowQueryHandlerMethods() {
        return showQueryHandlerMethods;
    }

    /**
     * Sets whether to show query handler methods
     *
     * @param showQueryHandlerMethods Whether query handler methods should be shown
     */
    public void setShowQueryHandlerMethods(Boolean showQueryHandlerMethods) {
        this.showQueryHandlerMethods = showQueryHandlerMethods;
    }

    /**
     * Gets whether to show outbound services in the diagram
     *
     * @return Whether outbound services should be shown
     */
    public Boolean getShowOutboundServices() {
        return showOutboundServices;
    }

    /**
     * Sets whether to show outbound services in the diagram
     *
     * @param showOutboundServices Whether outbound services should be shown
     */
    public void setShowOutboundServices(Boolean showOutboundServices) {
        this.showOutboundServices = showOutboundServices;
    }

    /**
     * Gets whether to show outbound service fields
     *
     * @return Whether outbound service fields should be shown
     */
    public Boolean getShowOutboundServiceFields() {
        return showOutboundServiceFields;
    }

    /**
     * Sets whether to show outbound service fields
     *
     * @param showOutboundServiceFields Whether outbound service fields should be shown
     */
    public void setShowOutboundServiceFields(Boolean showOutboundServiceFields) {
        this.showOutboundServiceFields = showOutboundServiceFields;
    }

    /**
     * Gets whether to show outbound service methods
     *
     * @return Whether outbound service methods should be shown
     */
    public Boolean getShowOutboundServiceMethods() {
        return showOutboundServiceMethods;
    }

    /**
     * Sets whether to show outbound service methods
     *
     * @param showOutboundServiceMethods Whether outbound service methods should be shown
     */
    public void setShowOutboundServiceMethods(Boolean showOutboundServiceMethods) {
        this.showOutboundServiceMethods = showOutboundServiceMethods;
    }

    /**
     * Gets whether to show unspecified service kinds in the diagram
     *
     * @return Whether unspecified services should be shown
     */
    public Boolean getShowUnspecifiedServiceKinds() {
        return showUnspecifiedServiceKinds;
    }

    /**
     * Sets whether to show unspecified service kinds in the diagram
     *
     * @param showUnspecifiedServiceKinds Whether unspecified services should be shown
     */
    public void setShowUnspecifiedServiceKinds(Boolean showUnspecifiedServiceKinds) {
        this.showUnspecifiedServiceKinds = showUnspecifiedServiceKinds;
    }

    /**
     * Gets whether to show unspecified service kind fields
     *
     * @return Whether unspecified service fields should be shown
     */
    public Boolean getShowUnspecifiedServiceKindFields() {
        return showUnspecifiedServiceKindFields;
    }

    /**
     * Sets whether to show unspecified service kind fields
     *
     * @param showUnspecifiedServiceKindFields Whether unspecified service fields should be shown
     */
    public void setShowUnspecifiedServiceKindFields(Boolean showUnspecifiedServiceKindFields) {
        this.showUnspecifiedServiceKindFields = showUnspecifiedServiceKindFields;
    }

    /**
     * Gets whether to show unspecified service kind methods
     *
     * @return Whether unspecified service methods should be shown
     */
    public Boolean getShowUnspecifiedServiceKindMethods() {
        return showUnspecifiedServiceKindMethods;
    }

    /**
     * Sets whether to show unspecified service kind methods
     *
     * @param showUnspecifiedServiceKindMethods Whether unspecified service methods should be shown
     */
    public void setShowUnspecifiedServiceKindMethods(Boolean showUnspecifiedServiceKindMethods) {
        this.showUnspecifiedServiceKindMethods = showUnspecifiedServiceKindMethods;
    }

    /**
     * Gets whether to call application service driver
     *
     * @return Whether application service driver should be called
     */
    public Boolean getCallApplicationServiceDriver() {
        return callApplicationServiceDriver;
    }

    /**
     * Sets whether to call application service driver
     *
     * @param callApplicationServiceDriver Whether application service driver should be called
     */
    public void setCallApplicationServiceDriver(Boolean callApplicationServiceDriver) {
        this.callApplicationServiceDriver = callApplicationServiceDriver;
    }

    /**
     * Gets the list of field names to exclude from the diagram
     *
     * @return The blacklisted field names
     */
    public List<String> getFieldBlacklist() {
        return fieldBlacklist;
    }

    /**
     * Sets the list of field names to exclude from the diagram
     *
     * @param fieldBlacklist The field names to blacklist
     */
    public void setFieldBlacklist(List<String> fieldBlacklist) {
        this.fieldBlacklist = fieldBlacklist;
    }

    /**
     * Gets the list of method names to exclude from the diagram
     *
     * @return The blacklisted method names
     */
    public List<String> getMethodBlacklist() {
        return methodBlacklist;
    }

    /**
     * Sets the list of method names to exclude from the diagram
     *
     * @param methodBlacklist The method names to blacklist
     */
    public void setMethodBlacklist(List<String> methodBlacklist) {
        this.methodBlacklist = methodBlacklist;
    }

    /**
     * Gets whether to show inherited members in classes
     *
     * @return Whether inherited members should be shown
     */
    public Boolean getShowInheritedMembersInClasses() {
        return showInheritedMembersInClasses;
    }

    /**
     * Sets whether to show inherited members in classes
     *
     * @param showInheritedMembersInClasses Whether inherited members should be shown
     */
    public void setShowInheritedMembersInClasses(Boolean showInheritedMembersInClasses) {
        this.showInheritedMembersInClasses = showInheritedMembersInClasses;
    }

    /**
     * Gets whether to show Object members in classes
     *
     * @return Whether Object members should be shown
     */
    public Boolean getShowObjectMembersInClasses() {
        return showObjectMembersInClasses;
    }

    /**
     * Sets whether to show Object members in classes
     *
     * @param showObjectMembersInClasses Whether Object members should be shown
     */
    public void setShowObjectMembersInClasses(Boolean showObjectMembersInClasses) {
        this.showObjectMembersInClasses = showObjectMembersInClasses;
    }

    /**
     * Gets whether to show multiplicity in relationship labels
     *
     * @return Whether multiplicity should be shown in labels
     */
    public Boolean getMultiplicityInLabel() {
        return multiplicityInLabel;
    }

    /**
     * Sets whether to show multiplicity in relationship labels
     *
     * @param multiplicityInLabel Whether multiplicity should be shown in labels
     */
    public void setMultiplicityInLabel(Boolean multiplicityInLabel) {
        this.multiplicityInLabel = multiplicityInLabel;
    }

    /**
     * Gets whether to show field stereotypes
     *
     * @return Whether field stereotypes should be shown
     */
    public Boolean getFieldStereotypes() {
        return fieldStereotypes;
    }

    /**
     * Sets whether to show field stereotypes
     *
     * @param fieldStereotypes Whether field stereotypes should be shown
     */
    public void setFieldStereotypes(Boolean fieldStereotypes) {
        this.fieldStereotypes = fieldStereotypes;
    }

    /**
     * Gets the list of classes to include connections for
     *
     * @return The class names to include connections for
     */
    public List<String> getIncludeConnectedTo() {
        return includeConnectedTo;
    }

    /**
     * Sets the list of classes to include connections for
     *
     * @param includeConnectedTo The class names to include connections for
     */
    public void setIncludeConnectedTo(List<String> includeConnectedTo) {
        this.includeConnectedTo = includeConnectedTo;
    }

    /**
     * Gets the list of classes to include ingoing connections for
     *
     * @return The class names to include ingoing connections for
     */
    public List<String> getIncludeConnectedToIngoing() {
        return includeConnectedToIngoing;
    }

    /**
     * Sets the list of classes to include ingoing connections for
     *
     * @param includeConnectedToIngoing The class names to include ingoing connections for
     */
    public void setIncludeConnectedToIngoing(List<String> includeConnectedToIngoing) {
        this.includeConnectedToIngoing = includeConnectedToIngoing;
    }

    /**
     * Gets the list of classes to include outgoing connections for
     *
     * @return The class names to include outgoing connections for
     */
    public List<String> getIncludeConnectedToOutgoing() {
        return includeConnectedToOutgoing;
    }

    /**
     * Sets the list of classes to include outgoing connections for
     *
     * @param includeConnectedToOutgoing The class names to include outgoing connections for
     */
    public void setIncludeConnectedToOutgoing(List<String> includeConnectedToOutgoing) {
        this.includeConnectedToOutgoing = includeConnectedToOutgoing;
    }

    /**
     * Gets the list of classes to exclude ingoing connections for
     *
     * @return The class names to exclude ingoing connections for
     */
    public List<String> getExcludeConnectedToIngoing() {
        return excludeConnectedToIngoing;
    }

    /**
     * Sets the list of classes to exclude ingoing connections for
     *
     * @param excludeConnectedToIngoing The class names to exclude ingoing connections for
     */
    public void setExcludeConnectedToIngoing(List<String> excludeConnectedToIngoing) {
        this.excludeConnectedToIngoing = excludeConnectedToIngoing;
    }

    /**
     * Gets the list of classes to exclude outgoing connections for
     *
     * @return The class names to exclude outgoing connections for
     */
    public List<String> getExcludeConnectedToOutgoing() {
        return excludeConnectedToOutgoing;
    }

    /**
     * Sets the list of classes to exclude outgoing connections for
     *
     * @param excludeConnectedToOutgoing The class names to exclude outgoing connections for
     */
    public void setExcludeConnectedToOutgoing(List<String> excludeConnectedToOutgoing) {
        this.excludeConnectedToOutgoing = excludeConnectedToOutgoing;
    }

    /**
     * Gets the list of package names that should be explicitly included in the diagram
     *
     * @return The package names to explicitly include
     */
    public List<String> getExplicitlyIncludedPackageNames() {
        return explicitlyIncludedPackageNames;
    }

    /**
     * Sets the list of package names that should be explicitly included in the diagram
     *
     * @param explicitlyIncludedPackageNames The package names to explicitly include
     */
    public void setExplicitlyIncludedPackageNames(List<String> explicitlyIncludedPackageNames) {
        this.explicitlyIncludedPackageNames = explicitlyIncludedPackageNames;
    }

    /**
     * Gets whether to show all inheritance structures in the diagram
     *
     * @return Whether all inheritance structures should be shown
     */
    public Boolean getShowAllInheritanceStructures() {
        return showAllInheritanceStructures;
    }

    /**
     * Sets whether to show all inheritance structures in the diagram
     *
     * @param showAllInheritanceStructures Whether all inheritance structures should be shown
     */
    public void setShowAllInheritanceStructures(Boolean showAllInheritanceStructures) {
        this.showAllInheritanceStructures = showAllInheritanceStructures;
    }

    /**
     * Gets whether to show inheritance structures within aggregates
     *
     * @return Whether inheritance structures in aggregates should be shown
     */
    public Boolean getShowInheritanceStructuresInAggregates() {
        return showInheritanceStructuresInAggregates;
    }

    /**
     * Sets whether to show inheritance structures within aggregates
     *
     * @param showInheritanceStructuresInAggregates Whether inheritance structures in aggregates should be shown
     */
    public void setShowInheritanceStructuresInAggregates(Boolean showInheritanceStructuresInAggregates) {
        this.showInheritanceStructuresInAggregates = showInheritanceStructuresInAggregates;
    }

    /**
     * Gets whether to show inheritance structures for service types
     *
     * @return Whether inheritance structures for services should be shown
     */
    public Boolean getShowInheritanceStructuresForServiceKinds() {
        return showInheritanceStructuresForServiceKinds;
    }

    /**
     * Sets whether to show inheritance structures for service types
     *
     * @param showInheritanceStructuresForServiceKinds Whether inheritance structures for services should be shown
     */
    public void setShowInheritanceStructuresForServiceKinds(Boolean showInheritanceStructuresForServiceKinds) {
        this.showInheritanceStructuresForServiceKinds = showInheritanceStructuresForServiceKinds;
    }

    /**
     * Gets whether to show inheritance structures for read models
     *
     * @return Whether inheritance structures for read models should be shown
     */
    public Boolean getShowInheritanceStructuresForReadModels() {
        return showInheritanceStructuresForReadModels;
    }

    /**
     * Sets whether to show inheritance structures for read models
     *
     * @param showInheritanceStructuresForReadModels Whether inheritance structures for read models should be shown
     */
    public void setShowInheritanceStructuresForReadModels(Boolean showInheritanceStructuresForReadModels) {
        this.showInheritanceStructuresForReadModels = showInheritanceStructuresForReadModels;
    }

    /**
     * Gets whether to show inheritance structures for domain events
     *
     * @return Whether inheritance structures for domain events should be shown
     */
    public Boolean getShowInheritanceStructuresForDomainEvents() {
        return showInheritanceStructuresForDomainEvents;
    }

    /**
     * Sets whether to show inheritance structures for domain events
     *
     * @param showInheritanceStructuresForDomainEvents Whether inheritance structures for domain events should be shown
     */
    public void setShowInheritanceStructuresForDomainEvents(Boolean showInheritanceStructuresForDomainEvents) {
        this.showInheritanceStructuresForDomainEvents = showInheritanceStructuresForDomainEvents;
    }

    /**
     * Gets whether to show inheritance structures for domain commands
     *
     * @return Whether inheritance structures for domain commands should be shown
     */
    public Boolean getShowInheritanceStructuresForDomainCommands() {
        return showInheritanceStructuresForDomainCommands;
    }

    /**
     * Sets whether to show inheritance structures for domain commands
     *
     * @param showInheritanceStructuresForDomainCommands Whether inheritance structures for domain commands should be shown
     */
    public void setShowInheritanceStructuresForDomainCommands(Boolean showInheritanceStructuresForDomainCommands) {
        this.showInheritanceStructuresForDomainCommands = showInheritanceStructuresForDomainCommands;
    }

    /**
     * Maps various configuration styles, filters, and properties into a {@link DomainDiagramConfig} object
     * by utilizing a builder pattern. This method processes multiple optional style configurations,
     * filtering options, and display preferences to generate a comprehensive domain diagram configuration.
     *
     * @return a {@link DomainDiagramConfig} instance containing the mapped configuration based on
     *         the provided styles and settings.
     */
    public DomainDiagramConfig map() {
        DomainDiagramConfigBuilder configBuilder = DomainDiagramConfig.builder();
        var styleBuilder = StyleSettings.builder();
        var visualBuilder = GeneralVisualSettings.builder();
        var layoutBuilder = LayoutSettings.builder();
        var trimBuilder = DiagramTrimSettings.builder();

        if(aggregateRootStyle != null) styleBuilder.withAggregateRootStyle(aggregateRootStyle);
        if(aggregateFrameStyle != null) styleBuilder.withAggregateFrameStyle(aggregateFrameStyle);
        if(entityStyle != null) styleBuilder.withEntityStyle(entityStyle);
        if(valueObjectStyle != null) styleBuilder.withValueObjectStyle(valueObjectStyle);
        if(enumStyle != null) styleBuilder.withEnumStyle(enumStyle);
        if(identityStyle != null) styleBuilder.withIdentityStyle(identityStyle);
        if(domainEventStyle != null) styleBuilder.withDomainEventStyle(domainEventStyle);
        if(domainCommandStyle != null) styleBuilder.withDomainCommandStyle(domainCommandStyle);
        if(applicationServiceStyle != null) styleBuilder.withApplicationServiceStyle(applicationServiceStyle);
        if(domainServiceStyle != null) styleBuilder.withDomainServiceStyle(domainServiceStyle);
        if(repositoryStyle != null) styleBuilder.withRepositoryStyle(repositoryStyle);
        if(readModelStyle != null) styleBuilder.withReadModelStyle(readModelStyle);
        if(queryHandlerStyle != null) styleBuilder.withQueryHandlerStyle(queryHandlerStyle);
        if(outboundServiceStyle != null) styleBuilder.withOutboundServiceStyle(outboundServiceStyle);
        if(font != null) styleBuilder.withFont(font);
        if(direction != null) layoutBuilder.withDirection(direction);
        if(ranker != null) layoutBuilder.withRanker(ranker);
        if(acycler != null) layoutBuilder.withAcycler(acycler);
        if(backgroundColor != null) styleBuilder.withBackgroundColor(backgroundColor);
        if(classesBlacklist != null && !classesBlacklist.isEmpty()) trimBuilder.withClassesBlacklist(classesBlacklist);
        if(showFields != null) visualBuilder.withShowFields(showFields);
        if(showFullQualifiedClassNames != null) visualBuilder.withShowFullQualifiedClassNames(showFullQualifiedClassNames);
        if(showAssertions != null) visualBuilder.withShowAssertions(showAssertions);
        if(showMethods != null) visualBuilder.withShowMethods(showMethods);
        if(showOnlyPublicMethods != null) visualBuilder.withShowOnlyPublicMethods(showOnlyPublicMethods);
        if(showAggregates != null) visualBuilder.withShowAggregates(showAggregates);
        if(showAggregateFields != null) visualBuilder.withShowAggregateFields(showAggregateFields);
        if(showAggregateMethods != null) visualBuilder.withShowAggregateMethods(showAggregateMethods);
        if(showDomainEvents != null) visualBuilder.withShowDomainEvents(showDomainEvents);
        if(showDomainEventFields != null) visualBuilder.withShowDomainEventFields(showDomainEventFields);
        if(showDomainEventMethods != null) visualBuilder.withShowDomainEventMethods(showDomainEventMethods);
        if(showDomainCommands != null) visualBuilder.withShowDomainCommands(showDomainCommands);
        if(showOnlyTopLevelDomainCommandRelations != null) visualBuilder.withShowOnlyTopLevelDomainCommandRelations(showOnlyTopLevelDomainCommandRelations);
        if(showDomainCommandFields != null) visualBuilder.withShowDomainCommandFields(showDomainCommandFields);
        if(showDomainCommandMethods != null) visualBuilder.withShowDomainCommandMethods(showDomainCommandMethods);
        if(showDomainServices != null) visualBuilder.withShowDomainServices(showDomainServices);
        if(showDomainServiceFields != null) visualBuilder.withShowDomainServiceFields(showDomainServiceFields);
        if(showDomainServiceMethods != null) visualBuilder.withShowDomainServiceMethods(showDomainServiceMethods);
        if(showApplicationServices != null) visualBuilder.withShowApplicationServices(showApplicationServices);
        if(showApplicationServiceFields != null) visualBuilder.withShowApplicationServiceFields(showApplicationServiceFields);
        if(showApplicationServiceMethods != null) visualBuilder.withShowApplicationServiceMethods(showApplicationServiceMethods);
        if(showRepositories != null) visualBuilder.withShowRepositories(showRepositories);
        if(showRepositoryFields != null) visualBuilder.withShowRepositoryFields(showRepositoryFields);
        if(showRepositoryMethods != null) visualBuilder.withShowRepositoryMethods(showRepositoryMethods);
        if(showReadModels != null) visualBuilder.withShowReadModels(showReadModels);
        if(showReadModelFields != null) visualBuilder.withShowReadModelFields(showReadModelFields);
        if(showReadModelMethods != null) visualBuilder.withShowReadModelMethods(showReadModelMethods);
        if(showQueryHandlers != null) visualBuilder.withShowQueryHandlers(showQueryHandlers);
        if(showQueryHandlerFields != null) visualBuilder.withShowQueryHandlerFields(showQueryHandlerFields);
        if(showQueryHandlerMethods != null) visualBuilder.withShowQueryHandlerMethods(showQueryHandlerMethods);
        if(showOutboundServices != null) visualBuilder.withShowOutboundServices(showOutboundServices);
        if(showOutboundServiceFields != null) visualBuilder.withShowOutboundServiceFields(showOutboundServiceFields);
        if(showOutboundServiceMethods != null) visualBuilder.withShowOutboundServiceMethods(showOutboundServiceMethods);
        if(showUnspecifiedServiceKinds != null) visualBuilder.withShowUnspecifiedServiceKinds(showUnspecifiedServiceKinds);
        if(showUnspecifiedServiceKindFields != null) visualBuilder.withShowUnspecifiedServiceKindFields(showUnspecifiedServiceKindFields);
        if(showUnspecifiedServiceKindMethods != null) visualBuilder.withShowUnspecifiedServiceKindMethods(showUnspecifiedServiceKindMethods);
        if(callApplicationServiceDriver != null) visualBuilder.withCallApplicationServiceDriver(callApplicationServiceDriver);
        if(fieldBlacklist != null && !fieldBlacklist.isEmpty()) visualBuilder.withFieldBlacklist(fieldBlacklist);
        if(methodBlacklist != null && !methodBlacklist.isEmpty()) visualBuilder.withMethodBlacklist(classesBlacklist);
        if(showInheritedMembersInClasses != null) visualBuilder.withShowInheritedMembersInClasses(showInheritedMembersInClasses);
        if(showObjectMembersInClasses != null) visualBuilder.withShowObjectMembersInClasses(showObjectMembersInClasses);
        if(multiplicityInLabel != null) visualBuilder.withMultiplicityInLabel(multiplicityInLabel);
        if(fieldStereotypes != null) visualBuilder.withFieldStereotypes(fieldStereotypes);
        if(includeConnectedTo != null && !includeConnectedTo.isEmpty()) trimBuilder.withIncludeConnectedTo(includeConnectedTo);
        if(includeConnectedToIngoing != null && !includeConnectedToIngoing.isEmpty()) trimBuilder.withIncludeConnectedToIngoing(includeConnectedToIngoing);
        if(includeConnectedToOutgoing != null && !includeConnectedToOutgoing.isEmpty()) trimBuilder.withIncludeConnectedToOutgoing(includeConnectedToOutgoing);
        if(excludeConnectedToIngoing != null && !excludeConnectedToIngoing.isEmpty()) trimBuilder.withExcludeConnectedToIngoing(excludeConnectedToIngoing);
        if(excludeConnectedToOutgoing != null && !excludeConnectedToOutgoing.isEmpty()) trimBuilder.withExcludeConnectedToOutgoing(excludeConnectedToOutgoing);
        if(explicitlyIncludedPackageNames != null && !explicitlyIncludedPackageNames.isEmpty()) trimBuilder.withExplicitlyIncludedPackageNames(explicitlyIncludedPackageNames);
        if(showAllInheritanceStructures != null) visualBuilder.withShowAllInheritanceStructures(showAllInheritanceStructures);
        if(showInheritanceStructuresInAggregates != null) visualBuilder.withShowInheritanceStructuresInAggregates(showInheritanceStructuresInAggregates);
        if(showInheritanceStructuresForDomainCommands != null) visualBuilder.withShowInheritanceStructuresForDomainCommands(showInheritanceStructuresForDomainCommands);
        if(showInheritanceStructuresForDomainEvents != null) visualBuilder.withShowInheritanceStructuresForDomainEvents(showInheritanceStructuresForDomainEvents);
        if(showInheritanceStructuresForReadModels != null) visualBuilder.withShowInheritanceStructuresForReadModels(showInheritanceStructuresForReadModels);
        if(showInheritanceStructuresForServiceKinds != null) visualBuilder.withShowInheritanceStructuresForServiceKinds(showInheritanceStructuresForServiceKinds);
        configBuilder
            .withDiagramTrimSettings(trimBuilder.build())
            .withLayoutSettings(layoutBuilder.build())
            .withStyleSettings(styleBuilder.build())
            .withGeneralVisualSettings(visualBuilder.build());
        return configBuilder.build();
    }
}
