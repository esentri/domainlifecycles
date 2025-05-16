package io.domainlifecycles.diagram;

import io.domainlifecycles.diagram.domain.DomainDiagramGenerator;
import io.domainlifecycles.diagram.domain.config.DiagramTrimSettings;
import io.domainlifecycles.diagram.domain.config.DomainDiagramConfig;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
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
        Domain.initialize(new ReflectiveDomainMirrorFactory("sampleshop"));
        var trim = DiagramTrimSettings.builder().withExplicitlyIncludedPackageNames(List.of("sampleshop")).build();
        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder().withDiagramTrimSettings(trim).build();
        DomainDiagramGenerator generator = new DomainDiagramGenerator(
            diagramConfig, Domain.getDomainMirror());

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
        Domain.initialize(new ReflectiveDomainMirrorFactory("tests.shared"));
        var trim = DiagramTrimSettings.builder().withExplicitlyIncludedPackageNames(List.of("tests.shared")).build();
        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder().withDiagramTrimSettings(trim).build();
        DomainDiagramGenerator generator = new DomainDiagramGenerator(
            diagramConfig, Domain.getDomainMirror());

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
        var factory = new ReflectiveDomainMirrorFactory("sampleshop");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        Domain.initialize(factory);
        var trim = DiagramTrimSettings.builder().withExplicitlyIncludedPackageNames(List.of("sampleshop")).build();
        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder().withDiagramTrimSettings(trim).build();
        DomainDiagramGenerator generator = new DomainDiagramGenerator(
            diagramConfig, Domain.getDomainMirror());

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
        var factory = new ReflectiveDomainMirrorFactory("sampleshop");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        Domain.initialize(factory);
        var trim = DiagramTrimSettings.builder()
            .withExplicitlyIncludedPackageNames(List.of("sampleshop"))
            .withTransitiveFilterSeedDomainServiceTypeNames(List.of(OrderDriver.class.getName()))
            .build();
        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder().withDiagramTrimSettings(trim).build();


        DomainDiagramGenerator generator = new DomainDiagramGenerator(
            diagramConfig, Domain.getDomainMirror());

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
        var factory = new ReflectiveDomainMirrorFactory("tests.shared");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        Domain.initialize(factory);
        var trim = DiagramTrimSettings.builder().withExplicitlyIncludedPackageNames(List.of("tests.shared")).build();
        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder().withDiagramTrimSettings(trim).build();

        DomainDiagramGenerator generator = new DomainDiagramGenerator(
            diagramConfig, Domain.getDomainMirror());

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
