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

import io.domainlifecycles.plugins.diagram.DiagramConfig;
import io.domainlifecycles.plugins.diagram.FileType;
import io.domainlifecycles.plugins.exception.DLCPluginsException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DiagramConfigMapperTest {

    @Test
    void mapsMandatoryAndOptionalPropertiesToDiagramConfig() {
        PluginDiagramConfiguration mavenDiagramConfig = mock(PluginDiagramConfiguration.class);
        when(mavenDiagramConfig.getFormat()).thenReturn("svg");
        when(mavenDiagramConfig.getFileName()).thenReturn("aggregates");
        when(mavenDiagramConfig.getExplicitlyIncludedPackages()).thenReturn(List.of("com.example.domain"));
        when(mavenDiagramConfig.getAggregateRootStyle()).thenReturn("fill:#ff0000");
        when(mavenDiagramConfig.getShowFields()).thenReturn(true);
        when(mavenDiagramConfig.getMethodBlacklist()).thenReturn(List.of("internalHelper"));
        when(mavenDiagramConfig.getIncludeFlowsFrom()).thenReturn(List.of("com.example.order.PlaceOrder"));
        when(mavenDiagramConfig.getFlowMaxDepth()).thenReturn(2);
        when(mavenDiagramConfig.getFlowFollowEvents()).thenReturn(false);
        when(mavenDiagramConfig.getFlowFollowImplementations()).thenReturn(false);
        when(mavenDiagramConfig.getFlowExcludeAccessors()).thenReturn(true);

        DiagramConfig diagramConfig = DiagramConfigMapper.map(mavenDiagramConfig);

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
        PluginDiagramConfiguration mavenDiagramConfig = mock(PluginDiagramConfiguration.class);
        when(mavenDiagramConfig.getFormat()).thenReturn("png");
        when(mavenDiagramConfig.getFileName()).thenReturn("diagram");
        // Mockito's default answer for an unstubbed Boolean/Integer-returning method is `false`/`0`, not `null`;
        // stub these explicitly to test the actual "unset" mapping behaviour of DiagramConfigMapper.
        when(mavenDiagramConfig.getShowFields()).thenReturn(null);
        when(mavenDiagramConfig.getFlowMaxDepth()).thenReturn(null);
        when(mavenDiagramConfig.getFlowFollowEvents()).thenReturn(null);
        when(mavenDiagramConfig.getFlowFollowImplementations()).thenReturn(null);
        when(mavenDiagramConfig.getFlowExcludeAccessors()).thenReturn(null);

        DiagramConfig diagramConfig = DiagramConfigMapper.map(mavenDiagramConfig);

        assertThat(diagramConfig.getFileType()).isEqualTo(FileType.PNG);
        assertThat(diagramConfig.getAggregateRootStyle()).isNull();
        assertThat(diagramConfig.getShowFields()).isNull();
        // Mockito's default answer for an unstubbed List-returning method is an empty list, not null;
        // it is passed through by DiagramConfigMapper/DiagramConfig unchanged.
        assertThat(diagramConfig.getExplicitlyIncludedPackageNames()).isEmpty();
        assertThat(diagramConfig.getMethodBlacklist()).isEmpty();
        assertThat(diagramConfig.getIncludeFlowsFrom()).isEmpty();
        assertThat(diagramConfig.getFlowMaxDepth()).isNull();
        assertThat(diagramConfig.getFlowFollowEvents()).isNull();
        assertThat(diagramConfig.getFlowFollowImplementations()).isNull();
        assertThat(diagramConfig.getFlowExcludeAccessors()).isNull();
    }

    @Test
    void mapThrowsForUnknownFormat() {
        PluginDiagramConfiguration mavenDiagramConfig = mock(PluginDiagramConfiguration.class);
        when(mavenDiagramConfig.getFormat()).thenReturn("bogus-format");

        assertThatThrownBy(() -> DiagramConfigMapper.map(mavenDiagramConfig))
            .isInstanceOf(DLCPluginsException.class);
    }
}
