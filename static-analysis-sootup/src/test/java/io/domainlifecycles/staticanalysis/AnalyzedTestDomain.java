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

import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;

import java.nio.file.Path;
import java.util.List;

/**
 * Mirrors and analyzes the {@code test.domain} package once for the whole test run. The static
 * analysis needs a compiled classpath and takes a moment, so the result is shared across test
 * classes.
 */
final class AnalyzedTestDomain {

    private static DomainCalls domainCalls;

    private AnalyzedTestDomain() {
    }

    static synchronized DomainCalls domainCalls() {
        if (domainCalls == null) {
            Domain.initialize(new ReflectiveDomainMirrorFactory("test.domain"));
            domainCalls = new SootupStaticAnalyzer()
                .analyze(Domain.getDomainMirror(), classpath());
        }
        return domainCalls;
    }

    static DomainMirror domainMirror() {
        domainCalls();
        return Domain.getDomainMirror();
    }

    /**
     * The whole classpath is derived from the mirror - the domain classes as well as the DLC base
     * types they inherit from all report their own code source.
     */
    static List<Path> classpath() {
        return DomainClasspath.ofMirroredTypes(Domain.getDomainMirror());
    }
}
