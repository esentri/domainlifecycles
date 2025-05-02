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

import io.domainlifecycles.diagram.domain.config.DomainDiagramConfig;
import io.domainlifecycles.diagram.domain.config.DomainDiagramConfig.DomainDiagramConfigBuilder;
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
    private List<String> domainModelPackageNames;

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
    private List<String> transitiveFilterSeedDomainServiceTypeNames;
    private List<String> filteredPackageNames;
    private Boolean showAbstractTypes;
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

    public List<String> getDomainModelPackageNames() {
        return domainModelPackageNames;
    }

    public void setDomainModelPackageNames(List<String> domainModelPackageNames) {
        this.domainModelPackageNames = domainModelPackageNames;
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

    public List<String> getTransitiveFilterSeedDomainServiceTypeNames() {
        return transitiveFilterSeedDomainServiceTypeNames;
    }

    public void setTransitiveFilterSeedDomainServiceTypeNames(List<String> transitiveFilterSeedDomainServiceTypeNames) {
        this.transitiveFilterSeedDomainServiceTypeNames = transitiveFilterSeedDomainServiceTypeNames;
    }

    public List<String> getFilteredPackageNames() {
        return filteredPackageNames;
    }

    public void setFilteredPackageNames(List<String> filteredPackageNames) {
        this.filteredPackageNames = filteredPackageNames;
    }

    public Boolean isShowAbstractTypes() {
        return showAbstractTypes;
    }

    public void setShowAbstractTypes(Boolean showAbstractTypes) {
        this.showAbstractTypes = showAbstractTypes;
    }

    public Boolean isUseAbstractTypeNameForConcreteServiceKinds() {
        return useAbstractTypeNameForConcreteServiceKinds;
    }

    public void setUseAbstractTypeNameForConcreteServiceKinds(Boolean useAbstractTypeNameForConcreteServiceKinds) {
        this.useAbstractTypeNameForConcreteServiceKinds = useAbstractTypeNameForConcreteServiceKinds;
    }



    public DomainDiagramConfig map() {
        DomainDiagramConfigBuilder builder = DomainDiagramConfig.builder();

        if(aggregateRootStyle != null) builder.withAggregateRootStyle(aggregateRootStyle);
        if(aggregateFrameStyle != null) builder.withAggregateFrameStyle(aggregateFrameStyle);
        if(entityStyle != null) builder.withEntityStyle(entityStyle);
        if(valueObjectStyle != null) builder.withValueObjectStyle(valueObjectStyle);
        if(enumStyle != null) builder.withEnumStyle(enumStyle);
        if(identityStyle != null) builder.withIdentityStyle(identityStyle);
        if(domainEventStyle != null) builder.withDomainEventStyle(domainEventStyle);
        if(domainCommandStyle != null) builder.withDomainCommandStyle(domainCommandStyle);
        if(applicationServiceStyle != null) builder.withApplicationServiceStyle(applicationServiceStyle);
        if(domainServiceStyle != null) builder.withDomainServiceStyle(domainServiceStyle);
        if(repositoryStyle != null) builder.withRepositoryStyle(repositoryStyle);
        if(readModelStyle != null) builder.withReadModelStyle(readModelStyle);
        if(queryHandlerStyle != null) builder.withQueryHandlerStyle(queryHandlerStyle);
        if(outboundServiceStyle != null) builder.withOutboundServiceStyle(outboundServiceStyle);
        if(font != null) builder.withFont(font);
        if(direction != null) builder.withDirection(direction);
        if(ranker != null) builder.withRanker(ranker);
        if(acycler != null) builder.withAcycler(acycler);
        if(backgroundColor != null) builder.withBackgroundColor(backgroundColor);
        if(classesBlacklist != null && !classesBlacklist.isEmpty()) builder.withClassesBlacklist(classesBlacklist);
        if(showFields != null) builder.withShowFields(showFields);
        if(showFullQualifiedClassNames != null) builder.withShowFullQualifiedClassNames(showFullQualifiedClassNames);
        if(showAssertions != null) builder.withShowAssertions(showAssertions);
        if(showMethods != null) builder.withShowMethods(showMethods);
        if(showOnlyPublicMethods != null) builder.withShowOnlyPublicMethods(showOnlyPublicMethods);
        if(showDomainEvents != null) builder.withShowDomainEvents(showDomainEvents);
        if(showDomainEventFields != null) builder.withShowDomainEventFields(showDomainEventFields);
        if(showDomainEventMethods != null) builder.withShowDomainEventMethods(showDomainEventMethods);
        if(showDomainCommands != null) builder.withShowDomainCommands(showDomainCommands);
        if(showOnlyTopLevelDomainCommandRelations != null) builder.withShowOnlyTopLevelDomainCommandRelations(showOnlyTopLevelDomainCommandRelations);
        if(showDomainCommandFields != null) builder.withShowDomainCommandFields(showDomainCommandFields);
        if(showDomainCommandMethods != null) builder.withShowDomainCommandMethods(showDomainCommandMethods);
        if(showDomainServices != null) builder.withShowDomainServices(showDomainServices);
        if(showDomainServiceFields != null) builder.withShowDomainServiceFields(showDomainServiceFields);
        if(showDomainServiceMethods != null) builder.withShowDomainServiceMethods(showDomainServiceMethods);
        if(showApplicationServices != null) builder.withShowApplicationServices(showApplicationServices);
        if(showApplicationServiceFields != null) builder.withShowApplicationServiceFields(showApplicationServiceFields);
        if(showApplicationServiceMethods != null) builder.withShowApplicationServiceMethods(showApplicationServiceMethods);
        if(showRepositories != null) builder.withShowRepositories(showRepositories);
        if(showRepositoryFields != null) builder.withShowRepositoryFields(showRepositoryFields);
        if(showRepositoryMethods != null) builder.withShowRepositoryMethods(showRepositoryMethods);
        if(showReadModels != null) builder.withShowReadModels(showReadModels);
        if(showReadModelFields != null) builder.withShowReadModelFields(showReadModelFields);
        if(showReadModelMethods != null) builder.withShowReadModelMethods(showReadModelMethods);
        if(showQueryHandlers != null) builder.withShowQueryHandlers(showQueryHandlers);
        if(showQueryHandlerFields != null) builder.withShowQueryHandlerFields(showQueryHandlerFields);
        if(showQueryHandlerMethods != null) builder.withShowQueryHandlerMethods(showQueryHandlerMethods);
        if(showOutboundServices != null) builder.withShowOutboundServices(showOutboundServices);
        if(showOutboundServiceFields != null) builder.withShowOutboundServiceFields(showOutboundServiceFields);
        if(showOutboundServiceMethods != null) builder.withShowOutboundServiceMethods(showOutboundServiceMethods);
        if(showUnspecifiedServiceKinds != null) builder.withShowUnspecifiedServiceKinds(showUnspecifiedServiceKinds);
        if(showUnspecifiedServiceKindFields != null) builder.withShowUnspecifiedServiceKindFields(showUnspecifiedServiceKindFields);
        if(showUnspecifiedServiceKindMethods != null) builder.withShowUnspecifiedServiceKindMethods(showUnspecifiedServiceKindMethods);
        if(callApplicationServiceDriver != null) builder.withCallApplicationServiceDriver(callApplicationServiceDriver);
        if(fieldBlacklist != null && !fieldBlacklist.isEmpty()) builder.withFieldBlacklist(fieldBlacklist);
        if(methodBlacklist != null && !methodBlacklist.isEmpty()) builder.withMethodBlacklist(classesBlacklist);
        if(showInheritedMembersInClasses != null) builder.withShowInheritedMembersInClasses(showInheritedMembersInClasses);
        if(showObjectMembersInClasses != null) builder.withShowObjectMembersInClasses(showObjectMembersInClasses);
        if(multiplicityInLabel != null) builder.withMultiplicityInLabel(multiplicityInLabel);
        if(fieldStereotypes != null) builder.withFieldStereotypes(fieldStereotypes);
        if(transitiveFilterSeedDomainServiceTypeNames != null && !transitiveFilterSeedDomainServiceTypeNames.isEmpty()) builder.withTransitiveFilterSeedDomainServiceTypeNames(transitiveFilterSeedDomainServiceTypeNames);
        if(filteredPackageNames != null && !filteredPackageNames.isEmpty()) builder.withFilteredPackageNames(filteredPackageNames);
        if(useAbstractTypeNameForConcreteServiceKinds != null) builder.withUseAbstractTypeNameForConcreteServiceKinds(useAbstractTypeNameForConcreteServiceKinds);
        if(showAbstractTypes != null) builder.withShowAbstractTypes(showAbstractTypes);

        return builder.build();
    }
}
