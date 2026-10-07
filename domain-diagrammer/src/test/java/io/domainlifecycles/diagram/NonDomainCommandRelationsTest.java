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

package io.domainlifecycles.diagram;

import fixtures.nondomaincommand.CancelOrder;
import fixtures.nondomaincommand.OrderApplicationService;
import fixtures.nondomaincommand.OrderController;
import fixtures.nondomaincommand.PlaceOrder;
import io.domainlifecycles.diagram.domain.DomainDiagramGenerator;
import io.domainlifecycles.diagram.domain.config.DiagramTrimSettings;
import io.domainlifecycles.diagram.domain.config.DomainDiagramConfig;
import io.domainlifecycles.diagram.domain.config.GeneralVisualSettings;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A command received by a non-domain class - a controller or message listener - is connected to it, just like to an
 * ApplicationService processing it; with {@code showOnlyTopLevelDomainCommandRelations} (default) only to the
 * outermost of the classes processing it.
 * <p>
 * The fixture in {@code fixtures.nondomaincommand}:
 * <pre>
 * PlaceOrder  --received by--&gt; OrderController.place --field--&gt; OrderApplicationService.place
 * CancelOrder --received by--&gt; OrderController.cancel
 * </pre>
 *
 * @author Mario Herb
 */
public class NonDomainCommandRelationsTest {

    @BeforeEach
    void initializeMirror() {
        Domain.initialize(new ReflectiveDomainMirrorFactory("fixtures.nondomaincommand"));
    }

    @Test
    void testACommandOnlyANonDomainClassReceivesIsConnectedToIt() {
        var diagramText = generate(GeneralVisualSettings.builder().build());

        assertThat(diagramText).contains("[<DC>CancelOrder <<DomainCommand>>]  --[<label> <<is processed by>> "
            + "OrderController.cancel] --> [<ND>OrderController <<NonDomain>>]");
    }

    @Test
    void testByDefaultTheNonDomainClassForwardingTheCommandIsItsOnlyConsumerConnected() {
        var diagramText = generate(GeneralVisualSettings.builder().build());

        assertThat(hasProcessingRelationship(diagramText, PlaceOrder.class, OrderController.class)).isTrue();
        assertThat(hasProcessingRelationship(diagramText, PlaceOrder.class, OrderApplicationService.class))
            .as("referenced by OrderController, which receives the same command")
            .isFalse();
    }

    @Test
    void testDisablingTheSettingConnectsEveryConsumer() {
        var diagramText = generate(
            GeneralVisualSettings.builder().withShowOnlyTopLevelDomainCommandRelations(false).build());

        assertThat(hasProcessingRelationship(diagramText, PlaceOrder.class, OrderController.class)).isTrue();
        assertThat(hasProcessingRelationship(diagramText, PlaceOrder.class, OrderApplicationService.class)).isTrue();
        assertThat(hasProcessingRelationship(diagramText, CancelOrder.class, OrderController.class)).isTrue();
    }

    @Test
    void testAHiddenNonDomainClassLeavesTheRelationshipToTheServiceBehindIt() {
        var diagramText = generate(GeneralVisualSettings.builder().withShowNonDomainClasses(false).build());

        assertThat(diagramText).doesNotContain(OrderController.class.getSimpleName());
        assertThat(hasProcessingRelationship(diagramText, PlaceOrder.class, OrderApplicationService.class))
            .as("otherwise nothing shown would be connected to the command")
            .isTrue();
    }

    private static String generate(GeneralVisualSettings generalVisualSettings) {
        var trim = DiagramTrimSettings.builder()
            .withExplicitlyIncludedPackageNames(List.of("fixtures.nondomaincommand"))
            .build();
        var config = DomainDiagramConfig.builder()
            .withDiagramTrimSettings(trim)
            .withGeneralVisualSettings(generalVisualSettings)
            .build();
        return new DomainDiagramGenerator(config, Domain.getDomainMirror()).generateDiagramText();
    }

    private static boolean hasProcessingRelationship(String diagramText, Class<?> command, Class<?> consumer) {
        return diagramText.lines()
            .anyMatch(line -> line.startsWith("[<DC>" + command.getSimpleName() + " ")
                && line.contains("is processed by")
                && line.endsWith(consumer.getSimpleName() + " <<" + stereotype(consumer) + ">>]"));
    }

    private static String stereotype(Class<?> consumer) {
        return consumer.equals(OrderController.class) ? "NonDomain" : "ApplicationService";
    }
}
