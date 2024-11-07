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
