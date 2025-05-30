package io.domainlifecycles.diagram;

import io.domainlifecycles.diagram.domain.DomainDiagramGenerator;
import io.domainlifecycles.diagram.domain.config.DiagramTrimSettings;
import io.domainlifecycles.diagram.domain.config.DomainDiagramConfig;
import io.domainlifecycles.diagram.domain.config.GeneralVisualSettings;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.mirror.resolver.TypeMetaResolver;
import org.junit.jupiter.api.Test;
import sampleshop.core.domain.customer.ChangeCustomerAddress;
import sampleshop.core.domain.customer.CustomerAdressChanged;
import sampleshop.core.domain.customer.CustomerCreditCardChanged;
import sampleshop.core.domain.customer.FraudDetected;
import sampleshop.core.domain.customer.NewCustomerAdded;
import sampleshop.core.domain.order.Order;
import sampleshop.core.domain.order.OrderCanceled;
import sampleshop.core.domain.order.OrderPlacementService;
import sampleshop.core.domain.order.OrderShipped;
import sampleshop.core.domain.order.PlaceOrder;
import sampleshop.core.inport.OrderDriver;
import sampleshop.core.outport.OrderRepository;
import sampleshop.core.outport.OrdersByCustomerQueryHandler;

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
    void generateSampleAppWithResolvedGenericsIncludeDomainCommandOutgoingExcludeSpecificEvents() {
        var factory = new ReflectiveDomainMirrorFactory("sampleshop");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        Domain.initialize(factory);
        var trim = DiagramTrimSettings.builder()
            .withIncludeConnectedToOutgoing(List.of(PlaceOrder.class.getName()))
            .withClassesBlacklist(List.of(
                CustomerAdressChanged.class.getName(),
                CustomerCreditCardChanged.class.getName(),
                OrderCanceled.class.getName(),
                OrderShipped.class.getName()
            ))
            .build();
        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder().withDiagramTrimSettings(trim).build();
        DomainDiagramGenerator generator = new DomainDiagramGenerator(
            diagramConfig, Domain.getDomainMirror());

        // when
        String actualDiagramText = generator.generateDiagramText();

        Path filePath = Path.of("src/test/resources/sampleshop_resolved_ingoing_command_exclude_events.nomnoml");
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
    void generateSampleAppWithResolvedGenericsExcludeDomainEventIngoing() {
        var factory = new ReflectiveDomainMirrorFactory("sampleshop");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        Domain.initialize(factory);
        var trim = DiagramTrimSettings.builder()
            .withExcludeConnectedToIngoing(List.of(NewCustomerAdded.class.getName()))
            .build();

        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder().withDiagramTrimSettings(trim).build();
        DomainDiagramGenerator generator = new DomainDiagramGenerator(
            diagramConfig, Domain.getDomainMirror());

        // when
        String actualDiagramText = generator.generateDiagramText();

        Path filePath = Path.of("src/test/resources/sampleshop_resolved_exclude_domain_event_ingoing.nomnoml");
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
    void generateSampleAppWithResolvedGenericsExcludeAggregateIngoing() {
        var factory = new ReflectiveDomainMirrorFactory("sampleshop");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        Domain.initialize(factory);
        var trim = DiagramTrimSettings.builder()
            .withExplicitlyIncludedPackageNames(List.of("sampleshop"))
            .withExcludeConnectedToIngoing(List.of(Order.class.getName()))
            .build();

        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder().withDiagramTrimSettings(trim).build();
        DomainDiagramGenerator generator = new DomainDiagramGenerator(
            diagramConfig, Domain.getDomainMirror());

        // when
        String actualDiagramText = generator.generateDiagramText();

        Path filePath = Path.of("src/test/resources/sampleshop_resolved_exclude_aggregate_ingoing.nomnoml");
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
    void generateSampleAppWithResolvedGenericsExcludeRepositoryIngoing() {
        var factory = new ReflectiveDomainMirrorFactory("sampleshop");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        Domain.initialize(factory);
        var trim = DiagramTrimSettings.builder()
            .withExplicitlyIncludedPackageNames(List.of("sampleshop"))
            .withExcludeConnectedToIngoing(List.of(OrderRepository.class.getName()))
            .build();

        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder().withDiagramTrimSettings(trim).build();
        DomainDiagramGenerator generator = new DomainDiagramGenerator(
            diagramConfig, Domain.getDomainMirror());

        // when
        String actualDiagramText = generator.generateDiagramText();

        Path filePath = Path.of("src/test/resources/sampleshop_resolved_exclude_repository_ingoing.nomnoml");
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
    void generateSampleAppWithResolvedGenericsShowAbstract() {
        var factory = new ReflectiveDomainMirrorFactory("sampleshop");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        Domain.initialize(factory);
        var trim = DiagramTrimSettings.builder().withExplicitlyIncludedPackageNames(List.of("sampleshop")).build();
        var gen = GeneralVisualSettings.builder()
            .withShowAllInheritanceStructures(true)
            .build();
        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder()
            .withDiagramTrimSettings(trim)
            .withGeneralVisualSettings(gen)
            .build();
        DomainDiagramGenerator generator = new DomainDiagramGenerator(
            diagramConfig, Domain.getDomainMirror());

        // when
        String actualDiagramText = generator.generateDiagramText();

        Path filePath = Path.of("src/test/resources/sampleshop_resolved_show_abstract.nomnoml");
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
    void generateSampleAppWithResolvedGenericsShowAbstractExcludeQueryHandlerOutgoing() {
        var factory = new ReflectiveDomainMirrorFactory("sampleshop");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        Domain.initialize(factory);
        var trim = DiagramTrimSettings.builder()
            .withExplicitlyIncludedPackageNames(List.of("sampleshop"))
            .withExcludeConnectedToOutgoing(List.of(OrdersByCustomerQueryHandler.class.getName()))
            .build();
        var gen = GeneralVisualSettings.builder()
            .withShowAllInheritanceStructures(true)
            .build();
        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder()
            .withDiagramTrimSettings(trim)
            .withGeneralVisualSettings(gen)
            .build();
        DomainDiagramGenerator generator = new DomainDiagramGenerator(
            diagramConfig, Domain.getDomainMirror());

        // when
        String actualDiagramText = generator.generateDiagramText();

        Path filePath = Path.of("src/test/resources/sampleshop_resolved_show_abstract_exclude_query_handler_outgoing.nomnoml");
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
    void generateSampleAppWithResolvedGenericsWithConnectedTo() {
        var factory = new ReflectiveDomainMirrorFactory("sampleshop");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        Domain.initialize(factory);
        var trim = DiagramTrimSettings.builder()
            .withExplicitlyIncludedPackageNames(List.of("sampleshop"))
            .withIncludeConnectedTo(List.of(OrderDriver.class.getName()))
            .build();
        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder().withDiagramTrimSettings(trim).build();


        DomainDiagramGenerator generator = new DomainDiagramGenerator(
            diagramConfig, Domain.getDomainMirror());

        // when
        String actualDiagramText = generator.generateDiagramText();

        Path filePath = Path.of("src/test/resources/sampleshop_resolved_connected_to.nomnoml");
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
    void generateSampleAppWithResolvedGenericsWithConnectedToExcludeDomainServiceOutgoing() {
        var factory = new ReflectiveDomainMirrorFactory("sampleshop");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        Domain.initialize(factory);
        var trim = DiagramTrimSettings.builder()
            .withExplicitlyIncludedPackageNames(List.of("sampleshop"))
            .withIncludeConnectedTo(List.of(OrderDriver.class.getName()))
            .withExcludeConnectedToOutgoing(List.of(OrderPlacementService.class.getName()))
            .build();
        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder().withDiagramTrimSettings(trim).build();


        DomainDiagramGenerator generator = new DomainDiagramGenerator(
            diagramConfig, Domain.getDomainMirror());

        // when
        String actualDiagramText = generator.generateDiagramText();

        Path filePath = Path.of("src/test/resources/sampleshop_resolved_connected_to_exclude_domain_service_outgoing.nomnoml");
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
    void generateSampleAppWithResolvedGenericsWithInclusiveEventOutgoing() {
        var factory = new ReflectiveDomainMirrorFactory("sampleshop");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        Domain.initialize(factory);
        var trim = DiagramTrimSettings.builder()
            .withExplicitlyIncludedPackageNames(List.of("sampleshop"))
            .withIncludeConnectedToOutgoing(List.of(FraudDetected.class.getName()))
            .build();
        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder().withDiagramTrimSettings(trim).build();


        DomainDiagramGenerator generator = new DomainDiagramGenerator(
            diagramConfig, Domain.getDomainMirror());

        // when
        String actualDiagramText = generator.generateDiagramText();

        Path filePath = Path.of("src/test/resources/sampleshop_resolved_outgoing_event.nomnoml");
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
    void generateSampleAppWithResolvedGenericsWithInclusiveEventIngoing() {
        var factory = new ReflectiveDomainMirrorFactory("sampleshop");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        Domain.initialize(factory);
        var trim = DiagramTrimSettings.builder()
            .withExplicitlyIncludedPackageNames(List.of("sampleshop"))
            .withIncludeConnectedToIngoing(List.of(OrderCanceled.class.getName()))
            .build();
        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder().withDiagramTrimSettings(trim).build();


        DomainDiagramGenerator generator = new DomainDiagramGenerator(
            diagramConfig, Domain.getDomainMirror());

        // when
        String actualDiagramText = generator.generateDiagramText();

        Path filePath = Path.of("src/test/resources/sampleshop_resolved_ingoing_event.nomnoml");
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
    void generateSampleAppWithResolvedGenericsWithInclusiveCommandOutgoing() {
        var factory = new ReflectiveDomainMirrorFactory("sampleshop");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        Domain.initialize(factory);
        var trim = DiagramTrimSettings.builder()
            .withExplicitlyIncludedPackageNames(List.of("sampleshop"))
            .withIncludeConnectedToOutgoing(List.of(ChangeCustomerAddress.class.getName()))
            .build();
        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder().withDiagramTrimSettings(trim).build();


        DomainDiagramGenerator generator = new DomainDiagramGenerator(
            diagramConfig, Domain.getDomainMirror());

        // when
        String actualDiagramText = generator.generateDiagramText();

        Path filePath = Path.of("src/test/resources/sampleshop_resolved_outgoing_command.nomnoml");
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
    void generateSampleAppWithResolvedGenericsWithInclusiveToDomainServiceOutgoing() {
        var factory = new ReflectiveDomainMirrorFactory("sampleshop");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        Domain.initialize(factory);
        var trim = DiagramTrimSettings.builder()
            .withExplicitlyIncludedPackageNames(List.of("sampleshop"))
            .withIncludeConnectedToOutgoing(List.of(OrderPlacementService.class.getName()))
            .build();
        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder().withDiagramTrimSettings(trim).build();


        DomainDiagramGenerator generator = new DomainDiagramGenerator(
            diagramConfig, Domain.getDomainMirror());

        // when
        String actualDiagramText = generator.generateDiagramText();

        Path filePath = Path.of("src/test/resources/sampleshop_resolved_outgoing_domainservice.nomnoml");
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
    void generateSampleAppWithResolvedGenericsWithInclusiveDomainServiceIngoing() {
        var factory = new ReflectiveDomainMirrorFactory("sampleshop");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        Domain.initialize(factory);
        var trim = DiagramTrimSettings.builder()
            .withExplicitlyIncludedPackageNames(List.of("sampleshop"))
            .withIncludeConnectedToIngoing(List.of(OrderPlacementService.class.getName()))
            .build();
        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder().withDiagramTrimSettings(trim).build();


        DomainDiagramGenerator generator = new DomainDiagramGenerator(
            diagramConfig, Domain.getDomainMirror());

        // when
        String actualDiagramText = generator.generateDiagramText();

        Path filePath = Path.of("src/test/resources/sampleshop_resolved_ingoing_domainservice.nomnoml");
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
    void generateSampleAppWithResolvedGenericsWithInclusiveApplicationServiceOutgoing() {
        var factory = new ReflectiveDomainMirrorFactory("sampleshop");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        Domain.initialize(factory);
        var trim = DiagramTrimSettings.builder()
            .withExplicitlyIncludedPackageNames(List.of("sampleshop"))
            .withIncludeConnectedToOutgoing(List.of(OrderDriver.class.getName()))
            .build();
        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder().withDiagramTrimSettings(trim).build();


        DomainDiagramGenerator generator = new DomainDiagramGenerator(
            diagramConfig, Domain.getDomainMirror());

        // when
        String actualDiagramText = generator.generateDiagramText();

        Path filePath = Path.of("src/test/resources/sampleshop_resolved_outgoing_applicationservice.nomnoml");
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
    void generateSampleAppWithResolvedGenericsWithConnectedToApplicationServiceIngoing() {
        var factory = new ReflectiveDomainMirrorFactory("sampleshop");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        Domain.initialize(factory);
        var trim = DiagramTrimSettings.builder()
            .withExplicitlyIncludedPackageNames(List.of("sampleshop"))
            .withIncludeConnectedToIngoing(List.of(OrderDriver.class.getName()))
            .build();
        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder().withDiagramTrimSettings(trim).build();


        DomainDiagramGenerator generator = new DomainDiagramGenerator(
            diagramConfig, Domain.getDomainMirror());

        // when
        String actualDiagramText = generator.generateDiagramText();

        Path filePath = Path.of("src/test/resources/sampleshop_resolved_ingoing_applicationservice.nomnoml");
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
