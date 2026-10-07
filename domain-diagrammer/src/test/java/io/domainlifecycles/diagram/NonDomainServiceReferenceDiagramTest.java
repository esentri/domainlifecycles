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

import fixtures.nondomain.NonDomainHelperClient;
import fixtures.nondomain.SomeOutboundService;
import fixtures.nondomain.UnusedHelper;
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
 * A non-domain class (implementing no domain marker interface) that is injected into a service
 * kind - here an {@link fixtures.nondomain.SomeOutboundService} - and called from within one of
 * its methods.
 * <p>
 * Two independent things are verified: that the class diagram draws the injected class as a node
 * connected to the service (structural, derived from the mirror's field typing alone), and that
 * the same relationship is also traceable through the call-flow analysis (behavioural, derived
 * from a {@link DomainCalls} result) - and that a flow restriction can narrow the structurally
 * included class back out again if the analysis does not actually report the call.
 *
 * @author Mario Herb
 */
public class NonDomainServiceReferenceDiagramTest {

    private static final String SERVICE = SomeOutboundService.class.getName();
    private static final String HELPER = NonDomainHelperClient.class.getName();
    private static final String UNUSED = UnusedHelper.class.getName();

    /** Each rendered class is preceded by a comment naming it in full. */
    private static final Pattern RENDERED_CLASS =
        Pattern.compile("^// !!! (?:\\{Frame} )?(\\S+) !!!$", Pattern.MULTILINE);

    @BeforeEach
    void initializeMirror() {
        Domain.initialize(new ReflectiveDomainMirrorFactory("fixtures.nondomain"));
    }

    // ---------------------------------------------------------------------
    // Structural inclusion (mirror field typing alone, no flow analysis)
    // ---------------------------------------------------------------------

    @Test
    void testInjectedNonDomainClassIsShownAndConnectedToTheService() {
        var diagramText = generate(GeneralVisualSettings.builder().build());

        assertThat(renderedClasses(diagramText)).contains(SERVICE, HELPER);
        assertThat(hasDirectedAssociation(diagramText,
            SomeOutboundService.class.getSimpleName(), NonDomainHelperClient.class.getSimpleName()))
            .as("the service references the helper via a field, so an edge is drawn to it")
            .isTrue();

        assertThat(renderedClasses(diagramText))
            .as("a class referenced by nothing and referencing nothing itself never appears")
            .doesNotContain(UNUSED);
    }

    @Test
    void testNonDomainClassesCanBeHiddenViaSetting() {
        var diagramText = generate(
            GeneralVisualSettings.builder().withShowNonDomainClasses(false).build());

        assertThat(renderedClasses(diagramText)).contains(SERVICE);
        assertThat(renderedClasses(diagramText)).doesNotContain(HELPER);
    }

    // ---------------------------------------------------------------------
    // The same relationship, traced through the call-flow analysis
    // ---------------------------------------------------------------------

    @Test
    void testFlowFromTheServiceReachesTheInjectedNonDomainClass() {
        var diagramText = generate(List.of(SERVICE + "#execute"), callsReachingTheHelper());

        assertThat(renderedClasses(diagramText))
            .as("the flow analysis reports the call into the injected helper, so it survives "
                + "the flow restriction, on top of already being structurally included")
            .contains(SERVICE, HELPER);
    }

    @Test
    void testFlowRestrictionNarrowsOutAStructurallyReferencedClassTheAnalysisDoesNotReportACallTo() {
        var diagramText = generate(List.of(SERVICE + "#execute"), DomainCalls.builder().build());

        assertThat(renderedClasses(diagramText))
            .as("structurally referenced via the field, but the (empty) analysis result never "
                + "reports a call into it, so the flow restriction - a pure narrowing filter - "
                + "leaves it out")
            .contains(SERVICE)
            .doesNotContain(HELPER);
    }

    // ---------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------

    private static DomainCalls callsReachingTheHelper() {
        var caller = domainMethod(SERVICE, "execute");
        var called = domainMethod(HELPER, "doSomething");
        return DomainCalls.builder()
            .add(caller, List.of(new DomainCalls.CallSite(called, SERVICE, 17)))
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

    private static String generate(GeneralVisualSettings generalVisualSettings) {
        var trim = DiagramTrimSettings.builder()
            .withExplicitlyIncludedPackageNames(List.of("fixtures.nondomain"))
            .build();
        var config = DomainDiagramConfig.builder()
            .withDiagramTrimSettings(trim)
            .withGeneralVisualSettings(generalVisualSettings)
            .build();
        return new DomainDiagramGenerator(config, Domain.getDomainMirror()).generateDiagramText();
    }

    private static String generate(List<String> includeFlowsFrom, DomainCalls domainCalls) {
        var trim = DiagramTrimSettings.builder()
            .withExplicitlyIncludedPackageNames(List.of("fixtures.nondomain"))
            .withIncludeFlowsFrom(includeFlowsFrom)
            .build();
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

    private static boolean hasDirectedAssociation(String diagramText, String fromSimpleName, String toSimpleName) {
        return diagramText.lines()
            .filter(line -> line.contains("->"))
            .anyMatch(line -> {
                var parts = line.split("->", 2);
                return parts.length == 2
                    && parts[0].contains(fromSimpleName)
                    && parts[1].contains(toSimpleName);
            });
    }
}
