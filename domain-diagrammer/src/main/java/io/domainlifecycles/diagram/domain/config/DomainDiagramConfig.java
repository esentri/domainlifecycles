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

package io.domainlifecycles.diagram.domain.config;

import io.domainlifecycles.diagram.DiagramConfig;
import io.domainlifecycles.diagram.domain.mapper.TransitiveDomainTypeAnPackageFilter;

import java.util.Collections;
import java.util.List;

/**
 * Configuration options for a Domain Diagram
 *
 * @author Mario Herb
 */
public class DomainDiagramConfig implements DiagramConfig {

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
     * Style declaration for QueryHandlers  (see Nomnoml style options)
     */
    private String queryHandlerStyle = "fill=#C0C0C0 bold";
    /**
     * Style declaration for OutboundServices  (see Nomnoml style options)
     */
    private String outboundServiceStyle = "fill=#C0C0C0 bold";
    /**
     * Style declaration for unspecified ServiceKinds  (see Nomnoml style options)
     */
    private String unspecifiedServiceKindStyle = "fill=#C0C0C0 bold";
    /**
     * General font style declaration  (see Nomnoml style options)
     */
    private String font = "Helvetica";
    /**
     * General layout direction style declaration (see Nomnoml style options, 'down' or 'right' is supported)
     */
    private String direction = "down";
    /**
     * General layout direction style declaration (see Nomnoml style options, 'network-simplex' or 'tight-tree' or
     * 'longest-path' is supported)
     */
    private String ranker = "longest-path";
    /**
     * General acycling style declaration  (see Nomnoml style options, only 'greedy' supported)
     */
    private String acycler = "greedy";
    /**
     * Background color style declaration (see Nomnoml style options, only 'transparent' or HEX color-codes supported)
     */
    private String backgroundColor = "transparent";
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
     * If true, QueryHandler classes are included
     */
    private boolean showQueryHandlers = true;
    /**
     * If true, fields of QueryHandlers are included
     */
    private boolean showQueryHandlerFields = false;
    /**
     * If true, methods of QueryHandlers are included
     */
    private boolean showQueryHandlerMethods = false;
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
     * If true, unspecified ServiceKind classes are included
     */
    private boolean showUnspecifiedServiceKinds = true;
    /**
     * If true, fields of unspecified ServiceKinds are included
     */
    private boolean showUnspecifiedServiceKindFields = false;
    /**
     * If true, methods of unspecified ServiceKinds are included
     */
    private boolean showUnspecifiedServiceKindMethods = false;

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
     *   <li>{@code <IDREF>}: Denotes an identifier reference field that holds a reference to another AggregateRoot
     *   .</li>
     *   <li>{@code <VO>}: Signifies a value object.</li>
     * </ul>
     */
    private boolean fieldStereotypes = true;

    /**
     * Enabling and initializing the seed for the {@link TransitiveDomainTypeAnPackageFilter}.
     */
    private List<String> transitiveFilterSeedDomainServiceTypeNames = Collections.emptyList();

    /**
     * Filter shown domain types by packageName
     */
    private List<String> filteredPackageNames = Collections.emptyList();

    /**
     * A flag that determines whether abstract types should be displayed in the domain diagram.
     * If set to true, abstract types will be included in the generated diagram; if false, 
     * abstract types will be omitted. This is used to control the inclusion of more high-level 
     * or generalized types that may not always be relevant in specific diagram views.
     */
    private boolean showAbstractTypes = false;

    /**
     * Indicates whether to use an abstract type name for concrete service kinds.
     * If set to true, the diagram will show most specific abstract type names when
     * displaying concrete service kinds.
     */
    private boolean useAbstractTypeNameForConcreteServiceKinds = true;

    DomainDiagramConfig(String aggregateRootStyle,
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
                        String direction,
                        String ranker,
                        String acycler,
                        String backgroundColor,
                        List<String> classesBlacklist,
                        boolean showFields,
                        boolean showFullQualifiedClassNames,
                        boolean showAssertions,
                        boolean showMethods,
                        boolean showOnlyPublicMethods,
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
                        List<String> transitiveFilterSeedDomainServiceTypeNames,
                        List<String> filteredPackageNames,
                        boolean showAbstractTypes,
                        boolean useAbstractTypeNameForConcreteServiceKinds
    ) {
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
        this.direction = direction;
        this.ranker = ranker;
        this.acycler = acycler;
        this.backgroundColor = backgroundColor;
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
        this.transitiveFilterSeedDomainServiceTypeNames = transitiveFilterSeedDomainServiceTypeNames;
        this.filteredPackageNames = filteredPackageNames;
        this.showAbstractTypes = showAbstractTypes;
        this.useAbstractTypeNameForConcreteServiceKinds = useAbstractTypeNameForConcreteServiceKinds;
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

    private static String $default$queryHandlerStyle() {
        return "fill=#C0C0C0 bold";
    }

    private static String $default$outboundServiceStyle() {
        return "fill=#C0C0C0 bold";
    }

    private static String $default$unspecifiedServiceKindStyle() {
        return "fill=#C0C0C0 bold";
    }

    private static String $default$font() {
        return "Helvetica";
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

    private static String $default$backgroundColor() {
        return "transparent";
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

    private static boolean $default$showQueryHandlers() {
        return true;
    }

    private static boolean $default$showQueryHandlerFields() {
        return false;
    }

    private static boolean $default$showQueryHandlerMethods() {
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

    private static boolean $default$showUnspecifiedServiceKinds() {
        return true;
    }

    private static boolean $default$showUnspecifiedServiceKindFields() {
        return false;
    }

    private static boolean $default$showUnspecifiedServiceKindMethods() {
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

    private static List<String> $default$filteredPackageNames() {
        return Collections.emptyList();
    }

    private static boolean $default$showAbstractTypes() {
        return false;
    }

    private static boolean $default$useAbstractTypeNameForConcreteServiceKinds() {
        return true;

    }

    /**
     * Returns a new instance of DomainDiagramConfigBuilder used to build configurations for domain diagrams.
     *
     * @return a new DomainDiagramConfigBuilder instance
     */
    public static DomainDiagramConfigBuilder builder() {
        return new DomainDiagramConfigBuilder();
    }

    /**
     * Retrieves the style configuration for the aggregate root representation.
     *
     * @return the style string for the aggregate root
     */
    public String getAggregateRootStyle() {
        return this.aggregateRootStyle;
    }

    /**
     * Retrieves the style configuration for the aggregate frame representation.
     *
     * @return the style string for the aggregate frame
     */
    public String getAggregateFrameStyle() {
        return this.aggregateFrameStyle;
    }

    /**
     * Retrieves the style configuration for the entity representation.
     *
     * @return the style string for the entity
     */
    public String getEntityStyle() {
        return this.entityStyle;
    }

    /**
     * Retrieves the style associated with the value object.
     *
     * @return the style of the value object as a String
     */
    public String getValueObjectStyle() {
        return this.valueObjectStyle;
    }

    /**
     * Retrieves the style format of the enum as a string.
     *
     * @return the enum style as a string
     */
    public String getEnumStyle() {
        return this.enumStyle;
    }

    /**
     * Retrieves the identity style associated with this instance.
     *
     * @return the identity style as a string
     */
    public String getIdentityStyle() {
        return this.identityStyle;
    }

    /**
     * Retrieves the style associated with the domain event.
     *
     * @return a string representing the domain event style.
     */
    public String getDomainEventStyle() {
        return this.domainEventStyle;
    }

    /**
     * Retrieves the domain command style.
     *
     * @return the domain command style as a String
     */
    public String getDomainCommandStyle() {
        return this.domainCommandStyle;
    }

    /**
     * Retrieves the style of the application service.
     *
     * @return a string representing the application service style.
     */
    public String getApplicationServiceStyle() {
        return this.applicationServiceStyle;
    }

    /**
     * Retrieves the style configuration for the domain service.
     *
     * @return the domain service style as a String
     */
    public String getDomainServiceStyle() {
        return this.domainServiceStyle;
    }

    /**
     * Retrieves the repository style.
     *
     * @return the repository style as a String
     */
    public String getRepositoryStyle() {
        return this.repositoryStyle;
    }

    /**
     * Retrieves the style of the read model currently set.
     *
     * @return the style of the read model as a String
     */
    public String getReadModelStyle() {
        return this.readModelStyle;
    }

    /**
     * Retrieves the query handler style used in the current context.
     *
     * @return the query handler style as a String
     */
    public String getQueryHandlerStyle() {
        return this.queryHandlerStyle;
    }

    /**
     * Retrieves the style configuration for the outbound service.
     *
     * @return the style of the outbound service as a String
     */
    public String getOutboundServiceStyle() {
        return this.outboundServiceStyle;
    }

    /**
     * Retrieves the style associated with an unspecified service kind.
     *
     * @return a String representing the style for unspecified service kind.
     */
    public String getUnspecifiedServiceKindStyle() {
        return unspecifiedServiceKindStyle;
    }

    /**
     * Retrieves the font associated with the current object.
     *
     * @return the name of the font as a String
     */
    public String getFont() {
        return this.font;
    }

    /**
     * Retrieves the current Nomnoml diagram direction.
     *
     * @return the current direction as a String
     */
    public String getDirection() {
        return this.direction;
    }

    /**
     * Retrieves the Nomnoml ranker associated with this instance.
     *
     * @return the ranker as a String
     */
    public String getRanker() {
        return this.ranker;
    }

    /**
     * Retrieves the value of the Nomnoml acycler field.
     *
     * @return the current value of the acycler field
     */
    public String getAcycler() {
        return this.acycler;
    }

    /**
     * Retrieves the background color.
     *
     * @return the background color as a String
     */
    public String getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Retrieves the list of full qualified class names that are blacklisted.
     *
     * @return a list of class names that are currently blacklisted
     */
    public List<String> getClassesBlacklist() {
        return this.classesBlacklist;
    }

    /**
     * Determines whether fields should be shown.
     *
     * @return true if fields are set to be shown, false otherwise
     */
    public boolean isShowFields() {
        return this.showFields;
    }

    /**
     * Determines whether fully qualified class names are displayed.
     *
     * @return true if fully qualified class names are displayed, false otherwise.
     */
    public boolean isShowFullQualifiedClassNames() {
        return this.showFullQualifiedClassNames;
    }

    /**
     * Indicates whether assertions are enabled.
     *
     * @return true if assertions are enabled, false otherwise
     */
    public boolean isShowAssertions() {
        return this.showAssertions;
    }

    /**
     * Checks whether methods are set to be shown.
     *
     * @return true if methods are configured to be shown, false otherwise
     */
    public boolean isShowMethods() {
        return this.showMethods;
    }

    /**
     * Determines whether only public methods are set to be shown.
     *
     * @return true if only public methods are to be shown; false otherwise.
     */
    public boolean isShowOnlyPublicMethods() {
        return this.showOnlyPublicMethods;
    }

    /**
     * Determines whether domain events should be shown.
     *
     * @return true if domain events are to be displayed, false otherwise.
     */
    public boolean isShowDomainEvents() {
        return this.showDomainEvents;
    }

    /**
     * Indicates whether the domain event fields should be displayed.
     *
     * @return true if domain event fields are to be shown, false otherwise
     */
    public boolean isShowDomainEventFields() {
        return this.showDomainEventFields;
    }

    /**
     * Determines whether domain event methods should be displayed.
     *
     * @return true if domain event methods are set to be shown, false otherwise
     */
    public boolean isShowDomainEventMethods() {
        return this.showDomainEventMethods;
    }

    /**
     * Determines if domain commands should be displayed.
     *
     * @return {@code true} if domain commands are to be displayed, {@code false} otherwise.
     */
    public boolean isShowDomainCommands() {
        return this.showDomainCommands;
    }

    /**
     * Determines whether only top-level domain command relations are shown.
     *
     * @return true if only top-level domain command relations are shown; false otherwise
     */
    public boolean isShowOnlyTopLevelDomainCommandRelations() {
        return this.showOnlyTopLevelDomainCommandRelations;
    }

    /**
     * Determines whether the domain command fields are set to be displayed.
     *
     * @return true if the domain command fields are set to be displayed, false otherwise
     */
    public boolean isShowDomainCommandFields() {
        return this.showDomainCommandFields;
    }

    /**
     * Determines whether the domain command methods should be displayed.
     *
     * @return true if the domain command methods should be displayed, false otherwise.
     */
    public boolean isShowDomainCommandMethods() {
        return this.showDomainCommandMethods;
    }

    /**
     * Determines if the domain services should be displayed.
     *
     * @return true if domain services are to be shown, false otherwise
     */
    public boolean isShowDomainServices() {
        return this.showDomainServices;
    }

    /**
     * Determines whether the domain service fields should be displayed.
     *
     * @return true if the domain service fields should be shown, false otherwise
     */
    public boolean isShowDomainServiceFields() {
        return this.showDomainServiceFields;
    }

    /**
     * Determines if domain service methods should be displayed.
     *
     * @return true if domain service methods are to be displayed, false otherwise.
     */
    public boolean isShowDomainServiceMethods() {
        return this.showDomainServiceMethods;
    }

    /**
     * Indicates whether the application services should be displayed.
     *
     * @return true if application services are set to be shown, false otherwise
     */
    public boolean isShowApplicationServices() {
        return this.showApplicationServices;
    }

    /**
     * Determines whether application service fields should be displayed.
     *
     * @return true if application service fields should be shown, false otherwise.
     */
    public boolean isShowApplicationServiceFields() {
        return this.showApplicationServiceFields;
    }

    /**
     * Determines if application service methods should be displayed.
     *
     * @return true if application service methods should be displayed; false otherwise
     */
    public boolean isShowApplicationServiceMethods() {
        return this.showApplicationServiceMethods;
    }

    /**
     * Checks whether the repositories should be shown or not.
     *
     * @return true if repositories are to be displayed, otherwise false.
     */
    public boolean isShowRepositories() {
        return this.showRepositories;
    }

    /**
     * Determines whether repository fields should be displayed.
     *
     * @return true if repository fields are to be shown, false otherwise
     */
    public boolean isShowRepositoryFields() {
        return this.showRepositoryFields;
    }

    /**
     * Determines whether repository methods should be shown.
     *
     * @return true if repository methods are to be shown, false otherwise.
     */
    public boolean isShowRepositoryMethods() {
        return this.showRepositoryMethods;
    }

    /**
     * Determines if the read models should be displayed.
     *
     * @return true if read models are to be shown; false otherwise.
     */
    public boolean isShowReadModels() {
        return this.showReadModels;
    }

    /**
     * Determines whether the read model fields should be displayed.
     *
     * @return true if the read model fields are to be displayed; false otherwise.
     */
    public boolean isShowReadModelFields() {
        return this.showReadModelFields;
    }

    /**
     * Checks whether the read model methods should be shown.
     *
     * @return true if the read model methods are set to be shown, false otherwise
     */
    public boolean isShowReadModelMethods() {
        return this.showReadModelMethods;
    }

    /**
     * Determines whether the display of query handlers is enabled.
     *
     * @return true if query handlers are set to be shown, false otherwise
     */
    public boolean isShowQueryHandlers() {
        return this.showQueryHandlers;
    }

    /**
     * Determines whether the query handler fields should be displayed.
     *
     * @return true if the query handler fields should be shown; false otherwise.
     */
    public boolean isShowQueryHandlerFields() {
        return this.showQueryHandlerFields;
    }

    /**
     * Determines if query handler methods should be displayed.
     *
     * @return true if query handler methods are set to be shown, false otherwise
     */
    public boolean isShowQueryHandlerMethods() {
        return this.showQueryHandlerMethods;
    }

    /**
     * Determines whether outbound services should be displayed.
     *
     * @return true if outbound services are to be shown, false otherwise.
     */
    public boolean isShowOutboundServices() {
        return this.showOutboundServices;
    }

    /**
     * Determines whether the outbound service fields should be displayed.
     *
     * @return true if the outbound service fields should be displayed; false otherwise.
     */
    public boolean isShowOutboundServiceFields() {
        return this.showOutboundServiceFields;
    }

    /**
     * Determines if outbound service methods should be displayed.
     *
     * @return true if outbound service methods are set to be displayed, false otherwise.
     */
    public boolean isShowOutboundServiceMethods() {
        return this.showOutboundServiceMethods;
    }

    /**
     * Determines whether unspecified service kinds should be displayed.
     *
     * @return true if unspecified service kinds are to be shown, false otherwise
     */
    public boolean isShowUnspecifiedServiceKinds() {
        return this.showUnspecifiedServiceKinds;
    }

    /**
     * Determines whether unspecified service kind fields should be displayed.
     *
     * @return true if unspecified service kind fields should be shown; false otherwise.
     */
    public boolean isShowUnspecifiedServiceKindFields() {
        return this.showUnspecifiedServiceKindFields;
    }

    /**
     * Determines whether methods for unspecified service kinds should be displayed.
     *
     * @return true if unspecified service kind methods are to be shown; false otherwise.
     */
    public boolean isShowUnspecifiedServiceKindMethods() {
        return this.showUnspecifiedServiceKindMethods;
    }

    /**
     * Determines whether the call application service driver is enabled.
     *
     * @return true if the call application service driver is enabled; false otherwise.
     */
    public boolean isCallApplicationServiceDriver() {
        return this.callApplicationServiceDriver;
    }

    /**
     * Retrieves the list of fields that are blacklisted.
     *
     * @return a list of strings representing the blacklisted fields.
     */
    public List<String> getFieldBlacklist() {
        return this.fieldBlacklist;
    }

    /**
     * Retrieves the list of blacklisted method names.
     *
     * @return a list of method names that are blacklisted.
     */
    public List<String> getMethodBlacklist() {
        return this.methodBlacklist;
    }

    /**
     * Determines whether inherited members in classes should be displayed.
     *
     * @return true if inherited members are set to be shown in classes, false otherwise.
     */
    public boolean isShowInheritedMembersInClasses() {
        return this.showInheritedMembersInClasses;
    }

    /**
     * Determines whether object members are displayed within classes.
     *
     * @return true if object members are shown in classes; false otherwise.
     */
    public boolean isShowObjectMembersInClasses() {
        return this.showObjectMembersInClasses;
    }

    /**
     * Checks whether the multiplicity is included in the label.
     *
     * @return true if the multiplicity is included in the label, false otherwise.
     */
    public boolean isMultiplicityInLabel() {
        return this.multiplicityInLabel;
    }

    /**
     * Checks weather field stereotypes should be shown.
     *
     * @return true if the field stereotypes are used, false otherwise.
     */
    public boolean isFieldStereotypes() {
        return this.fieldStereotypes;
    }

    /**
     * Retrieves the list of transitive filter seed domain service type names (fullqualified).
     *
     * @return a list of strings representing the transitive filter seed domain service type names
     */
    public List<String> getTransitiveFilterSeedDomainServiceTypeNames() {
        return this.transitiveFilterSeedDomainServiceTypeNames;
    }

    /**
     * Retrieves the list of filtered package names.
     *
     * @return a list of strings representing the filtered package names.
     */
    public List<String> getFilteredPackageNames() {
        return this.filteredPackageNames;
    }

    /**
     * Determines whether abstract types are set to be shown.
     *
     * @return true if abstract types are to be displayed; false otherwise.
     */
    public boolean isShowAbstractTypes() {
        return this.showAbstractTypes;
    }

    /**
     * Checks if the system is configured to use abstract type names for concrete service kinds.
     *
     * @return true if abstract type names are used for concrete service kinds, false otherwise.
     */
    public boolean isUseAbstractTypeNameForConcreteServiceKinds() {
        return this.useAbstractTypeNameForConcreteServiceKinds;
    }

    /**
     * Converts the current DomainDiagramConfig object into a builder instance, allowing further modifications.
     *
     * @return a {@link DomainDiagramConfigBuilder} pre-populated with the settings of the current DomainDiagramConfig object.
     */
    public DomainDiagramConfigBuilder toBuilder() {
        return new DomainDiagramConfigBuilder()
            .withAggregateRootStyle(this.aggregateRootStyle)
            .withAggregateFrameStyle(this.aggregateFrameStyle)
            .withEntityStyle(this.entityStyle)
            .withValueObjectStyle(this.valueObjectStyle)
            .withEnumStyle(this.enumStyle)
            .withIdentityStyle(this.identityStyle)
            .withDomainEventStyle(this.domainEventStyle)
            .withDomainCommandStyle(this.domainCommandStyle)
            .withApplicationServiceStyle(this.applicationServiceStyle)
            .withDomainServiceStyle(this.domainServiceStyle)
            .withRepositoryStyle(this.repositoryStyle)
            .withReadModelStyle(this.readModelStyle)
            .withQueryHandlerStyle(this.queryHandlerStyle)
            .withOutboundServiceStyle(this.outboundServiceStyle)
            .withUnspecifiedServiceKindStyle(this.unspecifiedServiceKindStyle)
            .withFont(this.font).withDirection(this.direction)
            .withRanker(this.ranker).withAcycler(this.acycler)
            .withBackgroundColor(this.backgroundColor)
            .withClassesBlacklist(this.classesBlacklist)
            .withShowFields(this.showFields)
            .withShowFullQualifiedClassNames(this.showFullQualifiedClassNames)
            .withShowAssertions(this.showAssertions)
            .withShowMethods(this.showMethods)
            .withShowOnlyPublicMethods(this.showOnlyPublicMethods)
            .withShowDomainEvents(this.showDomainEvents)
            .withShowDomainEventFields(this.showDomainEventFields)
            .withShowDomainEventMethods(this.showDomainEventMethods)
            .withShowDomainCommands(this.showDomainCommands)
            .withShowOnlyTopLevelDomainCommandRelations(this.showOnlyTopLevelDomainCommandRelations)
            .withShowDomainCommandFields(this.showDomainCommandFields)
            .withShowDomainCommandMethods(this.showDomainCommandMethods)
            .withShowDomainServices(this.showDomainServices)
            .withShowDomainServiceFields(this.showDomainServiceFields)
            .withShowDomainServiceMethods(this.showDomainServiceMethods)
            .withShowApplicationServices(this.showApplicationServices)
            .withShowApplicationServiceFields(this.showApplicationServiceFields)
            .withShowApplicationServiceMethods(this.showApplicationServiceMethods)
            .withShowRepositories(this.showRepositories)
            .withShowRepositoryFields(this.showRepositoryFields)
            .withShowRepositoryMethods(this.showRepositoryMethods)
            .withShowReadModels(this.showReadModels)
            .withShowReadModelFields(this.showReadModelFields)
            .withShowReadModelMethods(this.showReadModelMethods)
            .withShowQueryHandlers(this.showQueryHandlers)
            .withShowQueryHandlerFields(this.showQueryHandlerFields)
            .withShowQueryHandlerMethods(this.showQueryHandlerMethods)
            .withShowOutboundServices(this.showOutboundServices)
            .withShowOutboundServiceFields(this.showOutboundServiceFields)
            .withShowOutboundServiceMethods(this.showOutboundServiceMethods)
            .withShowUnspecifiedServiceKinds(this.showUnspecifiedServiceKinds)
            .withShowUnspecifiedServiceKindFields(this.showUnspecifiedServiceKindFields)
            .withShowUnspecifiedServiceKindMethods(this.showUnspecifiedServiceKindMethods)
            .withCallApplicationServiceDriver(this.callApplicationServiceDriver)
            .withFieldBlacklist(this.fieldBlacklist)
            .withMethodBlacklist(this.methodBlacklist)
            .withShowInheritedMembersInClasses(this.showInheritedMembersInClasses)
            .withShowObjectMembersInClasses(this.showObjectMembersInClasses)
            .withMultiplicityInLabel(this.multiplicityInLabel)
            .withFieldStereotypes(this.fieldStereotypes)
            .withTransitiveFilterSeedDomainServiceTypeNames(this.transitiveFilterSeedDomainServiceTypeNames)
            .withFilteredPackageNames(this.filteredPackageNames)
            .withShowAbstractTypes(this.showAbstractTypes)
            .withUseAbstractTypeNameForConcreteServiceKinds(this.useAbstractTypeNameForConcreteServiceKinds);
    }

    /**
     * A builder class for configuring the domain diagram representation settings.
     * This class provides a fluent API to define various visual and structural elements
     * of a domain model diagram, including styles, visibility of elements, and filtering capabilities.
     *
     * The configuration options allow customization of the appearance of domain entities, services,
     * commands, events, and other relevant components in the diagram.
     *
     * This class supports chaining of method calls to enable concise and expressive configurations.
     */
    public static class DomainDiagramConfigBuilder {
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
        private String queryHandlerStyle$value;
        private boolean queryHandlerStyle$set;
        private String outboundServiceStyle$value;
        private boolean outboundServiceStyle$set;
        private String unspecifiedServiceKindStyle$value;
        private boolean unspecifiedServiceKindStyle$set;
        private String font$value;
        private boolean font$set;
        private String direction$value;
        private boolean direction$set;
        private String ranker$value;
        private boolean ranker$set;
        private String acycler$value;
        private boolean acycler$set;
        private String backgroundColor$value;
        private boolean backgroundColor$set;
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
        private boolean showQueryHandlers$value;
        private boolean showQueryHandlers$set;
        private boolean showQueryHandlerFields$value;
        private boolean showQueryHandlerFields$set;
        private boolean showQueryHandlerMethods$value;
        private boolean showQueryHandlerMethods$set;
        private boolean showOutboundServices$value;
        private boolean showOutboundServices$set;
        private boolean showOutboundServiceFields$value;
        private boolean showOutboundServiceFields$set;
        private boolean showOutboundServiceMethods$value;
        private boolean showOutboundServiceMethods$set;
        private boolean showUnspecifiedServiceKinds$value;
        private boolean showUnspecifiedServiceKinds$set;
        private boolean showUnspecifiedServiceKindFields$value;
        private boolean showUnspecifiedServiceKindFields$set;
        private boolean showUnspecifiedServiceKindMethods$value;
        private boolean showUnspecifiedServiceKindMethods$set;
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
        private List<String> filteredPackageNames$value;
        private boolean filteredPackageNames$set;
        private boolean showAbstractTypes$value;
        private boolean showAbstractTypes$set;
        private boolean useAbstractTypeNameForConcreteServiceKinds$value;
        private boolean useAbstractTypeNameForConcreteServiceKinds$set;

        DomainDiagramConfigBuilder() {
        }

        /**
         * Sets the style to be used for aggregate root elements in the domain diagram configuration.
         *
         * @param aggregateRootStyle the style to apply to aggregate root elements
         * @return the current instance of {@code DomainDiagramConfigBuilder} for method chaining
         */
        public DomainDiagramConfigBuilder withAggregateRootStyle(String aggregateRootStyle) {
            this.aggregateRootStyle$value = aggregateRootStyle;
            this.aggregateRootStyle$set = true;
            return this;
        }

        /**
         * Sets the style for the aggregate frame in the domain diagram configuration.
         *
         * @param aggregateFrameStyle the style to be applied to the aggregate frame
         * @return the current instance of DomainDiagramConfigBuilder for method chaining
         */
        public DomainDiagramConfigBuilder withAggregateFrameStyle(String aggregateFrameStyle) {
            this.aggregateFrameStyle$value = aggregateFrameStyle;
            this.aggregateFrameStyle$set = true;
            return this;
        }

        /**
         * Sets the style for entities in the domain diagram configuration.
         *
         * @param entityStyle the style to be applied to entities
         * @return the current instance of DomainDiagramConfigBuilder for method chaining
         */
        public DomainDiagramConfigBuilder withEntityStyle(String entityStyle) {
            this.entityStyle$value = entityStyle;
            this.entityStyle$set = true;
            return this;
        }

        /**
         * Sets the value object style for the Domain Diagram configuration.
         *
         * @param valueObjectStyle the value object style to be applied
         * @return the current instance of DomainDiagramConfigBuilder
         */
        public DomainDiagramConfigBuilder withValueObjectStyle(String valueObjectStyle) {
            this.valueObjectStyle$value = valueObjectStyle;
            this.valueObjectStyle$set = true;
            return this;
        }

        /**
         * Sets the style to be used for enums in the domain diagram configuration.
         *
         * @param enumStyle the style to apply to enums
         * @return the updated DomainDiagramConfigBuilder instance
         */
        public DomainDiagramConfigBuilder withEnumStyle(String enumStyle) {
            this.enumStyle$value = enumStyle;
            this.enumStyle$set = true;
            return this;
        }

        /**
         * Sets the identity style for the domain diagram configuration.
         *
         * @param identityStyle the style to be used for identity representation in the diagram
         * @return the updated instance of DomainDiagramConfigBuilder
         */
        public DomainDiagramConfigBuilder withIdentityStyle(String identityStyle) {
            this.identityStyle$value = identityStyle;
            this.identityStyle$set = true;
            return this;
        }

        /**
         * Sets the style for domain events in the DomainDiagramConfigBuilder.
         *
         * @param domainEventStyle the style to be applied to domain events
         * @return the updated instance of DomainDiagramConfigBuilder
         */
        public DomainDiagramConfigBuilder withDomainEventStyle(String domainEventStyle) {
            this.domainEventStyle$value = domainEventStyle;
            this.domainEventStyle$set = true;
            return this;
        }

        /**
         * Sets the style for domain commands in the domain diagram configuration.
         *
         * @param domainCommandStyle the style to be applied for domain commands
         * @return the updated DomainDiagramConfigBuilder instance
         */
        public DomainDiagramConfigBuilder withDomainCommandStyle(String domainCommandStyle) {
            this.domainCommandStyle$value = domainCommandStyle;
            this.domainCommandStyle$set = true;
            return this;
        }

        /**
         * Sets the application service style for the domain diagram configuration.
         *
         * @param applicationServiceStyle the style to be applied to the application service
         * @return the updated DomainDiagramConfigBuilder instance
         */
        public DomainDiagramConfigBuilder withApplicationServiceStyle(String applicationServiceStyle) {
            this.applicationServiceStyle$value = applicationServiceStyle;
            this.applicationServiceStyle$set = true;
            return this;
        }

        /**
         * Sets the style for the domain service in the domain diagram configuration.
         *
         * @param domainServiceStyle the style to be applied for the domain service
         * @return the current instance of DomainDiagramConfigBuilder for method chaining
         */
        public DomainDiagramConfigBuilder withDomainServiceStyle(String domainServiceStyle) {
            this.domainServiceStyle$value = domainServiceStyle;
            this.domainServiceStyle$set = true;
            return this;
        }

        /**
         * Sets the repository style for the domain diagram configuration.
         *
         * @param repositoryStyle the repository style to be used
         * @return the current instance of DomainDiagramConfigBuilder for method chaining
         */
        public DomainDiagramConfigBuilder withRepositoryStyle(String repositoryStyle) {
            this.repositoryStyle$value = repositoryStyle;
            this.repositoryStyle$set = true;
            return this;
        }

        /**
         * Sets the read model style configuration for the domain diagram.
         *
         * @param readModelStyle the style to be applied to the read model
         * @return the updated DomainDiagramConfigBuilder instance
         */
        public DomainDiagramConfigBuilder withReadModelStyle(String readModelStyle) {
            this.readModelStyle$value = readModelStyle;
            this.readModelStyle$set = true;
            return this;
        }

        /**
         * Sets the query handler style for the domain diagram configuration.
         *
         * @param queryHandlerStyle the query handler style to be set
         * @return the current instance of DomainDiagramConfigBuilder
         */
        public DomainDiagramConfigBuilder withQueryHandlerStyle(String queryHandlerStyle) {
            this.queryHandlerStyle$value = queryHandlerStyle;
            this.queryHandlerStyle$set = true;
            return this;
        }

        /**
         * Sets the outbound service style for the domain diagram configuration.
         *
         * @param outboundServiceStyle the style to be applied for outbound services in the domain diagram
         * @return the current instance of DomainDiagramConfigBuilder for method chaining
         */
        public DomainDiagramConfigBuilder withOutboundServiceStyle(String outboundServiceStyle) {
            this.outboundServiceStyle$value = outboundServiceStyle;
            this.outboundServiceStyle$set = true;
            return this;
        }

        /**
         * Sets the style to be used for unspecified service kinds in the domain diagram.
         *
         * @param unspecifiedServiceKindStyle the style to use for unspecified service kinds
         * @return the current instance of {@code DomainDiagramConfigBuilder} for method chaining
         */
        public DomainDiagramConfigBuilder withUnspecifiedServiceKindStyle(String unspecifiedServiceKindStyle) {
            this.unspecifiedServiceKindStyle$value = unspecifiedServiceKindStyle;
            this.unspecifiedServiceKindStyle$set = true;
            return this;
        }

        /**
         * Sets the font configuration for the domain diagram.
         *
         * @param font the font to be used in the domain diagram
         * @return the current instance of {@code DomainDiagramConfigBuilder} for method chaining
         */
        public DomainDiagramConfigBuilder withFont(String font) {
            this.font$value = font;
            this.font$set = true;
            return this;
        }

        /**
         * Configures the direction of the layout in the domain diagram.
         *
         * @param direction the direction to apply for the layout (e.g., "right", "down")
         * @return the current instance of {@code DomainDiagramConfigBuilder} for method chaining
         */
        public DomainDiagramConfigBuilder withDirection(String direction) {
            this.direction$value = direction;
            this.direction$set = true;
            return this;
        }

        /**
         * Sets the ranker configuration for the domain diagram.
         *
         * @param ranker the ranker style to use for organizing elements (e.g., "network-simplex")
         * @return the current instance of {@code DomainDiagramConfigBuilder} for method chaining
         */
        public DomainDiagramConfigBuilder withRanker(String ranker) {
            this.ranker$value = ranker;
            this.ranker$set = true;
            return this;
        }

        /**
         * Configures the acycler setting for the domain diagram.
         *
         * @param acycler the acycler algorithm to be used for edge rendering
         * @return the current instance of {@code DomainDiagramConfigBuilder} for method chaining
         */
        public DomainDiagramConfigBuilder withAcycler(String acycler) {
            this.acycler$value = acycler;
            this.acycler$set = true;
            return this;
        }

        /**
         * Sets the background color of the domain diagram.
         *
         * @param backgroundColor the color to be used as the background of the diagram
         * @return the current instance of {@code DomainDiagramConfigBuilder} for method chaining
         */
        public DomainDiagramConfigBuilder withBackgroundColor(String backgroundColor) {
            this.backgroundColor$value = backgroundColor;
            this.backgroundColor$set = true;
            return this;
        }

        /**
         * Specifies a list of classes that should be blacklisted in the diagram (full qualified class names).
         *
         * @param classesBlacklist a list of class names to exclude from the diagram
         * @return the updated instance of {@code DomainDiagramConfigBuilder} for method chaining
         */
        public DomainDiagramConfigBuilder withClassesBlacklist(List<String> classesBlacklist) {
            this.classesBlacklist$value = classesBlacklist;
            this.classesBlacklist$set = true;
            return this;
        }

        /**
         * Configures whether fields should be displayed in the diagram.
         *
         * @param showFields a boolean value indicating whether fields should be shown
         * @return the updated instance of {@code DomainDiagramConfigBuilder} for method chaining
         */
        public DomainDiagramConfigBuilder withShowFields(boolean showFields) {
            this.showFields$value = showFields;
            this.showFields$set = true;
            return this;
        }

        /**
         * Configures whether fully qualified class names should be displayed.
         *
         * @param showFullQualifiedClassNames a boolean value indicating whether to show fully qualified class names
         * @return the updated instance of {@code DomainDiagramConfigBuilder} for method chaining
         */
        public DomainDiagramConfigBuilder withShowFullQualifiedClassNames(boolean showFullQualifiedClassNames) {
            this.showFullQualifiedClassNames$value = showFullQualifiedClassNames;
            this.showFullQualifiedClassNames$set = true;
            return this;
        }

        /**
         * Configures whether assertions should be displayed in the diagram.
         *
         * @param showAssertions a boolean value indicating whether assertions should be shown
         * @return the updated instance of {@code DomainDiagramConfigBuilder} for method chaining
         */
        public DomainDiagramConfigBuilder withShowAssertions(boolean showAssertions) {
            this.showAssertions$value = showAssertions;
            this.showAssertions$set = true;
            return this;
        }

        /**
         * Configures whether methods should be displayed in the diagram.
         *
         * @param showMethods a boolean value indicating whether methods should be shown
         * @return the updated instance of {@code DomainDiagramConfigBuilder} for method chaining
         */
        public DomainDiagramConfigBuilder withShowMethods(boolean showMethods) {
            this.showMethods$value = showMethods;
            this.showMethods$set = true;
            return this;
        }

        /**
         * Configures whether only public methods should be displayed in the diagram.
         *
         * @param showOnlyPublicMethods a boolean value indicating whether only public methods should be shown
         * @return the updated instance of {@code DomainDiagramConfigBuilder} for method chaining
         */
        public DomainDiagramConfigBuilder withShowOnlyPublicMethods(boolean showOnlyPublicMethods) {
            this.showOnlyPublicMethods$value = showOnlyPublicMethods;
            this.showOnlyPublicMethods$set = true;
            return this;
        }

        /**
         * Configures whether domain events should be displayed in the diagram.
         *
         * @param showDomainEvents a boolean value indicating whether domain events should be shown
         * @return the updated instance of {@code DomainDiagramConfigBuilder} for method chaining
         */
        public DomainDiagramConfigBuilder withShowDomainEvents(boolean showDomainEvents) {
            this.showDomainEvents$value = showDomainEvents;
            this.showDomainEvents$set = true;
            return this;
        }

        /**
         * Configures whether fields of domain events should be displayed in the diagram.
         *
         * @param showDomainEventFields a boolean value indicating whether domain event fields should be shown
         * @return the updated instance of {@code DomainDiagramConfigBuilder} for method chaining
         */
        public DomainDiagramConfigBuilder withShowDomainEventFields(boolean showDomainEventFields) {
            this.showDomainEventFields$value = showDomainEventFields;
            this.showDomainEventFields$set = true;
            return this;
        }

        /**
         * Configures whether methods of domain events should be displayed in the diagram.
         *
         * @param showDomainEventMethods a boolean value indicating whether domain event methods should be shown
         * @return the updated instance of {@code DomainDiagramConfigBuilder} for method chaining
         */
        public DomainDiagramConfigBuilder withShowDomainEventMethods(boolean showDomainEventMethods) {
            this.showDomainEventMethods$value = showDomainEventMethods;
            this.showDomainEventMethods$set = true;
            return this;
        }

        /**
         * Configures whether domain commands should be displayed in the diagram.
         *
         * @param showDomainCommands a boolean value indicating whether domain commands should be shown
         * @return the updated instance of {@code DomainDiagramConfigBuilder} for method chaining
         */
        public DomainDiagramConfigBuilder withShowDomainCommands(boolean showDomainCommands) {
            this.showDomainCommands$value = showDomainCommands;
            this.showDomainCommands$set = true;
            return this;
        }

        /**
         * Configures whether only top-level domain command relations should be displayed.
         *
         * @param showOnlyTopLevelDomainCommandRelations a boolean value indicating whether to show only top-level domain command relations
         * @return the updated instance of {@code DomainDiagramConfigBuilder} for method chaining
         */
        public DomainDiagramConfigBuilder withShowOnlyTopLevelDomainCommandRelations(boolean showOnlyTopLevelDomainCommandRelations) {
            this.showOnlyTopLevelDomainCommandRelations$value = showOnlyTopLevelDomainCommandRelations;
            this.showOnlyTopLevelDomainCommandRelations$set = true;
            return this;
        }

        /**
         * Configures whether fields of domain commands should be displayed in the diagram.
         *
         * @param showDomainCommandFields a boolean value indicating whether domain command fields should be shown
         * @return the updated instance of {@code DomainDiagramConfigBuilder} for method chaining
         */
        public DomainDiagramConfigBuilder withShowDomainCommandFields(boolean showDomainCommandFields) {
            this.showDomainCommandFields$value = showDomainCommandFields;
            this.showDomainCommandFields$set = true;
            return this;
        }

        /**
         * Configures whether to show domain command methods in the domain diagram.
         *
         * @param showDomainCommandMethods a boolean flag indicating whether to display domain command methods
         * @return the updated DomainDiagramConfigBuilder instance
         */
        public DomainDiagramConfigBuilder withShowDomainCommandMethods(boolean showDomainCommandMethods) {
            this.showDomainCommandMethods$value = showDomainCommandMethods;
            this.showDomainCommandMethods$set = true;
            return this;
        }

        /**
         * Configures whether domain services should be displayed in the domain diagram.
         *
         * @param showDomainServices a boolean indicating whether to show domain services
         * @return the updated DomainDiagramConfigBuilder instance
         */
        public DomainDiagramConfigBuilder withShowDomainServices(boolean showDomainServices) {
            this.showDomainServices$value = showDomainServices;
            this.showDomainServices$set = true;
            return this;
        }

        /**
         * Configures whether domain service fields should be displayed in the domain diagram.
         *
         * @param showDomainServiceFields a boolean flag indicating whether to display domain service fields
         * @return the updated instance of DomainDiagramConfigBuilder
         */
        public DomainDiagramConfigBuilder withShowDomainServiceFields(boolean showDomainServiceFields) {
            this.showDomainServiceFields$value = showDomainServiceFields;
            this.showDomainServiceFields$set = true;
            return this;
        }

        /**
         * Sets whether to display domain service methods in the domain diagram configuration.
         *
         * @param showDomainServiceMethods a boolean indicating if domain service methods should be shown
         * @return the updated DomainDiagramConfigBuilder instance
         */
        public DomainDiagramConfigBuilder withShowDomainServiceMethods(boolean showDomainServiceMethods) {
            this.showDomainServiceMethods$value = showDomainServiceMethods;
            this.showDomainServiceMethods$set = true;
            return this;
        }

        /**
         * Configures whether application services should be shown in the domain diagram.
         *
         * @param showApplicationServices a boolean indicating if application services should be shown
         * @return the updated DomainDiagramConfigBuilder instance
         */
        public DomainDiagramConfigBuilder withShowApplicationServices(boolean showApplicationServices) {
            this.showApplicationServices$value = showApplicationServices;
            this.showApplicationServices$set = true;
            return this;
        }

        /**
         * Sets the flag to determine whether application service fields should be displayed.
         *
         * @param showApplicationServiceFields a boolean value indicating whether to show application service fields
         * @return the updated DomainDiagramConfigBuilder instance
         */
        public DomainDiagramConfigBuilder withShowApplicationServiceFields(boolean showApplicationServiceFields) {
            this.showApplicationServiceFields$value = showApplicationServiceFields;
            this.showApplicationServiceFields$set = true;
            return this;
        }

        /**
         * Sets the flag to determine whether application service methods should be displayed.
         *
         * @param showApplicationServiceMethods a boolean indicating if application service methods should be shown
         * @return the updated DomainDiagramConfigBuilder instance
         */
        public DomainDiagramConfigBuilder withShowApplicationServiceMethods(boolean showApplicationServiceMethods) {
            this.showApplicationServiceMethods$value = showApplicationServiceMethods;
            this.showApplicationServiceMethods$set = true;
            return this;
        }

        /**
         * Configures whether repositories should be shown in the domain diagram.
         *
         * @param showRepositories a boolean indicating if repositories should be displayed (true) or not (false).
         * @return the updated instance of {@code DomainDiagramConfigBuilder}.
         */
        public DomainDiagramConfigBuilder withShowRepositories(boolean showRepositories) {
            this.showRepositories$value = showRepositories;
            this.showRepositories$set = true;
            return this;
        }

        /**
         * Sets the value indicating whether repository fields should be displayed in the domain diagram.
         *
         * @param showRepositoryFields a boolean specifying whether to show repository fields
         * @return the current instance of DomainDiagramConfigBuilder for method chaining
         */
        public DomainDiagramConfigBuilder withShowRepositoryFields(boolean showRepositoryFields) {
            this.showRepositoryFields$value = showRepositoryFields;
            this.showRepositoryFields$set = true;
            return this;
        }

        /**
         * Sets the flag to specify whether repository methods should be displayed in the domain diagram configuration.
         *
         * @param showRepositoryMethods a boolean indicating whether to show repository methods
         * @return the updated instance of DomainDiagramConfigBuilder
         */
        public DomainDiagramConfigBuilder withShowRepositoryMethods(boolean showRepositoryMethods) {
            this.showRepositoryMethods$value = showRepositoryMethods;
            this.showRepositoryMethods$set = true;
            return this;
        }

        /**
         * Sets the flag indicating whether to show read models in the domain diagram.
         *
         * @param showReadModels a boolean value indicating whether read models should be shown
         * @return the updated DomainDiagramConfigBuilder instance
         */
        public DomainDiagramConfigBuilder withShowReadModels(boolean showReadModels) {
            this.showReadModels$value = showReadModels;
            this.showReadModels$set = true;
            return this;
        }

        /**
         * Sets the flag to determine whether read model fields should be displayed.
         *
         * @param showReadModelFields a boolean value indicating whether read model fields should be shown
         * @return the current instance of {@code DomainDiagramConfigBuilder} for method chaining
         */
        public DomainDiagramConfigBuilder withShowReadModelFields(boolean showReadModelFields) {
            this.showReadModelFields$value = showReadModelFields;
            this.showReadModelFields$set = true;
            return this;
        }

        /**
         * Sets the flag indicating whether to show read model methods in the domain diagram configuration.
         *
         * @param showReadModelMethods a boolean value indicating whether read model methods should be displayed
         * @return the updated instance of DomainDiagramConfigBuilder
         */
        public DomainDiagramConfigBuilder withShowReadModelMethods(boolean showReadModelMethods) {
            this.showReadModelMethods$value = showReadModelMethods;
            this.showReadModelMethods$set = true;
            return this;
        }

        /**
         * Sets whether to include query handlers in the domain diagram configuration.
         *
         * @param showQueryHandlers a boolean indicating whether query handlers should be shown
         * @return the current instance of {@code DomainDiagramConfigBuilder} for method chaining
         */
        public DomainDiagramConfigBuilder withShowQueryHandlers(boolean showQueryHandlers) {
            this.showQueryHandlers$value = showQueryHandlers;
            this.showQueryHandlers$set = true;
            return this;
        }

        /**
         * Sets whether to show query handler fields in the domain diagram configuration.
         *
         * @param showQueryHandlerFields a boolean indicating whether query handler fields should be displayed
         * @return the updated instance of DomainDiagramConfigBuilder
         */
        public DomainDiagramConfigBuilder withShowQueryHandlerFields(boolean showQueryHandlerFields) {
            this.showQueryHandlerFields$value = showQueryHandlerFields;
            this.showQueryHandlerFields$set = true;
            return this;
        }

        /**
         * Sets whether query handler methods should be shown in the domain diagram configuration.
         *
         * @param showQueryHandlerMethods a boolean indicating whether query handler methods should be included
         * @return the updated instance of DomainDiagramConfigBuilder
         */
        public DomainDiagramConfigBuilder withShowQueryHandlerMethods(boolean showQueryHandlerMethods) {
            this.showQueryHandlerMethods$value = showQueryHandlerMethods;
            this.showQueryHandlerMethods$set = true;
            return this;
        }

        /**
         * Configures whether outbound services should be displayed in the domain diagram.
         *
         * @param showOutboundServices a boolean indicating whether to show outbound services
         * @return the updated DomainDiagramConfigBuilder instance
         */
        public DomainDiagramConfigBuilder withShowOutboundServices(boolean showOutboundServices) {
            this.showOutboundServices$value = showOutboundServices;
            this.showOutboundServices$set = true;
            return this;
        }

        /**
         * Configures the builder to display or hide outbound service fields in the domain diagram.
         *
         * @param showOutboundServiceFields a boolean indicating whether outbound service fields should be shown 
         *                                  (true to display, false to hide).
         * @return the current instance of DomainDiagramConfigBuilder for method chaining*/
        public DomainDiagramConfigBuilder withShowOutboundServiceFields(boolean showOutboundServiceFields) {
            this.showOutboundServiceFields$value = showOutboundServiceFields;
            this.showOutboundServiceFields$set = true;
            return this;
        }

        /**
         * Sets whether to show outbound service methods in the domain diagram.
         *
         * @param showOutboundServiceMethods a boolean indicating whether outbound service methods should be displayed
         * @return the current instance of the {@code DomainDiagramConfigBuilder} for method chaining
         */
        public DomainDiagramConfigBuilder withShowOutboundServiceMethods(boolean showOutboundServiceMethods) {
            this.showOutboundServiceMethods$value = showOutboundServiceMethods;
            this.showOutboundServiceMethods$set = true;
            return this;
        }

        /**
         * Configures whether to display unspecified service kinds in the domain diagram.
         *
         * @param showUnspecifiedServiceKinds a boolean indicating whether to show unspecified service kinds
         * @return the {@code DomainDiagramConfigBuilder} instance for method chaining
         */
        public DomainDiagramConfigBuilder withShowUnspecifiedServiceKinds(boolean showUnspecifiedServiceKinds) {
            this.showUnspecifiedServiceKinds$value = showUnspecifiedServiceKinds;
            this.showUnspecifiedServiceKinds$set = true;
            return this;
        }

        /**
         * Configures whether fields with unspecified service kinds will be displayed in the domain diagram.
         *
         * @param showUnspecifiedServiceKindFields a boolean value indicating whether to show fields 
         *                                         with unspecified service kinds
         * @return the updated instance of DomainDiagramConfigBuilder
         */
        public DomainDiagramConfigBuilder withShowUnspecifiedServiceKindFields(boolean showUnspecifiedServiceKindFields) {
            this.showUnspecifiedServiceKindFields$value = showUnspecifiedServiceKindFields;
            this.showUnspecifiedServiceKindFields$set = true;
            return this;
        }

        /**
         * Configures whether to display methods with unspecified service kinds in the domain diagram.
         *
         * @param showUnspecifiedServiceKindMethods a boolean indicating whether to show methods with unspecified service kinds
         * @return the current instance of DomainDiagramConfigBuilder for method chaining
         */
        public DomainDiagramConfigBuilder withShowUnspecifiedServiceKindMethods(boolean showUnspecifiedServiceKindMethods) {
            this.showUnspecifiedServiceKindMethods$value = showUnspecifiedServiceKindMethods;
            this.showUnspecifiedServiceKindMethods$set = true;
            return this;
        }

        /**
         * Sets whether the application service should be called driver.
         *
         * @param callApplicationServiceDriver a boolean indicating if the application service should be called 
         *                                     'driver' in the diagram
         * @return the updated DomainDiagramConfigBuilder instance
         */
        public DomainDiagramConfigBuilder withCallApplicationServiceDriver(boolean callApplicationServiceDriver) {
            this.callApplicationServiceDriver$value = callApplicationServiceDriver;
            this.callApplicationServiceDriver$set = true;
            return this;
        }

        /**
         * Specifies a blacklist of fields that should be excluded from the domain diagram configuration.
         *
         * @param fieldBlacklist a list of field names to be blacklisted
         * @return the current instance of DomainDiagramConfigBuilder with the specified field blacklist applied
         */
        public DomainDiagramConfigBuilder withFieldBlacklist(List<String> fieldBlacklist) {
            this.fieldBlacklist$value = fieldBlacklist;
            this.fieldBlacklist$set = true;
            return this;
        }

        /**
         * Sets the blacklist of method names for the domain diagram configuration.
         *
         * @param methodBlacklist a list of method names to be blacklisted
         * @return the current instance of DomainDiagramConfigBuilder for method chaining
         */
        public DomainDiagramConfigBuilder withMethodBlacklist(List<String> methodBlacklist) {
            this.methodBlacklist$value = methodBlacklist;
            this.methodBlacklist$set = true;
            return this;
        }

        /**
         * Sets the visibility of inherited members in classes for the domain diagram configuration.
         *
         * @param showInheritedMembersInClasses a boolean value indicating whether inherited members 
         *                                       should be shown in classes.
         * @return the current instance of DomainDiagramConfigBuilder with the updated configuration.
         */
        public DomainDiagramConfigBuilder withShowInheritedMembersInClasses(boolean showInheritedMembersInClasses) {
            this.showInheritedMembersInClasses$value = showInheritedMembersInClasses;
            this.showInheritedMembersInClasses$set = true;
            return this;
        }

        /**
         * Sets whether object members should be displayed in classes within the domain diagram configuration.
         *
         * @param showObjectMembersInClasses a boolean indicating whether object members should be shown in classes
         * @return the current instance of DomainDiagramConfigBuilder for method chaining
         */
        public DomainDiagramConfigBuilder withShowObjectMembersInClasses(boolean showObjectMembersInClasses) {
            this.showObjectMembersInClasses$value = showObjectMembersInClasses;
            this.showObjectMembersInClasses$set = true;
            return this;
        }

        /**
         * Sets whether multiplicity information should be included in the label.
         *
         * @param multiplicityInLabel a boolean value indicating if multiplicity 
         *                            should be included in the label
         * @return the updated DomainDiagramConfigBuilder instance
         */
        public DomainDiagramConfigBuilder withMultiplicityInLabel(boolean multiplicityInLabel) {
            this.multiplicityInLabel$value = multiplicityInLabel;
            this.multiplicityInLabel$set = true;
            return this;
        }

        /**
         * Configures whether field stereotypes should be included in the domain diagram.
         *
         * @param fieldStereotypes a boolean value indicating whether to include field stereotypes
         * @return the updated instance of DomainDiagramConfigBuilder
         */
        public DomainDiagramConfigBuilder withFieldStereotypes(boolean fieldStereotypes) {
            this.fieldStereotypes$value = fieldStereotypes;
            this.fieldStereotypes$set = true;
            return this;
        }

        /**
         * Sets the list of transitive filter seed domain service type names for the domain diagram configuration.
         * This allows specific domain service types to be used as seeds for transitive filtering.
         *
         * @param transitiveFilterSeedDomainServiceTypeNames the list of domain service type names to be used as transitive filter seeds
         * @return the current instance of {@code DomainDiagramConfigBuilder} for method chaining
         */
        public DomainDiagramConfigBuilder withTransitiveFilterSeedDomainServiceTypeNames(List<String> transitiveFilterSeedDomainServiceTypeNames) {
            this.transitiveFilterSeedDomainServiceTypeNames$value = transitiveFilterSeedDomainServiceTypeNames;
            this.transitiveFilterSeedDomainServiceTypeNames$set = true;
            return this;
        }

        /**
         * Sets the list of package names to be filtered in the domain diagram configuration. 
         * This allows specific packages to be included during diagram generation.
         *
         * @param filteredPackageNames the list of package names to filter
         * @return the current instance of {@code DomainDiagramConfigBuilder} for method chaining
         */
        public DomainDiagramConfigBuilder withFilteredPackageNames(List<String> filteredPackageNames) {
            this.filteredPackageNames$value = filteredPackageNames;
            this.filteredPackageNames$set = true;
            return this;
        }

        /**
         * Sets whether abstract types should be shown in the domain diagram.
         *
         * @param showAbstractTypes a boolean indicating if abstract types should be displayed
         * @return the current instance of DomainDiagramConfigBuilder for method chaining
         */
        public DomainDiagramConfigBuilder withShowAbstractTypes(boolean showAbstractTypes) {
            this.showAbstractTypes$value = showAbstractTypes;
            this.showAbstractTypes$set = true;
            return this;
        }

        /**
         * Configures whether to use the abstract type name for concrete service kinds in the domain diagram.
         *
         * @param useAbstractTypeNameForConcreteServiceKinds a boolean value indicating whether to use the abstract type name for concrete service kinds
         * @return the updated instance of DomainDiagramConfigBuilder
         */
        public DomainDiagramConfigBuilder withUseAbstractTypeNameForConcreteServiceKinds(boolean useAbstractTypeNameForConcreteServiceKinds) {
            this.useAbstractTypeNameForConcreteServiceKinds$value = useAbstractTypeNameForConcreteServiceKinds;
            this.useAbstractTypeNameForConcreteServiceKinds$set = true;
            return this;
        };

        /**
         * Builds and returns a complete instance of {@code DomainDiagramConfig} using the current state
         * of the builder. If any configuration parameter is not explicitly set, its default value will be used.
         *
         * @return a fully constructed {@code DomainDiagramConfig} instance with all specified or default configurations
         */
        public DomainDiagramConfig build() {

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
            String queryHandlerStyle$value = this.queryHandlerStyle$value;
            if (!this.queryHandlerStyle$set) {
                queryHandlerStyle$value = DomainDiagramConfig.$default$queryHandlerStyle();
            }
            String outboundServiceStyle$value = this.outboundServiceStyle$value;
            if (!this.outboundServiceStyle$set) {
                outboundServiceStyle$value = DomainDiagramConfig.$default$outboundServiceStyle();
            }
            String unspecifiedServiceKindStyle$value = this.unspecifiedServiceKindStyle$value;
            if (!this.unspecifiedServiceKindStyle$set) {
                unspecifiedServiceKindStyle$value = DomainDiagramConfig.$default$unspecifiedServiceKindStyle();
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
            String backgroundColor$value = this.backgroundColor$value;
            if (!this.backgroundColor$set) {
                backgroundColor$value = DomainDiagramConfig.$default$backgroundColor();
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
            boolean showQueryHandlers$value = this.showQueryHandlers$value;
            if (!this.showQueryHandlers$set) {
                showQueryHandlers$value = DomainDiagramConfig.$default$showQueryHandlers();
            }
            boolean showQueryHandlerFields$value = this.showQueryHandlerFields$value;
            if (!this.showQueryHandlerFields$set) {
                showQueryHandlerFields$value = DomainDiagramConfig.$default$showQueryHandlerFields();
            }
            boolean showQueryHandlerMethods$value = this.showQueryHandlerMethods$value;
            if (!this.showQueryHandlerMethods$set) {
                showQueryHandlerMethods$value = DomainDiagramConfig.$default$showQueryHandlerMethods();
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
            boolean showUnspecifiedServiceKinds$value = this.showUnspecifiedServiceKinds$value;
            if (!this.showUnspecifiedServiceKinds$set) {
                showUnspecifiedServiceKinds$value = DomainDiagramConfig.$default$showUnspecifiedServiceKinds();
            }
            boolean showUnspecifiedServiceKindFields$value = this.showUnspecifiedServiceKindFields$value;
            if (!this.showUnspecifiedServiceKindFields$set) {
                showUnspecifiedServiceKindFields$value = DomainDiagramConfig.$default$showUnspecifiedServiceKindFields();
            }
            boolean showUnspecifiedServiceKindMethods$value = this.showUnspecifiedServiceKindMethods$value;
            if (!this.showUnspecifiedServiceKindMethods$set) {
                showUnspecifiedServiceKindMethods$value = DomainDiagramConfig.$default$showUnspecifiedServiceKindMethods();
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
            List<String> filteredPackageNames$value = this.filteredPackageNames$value;
            if (!this.filteredPackageNames$set) {
                filteredPackageNames$value = DomainDiagramConfig.$default$filteredPackageNames();
            }
            boolean showAbstractTypes$value = this.showAbstractTypes$value;
            if (!this.showAbstractTypes$set) {
                showAbstractTypes$value = DomainDiagramConfig.$default$showAbstractTypes();
            }
            boolean useAbstractTypeNameForConcreteServiceKinds$value = this.useAbstractTypeNameForConcreteServiceKinds$value;
            if (!this.showAbstractTypes$set) {
                useAbstractTypeNameForConcreteServiceKinds$value = DomainDiagramConfig.$default$useAbstractTypeNameForConcreteServiceKinds();
            }
            return new DomainDiagramConfig(
                aggregateRootStyle$value,
                aggregateFrameStyle$value,
                entityStyle$value,
                valueObjectStyle$value,
                enumStyle$value,
                identityStyle$value,
                domainEventStyle$value,
                domainCommandStyle$value,
                applicationServiceStyle$value,
                domainServiceStyle$value,
                repositoryStyle$value,
                readModelStyle$value,
                queryHandlerStyle$value,
                outboundServiceStyle$value,
                unspecifiedServiceKindStyle$value,
                font$value,
                direction$value,
                ranker$value,
                acycler$value,
                backgroundColor$value,
                classesBlacklist$value,
                showFields$value,
                showFullQualifiedClassNames$value,
                showAssertions$value,
                showMethods$value,
                showOnlyPublicMethods$value,
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
                fieldBlacklist$value,
                methodBlacklist$value,
                showInheritedMembersInClasses$value,
                showObjectMembersInClasses$value,
                multiplicityInLabel$value,
                fieldStereotypes$value,
                transitiveFilterSeedDomainServiceTypeNames$value,
                filteredPackageNames$value,
                showAbstractTypes$value,
                useAbstractTypeNameForConcreteServiceKinds$value
            );
        }

        /**
         * Returns a string representation of the DomainDiagramConfig.DomainDiagramConfigBuilder object.
         * The representation includes the values of various configuration properties.
         *
         * @return a string describing the DomainDiagramConfig.DomainDiagramConfigBuilder object
         */
        public String toString() {
            return "DomainDiagramConfig.DomainDiagramConfigBuilder(" +
                ", aggregateRootStyle$value=" +
                this.aggregateRootStyle$value +
                ", aggregateFrameStyle$value=" +
                this.aggregateFrameStyle$value +
                ", entityStyle$value=" +
                this.entityStyle$value +
                ", valueObjectStyle$value=" +
                this.valueObjectStyle$value +
                ", enumStyle$value=" +
                this.enumStyle$value +
                ", identityStyle$value=" +
                this.identityStyle$value +
                ", domainEventStyle$value=" +
                this.domainEventStyle$value +
                ", domainCommandStyle$value=" +
                this.domainCommandStyle$value +
                ", applicationServiceStyle$value=" +
                this.applicationServiceStyle$value +
                ", domainServiceStyle$value=" +
                this.domainServiceStyle$value +
                ", repositoryStyle$value=" +
                this.repositoryStyle$value +
                ", readModelStyle$value=" +
                this.readModelStyle$value +
                ", queryHandlerStyle$value=" +
                this.queryHandlerStyle$value +
                ", outboundServiceStyle$value=" +
                this.outboundServiceStyle$value +
                ", unspecifiedServiceKindStyle$value=" +
                this.unspecifiedServiceKindStyle$value +
                ", font$value=" +
                this.font$value +
                ", direction$value=" +
                this.direction$value +
                ", ranker$value=" +
                this.ranker$value +
                ", acycler$value=" +
                this.acycler$value +
                ", backgroundColor$value=" +
                this.backgroundColor$value +
                ", classesBlacklist$value=" +
                this.classesBlacklist$value +
                ", showFields$value=" +
                this.showFields$value +
                ", showFullQualifiedClassNames$value=" +
                this.showFullQualifiedClassNames$value +
                ", showAssertions$value=" +
                this.showAssertions$value +
                ", showMethods$value=" +
                this.showMethods$value +
                ", showOnlyPublicMethods$value=" +
                this.showOnlyPublicMethods$value +
                ", showDomainEvents$value=" +
                this.showDomainEvents$value +
                ", showDomainEventFields$value=" +
                this.showDomainEventFields$value +
                ", showDomainEventMethods$value=" +
                this.showDomainEventMethods$value +
                ", showDomainCommands$value=" +
                this.showDomainCommands$value +
                ", showOnlyTopLevelDomainCommandRelations$value=" +
                this.showOnlyTopLevelDomainCommandRelations$value +
                ", showDomainCommandFields$value=" +
                this.showDomainCommandFields$value +
                ", showDomainCommandMethods$value=" +
                this.showDomainCommandMethods$value +
                ", showDomainServices$value=" +
                this.showDomainServices$value +
                ", showDomainServiceFields$value=" +
                this.showDomainServiceFields$value +
                ", showDomainServiceMethods$value=" +
                this.showDomainServiceMethods$value +
                ", showApplicationServices$value=" +
                this.showApplicationServices$value +
                ", showApplicationServiceFields$value=" +
                this.showApplicationServiceFields$value +
                ", showApplicationServiceMethods$value=" +
                this.showApplicationServiceMethods$value +
                ", showRepositories$value=" +
                this.showRepositories$value +
                ", showRepositoryFields$value=" +
                this.showRepositoryFields$value +
                ", showRepositoryMethods$value=" +
                this.showRepositoryMethods$value +
                ", showReadModels$value=" +
                this.showReadModels$value +
                ", showReadModelFields$value=" +
                this.showReadModelFields$value +
                ", showReadModelMethods$value=" +
                this.showReadModelMethods$value +
                ", showQueryHandlers$value=" +
                this.showQueryHandlers$value +
                ", showQueryHandlerFields$value=" +
                this.showQueryHandlerFields$value +
                ", showQueryHandlerMethods$value=" +
                this.showQueryHandlerMethods$value +
                ", showOutboundServices$value=" +
                this.showOutboundServices$value +
                ", showOutboundServiceFields$value=" +
                this.showOutboundServiceFields$value +
                ", showOutboundServiceMethods$value=" +
                this.showOutboundServiceMethods$value +
                ", showUnspecifiedServiceKinds$value=" +
                this.showUnspecifiedServiceKinds$value +
                ", showUnspecifiedServiceKindFields$value=" +
                this.showUnspecifiedServiceKindFields$value +
                ", showUnspecifiedServiceKindMethods$value=" +
                this.showUnspecifiedServiceKindMethods$value +
                ", callApplicationServiceDriver$value=" +
                this.callApplicationServiceDriver$value +
                ", fieldBlacklist$value=" +
                this.fieldBlacklist$value +
                ", methodBlacklist$value=" +
                this.methodBlacklist$value +
                ", showInheritedMembersInClasses$value=" +
                this.showInheritedMembersInClasses$value +
                ", showObjectMembersInClasses$value=" +
                this.showObjectMembersInClasses$value +
                ", multiplicityInLabel$value=" +
                this.multiplicityInLabel$value +
                ", fieldStereotypes$value=" +
                this.fieldStereotypes$value +
                ", transitiveFilterSeedDomainServiceTypeNames$value=" +
                this.transitiveFilterSeedDomainServiceTypeNames$value +
                ", filteredPackageNames$value=" +
                this.filteredPackageNames$value +
                ", showAbstractTypes$value=" +
                this.showAbstractTypes$value +
                ", useAbstractTypeNameForConcreteServiceKinds$value=" +
                this.useAbstractTypeNameForConcreteServiceKinds$value +
                ")";
        }
    }
}
