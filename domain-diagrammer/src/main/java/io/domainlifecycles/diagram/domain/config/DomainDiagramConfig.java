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
import io.domainlifecycles.diagram.domain.mapper.TransitiveDomainTypeFilter;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * Configuration options for a Domain Diagram
 *
 * @author Mario Herb
 */
@Builder(toBuilder = true, setterPrefix = "with")
@Getter
public class DomainDiagramConfig implements DiagramConfig {

    /**
     * A DomainDiagram is meant focus on domain elements which are contained in one specific package (e.g. bounded
     * context).
     * The package name must be specified.
     */
    @Builder.Default
    private String contextPackageName = null;

    /**
     * Style declaration for AggregateRoots (see Nomnoml style options)
     */
    @Builder.Default
    private String aggregateRootStyle = "fill=#8f8f bold";
    /**
     * Style declaration for AggregateRoot frames (see Nomnoml style options)
     */
    @Builder.Default
    private String aggregateFrameStyle = "visual=frame align=left";
    /**
     * Style declaration for Entities  (see Nomnoml style options)
     */
    @Builder.Default
    private String entityStyle = "fill=#88AAFF bold";
    /**
     * Style declaration for ValueObjects  (see Nomnoml style options)
     */
    @Builder.Default
    private String valueObjectStyle = "fill=#FFFFCC bold";
    /**
     * Style declaration for Enums  (see Nomnoml style options)
     */
    @Builder.Default
    private String enumStyle = "fill=#FFFFCC bold";
    /**
     * Style declaration for Identities  (see Nomnoml style options)
     */
    @Builder.Default
    private String identityStyle = "fill=#FFFFCC bold";
    /**
     * Style declaration for DomainEvents  (see Nomnoml style options)
     */
    @Builder.Default
    private String domainEventStyle = "fill=#CCFFFF bold";
    /**
     * Style declaration for DomainCommands  (see Nomnoml style options)
     */
    @Builder.Default
    private String domainCommandStyle = "fill=#FFB266 bold";
    /**
     * Style declaration for ApplicationServices (see Nomnoml style options)
     */
    @Builder.Default
    private String applicationServiceStyle = "bold";
    /**
     * Style declaration for DomainServices  (see Nomnoml style options)
     */
    @Builder.Default
    private String domainServiceStyle = "fill=#E0E0E0 bold";
    /**
     * Style declaration for Repositories  (see Nomnoml style options)
     */
    @Builder.Default
    private String repositoryStyle = "fill=#C0C0C0 bold";
    /**
     * Style declaration for ReadModels  (see Nomnoml style options)
     */
    @Builder.Default
    private String readModelStyle = "fill=#FFCCE5 bold";
    /**
     * Style declaration for QueryClients  (see Nomnoml style options)
     */
    @Builder.Default
    private String queryClientStyle = "fill=#C0C0C0 bold";
    /**
     * Style declaration for OutboundServices  (see Nomnoml style options)
     */
    @Builder.Default
    private String outboundServiceStyle = "fill=#C0C0C0 bold";
    /**
     * General font style declaration  (see Nomnoml style options)
     */
    @Builder.Default
    private String font = "Courier";
    /**
     * General layout direction style declaration (see Nomnoml style options, 'down' or 'right' is supported)
     */
    @Builder.Default
    private String direction = "down";

    /**
     * General layout direction style declaration (see Nomnoml style options, 'network-simplex' or 'tight-tree' or
     * 'longest-path' is supported)
     */
    @Builder.Default
    private String ranker = "longest-path";

    /**
     * General acycling style declaration  (see Nomnoml style options, only 'greedy' supported)
     */
    @Builder.Default
    private String acycler = "greedy";

    /**
     * Full qualified class names to be excluded from the diagram
     */
    @Builder.Default
    private List<String> classesBlacklist = Collections.emptyList();
    /**
     * If false, generally no fields are included
     */
    @Builder.Default
    private boolean showFields = true;
    /**
     * If true, generally full qualified class names are used
     */
    @Builder.Default
    private boolean showFullQualifiedClassNames = false;
    /**
     * If true, assertions (currently only if specified by Bean Validation Annotations) are included.
     */
    @Builder.Default
    private boolean showAssertions = true;
    /**
     * If false, generally no methods are included
     */
    @Builder.Default
    private boolean showMethods = true;
    /**
     * If true, generally only public methods are included
     */
    @Builder.Default
    private boolean showOnlyPublicMethods = true;
    /**
     * If true, DomainEvent classes are included
     */
    @Builder.Default
    private boolean showDomainEvents = true;
    /**
     * If true, fields of DomainEvents are included
     */
    @Builder.Default
    private boolean showDomainEventFields = false;
    /**
     * If true, methods of DomainEvents are included
     */
    @Builder.Default
    private boolean showDomainEventMethods = false;
    /**
     * If true, DomainCommand classes are included
     */
    @Builder.Default
    private boolean showDomainCommands = true;
    /**
     * DomainCommands might be passed down to subsequent classes in the flow.
     * If true, DomainCommands relations are only drawn on the top most processing class.
     */
    @Builder.Default
    private boolean showOnlyTopLevelDomainCommandRelations = true;
    /**
     * If true, fields of DomainCommands are included
     */
    @Builder.Default
    private boolean showDomainCommandFields = false;
    /**
     * If true, methods of DomainCommands are included
     */
    @Builder.Default
    private boolean showDomainCommandMethods = false;
    /**
     * If true, DomainService classes are included
     */
    @Builder.Default
    private boolean showDomainServices = true;
    /**
     * If true, fields of DomainServices are included
     */
    @Builder.Default
    private boolean showDomainServiceFields = false;
    /**
     * If true, methods of DomainServices are included
     */
    @Builder.Default
    private boolean showDomainServiceMethods = true;
    /**
     * If true, ApplicationService/Driver classes are included
     */
    @Builder.Default
    private boolean showApplicationServices = true;
    /**
     * If true, fields of ApplicationServices/Drivers are included
     */
    @Builder.Default
    private boolean showApplicationServiceFields = false;
    /**
     * If true, methods of ApplicationServices/Drivers are included
     */
    @Builder.Default
    private boolean showApplicationServiceMethods = true;
    /**
     * If true, Repository classes are included
     */
    @Builder.Default
    private boolean showRepositories = true;
    /**
     * If true, fields of Repositories are included
     */
    @Builder.Default
    private boolean showRepositoryFields = false;
    /**
     * If true, methods of Repositories are included
     */
    @Builder.Default
    private boolean showRepositoryMethods = true;
    /**
     * If true, ReadModel classes are included
     */
    @Builder.Default
    private boolean showReadModels = true;
    /**
     * If true, fields of ReadModels are included
     */
    @Builder.Default
    private boolean showReadModelFields = true;
    /**
     * If true, methods of ReadModels are included
     */
    @Builder.Default
    private boolean showReadModelMethods = false;
    /**
     * If true, QueryClient classes are included
     */
    @Builder.Default
    private boolean showQueryClients = true;
    /**
     * If true, fields of QueryClients are included
     */
    @Builder.Default
    private boolean showQueryClientFields = false;
    /**
     * If true, methods of QueryClients are included
     */
    @Builder.Default
    private boolean showQueryClientMethods = false;
    /**
     * If true, OutboundService classes are included
     */
    @Builder.Default
    private boolean showOutboundServices = true;
    /**
     * If true, fields of OutboundServices are included
     */
    @Builder.Default
    private boolean showOutboundServiceFields = false;
    /**
     * If true, methods of OutboundServices are included
     */
    @Builder.Default
    private boolean showOutboundServiceMethods = false;

    /**
     * If true, unspecified ServiceKind classes are included
     */
    @Builder.Default
    private boolean showUnspecifiedServiceKinds = true;
    /**
     * If true, fields of unspecified ServiceKinds are included
     */
    @Builder.Default
    private boolean showUnspecifiedServiceKindFields = false;
    /**
     * If true, methods of unspecified ServiceKinds are included
     */
    @Builder.Default
    private boolean showUnspecifiedServiceKindMethods = false;

    /**
     * If true, the stereotype {@code <Driver>} is used instead of {@code <ApplicationService>}
     */
    @Builder.Default
    private boolean callApplicationServiceDriver = true;

    /**
     * Fields with named like elements of this black list are excluded
     */
    @Builder.Default
    private List<String> fieldBlacklist = List.of("concurrencyVersion");

    /**
     * Methods with named like elements of this black list are excluded
     */
    @Builder.Default
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
    @Builder.Default
    private boolean showInheritedMembersInClasses = true;
    /**
     * If true, members declared by {@code java.lang.Object} are included
     */
    @Builder.Default
    private boolean showObjectMembersInClasses = true;

    /**
     * If true, multiplicity is added to the associations label.
     */
    @Builder.Default
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
    @Builder.Default
    private boolean fieldStereotypes = true;

    /**
     * Enabling and initializing the seed for the {@link TransitiveDomainTypeFilter}.
     */
    @Builder.Default
    private List<String> transitiveFilterSeedDomainServiceTypeNames = Collections.emptyList();


}
