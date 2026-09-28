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

import fixtures.flowcalls.Invoice;
import fixtures.flowcalls.InvoiceId;
import fixtures.flowcalls.ReportController;
import fixtures.flowcalls.ReportHelper;
import fixtures.flowcalls.Summary;
import fixtures.flowcalls.SummaryDriver;
import fixtures.flowcalls.SummaryRule;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Relationships besides the service kinds': a non-domain class holding another one, a class providing a read model
 * no query handler provides, and - in a diagram restricted to flows, if switched on - {@code <<calls>>} relationships
 * between classes calling each other in a flow, where nothing else connects them.
 * <p>
 * The fixture in {@code fixtures.flowcalls}, with hand-built calls:
 * <pre>
 * ReportController.show  --&gt; ReportHelper.render (field)  --&gt; SummaryDriver.compute (field)
 * SummaryRule.check      --&gt; SummaryDriver.compute (field), Summary.total, count, average, max (no field)
 * SummaryDriver.compute  --&gt; Invoice.amount, InvoiceId.value; returns Summary, provided by no query handler
 * SummaryDriver.stats    returns Stats, provided by StatsQueryHandler
 * </pre>
 *
 * @author Mario Herb
 */
public class FlowCallRelationsDiagramTest {

    private static final String PACKAGE = "fixtures.flowcalls";

    /** The calls of the flows are drawn only if switched on. */
    private static final GeneralVisualSettings CALLS_SHOWN =
        GeneralVisualSettings.builder().withShowFlowCallRelations(true).build();

    /** Each rendered class is preceded by a comment naming it in full. */
    private static final Pattern RENDERED_CLASS =
        Pattern.compile("^// !!! (?:\\{Frame} )?(\\S+) !!!$", Pattern.MULTILINE);

    @BeforeEach
    void initializeMirror() {
        Domain.initialize(new ReflectiveDomainMirrorFactory(PACKAGE));
    }

    // ---------------------------------------------------------------------
    // Non-domain classes holding non-domain classes
    // ---------------------------------------------------------------------

    @Test
    void testANonDomainClassHoldingAnotherIsConnectedToIt() {
        var diagramText = generate(List.of(), List.of(), GeneralVisualSettings.builder().build());

        assertThat(relationship(diagramText, "<ND>ReportController", null, "<ND>ReportHelper")).isTrue();
    }

    // ---------------------------------------------------------------------
    // Read models without query handler
    // ---------------------------------------------------------------------

    @Test
    void testAClassReturningAReadModelWithoutQueryHandlerProvidesIt() {
        var diagramText = generate(List.of(), List.of(), GeneralVisualSettings.builder().build());

        assertThat(relationship(diagramText, "<AS>SummaryDriver", "provides SummaryDriver.compute", "<RM>Summary"))
            .isTrue();
    }

    @Test
    void testAReadModelWithQueryHandlerIsProvidedByItOnly() {
        var diagramText = generate(List.of(), List.of(), GeneralVisualSettings.builder().build());

        assertThat(relationship(diagramText, "<QH>StatsQueryHandler", null, "<RM>Stats")).isTrue();
        assertThat(relationship(diagramText, "<AS>SummaryDriver", "provides", "<RM>Stats")).isFalse();
    }

    @Test
    void testTheBackwardFlowToAReadModelWithoutQueryHandlerLeadsThroughItsProvider() {
        var rendered = renderedClasses(generate(List.of(), List.of(Summary.class.getName()),
            GeneralVisualSettings.builder().build()));

        assertThat(rendered).contains(
            Summary.class.getName(), SummaryDriver.class.getName(), SummaryRule.class.getName(),
            ReportHelper.class.getName(), ReportController.class.getName());
    }

    // ---------------------------------------------------------------------
    // Calls of the flows
    // ---------------------------------------------------------------------

    @Test
    void testClassesCallingEachOtherInAFlowAreConnected_When_NothingElseConnectsThem() {
        var diagramText = generate(List.of(), List.of(Summary.class.getName()), CALLS_SHOWN);

        assertThat(relationship(diagramText, "<ND>SummaryRule", "calls Summary.total, Summary.count, Summary.average, …",
            "<RM>Summary")).isTrue();
    }

    @Test
    void testNoCallRelationshipIsDrawn_When_AnotherRelationshipConnectsTheClasses() {
        var diagramText = generate(List.of(), List.of(Summary.class.getName()), CALLS_SHOWN);

        assertThat(relationship(diagramText, "<ND>ReportHelper", "calls", "<AS>SummaryDriver")).isFalse();
        assertThat(relationship(diagramText, "<ND>ReportController", "calls", "<ND>ReportHelper")).isFalse();
    }

    @Test
    void testACallIntoAnAggregateConnectsToItsFrame_And_CallsOfItsPartsAreLeftOut() {
        var diagramText = generate(List.of(SummaryRule.class.getName() + "#check"), List.of(),
            CALLS_SHOWN);

        assertThat(relationship(diagramText, "<AS>SummaryDriver", "calls Invoice.amount", "<AF> Invoice <<Aggregate>>"))
            .isTrue();
        assertThat(diagramText).doesNotContain("InvoiceId.value");
    }

    @Test
    void testNoCallRelationshipIsDrawnByDefault() {
        var diagramText = generate(List.of(), List.of(Summary.class.getName()), GeneralVisualSettings.builder().build());

        assertThat(diagramText).doesNotContain("<<calls>>");
    }

    @Test
    void testNoCallRelationshipIsDrawn_When_NoFlowIsConfigured() {
        var diagramText = generate(List.of(), List.of(), CALLS_SHOWN);

        assertThat(diagramText).doesNotContain("<<calls>>");
    }

    private static DomainCalls calls() {
        return DomainCalls.builder()
            .add(method(ReportController.class, "show"), List.of(
                callSite(ReportHelper.class, "render", ReportController.class)))
            .add(method(ReportHelper.class, "render"), List.of(
                callSite(SummaryDriver.class, "compute", ReportHelper.class)))
            .add(method(SummaryRule.class, "check"), List.of(
                callSite(SummaryDriver.class, "compute", SummaryRule.class),
                callSite(Summary.class, "total", SummaryRule.class),
                callSite(Summary.class, "count", SummaryRule.class),
                callSite(Summary.class, "average", SummaryRule.class),
                callSite(Summary.class, "max", SummaryRule.class)))
            .add(method(SummaryDriver.class, "compute"), List.of(
                callSite(Invoice.class, "amount", SummaryDriver.class),
                callSite(InvoiceId.class, "value", SummaryDriver.class)))
            .build();
    }

    private static DomainCalls.CallSite callSite(Class<?> called, String methodName, Class<?> caller) {
        return new DomainCalls.CallSite(method(called, methodName), caller.getName(), 1);
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
            .withExplicitlyIncludedPackageNames(List.of(PACKAGE))
            .withIncludeFlowsFrom(flowsFrom)
            .withIncludeFlowsTo(flowsTo)
            .build();
        var config = DomainDiagramConfig.builder()
            .withDiagramTrimSettings(trim)
            .withGeneralVisualSettings(generalVisualSettings)
            .build();
        return new DomainDiagramGenerator(config, Domain.getDomainMirror(), calls()).generateDiagramText();
    }

    /**
     * Whether a relationship line starts at the given node and ends at the given one, with the given
     * stereotype and label if given, e.g. {@code provides SummaryDriver.compute}.
     */
    private static boolean relationship(String diagramText, String from, String stereotypeAndLabel, String to) {
        return diagramText.lines()
            .filter(line -> line.startsWith("[" + from + " "))
            .filter(line -> line.contains("[" + to + (to.contains("<<") ? "]" : " ")))
            .anyMatch(line -> stereotypeAndLabel == null || line.replaceAll("<<|>>", "").contains(stereotypeAndLabel));
    }

    private static List<String> renderedClasses(String diagramText) {
        Matcher matcher = RENDERED_CLASS.matcher(diagramText);
        return matcher.results().map(result -> result.group(1)).distinct().toList();
    }
}
