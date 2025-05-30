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

package io.domainlifecycles.plugin.diagram;

import java.util.List;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * This class represents the configuration for a plugin diagram generation process.
 * It includes various properties to control the output format, appearance, content,
 * and filtering of elements in the generated diagram.
 *
 * The configuration supports detailed customization for styles, visibility of different
 * components, and filtering options. This allows fine-grained control over what is 
 * included in the diagram and how it is visually represented.
 *
 * Mandatory parameters:
 * - `format`: Specifies the output format of the diagram.
 * - `fileName`: Defines the name of the output file.
 * - `contextPackages`: Lists the packages to consider for diagram generation.
 *
 * Optional parameters include:
 * - Visual styles for aggregates, entities, value objects, enums, services, and other components.
 * - Flags and properties for showing/hiding specific diagram elements, such as fields, methods, domain events, commands, and services.
 * - Blacklists for classes, fields, and methods to exclude them from the diagram.
 * - Fonts, diagram orientation (`direction`), layout ranker, acycling strategies, etc.
 * - Background color and additional visual adjustments.
 * - Filters for transitive relations and domain-specific types.
 *
 * Getters are provided for all configuration fields to allow easy access to their values at runtime.
 *
 * @author Mario Herb
 * @author Leon Völlinger
 */
public class PluginDiagramConfiguration {

     @Parameter(property = "format", required = true)
     private String format;

     @Parameter(property = "fileName", required = true)
     private String fileName;

     @Parameter(property = "domainModelPackages", required = true)
     private List<String> domainModelPackages;

     @Parameter(property = "explicitlyIncludedPackages", required = true)
     private List<String> explicitlyIncludedPackages;

     @Parameter(property = "aggregateRootStyle", required = false)
     private String aggregateRootStyle;

     @Parameter(property = "aggregateFrameStyle", required = false)
     private String aggregateFrameStyle;

     @Parameter(property = "entityStyle", required = false)
     private String entityStyle;

     @Parameter(property = "valueObjectStyle", required = false)
     private String valueObjectStyle;

     @Parameter(property = "enumStyle", required = false)
     private String enumStyle;

     @Parameter(property = "identityStyle", required = false)
     private String identityStyle;

     @Parameter(property = "domainEventStyle", required = false)
     private String domainEventStyle;

     @Parameter(property = "domainCommandStyle", required = false)
     private String domainCommandStyle;

     @Parameter(property = "applicationServiceStyle", required = false)
     private String applicationServiceStyle;

     @Parameter(property = "domainServiceStyle", required = false)
     private String domainServiceStyle;

     @Parameter(property = "repositoryStyle", required = false)
     private String repositoryStyle;

     @Parameter(property = "readModelStyle", required = false)
     private String readModelStyle;

     @Parameter(property = "queryHandlerStyle", required = false)
     private String queryHandlerStyle;

     @Parameter(property = "outboundServiceStyle", required = false)
     private String outboundServiceStyle;

     @Parameter(property = "font", required = false)
     private String font;

     @Parameter(property = "direction", required = false)
     private String direction;

     @Parameter(property = "ranker", required = false)
     private String ranker;

     @Parameter(property = "acycler", required = false)
     private String acycler;

     @Parameter(property = "backgroundColor", required = false)
     private String backgroundColor;

     @Parameter(property = "classesBlacklist", required = false)
     private List<String> classesBlacklist;

     @Parameter(property = "showFields", required = false)
     private Boolean showFields;

     @Parameter(property = "showFullQualifiedClassNames", required = false)
     private Boolean showFullQualifiedClassNames;

     @Parameter(property = "showAssertions", required = false)
     private Boolean showAssertions;

     @Parameter(property = "showMethods", required = false)
     private Boolean showMethods;

     @Parameter(property = "showOnlyPublicMethods", required = false)
     private Boolean showOnlyPublicMethods;

    @Parameter(property = "showAggregates", required = false)
    private Boolean showAggregates;

    @Parameter(property = "showAggregateFields", required = false)
    private Boolean showAggregateFields;

    @Parameter(property = "showAggregateMethods", required = false)
    private Boolean showAggregateMethods;

     @Parameter(property = "showDomainEvents", required = false)
     private Boolean showDomainEvents;

     @Parameter(property = "showDomainEventFields", required = false)
     private Boolean showDomainEventFields;

     @Parameter(property = "showDomainEventMethods", required = false)
     private Boolean showDomainEventMethods;

     @Parameter(property = "showDomainCommands", required = false)
     private Boolean showDomainCommands;

     @Parameter(property = "showOnlyTopLevelDomainCommandRelations", required = false)
     private Boolean showOnlyTopLevelDomainCommandRelations;

     @Parameter(property = "showDomainCommandFields", required = false)
     private Boolean showDomainCommandFields;

     @Parameter(property = "showDomainCommandMethods", required = false)
     private Boolean showDomainCommandMethods;

     @Parameter(property = "showDomainServices", required = false)
     private Boolean showDomainServices;

     @Parameter(property = "showDomainServiceFields", required = false)
     private Boolean showDomainServiceFields;

     @Parameter(property = "showDomainServiceMethods", required = false)
     private Boolean showDomainServiceMethods;

     @Parameter(property = "showApplicationServices", required = false)
     private Boolean showApplicationServices;

     @Parameter(property = "showApplicationServiceFields", required = false)
     private Boolean showApplicationServiceFields;

     @Parameter(property = "showApplicationServiceMethods", required = false)
     private Boolean showApplicationServiceMethods;

     @Parameter(property = "showRepositories", required = false)
     private Boolean showRepositories;

     @Parameter(property = "showRepositoryFields", required = false)
     private Boolean showRepositoryFields;

     @Parameter(property = "showRepositoryMethods", required = false)
     private Boolean showRepositoryMethods;

     @Parameter(property = "showReadModels", required = false)
     private Boolean showReadModels;

     @Parameter(property = "showReadModelFields", required = false)
     private Boolean showReadModelFields;

     @Parameter(property = "showReadModelMethods", required = false)
     private Boolean showReadModelMethods;

     @Parameter(property = "showQueryHandlers", required = false)
     private Boolean showQueryHandlers;

     @Parameter(property = "showQueryHandlerFields", required = false)
     private Boolean showQueryHandlerFields;

     @Parameter(property = "showQueryHandlerMethods", required = false)
     private Boolean showQueryHandlerMethods;

     @Parameter(property = "showOutboundServices", required = false)
     private Boolean showOutboundServices;

     @Parameter(property = "showOutboundServiceFields", required = false)
     private Boolean showOutboundServiceFields;

     @Parameter(property = "showOutboundServiceMethods", required = false)
     private Boolean showOutboundServiceMethods;

     @Parameter(property = "showUnspecifiedServiceKinds", required = false)
     private Boolean showUnspecifiedServiceKinds;

     @Parameter(property = "showUnspecifiedServiceKindFields", required = false)
     private Boolean showUnspecifiedServiceKindFields;

     @Parameter(property = "showUnspecifiedServiceKindMethods", required = false)
     private Boolean showUnspecifiedServiceKindMethods;

     @Parameter(property = "callApplicationServiceDriver", required = false)
     private Boolean callApplicationServiceDriver;

     @Parameter(property = "fieldBlacklist", required = false)
     private List<String> fieldBlacklist;

     @Parameter(property = "methodBlacklist", required = false)
     private List<String> methodBlacklist;

     @Parameter(property = "showInheritedMembersInClasses", required = false)
     private Boolean showInheritedMembersInClasses;

     @Parameter(property = "showObjectMembersInClasses", required = false)
     private Boolean showObjectMembersInClasses;

     @Parameter(property = "multiplicityInLabel", required = false)
     private Boolean multiplicityInLabel;

     @Parameter(property = "fieldStereotypes", required = false)
     private Boolean fieldStereotypes;

    @Parameter(property = "includeConnectedTo", required = false)
    private List<String> includeConnectedTo;
    
    @Parameter(property = "includeConnectedToIngoing", required = false)
    private List<String> includeConnectedToIngoing;
    
    @Parameter(property = "includeConnectedToOutgoing", required = false)
    private List<String> includeConnectedToOutgoing;
    
    @Parameter(property = "excludeConnectedToIngoing", required = false)
    private List<String> excludeConnectedToIngoing;
    
    @Parameter(property = "excludeConnectedToOutgoing", required = false)
    private List<String> excludeConnectedToOutgoing;
    
    @Parameter(property = "showAllInheritanceStructures", required = false)
    private Boolean showAllInheritanceStructures;

    @Parameter(property = "showInheritanceStructuresInAggregates", required = false)
    private Boolean showInheritanceStructuresInAggregates;

    @Parameter(property = "showInheritanceStructuresForServiceKinds", required = false)
    private Boolean showInheritanceStructuresForServiceKinds;

    @Parameter(property = "showInheritanceStructuresForReadModels", required = false)
    private Boolean showInheritanceStructuresForReadModels;

    @Parameter(property = "showInheritanceStructuresForDomainEvents", required = false)
    private Boolean showInheritanceStructuresForDomainEvents;

    @Parameter(property = "showInheritanceStructuresForDomainCommands", required = false)
    private Boolean showInheritanceStructuresForDomainCommands;
    

    /**
     * Gets the output format of the diagram.
     *
     * @return the format of the diagram.
     */
    public String getFormat() {
        return format;
    }

    /**
     * Gets the name of the output file.
     *
     * @return the file name.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Retrieves the list of domain model packages.
     *
     * @return a list of strings containing the names of domain model packages
     */
    public List<String> getDomainModelPackages() {
        return domainModelPackages;
    }

    /**
     * Retrieves a list of filtered packages.
     *
     * @return a list of package names that have been filtered
     */
    public List<String> getExplicitlyIncludedPackages() {
        return explicitlyIncludedPackages;
    }

    /**
     * Gets the style of aggregate roots.
     *
     * @return the aggregate root style.
     */
    public String getAggregateRootStyle() {
        return aggregateRootStyle;
    }

    /**
     * Gets the style of aggregate frames.
     *
     * @return the aggregate frame style.
     */
    public String getAggregateFrameStyle() {
        return aggregateFrameStyle;
    }

    /**
     * Gets the style of entities.
     *
     * @return the entity style.
     */
    public String getEntityStyle() {
        return entityStyle;
    }

    /**
     * Gets the style of value objects.
     *
     * @return the value object style.
     */
    public String getValueObjectStyle() {
        return valueObjectStyle;
    }

    /**
     * Gets the style of enums.
     *
     * @return the enum style.
     */
    public String getEnumStyle() {
        return enumStyle;
    }

    /**
     * Gets the style for identities.
     *
     * @return the identity style.
     */
    public String getIdentityStyle() {
        return identityStyle;
    }

    /**
     * Gets the style for domain events.
     *
     * @return the domain event style.
     */
    public String getDomainEventStyle() {
        return domainEventStyle;
    }

    /**
     * Gets the style for domain commands.
     *
     * @return the domain command style.
     */
    public String getDomainCommandStyle() {
        return domainCommandStyle;
    }

    /**
     * Gets the style for application services.
     *
     * @return the application service style.
     */
    public String getApplicationServiceStyle() {
        return applicationServiceStyle;
    }

    /**
     * Gets the style for domain services.
     *
     * @return the domain service style.
     */
    public String getDomainServiceStyle() {
        return domainServiceStyle;
    }

    /**
     * Gets the style for repositories.
     *
     * @return the repository style.
     */
    public String getRepositoryStyle() {
        return repositoryStyle;
    }

    /**
     * Gets the style for read models.
     *
     * @return the read model style.
     */
    public String getReadModelStyle() {
        return readModelStyle;
    }

    /**
     * Gets the style for query handlers.
     *
     * @return the query handler style.
     */
    public String getQueryHandlerStyle() {
        return queryHandlerStyle;
    }

    /**
     * Gets the style for outbound services.
     *
     * @return the outbound service style.
     */
    public String getOutboundServiceStyle() {
        return outboundServiceStyle;
    }

    /**
     * Gets the font used in the diagram.
     *
     * @return the font.
     */
    public String getFont() {
        return font;
    }

    /**
     * Gets the direction of the diagram (e.g., top-to-bottom, left-to-right).
     *
     * @return the direction.
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Gets the ranker used for layout in the diagram.
     *
     * @return the ranker.
     */
    public String getRanker() {
        return ranker;
    }

    /**
     * Gets the acycling strategy used to handle cycles in the diagram.
     *
     * @return the acycler strategy.
     */
    public String getAcycler() {
        return acycler;
    }

    /**
     * Gets the background color of the diagram.
     *
     * @return the background color.
     */
    public String getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Gets the classes blacklisted from the diagram.
     *
     * @return the list of blacklisted classes.
     */
    public List<String> getClassesBlacklist() {
        return classesBlacklist;
    }

    /**
     * Checks if fields should be shown in the diagram.
     *
     * @return true if fields are shown, false otherwise.
     */
    public Boolean getShowFields() {
        return showFields;
    }

    /**
     * Checks if fully qualified class names should be shown.
     *
     * @return true if class names are fully qualified, false otherwise.
     */
    public Boolean getShowFullQualifiedClassNames() {
        return showFullQualifiedClassNames;
    }

    /**
     * Checks if assertions should be shown in the diagram.
     *
     * @return true if assertions are shown, false otherwise.
     */
    public Boolean getShowAssertions() {
        return showAssertions;
    }

    /**
     * Checks if methods should be shown in the diagram.
     *
     * @return true if methods are shown, false otherwise.
     */
    public Boolean getShowMethods() {
        return showMethods;
    }

    /**
     * Checks if only public methods should be shown in the diagram.
     *
     * @return true if only public methods are shown, false otherwise.
     */
    public Boolean getShowOnlyPublicMethods() {
        return showOnlyPublicMethods;
    }

    /**
     * Retrieves the current state of whether aggregates should be displayed.
     *
     * @return a Boolean value indicating if aggregates are displayed (true) or not (false).
     */
    public Boolean getShowAggregates() {
        return showAggregates;
    }

    /**
     * Retrieves the current state of the showAggregateFields property.
     *
     * @return a Boolean value indicating whether aggregate fields should be shown.
     */
    public Boolean getShowAggregateFields() {
        return showAggregateFields;
    }

    /**
     * Retrieves the status indicating whether aggregate methods should be displayed.
     *
     * @return a Boolean value; true if aggregate methods should be shown, false otherwise.
     */
    public Boolean getShowAggregateMethods() {
        return showAggregateMethods;
    }

    /**
     * Checks if domain events should be shown in the diagram.
     *
     * @return true if domain events are shown, false otherwise.
     */
    public Boolean getShowDomainEvents() {
        return showDomainEvents;
    }

    /**
     * Checks if domain event fields should be shown in the diagram.
     *
     * @return true if domain event fields are shown, false otherwise.
     */
    public Boolean getShowDomainEventFields() {
        return showDomainEventFields;
    }

    /**
     * Checks if domain event methods should be shown in the diagram.
     *
     * @return true if domain event methods are shown, false otherwise.
     */
    public Boolean getShowDomainEventMethods() {
        return showDomainEventMethods;
    }

    /**
     * Checks if domain commands should be shown in the diagram.
     *
     * @return true if domain commands are shown, false otherwise.
     */
    public Boolean getShowDomainCommands() {
        return showDomainCommands;
    }

    /**
     * Checks if only top-level domain command relations should be shown.
     *
     * @return true if only top-level domain command relations are shown, false otherwise.
     */
    public Boolean getShowOnlyTopLevelDomainCommandRelations() {
        return showOnlyTopLevelDomainCommandRelations;
    }

    /**
     * Checks if domain command fields should be shown in the diagram.
     *
     * @return true if domain command fields are shown, false otherwise.
     */
    public Boolean getShowDomainCommandFields() {
        return showDomainCommandFields;
    }

    /**
     * Checks if domain command methods should be shown in the diagram.
     *
     * @return true if domain command methods are shown, false otherwise.
     */
    public Boolean getShowDomainCommandMethods() {
        return showDomainCommandMethods;
    }

    /**
     * Checks if domain services should be shown in the diagram.
     *
     * @return true if domain services are shown, false otherwise.
     */
    public Boolean getShowDomainServices() {
        return showDomainServices;
    }

    /**
     * Checks if domain service fields should be shown in the diagram.
     *
     * @return true if domain service fields are shown, false otherwise.
     */
    public Boolean getShowDomainServiceFields() {
        return showDomainServiceFields;
    }

    /**
     * Checks if domain service methods should be shown in the diagram.
     *
     * @return true if domain service methods are shown, false otherwise.
     */
    public Boolean getShowDomainServiceMethods() {
        return showDomainServiceMethods;
    }

    /**
     * Checks if application services should be shown in the diagram.
     *
     * @return true if application services are shown, false otherwise.
     */
    public Boolean getShowApplicationServices() {
        return showApplicationServices;
    }

    /**
     * Checks if application service fields should be shown in the diagram.
     *
     * @return true if application service fields are shown, false otherwise.
     */
    public Boolean getShowApplicationServiceFields() {
        return showApplicationServiceFields;
    }

    /**
     * Checks if application service methods should be shown in the diagram.
     *
     * @return true if application service methods are shown, false otherwise.
     */
    public Boolean getShowApplicationServiceMethods() {
        return showApplicationServiceMethods;
    }

    /**
     * Checks if repositories should be shown in the diagram.
     *
     * @return true if repositories are shown, false otherwise.
     */
    public Boolean getShowRepositories() {
        return showRepositories;
    }

    /**
     * Checks if repository fields should be shown in the diagram.
     *
     * @return true if repository fields are shown, false otherwise.
     */
    public Boolean getShowRepositoryFields() {
        return showRepositoryFields;
    }

    /**
     * Checks if repository methods should be shown in the diagram.
     *
     * @return true if repository methods are shown, false otherwise.
     */
    public Boolean getShowRepositoryMethods() {
        return showRepositoryMethods;
    }

    /**
     * Checks if read models should be shown in the diagram.
     *
     * @return true if read models are shown, false otherwise.
     */
    public Boolean getShowReadModels() {
        return showReadModels;
    }

    /**
     * Checks if read model fields should be shown in the diagram.
     *
     * @return true if read model fields are shown, false otherwise.
     */
    public Boolean getShowReadModelFields() {
        return showReadModelFields;
    }

    /**
     * Checks if read model methods should be shown in the diagram.
     *
     * @return true if read model methods are shown, false otherwise.
     */
    public Boolean getShowReadModelMethods() {
        return showReadModelMethods;
    }

    /**
     * Checks if query handlers should be shown in the diagram.
     *
     * @return true if query handlers are shown, false otherwise.
     */
    public Boolean getShowQueryHandlers() {
        return showQueryHandlers;
    }

    /**
     * Checks if query handler fields should be shown in the diagram.
     *
     * @return true if query handler fields are shown, false otherwise.
     */
    public Boolean getShowQueryHandlerFields() {
        return showQueryHandlerFields;
    }

    /**
     * Checks if query handler methods should be shown in the diagram.
     *
     * @return true if query handler methods are shown, false otherwise.
     */
    public Boolean getShowQueryHandlerMethods() {
        return showQueryHandlerMethods;
    }

    /**
     * Retrieves the current state of whether outbound services are shown or not.
     *
     * @return a Boolean value indicating whether outbound services are displayed.
     *         Returns true if outbound services are shown, false otherwise.
     */
    public Boolean getShowOutboundServices() {
        return showOutboundServices;
    }

    /**
     * Checks if outbound service fields should be shown in the diagram.
     *
     * @return true if outbound service fields are shown, false otherwise.
     */
    public Boolean getShowOutboundServiceFields() {
        return showOutboundServiceFields;
    }

    /**
     * Checks if outbound service methods should be shown in the diagram.
     *
     * @return true if outbound service methods are shown, false otherwise.
     */
    public Boolean getShowOutboundServiceMethods() {
        return showOutboundServiceMethods;
    }

    /**
     * Checks if unspecified service kinds should be shown in the diagram.
     *
     * @return true if unspecified service kinds are shown, false otherwise.
     */
    public Boolean getShowUnspecifiedServiceKinds() {
        return showUnspecifiedServiceKinds;
    }

    /**
     * Checks if unspecified service kind fields should be shown in the diagram.
     *
     * @return true if unspecified service kind fields are shown, false otherwise.
     */
    public Boolean getShowUnspecifiedServiceKindFields() {
        return showUnspecifiedServiceKindFields;
    }

    /**
     * Checks if unspecified service kind methods should be shown in the diagram.
     *
     * @return true if unspecified service kind methods are shown, false otherwise.
     */
    public Boolean getShowUnspecifiedServiceKindMethods() {
        return showUnspecifiedServiceKindMethods;
    }

    /**
     * Checks if the application service driver should be called.
     *
     * @return true if the application service driver is called, false otherwise.
     */
    public Boolean getCallApplicationServiceDriver() {
        return callApplicationServiceDriver;
    }

    /**
     * Gets the list of fields that are blacklisted from the diagram.
     *
     * @return the list of blacklisted fields.
     */
    public List<String> getFieldBlacklist() {
        return fieldBlacklist;
    }

    /**
     * Gets the list of methods that are blacklisted from the diagram.
     *
     * @return the list of blacklisted methods.
     */
    public List<String> getMethodBlacklist() {
        return methodBlacklist;
    }

    /**
     * Checks if inherited members in classes should be shown in the diagram.
     *
     * @return true if inherited members in classes are shown, false otherwise.
     */
    public Boolean getShowInheritedMembersInClasses() {
        return showInheritedMembersInClasses;
    }

    /**
     * Checks if object members in classes should be shown in the diagram.
     *
     * @return true if object members in classes are shown, false otherwise.
     */
    public Boolean getShowObjectMembersInClasses() {
        return showObjectMembersInClasses;
    }

    /**
     * Checks if multiplicity should be included in the label in the diagram.
     *
     * @return true if multiplicity is included in the label, false otherwise.
     */
    public Boolean getMultiplicityInLabel() {
        return multiplicityInLabel;
    }

    /**
     * Checks if field stereotypes should be shown in the diagram.
     *
     * @return true if field stereotypes are shown, false otherwise.
     */
    public Boolean getFieldStereotypes() {
        return fieldStereotypes;
    }


    /**
     * Gets the list of class names to include in the diagram based on their connectivity to other classes.
     * This filter includes classes that are connected in any direction (ingoing or outgoing).
     *
     * @return a list of fully qualified class names to include based on connectivity
     */
    public List<String> getIncludeConnectedTo() {
        return includeConnectedTo;
    }

    /**
     * Gets the list of class names to include in the diagram based on ingoing connections.
     * This filter includes classes that have ingoing connections to the specified classes.
     *
     * @return a list of fully qualified class names to include based on ingoing connections
     */
    public List<String> getIncludeConnectedToIngoing() {
        return includeConnectedToIngoing;
    }

    /**
     * Gets the list of class names to include in the diagram based on outgoing connections.
     * This filter includes classes that have outgoing connections from the specified classes.
     *
     * @return a list of fully qualified class names to include based on outgoing connections
     */
    public List<String> getIncludeConnectedToOutgoing() {
        return includeConnectedToOutgoing;
    }

    /**
     * Gets the list of class names to exclude from the diagram based on ingoing connections.
     * This filter excludes classes that have ingoing connections to the specified classes.
     *
     * @return a list of fully qualified class names to exclude based on ingoing connections
     */
    public List<String> getExcludeConnectedToIngoing() {
        return excludeConnectedToIngoing;
    }

    /**
     * Gets the list of class names to exclude from the diagram based on outgoing connections.
     * This filter excludes classes that have outgoing connections from the specified classes.
     *
     * @return a list of fully qualified class names to exclude based on outgoing connections
     */
    public List<String> getExcludeConnectedToOutgoing() {
        return excludeConnectedToOutgoing;
    }

    /**
     * Gets whether all inheritance structures should be shown in the diagram.
     * This setting controls the display of all inheritance relationships regardless of type.
     *
     * @return true if all inheritance structures should be shown, false otherwise
     */
    public Boolean getShowAllInheritanceStructures() {
        return showAllInheritanceStructures;
    }

    /**
     * Gets whether inheritance structures within aggregates should be shown in the diagram.
     * Controls visibility of inheritance relationships between aggregate components.
     *
     * @return true if inheritance structures in aggregates should be shown, false otherwise
     */
    public Boolean getShowInheritanceStructuresInAggregates() {
        return showInheritanceStructuresInAggregates;
    }

    /**
     * Gets whether inheritance structures for service kinds should be shown in the diagram.
     * Controls visibility of inheritance relationships between different service types.
     *
     * @return true if inheritance structures for service kinds should be shown, false otherwise
     */
    public Boolean getShowInheritanceStructuresForServiceKinds() {
        return showInheritanceStructuresForServiceKinds;
    }

    /**
     * Gets whether inheritance structures for read models should be shown in the diagram.
     * Controls visibility of inheritance relationships between read model classes.
     *
     * @return true if inheritance structures for read models should be shown, false otherwise
     */
    public Boolean getShowInheritanceStructuresForReadModels() {
        return showInheritanceStructuresForReadModels;
    }

    /**
     * Gets whether inheritance structures for domain events should be shown in the diagram.
     * Controls visibility of inheritance relationships between domain event classes.
     *
     * @return true if inheritance structures for domain events should be shown, false otherwise
     */
    public Boolean getShowInheritanceStructuresForDomainEvents() {
        return showInheritanceStructuresForDomainEvents;
    }

    /**
     * Gets whether inheritance structures for domain commands should be shown in the diagram.
     * Controls visibility of inheritance relationships between domain command classes.
     *
     * @return true if inheritance structures for domain commands should be shown, false otherwise
     */
    public Boolean getShowInheritanceStructuresForDomainCommands() {
        return showInheritanceStructuresForDomainCommands;
    }
}
