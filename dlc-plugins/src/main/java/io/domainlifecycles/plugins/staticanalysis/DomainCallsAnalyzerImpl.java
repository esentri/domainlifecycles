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
import io.domainlifecycles.plugins.exception.DLCPluginsException;
import io.domainlifecycles.staticanalysis.DomainCalls;
import io.domainlifecycles.staticanalysis.SootupStaticAnalyzer;
import io.domainlifecycles.staticanalysis.StaticAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SootUp based implementation of {@link DomainCallsAnalyzer}.
 *
 * @author Mario Herb
 */
public class DomainCallsAnalyzerImpl implements DomainCallsAnalyzer {

    private final static Logger log = LoggerFactory.getLogger(DomainCallsAnalyzerImpl.class);

    /**
     * Default size of the bounded class cache the underlying {@link SootupStaticAnalyzer} is created
     * with, used when no explicit size is given via {@link #DomainCallsAnalyzerImpl(int)}. Re-exposed
     * here (rather than referencing {@link SootupStaticAnalyzer#DEFAULT_CACHE_SIZE} directly) so
     * Gradle/Maven plugin code, which does not depend on {@code static-analysis-sootup} itself, has a
     * documented default to fall back to.
     */
    public static final int DEFAULT_CACHE_SIZE = SootupStaticAnalyzer.DEFAULT_CACHE_SIZE;

    private final StaticAnalyzer staticAnalyzer;

    /**
     * Creates a new analyzer whose underlying {@link SootupStaticAnalyzer} is backed by a bounded
     * LRU cache sized {@link #DEFAULT_CACHE_SIZE}.
     */
    public DomainCallsAnalyzerImpl() {
        this(DEFAULT_CACHE_SIZE);
    }

    /**
     * Creates a new analyzer whose underlying {@link SootupStaticAnalyzer} is backed by a bounded
     * LRU cache of the given size, trading CPU (re-parsing evicted classes) for a lower, bounded
     * memory footprint. See {@link SootupStaticAnalyzer#SootupStaticAnalyzer(int)}.
     *
     * @param cacheSize the maximum number of classes held in the analyzer's cache at once
     */
    public DomainCallsAnalyzerImpl(int cacheSize) {
        this.staticAnalyzer = new SootupStaticAnalyzer(cacheSize);
    }

    /**
     * {@inheritDoc}
     *
     * @throws DLCPluginsException if the static analysis fails, e.g. because a classpath entry could
     *                             not be resolved
     */
    @Override
    public DomainCalls analyze(List<URL> classPathFiles, DomainMirror domainMirror) {
        log.info("Running static analysis of the domain classes");
        try {
            final List<Path> classpath = classPathFiles.stream()
                .map(this::toPath)
                .collect(Collectors.toList());
            return staticAnalyzer.analyze(domainMirror, classpath);
        } catch (RuntimeException e) {
            throw DLCPluginsException.fail("Static analysis of the domain classes failed.", e);
        }
    }

    private Path toPath(URL url) {
        try {
            return Path.of(url.toURI());
        } catch (URISyntaxException e) {
            throw DLCPluginsException.fail(String.format("Could not resolve classpath entry '%s'.", url), e);
        }
    }
}
