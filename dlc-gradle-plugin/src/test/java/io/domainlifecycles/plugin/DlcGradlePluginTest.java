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

package io.domainlifecycles.plugin;

import io.domainlifecycles.plugin.diagram.CreateDiagramTask;
import io.domainlifecycles.plugin.extensions.DlcGradlePluginExtension;
import io.domainlifecycles.plugin.mirror.MirrorSerializerTask;
import io.domainlifecycles.plugin.viewer.UploadDomainModelTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies that applying the plugin wires up the {@code dlcGradlePlugin} extension (with its
 * nested {@code diagram}, {@code serializeMirror} and {@code domainModelUpload} extensions) and
 * registers the corresponding tasks once the project is evaluated.
 */
public class DlcGradlePluginTest {

    @Test
    void applyRegistersMainExtension() {
        Project project = ProjectBuilder.builder().build();

        project.getPlugins().apply(DlcGradlePlugin.class);

        assertThat(project.getExtensions().findByName("dlcGradlePlugin"))
            .isInstanceOf(DlcGradlePluginExtension.class);
    }

    @Test
    void afterEvaluateRegistersTasksWithDefaultConfiguration() {
        Project project = ProjectBuilder.builder().build();

        project.getPlugins().apply(DlcGradlePlugin.class);
        ((ProjectInternal) project).evaluate();

        Task createDiagram = project.getTasks().findByName("createDiagram");
        Task serializeMirror = project.getTasks().findByName("serializeMirror");
        Task domainModelUpload = project.getTasks().findByName("domainModelUpload");

        assertThat(createDiagram).isInstanceOf(CreateDiagramTask.class);
        assertThat(serializeMirror).isInstanceOf(MirrorSerializerTask.class);
        assertThat(domainModelUpload).isInstanceOf(UploadDomainModelTask.class);
    }
}
