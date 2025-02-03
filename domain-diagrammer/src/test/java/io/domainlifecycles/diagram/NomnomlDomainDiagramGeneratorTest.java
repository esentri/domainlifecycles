package io.domainlifecycles.diagram;

import io.domainlifecycles.diagram.domain.DomainDiagramGenerator;
import io.domainlifecycles.diagram.domain.config.DomainDiagramConfig;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainModelFactory;
import io.domainlifecycles.mirror.resolver.DefaultEmptyGenericTypeResolver;
import io.domainlifecycles.mirror.resolver.TypeMetaResolver;
import org.junit.jupiter.api.Test;
import sampleshop.core.inport.OrderDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class NomnomlDomainDiagramGeneratorTest {

    @Test
    void generateSampleApp() {
        Domain.initialize(new ReflectiveDomainModelFactory("sampleshop"));
        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder().withContextPackageName("sampleshop").build();
        DomainDiagramGenerator generator = new DomainDiagramGenerator(
            diagramConfig, Domain.getDomainModel());

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
        Domain.initialize(new ReflectiveDomainModelFactory("tests.shared"));
        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder()
            .withContextPackageName("tests.shared")
            .build();
        DomainDiagramGenerator generator = new DomainDiagramGenerator(
            diagramConfig, Domain.getDomainModel());

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
        Domain.initialize(new ReflectiveDomainModelFactory(new TypeMetaResolver(), "sampleshop"));
        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder().withContextPackageName("sampleshop").build();
        DomainDiagramGenerator generator = new DomainDiagramGenerator(
            diagramConfig, Domain.getDomainModel());

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
        Domain.initialize(new ReflectiveDomainModelFactory(new TypeMetaResolver(), "sampleshop"));
        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder()
            .withContextPackageName("sampleshop")
            .withTransitiveFilterSeedDomainServiceTypeNames(List.of(OrderDriver.class.getName()))
            .build();

        DomainDiagramGenerator generator = new DomainDiagramGenerator(
            diagramConfig, Domain.getDomainModel());

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
        Domain.initialize(new ReflectiveDomainModelFactory(new TypeMetaResolver(), "tests.shared"));
        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder()
            .withContextPackageName("tests.shared")
            .build();
        DomainDiagramGenerator generator = new DomainDiagramGenerator(
            diagramConfig, Domain.getDomainModel());

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
