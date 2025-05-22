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
    private Boolean showAbstractAllTypes;
    private Boolean showAbstractTypesInAggregates;
    private Boolean useAbstractTypeNameForConcreteServiceKinds;

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAggregateRootStyle() {
        return aggregateRootStyle;
    }

    public void setAggregateRootStyle(String aggregateRootStyle) {
        this.aggregateRootStyle = aggregateRootStyle;
    }

    public String getAggregateFrameStyle() {
        return aggregateFrameStyle;
    }

    public void setAggregateFrameStyle(String aggregateFrameStyle) {
        this.aggregateFrameStyle = aggregateFrameStyle;
    }

    public String getEntityStyle() {
        return entityStyle;
    }

    public void setEntityStyle(String entityStyle) {
        this.entityStyle = entityStyle;
    }

    public String getValueObjectStyle() {
        return valueObjectStyle;
    }

    public void setValueObjectStyle(String valueObjectStyle) {
        this.valueObjectStyle = valueObjectStyle;
    }

    public String getEnumStyle() {
        return enumStyle;
    }

    public void setEnumStyle(String enumStyle) {
        this.enumStyle = enumStyle;
    }

    public String getIdentityStyle() {
        return identityStyle;
    }

    public void setIdentityStyle(String identityStyle) {
        this.identityStyle = identityStyle;
    }

    public String getDomainEventStyle() {
        return domainEventStyle;
    }

    public void setDomainEventStyle(String domainEventStyle) {
        this.domainEventStyle = domainEventStyle;
    }

    public String getDomainCommandStyle() {
        return domainCommandStyle;
    }

    public void setDomainCommandStyle(String domainCommandStyle) {
        this.domainCommandStyle = domainCommandStyle;
    }

    public String getApplicationServiceStyle() {
        return applicationServiceStyle;
    }

    public void setApplicationServiceStyle(String applicationServiceStyle) {
        this.applicationServiceStyle = applicationServiceStyle;
    }

    public String getDomainServiceStyle() {
        return domainServiceStyle;
    }

    public void setDomainServiceStyle(String domainServiceStyle) {
        this.domainServiceStyle = domainServiceStyle;
    }

    public String getRepositoryStyle() {
        return repositoryStyle;
    }

    public void setRepositoryStyle(String repositoryStyle) {
        this.repositoryStyle = repositoryStyle;
    }

    public String getReadModelStyle() {
        return readModelStyle;
    }

    public void setReadModelStyle(String readModelStyle) {
        this.readModelStyle = readModelStyle;
    }

    public String getQueryHandlerStyle() {
        return queryHandlerStyle;
    }

    public void setQueryHandlerStyle(String queryHandlerStyle) {
        this.queryHandlerStyle = queryHandlerStyle;
    }

    public String getOutboundServiceStyle() {
        return outboundServiceStyle;
    }

    public void setOutboundServiceStyle(String outboundServiceStyle) {
        this.outboundServiceStyle = outboundServiceStyle;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getRanker() {
        return ranker;
    }

    public void setRanker(String ranker) {
        this.ranker = ranker;
    }

    public String getAcycler() {
        return acycler;
    }

    public void setAcycler(String acycler) {
        this.acycler = acycler;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public List<String> getClassesBlacklist() {
        return classesBlacklist;
    }

    public void setClassesBlacklist(List<String> classesBlacklist) {
        this.classesBlacklist = classesBlacklist;
    }

    public Boolean getShowFields() {
        return showFields;
    }

    public void setShowFields(Boolean showFields) {
        this.showFields = showFields;
    }

    public Boolean getShowFullQualifiedClassNames() {
        return showFullQualifiedClassNames;
    }

    public void setShowFullQualifiedClassNames(Boolean showFullQualifiedClassNames) {
        this.showFullQualifiedClassNames = showFullQualifiedClassNames;
    }

    public Boolean getShowAssertions() {
        return showAssertions;
    }

    public void setShowAssertions(Boolean showAssertions) {
        this.showAssertions = showAssertions;
    }

    public Boolean getShowMethods() {
        return showMethods;
    }

    public void setShowMethods(Boolean showMethods) {
        this.showMethods = showMethods;
    }

    public Boolean getShowOnlyPublicMethods() {
        return showOnlyPublicMethods;
    }

    public void setShowOnlyPublicMethods(Boolean showOnlyPublicMethods) {
        this.showOnlyPublicMethods = showOnlyPublicMethods;
    }

    public Boolean getShowAggregates() {
        return showAggregates;
    }

    public void setShowAggregates(Boolean showAggregates) {
        this.showAggregates = showAggregates;
    }

    public Boolean getShowAggregateFields() {
        return showAggregateFields;
    }

    public void setShowAggregateFields(Boolean showAggregateFields) {
        this.showAggregateFields = showAggregateFields;
    }

    public Boolean getShowAggregateMethods() {
        return showAggregateMethods;
    }

    public void setShowAggregateMethods(Boolean showAggregateMethods) {
        this.showAggregateMethods = showAggregateMethods;
    }

    public Boolean getShowDomainEvents() {
        return showDomainEvents;
    }

    public void setShowDomainEvents(Boolean showDomainEvents) {
        this.showDomainEvents = showDomainEvents;
    }

    public Boolean getShowDomainEventFields() {
        return showDomainEventFields;
    }

    public void setShowDomainEventFields(Boolean showDomainEventFields) {
        this.showDomainEventFields = showDomainEventFields;
    }

    public Boolean getShowDomainEventMethods() {
        return showDomainEventMethods;
    }

    public void setShowDomainEventMethods(Boolean showDomainEventMethods) {
        this.showDomainEventMethods = showDomainEventMethods;
    }

    public Boolean getShowDomainCommands() {
        return showDomainCommands;
    }

    public void setShowDomainCommands(Boolean showDomainCommands) {
        this.showDomainCommands = showDomainCommands;
    }

    public Boolean getShowOnlyTopLevelDomainCommandRelations() {
        return showOnlyTopLevelDomainCommandRelations;
    }

    public void setShowOnlyTopLevelDomainCommandRelations(Boolean showOnlyTopLevelDomainCommandRelations) {
        this.showOnlyTopLevelDomainCommandRelations = showOnlyTopLevelDomainCommandRelations;
    }

    public Boolean getShowDomainCommandFields() {
        return showDomainCommandFields;
    }

    public void setShowDomainCommandFields(Boolean showDomainCommandFields) {
        this.showDomainCommandFields = showDomainCommandFields;
    }

    public Boolean getShowDomainCommandMethods() {
        return showDomainCommandMethods;
    }

    public void setShowDomainCommandMethods(Boolean showDomainCommandMethods) {
        this.showDomainCommandMethods = showDomainCommandMethods;
    }

    public Boolean getShowDomainServices() {
        return showDomainServices;
    }

    public void setShowDomainServices(Boolean showDomainServices) {
        this.showDomainServices = showDomainServices;
    }

    public Boolean getShowDomainServiceFields() {
        return showDomainServiceFields;
    }

    public void setShowDomainServiceFields(Boolean showDomainServiceFields) {
        this.showDomainServiceFields = showDomainServiceFields;
    }

    public Boolean getShowDomainServiceMethods() {
        return showDomainServiceMethods;
    }

    public void setShowDomainServiceMethods(Boolean showDomainServiceMethods) {
        this.showDomainServiceMethods = showDomainServiceMethods;
    }

    public Boolean getShowApplicationServices() {
        return showApplicationServices;
    }

    public void setShowApplicationServices(Boolean showApplicationServices) {
        this.showApplicationServices = showApplicationServices;
    }

    public Boolean getShowApplicationServiceFields() {
        return showApplicationServiceFields;
    }

    public void setShowApplicationServiceFields(Boolean showApplicationServiceFields) {
        this.showApplicationServiceFields = showApplicationServiceFields;
    }

    public Boolean getShowApplicationServiceMethods() {
        return showApplicationServiceMethods;
    }

    public void setShowApplicationServiceMethods(Boolean showApplicationServiceMethods) {
        this.showApplicationServiceMethods = showApplicationServiceMethods;
    }

    public Boolean getShowRepositories() {
        return showRepositories;
    }

    public void setShowRepositories(Boolean showRepositories) {
        this.showRepositories = showRepositories;
    }

    public Boolean getShowRepositoryFields() {
        return showRepositoryFields;
    }

    public void setShowRepositoryFields(Boolean showRepositoryFields) {
        this.showRepositoryFields = showRepositoryFields;
    }

    public Boolean getShowRepositoryMethods() {
        return showRepositoryMethods;
    }

    public void setShowRepositoryMethods(Boolean showRepositoryMethods) {
        this.showRepositoryMethods = showRepositoryMethods;
    }

    public Boolean getShowReadModels() {
        return showReadModels;
    }

    public void setShowReadModels(Boolean showReadModels) {
        this.showReadModels = showReadModels;
    }

    public Boolean getShowReadModelFields() {
        return showReadModelFields;
    }

    public void setShowReadModelFields(Boolean showReadModelFields) {
        this.showReadModelFields = showReadModelFields;
    }

    public Boolean getShowReadModelMethods() {
        return showReadModelMethods;
    }

    public void setShowReadModelMethods(Boolean showReadModelMethods) {
        this.showReadModelMethods = showReadModelMethods;
    }

    public Boolean getShowQueryHandlers() {
        return showQueryHandlers;
    }

    public void setShowQueryHandlers(Boolean showQueryHandlers) {
        this.showQueryHandlers = showQueryHandlers;
    }

    public Boolean getShowQueryHandlerFields() {
        return showQueryHandlerFields;
    }

    public void setShowQueryHandlerFields(Boolean showQueryHandlerFields) {
        this.showQueryHandlerFields = showQueryHandlerFields;
    }

    public Boolean getShowQueryHandlerMethods() {
        return showQueryHandlerMethods;
    }

    public void setShowQueryHandlerMethods(Boolean showQueryHandlerMethods) {
        this.showQueryHandlerMethods = showQueryHandlerMethods;
    }

    public Boolean getShowOutboundServices() {
        return showOutboundServices;
    }

    public void setShowOutboundServices(Boolean showOutboundServices) {
        this.showOutboundServices = showOutboundServices;
    }

    public Boolean getShowOutboundServiceFields() {
        return showOutboundServiceFields;
    }

    public void setShowOutboundServiceFields(Boolean showOutboundServiceFields) {
        this.showOutboundServiceFields = showOutboundServiceFields;
    }

    public Boolean getShowOutboundServiceMethods() {
        return showOutboundServiceMethods;
    }

    public void setShowOutboundServiceMethods(Boolean showOutboundServiceMethods) {
        this.showOutboundServiceMethods = showOutboundServiceMethods;
    }

    public Boolean getShowUnspecifiedServiceKinds() {
        return showUnspecifiedServiceKinds;
    }

    public void setShowUnspecifiedServiceKinds(Boolean showUnspecifiedServiceKinds) {
        this.showUnspecifiedServiceKinds = showUnspecifiedServiceKinds;
    }

    public Boolean getShowUnspecifiedServiceKindFields() {
        return showUnspecifiedServiceKindFields;
    }

    public void setShowUnspecifiedServiceKindFields(Boolean showUnspecifiedServiceKindFields) {
        this.showUnspecifiedServiceKindFields = showUnspecifiedServiceKindFields;
    }

    public Boolean getShowUnspecifiedServiceKindMethods() {
        return showUnspecifiedServiceKindMethods;
    }

    public void setShowUnspecifiedServiceKindMethods(Boolean showUnspecifiedServiceKindMethods) {
        this.showUnspecifiedServiceKindMethods = showUnspecifiedServiceKindMethods;
    }

    public Boolean getCallApplicationServiceDriver() {
        return callApplicationServiceDriver;
    }

    public void setCallApplicationServiceDriver(Boolean callApplicationServiceDriver) {
        this.callApplicationServiceDriver = callApplicationServiceDriver;
    }

    public List<String> getFieldBlacklist() {
        return fieldBlacklist;
    }

    public void setFieldBlacklist(List<String> fieldBlacklist) {
        this.fieldBlacklist = fieldBlacklist;
    }

    public List<String> getMethodBlacklist() {
        return methodBlacklist;
    }

    public void setMethodBlacklist(List<String> methodBlacklist) {
        this.methodBlacklist = methodBlacklist;
    }

    public Boolean getShowInheritedMembersInClasses() {
        return showInheritedMembersInClasses;
    }

    public void setShowInheritedMembersInClasses(Boolean showInheritedMembersInClasses) {
        this.showInheritedMembersInClasses = showInheritedMembersInClasses;
    }

    public Boolean getShowObjectMembersInClasses() {
        return showObjectMembersInClasses;
    }

    public void setShowObjectMembersInClasses(Boolean showObjectMembersInClasses) {
        this.showObjectMembersInClasses = showObjectMembersInClasses;
    }

    public Boolean getMultiplicityInLabel() {
        return multiplicityInLabel;
    }

    public void setMultiplicityInLabel(Boolean multiplicityInLabel) {
        this.multiplicityInLabel = multiplicityInLabel;
    }

    public Boolean getFieldStereotypes() {
        return fieldStereotypes;
    }

    public void setFieldStereotypes(Boolean fieldStereotypes) {
        this.fieldStereotypes = fieldStereotypes;
    }

    public List<String> getIncludeConnectedTo() {
        return includeConnectedTo;
    }

    public void setIncludeConnectedTo(List<String> includeConnectedTo) {
        this.includeConnectedTo = includeConnectedTo;
    }

    public List<String> getIncludeConnectedToIngoing() {
        return includeConnectedToIngoing;
    }

    public void setIncludeConnectedToIngoing(List<String> includeConnectedToIngoing) {
        this.includeConnectedToIngoing = includeConnectedToIngoing;
    }

    public List<String> getIncludeConnectedToOutgoing() {
        return includeConnectedToOutgoing;
    }

    public void setIncludeConnectedToOutgoing(List<String> includeConnectedToOutgoing) {
        this.includeConnectedToOutgoing = includeConnectedToOutgoing;
    }

    public List<String> getExcludeConnectedToIngoing() {
        return excludeConnectedToIngoing;
    }

    public void setExcludeConnectedToIngoing(List<String> excludeConnectedToIngoing) {
        this.excludeConnectedToIngoing = excludeConnectedToIngoing;
    }

    public List<String> getExcludeConnectedToOutgoing() {
        return excludeConnectedToOutgoing;
    }

    public void setExcludeConnectedToOutgoing(List<String> excludeConnectedToOutgoing) {
        this.excludeConnectedToOutgoing = excludeConnectedToOutgoing;
    }

    public Boolean getShowAbstractTypesInAggregates() {
        return showAbstractTypesInAggregates;
    }

    public Boolean getUseAbstractTypeNameForConcreteServiceKinds() {
        return useAbstractTypeNameForConcreteServiceKinds;
    }

    public List<String> getExplicitlyIncludedPackageNames() {
        return explicitlyIncludedPackageNames;
    }

    public void setExplicitlyIncludedPackageNames(List<String> explicitlyIncludedPackageNames) {
        this.explicitlyIncludedPackageNames = explicitlyIncludedPackageNames;
    }

    public Boolean isShowAllAbstractTypes() {
        return showAbstractAllTypes;
    }

    public void setShowAllAbstractTypes(Boolean showAbstractAllTypes) {
        this.showAbstractAllTypes = showAbstractAllTypes;
    }

    public void setShowAbstractTypesInAggregates(Boolean showAbstractTypesInAggregates) {
        this.showAbstractTypesInAggregates = showAbstractTypesInAggregates;
    }

    public Boolean isUseAbstractTypeNameForConcreteServiceKinds() {
        return useAbstractTypeNameForConcreteServiceKinds;
    }

    public void setUseAbstractTypeNameForConcreteServiceKinds(Boolean useAbstractTypeNameForConcreteServiceKinds) {
        this.useAbstractTypeNameForConcreteServiceKinds = useAbstractTypeNameForConcreteServiceKinds;
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
        if(useAbstractTypeNameForConcreteServiceKinds != null) visualBuilder.withUseAbstractTypeNameForConcreteServiceKinds(useAbstractTypeNameForConcreteServiceKinds);
        if(showAbstractAllTypes != null) visualBuilder.withShowAllAbstractTypes(showAbstractAllTypes);
        if(showAbstractTypesInAggregates != null) visualBuilder.withShowAbstractTypesInAggregates(showAbstractTypesInAggregates);

        configBuilder
            .withDiagramTrimSettings(trimBuilder.build())
            .withLayoutSettings(layoutBuilder.build())
            .withStyleSettings(styleBuilder.build())
            .withGeneralVisualSettings(visualBuilder.build());
        return configBuilder.build();
    }
}
