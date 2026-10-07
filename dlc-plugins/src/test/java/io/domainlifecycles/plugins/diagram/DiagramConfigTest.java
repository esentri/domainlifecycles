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
 *  Copyright 2019-2026 the original author or authors.
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
import io.domainlifecycles.plugins.exception.DLCPluginsException;
import io.domainlifecycles.staticanalysis.FlowConfig;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DiagramConfigTest {

    @Test
    void mapWithAllFieldsUnsetProducesDefaultDomainDiagramConfig() {
        DiagramConfig diagramConfig = new DiagramConfig();

        DomainDiagramConfig mapped = diagramConfig.map();

        assertThat(mapped).isNotNull();
        assertThat(mapped.getGeneralVisualSettings()).isNotNull();
        assertThat(mapped.getStyleSettings()).isNotNull();
        assertThat(mapped.getLayoutSettings()).isNotNull();
        assertThat(mapped.getDiagramTrimSettings()).isNotNull();
    }

    @Test
    void mapCopiesStyleAndLayoutSettings() {
        DiagramConfig diagramConfig = new DiagramConfig();
        diagramConfig.setAggregateRootStyle("aggregateRootStyle");
        diagramConfig.setEntityStyle("entityStyle");
        diagramConfig.setFont("Arial");
        diagramConfig.setBackgroundColor("#FFFFFF");
        diagramConfig.setDirection("TB");
        diagramConfig.setRanker("network-simplex");
        diagramConfig.setAcycler("greedy");

        DomainDiagramConfig mapped = diagramConfig.map();

        assertThat(mapped.getStyleSettings().getAggregateRootStyle()).isEqualTo("aggregateRootStyle");
        assertThat(mapped.getStyleSettings().getEntityStyle()).isEqualTo("entityStyle");
        assertThat(mapped.getStyleSettings().getFont()).isEqualTo("Arial");
        assertThat(mapped.getStyleSettings().getBackgroundColor()).isEqualTo("#FFFFFF");
        assertThat(mapped.getLayoutSettings().getDirection()).isEqualTo("TB");
        assertThat(mapped.getLayoutSettings().getRanker()).isEqualTo("network-simplex");
        assertThat(mapped.getLayoutSettings().getAcycler()).isEqualTo("greedy");
    }

    @Test
    void mapCopiesTheMaximalNumberOfFieldsOfInlinedValueObjects() {
        DiagramConfig diagramConfig = new DiagramConfig();
        diagramConfig.setMaxInlinedValueObjectFields(4);

        assertThat(diagramConfig.map().getGeneralVisualSettings().getMaxInlinedValueObjectFields()).isEqualTo(4);
        assertThat(new DiagramConfig().map().getGeneralVisualSettings().getMaxInlinedValueObjectFields())
            .as("unset, the diagrammer's default applies")
            .isEqualTo(2);
    }

    @Test
    void mapCopiesWhetherOnlyTheMethodsCalledInFlowsAreShown() {
        DiagramConfig diagramConfig = new DiagramConfig();
        diagramConfig.setShowOnlyFlowMethods(false);

        assertThat(diagramConfig.map().getGeneralVisualSettings().isShowOnlyFlowMethods()).isFalse();
        assertThat(new DiagramConfig().map().getGeneralVisualSettings().isShowOnlyFlowMethods())
            .as("unset, the diagrammer's default applies")
            .isTrue();
    }

    @Test
    void mapCopiesTheFactorySettings() {
        DiagramConfig diagramConfig = new DiagramConfig();
        diagramConfig.setFactoryStyle("fill=#123456 bold");
        diagramConfig.setShowFactories(false);
        diagramConfig.setShowFactoryFields(true);
        diagramConfig.setShowFactoryMethods(false);
        diagramConfig.setShowFactoryRelations(false);

        var mapped = diagramConfig.map();

        assertThat(mapped.getStyleSettings().getFactoryStyle()).isEqualTo("fill=#123456 bold");
        assertThat(mapped.getGeneralVisualSettings().isShowFactories()).isFalse();
        assertThat(mapped.getGeneralVisualSettings().isShowFactoryFields()).isTrue();
        assertThat(mapped.getGeneralVisualSettings().isShowFactoryMethods()).isFalse();
        assertThat(mapped.getGeneralVisualSettings().isShowFactoryRelations()).isFalse();
    }

    @Test
    void mapKeepsTheDiagrammersFactoryDefaults_When_TheFactorySettingsAreUnset() {
        var mapped = new DiagramConfig().map();

        assertThat(mapped.getGeneralVisualSettings().isShowFactories()).isTrue();
        assertThat(mapped.getGeneralVisualSettings().isShowFactoryFields()).isFalse();
        assertThat(mapped.getGeneralVisualSettings().isShowFactoryMethods()).isTrue();
        assertThat(mapped.getGeneralVisualSettings().isShowFactoryRelations()).isTrue();
        assertThat(mapped.getStyleSettings().getFactoryStyle()).isNotBlank();
    }

    @Test
    void mapCopiesTheDepthsOfTheConnectionsFollowed() {
        DiagramConfig diagramConfig = new DiagramConfig();
        diagramConfig.setIncludeConnectedToIngoingDepth(2);
        diagramConfig.setIncludeConnectedToOutgoingDepth(-1);

        var trimSettings = diagramConfig.map().getDiagramTrimSettings();

        assertThat(trimSettings.getIncludeConnectedToIngoingDepth()).isEqualTo(2);
        assertThat(trimSettings.getIncludeConnectedToOutgoingDepth()).isEqualTo(-1);
    }

    @Test
    void mapFollowsTheCompletePathOfTheConnections_When_TheDepthsAreUnset() {
        var trimSettings = new DiagramConfig().map().getDiagramTrimSettings();

        assertThat(trimSettings.getIncludeConnectedToIngoingDepth()).isZero();
        assertThat(trimSettings.getIncludeConnectedToOutgoingDepth()).isZero();
    }

    @Test
    void mapCopiesWhetherOnlyTheFramesOfTheAggregatesAreShown() {
        DiagramConfig diagramConfig = new DiagramConfig();
        diagramConfig.setShowOnlyAggregateFrames(true);

        assertThat(diagramConfig.map().getGeneralVisualSettings().isShowOnlyAggregateFrames()).isTrue();
        assertThat(new DiagramConfig().map().getGeneralVisualSettings().isShowOnlyAggregateFrames())
            .as("unset, the diagrammer's default applies")
            .isFalse();
    }

    @Test
    void mapCopiesWhetherTheCallsOfTheFlowsAreDrawn() {
        DiagramConfig diagramConfig = new DiagramConfig();
        diagramConfig.setShowFlowCallRelations(true);

        assertThat(diagramConfig.map().getGeneralVisualSettings().isShowFlowCallRelations()).isTrue();
        assertThat(new DiagramConfig().map().getGeneralVisualSettings().isShowFlowCallRelations())
            .as("unset, the diagrammer's default applies")
            .isFalse();
    }

    @Test
    void mapCopiesFieldAndMethodBlacklistsIntoTheirOwnSettingsIndependently() {
        DiagramConfig diagramConfig = new DiagramConfig();
        diagramConfig.setFieldBlacklist(List.of("password", "secret"));
        diagramConfig.setMethodBlacklist(List.of("internalHelper"));

        DomainDiagramConfig mapped = diagramConfig.map();

        assertThat(mapped.getGeneralVisualSettings().getFieldBlacklist()).containsExactly("password", "secret");
        assertThat(mapped.getGeneralVisualSettings().getMethodBlacklist()).containsExactly("internalHelper");
    }

    @Test
    void mapCopiesClassesBlacklistIntoTrimSettingsOnly() {
        DiagramConfig diagramConfig = new DiagramConfig();
        diagramConfig.setClassesBlacklist(List.of("com.example.Legacy"));

        DomainDiagramConfig mapped = diagramConfig.map();
        DomainDiagramConfig defaultMapped = new DiagramConfig().map();

        assertThat(mapped.getDiagramTrimSettings().getClassesBlacklist()).containsExactly("com.example.Legacy");
        assertThat(mapped.getGeneralVisualSettings().getMethodBlacklist())
            .isEqualTo(defaultMapped.getGeneralVisualSettings().getMethodBlacklist());
    }

    @Test
    void mapIgnoresEmptyListsAndKeepsDefaults() {
        DiagramConfig diagramConfig = new DiagramConfig();
        diagramConfig.setFieldBlacklist(List.of());
        diagramConfig.setMethodBlacklist(List.of());
        diagramConfig.setClassesBlacklist(List.of());

        DomainDiagramConfig defaultMapped = new DiagramConfig().map();
        DomainDiagramConfig mapped = diagramConfig.map();

        assertThat(mapped.getGeneralVisualSettings().getFieldBlacklist())
            .isEqualTo(defaultMapped.getGeneralVisualSettings().getFieldBlacklist());
        assertThat(mapped.getGeneralVisualSettings().getMethodBlacklist())
            .isEqualTo(defaultMapped.getGeneralVisualSettings().getMethodBlacklist());
        assertThat(mapped.getDiagramTrimSettings().getClassesBlacklist())
            .isEqualTo(defaultMapped.getDiagramTrimSettings().getClassesBlacklist());
    }

    @Test
    void mapCopiesVisualBooleanFlags() {
        DiagramConfig diagramConfig = new DiagramConfig();
        diagramConfig.setShowFields(false);
        diagramConfig.setShowMethods(true);
        diagramConfig.setShowOnlyPublicMethods(true);
        diagramConfig.setShowRelationshipLabels(false);
        diagramConfig.setShowRelationshipStereotypes(true);

        DomainDiagramConfig mapped = diagramConfig.map();

        assertThat(mapped.getGeneralVisualSettings().isShowFields()).isFalse();
        assertThat(mapped.getGeneralVisualSettings().isShowMethods()).isTrue();
        assertThat(mapped.getGeneralVisualSettings().isShowOnlyPublicMethods()).isTrue();
        assertThat(mapped.getGeneralVisualSettings().isShowRelationshipLabels()).isFalse();
        assertThat(mapped.getGeneralVisualSettings().isShowRelationshipStereotypes()).isTrue();
    }

    @Test
    void mapCopiesConnectionFiltersAndExplicitlyIncludedPackages() {
        DiagramConfig diagramConfig = new DiagramConfig();
        diagramConfig.setIncludeConnectedTo(List.of("com.example.A"));
        diagramConfig.setIncludeConnectedToIngoing(List.of("com.example.B"));
        diagramConfig.setIncludeConnectedToOutgoing(List.of("com.example.C"));
        diagramConfig.setExcludeConnectedToIngoing(List.of("com.example.D"));
        diagramConfig.setExcludeConnectedToOutgoing(List.of("com.example.E"));
        diagramConfig.setExplicitlyIncludedPackageNames(List.of("com.example.pkg"));

        DomainDiagramConfig mapped = diagramConfig.map();

        assertThat(mapped.getDiagramTrimSettings().getIncludeConnectedTo()).containsExactly("com.example.A");
        assertThat(mapped.getDiagramTrimSettings().getIncludeConnectedToIngoing()).containsExactly("com.example.B");
        assertThat(mapped.getDiagramTrimSettings().getIncludeConnectedToOutgoing()).containsExactly("com.example.C");
        assertThat(mapped.getDiagramTrimSettings().getExcludeConnectedToIngoing()).containsExactly("com.example.D");
        assertThat(mapped.getDiagramTrimSettings().getExcludeConnectedToOutgoing()).containsExactly("com.example.E");
        assertThat(mapped.getDiagramTrimSettings().getExplicitlyIncludedPackageNames()).containsExactly("com.example.pkg");
    }

    @Test
    void mapAppliesFlowMaxDepthEvenWithoutIncludeFlowsFromSet() {
        DiagramConfig diagramConfig = new DiagramConfig();
        // flow traversal knobs alone (without includeFlowsFrom) still take effect on the mapped FlowConfig,
        // since FlowConfig is only *used* by the diagrammer together with includeFlowsFrom, not gated by it here.
        diagramConfig.setFlowMaxDepth(3);

        DomainDiagramConfig mapped = diagramConfig.map();

        assertThat(mapped.getDiagramTrimSettings().getIncludeFlowsFrom()).isEmpty();
        assertThat(mapped.getFlowConfig().maxDepth()).isEqualTo(3);
    }

    @Test
    void mapCopiesIncludeFlowsFromIntoTrimSettings() {
        DiagramConfig diagramConfig = new DiagramConfig();
        diagramConfig.setIncludeFlowsFrom(List.of("com.example.order.PlaceOrder"));

        DomainDiagramConfig mapped = diagramConfig.map();

        assertThat(mapped.getDiagramTrimSettings().getIncludeFlowsFrom()).containsExactly("com.example.order.PlaceOrder");
    }

    @Test
    void mapCopiesIncludeFlowsToIntoTrimSettings() {
        DiagramConfig diagramConfig = new DiagramConfig();
        diagramConfig.setIncludeFlowsTo(List.of("com.example.order.OrderPlaced"));

        DomainDiagramConfig mapped = diagramConfig.map();

        assertThat(mapped.getDiagramTrimSettings().getIncludeFlowsTo()).containsExactly("com.example.order.OrderPlaced");
    }

    @Test
    void mapWithoutAnyFlowSettingKeepsDefaultFlowConfig() {
        FlowConfig defaults = FlowConfig.defaults();

        DomainDiagramConfig mapped = new DiagramConfig().map();

        assertThat(mapped.getFlowConfig().maxDepth()).isEqualTo(defaults.maxDepth());
        assertThat(mapped.getFlowConfig().followEvents()).isEqualTo(defaults.followEvents());
        assertThat(mapped.getFlowConfig().followImplementations()).isEqualTo(defaults.followImplementations());
    }

    @Test
    void mapCopiesFlowTraversalSettingsIntoFlowConfig() {
        DiagramConfig diagramConfig = new DiagramConfig();
        diagramConfig.setIncludeFlowsFrom(List.of("com.example.order.PlaceOrder"));
        diagramConfig.setFlowMaxDepth(2);
        diagramConfig.setFlowFollowEvents(false);
        diagramConfig.setFlowFollowImplementations(false);

        DomainDiagramConfig mapped = diagramConfig.map();

        FlowConfig flowConfig = mapped.getFlowConfig();
        assertThat(flowConfig.maxDepth()).isEqualTo(2);
        assertThat(flowConfig.followEvents()).isFalse();
        assertThat(flowConfig.followImplementations()).isFalse();
    }

    @Test
    void mapExcludingAccessorsAppliesTheAccessorFilterOnTopOfDefaults() {
        DiagramConfig withoutFilter = new DiagramConfig();
        withoutFilter.setIncludeFlowsFrom(List.of("com.example.order.PlaceOrder"));
        DiagramConfig withFilter = new DiagramConfig();
        withFilter.setIncludeFlowsFrom(List.of("com.example.order.PlaceOrder"));
        withFilter.setFlowExcludeAccessors(true);

        FlowConfig defaultFlowConfig = withoutFilter.map().getFlowConfig();
        FlowConfig excludingAccessorsFlowConfig = withFilter.map().getFlowConfig();

        // the default (no-op) methodFilter accepts everything, excludingAccessors() must reject at least accessors
        assertThat(defaultFlowConfig.methodFilter()).isNotSameAs(excludingAccessorsFlowConfig.methodFilter());
    }

    @Test
    void fileTypeAndFileNameAreSimplePlainGettersAndSetters() {
        DiagramConfig diagramConfig = new DiagramConfig();

        diagramConfig.setFileType(FileType.SVG);
        diagramConfig.setFileName("myDiagram");

        assertThat(diagramConfig.getFileType()).isEqualTo(FileType.SVG);
        assertThat(diagramConfig.getFileName()).isEqualTo("myDiagram");
    }

    @Test
    void nonDomainClassFilterExcludesJooqGeneratedClassesByDefault() {
        DiagramConfig diagramConfig = new DiagramConfig();

        var filter = diagramConfig.nonDomainClassFilter();

        assertThat(filter.excludedSupertypePackages()).containsExactly("org.jooq");
        assertThat(filter.excludedPackages()).isEmpty();
    }

    @Test
    void nonDomainClassFilterUsesConfiguredExclusions() {
        DiagramConfig diagramConfig = new DiagramConfig();
        diagramConfig.setNonDomainExcludedSupertypePackages(List.of("com.example.codegen"));
        diagramConfig.setNonDomainExcludedPackages(List.of("com.example.generated"));

        var filter = diagramConfig.nonDomainClassFilter();

        assertThat(filter.excludedSupertypePackages()).containsExactly("com.example.codegen");
        assertThat(filter.excludedPackages()).containsExactly("com.example.generated");
    }

    @Test
    void nonDomainClassFilterTreatsEmptySupertypePackagesAsDefault() {
        // Maven injects an empty list for an unconfigured list parameter (verified with -X), Gradle
        // list properties default to an empty list - both must keep the default exclusion of jOOQ code
        DiagramConfig diagramConfig = new DiagramConfig();
        diagramConfig.setNonDomainExcludedSupertypePackages(List.of());
        diagramConfig.setNonDomainExcludedPackages(List.of());

        var filter = diagramConfig.nonDomainClassFilter();

        assertThat(filter.excludedSupertypePackages()).containsExactly("org.jooq");
        assertThat(filter.excludedPackages()).isEmpty();
    }

    @Test
    void callRelationsSwitchedOnWithoutAFlowAreRejected() {
        DiagramConfig diagramConfig = new DiagramConfig();
        diagramConfig.setShowFlowCallRelations(true);

        assertThatThrownBy(() -> DiagramGeneratorImpl.requireFlowForCallRelations(diagramConfig, false))
            .isInstanceOf(DLCPluginsException.class)
            .hasMessageContaining("showFlowCallRelations")
            .hasMessageContaining("includeFlowsFrom or includeFlowsTo");
    }

    @Test
    void callRelationsWithAFlowOrSwitchedOffAreAccepted() {
        DiagramConfig switchedOn = new DiagramConfig();
        switchedOn.setShowFlowCallRelations(true);

        assertThatCode(() -> DiagramGeneratorImpl.requireFlowForCallRelations(switchedOn, true))
            .doesNotThrowAnyException();
        assertThatCode(() -> DiagramGeneratorImpl.requireFlowForCallRelations(new DiagramConfig(), false))
            .doesNotThrowAnyException();
    }
}
