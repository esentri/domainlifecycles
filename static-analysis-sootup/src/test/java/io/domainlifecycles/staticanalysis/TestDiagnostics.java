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

package io.domainlifecycles.staticanalysis;

import io.domainlifecycles.domain.types.base.AggregateRootBase;
import io.domainlifecycles.mirror.api.DomainMirror;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.domain.MyAggregateRoot;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TestDiagnostics {

    private static DomainMirror domainMirror;

    private static DomainCalls calls;

    @BeforeAll
    public static void before() {
        calls = AnalyzedTestDomain.domainCalls();
        domainMirror = AnalyzedTestDomain.domainMirror();
    }

    @Test
    public void testCleanRunReportsNothingAndIsComplete() {
        // the classpath is derived from the mirror, so nothing should be missing
        assertThat(calls.diagnostics()).isEmpty();
        assertThat(calls.isComplete()).isTrue();
    }

    @Test
    public void testIncompleteClasspathIsReportedAsWarning() {
        // only the test domain output directory - the DLC base types the domain inherits from
        // are missing, which silently shrinks the result
        var partialClasspath = DomainClasspath.ofTypes(MyAggregateRoot.class);
        var partial = new SootupStaticAnalyzer().analyze(domainMirror, partialClasspath);

        assertThat(partial.isComplete()).isFalse();

        var warnings = partial.diagnostics(Diagnostic.Severity.WARNING);
        assertThat(warnings).isNotEmpty();
        assertThat(warnings).allMatch(
            diagnostic -> diagnostic.kind() == Diagnostic.Kind.TYPE_NOT_ON_CLASSPATH);
        assertThat(warnings).extracting(Diagnostic::subject)
            .contains(AggregateRootBase.class.getTypeName());

        // and the diagnostic is not decorative: the result really is smaller
        assertThat(partial.size()).isLessThan(calls.size());
    }

    @Test
    public void testCompilerGeneratedMethodsAreNotReported() {
        // lambda bodies and constructors have no counterpart in the mirror by design. Reporting
        // them would drown the real gaps - and for a lambda it would be wrong, its calls are
        // attributed to the method that defines it.
        var partialClasspath = DomainClasspath.ofTypes(MyAggregateRoot.class);
        var partial = new SootupStaticAnalyzer().analyze(domainMirror, partialClasspath);

        List<Diagnostic> allDiagnostics = partial.diagnostics();
        assertThat(allDiagnostics).noneMatch(
            diagnostic -> diagnostic.subject().contains("lambda$"));
        assertThat(allDiagnostics).noneMatch(
            diagnostic -> diagnostic.subject().contains("<init>"));
        assertThat(allDiagnostics).noneMatch(
            diagnostic -> diagnostic.subject().contains("<clinit>"));
    }

    @Test
    public void testSeverityFilterPartitionsTheDiagnostics() {
        var partialClasspath = DomainClasspath.ofTypes(MyAggregateRoot.class);
        var partial = new SootupStaticAnalyzer().analyze(domainMirror, partialClasspath);

        var warnings = partial.diagnostics(Diagnostic.Severity.WARNING);
        var infos = partial.diagnostics(Diagnostic.Severity.INFO);

        assertThat(warnings.size() + infos.size()).isEqualTo(partial.diagnostics().size());
        assertThat(warnings).allMatch(d -> d.severity() == Diagnostic.Severity.WARNING);
        assertThat(infos).allMatch(d -> d.severity() == Diagnostic.Severity.INFO);
    }

    @Test
    public void testDiagnosticsAreDeduplicated() {
        var duplicate = Diagnostic.typeNotOnClasspath("some.Type");

        var result = DomainCalls.builder()
            .add(duplicate)
            .add(Diagnostic.typeNotOnClasspath("some.Type"))
            .addAll(List.of(duplicate, Diagnostic.typeNotOnClasspath("some.other.Type")))
            .build();

        assertThat(result.diagnostics()).hasSize(2);
        assertThat(result.diagnostics()).startsWith(duplicate);
        assertThat(result.isComplete()).isFalse();
    }

    @Test
    public void testSeverityIsDerivedFromTheKind() {
        assertThat(Diagnostic.typeNotOnClasspath("x").severity())
            .isEqualTo(Diagnostic.Severity.WARNING);
        assertThat(Diagnostic.bodyNotLoadable("x", "boom").severity())
            .isEqualTo(Diagnostic.Severity.WARNING);
        assertThat(Diagnostic.methodNotOnClasspath("x").severity())
            .isEqualTo(Diagnostic.Severity.INFO);
        assertThat(Diagnostic.targetNotInMirror("x", "y").severity())
            .isEqualTo(Diagnostic.Severity.INFO);
        assertThat(Diagnostic.callerNotInMirror("x", "y").severity())
            .isEqualTo(Diagnostic.Severity.INFO);
    }

    @Test
    public void testOnlyWarningsAffectCompleteness() {
        var infoOnly = DomainCalls.builder()
            .add(Diagnostic.targetNotInMirror("x", "y"))
            .build();
        assertThat(infoOnly.isComplete()).isTrue();

        var withWarning = DomainCalls.builder()
            .add(Diagnostic.targetNotInMirror("x", "y"))
            .add(Diagnostic.bodyNotLoadable("x", "boom"))
            .build();
        assertThat(withWarning.isComplete()).isFalse();
    }

    @Test
    public void testDiagnosticRendersSeverityKindAndSubject() {
        assertThat(Diagnostic.typeNotOnClasspath("some.Type").toString())
            .startsWith("WARNING TYPE_NOT_ON_CLASSPATH some.Type - ");
    }
}
