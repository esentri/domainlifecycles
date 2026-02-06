
package io.domainlifecycles.diagram;

import io.domainlifecycles.diagram.domain.DomainDiagramGenerator;
import io.domainlifecycles.diagram.domain.config.DiagramTrimSettings;
import io.domainlifecycles.diagram.domain.config.DomainDiagramConfig;
import io.domainlifecycles.diagram.domain.config.GeneralVisualSettings;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.mirror.resolver.TypeMetaResolver;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class NomnomlDomainDiagramGeneratorTest {

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

    @Test
    void generateAllTestsWithResolvedGenericsAllInheritance() {
        var factory = new ReflectiveDomainMirrorFactory("tests.shared");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        Domain.initialize(factory);
        var trim = DiagramTrimSettings.builder().withExplicitlyIncludedPackageNames(List.of("tests.shared")).build();
        var general = GeneralVisualSettings.builder()
            .withShowAllInheritanceStructures(true)
            .build();

        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder()
            .withDiagramTrimSettings(trim)
            .withGeneralVisualSettings(general)
            .build();

        DomainDiagramGenerator generator = new DomainDiagramGenerator(
            diagramConfig, Domain.getDomainMirror());

        // when
        String actualDiagramText = generator.generateDiagramText();

        Path filePath = Path.of("src/test/resources/tests_resolved_all_inheritance.nomnoml");
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
    void generateAllTestsWithResolvedGenericsReadModelInheritance() {
        var factory = new ReflectiveDomainMirrorFactory("tests.shared");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        Domain.initialize(factory);
        var trim = DiagramTrimSettings.builder().withExplicitlyIncludedPackageNames(List.of("tests.shared")).build();
        var general = GeneralVisualSettings.builder()
            .withShowInheritanceStructuresForReadModels(true)
            .build();

        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder()
            .withDiagramTrimSettings(trim)
            .withGeneralVisualSettings(general)
            .build();

        DomainDiagramGenerator generator = new DomainDiagramGenerator(
            diagramConfig, Domain.getDomainMirror());

        // when
        String actualDiagramText = generator.generateDiagramText();

        Path filePath = Path.of("src/test/resources/tests_resolved_read_model_inheritance.nomnoml");
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
    void generateAllTestsWithResolvedGenericsDomainCommandInheritance() {
        var factory = new ReflectiveDomainMirrorFactory("tests.shared");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        Domain.initialize(factory);
        var trim = DiagramTrimSettings.builder().withExplicitlyIncludedPackageNames(List.of("tests.shared")).build();
        var general = GeneralVisualSettings.builder()
            .withShowInheritanceStructuresForDomainCommands(true)
            .build();

        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder()
            .withDiagramTrimSettings(trim)
            .withGeneralVisualSettings(general)
            .build();

        DomainDiagramGenerator generator = new DomainDiagramGenerator(
            diagramConfig, Domain.getDomainMirror());

        // when
        String actualDiagramText = generator.generateDiagramText();

        Path filePath = Path.of("src/test/resources/tests_resolved_domain_command_inheritance.nomnoml");
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
    void generateAllTestsWithResolvedGenericsDomainEventInheritance() {
        var factory = new ReflectiveDomainMirrorFactory("tests.shared");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        Domain.initialize(factory);
        var trim = DiagramTrimSettings.builder().withExplicitlyIncludedPackageNames(List.of("tests.shared")).build();
        var general = GeneralVisualSettings.builder()
            .withShowInheritanceStructuresForDomainEvents(true)
            .build();

        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder()
            .withDiagramTrimSettings(trim)
            .withGeneralVisualSettings(general)
            .build();

        DomainDiagramGenerator generator = new DomainDiagramGenerator(
            diagramConfig, Domain.getDomainMirror());

        // when
        String actualDiagramText = generator.generateDiagramText();

        Path filePath = Path.of("src/test/resources/tests_resolved_domain_event_inheritance.nomnoml");
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

