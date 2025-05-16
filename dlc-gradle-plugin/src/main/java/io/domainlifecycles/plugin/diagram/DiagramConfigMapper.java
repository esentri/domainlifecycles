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

import io.domainlifecycles.plugin.extensions.PluginDiagramConfigurationExtension;
import io.domainlifecycles.plugins.diagram.DiagramConfig;
import io.domainlifecycles.plugins.diagram.FileType;

/**
 * The DiagramConfigMapper class is responsible for mapping configuration settings
 * from a {@link PluginDiagramConfigurationExtension} instance to a {@link DiagramConfig} instance.
 * It reads and transforms configuration properties from the provided extension object and applies
 * appropriate default values where necessary.
 *
 * This class serves as a utility to ensure that all relevant configurations are correctly
 * mapped, allowing for the generation of a diagram based on user-defined or default settings.
 *
 * @author Mario Herb
 */
public class DiagramConfigMapper {

    private static final String DEFAULT_DIAGRAM_FILE_NAME = "diagram";

    /**
     * Maps the given {@code PluginDiagramConfigurationExtension} to a {@code DiagramConfig} instance,
     * configuring various diagram settings based on the provided extension.
     *
     * @param extension the {@code PluginDiagramConfigurationExtension} containing configuration options for the diagram
     * @return a {@code DiagramConfig} instance populated with the settings from the provided extension
     */
    public static DiagramConfig map(PluginDiagramConfigurationExtension extension) {
        DiagramConfig diagramConfig = new DiagramConfig();

        diagramConfig.setFileType(FileType.byName(extension.getFormat().getOrNull()));
        diagramConfig.setFileName(extension.getFileName().getOrElse(DEFAULT_DIAGRAM_FILE_NAME));
        diagramConfig.setExplicitlyIncludedPackageNames(extension.getExplicitlyIncludedPackages().getOrNull());
        diagramConfig.setShowAbstractTypes(extension.getShowAbstractTypes().getOrNull());
        diagramConfig.setUseAbstractTypeNameForConcreteServiceKinds(extension.getUseAbstractTypeNameForConcreteServiceKinds().getOrNull());
        diagramConfig.setAggregateRootStyle(extension.getAggregateRootStyle().getOrNull());
        diagramConfig.setAggregateFrameStyle(extension.getAggregateFrameStyle().getOrNull());
        diagramConfig.setEntityStyle(extension.getEntityStyle().getOrNull());
        diagramConfig.setValueObjectStyle(extension.getValueObjectStyle().getOrNull());
        diagramConfig.setEnumStyle(extension.getEnumStyle().getOrNull());
        diagramConfig.setIdentityStyle(extension.getIdentityStyle().getOrNull());
        diagramConfig.setDomainEventStyle(extension.getDomainEventStyle().getOrNull());
        diagramConfig.setDomainCommandStyle(extension.getDomainCommandStyle().getOrNull());
        diagramConfig.setApplicationServiceStyle(extension.getApplicationServiceStyle().getOrNull());
        diagramConfig.setDomainServiceStyle(extension.getDomainServiceStyle().getOrNull());
        diagramConfig.setRepositoryStyle(extension.getRepositoryStyle().getOrNull());
        diagramConfig.setReadModelStyle(extension.getReadModelStyle().getOrNull());
        diagramConfig.setQueryHandlerStyle(extension.getQueryHandlerStyle().getOrNull());
        diagramConfig.setOutboundServiceStyle(extension.getOutboundServiceStyle().getOrNull());
        diagramConfig.setFont(extension.getFont().getOrNull());
        diagramConfig.setDirection(extension.getDirection().getOrNull());
        diagramConfig.setRanker(extension.getRanker().getOrNull());
        diagramConfig.setAcycler(extension.getAcycler().getOrNull());
        diagramConfig.setBackgroundColor(extension.getBackgroundColor().getOrNull());
        diagramConfig.setClassesBlacklist(extension.getClassesBlacklist().getOrNull());
        diagramConfig.setShowFields(extension.getShowFields().getOrNull());
        diagramConfig.setShowFullQualifiedClassNames(extension.getShowFullQualifiedClassNames().getOrNull());
        diagramConfig.setShowAssertions(extension.getShowAssertions().getOrNull());
        diagramConfig.setShowMethods(extension.getShowMethods().getOrNull());
        diagramConfig.setShowOnlyPublicMethods(extension.getShowOnlyPublicMethods().getOrNull());
        diagramConfig.setShowAggregates(extension.getShowAggregates().getOrNull());
        diagramConfig.setShowAggregateFields(extension.getShowAggregateFields().getOrNull());
        diagramConfig.setShowAggregateMethods(extension.getShowAggregateMethods().getOrNull());
        diagramConfig.setShowDomainEvents(extension.getShowDomainEvents().getOrNull());
        diagramConfig.setShowDomainEventFields(extension.getShowDomainEventFields().getOrNull());
        diagramConfig.setShowDomainEventMethods(extension.getShowDomainEventMethods().getOrNull());
        diagramConfig.setShowDomainCommands(extension.getShowDomainCommands().getOrNull());
        diagramConfig.setShowOnlyTopLevelDomainCommandRelations(extension.getShowOnlyTopLevelDomainCommandRelations().getOrNull());
        diagramConfig.setShowDomainCommandFields(extension.getShowDomainCommandFields().getOrNull());
        diagramConfig.setShowDomainCommandMethods(extension.getShowDomainCommandMethods().getOrNull());
        diagramConfig.setShowDomainServices(extension.getShowDomainServices().getOrNull());
        diagramConfig.setShowDomainServiceFields(extension.getShowDomainServiceFields().getOrNull());
        diagramConfig.setShowDomainServiceMethods(extension.getShowDomainServiceMethods().getOrNull());
        diagramConfig.setShowApplicationServices(extension.getShowApplicationServices().getOrNull());
        diagramConfig.setShowApplicationServiceFields(extension.getShowApplicationServiceFields().getOrNull());
        diagramConfig.setShowApplicationServiceMethods(extension.getShowApplicationServiceMethods().getOrNull());
        diagramConfig.setShowRepositories(extension.getShowRepositories().getOrNull());
        diagramConfig.setShowRepositoryFields(extension.getShowRepositoryFields().getOrNull());
        diagramConfig.setShowRepositoryMethods(extension.getShowRepositoryMethods().getOrNull());
        diagramConfig.setShowReadModels(extension.getShowReadModels().getOrNull());
        diagramConfig.setShowReadModelFields(extension.getShowReadModelFields().getOrNull());
        diagramConfig.setShowReadModelMethods(extension.getShowReadModelMethods().getOrNull());
        diagramConfig.setShowQueryHandlers(extension.getShowQueryHandlers().getOrNull());
        diagramConfig.setShowQueryHandlerFields(extension.getShowQueryHandlerFields().getOrNull());
        diagramConfig.setShowQueryHandlerMethods(extension.getShowQueryHandlerMethods().getOrNull());
        diagramConfig.setShowOutboundServices(extension.getShowOutboundServices().getOrNull());
        diagramConfig.setShowOutboundServiceFields(extension.getShowOutboundServiceFields().getOrNull());
        diagramConfig.setShowOutboundServiceMethods(extension.getShowOutboundServiceMethods().getOrNull());
        diagramConfig.setShowUnspecifiedServiceKinds(extension.getShowUnspecifiedServiceKinds().getOrNull());
        diagramConfig.setShowUnspecifiedServiceKindFields(extension.getShowUnspecifiedServiceKindFields().getOrNull());
        diagramConfig.setShowUnspecifiedServiceKindMethods(extension.getShowUnspecifiedServiceKindMethods().getOrNull());
        diagramConfig.setCallApplicationServiceDriver(extension.getCallApplicationServiceDriver().getOrNull());
        diagramConfig.setFieldBlacklist(extension.getFieldBlacklist().getOrNull());
        diagramConfig.setMethodBlacklist(extension.getMethodBlacklist().getOrNull());
        diagramConfig.setShowInheritedMembersInClasses(extension.getShowInheritedMembersInClasses().getOrNull());
        diagramConfig.setShowObjectMembersInClasses(extension.getShowObjectMembersInClasses().getOrNull());
        diagramConfig.setMultiplicityInLabel(extension.getMultiplicityInLabel().getOrNull());
        diagramConfig.setFieldStereotypes(extension.getFieldStereotypes().getOrNull());
        diagramConfig.setTransitiveFilterSeedDomainServiceTypeNames(extension.getTransitiveFilterSeedDomainServiceTypeNames().getOrNull());
        return diagramConfig;
    }
}
