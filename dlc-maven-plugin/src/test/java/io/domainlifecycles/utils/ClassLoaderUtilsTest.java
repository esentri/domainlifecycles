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

package io.domainlifecycles.utils;

import io.domainlifecycles.exception.DLCMavenPluginException;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.model.Build;
import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClassLoaderUtilsTest {

    @Test
    void combinesCompileAndRuntimeClasspathWithOutputDirectory() throws DependencyResolutionRequiredException {
        MavenProject project = mock(MavenProject.class);
        when(project.getCompileClasspathElements())
            .thenReturn(new ArrayList<>(List.of("/project/compile.jar")));
        when(project.getRuntimeClasspathElements())
            .thenReturn(new ArrayList<>(List.of("/project/runtime.jar")));
        Build build = new Build();
        build.setOutputDirectory("/project/target/classes");
        when(project.getBuild()).thenReturn(build);

        List<URL> urls = ClassLoaderUtils.getParentClasspathFiles(project);

        List<String> paths = urls.stream().map(URL::getPath).toList();
        assertThat(paths).hasSize(3);
        assertThat(paths).anySatisfy(path -> assertThat(path).endsWith("compile.jar"));
        assertThat(paths).anySatisfy(path -> assertThat(path).endsWith("runtime.jar"));
        assertThat(paths).anySatisfy(path -> assertThat(path).contains("classes"));
    }

    @Test
    void wrapsDependencyResolutionRequiredExceptionInDLCMavenPluginException() throws DependencyResolutionRequiredException {
        MavenProject project = mock(MavenProject.class);
        when(project.getId()).thenReturn("io.domainlifecycles:example:1.0.0");
        when(project.getCompileClasspathElements())
            .thenThrow(new DependencyResolutionRequiredException(null));

        assertThatThrownBy(() -> ClassLoaderUtils.getParentClasspathFiles(project))
            .isInstanceOf(DLCMavenPluginException.class)
            .hasMessageContaining("io.domainlifecycles:example:1.0.0");
    }
}
