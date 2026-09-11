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
import io.domainlifecycles.staticanalysis.FlowConfig;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
}
