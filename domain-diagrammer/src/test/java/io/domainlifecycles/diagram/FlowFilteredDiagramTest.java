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
import io.domainlifecycles.mirror.resolver.TypeMetaResolver;
import io.domainlifecycles.staticanalysis.DomainCalls;
import io.domainlifecycles.staticanalysis.DomainMethod;
import io.domainlifecycles.staticanalysis.FlowConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Restricting a diagram to the domain types taking part in a flow.
 * <p>
 * The {@link DomainCalls} used here is built by hand instead of being produced by a real static
 * analysis: the diagrammer only ever reads such a result, so its tests need neither a bytecode
 * analysis framework nor the compiled classes of the analyzed domain.
 * <p>
 * The flow modelled below is the one the shared test domain already contains:
 * <pre>
 * StarteAuslieferung --processed by--&gt; ZustellungsService.liefereAus
 *      --calls--&gt; BestellungRepository.findById / update
 *      --calls--&gt; BestellungBv3.starteLieferung --publishes--&gt; AuslieferungGestartet
 *                                                --listened by--&gt; BenachrichtigungService
 * </pre>
 * Only the call edges have to be supplied, the command and event edges come from the mirror.
 *
 * @author Mario Herb
 */
public class FlowFilteredDiagramTest {

    private static final String COMMAND =
        "tests.shared.complete.onlinehandel.zustellung.StarteAuslieferung";
    private static final String EVENT =
        "tests.shared.complete.onlinehandel.zustellung.AuslieferungGestartet";
    private static final String ZUSTELLUNGS_SERVICE =
        "tests.shared.complete.onlinehandel.zustellung.ZustellungsService";
    private static final String BENACHRICHTIGUNGS_SERVICE =
        "tests.shared.complete.onlinehandel.benachrichtigung.BenachrichtigungService";
    private static final String REPOSITORY =
        "tests.shared.complete.onlinehandel.bestellung.BestellungRepository";
    private static final String AGGREGATE =
        "tests.shared.complete.onlinehandel.bestellung.BestellungBv3";

    /** Each rendered class is preceded by a comment naming it in full. */
    private static final Pattern RENDERED_CLASS =
        Pattern.compile("^// !!! (?:\\{Frame} )?(\\S+) !!!$", Pattern.MULTILINE);

    @BeforeEach
    void initializeMirror() {
        var factory = new ReflectiveDomainMirrorFactory("tests.shared");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        Domain.initialize(factory);
    }

    // ---------------------------------------------------------------------
    // Restriction to a flow
    // ---------------------------------------------------------------------

    @Test
    void testFlowFromADomainCommandKeepsOnlyTheParticipatingTypes() {
        var diagramText = generate(List.of(COMMAND), analyzedCalls());

        assertThat(renderedClasses(diagramText))
            .contains(COMMAND, ZUSTELLUNGS_SERVICE, REPOSITORY, AGGREGATE, EVENT,
                BENACHRICHTIGUNGS_SERVICE);

        // the rest of the domain is gone - the unfiltered diagram holds 124 classes
        assertThat(renderedClasses(diagramText)).hasSizeLessThan(20);
        assertThat(renderedClasses(diagramText))
            .noneMatch(typeName -> typeName.startsWith("tests.shared.validation"))
            .noneMatch(typeName -> typeName.startsWith("tests.shared.inheritance"));
    }

    @Test
    void testFlowFromAMethodStartsBelowTheCommand() {
        var diagramText = generate(List.of(ZUSTELLUNGS_SERVICE + "#liefereAus"), analyzedCalls());

        assertThat(renderedClasses(diagramText))
            .contains(ZUSTELLUNGS_SERVICE, REPOSITORY, AGGREGATE, EVENT, BENACHRICHTIGUNGS_SERVICE);

        // the command triggers the service, it is not reached BY the service
        assertThat(renderedClasses(diagramText)).doesNotContain(COMMAND);
    }

    @Test
    void testFlowFromADomainEventKeepsOnlyTheListeningBranch() {
        var diagramText = generate(List.of(EVENT), analyzedCalls());

        assertThat(renderedClasses(diagramText)).contains(EVENT, BENACHRICHTIGUNGS_SERVICE);

        // everything upstream of the event is not part of the flow starting at it
        assertThat(renderedClasses(diagramText))
            .doesNotContain(COMMAND, ZUSTELLUNGS_SERVICE, REPOSITORY);
    }

    @Test
    void testTypeWithoutMethodNameStartsFromAllOfItsMethods() {
        var fromType = generate(List.of(ZUSTELLUNGS_SERVICE), analyzedCalls());
        var fromMethod = generate(List.of(ZUSTELLUNGS_SERVICE + "#liefereAus"), analyzedCalls());

        // liefereAus is the only method of the service that calls anything
        assertThat(renderedClasses(fromType)).isEqualTo(renderedClasses(fromMethod));
    }

    @Test
    void testSeveralStartingPointsAreUnited() {
        var fromEvent = renderedClasses(generate(List.of(EVENT), analyzedCalls()));
        var fromBoth = renderedClasses(generate(List.of(COMMAND, EVENT), analyzedCalls()));

        assertThat(fromBoth).containsAll(fromEvent);
        assertThat(fromBoth).contains(ZUSTELLUNGS_SERVICE);
    }

    // ---------------------------------------------------------------------
    // Interplay with the settings that already existed
    // ---------------------------------------------------------------------

    @Test
    void testFlowRestrictionIsCombinedWithTheClassesBlacklist() {
        var trim = DiagramTrimSettings.builder()
            .withExplicitlyIncludedPackageNames(List.of("tests.shared"))
            .withIncludeFlowsFrom(List.of(COMMAND))
            .withClassesBlacklist(List.of(REPOSITORY))
            .build();

        var diagramText = generate(trim, analyzedCalls());

        // on the flow, but blacklisted: the restrictions narrow each other, they do not compete
        assertThat(renderedClasses(diagramText)).doesNotContain(REPOSITORY);
        assertThat(renderedClasses(diagramText)).contains(ZUSTELLUNGS_SERVICE);
    }

    @Test
    void testFlowRestrictionCannotWidenAPackageRestriction() {
        var trim = DiagramTrimSettings.builder()
            .withExplicitlyIncludedPackageNames(
                List.of("tests.shared.complete.onlinehandel.zustellung"))
            .withIncludeFlowsFrom(List.of(COMMAND))
            .build();

        var diagramText = generate(trim, analyzedCalls());

        // reached by the flow, but outside the included package
        assertThat(renderedClasses(diagramText)).doesNotContain(REPOSITORY, BENACHRICHTIGUNGS_SERVICE);
        assertThat(renderedClasses(diagramText)).contains(COMMAND, ZUSTELLUNGS_SERVICE, EVENT);
    }

    @Test
    void testMaxDepthOfTheFlowIsConfigurable() {
        var trim = DiagramTrimSettings.builder()
            .withExplicitlyIncludedPackageNames(List.of("tests.shared"))
            .withIncludeFlowsFrom(List.of(COMMAND))
            .build();
        var config = DomainDiagramConfig.builder()
            .withDiagramTrimSettings(trim)
            .withFlowConfig(FlowConfig.defaults().withMaxDepth(1))
            .build();

        var diagramText = new DomainDiagramGenerator(
            config, Domain.getDomainMirror(), analyzedCalls()).generateDiagramText();

        // depth 1 reaches the processing service, not what the service itself calls
        assertThat(renderedClasses(diagramText)).contains(COMMAND, ZUSTELLUNGS_SERVICE);
        assertThat(renderedClasses(diagramText)).doesNotContain(REPOSITORY, BENACHRICHTIGUNGS_SERVICE);
    }

    // ---------------------------------------------------------------------
    // The analysis result stays optional
    // ---------------------------------------------------------------------

    @Test
    void testWithoutAFlowSettingTheAnalysisResultChangesNothing() throws IOException {
        var trim = DiagramTrimSettings.builder()
            .withExplicitlyIncludedPackageNames(List.of("tests.shared"))
            .build();
        var config = DomainDiagramConfig.builder().withDiagramTrimSettings(trim).build();

        var withCalls = new DomainDiagramGenerator(
            config, Domain.getDomainMirror(), analyzedCalls()).generateDiagramText();

        assertThat(withCalls)
            .isEqualTo(Files.readString(Path.of("src/test/resources/tests_resolved.nomnoml")));
    }

    @Test
    void testFlowSettingWithoutAnAnalysisResultIsRejected() {
        assertThatThrownBy(() -> generate(List.of(COMMAND), null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("needs the result of a static analysis");
    }

    @Test
    void testUnresolvableStartingPointIsRejected() {
        assertThatThrownBy(() -> generate(List.of("does.not.Exist"), analyzedCalls()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("unknown to the mirror");

        assertThatThrownBy(
            () -> generate(List.of(ZUSTELLUNGS_SERVICE + "#doesNotExist"), analyzedCalls()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("names no method");
    }

    // ---------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------

    /**
     * The calls {@code ZustellungsService.liefereAus} makes, as a real static analysis would
     * report them.
     */
    private static DomainCalls analyzedCalls() {
        var caller = domainMethod(ZUSTELLUNGS_SERVICE, "liefereAus");
        return DomainCalls.builder()
            .add(caller, List.of(
                new DomainCalls.CallSite(
                    domainMethod(REPOSITORY, "findById"), ZUSTELLUNGS_SERVICE, 42),
                new DomainCalls.CallSite(
                    domainMethod(AGGREGATE, "starteLieferung"), ZUSTELLUNGS_SERVICE, 44),
                new DomainCalls.CallSite(
                    domainMethod(REPOSITORY, "update"), ZUSTELLUNGS_SERVICE, 45)))
            .build();
    }

    private static DomainMethod domainMethod(String typeName, String methodName) {
        var typeMirror = Domain.getDomainMirror().getDomainTypeMirror(typeName).orElseThrow();
        var method = typeMirror.getMethods().stream()
            .filter(m -> m.getName().equals(methodName))
            .findFirst()
            .orElseThrow();
        return new DomainMethod(typeMirror.getTypeName(), method);
    }

    private static String generate(List<String> includeFlowsFrom, DomainCalls domainCalls) {
        return generate(DiagramTrimSettings.builder()
            .withExplicitlyIncludedPackageNames(List.of("tests.shared"))
            .withIncludeFlowsFrom(includeFlowsFrom)
            .build(), domainCalls);
    }

    private static String generate(DiagramTrimSettings trim, DomainCalls domainCalls) {
        var config = DomainDiagramConfig.builder().withDiagramTrimSettings(trim).build();
        return new DomainDiagramGenerator(config, Domain.getDomainMirror(), domainCalls)
            .generateDiagramText();
    }

    private static List<String> renderedClasses(String diagramText) {
        Matcher matcher = RENDERED_CLASS.matcher(diagramText);
        return matcher.results()
            .map(result -> result.group(1))
            .distinct()
            .toList();
    }
}
