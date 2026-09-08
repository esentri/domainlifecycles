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
 *  Copyright 2019-2025 the original author or authors.
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
import test.domain.MyApplicationService;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class TestDomainClasspath {

    private static DomainMirror domainMirror;

    @BeforeAll
    public static void before() {
        domainMirror = AnalyzedTestDomain.domainMirror();
    }

    @Test
    public void testMirroredTypesCoverDomainAndDlcBaseTypes() {
        var classpath = DomainClasspath.ofMirroredTypes(domainMirror);

        assertThat(classpath).isNotEmpty();
        assertThat(classpath).allMatch(Files::exists);
        assertThat(classpath).doesNotHaveDuplicates();

        // the domain classes themselves ...
        assertThat(classpath).containsAll(DomainClasspath.ofTypes(MyAggregateRoot.class));
        // ... and the DLC base types the domain inherits from, which the mirror also holds
        assertThat(classpath).containsAll(DomainClasspath.ofTypes(AggregateRootBase.class));
    }

    @Test
    public void testMirroredTypesSkipWhatCannotBeResolved() {
        // resolving against a class loader that knows nothing yields no entries instead of failing
        var classpath = DomainClasspath.ofMirroredTypes(domainMirror,
            new ClassLoader(null) {
                @Override
                protected Class<?> findClass(String name) throws ClassNotFoundException {
                    throw new ClassNotFoundException(name);
                }
            });

        assertThat(classpath).isEmpty();
    }

    @Test
    public void testOfTypesDeduplicates() {
        // both live in the same output directory
        var classpath = DomainClasspath.ofTypes(MyAggregateRoot.class, MyApplicationService.class);

        assertThat(classpath).hasSize(1);
        assertThat(classpath).doesNotHaveDuplicates();
    }

    @Test
    public void testParseSkipsBlankAndMissingEntries() {
        var existing = DomainClasspath.ofTypes(MyAggregateRoot.class).get(0);

        var parsed = DomainClasspath.parse(String.join(File.pathSeparator,
            existing.toString(),
            "",
            "/this/path/does/not/exist",
            existing.toString()));

        assertThat(parsed).containsExactly(existing);
    }

    @Test
    public void testMergeKeepsFirstOccurrenceOrder() {
        var domain = DomainClasspath.ofTypes(MyAggregateRoot.class);
        var base = DomainClasspath.ofTypes(AggregateRootBase.class);

        var merged = DomainClasspath.merge(domain, base, domain);

        assertThat(merged).doesNotHaveDuplicates();
        assertThat(merged).startsWith(domain.get(0));
        assertThat(merged).containsAll(base);
    }

    @Test
    public void testStringOverloadIsEquivalentToPathList() {
        List<Path> classpath = DomainClasspath.ofMirroredTypes(domainMirror);
        String asString = classpath.stream()
            .map(Path::toString)
            .collect(Collectors.joining(File.pathSeparator));

        var fromPaths = new SootupStaticAnalyzer().analyze(domainMirror, classpath);
        var fromString = new SootupStaticAnalyzer().analyze(domainMirror, asString);

        assertThat(fromString.callers()).isEqualTo(fromPaths.callers());
        assertThat(fromString.size()).isEqualTo(fromPaths.size());
        assertThat(fromString.callers()).allSatisfy(caller ->
            assertThat(fromString.callsFor(caller)).isEqualTo(fromPaths.callsFor(caller)));
    }
}
