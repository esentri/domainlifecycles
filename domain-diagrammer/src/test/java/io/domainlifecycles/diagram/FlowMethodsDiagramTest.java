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

import fixtures.flowmethods.Order;
import fixtures.flowmethods.OrderApplicationService;
import fixtures.flowmethods.OrderOverview;
import fixtures.flowmethods.OrderOverviewQueryHandler;
import fixtures.flowmethods.OrderRepository;
import fixtures.flowmethods.PricingService;
import io.domainlifecycles.diagram.domain.DomainDiagramGenerator;
import io.domainlifecycles.diagram.domain.config.DiagramTrimSettings;
import io.domainlifecycles.diagram.domain.config.DomainDiagramConfig;
import io.domainlifecycles.diagram.domain.config.GeneralVisualSettings;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.staticanalysis.DomainCalls;
import io.domainlifecycles.staticanalysis.DomainMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * {@code GeneralVisualSettings.showOnlyFlowMethods} (default {@code true}): in a diagram restricted to flows, the classes
 * taking part in a flow show only the methods called in it. Classes shown for another reason - an entity of a shown
 * aggregate, a read model contained in a shown read model - show their methods as without flow.
 * <p>
 * The fixture in {@code fixtures.flowmethods}, with hand-built calls:
 * <pre>
 * OrderApplicationService.placeOrder  --&gt; PricingService.price, Order.confirm, OrderRepository.store
 * OrderApplicationService.cancelOrder --&gt; OrderRepository.findOrder, Order.cancel
 * OrderApplicationService.report      --&gt; OrderOverviewQueryHandler.find, OrderOverview.total
 * </pre>
 * Not called at all: {@code PricingService.discount}, {@code OrderRepository.remove}, {@code OrderOverview.count}, the
 * methods of the entity {@code OrderLine} and of the read model {@code LineView} contained in {@code OrderOverview}.
 *
 * @author Mario Herb
 */
public class FlowMethodsDiagramTest {

    private static final String PLACE_ORDER = OrderApplicationService.class.getName() + "#placeOrder";

    @BeforeEach
    void initializeMirror() {
        Domain.initialize(new ReflectiveDomainMirrorFactory("fixtures.flowmethods"));
    }

    @Test
    void testClassesOfAForwardFlowShowOnlyTheMethodsCalledInIt() {
        var diagramText = generate(List.of(PLACE_ORDER), List.of(), GeneralVisualSettings.builder().build());

        assertThat(box(diagramText, "<AS> OrderApplicationService"))
            .contains("placeOrder(OrderId)").doesNotContain("cancelOrder", "report");
        assertThat(box(diagramText, "<DS> PricingService")).contains("price()").doesNotContain("discount");
        assertThat(box(diagramText, "<AR> Order ")).contains("confirm()").doesNotContain("cancel()");
        assertThat(box(diagramText, "<R> OrderRepository"))
            .as("called through the interface, shown on the implementation drawn as the interface")
            .contains("store(Order)").doesNotContain("findOrder", "remove");
    }

    @Test
    void testAPartOfAShownAggregateReachedByNoFlowShowsAllItsMethods() {
        var diagramText = generate(List.of(PLACE_ORDER), List.of(), GeneralVisualSettings.builder().build());

        assertThat(box(diagramText, "<E> OrderLine")).contains("increase()", "decrease()");
    }

    @Test
    void testAllMethodsAreShown_When_TheSettingIsSwitchedOff() {
        var diagramText = generate(List.of(PLACE_ORDER), List.of(),
            GeneralVisualSettings.builder().withShowOnlyFlowMethods(false).build());

        assertThat(box(diagramText, "<AS> OrderApplicationService")).contains("placeOrder", "cancelOrder", "report");
        assertThat(box(diagramText, "<DS> PricingService")).contains("price()", "discount()");
    }

    @Test
    void testAllMethodsAreShown_When_NoFlowIsConfigured() {
        var diagramText = generate(List.of(), List.of(), GeneralVisualSettings.builder().build());

        assertThat(box(diagramText, "<AS> OrderApplicationService")).contains("placeOrder", "cancelOrder", "report");
        assertThat(box(diagramText, "<R> OrderRepository")).contains("findOrder", "store", "remove");
    }

    @Test
    void testTheMethodsOfForwardAndBackwardFlowsAreShownTogether() {
        // the backward flow to discount has no callers: it reaches discount itself
        var diagramText = generate(List.of(PLACE_ORDER), List.of(PricingService.class.getName() + "#discount"),
            GeneralVisualSettings.builder().build());

        assertThat(box(diagramText, "<DS> PricingService")).contains("price()", "discount()");
        assertThat(box(diagramText, "<AS> OrderApplicationService"))
            .contains("placeOrder").doesNotContain("cancelOrder", "report");
    }

    @Test
    void testABackwardFlowIntoAWholeTypeShowsTheMethodsOfItCalledByItsCallers() {
        var diagramText = generate(List.of(), List.of(OrderRepository.class.getName()),
            GeneralVisualSettings.builder().build());

        assertThat(box(diagramText, "<R> OrderRepository"))
            .contains("findOrder(OrderId)", "store(Order)").doesNotContain("remove");
        assertThat(box(diagramText, "<AS> OrderApplicationService"))
            .contains("placeOrder", "cancelOrder").doesNotContain("report");
    }

    @Test
    void testAReadModelContainedInAShownOneAndReachedByNoFlowShowsAllItsMethods() {
        var diagramText = generate(List.of(), List.of(OrderOverview.class.getName()),
            GeneralVisualSettings.builder().withShowReadModelMethods(true).withShowQueryHandlerMethods(true).build());

        assertThat(box(diagramText, "<RM> OrderOverview")).contains("total()").doesNotContain("count()");
        assertThat(box(diagramText, "<QH> OrderOverviewQueryHandler")).contains("find(OrderId)");
        assertThat(box(diagramText, "<RM> LineView")).contains("label()", "details()");
    }

    private static DomainCalls calls() {
        var placeOrder = method(OrderApplicationService.class, "placeOrder");
        var cancelOrder = method(OrderApplicationService.class, "cancelOrder");
        var report = method(OrderApplicationService.class, "report");
        String service = OrderApplicationService.class.getName();
        return DomainCalls.builder()
            .add(placeOrder, List.of(
                new DomainCalls.CallSite(method(PricingService.class, "price"), service, 10),
                new DomainCalls.CallSite(method(Order.class, "confirm"), service, 11),
                new DomainCalls.CallSite(method(OrderRepository.class, "store"), service, 12)))
            .add(cancelOrder, List.of(
                new DomainCalls.CallSite(method(OrderRepository.class, "findOrder"), service, 20),
                new DomainCalls.CallSite(method(Order.class, "cancel"), service, 21)))
            .add(report, List.of(
                new DomainCalls.CallSite(method(OrderOverviewQueryHandler.class, "find"), service, 30),
                new DomainCalls.CallSite(method(OrderOverview.class, "total"), service, 31)))
            .build();
    }

    private static DomainMethod method(Class<?> ownerClass, String methodName) {
        var typeMirror = Domain.getDomainMirror().getDomainTypeMirror(ownerClass.getName()).orElseThrow();
        var method = typeMirror.getMethods().stream()
            .filter(m -> m.getName().equals(methodName))
            .findFirst()
            .orElseThrow();
        return new DomainMethod(typeMirror.getTypeName(), method);
    }

    private static String generate(List<String> flowsFrom, List<String> flowsTo,
                                   GeneralVisualSettings generalVisualSettings) {
        var trim = DiagramTrimSettings.builder()
            .withExplicitlyIncludedPackageNames(List.of("fixtures.flowmethods"))
            .withIncludeFlowsFrom(flowsFrom)
            .withIncludeFlowsTo(flowsTo)
            .build();
        var config = DomainDiagramConfig.builder()
            .withDiagramTrimSettings(trim)
            .withGeneralVisualSettings(generalVisualSettings)
            .build();
        return new DomainDiagramGenerator(config, Domain.getDomainMirror(), calls()).generateDiagramText();
    }

    /** The box of a class, starting with its style classifier and name, e.g. {@code <DS> PricingService}. */
    private static String box(String diagramText, String classifierAndName) {
        int start = diagramText.indexOf("[" + classifierAndName);
        assertThat(start).as("box of " + classifierAndName).isNotNegative();
        return diagramText.substring(start, diagramText.indexOf(']', start) + 1);
    }
}
