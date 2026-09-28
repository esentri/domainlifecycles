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
 * Nomnoml identifies a node by its name. Classes sharing a simple name but living in different packages must still be
 * drawn as nodes of their own, named with a hint to their package.
 * <p>
 * The fixture in {@code fixtures.samename} has three {@code OrderService} domain services - in
 * {@code fixtures.samename}, {@code fixtures.samename.billing} and {@code fixtures.samename.shipping} - and the
 * application service {@code Checkout} using the latter two. {@code billing.PaymentClient} is implemented by
 * {@code billing.impl.PaymentClient}, named like its interface.
 *
 * @author Mario Herb
 */
public class SameSimpleNameDiagramTest {

    @BeforeEach
    void initializeMirror() {
        Domain.initialize(new ReflectiveDomainMirrorFactory("fixtures.samename"));
    }

    @Test
    void testClassesSharingANameAreSeparateNodesNamedWithTheirPackageBelowTheCommonOne() {
        var diagramText = generate(List.of("fixtures.samename"), GeneralVisualSettings.builder().build());

        assertThat(diagramText)
            .contains("[<DS> OrderService (billing) <<DomainService>>")
            .contains("[<DS> OrderService (shipping) <<DomainService>>")
            .as("a class right in the common package is named by its full package")
            .contains("[<DS> OrderService (fixtures.samename) <<DomainService>>")
            .doesNotContain("[<DS> OrderService <<DomainService>>");
    }

    @Test
    void testRelationshipsConnectTheNodeOfTheirClass() {
        var diagramText = generate(List.of("fixtures.samename"), GeneralVisualSettings.builder().build());

        assertThat(diagramText)
            .contains("[<AS>Checkout <<ApplicationService>>]  -> [<DS>OrderService (billing) <<DomainService>>]")
            .contains("[<AS>Checkout <<ApplicationService>>]  -> [<DS>OrderService (shipping) <<DomainService>>]")
            .doesNotContain("-> [<DS>OrderService (fixtures.samename) <<DomainService>>]");
    }

    @Test
    void testAClassWithANameOfItsOwnGetsNoHint() {
        var diagramText = generate(List.of("fixtures.samename"), GeneralVisualSettings.builder().build());

        assertThat(diagramText).contains("[<AS> Checkout <<ApplicationService>>");
    }

    @Test
    void testAnImplementationNamedLikeItsInterfaceStaysOneNodeWithTheInterface() {
        var diagramText = generate(List.of("fixtures.samename"), GeneralVisualSettings.builder().build());

        assertThat(diagramText)
            .contains("[<OS> PaymentClient <<OutboundService>>")
            .doesNotContain("PaymentClient (");
    }

    @Test
    void testNoHintWhenOnlyOneOfTheClassesSharingANameIsShown() {
        var diagramText = generate(List.of("fixtures.samename.billing"), GeneralVisualSettings.builder().build());

        assertThat(diagramText)
            .contains("[<DS> OrderService <<DomainService>>")
            .doesNotContain("OrderService (");
    }

    @Test
    void testNoHintWhenFullQualifiedClassNamesAreShown() {
        var diagramText = generate(List.of("fixtures.samename"),
            GeneralVisualSettings.builder().withShowFullQualifiedClassNames(true).build());

        assertThat(diagramText)
            .contains("[<DS> fixtures.samename.billing.OrderService <<DomainService>>")
            .contains("[<DS> fixtures.samename.shipping.OrderService <<DomainService>>")
            .doesNotContain("OrderService (");
    }

    private static String generate(List<String> packages, GeneralVisualSettings generalVisualSettings) {
        var trim = DiagramTrimSettings.builder()
            .withExplicitlyIncludedPackageNames(packages)
            .build();
        var config = DomainDiagramConfig.builder()
            .withDiagramTrimSettings(trim)
            .withGeneralVisualSettings(generalVisualSettings)
            .build();
        return new DomainDiagramGenerator(config, Domain.getDomainMirror()).generateDiagramText();
    }
}
