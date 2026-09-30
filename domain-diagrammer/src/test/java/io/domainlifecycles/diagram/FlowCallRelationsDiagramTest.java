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

import fixtures.flowcalls.AuditService;
import fixtures.flowcalls.BillingController;
import fixtures.flowcalls.BillingService;
import fixtures.flowcalls.EscalationService;
import fixtures.flowcalls.Invoice;
import fixtures.flowcalls.InvoiceId;
import fixtures.flowcalls.InvoiceLine;
import fixtures.flowcalls.InvoiceRepository;
import fixtures.flowcalls.ReportController;
import fixtures.flowcalls.ReportHelper;
import fixtures.flowcalls.ReportingService;
import fixtures.flowcalls.ServiceLocator;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Relationships besides the service kinds': a non-domain class holding another one, a class providing a read model
 * no query handler provides, and - in a diagram restricted to flows, if switched on - {@code <<calls>>} relationships
 * from service kinds and non-domain classes to the service kinds and non-domain classes they call in a flow, where
 * nothing else connects them, and to the read models and aggregates they call, where no path leads there yet.
 * <p>
 * The fixture in {@code fixtures.flowcalls}, with hand-built calls:
 * <pre>
 * ReportController.show  --&gt; ReportHelper.render (field)  --&gt; SummaryDriver.compute (field)
 * SummaryRule.check      --&gt; SummaryDriver.compute (field), Summary.total, count, average, max (no field)
 * SummaryDriver.compute  --&gt; Invoice.amount, InvoiceId.value; returns Summary, provided by no query handler
 * SummaryDriver.stats    returns Stats, provided by StatsQueryHandler
 * ReportingService.report --&gt; ServiceLocator.driver, SummaryDriver.compute, EscalationService.escalate (no fields)
 * EscalationService.escalate --&gt; ReportingService.report (no field)
 * AuditService.audit     --&gt; Summary.count (no path to Summary)
 * BillingController.bill --&gt; BillingService.bill (field), Invoice.amount
 * BillingService.bill    --&gt; InvoiceRepository.findInvoice (field), Invoice.amount
 * SummaryDriver.compute  also --&gt; InvoiceLine.cancel, an entity of Invoice
 * </pre>
 *
 * @author Mario Herb
 */
public class FlowCallRelationsDiagramTest {

    private static final String PACKAGE = "fixtures.flowcalls";

    private static final String REPORT = "fixtures.flowcalls.ReportingService#report";

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
    void testAnExceptionIsNeverShown() {
        var diagramText = generate(List.of(), List.of(), GeneralVisualSettings.builder().build());

        assertThat(renderedClasses(diagramText)).doesNotContain(PACKAGE + ".BillingException");
        assertThat(renderedClasses(diagramText)).contains(PACKAGE + ".BillingService");
    }

    @Test
    void testAnEventNotifiesANonDomainClassListeningToIt() {
        var diagramText = generate(List.of(), List.of(), GeneralVisualSettings.builder().build());

        assertThat(relationship(diagramText, "<DE>InvoiceIssued", "notifies InvoiceMailer.onInvoiceIssued",
            "<ND>InvoiceMailer")).isTrue();
    }

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
    void testAServiceCallingANonDomainClassIsConnectedToIt_When_NothingElseConnectsThem() {
        var diagramText = generate(List.of(REPORT), List.of(), CALLS_SHOWN);

        assertThat(relationship(diagramText, "<DS>ReportingService", "calls ServiceLocator.driver", "<ND>ServiceLocator"))
            .isTrue();
    }

    @Test
    void testAServiceCallingAnotherServiceIsConnectedToIt_When_NothingElseConnectsThem() {
        var diagramText = generate(List.of(REPORT), List.of(), CALLS_SHOWN);

        assertThat(relationship(diagramText, "<DS>ReportingService", "calls SummaryDriver.compute", "<AS>SummaryDriver"))
            .isTrue();
    }

    @Test
    void testTheCallRelationshipPointsFromTheCallerToTheCalled() {
        var diagramText = generate(List.of(REPORT), List.of(), CALLS_SHOWN);

        assertThat(relationship(diagramText, "<ND>ServiceLocator", "calls", "<DS>ReportingService")).isFalse();
        assertThat(relationship(diagramText, "<AS>SummaryDriver", "calls", "<DS>ReportingService")).isFalse();
    }

    @Test
    void testTwoClassesCallingEachOtherGetARelationshipInEachDirection() {
        var diagramText = generate(List.of(REPORT), List.of(), CALLS_SHOWN);

        assertThat(relationship(diagramText, "<DS>ReportingService", "calls EscalationService.escalate",
            "<DS>EscalationService")).isTrue();
        assertThat(relationship(diagramText, "<DS>EscalationService", "calls ReportingService.report",
            "<DS>ReportingService")).isTrue();
    }

    @Test
    void testACallOfAReadModelIsConnected_When_NoPathLeadsToIt() {
        var diagramText = generate(List.of(AuditService.class.getName() + "#audit"), List.of(), CALLS_SHOWN);

        assertThat(relationship(diagramText, "<DS>AuditService", "calls Summary.count", "<RM>Summary")).isTrue();
    }

    @Test
    void testACallOfAReadModelIsNotConnected_When_APathOverItsProviderLeadsToIt() {
        // the rule holds the driver, which provides the summary it reads
        var diagramText = generate(List.of(SummaryRule.class.getName() + "#check"), List.of(), CALLS_SHOWN);

        assertThat(relationship(diagramText, "<AS>SummaryDriver", "provides", "<RM>Summary")).isTrue();
        assertThat(relationship(diagramText, "<ND>SummaryRule", "calls", "<RM>Summary")).isFalse();
    }

    @Test
    void testCallsOfAnAggregateRootAndItsEntitiesAreConnectedToItsFrame_When_NoRepositoryIsInBetween() {
        var diagramText = generate(List.of(SummaryDriver.class.getName() + "#compute"), List.of(), CALLS_SHOWN);

        assertThat(relationship(diagramText, "<AS>SummaryDriver", "calls Invoice.amount, InvoiceLine.cancel",
            "<AF> Invoice <<Aggregate>>")).isTrue();
        assertThat(diagramText)
            .as("identities are no nodes to connect")
            .doesNotContain("InvoiceId.value");
    }

    @Test
    void testACallOfAnAggregateIsNotConnected_When_ItsRepositoryIsInBetween() {
        var diagramText = generate(List.of(BillingController.class.getName() + "#bill"), List.of(), CALLS_SHOWN);

        assertThat(relationship(diagramText, "<R>InvoiceRepository", null, "<AF> Invoice <<Aggregate>>")).isTrue();
        assertThat(relationship(diagramText, "<DS>BillingService", "calls", "<AF> Invoice <<Aggregate>>"))
            .as("holds the repository").isFalse();
        assertThat(relationship(diagramText, "<ND>BillingController", "calls", "<AF> Invoice <<Aggregate>>"))
            .as("holds the service holding the repository").isFalse();
    }

    @Test
    void testAnAggregateCallingANonDomainClassIsConnectedToIt() {
        var diagramText = generate(List.of(SummaryDriver.class.getName() + "#compute"), List.of(), CALLS_SHOWN);

        assertThat(relationship(diagramText, "<AF> Invoice", "calls ServiceLocator.driver", "<ND>ServiceLocator")).isTrue();
    }

    @Test
    void testCallsOfValueObjectsCommandsAndEventsAreNotConnected() {
        var diagramText = generate(List.of(REPORT, SummaryDriver.class.getName() + "#compute"), List.of(), CALLS_SHOWN);

        assertThat(diagramText.lines().filter(line -> line.contains("<<calls>>")))
            .noneMatch(line -> line.contains("<ID>") || line.contains("<VO>") || line.contains("<DC>")
                || line.contains("<DE>"));
    }

    @Test
    void testNoCallRelationshipIsDrawn_When_AnotherRelationshipConnectsTheClasses() {
        var diagramText = generate(List.of(), List.of(Summary.class.getName()), CALLS_SHOWN);

        assertThat(relationship(diagramText, "<ND>ReportHelper", "calls", "<AS>SummaryDriver")).isFalse();
        assertThat(relationship(diagramText, "<ND>ReportController", "calls", "<ND>ReportHelper")).isFalse();
        assertThat(relationship(diagramText, "<ND>SummaryRule", "calls", "<AS>SummaryDriver")).isFalse();
    }

    @Test
    void testNoCallRelationshipIsDrawnByDefault() {
        var diagramText = generate(List.of(REPORT), List.of(), GeneralVisualSettings.builder().build());

        assertThat(diagramText).doesNotContain("<<calls>>");
    }

    @Test
    void testNoCallRelationshipIsDrawn_When_NoFlowIsConfigured() {
        var diagramText = generate(List.of(), List.of(), CALLS_SHOWN);

        assertThat(diagramText).doesNotContain("<<calls>>");
    }

    @Test
    void testCallRelationsSwitchedOnWithoutAnAnalysisResultAreRejected() {
        var config = DomainDiagramConfig.builder()
            .withDiagramTrimSettings(DiagramTrimSettings.builder()
                .withExplicitlyIncludedPackageNames(List.of(PACKAGE)).build())
            .withGeneralVisualSettings(CALLS_SHOWN)
            .build();

        assertThatThrownBy(() -> new DomainDiagramGenerator(config, Domain.getDomainMirror()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("showFlowCallRelations")
            .hasMessageContaining("needs the result of a static analysis");
    }

    @Test
    void testDefaultSettingsNeedNoAnalysisResult() {
        var config = DomainDiagramConfig.builder()
            .withDiagramTrimSettings(DiagramTrimSettings.builder()
                .withExplicitlyIncludedPackageNames(List.of(PACKAGE)).build())
            .build();

        assertThat(new DomainDiagramGenerator(config, Domain.getDomainMirror()).generateDiagramText())
            .contains("ReportingService")
            .doesNotContain("<<calls>>");
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
            .add(method(ReportingService.class, "report"), List.of(
                callSite(ServiceLocator.class, "driver", ReportingService.class),
                callSite(SummaryDriver.class, "compute", ReportingService.class),
                callSite(EscalationService.class, "escalate", ReportingService.class)))
            .add(method(EscalationService.class, "escalate"), List.of(
                callSite(ReportingService.class, "report", EscalationService.class)))
            .add(method(SummaryDriver.class, "compute"), List.of(
                callSite(Invoice.class, "amount", SummaryDriver.class),
                callSite(InvoiceLine.class, "cancel", SummaryDriver.class),
                callSite(InvoiceId.class, "value", SummaryDriver.class)))
            .add(method(Invoice.class, "amount"), List.of(
                callSite(ServiceLocator.class, "driver", Invoice.class)))
            .add(method(AuditService.class, "audit"), List.of(
                callSite(Summary.class, "count", AuditService.class)))
            .add(method(BillingController.class, "bill"), List.of(
                callSite(BillingService.class, "bill", BillingController.class),
                callSite(Invoice.class, "amount", BillingController.class)))
            .add(method(BillingService.class, "bill"), List.of(
                callSite(InvoiceRepository.class, "findInvoice", BillingService.class),
                callSite(Invoice.class, "amount", BillingService.class)))
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
