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

package io.domainlifecycles.plugin.extensions;

import org.gradle.api.Named;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;

/**
 * Represents an abstract extension for configuring plugin-based diagram generation.
 *
 * This class provides a collection of abstract methods to define various configuration
 * properties related to diagram generation. These properties include format, file name,
 * context packages, styling options for domain entities and services, visual settings, 
 * and inclusion/exclusion options for class members or certain types of objects. 
 *
 * Implementations can use this extension to dictate how diagrams should be visualized 
 * and what elements of the domain model should be included in the diagrams.
 *
 * The configuration properties primarily rely on Gradle's {@code Property} and 
 * {@code ListProperty} abstractions to enable deferred configuration and improved 
 * flexibility during build time.
 * 
 * @author Leon Völlinger
 */
public abstract class PluginDiagramConfigurationExtension implements Named {

    /**
     * Retrieves the format property of an object.
     *
     * @return a Property object containing the format as a String.
     */
    public abstract Property<String> getFormat();

    /**
     * Gets the file name for the generated diagram.
     *
     * @return the file name for the generated diagram
     */
    public abstract Property<String> getFileName();

    /**
     * Retrieves the file extension represented as a string.
     *
     * @return a {@code Property<String>} representing the file extension
     */
    public abstract Property<String> getFileExtension();

    /**
     * Retrieves a list property representing the filtered packages.
     *
     * @return a ListProperty containing the filtered package names as strings.
     */
    public abstract ListProperty<String> getFilteredPackages();

    /**
     * Retrieves the property indicating whether abstract types should be shown.
     *
     * @return a Property object containing a Boolean value that determines if abstract types are shown
     */
    public abstract Property<Boolean> getShowAbstractTypes();

    /**
     * Determines whether the system should use an abstract type name
     * for concrete service kinds.
     *
     * @return a Boolean indicating if abstract type names are used for
     *         concrete service kinds.
     */
    public abstract Property<Boolean> getUseAbstractTypeNameForConcreteServiceKinds();

    /**
     * Retrieves a list property containing the domain model package names.
     *
     * @return a ListProperty of Strings representing the domain model package names.
     */
    public abstract ListProperty<String> getDomainModelPackages();

    /**
     * Gets the style for aggregate root elements in the diagram.
     *
     * @return the style for aggregate root elements in the diagram
     */
    public abstract Property<String> getAggregateRootStyle();

    /**
     * Gets the style for frame around aggregates in the diagram.
     *
     * @return the style for the frame around aggregates in the diagram
     */
    public abstract Property<String> getAggregateFrameStyle();

    /**
     * Gets the style for domain entities in the diagram.
     *
     * @return the style for domain entities in the diagram
     */
    public abstract Property<String> getEntityStyle();

    /**
     * Gets the style for value objects in the diagram.
     *
     * @return the style for value objects in the diagram
     */
    public abstract Property<String> getValueObjectStyle();

    /**
     * Gets the style for enums in the diagram.
     *
     * @return the style for enums in the diagram
     */
    public abstract Property<String> getEnumStyle();

    /**
     * Gets the style for identity properties in the diagram.
     *
     * @return the style for identity properties in the diagram
     */
    public abstract Property<String> getIdentityStyle();

    /**
     * Gets the style for domain events in the diagram.
     *
     * @return the style for domain events in the diagram
     */
    public abstract Property<String> getDomainEventStyle();

    /**
     * Gets the style for domain commands in the diagram.
     *
     * @return the style for domain commands in the diagram
     */
    public abstract Property<String> getDomainCommandStyle();

    /**
     * Gets the style for application services in the diagram.
     *
     * @return the style for application services in the diagram
     */
    public abstract Property<String> getApplicationServiceStyle();

    /**
     * Gets the style for domain services in the diagram.
     *
     * @return the style for domain services in the diagram
     */
    public abstract Property<String> getDomainServiceStyle();

    /**
     * Gets the style for repositories in the diagram.
     *
     * @return the style for repositories in the diagram
     */
    public abstract Property<String> getRepositoryStyle();

    /**
     * Gets the style for read models in the diagram.
     *
     * @return the style for read models in the diagram
     */
    public abstract Property<String> getReadModelStyle();

    /**
     * Gets the style for query handlers in the diagram.
     *
     * @return the style for query handlers in the diagram
     */
    public abstract Property<String> getQueryHandlerStyle();

    /**
     * Gets the style for outbound services in the diagram.
     *
     * @return the style for outbound services in the diagram
     */
    public abstract Property<String> getOutboundServiceStyle();

    /**
     * Gets the font style for the diagram.
     *
     * @return the font style for the diagram
     */
    public abstract Property<String> getFont();

    /**
     * Gets the direction of diagram layout (e.g., horizontal or vertical).
     *
     * @return the direction of the diagram layout
     */
    public abstract Property<String> getDirection();

    /**
     * Gets the ranker configuration for the diagram layout.
     *
     * @return the ranker configuration for the diagram layout
     */
    public abstract Property<String> getRanker();

    /**
     * Gets the acycler setting for breaking cycles in the diagram.
     *
     * @return the acycler setting for breaking cycles in the diagram
     */
    public abstract Property<String> getAcycler();

    /**
     * Gets the background color of the diagram.
     *
     * @return the background color of the diagram
     */
    public abstract Property<String> getBackgroundColor();

    /**
     * Gets the list of classes to exclude from diagram generation.
     *
     * @return the list of classes to exclude from diagram generation
     */
    public abstract ListProperty<String> getClassesBlacklist();

    /**
     * Indicates whether fields should be shown in the diagram.
     *
     * @return true if fields should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowFields();

    /**
     * Indicates whether fully qualified class names should be shown in the diagram.
     *
     * @return true if fully qualified class names should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowFullQualifiedClassNames();

    /**
     * Indicates whether assertions should be shown in the diagram.
     *
     * @return true if assertions should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowAssertions();

    /**
     * Indicates whether methods should be shown in the diagram.
     *
     * @return true if methods should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowMethods();

    /**
     * Indicates whether only public methods should be shown in the diagram.
     *
     * @return true if only public methods should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowOnlyPublicMethods();

    /**
     * Indicates whether aggregates should be shown in the diagram.
     *
     * @return true if aggregates should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowAggregates();

    /**
     * Indicates whether fields of aggregates should be shown in the diagram.
     *
     * @return true if fields of aggregates should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowAggregateFields();

    /**
     * Indicates whether methods of aggregates should be shown in the diagram.
     *
     * @return true if methods of aggregates should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowAggregateMethods();

    /**
     * Indicates whether domain events should be shown in the diagram.
     *
     * @return true if domain events should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowDomainEvents();

    /**
     * Indicates whether fields of domain events should be shown in the diagram.
     *
     * @return true if fields of domain events should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowDomainEventFields();

    /**
     * Indicates whether methods of domain events should be shown in the diagram.
     *
     * @return true if methods of domain events should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowDomainEventMethods();

    /**
     * Indicates whether domain commands should be shown in the diagram.
     *
     * @return true if domain commands should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowDomainCommands();

    /**
     * Indicates whether only top-level domain command relations should be shown.
     *
     * @return true if only top-level domain command relations should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowOnlyTopLevelDomainCommandRelations();

    /**
     * Indicates whether fields of domain commands should be shown in the diagram.
     *
     * @return true if fields of domain commands should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowDomainCommandFields();

    /**
     * Indicates whether methods of domain commands should be shown in the diagram.
     *
     * @return true if methods of domain commands should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowDomainCommandMethods();

    /**
     * Indicates whether domain services should be shown in the diagram.
     *
     * @return true if domain services should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowDomainServices();

    /**
     * Indicates whether fields of domain services should be shown in the diagram.
     *
     * @return true if fields of domain services should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowDomainServiceFields();

    /**
     * Indicates whether methods of domain services should be shown in the diagram.
     *
     * @return true if methods of domain services should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowDomainServiceMethods();

    /**
     * Indicates whether application services should be shown in the diagram.
     *
     * @return true if application services should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowApplicationServices();

    /**
     * Indicates whether fields of application services should be shown in the diagram.
     *
     * @return true if fields of application services should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowApplicationServiceFields();


    /**
     * Indicates whether methods of application services should be shown in the diagram.
     *
     * @return true if methods of application services should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowApplicationServiceMethods();

    /**
     * Indicates whether repositories should be shown in the diagram.
     *
     * @return true if repositories should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowRepositories();

    /**
     * Indicates whether fields of repositories should be shown in the diagram.
     *
     * @return true if fields of repositories should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowRepositoryFields();

    /**
     * Indicates whether methods of repositories should be shown in the diagram.
     *
     * @return true if methods of repositories should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowRepositoryMethods();

    /**
     * Indicates whether read models should be shown in the diagram.
     *
     * @return true if read models should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowReadModels();

    /**
     * Indicates whether fields of read models should be shown in the diagram.
     *
     * @return true if fields of read models should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowReadModelFields();

    /**
     * Indicates whether methods of read models should be shown in the diagram.
     *
     * @return true if methods of read models should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowReadModelMethods();

    /**
     * Indicates whether query handlers should be shown in the diagram.
     *
     * @return true if query handlers should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowQueryHandlers();

    /**
     * Indicates whether fields of query handlers should be shown in the diagram.
     *
     * @return true if fields of query handlers should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowQueryHandlerFields();

    /**
     * Indicates whether methods of query handlers should be shown in the diagram.
     *
     * @return true if methods of query handlers should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowQueryHandlerMethods();

    /**
     * Indicates whether outbound services should be shown in the diagram.
     *
     * @return true if outbound services should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowOutboundServices();

    /**
     * Indicates whether fields of outbound services should be shown in the diagram.
     *
     * @return true if fields of outbound services should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowOutboundServiceFields();

    /**
     * Indicates whether methods of outbound services should be shown in the diagram.
     *
     * @return true if methods of outbound services should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowOutboundServiceMethods();

    /**
     * Indicates whether unspecified service kinds should be shown in the diagram.
     *
     * @return true if unspecified service kinds should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowUnspecifiedServiceKinds();

    /**
     * Indicates whether fields of unspecified service kinds should be shown in the diagram.
     *
     * @return true if fields of unspecified service kinds should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowUnspecifiedServiceKindFields();

    /**
     * Indicates whether methods of unspecified service kinds should be shown in the diagram.
     *
     * @return true if methods of unspecified service kinds should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowUnspecifiedServiceKindMethods();

    /**
     * Indicates whether the application service should be called 'driver'.
     *
     * @return true if the application services should be call 'driver' as stereotype, false otherwise
     */
    public abstract Property<Boolean> getCallApplicationServiceDriver();

    /**
     * Gets the list of fields to exclude from diagram generation.
     *
     * @return the list of fields to exclude from diagram generation
     */
    public abstract ListProperty<String> getFieldBlacklist();

    /**
     * Gets the list of methods to exclude from diagram generation.
     *
     * @return the list of methods to exclude from diagram generation
     */
    public abstract ListProperty<String> getMethodBlacklist();

    /**
     * Indicates whether inherited members of classes should be shown in the diagram.
     *
     * @return true if inherited members of classes should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowInheritedMembersInClasses();

    /**
     * Indicates whether object members of classes should be shown in the diagram.
     *
     * @return true if object members of classes should be shown, false otherwise
     */
    public abstract Property<Boolean> getShowObjectMembersInClasses();

    /**
     * Indicates whether multiplicity labels should be displayed.
     *
     * @return true if multiplicity labels should be displayed, false otherwise
     */
    public abstract Property<Boolean> getMultiplicityInLabel();

    /**
     * Indicates whether field stereotypes should be shown in the diagram.
     *
     * @return true if field stereotypes should be shown in the diagram, false otherwise
     */
    public abstract Property<Boolean> getFieldStereotypes();

    /**
     * Gets the list of type names used as seeds for transitive filtering of domain services.
     *
     * @return the list of type names used as seeds for transitive filtering of domain services
     */
    public abstract ListProperty<String> getTransitiveFilterSeedDomainServiceTypeNames();

}
