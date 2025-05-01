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
 *  Copyright 2019-2024 the original author or authors.
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

package io.domainlifecycles.plugin.viewer;

import io.domainlifecycles.plugins.json.JsonSerializer;
import io.domainlifecycles.plugins.json.JsonSerializerImpl;
import io.domainlifecycles.plugins.viewer.DomainModelUploader;
import io.domainlifecycles.plugins.viewer.DomainModelUploaderImpl;
import io.domainlifecycles.utils.ClassLoaderUtils;
import java.util.List;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mojo(name = "uploadDomainModel", requiresDependencyResolution = ResolutionScope.COMPILE, defaultPhase = LifecyclePhase.INITIALIZE)
public class UploadDomainModelGoal extends AbstractMojo {

    private final static Logger LOGGER = LoggerFactory.getLogger(UploadDomainModelGoal.class);

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Parameter(property = "diagramViewerBaseUrl", required = true)
    private String diagramViewerBaseUrl;

    @Parameter(property = "apiKey", required = true)
    private String apiKey;

    @Parameter(property = "projectName", required = true)
    private String projectName;

    @Parameter(property = "contextPackages", required = true)
    private List<String> contextPackages;

    private DomainModelUploader domainModelUploader;

    @Override
    public void execute() {
        LOGGER.info("Running Upload Domain Model Goal...");
        domainModelUploader = new DomainModelUploaderImpl();
        uploadDomainModel();
    }

    private void uploadDomainModel() {
        JsonSerializer jsonSerializer = new JsonSerializerImpl(true);
        final String domainModelJson = jsonSerializer.serialize(ClassLoaderUtils.getParentClasspathFiles(project), contextPackages);
        domainModelUploader.uploadDomainModel(domainModelJson, apiKey, projectName, diagramViewerBaseUrl);
    }
}
