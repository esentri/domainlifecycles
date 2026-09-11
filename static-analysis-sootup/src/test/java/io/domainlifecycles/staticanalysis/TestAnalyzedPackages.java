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

import io.domainlifecycles.mirror.api.DomainMirror;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Exercises the {@code analyzedPackages} restriction ({@link SootupStaticAnalyzer#analyze}), which
 * keeps the (comparatively expensive) type hierarchy scan confined to a configurable set of
 * packages instead of touching every class reachable from the classpath.
 */
public class TestAnalyzedPackages {

    private static DomainMirror domainMirror;

    private static DomainCalls unrestricted;

    private static List<Path> classpath;

    @BeforeAll
    public static void before() {
        unrestricted = AnalyzedTestDomain.domainCalls();
        domainMirror = AnalyzedTestDomain.domainMirror();
        classpath = AnalyzedTestDomain.classpath();
    }

    @Test
    public void testRestrictingToTheDomainPackageMatchesUnrestrictedAnalysis() {
        // the mirror also holds the DLC base types the domain extends (e.g. AggregateRootBase),
        // which live outside "test.domain" - analyze() is expected to widen the restriction with
        // the mirror's own packages automatically, so this still finds everything
        var restricted = new SootupStaticAnalyzer().analyze(domainMirror, classpath, List.of("test.domain"));

        assertThat(restricted.callers()).isEqualTo(unrestricted.callers());
        assertThat(restricted.size()).isEqualTo(unrestricted.size());
        assertThat(restricted.diagnostics()).isEqualTo(unrestricted.diagnostics());
    }

    @Test
    public void testAnalyzedPackagesAreWidenedWithTheMirrorsOwnPackagesRegardless() {
        // a deliberately unrelated (and otherwise pointless) restriction still finds everything,
        // because every mirrored type's own package - domain and DLC base types alike - is always
        // included, regardless of what is explicitly configured
        var restricted = new SootupStaticAnalyzer()
            .analyze(domainMirror, classpath, List.of("some.completely.unrelated.package"));

        assertThat(restricted.callers()).isEqualTo(unrestricted.callers());
        assertThat(restricted.size()).isEqualTo(unrestricted.size());
    }

    @Test
    public void testEmptyAnalyzedPackagesMeansNoRestriction() {
        var explicit = new SootupStaticAnalyzer().analyze(domainMirror, classpath, List.of());

        assertThat(explicit.callers()).isEqualTo(unrestricted.callers());
        assertThat(explicit.size()).isEqualTo(unrestricted.size());
    }
}
