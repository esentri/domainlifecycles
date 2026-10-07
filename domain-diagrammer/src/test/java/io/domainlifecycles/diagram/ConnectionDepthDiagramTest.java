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
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * The structural filters "what leads to" ({@code includeConnectedToIngoing}) and "what does it lead to"
 * ({@code includeConnectedToOutgoing}) can be limited in depth; {@code 0} or a negative depth follows the complete
 * path.
 * <p>
 * The fixture in {@code fixtures.connectiondepth} is a chain, one step each:
 * <pre>
 * PlaceOrder -&gt; OrderApplicationService -&gt; OrderDomainService (interface, OrderDomainServiceImpl)
 *   -&gt; OrderRepository (interface, OrderRepositoryImpl) -&gt; Order -&gt; OrderPlaced -&gt; NotificationService
 * </pre>
 * and the {@code UnrelatedService}, connected to nothing.
 *
 * @author Mario Herb
 */
class ConnectionDepthDiagramTest {

    private static final String PACKAGE = "fixtures.connectiondepth";

    /** the declaration of a class box, e.g. {@code [<DS> NotificationService <<DomainService>> |} */
    private static final Pattern CLASS_BOX = Pattern.compile("^\\[<(?!AF)\\w+> (\\S+) <<");

    @BeforeEach
    void initializeMirror() {
        Domain.initialize(new ReflectiveDomainMirrorFactory(PACKAGE));
    }

    /** the chain in the order "what does it lead to" follows it */
    private static final List<String> CHAIN = List.of("PlaceOrder", "OrderApplicationService", "OrderDomainService",
        "OrderRepository", "Order", "OrderPlaced", "NotificationService");

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6})
    void testWhatItLeadsToIsFollowedUpToTheDepth(int depth) {
        var shown = shownClasses(DiagramTrimSettings.builder()
            .withIncludeConnectedToOutgoing(List.of(PACKAGE + ".PlaceOrder"))
            .withIncludeConnectedToOutgoingDepth(depth));

        assertThat(shown).containsExactlyInAnyOrderElementsOf(CHAIN.subList(0, depth + 1));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6})
    void testWhatLeadsToItIsFollowedUpToTheDepth(int depth) {
        var shown = shownClasses(DiagramTrimSettings.builder()
            .withIncludeConnectedToIngoing(List.of(PACKAGE + ".NotificationService"))
            .withIncludeConnectedToIngoingDepth(depth));

        assertThat(shown).containsExactlyInAnyOrderElementsOf(CHAIN.subList(CHAIN.size() - 1 - depth, CHAIN.size()));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void testTheCompletePathIsFollowed_When_TheDepthIsZeroOrNegative(int depth) {
        var outgoing = shownClasses(DiagramTrimSettings.builder()
            .withIncludeConnectedToOutgoing(List.of(PACKAGE + ".PlaceOrder"))
            .withIncludeConnectedToOutgoingDepth(depth));
        var ingoing = shownClasses(DiagramTrimSettings.builder()
            .withIncludeConnectedToIngoing(List.of(PACKAGE + ".NotificationService"))
            .withIncludeConnectedToIngoingDepth(depth));

        assertThat(outgoing).containsExactlyInAnyOrderElementsOf(CHAIN).doesNotContain("UnrelatedService");
        assertThat(ingoing).containsExactlyInAnyOrderElementsOf(CHAIN);
    }

    @Test
    void testTheCompletePathIsFollowedByDefault() {
        var withoutDepth = shownClasses(DiagramTrimSettings.builder()
            .withIncludeConnectedToOutgoing(List.of(PACKAGE + ".PlaceOrder")));

        assertThat(withoutDepth).containsExactlyInAnyOrderElementsOf(CHAIN);
        assertThat(DiagramTrimSettings.builder().build().getIncludeConnectedToOutgoingDepth()).isZero();
        assertThat(DiagramTrimSettings.builder().build().getIncludeConnectedToIngoingDepth()).isZero();
    }

    @Test
    void testAnInterfaceAndItsImplementationTakeOneStep() {
        // OrderDomainService is referenced as interface, its implementation references the repository
        var shown = shownClasses(DiagramTrimSettings.builder()
            .withIncludeConnectedToOutgoing(List.of(PACKAGE + ".OrderApplicationService"))
            .withIncludeConnectedToOutgoingDepth(2));

        assertThat(shown).containsExactlyInAnyOrder("OrderApplicationService", "OrderDomainService", "OrderRepository");
    }

    @Test
    void testTheDepthsOfBothDirectionsApplyIndependently() {
        var shown = shownClasses(DiagramTrimSettings.builder()
            .withIncludeConnectedToIngoing(List.of(PACKAGE + ".OrderRepository"))
            .withIncludeConnectedToIngoingDepth(1)
            .withIncludeConnectedToOutgoing(List.of(PACKAGE + ".Order"))
            .withIncludeConnectedToOutgoingDepth(0));

        assertThat(shown).containsExactlyInAnyOrder(
            "OrderDomainService", "OrderRepository", "Order", "OrderPlaced", "NotificationService");
    }

    @Test
    void testWhatLeadsToAClassAndWhatItLeadsToCanBeShownTogether_WithTheirOwnDepths() {
        var oneStepEach = shownClasses(DiagramTrimSettings.builder()
            .withIncludeConnectedToIngoing(List.of(PACKAGE + ".OrderRepository"))
            .withIncludeConnectedToOutgoing(List.of(PACKAGE + ".OrderRepository"))
            .withIncludeConnectedToIngoingDepth(1)
            .withIncludeConnectedToOutgoingDepth(1));
        var twoStepsBackOneForward = shownClasses(DiagramTrimSettings.builder()
            .withIncludeConnectedToIngoing(List.of(PACKAGE + ".OrderRepository"))
            .withIncludeConnectedToOutgoing(List.of(PACKAGE + ".OrderRepository"))
            .withIncludeConnectedToIngoingDepth(2)
            .withIncludeConnectedToOutgoingDepth(1));

        assertThat(oneStepEach).containsExactlyInAnyOrder("OrderDomainService", "OrderRepository", "Order");
        assertThat(twoStepsBackOneForward)
            .containsExactlyInAnyOrder("OrderApplicationService", "OrderDomainService", "OrderRepository", "Order");
    }

    @Test
    void testWhatLeadsToAClassAndWhatItLeadsToCanBeLeftOutTogether() {
        var shown = shownClasses(DiagramTrimSettings.builder()
            .withExcludeConnectedToIngoing(List.of(PACKAGE + ".Order"))
            .withExcludeConnectedToOutgoing(List.of(PACKAGE + ".Order")));

        assertThat(shown).containsExactly("UnrelatedService");
    }

    @Test
    void testAClassCannotBeIncludedAndExcludedAtOnce() {
        var trimSettings = DiagramTrimSettings.builder()
            .withIncludeConnectedToOutgoing(List.of(PACKAGE + ".Order"))
            .withExcludeConnectedToIngoing(List.of(PACKAGE + ".Order"));

        assertThatThrownBy(trimSettings::build)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining(PACKAGE + ".Order")
            .hasMessageContaining("includeConnectedToIngoing/includeConnectedToOutgoing")
            .hasMessageContaining("excludeConnectedToIngoing/excludeConnectedToOutgoing");
    }

    @Test
    void testAClassCannotBeFollowedInBothDirectionsCompletelyAndInOneOfThem() {
        var trimSettings = DiagramTrimSettings.builder()
            .withIncludeConnectedTo(List.of(PACKAGE + ".Order"))
            .withIncludeConnectedToIngoing(List.of(PACKAGE + ".Order"));

        assertThatThrownBy(trimSettings::build)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("includeConnectedTo and includeConnectedToIngoing/includeConnectedToOutgoing");
    }

    private static Set<String> shownClasses(DiagramTrimSettings.DiagramTrimSettingsBuilder trimSettings) {
        var config = DomainDiagramConfig.builder()
            .withDiagramTrimSettings(trimSettings.withExplicitlyIncludedPackageNames(List.of(PACKAGE)).build())
            .build();
        var diagramText = new DomainDiagramGenerator(config, Domain.getDomainMirror()).generateDiagramText();
        return diagramText.lines()
            .map(CLASS_BOX::matcher)
            .filter(Matcher::find)
            .map(matcher -> matcher.group(1))
            .collect(Collectors.toCollection(TreeSet::new));
    }
}
