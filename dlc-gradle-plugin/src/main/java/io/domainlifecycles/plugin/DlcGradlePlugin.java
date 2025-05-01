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

package io.domainlifecycles.plugin;

import io.domainlifecycles.plugin.diagram.CreateDiagramTask;
import io.domainlifecycles.plugin.extensions.DiagramTaskConfigurationExtension;
import io.domainlifecycles.plugin.extensions.DlcGradlePluginExtension;
import io.domainlifecycles.plugin.extensions.DomainModelUploadTaskConfigurationExtension;
import io.domainlifecycles.plugin.extensions.JsonTaskConfigurationExtension;
import io.domainlifecycles.plugin.json.JsonRenderTask;
import io.domainlifecycles.plugin.viewer.UploadDomainModelTask;
import io.domainlifecycles.plugins.exception.DLCPluginsException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.ExtensionAware;

/**
 * Implements a Gradle plugin for dynamically configuring and registering tasks related
 * to diagram generation and JSON model rendering during a Gradle build lifecycle.
 *
 * This plugin provides two primary task configurations that can be customized via Gradle DSL:
 * - Diagram generation (`createDiagram`) through the `DiagramTaskConfigurationExtension`.
 * - JSON model rendering (`renderJson`) through the `JsonTaskConfigurationExtension`.
 *
 * The plugin defines its main extension, {@code DlcGradlePluginExtension}, which serves as
 * an entry point for setting up these configurations. Specific configurations for diagram
 * and JSON model tasks are additionally handled through the {@code ExtensionAware} interface.
 *
 * Tasks are registered after the project's evaluation phase to ensure all configurations
 * are resolved before task execution.
 *
 * Throws:
 * - {@code DLCPluginsException} if the creation of necessary Gradle plugin extensions fails.
 *
 * @author Leon Völlinger
 */
public class DlcGradlePlugin implements Plugin<Project> {

    /**
     * Configures and applies the plugin to the given Gradle project. This method initializes
     * the necessary plugin extensions and defines tasks for the project's lifecycle. It registers
     * two tasks, "createDiagram" and "renderJson", after all project evaluations have been completed.
     *
     * - The "createDiagram" task generates diagrams based on the configurations provided
     *   by {@code DiagramTaskConfigurationExtension}.
     * - The "renderJson" task renders JSON models according to the configurations defined
     *   in the {@code JsonTaskConfigurationExtension}.
     *
     * If the required extensions cannot be created, this method throws a {@code DLCPluginsException}.
     *
     * @param project the Gradle project to which this plugin is applied
     */
    @Override
    public void apply(Project project) {
        DlcGradlePluginExtension pluginExtension =
            project.getExtensions().create("dlcGradlePlugin", DlcGradlePluginExtension.class);

        JsonTaskConfigurationExtension jsonModelExtension;
        DiagramTaskConfigurationExtension diagramExtension;
        DomainModelUploadTaskConfigurationExtension domainModelUploadTaskConfigurationExtension;

        if (pluginExtension instanceof ExtensionAware extensionAware) {
            jsonModelExtension = extensionAware.getExtensions().create("jsonModel", JsonTaskConfigurationExtension.class);
            diagramExtension = extensionAware.getExtensions().create("diagram", DiagramTaskConfigurationExtension.class);
            domainModelUploadTaskConfigurationExtension = extensionAware.getExtensions()
                .create("domainModelUpload", DomainModelUploadTaskConfigurationExtension.class);
        }
        else {
            throw DLCPluginsException.fail("Could not create Gradle extension of DlcGradlePluginExtension.");
        }

        project.afterEvaluate(proj -> {
            project.getTasks().register("createDiagram", CreateDiagramTask.class, task -> {
                task.getFileOutputDir().set(diagramExtension.getFileOutputDir());
                task.getDiagrams().addAll(diagramExtension.getDiagrams());
            });

            project.getTasks().register("renderJson", JsonRenderTask.class, task -> {
                task.getFileOutputDir().set(jsonModelExtension.getFileOutputDir());
                task.getSerializations().addAll(jsonModelExtension.getSerializations());
            });

            project.getTasks().register("domainModelUpload", UploadDomainModelTask.class, task -> {
                task.getProjectName().set(domainModelUploadTaskConfigurationExtension.getProjectName());
                task.getApiKey().set(domainModelUploadTaskConfigurationExtension.getApiKey());
                task.getDiagramViewerBaseUrl().set(domainModelUploadTaskConfigurationExtension.getDiagramViewerBaseUrl());
                task.getContextPackages().set(domainModelUploadTaskConfigurationExtension.getContextPackages());
            });
        });
    }
}
