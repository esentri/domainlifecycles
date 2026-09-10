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

package io.domainlifecycles.plugin.diagram;

import io.domainlifecycles.plugin.extensions.PluginDiagramConfigurationExtension;
import io.domainlifecycles.plugins.diagram.DiagramConfig;
import io.domainlifecycles.plugins.diagram.FileType;
import io.domainlifecycles.plugins.exception.DLCPluginsException;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DiagramConfigMapperTest {

    private NamedDomainObjectContainer<PluginDiagramConfigurationExtension> diagrams;

    @BeforeEach
    void setUp() {
        Project project = ProjectBuilder.builder().build();
        diagrams = project.getObjects().domainObjectContainer(PluginDiagramConfigurationExtension.class);
    }

    @Test
    void mapsMandatoryAndOptionalPropertiesToDiagramConfig() {
        PluginDiagramConfigurationExtension extension = diagrams.create("aggregates");
        extension.getFormat().set("svg");
        extension.getFileName().set("aggregates");
        extension.getExplicitlyIncludedPackages().set(List.of("com.example.domain"));
        extension.getAggregateRootStyle().set("fill:#ff0000");
        extension.getShowFields().set(true);
        extension.getMethodBlacklist().set(List.of("internalHelper"));
        extension.getIncludeFlowsFrom().set(List.of("com.example.order.PlaceOrder"));
        extension.getFlowMaxDepth().set(2);
        extension.getFlowFollowEvents().set(false);
        extension.getFlowFollowImplementations().set(false);
        extension.getFlowExcludeAccessors().set(true);

        DiagramConfig diagramConfig = DiagramConfigMapper.map(extension);

        assertThat(diagramConfig.getFileType()).isEqualTo(FileType.SVG);
        assertThat(diagramConfig.getFileName()).isEqualTo("aggregates");
        assertThat(diagramConfig.getExplicitlyIncludedPackageNames()).containsExactly("com.example.domain");
        assertThat(diagramConfig.getAggregateRootStyle()).isEqualTo("fill:#ff0000");
        assertThat(diagramConfig.getShowFields()).isTrue();
        assertThat(diagramConfig.getMethodBlacklist()).containsExactly("internalHelper");
        assertThat(diagramConfig.getIncludeFlowsFrom()).containsExactly("com.example.order.PlaceOrder");
        assertThat(diagramConfig.getFlowMaxDepth()).isEqualTo(2);
        assertThat(diagramConfig.getFlowFollowEvents()).isFalse();
        assertThat(diagramConfig.getFlowFollowImplementations()).isFalse();
        assertThat(diagramConfig.getFlowExcludeAccessors()).isTrue();
    }

    @Test
    void unsetOptionalPropertiesAreMappedAsNull() {
        PluginDiagramConfigurationExtension extension = diagrams.create("minimal");
        extension.getFormat().set("png");

        DiagramConfig diagramConfig = DiagramConfigMapper.map(extension);

        assertThat(diagramConfig.getFileType()).isEqualTo(FileType.PNG);
        assertThat(diagramConfig.getFileName()).isEqualTo("diagram");
        assertThat(diagramConfig.getAggregateRootStyle()).isNull();
        assertThat(diagramConfig.getShowFields()).isNull();
        // ListProperty falls back to Gradle's built-in empty-list convention rather than null when unset
        assertThat(diagramConfig.getExplicitlyIncludedPackageNames()).isEmpty();
        assertThat(diagramConfig.getIncludeFlowsFrom()).isEmpty();
        assertThat(diagramConfig.getFlowMaxDepth()).isNull();
        assertThat(diagramConfig.getFlowFollowEvents()).isNull();
        assertThat(diagramConfig.getFlowFollowImplementations()).isNull();
        assertThat(diagramConfig.getFlowExcludeAccessors()).isNull();
    }

    @Test
    void mapThrowsForUnsetOrUnknownFormat() {
        PluginDiagramConfigurationExtension extension = diagrams.create("broken");
        extension.getFormat().set("bogus-format");

        assertThatThrownBy(() -> DiagramConfigMapper.map(extension))
            .isInstanceOf(DLCPluginsException.class);
    }
}
