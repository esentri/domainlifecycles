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

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

/**
 * Statically analyzes the method calls made between the types of a mirrored domain.
 * <p>
 * The analysis needs compiled bytecode, so it operates on a classpath rather than on the mirror
 * alone. The mirror defines the boundary: only calls whose target resolves to a mirrored type end
 * up in the result, which keeps the analysis out of JDK and third-party code.
 * <p>
 * The result is a plain {@link DomainCalls} graph without any dependency on the analysis backend,
 * so it can be consumed independently - e.g. by the {@link FlowAnalyzer}. It also carries the
 * {@link Diagnostic}s of the run: a static analysis fails quietly, so an implementation is
 * expected to report what it could not do rather than to return a silently shortened result.
 *
 * @author Mario Herb
 */
public interface StaticAnalyzer {

    /**
     * Same as {@link #analyze(DomainMirror, List, Collection)}, without restricting which classes
     * on the classpath are analyzed.
     *
     * @param domainMirror the mirror defining which types belong to the domain
     * @param classpath    the classpath entries to analyze
     * @return the resolved domain calls
     */
    default DomainCalls analyze(DomainMirror domainMirror, List<Path> classpath) {
        return analyze(domainMirror, classpath, List.of());
    }

    /**
     * Analyzes all methods of all mirrored types and resolves the calls they make into the domain.
     * <p>
     * The classpath must hold the compiled domain classes and everything needed to resolve them.
     * It is not validated: a missing entry does not fail the analysis, it silently shrinks the
     * result. Use {@link DomainClasspath#ofMirroredTypes(DomainMirror)} to derive it from the
     * mirror instead of assembling it by hand.
     *
     * @param domainMirror     the mirror defining which types belong to the domain
     * @param classpath        the classpath entries to analyze
     * @param analyzedPackages restricts which classes on the classpath are considered, to a
     *                         package itself or any of its sub-packages; an empty collection
     *                         means no restriction. This is a scanning-scope optimization, not a
     *                         correctness boundary: a concrete implementation of a mirrored
     *                         domain interface is only found if its package is included here,
     *                         same as if it were simply missing from the classpath, so this
     *                         should cover at least the domain model packages and any package
     *                         holding relevant implementations of domain interfaces
     * @return the resolved domain calls
     */
    DomainCalls analyze(DomainMirror domainMirror, List<Path> classpath, Collection<String> analyzedPackages);

    /**
     * Analyzes a classpath given in the string form used on command lines and by build plugins.
     *
     * @param domainMirror the mirror defining which types belong to the domain
     * @param classpath    the classpath entries, separated by the platform path separator
     * @return the resolved domain calls
     */
    default DomainCalls analyze(DomainMirror domainMirror, String classpath) {
        return analyze(domainMirror, DomainClasspath.parse(classpath));
    }
}
