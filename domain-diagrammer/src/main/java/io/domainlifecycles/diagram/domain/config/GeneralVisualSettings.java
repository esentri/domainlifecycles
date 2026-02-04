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

import java.util.Collections;
import java.util.List;

/**
 * The GeneralVisualSettings class is responsible for managing visualization settings in a domain-specific diagram.
 * It encapsulates configuration preferences for various entities and their attributes,
 * determining their visibility and representation in the diagram.
 *
 * @author Mario Herb
 */
public class GeneralVisualSettings {
    private static final boolean DEFAULT_SHOW_FIELDS = true;
    private static final boolean DEFAULT_SHOW_FULL_QUALIFIED_CLASS_NAMES = false;
    private static final boolean DEFAULT_SHOW_ASSERTIONS = true;
    private static final boolean DEFAULT_SHOW_METHODS = true;
    private static final boolean DEFAULT_SHOW_ONLY_PUBLIC_METHODS = true;
    private static final boolean DEFAULT_SHOW_AGGREGATES = true;
    private static final boolean DEFAULT_SHOW_AGGREGATE_FIELDS = true;
    private static final boolean DEFAULT_SHOW_AGGREGATE_METHODS = true;
    private static final boolean DEFAULT_SHOW_DOMAIN_EVENTS = true;
    private static final boolean DEFAULT_SHOW_DOMAIN_EVENT_FIELDS = false;
    private static final boolean DEFAULT_SHOW_DOMAIN_EVENT_METHODS = false;
    private static final boolean DEFAULT_SHOW_DOMAIN_COMMANDS = true;
    private static final boolean DEFAULT_SHOW_ONLY_TOP_LEVEL_DOMAIN_COMMAND_RELATIONS = true;
    private static final boolean DEFAULT_SHOW_DOMAIN_COMMAND_FIELDS = false;
    private static final boolean DEFAULT_SHOW_DOMAIN_COMMAND_METHODS = false;
    private static final boolean DEFAULT_SHOW_DOMAIN_SERVICES = true;
    private static final boolean DEFAULT_SHOW_DOMAIN_SERVICE_FIELDS = false;
    private static final boolean DEFAULT_SHOW_DOMAIN_SERVICE_METHODS = true;
    private static final boolean DEFAULT_SHOW_APPLICATION_SERVICES = true;
    private static final boolean DEFAULT_SHOW_APPLICATION_SERVICE_FIELDS = false;
    private static final boolean DEFAULT_SHOW_APPLICATION_SERVICE_METHODS = true;
    private static final boolean DEFAULT_SHOW_REPOSITORIES = true;
    private static final boolean DEFAULT_SHOW_REPOSITORY_FIELDS = false;
    private static final boolean DEFAULT_SHOW_REPOSITORY_METHODS = true;
    private static final boolean DEFAULT_SHOW_READ_MODELS = true;
    private static final boolean DEFAULT_SHOW_READ_MODEL_FIELDS = true;
    private static final boolean DEFAULT_SHOW_READ_MODEL_METHODS = false;
    private static final boolean DEFAULT_SHOW_QUERY_HANDLERS = true;
    private static final boolean DEFAULT_SHOW_QUERY_HANDLER_FIELDS = false;
    private static final boolean DEFAULT_SHOW_QUERY_HANDLER_METHODS = false;
    private static final boolean DEFAULT_SHOW_OUTBOUND_SERVICES = true;
    private static final boolean DEFAULT_SHOW_OUTBOUND_SERVICE_FIELDS = false;
    private static final boolean DEFAULT_SHOW_OUTBOUND_SERVICE_METHODS = false;
    private static final boolean DEFAULT_SHOW_UNSPECIFIED_SERVICE_KINDS = true;
    private static final boolean DEFAULT_SHOW_UNSPECIFIED_SERVICE_KIND_FIELDS = false;
    private static final boolean DEFAULT_SHOW_UNSPECIFIED_SERVICE_KIND_METHODS = false;
    private static final boolean DEFAULT_CALL_APPLICATION_SERVICE_DRIVER = false;
    private static final List<String> DEFAULT_FIELD_BLACKLIST = List.of("concurrencyVersion");
    private static final List<String> DEFAULT_METHOD_BLACKLIST = List.of(
        "builder",
        "validate",
        "concurrencyVersion",
        "id",
        "findResultById",
        "publish",
        "increaseVersion",
        "getFetcher",
        "equals",
        "hashCode",
        "toString");
    private static final boolean DEFAULT_SHOW_INHERITED_MEMBERS_IN_CLASSES = true;
    private static final boolean DEFAULT_SHOW_OBJECT_MEMBERS_IN_CLASSES = true;
    private static final boolean DEFAULT_MULTIPLICITY_IN_LABEL = true;
    private static final boolean DEFAULT_FIELD_STEREOTYPES = true;
    private static final boolean DEFAULT_SHOW_ALL_INHERITANCE_STRUCTURES = false;
    private static final boolean DEFAULT_SHOW_INHERITANCE_STRUCTURES_IN_AGGREGATES = true;
    private static final boolean DEFAULT_SHOW_INHERITANCE_STRUCTURES_FOR_SERVICE_KINDS = false;
    private static final boolean DEFAULT_SHOW_INHERITANCE_STRUCTURES_FOR_READ_MODELS = false;
    private static final boolean DEFAULT_SHOW_INHERITANCE_STRUCTURES_FOR_DOMAIN_EVENTS = false;
    private static final boolean DEFAULT_SHOW_INHERITANCE_STRUCTURES_FOR_DOMAIN_COMMANDS = false;
    private static final boolean DEFAULT_SHOW_NOTES = false;

    private final boolean showFields;
    private final boolean showFullQualifiedClassNames;
    private final boolean showAssertions;
    private final boolean showMethods;
    private final boolean showOnlyPublicMethods;
    private final boolean showAggregates;
    private final boolean showAggregateFields;
    private final boolean showAggregateMethods;
    private final boolean showDomainEvents;
    private final boolean showDomainEventFields;
    private final boolean showDomainEventMethods;
    private final boolean showDomainCommands;
    private final boolean showOnlyTopLevelDomainCommandRelations;
    private final boolean showDomainCommandFields;
    private final boolean showDomainCommandMethods;
    private final boolean showDomainServices;
    private final boolean showDomainServiceFields;
    private final boolean showDomainServiceMethods;
    private final boolean showApplicationServices;
    private final boolean showApplicationServiceFields;
    private final boolean showApplicationServiceMethods;
    private final boolean showRepositories;
    private final boolean showRepositoryFields;
    private final boolean showRepositoryMethods;
    private final boolean showReadModels;
    private final boolean showReadModelFields;
    private final boolean showReadModelMethods;
    private final boolean showQueryHandlers;
    private final boolean showQueryHandlerFields;
    private final boolean showQueryHandlerMethods;
    private final boolean showOutboundServices;
    private final boolean showOutboundServiceFields;
    private final boolean showOutboundServiceMethods;
    private final boolean showUnspecifiedServiceKinds;
    private final boolean showUnspecifiedServiceKindFields;
    private final boolean showUnspecifiedServiceKindMethods;
    private final boolean callApplicationServiceDriver;
    private final List<String> fieldBlacklist;
    private final List<String> methodBlacklist;
    private final boolean showInheritedMembersInClasses;
    private final boolean showObjectMembersInClasses;
    private final boolean multiplicityInLabel;
    private final boolean fieldStereotypes;
    private final boolean showAllInheritanceStructures;
    private final boolean showInheritanceStructuresInAggregates;
    private final boolean showInheritanceStructuresForServiceKinds;
    private final boolean showInheritanceStructuresForReadModels;
    private final boolean showInheritanceStructuresForDomainEvents;
    private final boolean showInheritanceStructuresForDomainCommands;
    private final boolean showNotes;

    private GeneralVisualSettings(
        boolean showFields,
        boolean showFullQualifiedClassNames,
        boolean showAssertions,
        boolean showMethods,
        boolean showOnlyPublicMethods,
        boolean showAggregates,
        boolean showAggregateFields,
        boolean showAggregateMethods,
        boolean showDomainEvents,
        boolean showDomainEventFields,
        boolean showDomainEventMethods,
        boolean showDomainCommands,
        boolean showOnlyTopLevelDomainCommandRelations,
        boolean showDomainCommandFields,
        boolean showDomainCommandMethods,
        boolean showDomainServices,
        boolean showDomainServiceFields,
        boolean showDomainServiceMethods,
        boolean showApplicationServices,
        boolean showApplicationServiceFields,
        boolean showApplicationServiceMethods,
        boolean showRepositories,
        boolean showRepositoryFields,
        boolean showRepositoryMethods,
        boolean showReadModels,
        boolean showReadModelFields,
        boolean showReadModelMethods,
        boolean showQueryHandlers,
        boolean showQueryHandlerFields,
        boolean showQueryHandlerMethods,
        boolean showOutboundServices,
        boolean showOutboundServiceFields,
        boolean showOutboundServiceMethods,
        boolean showUnspecifiedServiceKinds,
        boolean showUnspecifiedServiceKindFields,
        boolean showUnspecifiedServiceKindMethods,
        boolean callApplicationServiceDriver,
        List<String> fieldBlacklist,
        List<String> methodBlacklist,
        boolean showInheritedMembersInClasses,
        boolean showObjectMembersInClasses,
        boolean multiplicityInLabel,
        boolean fieldStereotypes,
        boolean showAllInheritanceStructures,
        boolean showInheritanceStructuresInAggregates,
        boolean showInheritanceStructuresForServiceKinds,
        boolean showInheritanceStructuresForReadModels,
        boolean showInheritanceStructuresForDomainEvents,
        boolean showInheritanceStructuresForDomainCommands,
        boolean showNotes
    ) {
        this.showFields = showFields;
        this.showFullQualifiedClassNames = showFullQualifiedClassNames;
        this.showAssertions = showAssertions;
        this.showMethods = showMethods;
        this.showOnlyPublicMethods = showOnlyPublicMethods;
        this.showAggregates = showAggregates;
        this.showAggregateFields = showAggregateFields;
        this.showAggregateMethods = showAggregateMethods;
        this.showDomainEvents = showDomainEvents;
        this.showDomainEventFields = showDomainEventFields;
        this.showDomainEventMethods = showDomainEventMethods;
        this.showDomainCommands = showDomainCommands;
        this.showOnlyTopLevelDomainCommandRelations = showOnlyTopLevelDomainCommandRelations;
        this.showDomainCommandFields = showDomainCommandFields;
        this.showDomainCommandMethods = showDomainCommandMethods;
        this.showDomainServices = showDomainServices;
        this.showDomainServiceFields = showDomainServiceFields;
        this.showDomainServiceMethods = showDomainServiceMethods;
        this.showApplicationServices = showApplicationServices;
        this.showApplicationServiceFields = showApplicationServiceFields;
        this.showApplicationServiceMethods = showApplicationServiceMethods;
        this.showRepositories = showRepositories;
        this.showRepositoryFields = showRepositoryFields;
        this.showRepositoryMethods = showRepositoryMethods;
        this.showReadModels = showReadModels;
        this.showReadModelFields = showReadModelFields;
        this.showReadModelMethods = showReadModelMethods;
        this.showQueryHandlers = showQueryHandlers;
        this.showQueryHandlerFields = showQueryHandlerFields;
        this.showQueryHandlerMethods = showQueryHandlerMethods;
        this.showOutboundServices = showOutboundServices;
        this.showOutboundServiceFields = showOutboundServiceFields;
        this.showOutboundServiceMethods = showOutboundServiceMethods;
        this.showUnspecifiedServiceKinds = showUnspecifiedServiceKinds;
        this.showUnspecifiedServiceKindFields = showUnspecifiedServiceKindFields;
        this.showUnspecifiedServiceKindMethods = showUnspecifiedServiceKindMethods;
        this.callApplicationServiceDriver = callApplicationServiceDriver;
        this.fieldBlacklist = fieldBlacklist;
        this.methodBlacklist = methodBlacklist;
        this.showInheritedMembersInClasses = showInheritedMembersInClasses;
        this.showObjectMembersInClasses = showObjectMembersInClasses;
        this.multiplicityInLabel = multiplicityInLabel;
        this.fieldStereotypes = fieldStereotypes;
        this.showAllInheritanceStructures = showAllInheritanceStructures;
        this.showInheritanceStructuresInAggregates = showInheritanceStructuresInAggregates;
        this.showInheritanceStructuresForServiceKinds = showInheritanceStructuresForServiceKinds;
        this.showInheritanceStructuresForReadModels = showInheritanceStructuresForReadModels;
        this.showInheritanceStructuresForDomainEvents = showInheritanceStructuresForDomainEvents;
        this.showInheritanceStructuresForDomainCommands = showInheritanceStructuresForDomainCommands;
        this.showNotes = showNotes;
    }

    /**
     * Returns whether fields should be shown in the diagram.
     *
     * @return true if fields should be shown, false otherwise
     */
    public boolean isShowFields() {
        return showFields;
    }

    /**
     * Returns whether fully qualified class names should be shown in the diagram.
     *
     * @return true if fully qualified class names should be shown, false otherwise
     */
    public boolean isShowFullQualifiedClassNames() {
        return showFullQualifiedClassNames;
    }

    /**
     * Returns whether assertions should be shown in the diagram.
     *
     * @return true if assertions should be shown, false otherwise
     */
    public boolean isShowAssertions() {
        return showAssertions;
    }

    /**
     * Returns whether methods should be shown in the diagram.
     *
     * @return true if methods should be shown, false otherwise
     */
    public boolean isShowMethods() {
        return showMethods;
    }

    /**
     * Returns whether only public methods should be shown in the diagram.
     *
     * @return true if only public methods should be shown, false otherwise
     */
    public boolean isShowOnlyPublicMethods() {
        return showOnlyPublicMethods;
    }

    /**
     * Returns whether aggregates should be shown in the diagram.
     *
     * @return true if aggregates should be shown, false otherwise
     */
    public boolean isShowAggregates() {
        return showAggregates;
    }

    /**
     * Returns whether aggregate fields should be shown in the diagram.
     *
     * @return true if aggregate fields should be shown, false otherwise
     */
    public boolean isShowAggregateFields() {
        return showAggregateFields;
    }

    /**
     * Returns whether aggregate methods should be shown in the diagram.
     *
     * @return true if aggregate methods should be shown, false otherwise
     */
    public boolean isShowAggregateMethods() {
        return showAggregateMethods;
    }

    /**
     * Returns whether domain events should be shown in the diagram.
     *
     * @return true if domain events should be shown, false otherwise
     */
    public boolean isShowDomainEvents() {
        return showDomainEvents;
    }

    /**
     * Returns whether domain event fields should be shown in the diagram.
     *
     * @return true if domain event fields should be shown, false otherwise
     */
    public boolean isShowDomainEventFields() {
        return showDomainEventFields;
    }

    /**
     * Returns whether domain event methods should be shown in the diagram.
     *
     * @return true if domain event methods should be shown, false otherwise
     */
    public boolean isShowDomainEventMethods() {
        return showDomainEventMethods;
    }

    /**
     * Returns whether domain commands should be shown in the diagram.
     *
     * @return true if domain commands should be shown, false otherwise
     */
    public boolean isShowDomainCommands() {
        return showDomainCommands;
    }

    /**
     * Returns whether only top level domain command relations should be shown in the diagram.
     *
     * @return true if only top level domain command relations should be shown, false otherwise
     */
    public boolean isShowOnlyTopLevelDomainCommandRelations() {
        return showOnlyTopLevelDomainCommandRelations;
    }

    /**
     * Returns whether domain command fields should be shown in the diagram.
     *
     * @return true if domain command fields should be shown, false otherwise
     */
    public boolean isShowDomainCommandFields() {
        return showDomainCommandFields;
    }

    /**
     * Returns whether domain command methods should be shown in the diagram.
     *
     * @return true if domain command methods should be shown, false otherwise
     */
    public boolean isShowDomainCommandMethods() {
        return showDomainCommandMethods;
    }

    /**
     * Returns whether domain services should be shown in the diagram.
     *
     * @return true if domain services should be shown, false otherwise
     */
    public boolean isShowDomainServices() {
        return showDomainServices;
    }

    /**
     * Returns whether domain service fields should be shown in the diagram.
     *
     * @return true if domain service fields should be shown, false otherwise
     */
    public boolean isShowDomainServiceFields() {
        return showDomainServiceFields;
    }

    /**
     * Returns whether domain service methods should be shown in the diagram.
     *
     * @return true if domain service methods should be shown, false otherwise
     */
    public boolean isShowDomainServiceMethods() {
        return showDomainServiceMethods;
    }

    /**
     * Returns whether application services should be shown in the diagram.
     *
     * @return true if application services should be shown, false otherwise
     */
    public boolean isShowApplicationServices() {
        return showApplicationServices;
    }

    /**
     * Returns whether application service fields should be shown in the diagram.
     *
     * @return true if application service fields should be shown, false otherwise
     */
    public boolean isShowApplicationServiceFields() {
        return showApplicationServiceFields;
    }

    /**
     * Returns whether application service methods should be shown in the diagram.
     *
     * @return true if application service methods should be shown, false otherwise
     */
    public boolean isShowApplicationServiceMethods() {
        return showApplicationServiceMethods;
    }

    /**
     * Returns whether repositories should be shown in the diagram.
     *
     * @return true if repositories should be shown, false otherwise
     */
    public boolean isShowRepositories() {
        return showRepositories;
    }

    /**
     * Returns whether repository fields should be shown in the diagram.
     *
     * @return true if repository fields should be shown, false otherwise
     */
    public boolean isShowRepositoryFields() {
        return showRepositoryFields;
    }

    /**
     * Returns whether repository methods should be shown in the diagram.
     *
     * @return true if repository methods should be shown, false otherwise
     */
    public boolean isShowRepositoryMethods() {
        return showRepositoryMethods;
    }

    /**
     * Returns whether read models should be shown in the diagram.
     *
     * @return true if read models should be shown, false otherwise
     */
    public boolean isShowReadModels() {
        return showReadModels;
    }

    /**
     * Returns whether read model fields should be shown in the diagram.
     *
     * @return true if read model fields should be shown, false otherwise
     */
    public boolean isShowReadModelFields() {
        return showReadModelFields;
    }

    /**
     * Returns whether read model methods should be shown in the diagram.
     *
     * @return true if read model methods should be shown, false otherwise
     */
    public boolean isShowReadModelMethods() {
        return showReadModelMethods;
    }

    /**
     * Returns whether query handlers should be shown in the diagram.
     *
     * @return true if query handlers should be shown, false otherwise
     */
    public boolean isShowQueryHandlers() {
        return showQueryHandlers;
    }

    /**
     * Returns whether query handler fields should be shown in the diagram.
     *
     * @return true if query handler fields should be shown, false otherwise
     */
    public boolean isShowQueryHandlerFields() {
        return showQueryHandlerFields;
    }

    /**
     * Returns whether query handler methods should be shown in the diagram.
     *
     * @return true if query handler methods should be shown, false otherwise
     */
    public boolean isShowQueryHandlerMethods() {
        return showQueryHandlerMethods;
    }

    /**
     * Returns whether outbound services should be shown in the diagram.
     *
     * @return true if outbound services should be shown, false otherwise
     */
    public boolean isShowOutboundServices() {
        return showOutboundServices;
    }

    /**
     * Returns whether outbound service fields should be shown in the diagram.
     *
     * @return true if outbound service fields should be shown, false otherwise
     */
    public boolean isShowOutboundServiceFields() {
        return showOutboundServiceFields;
    }

    /**
     * Returns whether outbound service methods should be shown in the diagram.
     *
     * @return true if outbound service methods should be shown, false otherwise
     */
    public boolean isShowOutboundServiceMethods() {
        return showOutboundServiceMethods;
    }

    /**
     * Returns whether unspecified service kinds should be shown in the diagram.
     *
     * @return true if unspecified service kinds should be shown, false otherwise
     */
    public boolean isShowUnspecifiedServiceKinds() {
        return showUnspecifiedServiceKinds;
    }

    /**
     * Returns whether unspecified service kind fields should be shown in the diagram.
     *
     * @return true if unspecified service kind fields should be shown, false otherwise
     */
    public boolean isShowUnspecifiedServiceKindFields() {
        return showUnspecifiedServiceKindFields;
    }

    /**
     * Returns whether unspecified service kind methods should be shown in the diagram.
     *
     * @return true if unspecified service kind methods should be shown, false otherwise
     */
    public boolean isShowUnspecifiedServiceKindMethods() {
        return showUnspecifiedServiceKindMethods;
    }

    /**
     * Returns whether application service driver calls should be included in the diagram.
     *
     * @return true if application service driver calls should be included, false otherwise
     */
    public boolean isCallApplicationServiceDriver() {
        return callApplicationServiceDriver;
    }

    /**
     * Returns the list of field names that should be excluded from the diagram.
     *
     * @return an unmodifiable list of field names to be excluded
     */
    public List<String> getFieldBlacklist() {
        return fieldBlacklist;
    }

    /**
     * Returns the list of method names that should be excluded from the diagram.
     *
     * @return an unmodifiable list of method names to be excluded
     */
    public List<String> getMethodBlacklist() {
        return methodBlacklist;
    }

    /**
     * Returns whether inherited members should be shown in classes in the diagram.
     *
     * @return true if inherited members should be shown, false otherwise
     */
    public boolean isShowInheritedMembersInClasses() {
        return showInheritedMembersInClasses;
    }

    /**
     * Returns whether Object class members should be shown in classes in the diagram.
     *
     * @return true if Object members should be shown, false otherwise
     */
    public boolean isShowObjectMembersInClasses() {
        return showObjectMembersInClasses;
    }

    /**
     * Returns whether multiplicity should be shown in relationship labels in the diagram.
     *
     * @return true if multiplicity should be shown in labels, false otherwise
     */
    public boolean isMultiplicityInLabel() {
        return multiplicityInLabel;
    }

    /**
     * Returns whether field stereotypes should be shown in the diagram.
     *
     * @return true if field stereotypes should be shown, false otherwise
     */
    public boolean isFieldStereotypes() {
        return fieldStereotypes;
    }

    /**
     * Determines whether all inheritance structures are being shown.
     *
     * @return true if all inheritance structures should be displayed, false otherwise
     */
    public boolean isShowAllInheritanceStructures() {
        return showAllInheritanceStructures;
    }

    /**
     * Indicates whether inheritance structures in aggregates should be displayed.
     *
     * @return true if inheritance structures in aggregates are to be shown, false otherwise
     */
    public boolean isShowInheritanceStructuresInAggregates() {
        return showInheritanceStructuresInAggregates;
    }

    /**
     * Returns whether inheritance structures should be shown for service kinds in the diagram.
     *
     * @return true if inheritance structures should be shown for service kinds, false otherwise
     */
    public boolean isShowInheritanceStructuresForServiceKinds() {
        return showInheritanceStructuresForServiceKinds;
    }

    /**
     * Returns whether inheritance structures should be shown for read models in the diagram.
     *
     * @return true if inheritance structures should be shown for read models, false otherwise
     */
    public boolean isShowInheritanceStructuresForReadModels() {
        return showInheritanceStructuresForReadModels;
    }

    /**
     * Returns whether inheritance structures should be shown for domain events in the diagram.
     *
     * @return true if inheritance structures should be shown for domain events, false otherwise
     */
    public boolean isShowInheritanceStructuresForDomainEvents() {
        return showInheritanceStructuresForDomainEvents;
    }

    /**
     * Returns whether inheritance structures should be shown for domain commands in the diagram.
     *
     * @return true if inheritance structures should be shown for domain commands, false otherwise
     */
    public boolean isShowInheritanceStructuresForDomainCommands() {
        return showInheritanceStructuresForDomainCommands;
    }

    /**
     * Returns whether notes should be shown in the diagram.
     *
     * @return true if notes should be shown, false otherwise
     */
    public boolean isShowNotes() {
        return showNotes;
    }

    /**
     * Creates and returns a new instance of GeneralVisualSettingsBuilder.
     *
     * @return a new instance of GeneralVisualSettingsBuilder to configure and build a GeneralVisualSettings object.
     */
    public static GeneralVisualSettingsBuilder builder() {
        return new GeneralVisualSettingsBuilder();
    }

    /**
     * DomainDiagramConfigBuilder class to configure and customize the general visual settings.
     * Provides a fluent API for setting various visualization options related to
     * fields, methods, class representations, and components in a domain model.
     * The builder includes options for toggling visibility of different artifacts
     * such as fields, methods, assertions, aggregates, domain events, domain services,
     * application services, repositories, read models, query handlers, outbound services,
     * and more. Additionally, it enables customization of behavior like showing only
     * public methods, top-level domain relations, and inherited or abstract members.
     * Each configuration method returns the builder instance, enabling method chaining.
     * This builder is initialized with default settings, which can be overridden
     * by calling the respective configuration methods.
     * Example methods provide control over options such as:
     * - Toggling visibility of fields, methods, aggregates, and domain-specific components.
     * - Handling blacklists for fields and methods.
     * - Configuring visualization for inherited members, object members, and abstract types.
     * - Adjusting multiplicity in labels and enabling field stereotypes.
     */
    public static class GeneralVisualSettingsBuilder {
        private boolean showFields$value = DEFAULT_SHOW_FIELDS;
        private boolean showFullQualifiedClassNames$value = DEFAULT_SHOW_FULL_QUALIFIED_CLASS_NAMES;
        private boolean showAssertions$value = DEFAULT_SHOW_ASSERTIONS;
        private boolean showMethods$value = DEFAULT_SHOW_METHODS;
        private boolean showOnlyPublicMethods$value = DEFAULT_SHOW_ONLY_PUBLIC_METHODS;
        private boolean showAggregates$value = DEFAULT_SHOW_AGGREGATES;
        private boolean showAggregateFields$value = DEFAULT_SHOW_AGGREGATE_FIELDS;
        private boolean showAggregateMethods$value = DEFAULT_SHOW_AGGREGATE_METHODS;
        private boolean showDomainEvents$value = DEFAULT_SHOW_DOMAIN_EVENTS;
        private boolean showDomainEventFields$value = DEFAULT_SHOW_DOMAIN_EVENT_FIELDS;
        private boolean showDomainEventMethods$value = DEFAULT_SHOW_DOMAIN_EVENT_METHODS;
        private boolean showDomainCommands$value = DEFAULT_SHOW_DOMAIN_COMMANDS;
        private boolean showOnlyTopLevelDomainCommandRelations$value = DEFAULT_SHOW_ONLY_TOP_LEVEL_DOMAIN_COMMAND_RELATIONS;
        private boolean showDomainCommandFields$value = DEFAULT_SHOW_DOMAIN_COMMAND_FIELDS;
        private boolean showDomainCommandMethods$value = DEFAULT_SHOW_DOMAIN_COMMAND_METHODS;
        private boolean showDomainServices$value = DEFAULT_SHOW_DOMAIN_SERVICES;
        private boolean showDomainServiceFields$value = DEFAULT_SHOW_DOMAIN_SERVICE_FIELDS;
        private boolean showDomainServiceMethods$value = DEFAULT_SHOW_DOMAIN_SERVICE_METHODS;
        private boolean showApplicationServices$value = DEFAULT_SHOW_APPLICATION_SERVICES;
        private boolean showApplicationServiceFields$value = DEFAULT_SHOW_APPLICATION_SERVICE_FIELDS;
        private boolean showApplicationServiceMethods$value = DEFAULT_SHOW_APPLICATION_SERVICE_METHODS;
        private boolean showRepositories$value = DEFAULT_SHOW_REPOSITORIES;
        private boolean showRepositoryFields$value = DEFAULT_SHOW_REPOSITORY_FIELDS;
        private boolean showRepositoryMethods$value = DEFAULT_SHOW_REPOSITORY_METHODS;
        private boolean showReadModels$value = DEFAULT_SHOW_READ_MODELS;
        private boolean showReadModelFields$value = DEFAULT_SHOW_READ_MODEL_FIELDS;
        private boolean showReadModelMethods$value = DEFAULT_SHOW_READ_MODEL_METHODS;
        private boolean showQueryHandlers$value = DEFAULT_SHOW_QUERY_HANDLERS;
        private boolean showQueryHandlerFields$value = DEFAULT_SHOW_QUERY_HANDLER_FIELDS;
        private boolean showQueryHandlerMethods$value = DEFAULT_SHOW_QUERY_HANDLER_METHODS;
        private boolean showOutboundServices$value = DEFAULT_SHOW_OUTBOUND_SERVICES;
        private boolean showOutboundServiceFields$value = DEFAULT_SHOW_OUTBOUND_SERVICE_FIELDS;
        private boolean showOutboundServiceMethods$value = DEFAULT_SHOW_OUTBOUND_SERVICE_METHODS;
        private boolean showUnspecifiedServiceKinds$value = DEFAULT_SHOW_UNSPECIFIED_SERVICE_KINDS;
        private boolean showUnspecifiedServiceKindFields$value = DEFAULT_SHOW_UNSPECIFIED_SERVICE_KIND_FIELDS;
        private boolean showUnspecifiedServiceKindMethods$value = DEFAULT_SHOW_UNSPECIFIED_SERVICE_KIND_METHODS;
        private boolean callApplicationServiceDriver$value = DEFAULT_CALL_APPLICATION_SERVICE_DRIVER;
        private List<String> fieldBlacklist$value;
        private List<String> methodBlacklist$value;
        private boolean showInheritedMembersInClasses$value = DEFAULT_SHOW_INHERITED_MEMBERS_IN_CLASSES;
        private boolean showObjectMembersInClasses$value = DEFAULT_SHOW_OBJECT_MEMBERS_IN_CLASSES;
        private boolean multiplicityInLabel$value = DEFAULT_MULTIPLICITY_IN_LABEL;
        private boolean fieldStereotypes$value = DEFAULT_FIELD_STEREOTYPES;
        private boolean showAllInheritanceStructures$value = DEFAULT_SHOW_ALL_INHERITANCE_STRUCTURES;
        private boolean showInheritanceStructuresInAggregates$value = DEFAULT_SHOW_INHERITANCE_STRUCTURES_IN_AGGREGATES;
        private boolean showInheritanceStructuresForServiceKinds$value = DEFAULT_SHOW_INHERITANCE_STRUCTURES_FOR_SERVICE_KINDS;
        private boolean showInheritanceStructuresForReadModels$value = DEFAULT_SHOW_INHERITANCE_STRUCTURES_FOR_READ_MODELS;
        private boolean showInheritanceStructuresForDomainEvents$value = DEFAULT_SHOW_INHERITANCE_STRUCTURES_FOR_DOMAIN_EVENTS;
        private boolean showInheritanceStructuresForDomainCommands$value = DEFAULT_SHOW_INHERITANCE_STRUCTURES_FOR_DOMAIN_COMMANDS;
        private boolean showNotes$value = DEFAULT_SHOW_NOTES;

        /**
         * Constructs a new GeneralVisualSettingsBuilder with default values.
         */
        public GeneralVisualSettingsBuilder() {
        }

        /**
         * Sets whether to show fields in the diagram.
         *
         * @param showFields true to show fields, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowFields(boolean showFields) {
            this.showFields$value = showFields;
            return this;
        }

        /**
         * Sets whether to show fully qualified class names in the diagram.
         *
         * @param showFullQualifiedClassNames true to show full names, false for simple names
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowFullQualifiedClassNames(boolean showFullQualifiedClassNames) {
            this.showFullQualifiedClassNames$value = showFullQualifiedClassNames;
            return this;
        }

        /**
         * Sets whether to show assertions in the diagram.
         *
         * @param showAssertions true to show assertions, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowAssertions(boolean showAssertions) {
            this.showAssertions$value = showAssertions;
            return this;
        }

        /**
         * Sets whether to show methods in the diagram.
         *
         * @param showMethods true to show methods, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowMethods(boolean showMethods) {
            this.showMethods$value = showMethods;
            return this;
        }

        /**
         * Sets whether to show only public methods in the diagram.
         *
         * @param showOnlyPublicMethods true to show only public methods, false to show all
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowOnlyPublicMethods(boolean showOnlyPublicMethods) {
            this.showOnlyPublicMethods$value = showOnlyPublicMethods;
            return this;
        }

        /**
         * Sets whether to show aggregates in the diagram.
         *
         * @param showAggregates true to show aggregates, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowAggregates(boolean showAggregates) {
            this.showAggregates$value = showAggregates;
            return this;
        }

        /**
         * Sets whether to show aggregate fields in the diagram.
         *
         * @param showAggregateFields true to show aggregate fields, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowAggregateFields(boolean showAggregateFields) {
            this.showAggregateFields$value = showAggregateFields;
            return this;
        }

        /**
         * Sets whether to show aggregate methods in the diagram.
         *
         * @param showAggregateMethods true to show aggregate methods, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowAggregateMethods(boolean showAggregateMethods) {
            this.showAggregateMethods$value = showAggregateMethods;
            return this;
        }

        /**
         * Sets whether to show domain events in the diagram.
         *
         * @param showDomainEvents true to show domain events, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowDomainEvents(boolean showDomainEvents) {
            this.showDomainEvents$value = showDomainEvents;
            return this;
        }

        /**
         * Sets whether to show domain event fields in the diagram.
         *
         * @param showDomainEventFields true to show domain event fields, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowDomainEventFields(boolean showDomainEventFields) {
            this.showDomainEventFields$value = showDomainEventFields;
            return this;
        }

        /**
         * Sets whether to show domain event methods in the diagram.
         *
         * @param showDomainEventMethods true to show domain event methods, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowDomainEventMethods(boolean showDomainEventMethods) {
            this.showDomainEventMethods$value = showDomainEventMethods;
            return this;
        }

        /**
         * Sets whether to show domain commands in the diagram.
         *
         * @param showDomainCommands true to show domain commands, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowDomainCommands(boolean showDomainCommands) {
            this.showDomainCommands$value = showDomainCommands;
            return this;
        }

        /**
         * Sets whether to show only top-level domain command relations in the diagram.
         *
         * @param showOnlyTopLevelDomainCommandRelations true to show only top-level relations, false to show all
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowOnlyTopLevelDomainCommandRelations(boolean showOnlyTopLevelDomainCommandRelations) {
            this.showOnlyTopLevelDomainCommandRelations$value = showOnlyTopLevelDomainCommandRelations;
            return this;
        }

        /**
         * Sets whether to show domain command fields in the diagram.
         *
         * @param showDomainCommandFields true to show domain command fields, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowDomainCommandFields(boolean showDomainCommandFields) {
            this.showDomainCommandFields$value = showDomainCommandFields;
            return this;
        }

        /**
         * Sets whether to show domain command methods in the diagram.
         *
         * @param showDomainCommandMethods true to show domain command methods, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowDomainCommandMethods(boolean showDomainCommandMethods) {
            this.showDomainCommandMethods$value = showDomainCommandMethods;
            return this;
        }

        /**
         * Sets whether to show domain services in the diagram.
         *
         * @param showDomainServices true to show domain services, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowDomainServices(boolean showDomainServices) {
            this.showDomainServices$value = showDomainServices;
            return this;
        }

        /**
         * Sets whether to show domain service fields in the diagram.
         *
         * @param showDomainServiceFields true to show domain service fields, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowDomainServiceFields(boolean showDomainServiceFields) {
            this.showDomainServiceFields$value = showDomainServiceFields;
            return this;
        }

        /**
         * Sets whether to show domain service methods in the diagram.
         *
         * @param showDomainServiceMethods true to show domain service methods, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowDomainServiceMethods(boolean showDomainServiceMethods) {
            this.showDomainServiceMethods$value = showDomainServiceMethods;
            return this;
        }

        /**
         * Sets whether to show application services in the diagram.
         *
         * @param showApplicationServices true to show application services, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowApplicationServices(boolean showApplicationServices) {
            this.showApplicationServices$value = showApplicationServices;
            return this;
        }

        /**
         * Sets whether to show application service fields in the diagram.
         *
         * @param showApplicationServiceFields true to show application service fields, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowApplicationServiceFields(boolean showApplicationServiceFields) {
            this.showApplicationServiceFields$value = showApplicationServiceFields;
            return this;
        }

        /**
         * Sets whether to show application service methods in the diagram.
         *
         * @param showApplicationServiceMethods true to show application service methods, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowApplicationServiceMethods(boolean showApplicationServiceMethods) {
            this.showApplicationServiceMethods$value = showApplicationServiceMethods;
            return this;
        }

        /**
         * Sets whether to show repositories in the diagram.
         *
         * @param showRepositories true to show repositories, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowRepositories(boolean showRepositories) {
            this.showRepositories$value = showRepositories;
            return this;
        }

        /**
         * Sets whether to show repository fields in the diagram.
         *
         * @param showRepositoryFields true to show repository fields, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowRepositoryFields(boolean showRepositoryFields) {
            this.showRepositoryFields$value = showRepositoryFields;
            return this;
        }

        /**
         * Sets whether to show repository methods in the diagram.
         *
         * @param showRepositoryMethods true to show repository methods, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowRepositoryMethods(boolean showRepositoryMethods) {
            this.showRepositoryMethods$value = showRepositoryMethods;
            return this;
        }

        /**
         * Sets whether to show read models in the diagram.
         *
         * @param showReadModels true to show read models, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowReadModels(boolean showReadModels) {
            this.showReadModels$value = showReadModels;
            return this;
        }

        /**
         * Sets whether to show read model fields in the diagram.
         *
         * @param showReadModelFields true to show read model fields, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowReadModelFields(boolean showReadModelFields) {
            this.showReadModelFields$value = showReadModelFields;
            return this;
        }

        /**
         * Sets whether to show read model methods in the diagram.
         *
         * @param showReadModelMethods true to show read model methods, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowReadModelMethods(boolean showReadModelMethods) {
            this.showReadModelMethods$value = showReadModelMethods;
            return this;
        }

        /**
         * Sets whether to show query handlers in the diagram.
         *
         * @param showQueryHandlers true to show query handlers, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowQueryHandlers(boolean showQueryHandlers) {
            this.showQueryHandlers$value = showQueryHandlers;
            return this;
        }

        /**
         * Sets whether to show query handler fields in the diagram.
         *
         * @param showQueryHandlerFields true to show query handler fields, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowQueryHandlerFields(boolean showQueryHandlerFields) {
            this.showQueryHandlerFields$value = showQueryHandlerFields;
            return this;
        }

        /**
         * Sets whether to show query handler methods in the diagram.
         *
         * @param showQueryHandlerMethods true to show query handler methods, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowQueryHandlerMethods(boolean showQueryHandlerMethods) {
            this.showQueryHandlerMethods$value = showQueryHandlerMethods;
            return this;
        }

        /**
         * Sets whether to show outbound services in the diagram.
         *
         * @param showOutboundServices true to show outbound services, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowOutboundServices(boolean showOutboundServices) {
            this.showOutboundServices$value = showOutboundServices;
            return this;
        }

        /**
         * Sets whether to show outbound service fields in the diagram.
         *
         * @param showOutboundServiceFields true to show outbound service fields, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowOutboundServiceFields(boolean showOutboundServiceFields) {
            this.showOutboundServiceFields$value = showOutboundServiceFields;
            return this;
        }

        /**
         * Sets whether to show outbound service methods in the diagram.
         *
         * @param showOutboundServiceMethods true to show outbound service methods, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowOutboundServiceMethods(boolean showOutboundServiceMethods) {
            this.showOutboundServiceMethods$value = showOutboundServiceMethods;
            return this;
        }

        /**
         * Sets whether to show unspecified service kinds in the diagram.
         *
         * @param showUnspecifiedServiceKinds true to show unspecified service kinds, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowUnspecifiedServiceKinds(boolean showUnspecifiedServiceKinds) {
            this.showUnspecifiedServiceKinds$value = showUnspecifiedServiceKinds;
            return this;
        }

        /**
         * Sets whether to show unspecified service kind fields in the diagram.
         *
         * @param showUnspecifiedServiceKindFields true to show unspecified service kind fields, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowUnspecifiedServiceKindFields(boolean showUnspecifiedServiceKindFields) {
            this.showUnspecifiedServiceKindFields$value = showUnspecifiedServiceKindFields;
            return this;
        }

        /**
         * Sets whether to show unspecified service kind methods in the diagram.
         *
         * @param showUnspecifiedServiceKindMethods true to show unspecified service kind methods, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowUnspecifiedServiceKindMethods(boolean showUnspecifiedServiceKindMethods) {
            this.showUnspecifiedServiceKindMethods$value = showUnspecifiedServiceKindMethods;
            return this;
        }

        /**
         * Sets whether to call application service driver.
         *
         * @param callApplicationServiceDriver true to enable calling application service driver, false to disable
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withCallApplicationServiceDriver(boolean callApplicationServiceDriver) {
            this.callApplicationServiceDriver$value = callApplicationServiceDriver;
            return this;
        }

        /**
         * Sets the list of fields to be excluded from the diagram.
         *
         * @param fieldBlacklist list of field names to exclude
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withFieldBlacklist(List<String> fieldBlacklist) {
            this.fieldBlacklist$value = Collections.unmodifiableList(new java.util.ArrayList<>(fieldBlacklist));
            return this;
        }

        /**
         * Sets the list of methods to be excluded from the diagram.
         *
         * @param methodBlacklist list of method names to exclude
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withMethodBlacklist(List<String> methodBlacklist) {
            this.methodBlacklist$value = Collections.unmodifiableList(new java.util.ArrayList<>(methodBlacklist));
            return this;
        }

        /**
         * Sets whether to show inherited members in classes in the diagram.
         *
         * @param showInheritedMembersInClasses true to show inherited members, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowInheritedMembersInClasses(boolean showInheritedMembersInClasses) {
            this.showInheritedMembersInClasses$value = showInheritedMembersInClasses;
            return this;
        }

        /**
         * Sets whether to show Object class members in classes in the diagram.
         *
         * @param showObjectMembersInClasses true to show Object members, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowObjectMembersInClasses(boolean showObjectMembersInClasses) {
            this.showObjectMembersInClasses$value = showObjectMembersInClasses;
            return this;
        }

        /**
         * Sets whether to show multiplicity in labels in the diagram.
         *
         * @param multiplicityInLabel true to show multiplicity in labels, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withMultiplicityInLabel(boolean multiplicityInLabel) {
            this.multiplicityInLabel$value = multiplicityInLabel;
            return this;
        }

        /**
         * Sets whether to show field stereotypes in the diagram.
         *
         * @param fieldStereotypes true to show field stereotypes, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withFieldStereotypes(boolean fieldStereotypes) {
            this.fieldStereotypes$value = fieldStereotypes;
            return this;
        }

        /**
         * Sets whether to show inheritance structures in the diagram.
         *
         * @param showAllInheritanceStructures true to show all inheritance structures, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowAllInheritanceStructures(boolean showAllInheritanceStructures) {
            this.showAllInheritanceStructures$value = showAllInheritanceStructures;
            return this;
        }

        /**
         * Sets whether inheritance structures should be displayed in aggregates.
         *
         * @param showInheritanceStructuresInAggregates a boolean flag indicating whether inheritance structures
         *                                      should be included in the display of aggregates
         * @return the current instance of {@code GeneralVisualSettingsBuilder} for method chaining
         */
        public GeneralVisualSettingsBuilder withShowInheritanceStructuresInAggregates(boolean showInheritanceStructuresInAggregates) {
            this.showInheritanceStructuresInAggregates$value = showInheritanceStructuresInAggregates;
            return this;
        }

        /**
         * Sets whether to show inheritance structures for service kinds in the diagram.
         *
         * @param showInheritanceStructuresForServiceKinds true to show inheritance structures for service kinds, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowInheritanceStructuresForServiceKinds(boolean showInheritanceStructuresForServiceKinds) {
            this.showInheritanceStructuresForServiceKinds$value = showInheritanceStructuresForServiceKinds;
            return this;
        }

        /**
         * Sets whether to show inheritance structures for read models in the diagram.
         *
         * @param showInheritanceStructuresForReadModels true to show inheritance structures for read models, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowInheritanceStructuresForReadModels(boolean showInheritanceStructuresForReadModels) {
            this.showInheritanceStructuresForReadModels$value = showInheritanceStructuresForReadModels;
            return this;
        }

        /**
         * Sets whether to show inheritance structures for domain events in the diagram.
         *
         * @param showInheritanceStructuresForDomainEvents true to show inheritance structures for domain events, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowInheritanceStructuresForDomainEvents(boolean showInheritanceStructuresForDomainEvents) {
            this.showInheritanceStructuresForDomainEvents$value = showInheritanceStructuresForDomainEvents;
            return this;
        }

        /**
         * Sets whether to show inheritance structures for domain commands in the diagram.
         *
         * @param showInheritanceStructuresForDomainCommands true to show inheritance structures for domain commands, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowInheritanceStructuresForDomainCommands(boolean showInheritanceStructuresForDomainCommands) {
            this.showInheritanceStructuresForDomainCommands$value = showInheritanceStructuresForDomainCommands;
            return this;
        }

        /**
         * Sets whether to show notes in the diagram.
         *
         * @param showNotes true to show notes, false to hide
         * @return this builder instance
         */
        public GeneralVisualSettingsBuilder withShowNotes(boolean showNotes) {
            this.showNotes$value = showNotes;
            return this;
        }

        /**
         * Constructs a new instance of the {@code GeneralVisualSettings} class initialized with
         * the current state of the builder. The method uses the configured attributes in this builder
         * to create a fully constructed {@code GeneralVisualSettings} object.
         *
         * @return a new {@code GeneralVisualSettings} instance containing the configured visual settings.
         */
        public GeneralVisualSettings build() {
            return new GeneralVisualSettings(
                showFields$value,
                showFullQualifiedClassNames$value,
                showAssertions$value,
                showMethods$value,
                showOnlyPublicMethods$value,
                showAggregates$value,
                showAggregateFields$value,
                showAggregateMethods$value,
                showDomainEvents$value,
                showDomainEventFields$value,
                showDomainEventMethods$value,
                showDomainCommands$value,
                showOnlyTopLevelDomainCommandRelations$value,
                showDomainCommandFields$value,
                showDomainCommandMethods$value,
                showDomainServices$value,
                showDomainServiceFields$value,
                showDomainServiceMethods$value,
                showApplicationServices$value,
                showApplicationServiceFields$value,
                showApplicationServiceMethods$value,
                showRepositories$value,
                showRepositoryFields$value,
                showRepositoryMethods$value,
                showReadModels$value,
                showReadModelFields$value,
                showReadModelMethods$value,
                showQueryHandlers$value,
                showQueryHandlerFields$value,
                showQueryHandlerMethods$value,
                showOutboundServices$value,
                showOutboundServiceFields$value,
                showOutboundServiceMethods$value,
                showUnspecifiedServiceKinds$value,
                showUnspecifiedServiceKindFields$value,
                showUnspecifiedServiceKindMethods$value,
                callApplicationServiceDriver$value,
                fieldBlacklist$value == null ? DEFAULT_FIELD_BLACKLIST : fieldBlacklist$value,
                methodBlacklist$value == null ? DEFAULT_METHOD_BLACKLIST : methodBlacklist$value,
                showInheritedMembersInClasses$value,
                showObjectMembersInClasses$value,
                multiplicityInLabel$value,
                fieldStereotypes$value,
                showAllInheritanceStructures$value,
                showInheritanceStructuresInAggregates$value,
                showInheritanceStructuresForServiceKinds$value,
                showInheritanceStructuresForReadModels$value,
                showInheritanceStructuresForDomainEvents$value,
                showInheritanceStructuresForDomainCommands$value,
                showNotes$value
            );
        }
    }

}
