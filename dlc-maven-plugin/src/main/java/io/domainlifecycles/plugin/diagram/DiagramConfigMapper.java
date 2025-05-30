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

import io.domainlifecycles.plugins.diagram.DiagramConfig;
import io.domainlifecycles.plugins.diagram.FileType;

/**
 * Utility class for mapping configuration data from a PluginDiagramConfiguration
 * object to a DiagramConfig object.
 *
 * @author Leon Völlinger
 */
public class DiagramConfigMapper {

    /**
     * Maps the properties of a {@link PluginDiagramConfiguration} instance to a new {@link DiagramConfig} instance.
     *
     * @param mavenDiagramConfig the {@link PluginDiagramConfiguration} object containing the configuration values to be mapped
     * @return a new {@link DiagramConfig} instance with all properties mapped from the provided {@link PluginDiagramConfiguration}
     */
    public static DiagramConfig map(PluginDiagramConfiguration mavenDiagramConfig) {
        DiagramConfig diagramConfig = new DiagramConfig();

        diagramConfig.setFileType(FileType.byName(mavenDiagramConfig.getFormat()));
        diagramConfig.setFileName(mavenDiagramConfig.getFileName());
        diagramConfig.setExplicitlyIncludedPackageNames(mavenDiagramConfig.getExplicitlyIncludedPackages());
        diagramConfig.setAggregateRootStyle(mavenDiagramConfig.getAggregateRootStyle());
        diagramConfig.setAggregateFrameStyle(mavenDiagramConfig.getAggregateFrameStyle());
        diagramConfig.setEntityStyle(mavenDiagramConfig.getEntityStyle());
        diagramConfig.setValueObjectStyle(mavenDiagramConfig.getValueObjectStyle());
        diagramConfig.setEnumStyle(mavenDiagramConfig.getEnumStyle());
        diagramConfig.setIdentityStyle(mavenDiagramConfig.getIdentityStyle());
        diagramConfig.setDomainEventStyle(mavenDiagramConfig.getDomainEventStyle());
        diagramConfig.setDomainCommandStyle(mavenDiagramConfig.getDomainCommandStyle());
        diagramConfig.setApplicationServiceStyle(mavenDiagramConfig.getApplicationServiceStyle());
        diagramConfig.setDomainServiceStyle(mavenDiagramConfig.getDomainServiceStyle());
        diagramConfig.setRepositoryStyle(mavenDiagramConfig.getRepositoryStyle());
        diagramConfig.setReadModelStyle(mavenDiagramConfig.getReadModelStyle());
        diagramConfig.setQueryHandlerStyle(mavenDiagramConfig.getQueryHandlerStyle());
        diagramConfig.setOutboundServiceStyle(mavenDiagramConfig.getOutboundServiceStyle());
        diagramConfig.setFont(mavenDiagramConfig.getFont());
        diagramConfig.setDirection(mavenDiagramConfig.getDirection());
        diagramConfig.setRanker(mavenDiagramConfig.getRanker());
        diagramConfig.setAcycler(mavenDiagramConfig.getAcycler());
        diagramConfig.setBackgroundColor(mavenDiagramConfig.getBackgroundColor());
        diagramConfig.setClassesBlacklist(mavenDiagramConfig.getClassesBlacklist());
        diagramConfig.setShowFields(mavenDiagramConfig.getShowFields());
        diagramConfig.setShowFullQualifiedClassNames(mavenDiagramConfig.getShowFullQualifiedClassNames());
        diagramConfig.setShowAssertions(mavenDiagramConfig.getShowAssertions());
        diagramConfig.setShowMethods(mavenDiagramConfig.getShowMethods());
        diagramConfig.setShowOnlyPublicMethods(mavenDiagramConfig.getShowOnlyPublicMethods());
        diagramConfig.setShowAggregates(mavenDiagramConfig.getShowAggregates());
        diagramConfig.setShowAggregateFields(mavenDiagramConfig.getShowAggregateFields());
        diagramConfig.setShowAggregateMethods(mavenDiagramConfig.getShowAggregateMethods());
        diagramConfig.setShowDomainEvents(mavenDiagramConfig.getShowDomainEvents());
        diagramConfig.setShowDomainEventFields(mavenDiagramConfig.getShowDomainEventFields());
        diagramConfig.setShowDomainEventMethods(mavenDiagramConfig.getShowDomainEventMethods());
        diagramConfig.setShowDomainCommands(mavenDiagramConfig.getShowDomainCommands());
        diagramConfig.setShowOnlyTopLevelDomainCommandRelations(mavenDiagramConfig.getShowOnlyTopLevelDomainCommandRelations());
        diagramConfig.setShowDomainCommandFields(mavenDiagramConfig.getShowDomainCommandFields());
        diagramConfig.setShowDomainCommandMethods(mavenDiagramConfig.getShowDomainCommandMethods());
        diagramConfig.setShowDomainServices(mavenDiagramConfig.getShowDomainServices());
        diagramConfig.setShowDomainServiceFields(mavenDiagramConfig.getShowDomainServiceFields());
        diagramConfig.setShowDomainServiceMethods(mavenDiagramConfig.getShowDomainServiceMethods());
        diagramConfig.setShowApplicationServices(mavenDiagramConfig.getShowApplicationServices());
        diagramConfig.setShowApplicationServiceFields(mavenDiagramConfig.getShowApplicationServiceFields());
        diagramConfig.setShowApplicationServiceMethods(mavenDiagramConfig.getShowApplicationServiceMethods());
        diagramConfig.setShowRepositories(mavenDiagramConfig.getShowRepositories());
        diagramConfig.setShowRepositoryFields(mavenDiagramConfig.getShowRepositoryFields());
        diagramConfig.setShowRepositoryMethods(mavenDiagramConfig.getShowRepositoryMethods());
        diagramConfig.setShowReadModels(mavenDiagramConfig.getShowReadModels());
        diagramConfig.setShowReadModelFields(mavenDiagramConfig.getShowReadModelFields());
        diagramConfig.setShowReadModelMethods(mavenDiagramConfig.getShowReadModelMethods());
        diagramConfig.setShowQueryHandlers(mavenDiagramConfig.getShowQueryHandlers());
        diagramConfig.setShowQueryHandlerFields(mavenDiagramConfig.getShowQueryHandlerFields());
        diagramConfig.setShowQueryHandlerMethods(mavenDiagramConfig.getShowQueryHandlerMethods());
        diagramConfig.setShowOutboundServices(mavenDiagramConfig.getShowOutboundServices());
        diagramConfig.setShowOutboundServiceFields(mavenDiagramConfig.getShowOutboundServiceFields());
        diagramConfig.setShowOutboundServiceMethods(mavenDiagramConfig.getShowOutboundServiceMethods());
        diagramConfig.setShowUnspecifiedServiceKinds(mavenDiagramConfig.getShowUnspecifiedServiceKinds());
        diagramConfig.setShowUnspecifiedServiceKindFields(mavenDiagramConfig.getShowUnspecifiedServiceKindFields());
        diagramConfig.setShowUnspecifiedServiceKindMethods(mavenDiagramConfig.getShowUnspecifiedServiceKindMethods());
        diagramConfig.setCallApplicationServiceDriver(mavenDiagramConfig.getCallApplicationServiceDriver());
        diagramConfig.setFieldBlacklist(mavenDiagramConfig.getFieldBlacklist());
        diagramConfig.setMethodBlacklist(mavenDiagramConfig.getMethodBlacklist());
        diagramConfig.setShowInheritedMembersInClasses(mavenDiagramConfig.getShowInheritedMembersInClasses());
        diagramConfig.setShowObjectMembersInClasses(mavenDiagramConfig.getShowObjectMembersInClasses());
        diagramConfig.setMultiplicityInLabel(mavenDiagramConfig.getMultiplicityInLabel());
        diagramConfig.setFieldStereotypes(mavenDiagramConfig.getFieldStereotypes());
        diagramConfig.setIncludeConnectedTo(mavenDiagramConfig.getIncludeConnectedTo());
        diagramConfig.setIncludeConnectedToIngoing(mavenDiagramConfig.getIncludeConnectedToIngoing());
        diagramConfig.setIncludeConnectedToOutgoing(mavenDiagramConfig.getIncludeConnectedToOutgoing());
        diagramConfig.setExcludeConnectedToIngoing(mavenDiagramConfig.getExcludeConnectedToIngoing());
        diagramConfig.setExcludeConnectedToOutgoing(mavenDiagramConfig.getExcludeConnectedToOutgoing());
        diagramConfig.setShowAllInheritanceStructures(mavenDiagramConfig.getShowAllInheritanceStructures());
        diagramConfig.setShowInheritanceStructuresInAggregates(mavenDiagramConfig.getShowInheritanceStructuresInAggregates());
        diagramConfig.setShowInheritanceStructuresForDomainEvents(mavenDiagramConfig.getShowInheritanceStructuresForDomainEvents());
        diagramConfig.setShowInheritanceStructuresForDomainCommands(mavenDiagramConfig.getShowInheritanceStructuresForDomainCommands());
        diagramConfig.setShowInheritanceStructuresForServiceKinds(mavenDiagramConfig.getShowInheritanceStructuresForServiceKinds());
        diagramConfig.setShowInheritanceStructuresForReadModels(mavenDiagramConfig.getShowInheritanceStructuresForReadModels());

        return diagramConfig;
    }
}
