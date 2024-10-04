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

package io.domainlifecycles.diagram.domain.config;

import io.domainlifecycles.diagram.DiagramConfig;
import io.domainlifecycles.diagram.domain.mapper.TransitiveDomainTypeFilter;

import java.util.Collections;
import java.util.List;

/**
 * Configuration options for a Domain Diagram
 *
 * @author Mario Herb
 */
public class DomainDiagramConfig implements DiagramConfig {

    /**
     * A DomainDiagram is meant focus on domain elements which are contained in one specific package (e.g. bounded context).
     * The package name must be specified.
     */
    private String contextPackageName = null;

    /**
     * Style declaration for AggregateRoots (see Nomnoml style options)
     */
    private String aggregateRootStyle = "fill=#8f8f bold";
    /**
     * Style declaration for AggregateRoot frames (see Nomnoml style options)
     */
    private String aggregateFrameStyle = "visual=frame align=left";
    /**
     * Style declaration for Entities  (see Nomnoml style options)
     */
    private String entityStyle = "fill=#88AAFF bold";
    /**
     * Style declaration for ValueObjects  (see Nomnoml style options)
     */
    private String valueObjectStyle = "fill=#FFFFCC bold";
    /**
     * Style declaration for Enums  (see Nomnoml style options)
     */
    private String enumStyle = "fill=#FFFFCC bold";
    /**
     * Style declaration for Identities  (see Nomnoml style options)
     */
    private String identityStyle = "fill=#FFFFCC bold";
    /**
     * Style declaration for DomainEvents  (see Nomnoml style options)
     */
    private String domainEventStyle = "fill=#CCFFFF bold";
    /**
     * Style declaration for DomainCommands  (see Nomnoml style options)
     */
    private String domainCommandStyle = "fill=#FFB266 bold";
    /**
     * Style declaration for ApplicationServices (see Nomnoml style options)
     */
    private String applicationServiceStyle = "bold";
    /**
     * Style declaration for DomainServices  (see Nomnoml style options)
     */
    private String domainServiceStyle = "fill=#E0E0E0 bold";
    /**
     * Style declaration for Repositories  (see Nomnoml style options)
     */
    private String repositoryStyle = "fill=#C0C0C0 bold";
    /**
     * Style declaration for ReadModels  (see Nomnoml style options)
     */
    private String readModelStyle = "fill=#FFCCE5 bold";
    /**
     * Style declaration for QueryClients  (see Nomnoml style options)
     */
    private String queryClientStyle = "fill=#C0C0C0 bold";
    /**
     * Style declaration for OutboundServices  (see Nomnoml style options)
     */
    private String outboundServiceStyle = "fill=#C0C0C0 bold";
    /**
     * General font style declaration  (see Nomnoml style options)
     */
    private String font = "Courier";
    /**
     * General layout direction style declaration (see Nomnoml style options, 'down' or 'right' is supported)
     */
    private String direction = "down";

    /**
     * General layout direction style declaration (see Nomnoml style options, 'network-simplex' or 'tight-tree' or 'longest-path' is supported)
     */
    private String ranker = "longest-path";

    /**
     * General acycling style declaration  (see Nomnoml style options, only 'greedy' supported)
     */
    private String acycler = "greedy";

    /**
     * Full qualified class names to be excluded from the diagram
     */
    private List<String> classesBlacklist = Collections.emptyList();
    /**
     * If false, generally no fields are included
     */
    private boolean showFields = true;
    /**
     * If true, generally full qualified class names are used
     */
    private boolean showFullQualifiedClassNames = false;
    /**
     * If true, assertions (currently only if specified by Bean Validation Annotations) are included.
     */
    private boolean showAssertions = true;
    /**
     * If false, generally no methods are included
     */
    private boolean showMethods = true;
    /**
     * If true, generally only public methods are included
     */
    private boolean showOnlyPublicMethods = true;
    /**
     * If true, DomainEvent classes are included
     */
    private boolean showDomainEvents = true;
    /**
     * If true, fields of DomainEvents are included
     */
    private boolean showDomainEventFields = false;
    /**
     * If true, methods of DomainEvents are included
     */
    private boolean showDomainEventMethods = false;
    /**
     * If true, DomainCommand classes are included
     */
    private boolean showDomainCommands = true;
    /**
     * DomainCommands might be passed down to subsequent classes in the flow.
     * If true, DomainCommands relations are only drawn on the top most processing class.
     */
    private boolean showOnlyTopLevelDomainCommandRelations = true;
    /**
     * If true, fields of DomainCommands are included
     */
    private boolean showDomainCommandFields = false;
    /**
     * If true, methods of DomainCommands are included
     */
    private boolean showDomainCommandMethods = false;
    /**
     * If true, DomainService classes are included
     */
    private boolean showDomainServices = true;
    /**
     * If true, fields of DomainServices are included
     */
    private boolean showDomainServiceFields = false;
    /**
     * If true, methods of DomainServices are included
     */
    private boolean showDomainServiceMethods = true;
    /**
     * If true, ApplicationService/Driver classes are included
     */
    private boolean showApplicationServices = true;
    /**
     * If true, fields of ApplicationServices/Drivers are included
     */
    private boolean showApplicationServiceFields = false;
    /**
     * If true, methods of ApplicationServices/Drivers are included
     */
    private boolean showApplicationServiceMethods = true;
    /**
     * If true, Repository classes are included
     */
    private boolean showRepositories = true;
    /**
     * If true, fields of Repositories are included
     */
    private boolean showRepositoryFields = false;
    /**
     * If true, methods of Repositories are included
     */
    private boolean showRepositoryMethods = true;
    /**
     * If true, ReadModel classes are included
     */
    private boolean showReadModels = true;
    /**
     * If true, fields of ReadModels are included
     */
    private boolean showReadModelFields = true;
    /**
     * If true, methods of ReadModels are included
     */
    private boolean showReadModelMethods = false;
    /**
     * If true, QueryClient classes are included
     */
    private boolean showQueryClients = true;
    /**
     * If true, fields of QueryClients are included
     */
    private boolean showQueryClientFields = false;
    /**
     * If true, methods of QueryClients are included
     */
    private boolean showQueryClientMethods = false;
    /**
     * If true, OutboundService classes are included
     */
    private boolean showOutboundServices = true;
    /**
     * If true, fields of OutboundServices are included
     */
    private boolean showOutboundServiceFields = false;
    /**
     * If true, methods of OutboundServices are included
     */
    private boolean showOutboundServiceMethods = false;

    /**
     * If true, the stereotype {@code <Driver>} is used instead of {@code <ApplicationService>}
     */
    private boolean callApplicationServiceDriver = true;

    /**
     * Fields with named like elements of this black list are excluded
     */
    private List<String> fieldBlacklist = List.of("concurrencyVersion");

    /**
     * Methods with named like elements of this black list are excluded
     */
    private List<String> methodBlacklist = List.of(
        "builder",
        "validate",
        "concurrencyVersion",
        "id",
        "findResultById",
        "publish",
        "increaseVersion",
        "equals",
        "hashCode",
        "toString");

    /**
     * If true, members declared by inherited classes are included
     */
    private boolean showInheritedMembersInClasses = true;
    /**
     * If true, members declared by {@code java.lang.Object} are included
     */
    private boolean showObjectMembersInClasses = true;

    /**
     * If true, multiplicity is added to the associations label.
     */
    private boolean multiplicityInLabel = true;

    /**
     * Show field 'stereotypes' like {@code <ID>, <ENUM, <IDREF> or <VO>}, indicating
     * what kind of field it is. This can bring clarity when dealing with complex domain models:
     * <ul>
     *   <li>{@code <ID>}: Represents a unique identifier for an Entity in the domain.</li>
     *   <li>{@code <ENUM>}: Indicates that the field is an enumeration, a distinct type that consists of a
     *       set of named constants.</li>
     *   <li>{@code <IDREF>}: Denotes an identifier reference field that holds a reference to another AggregateRoot.</li>
     *   <li>{@code <VO>}: Signifies a value object.</li>
     * </ul>
     */
    private boolean fieldStereotypes = true;

    /**
     * Enabling and initializing the seed for the {@link TransitiveDomainTypeFilter}.
     */
    private List<String> transitiveFilterSeedDomainServiceTypeNames = Collections.emptyList();

    DomainDiagramConfig(String contextPackageName, String aggregateRootStyle, String aggregateFrameStyle, String entityStyle, String valueObjectStyle, String enumStyle, String identityStyle, String domainEventStyle, String domainCommandStyle, String applicationServiceStyle, String domainServiceStyle, String repositoryStyle, String readModelStyle, String queryClientStyle, String outboundServiceStyle, String font, String direction, String ranker, String acycler, List<String> classesBlacklist, boolean showFields, boolean showFullQualifiedClassNames, boolean showAssertions, boolean showMethods, boolean showOnlyPublicMethods, boolean showDomainEvents, boolean showDomainEventFields, boolean showDomainEventMethods, boolean showDomainCommands, boolean showOnlyTopLevelDomainCommandRelations, boolean showDomainCommandFields, boolean showDomainCommandMethods, boolean showDomainServices, boolean showDomainServiceFields, boolean showDomainServiceMethods, boolean showApplicationServices, boolean showApplicationServiceFields, boolean showApplicationServiceMethods, boolean showRepositories, boolean showRepositoryFields, boolean showRepositoryMethods, boolean showReadModels, boolean showReadModelFields, boolean showReadModelMethods, boolean showQueryClients, boolean showQueryClientFields, boolean showQueryClientMethods, boolean showOutboundServices, boolean showOutboundServiceFields, boolean showOutboundServiceMethods, boolean callApplicationServiceDriver, List<String> fieldBlacklist, List<String> methodBlacklist, boolean showInheritedMembersInClasses, boolean showObjectMembersInClasses, boolean multiplicityInLabel, boolean fieldStereotypes, List<String> transitiveFilterSeedDomainServiceTypeNames) {
        this.contextPackageName = contextPackageName;
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
        this.queryClientStyle = queryClientStyle;
        this.outboundServiceStyle = outboundServiceStyle;
        this.font = font;
        this.direction = direction;
        this.ranker = ranker;
        this.acycler = acycler;
        this.classesBlacklist = classesBlacklist;
        this.showFields = showFields;
        this.showFullQualifiedClassNames = showFullQualifiedClassNames;
        this.showAssertions = showAssertions;
        this.showMethods = showMethods;
        this.showOnlyPublicMethods = showOnlyPublicMethods;
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
        this.showQueryClients = showQueryClients;
        this.showQueryClientFields = showQueryClientFields;
        this.showQueryClientMethods = showQueryClientMethods;
        this.showOutboundServices = showOutboundServices;
        this.showOutboundServiceFields = showOutboundServiceFields;
        this.showOutboundServiceMethods = showOutboundServiceMethods;
        this.callApplicationServiceDriver = callApplicationServiceDriver;
        this.fieldBlacklist = fieldBlacklist;
        this.methodBlacklist = methodBlacklist;
        this.showInheritedMembersInClasses = showInheritedMembersInClasses;
        this.showObjectMembersInClasses = showObjectMembersInClasses;
        this.multiplicityInLabel = multiplicityInLabel;
        this.fieldStereotypes = fieldStereotypes;
        this.transitiveFilterSeedDomainServiceTypeNames = transitiveFilterSeedDomainServiceTypeNames;
    }

    private static String $default$contextPackageName() {
        return null;
    }

    private static String $default$aggregateRootStyle() {
        return "fill=#8f8f bold";
    }

    private static String $default$aggregateFrameStyle() {
        return "visual=frame align=left";
    }

    private static String $default$entityStyle() {
        return "fill=#88AAFF bold";
    }

    private static String $default$valueObjectStyle() {
        return "fill=#FFFFCC bold";
    }

    private static String $default$enumStyle() {
        return "fill=#FFFFCC bold";
    }

    private static String $default$identityStyle() {
        return "fill=#FFFFCC bold";
    }

    private static String $default$domainEventStyle() {
        return "fill=#CCFFFF bold";
    }

    private static String $default$domainCommandStyle() {
        return "fill=#FFB266 bold";
    }

    private static String $default$applicationServiceStyle() {
        return "bold";
    }

    private static String $default$domainServiceStyle() {
        return "fill=#E0E0E0 bold";
    }

    private static String $default$repositoryStyle() {
        return "fill=#C0C0C0 bold";
    }

    private static String $default$readModelStyle() {
        return "fill=#FFCCE5 bold";
    }

    private static String $default$queryClientStyle() {
        return "fill=#C0C0C0 bold";
    }

    private static String $default$outboundServiceStyle() {
        return "fill=#C0C0C0 bold";
    }

    private static String $default$font() {
        return "Courier";
    }

    private static String $default$direction() {
        return "down";
    }

    private static String $default$ranker() {
        return "longest-path";
    }

    private static String $default$acycler() {
        return "greedy";
    }

    private static List<String> $default$classesBlacklist() {
        return Collections.emptyList();
    }

    private static boolean $default$showFields() {
        return true;
    }

    private static boolean $default$showFullQualifiedClassNames() {
        return false;
    }

    private static boolean $default$showAssertions() {
        return true;
    }

    private static boolean $default$showMethods() {
        return true;
    }

    private static boolean $default$showOnlyPublicMethods() {
        return true;
    }

    private static boolean $default$showDomainEvents() {
        return true;
    }

    private static boolean $default$showDomainEventFields() {
        return false;
    }

    private static boolean $default$showDomainEventMethods() {
        return false;
    }

    private static boolean $default$showDomainCommands() {
        return true;
    }

    private static boolean $default$showOnlyTopLevelDomainCommandRelations() {
        return true;
    }

    private static boolean $default$showDomainCommandFields() {
        return false;
    }

    private static boolean $default$showDomainCommandMethods() {
        return false;
    }

    private static boolean $default$showDomainServices() {
        return true;
    }

    private static boolean $default$showDomainServiceFields() {
        return false;
    }

    private static boolean $default$showDomainServiceMethods() {
        return true;
    }

    private static boolean $default$showApplicationServices() {
        return true;
    }

    private static boolean $default$showApplicationServiceFields() {
        return false;
    }

    private static boolean $default$showApplicationServiceMethods() {
        return true;
    }

    private static boolean $default$showRepositories() {
        return true;
    }

    private static boolean $default$showRepositoryFields() {
        return false;
    }

    private static boolean $default$showRepositoryMethods() {
        return true;
    }

    private static boolean $default$showReadModels() {
        return true;
    }

    private static boolean $default$showReadModelFields() {
        return true;
    }

    private static boolean $default$showReadModelMethods() {
        return false;
    }

    private static boolean $default$showQueryClients() {
        return true;
    }

    private static boolean $default$showQueryClientFields() {
        return false;
    }

    private static boolean $default$showQueryClientMethods() {
        return false;
    }

    private static boolean $default$showOutboundServices() {
        return true;
    }

    private static boolean $default$showOutboundServiceFields() {
        return false;
    }

    private static boolean $default$showOutboundServiceMethods() {
        return false;
    }

    private static boolean $default$callApplicationServiceDriver() {
        return true;
    }

    private static List<String> $default$fieldBlacklist() {
        return List.of("concurrencyVersion");
    }

    private static List<String> $default$methodBlacklist() {
        return List.of(
            "builder",
            "validate",
            "concurrencyVersion",
            "id",
            "findResultById",
            "publish",
            "increaseVersion",
            "equals",
            "hashCode",
            "toString");
    }

    private static boolean $default$showInheritedMembersInClasses() {
        return true;
    }

    private static boolean $default$showObjectMembersInClasses() {
        return true;
    }

    private static boolean $default$multiplicityInLabel() {
        return true;
    }

    private static boolean $default$fieldStereotypes() {
        return true;
    }

    private static List<String> $default$transitiveFilterSeedDomainServiceTypeNames() {
        return Collections.emptyList();
    }

    public static DomainDiagramConfigBuilder builder() {
        return new DomainDiagramConfigBuilder();
    }


    public String getContextPackageName() {
        return this.contextPackageName;
    }

    public String getAggregateRootStyle() {
        return this.aggregateRootStyle;
    }

    public String getAggregateFrameStyle() {
        return this.aggregateFrameStyle;
    }

    public String getEntityStyle() {
        return this.entityStyle;
    }

    public String getValueObjectStyle() {
        return this.valueObjectStyle;
    }

    public String getEnumStyle() {
        return this.enumStyle;
    }

    public String getIdentityStyle() {
        return this.identityStyle;
    }

    public String getDomainEventStyle() {
        return this.domainEventStyle;
    }

    public String getDomainCommandStyle() {
        return this.domainCommandStyle;
    }

    public String getApplicationServiceStyle() {
        return this.applicationServiceStyle;
    }

    public String getDomainServiceStyle() {
        return this.domainServiceStyle;
    }

    public String getRepositoryStyle() {
        return this.repositoryStyle;
    }

    public String getReadModelStyle() {
        return this.readModelStyle;
    }

    public String getQueryClientStyle() {
        return this.queryClientStyle;
    }

    public String getOutboundServiceStyle() {
        return this.outboundServiceStyle;
    }

    public String getFont() {
        return this.font;
    }

    public String getDirection() {
        return this.direction;
    }

    public String getRanker() {
        return this.ranker;
    }

    public String getAcycler() {
        return this.acycler;
    }

    public List<String> getClassesBlacklist() {
        return this.classesBlacklist;
    }

    public boolean isShowFields() {
        return this.showFields;
    }

    public boolean isShowFullQualifiedClassNames() {
        return this.showFullQualifiedClassNames;
    }

    public boolean isShowAssertions() {
        return this.showAssertions;
    }

    public boolean isShowMethods() {
        return this.showMethods;
    }

    public boolean isShowOnlyPublicMethods() {
        return this.showOnlyPublicMethods;
    }

    public boolean isShowDomainEvents() {
        return this.showDomainEvents;
    }

    public boolean isShowDomainEventFields() {
        return this.showDomainEventFields;
    }

    public boolean isShowDomainEventMethods() {
        return this.showDomainEventMethods;
    }

    public boolean isShowDomainCommands() {
        return this.showDomainCommands;
    }

    public boolean isShowOnlyTopLevelDomainCommandRelations() {
        return this.showOnlyTopLevelDomainCommandRelations;
    }

    public boolean isShowDomainCommandFields() {
        return this.showDomainCommandFields;
    }

    public boolean isShowDomainCommandMethods() {
        return this.showDomainCommandMethods;
    }

    public boolean isShowDomainServices() {
        return this.showDomainServices;
    }

    public boolean isShowDomainServiceFields() {
        return this.showDomainServiceFields;
    }

    public boolean isShowDomainServiceMethods() {
        return this.showDomainServiceMethods;
    }

    public boolean isShowApplicationServices() {
        return this.showApplicationServices;
    }

    public boolean isShowApplicationServiceFields() {
        return this.showApplicationServiceFields;
    }

    public boolean isShowApplicationServiceMethods() {
        return this.showApplicationServiceMethods;
    }

    public boolean isShowRepositories() {
        return this.showRepositories;
    }

    public boolean isShowRepositoryFields() {
        return this.showRepositoryFields;
    }

    public boolean isShowRepositoryMethods() {
        return this.showRepositoryMethods;
    }

    public boolean isShowReadModels() {
        return this.showReadModels;
    }

    public boolean isShowReadModelFields() {
        return this.showReadModelFields;
    }

    public boolean isShowReadModelMethods() {
        return this.showReadModelMethods;
    }

    public boolean isShowQueryClients() {
        return this.showQueryClients;
    }

    public boolean isShowQueryClientFields() {
        return this.showQueryClientFields;
    }

    public boolean isShowQueryClientMethods() {
        return this.showQueryClientMethods;
    }

    public boolean isShowOutboundServices() {
        return this.showOutboundServices;
    }

    public boolean isShowOutboundServiceFields() {
        return this.showOutboundServiceFields;
    }

    public boolean isShowOutboundServiceMethods() {
        return this.showOutboundServiceMethods;
    }

    public boolean isCallApplicationServiceDriver() {
        return this.callApplicationServiceDriver;
    }

    public List<String> getFieldBlacklist() {
        return this.fieldBlacklist;
    }

    public List<String> getMethodBlacklist() {
        return this.methodBlacklist;
    }

    public boolean isShowInheritedMembersInClasses() {
        return this.showInheritedMembersInClasses;
    }

    public boolean isShowObjectMembersInClasses() {
        return this.showObjectMembersInClasses;
    }

    public boolean isMultiplicityInLabel() {
        return this.multiplicityInLabel;
    }

    public boolean isFieldStereotypes() {
        return this.fieldStereotypes;
    }

    public List<String> getTransitiveFilterSeedDomainServiceTypeNames() {
        return this.transitiveFilterSeedDomainServiceTypeNames;
    }

    public DomainDiagramConfigBuilder toBuilder() {
        return new DomainDiagramConfigBuilder().withContextPackageName(this.contextPackageName).withAggregateRootStyle(this.aggregateRootStyle).withAggregateFrameStyle(this.aggregateFrameStyle).withEntityStyle(this.entityStyle).withValueObjectStyle(this.valueObjectStyle).withEnumStyle(this.enumStyle).withIdentityStyle(this.identityStyle).withDomainEventStyle(this.domainEventStyle).withDomainCommandStyle(this.domainCommandStyle).withApplicationServiceStyle(this.applicationServiceStyle).withDomainServiceStyle(this.domainServiceStyle).withRepositoryStyle(this.repositoryStyle).withReadModelStyle(this.readModelStyle).withQueryClientStyle(this.queryClientStyle).withOutboundServiceStyle(this.outboundServiceStyle).withFont(this.font).withDirection(this.direction).withRanker(this.ranker).withAcycler(this.acycler).withClassesBlacklist(this.classesBlacklist).withShowFields(this.showFields).withShowFullQualifiedClassNames(this.showFullQualifiedClassNames).withShowAssertions(this.showAssertions).withShowMethods(this.showMethods).withShowOnlyPublicMethods(this.showOnlyPublicMethods).withShowDomainEvents(this.showDomainEvents).withShowDomainEventFields(this.showDomainEventFields).withShowDomainEventMethods(this.showDomainEventMethods).withShowDomainCommands(this.showDomainCommands).withShowOnlyTopLevelDomainCommandRelations(this.showOnlyTopLevelDomainCommandRelations).withShowDomainCommandFields(this.showDomainCommandFields).withShowDomainCommandMethods(this.showDomainCommandMethods).withShowDomainServices(this.showDomainServices).withShowDomainServiceFields(this.showDomainServiceFields).withShowDomainServiceMethods(this.showDomainServiceMethods).withShowApplicationServices(this.showApplicationServices).withShowApplicationServiceFields(this.showApplicationServiceFields).withShowApplicationServiceMethods(this.showApplicationServiceMethods).withShowRepositories(this.showRepositories).withShowRepositoryFields(this.showRepositoryFields).withShowRepositoryMethods(this.showRepositoryMethods).withShowReadModels(this.showReadModels).withShowReadModelFields(this.showReadModelFields).withShowReadModelMethods(this.showReadModelMethods).withShowQueryClients(this.showQueryClients).withShowQueryClientFields(this.showQueryClientFields).withShowQueryClientMethods(this.showQueryClientMethods).withShowOutboundServices(this.showOutboundServices).withShowOutboundServiceFields(this.showOutboundServiceFields).withShowOutboundServiceMethods(this.showOutboundServiceMethods).withCallApplicationServiceDriver(this.callApplicationServiceDriver).withFieldBlacklist(this.fieldBlacklist).withMethodBlacklist(this.methodBlacklist).withShowInheritedMembersInClasses(this.showInheritedMembersInClasses).withShowObjectMembersInClasses(this.showObjectMembersInClasses).withMultiplicityInLabel(this.multiplicityInLabel).withFieldStereotypes(this.fieldStereotypes).withTransitiveFilterSeedDomainServiceTypeNames(this.transitiveFilterSeedDomainServiceTypeNames);
    }

    public static class DomainDiagramConfigBuilder {
        private String contextPackageName$value;
        private boolean contextPackageName$set;
        private String aggregateRootStyle$value;
        private boolean aggregateRootStyle$set;
        private String aggregateFrameStyle$value;
        private boolean aggregateFrameStyle$set;
        private String entityStyle$value;
        private boolean entityStyle$set;
        private String valueObjectStyle$value;
        private boolean valueObjectStyle$set;
        private String enumStyle$value;
        private boolean enumStyle$set;
        private String identityStyle$value;
        private boolean identityStyle$set;
        private String domainEventStyle$value;
        private boolean domainEventStyle$set;
        private String domainCommandStyle$value;
        private boolean domainCommandStyle$set;
        private String applicationServiceStyle$value;
        private boolean applicationServiceStyle$set;
        private String domainServiceStyle$value;
        private boolean domainServiceStyle$set;
        private String repositoryStyle$value;
        private boolean repositoryStyle$set;
        private String readModelStyle$value;
        private boolean readModelStyle$set;
        private String queryClientStyle$value;
        private boolean queryClientStyle$set;
        private String outboundServiceStyle$value;
        private boolean outboundServiceStyle$set;
        private String font$value;
        private boolean font$set;
        private String direction$value;
        private boolean direction$set;
        private String ranker$value;
        private boolean ranker$set;
        private String acycler$value;
        private boolean acycler$set;
        private List<String> classesBlacklist$value;
        private boolean classesBlacklist$set;
        private boolean showFields$value;
        private boolean showFields$set;
        private boolean showFullQualifiedClassNames$value;
        private boolean showFullQualifiedClassNames$set;
        private boolean showAssertions$value;
        private boolean showAssertions$set;
        private boolean showMethods$value;
        private boolean showMethods$set;
        private boolean showOnlyPublicMethods$value;
        private boolean showOnlyPublicMethods$set;
        private boolean showDomainEvents$value;
        private boolean showDomainEvents$set;
        private boolean showDomainEventFields$value;
        private boolean showDomainEventFields$set;
        private boolean showDomainEventMethods$value;
        private boolean showDomainEventMethods$set;
        private boolean showDomainCommands$value;
        private boolean showDomainCommands$set;
        private boolean showOnlyTopLevelDomainCommandRelations$value;
        private boolean showOnlyTopLevelDomainCommandRelations$set;
        private boolean showDomainCommandFields$value;
        private boolean showDomainCommandFields$set;
        private boolean showDomainCommandMethods$value;
        private boolean showDomainCommandMethods$set;
        private boolean showDomainServices$value;
        private boolean showDomainServices$set;
        private boolean showDomainServiceFields$value;
        private boolean showDomainServiceFields$set;
        private boolean showDomainServiceMethods$value;
        private boolean showDomainServiceMethods$set;
        private boolean showApplicationServices$value;
        private boolean showApplicationServices$set;
        private boolean showApplicationServiceFields$value;
        private boolean showApplicationServiceFields$set;
        private boolean showApplicationServiceMethods$value;
        private boolean showApplicationServiceMethods$set;
        private boolean showRepositories$value;
        private boolean showRepositories$set;
        private boolean showRepositoryFields$value;
        private boolean showRepositoryFields$set;
        private boolean showRepositoryMethods$value;
        private boolean showRepositoryMethods$set;
        private boolean showReadModels$value;
        private boolean showReadModels$set;
        private boolean showReadModelFields$value;
        private boolean showReadModelFields$set;
        private boolean showReadModelMethods$value;
        private boolean showReadModelMethods$set;
        private boolean showQueryClients$value;
        private boolean showQueryClients$set;
        private boolean showQueryClientFields$value;
        private boolean showQueryClientFields$set;
        private boolean showQueryClientMethods$value;
        private boolean showQueryClientMethods$set;
        private boolean showOutboundServices$value;
        private boolean showOutboundServices$set;
        private boolean showOutboundServiceFields$value;
        private boolean showOutboundServiceFields$set;
        private boolean showOutboundServiceMethods$value;
        private boolean showOutboundServiceMethods$set;
        private boolean callApplicationServiceDriver$value;
        private boolean callApplicationServiceDriver$set;
        private List<String> fieldBlacklist$value;
        private boolean fieldBlacklist$set;
        private List<String> methodBlacklist$value;
        private boolean methodBlacklist$set;
        private boolean showInheritedMembersInClasses$value;
        private boolean showInheritedMembersInClasses$set;
        private boolean showObjectMembersInClasses$value;
        private boolean showObjectMembersInClasses$set;
        private boolean multiplicityInLabel$value;
        private boolean multiplicityInLabel$set;
        private boolean fieldStereotypes$value;
        private boolean fieldStereotypes$set;
        private List<String> transitiveFilterSeedDomainServiceTypeNames$value;
        private boolean transitiveFilterSeedDomainServiceTypeNames$set;

        DomainDiagramConfigBuilder() {
        }

        public DomainDiagramConfigBuilder withContextPackageName(String contextPackageName) {
            this.contextPackageName$value = contextPackageName;
            this.contextPackageName$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withAggregateRootStyle(String aggregateRootStyle) {
            this.aggregateRootStyle$value = aggregateRootStyle;
            this.aggregateRootStyle$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withAggregateFrameStyle(String aggregateFrameStyle) {
            this.aggregateFrameStyle$value = aggregateFrameStyle;
            this.aggregateFrameStyle$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withEntityStyle(String entityStyle) {
            this.entityStyle$value = entityStyle;
            this.entityStyle$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withValueObjectStyle(String valueObjectStyle) {
            this.valueObjectStyle$value = valueObjectStyle;
            this.valueObjectStyle$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withEnumStyle(String enumStyle) {
            this.enumStyle$value = enumStyle;
            this.enumStyle$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withIdentityStyle(String identityStyle) {
            this.identityStyle$value = identityStyle;
            this.identityStyle$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withDomainEventStyle(String domainEventStyle) {
            this.domainEventStyle$value = domainEventStyle;
            this.domainEventStyle$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withDomainCommandStyle(String domainCommandStyle) {
            this.domainCommandStyle$value = domainCommandStyle;
            this.domainCommandStyle$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withApplicationServiceStyle(String applicationServiceStyle) {
            this.applicationServiceStyle$value = applicationServiceStyle;
            this.applicationServiceStyle$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withDomainServiceStyle(String domainServiceStyle) {
            this.domainServiceStyle$value = domainServiceStyle;
            this.domainServiceStyle$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withRepositoryStyle(String repositoryStyle) {
            this.repositoryStyle$value = repositoryStyle;
            this.repositoryStyle$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withReadModelStyle(String readModelStyle) {
            this.readModelStyle$value = readModelStyle;
            this.readModelStyle$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withQueryClientStyle(String queryClientStyle) {
            this.queryClientStyle$value = queryClientStyle;
            this.queryClientStyle$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withOutboundServiceStyle(String outboundServiceStyle) {
            this.outboundServiceStyle$value = outboundServiceStyle;
            this.outboundServiceStyle$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withFont(String font) {
            this.font$value = font;
            this.font$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withDirection(String direction) {
            this.direction$value = direction;
            this.direction$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withRanker(String ranker) {
            this.ranker$value = ranker;
            this.ranker$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withAcycler(String acycler) {
            this.acycler$value = acycler;
            this.acycler$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withClassesBlacklist(List<String> classesBlacklist) {
            this.classesBlacklist$value = classesBlacklist;
            this.classesBlacklist$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withShowFields(boolean showFields) {
            this.showFields$value = showFields;
            this.showFields$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withShowFullQualifiedClassNames(boolean showFullQualifiedClassNames) {
            this.showFullQualifiedClassNames$value = showFullQualifiedClassNames;
            this.showFullQualifiedClassNames$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withShowAssertions(boolean showAssertions) {
            this.showAssertions$value = showAssertions;
            this.showAssertions$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withShowMethods(boolean showMethods) {
            this.showMethods$value = showMethods;
            this.showMethods$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withShowOnlyPublicMethods(boolean showOnlyPublicMethods) {
            this.showOnlyPublicMethods$value = showOnlyPublicMethods;
            this.showOnlyPublicMethods$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withShowDomainEvents(boolean showDomainEvents) {
            this.showDomainEvents$value = showDomainEvents;
            this.showDomainEvents$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withShowDomainEventFields(boolean showDomainEventFields) {
            this.showDomainEventFields$value = showDomainEventFields;
            this.showDomainEventFields$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withShowDomainEventMethods(boolean showDomainEventMethods) {
            this.showDomainEventMethods$value = showDomainEventMethods;
            this.showDomainEventMethods$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withShowDomainCommands(boolean showDomainCommands) {
            this.showDomainCommands$value = showDomainCommands;
            this.showDomainCommands$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withShowOnlyTopLevelDomainCommandRelations(boolean showOnlyTopLevelDomainCommandRelations) {
            this.showOnlyTopLevelDomainCommandRelations$value = showOnlyTopLevelDomainCommandRelations;
            this.showOnlyTopLevelDomainCommandRelations$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withShowDomainCommandFields(boolean showDomainCommandFields) {
            this.showDomainCommandFields$value = showDomainCommandFields;
            this.showDomainCommandFields$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withShowDomainCommandMethods(boolean showDomainCommandMethods) {
            this.showDomainCommandMethods$value = showDomainCommandMethods;
            this.showDomainCommandMethods$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withShowDomainServices(boolean showDomainServices) {
            this.showDomainServices$value = showDomainServices;
            this.showDomainServices$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withShowDomainServiceFields(boolean showDomainServiceFields) {
            this.showDomainServiceFields$value = showDomainServiceFields;
            this.showDomainServiceFields$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withShowDomainServiceMethods(boolean showDomainServiceMethods) {
            this.showDomainServiceMethods$value = showDomainServiceMethods;
            this.showDomainServiceMethods$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withShowApplicationServices(boolean showApplicationServices) {
            this.showApplicationServices$value = showApplicationServices;
            this.showApplicationServices$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withShowApplicationServiceFields(boolean showApplicationServiceFields) {
            this.showApplicationServiceFields$value = showApplicationServiceFields;
            this.showApplicationServiceFields$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withShowApplicationServiceMethods(boolean showApplicationServiceMethods) {
            this.showApplicationServiceMethods$value = showApplicationServiceMethods;
            this.showApplicationServiceMethods$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withShowRepositories(boolean showRepositories) {
            this.showRepositories$value = showRepositories;
            this.showRepositories$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withShowRepositoryFields(boolean showRepositoryFields) {
            this.showRepositoryFields$value = showRepositoryFields;
            this.showRepositoryFields$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withShowRepositoryMethods(boolean showRepositoryMethods) {
            this.showRepositoryMethods$value = showRepositoryMethods;
            this.showRepositoryMethods$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withShowReadModels(boolean showReadModels) {
            this.showReadModels$value = showReadModels;
            this.showReadModels$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withShowReadModelFields(boolean showReadModelFields) {
            this.showReadModelFields$value = showReadModelFields;
            this.showReadModelFields$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withShowReadModelMethods(boolean showReadModelMethods) {
            this.showReadModelMethods$value = showReadModelMethods;
            this.showReadModelMethods$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withShowQueryClients(boolean showQueryClients) {
            this.showQueryClients$value = showQueryClients;
            this.showQueryClients$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withShowQueryClientFields(boolean showQueryClientFields) {
            this.showQueryClientFields$value = showQueryClientFields;
            this.showQueryClientFields$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withShowQueryClientMethods(boolean showQueryClientMethods) {
            this.showQueryClientMethods$value = showQueryClientMethods;
            this.showQueryClientMethods$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withShowOutboundServices(boolean showOutboundServices) {
            this.showOutboundServices$value = showOutboundServices;
            this.showOutboundServices$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withShowOutboundServiceFields(boolean showOutboundServiceFields) {
            this.showOutboundServiceFields$value = showOutboundServiceFields;
            this.showOutboundServiceFields$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withShowOutboundServiceMethods(boolean showOutboundServiceMethods) {
            this.showOutboundServiceMethods$value = showOutboundServiceMethods;
            this.showOutboundServiceMethods$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withCallApplicationServiceDriver(boolean callApplicationServiceDriver) {
            this.callApplicationServiceDriver$value = callApplicationServiceDriver;
            this.callApplicationServiceDriver$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withFieldBlacklist(List<String> fieldBlacklist) {
            this.fieldBlacklist$value = fieldBlacklist;
            this.fieldBlacklist$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withMethodBlacklist(List<String> methodBlacklist) {
            this.methodBlacklist$value = methodBlacklist;
            this.methodBlacklist$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withShowInheritedMembersInClasses(boolean showInheritedMembersInClasses) {
            this.showInheritedMembersInClasses$value = showInheritedMembersInClasses;
            this.showInheritedMembersInClasses$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withShowObjectMembersInClasses(boolean showObjectMembersInClasses) {
            this.showObjectMembersInClasses$value = showObjectMembersInClasses;
            this.showObjectMembersInClasses$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withMultiplicityInLabel(boolean multiplicityInLabel) {
            this.multiplicityInLabel$value = multiplicityInLabel;
            this.multiplicityInLabel$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withFieldStereotypes(boolean fieldStereotypes) {
            this.fieldStereotypes$value = fieldStereotypes;
            this.fieldStereotypes$set = true;
            return this;
        }

        public DomainDiagramConfigBuilder withTransitiveFilterSeedDomainServiceTypeNames(List<String> transitiveFilterSeedDomainServiceTypeNames) {
            this.transitiveFilterSeedDomainServiceTypeNames$value = transitiveFilterSeedDomainServiceTypeNames;
            this.transitiveFilterSeedDomainServiceTypeNames$set = true;
            return this;
        }

        public DomainDiagramConfig build() {
            String contextPackageName$value = this.contextPackageName$value;
            if (!this.contextPackageName$set) {
                contextPackageName$value = DomainDiagramConfig.$default$contextPackageName();
            }
            String aggregateRootStyle$value = this.aggregateRootStyle$value;
            if (!this.aggregateRootStyle$set) {
                aggregateRootStyle$value = DomainDiagramConfig.$default$aggregateRootStyle();
            }
            String aggregateFrameStyle$value = this.aggregateFrameStyle$value;
            if (!this.aggregateFrameStyle$set) {
                aggregateFrameStyle$value = DomainDiagramConfig.$default$aggregateFrameStyle();
            }
            String entityStyle$value = this.entityStyle$value;
            if (!this.entityStyle$set) {
                entityStyle$value = DomainDiagramConfig.$default$entityStyle();
            }
            String valueObjectStyle$value = this.valueObjectStyle$value;
            if (!this.valueObjectStyle$set) {
                valueObjectStyle$value = DomainDiagramConfig.$default$valueObjectStyle();
            }
            String enumStyle$value = this.enumStyle$value;
            if (!this.enumStyle$set) {
                enumStyle$value = DomainDiagramConfig.$default$enumStyle();
            }
            String identityStyle$value = this.identityStyle$value;
            if (!this.identityStyle$set) {
                identityStyle$value = DomainDiagramConfig.$default$identityStyle();
            }
            String domainEventStyle$value = this.domainEventStyle$value;
            if (!this.domainEventStyle$set) {
                domainEventStyle$value = DomainDiagramConfig.$default$domainEventStyle();
            }
            String domainCommandStyle$value = this.domainCommandStyle$value;
            if (!this.domainCommandStyle$set) {
                domainCommandStyle$value = DomainDiagramConfig.$default$domainCommandStyle();
            }
            String applicationServiceStyle$value = this.applicationServiceStyle$value;
            if (!this.applicationServiceStyle$set) {
                applicationServiceStyle$value = DomainDiagramConfig.$default$applicationServiceStyle();
            }
            String domainServiceStyle$value = this.domainServiceStyle$value;
            if (!this.domainServiceStyle$set) {
                domainServiceStyle$value = DomainDiagramConfig.$default$domainServiceStyle();
            }
            String repositoryStyle$value = this.repositoryStyle$value;
            if (!this.repositoryStyle$set) {
                repositoryStyle$value = DomainDiagramConfig.$default$repositoryStyle();
            }
            String readModelStyle$value = this.readModelStyle$value;
            if (!this.readModelStyle$set) {
                readModelStyle$value = DomainDiagramConfig.$default$readModelStyle();
            }
            String queryClientStyle$value = this.queryClientStyle$value;
            if (!this.queryClientStyle$set) {
                queryClientStyle$value = DomainDiagramConfig.$default$queryClientStyle();
            }
            String outboundServiceStyle$value = this.outboundServiceStyle$value;
            if (!this.outboundServiceStyle$set) {
                outboundServiceStyle$value = DomainDiagramConfig.$default$outboundServiceStyle();
            }
            String font$value = this.font$value;
            if (!this.font$set) {
                font$value = DomainDiagramConfig.$default$font();
            }
            String direction$value = this.direction$value;
            if (!this.direction$set) {
                direction$value = DomainDiagramConfig.$default$direction();
            }
            String ranker$value = this.ranker$value;
            if (!this.ranker$set) {
                ranker$value = DomainDiagramConfig.$default$ranker();
            }
            String acycler$value = this.acycler$value;
            if (!this.acycler$set) {
                acycler$value = DomainDiagramConfig.$default$acycler();
            }
            List<String> classesBlacklist$value = this.classesBlacklist$value;
            if (!this.classesBlacklist$set) {
                classesBlacklist$value = DomainDiagramConfig.$default$classesBlacklist();
            }
            boolean showFields$value = this.showFields$value;
            if (!this.showFields$set) {
                showFields$value = DomainDiagramConfig.$default$showFields();
            }
            boolean showFullQualifiedClassNames$value = this.showFullQualifiedClassNames$value;
            if (!this.showFullQualifiedClassNames$set) {
                showFullQualifiedClassNames$value = DomainDiagramConfig.$default$showFullQualifiedClassNames();
            }
            boolean showAssertions$value = this.showAssertions$value;
            if (!this.showAssertions$set) {
                showAssertions$value = DomainDiagramConfig.$default$showAssertions();
            }
            boolean showMethods$value = this.showMethods$value;
            if (!this.showMethods$set) {
                showMethods$value = DomainDiagramConfig.$default$showMethods();
            }
            boolean showOnlyPublicMethods$value = this.showOnlyPublicMethods$value;
            if (!this.showOnlyPublicMethods$set) {
                showOnlyPublicMethods$value = DomainDiagramConfig.$default$showOnlyPublicMethods();
            }
            boolean showDomainEvents$value = this.showDomainEvents$value;
            if (!this.showDomainEvents$set) {
                showDomainEvents$value = DomainDiagramConfig.$default$showDomainEvents();
            }
            boolean showDomainEventFields$value = this.showDomainEventFields$value;
            if (!this.showDomainEventFields$set) {
                showDomainEventFields$value = DomainDiagramConfig.$default$showDomainEventFields();
            }
            boolean showDomainEventMethods$value = this.showDomainEventMethods$value;
            if (!this.showDomainEventMethods$set) {
                showDomainEventMethods$value = DomainDiagramConfig.$default$showDomainEventMethods();
            }
            boolean showDomainCommands$value = this.showDomainCommands$value;
            if (!this.showDomainCommands$set) {
                showDomainCommands$value = DomainDiagramConfig.$default$showDomainCommands();
            }
            boolean showOnlyTopLevelDomainCommandRelations$value = this.showOnlyTopLevelDomainCommandRelations$value;
            if (!this.showOnlyTopLevelDomainCommandRelations$set) {
                showOnlyTopLevelDomainCommandRelations$value = DomainDiagramConfig.$default$showOnlyTopLevelDomainCommandRelations();
            }
            boolean showDomainCommandFields$value = this.showDomainCommandFields$value;
            if (!this.showDomainCommandFields$set) {
                showDomainCommandFields$value = DomainDiagramConfig.$default$showDomainCommandFields();
            }
            boolean showDomainCommandMethods$value = this.showDomainCommandMethods$value;
            if (!this.showDomainCommandMethods$set) {
                showDomainCommandMethods$value = DomainDiagramConfig.$default$showDomainCommandMethods();
            }
            boolean showDomainServices$value = this.showDomainServices$value;
            if (!this.showDomainServices$set) {
                showDomainServices$value = DomainDiagramConfig.$default$showDomainServices();
            }
            boolean showDomainServiceFields$value = this.showDomainServiceFields$value;
            if (!this.showDomainServiceFields$set) {
                showDomainServiceFields$value = DomainDiagramConfig.$default$showDomainServiceFields();
            }
            boolean showDomainServiceMethods$value = this.showDomainServiceMethods$value;
            if (!this.showDomainServiceMethods$set) {
                showDomainServiceMethods$value = DomainDiagramConfig.$default$showDomainServiceMethods();
            }
            boolean showApplicationServices$value = this.showApplicationServices$value;
            if (!this.showApplicationServices$set) {
                showApplicationServices$value = DomainDiagramConfig.$default$showApplicationServices();
            }
            boolean showApplicationServiceFields$value = this.showApplicationServiceFields$value;
            if (!this.showApplicationServiceFields$set) {
                showApplicationServiceFields$value = DomainDiagramConfig.$default$showApplicationServiceFields();
            }
            boolean showApplicationServiceMethods$value = this.showApplicationServiceMethods$value;
            if (!this.showApplicationServiceMethods$set) {
                showApplicationServiceMethods$value = DomainDiagramConfig.$default$showApplicationServiceMethods();
            }
            boolean showRepositories$value = this.showRepositories$value;
            if (!this.showRepositories$set) {
                showRepositories$value = DomainDiagramConfig.$default$showRepositories();
            }
            boolean showRepositoryFields$value = this.showRepositoryFields$value;
            if (!this.showRepositoryFields$set) {
                showRepositoryFields$value = DomainDiagramConfig.$default$showRepositoryFields();
            }
            boolean showRepositoryMethods$value = this.showRepositoryMethods$value;
            if (!this.showRepositoryMethods$set) {
                showRepositoryMethods$value = DomainDiagramConfig.$default$showRepositoryMethods();
            }
            boolean showReadModels$value = this.showReadModels$value;
            if (!this.showReadModels$set) {
                showReadModels$value = DomainDiagramConfig.$default$showReadModels();
            }
            boolean showReadModelFields$value = this.showReadModelFields$value;
            if (!this.showReadModelFields$set) {
                showReadModelFields$value = DomainDiagramConfig.$default$showReadModelFields();
            }
            boolean showReadModelMethods$value = this.showReadModelMethods$value;
            if (!this.showReadModelMethods$set) {
                showReadModelMethods$value = DomainDiagramConfig.$default$showReadModelMethods();
            }
            boolean showQueryClients$value = this.showQueryClients$value;
            if (!this.showQueryClients$set) {
                showQueryClients$value = DomainDiagramConfig.$default$showQueryClients();
            }
            boolean showQueryClientFields$value = this.showQueryClientFields$value;
            if (!this.showQueryClientFields$set) {
                showQueryClientFields$value = DomainDiagramConfig.$default$showQueryClientFields();
            }
            boolean showQueryClientMethods$value = this.showQueryClientMethods$value;
            if (!this.showQueryClientMethods$set) {
                showQueryClientMethods$value = DomainDiagramConfig.$default$showQueryClientMethods();
            }
            boolean showOutboundServices$value = this.showOutboundServices$value;
            if (!this.showOutboundServices$set) {
                showOutboundServices$value = DomainDiagramConfig.$default$showOutboundServices();
            }
            boolean showOutboundServiceFields$value = this.showOutboundServiceFields$value;
            if (!this.showOutboundServiceFields$set) {
                showOutboundServiceFields$value = DomainDiagramConfig.$default$showOutboundServiceFields();
            }
            boolean showOutboundServiceMethods$value = this.showOutboundServiceMethods$value;
            if (!this.showOutboundServiceMethods$set) {
                showOutboundServiceMethods$value = DomainDiagramConfig.$default$showOutboundServiceMethods();
            }
            boolean callApplicationServiceDriver$value = this.callApplicationServiceDriver$value;
            if (!this.callApplicationServiceDriver$set) {
                callApplicationServiceDriver$value = DomainDiagramConfig.$default$callApplicationServiceDriver();
            }
            List<String> fieldBlacklist$value = this.fieldBlacklist$value;
            if (!this.fieldBlacklist$set) {
                fieldBlacklist$value = DomainDiagramConfig.$default$fieldBlacklist();
            }
            List<String> methodBlacklist$value = this.methodBlacklist$value;
            if (!this.methodBlacklist$set) {
                methodBlacklist$value = DomainDiagramConfig.$default$methodBlacklist();
            }
            boolean showInheritedMembersInClasses$value = this.showInheritedMembersInClasses$value;
            if (!this.showInheritedMembersInClasses$set) {
                showInheritedMembersInClasses$value = DomainDiagramConfig.$default$showInheritedMembersInClasses();
            }
            boolean showObjectMembersInClasses$value = this.showObjectMembersInClasses$value;
            if (!this.showObjectMembersInClasses$set) {
                showObjectMembersInClasses$value = DomainDiagramConfig.$default$showObjectMembersInClasses();
            }
            boolean multiplicityInLabel$value = this.multiplicityInLabel$value;
            if (!this.multiplicityInLabel$set) {
                multiplicityInLabel$value = DomainDiagramConfig.$default$multiplicityInLabel();
            }
            boolean fieldStereotypes$value = this.fieldStereotypes$value;
            if (!this.fieldStereotypes$set) {
                fieldStereotypes$value = DomainDiagramConfig.$default$fieldStereotypes();
            }
            List<String> transitiveFilterSeedDomainServiceTypeNames$value = this.transitiveFilterSeedDomainServiceTypeNames$value;
            if (!this.transitiveFilterSeedDomainServiceTypeNames$set) {
                transitiveFilterSeedDomainServiceTypeNames$value = DomainDiagramConfig.$default$transitiveFilterSeedDomainServiceTypeNames();
            }
            return new DomainDiagramConfig(contextPackageName$value, aggregateRootStyle$value, aggregateFrameStyle$value, entityStyle$value, valueObjectStyle$value, enumStyle$value, identityStyle$value, domainEventStyle$value, domainCommandStyle$value, applicationServiceStyle$value, domainServiceStyle$value, repositoryStyle$value, readModelStyle$value, queryClientStyle$value, outboundServiceStyle$value, font$value, direction$value, ranker$value, acycler$value, classesBlacklist$value, showFields$value, showFullQualifiedClassNames$value, showAssertions$value, showMethods$value, showOnlyPublicMethods$value, showDomainEvents$value, showDomainEventFields$value, showDomainEventMethods$value, showDomainCommands$value, showOnlyTopLevelDomainCommandRelations$value, showDomainCommandFields$value, showDomainCommandMethods$value, showDomainServices$value, showDomainServiceFields$value, showDomainServiceMethods$value, showApplicationServices$value, showApplicationServiceFields$value, showApplicationServiceMethods$value, showRepositories$value, showRepositoryFields$value, showRepositoryMethods$value, showReadModels$value, showReadModelFields$value, showReadModelMethods$value, showQueryClients$value, showQueryClientFields$value, showQueryClientMethods$value, showOutboundServices$value, showOutboundServiceFields$value, showOutboundServiceMethods$value, callApplicationServiceDriver$value, fieldBlacklist$value, methodBlacklist$value, showInheritedMembersInClasses$value, showObjectMembersInClasses$value, multiplicityInLabel$value, fieldStereotypes$value, transitiveFilterSeedDomainServiceTypeNames$value);
        }

        public String toString() {
            return "DomainDiagramConfig.DomainDiagramConfigBuilder(contextPackageName$value=" + this.contextPackageName$value + ", aggregateRootStyle$value=" + this.aggregateRootStyle$value + ", aggregateFrameStyle$value=" + this.aggregateFrameStyle$value + ", entityStyle$value=" + this.entityStyle$value + ", valueObjectStyle$value=" + this.valueObjectStyle$value + ", enumStyle$value=" + this.enumStyle$value + ", identityStyle$value=" + this.identityStyle$value + ", domainEventStyle$value=" + this.domainEventStyle$value + ", domainCommandStyle$value=" + this.domainCommandStyle$value + ", applicationServiceStyle$value=" + this.applicationServiceStyle$value + ", domainServiceStyle$value=" + this.domainServiceStyle$value + ", repositoryStyle$value=" + this.repositoryStyle$value + ", readModelStyle$value=" + this.readModelStyle$value + ", queryClientStyle$value=" + this.queryClientStyle$value + ", outboundServiceStyle$value=" + this.outboundServiceStyle$value + ", font$value=" + this.font$value + ", direction$value=" + this.direction$value + ", ranker$value=" + this.ranker$value + ", acycler$value=" + this.acycler$value + ", classesBlacklist$value=" + this.classesBlacklist$value + ", showFields$value=" + this.showFields$value + ", showFullQualifiedClassNames$value=" + this.showFullQualifiedClassNames$value + ", showAssertions$value=" + this.showAssertions$value + ", showMethods$value=" + this.showMethods$value + ", showOnlyPublicMethods$value=" + this.showOnlyPublicMethods$value + ", showDomainEvents$value=" + this.showDomainEvents$value + ", showDomainEventFields$value=" + this.showDomainEventFields$value + ", showDomainEventMethods$value=" + this.showDomainEventMethods$value + ", showDomainCommands$value=" + this.showDomainCommands$value + ", showOnlyTopLevelDomainCommandRelations$value=" + this.showOnlyTopLevelDomainCommandRelations$value + ", showDomainCommandFields$value=" + this.showDomainCommandFields$value + ", showDomainCommandMethods$value=" + this.showDomainCommandMethods$value + ", showDomainServices$value=" + this.showDomainServices$value + ", showDomainServiceFields$value=" + this.showDomainServiceFields$value + ", showDomainServiceMethods$value=" + this.showDomainServiceMethods$value + ", showApplicationServices$value=" + this.showApplicationServices$value + ", showApplicationServiceFields$value=" + this.showApplicationServiceFields$value + ", showApplicationServiceMethods$value=" + this.showApplicationServiceMethods$value + ", showRepositories$value=" + this.showRepositories$value + ", showRepositoryFields$value=" + this.showRepositoryFields$value + ", showRepositoryMethods$value=" + this.showRepositoryMethods$value + ", showReadModels$value=" + this.showReadModels$value + ", showReadModelFields$value=" + this.showReadModelFields$value + ", showReadModelMethods$value=" + this.showReadModelMethods$value + ", showQueryClients$value=" + this.showQueryClients$value + ", showQueryClientFields$value=" + this.showQueryClientFields$value + ", showQueryClientMethods$value=" + this.showQueryClientMethods$value + ", showOutboundServices$value=" + this.showOutboundServices$value + ", showOutboundServiceFields$value=" + this.showOutboundServiceFields$value + ", showOutboundServiceMethods$value=" + this.showOutboundServiceMethods$value + ", callApplicationServiceDriver$value=" + this.callApplicationServiceDriver$value + ", fieldBlacklist$value=" + this.fieldBlacklist$value + ", methodBlacklist$value=" + this.methodBlacklist$value + ", showInheritedMembersInClasses$value=" + this.showInheritedMembersInClasses$value + ", showObjectMembersInClasses$value=" + this.showObjectMembersInClasses$value + ", multiplicityInLabel$value=" + this.multiplicityInLabel$value + ", fieldStereotypes$value=" + this.fieldStereotypes$value + ", transitiveFilterSeedDomainServiceTypeNames$value=" + this.transitiveFilterSeedDomainServiceTypeNames$value + ")";
        }
    }
}
