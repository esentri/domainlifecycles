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

package io.domainlifecycles.plugins.staticanalysis;

import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.staticanalysis.DomainCalls;

import java.net.URL;
import java.util.List;

/**
 * Interface for running a static (bytecode) analysis of the compiled domain classes, determining
 * which domain methods call which other domain methods.
 *
 * @author Mario Herb
 */
public interface DomainCallsAnalyzer {

    /**
     * Runs a static analysis of the classes on {@code classPathFiles}, resolving calls against the
     * given, already initialized {@code domainMirror}.
     *
     * @param classPathFiles the classpath the analyzed domain classes are loaded from
     * @param domainMirror   the domain mirror to resolve the analyzed calls against
     * @return the analysis result
     */
    DomainCalls analyze(List<URL> classPathFiles, DomainMirror domainMirror);
}
