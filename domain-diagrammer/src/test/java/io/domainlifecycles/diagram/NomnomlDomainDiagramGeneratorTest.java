/*
 *
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

package io.domainlifecycles.diagram;

import lombok.extern.slf4j.Slf4j;
import io.domainlifecycles.diagram.domain.DomainDiagramGenerator;
import io.domainlifecycles.diagram.domain.config.DomainDiagramConfig;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.mirror.resolver.DefaultEmptyGenericTypeResolver;
import io.domainlifecycles.mirror.resolver.TypeMetaResolver;

import org.junit.jupiter.api.Test;
import sampleshop.core.inport.OrderDriver;
import tests.shared.persistence.domain.records.RecordTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class NomnomlDomainDiagramGeneratorTest {

    @Test
    void generateSampleApp() {
        Domain.setGenericTypeResolver(new DefaultEmptyGenericTypeResolver());
        Domain.initialize(new ReflectiveDomainMirrorFactory("sampleshop"));
        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder().withContextPackageName("sampleshop").build();
        DomainDiagramGenerator generator = new DomainDiagramGenerator(
            diagramConfig);

        // when
        String actualDiagramText = generator.generateDiagramText();

        Path filePath = Path.of("src/test/resources/sampleshop.nomnoml");
        String content;
        try {
            content = Files.readString(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //then
        assertThat(actualDiagramText).isEqualTo(content);

    }

    @Test
    void generateAllTests() {
        Domain.setGenericTypeResolver(new DefaultEmptyGenericTypeResolver());
        Domain.initialize(new ReflectiveDomainMirrorFactory("tests.shared"));
        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder()
            .withContextPackageName("tests.shared")
            .build();
        DomainDiagramGenerator generator = new DomainDiagramGenerator(
            diagramConfig);

        // when
        String actualDiagramText = generator.generateDiagramText();

        Path filePath = Path.of("src/test/resources/tests.nomnoml");
        String content;
        try {
            content = Files.readString(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //then
        assertThat(actualDiagramText).isEqualTo(content);
    }

    @Test
    void generateSampleAppWithResolvedGenerics() {
        Domain.setGenericTypeResolver(new TypeMetaResolver());
        Domain.initialize(new ReflectiveDomainMirrorFactory("sampleshop"));
        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder().withContextPackageName("sampleshop").build();
        DomainDiagramGenerator generator = new DomainDiagramGenerator(
            diagramConfig);

        // when
        String actualDiagramText = generator.generateDiagramText();

        Path filePath = Path.of("src/test/resources/sampleshop_resolved.nomnoml");
        String content;
        try {
            content = Files.readString(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //then
        assertThat(actualDiagramText).isEqualTo(content);
    }

    @Test
    void generateSampleAppWithResolvedGenericsWithTransitiveFilter() {
        Domain.setGenericTypeResolver(new TypeMetaResolver());
        Domain.initialize(new ReflectiveDomainMirrorFactory("sampleshop"));
        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder()
            .withContextPackageName("sampleshop")
            .withTransitiveFilterSeedDomainServiceTypeNames(List.of(OrderDriver.class.getName()))
            .build();

        DomainDiagramGenerator generator = new DomainDiagramGenerator(
            diagramConfig);

        // when
        String actualDiagramText = generator.generateDiagramText();

        Path filePath = Path.of("src/test/resources/sampleshop_resolved_transistive_filter.nomnoml");
        String content;
        try {
            content = Files.readString(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //then
        assertThat(actualDiagramText).isEqualTo(content);
    }

    @Test
    void generateAllTestsWithResolvedGenerics() {
        Domain.setGenericTypeResolver(new TypeMetaResolver());
        Domain.initialize(new ReflectiveDomainMirrorFactory("tests.shared"));
        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder()
            .withContextPackageName("tests.shared")
            .build();
        DomainDiagramGenerator generator = new DomainDiagramGenerator(
            diagramConfig);

        // when
        String actualDiagramText = generator.generateDiagramText();

        Path filePath = Path.of("src/test/resources/tests_resolved.nomnoml");
        String content;
        try {
            content = Files.readString(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //then
        assertThat(actualDiagramText).isEqualTo(content);
    }

}
