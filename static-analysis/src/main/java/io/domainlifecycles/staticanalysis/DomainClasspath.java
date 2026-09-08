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

import io.domainlifecycles.mirror.api.DomainMirror;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Resolves the classpath a {@link StaticAnalyzer} needs.
 * <p>
 * The analysis works on compiled bytecode, so it has to be told where to find it. Assembling that
 * list by hand is error prone: a missing entry does not fail the analysis, it silently shrinks the
 * result. {@link #ofMirroredTypes(DomainMirror)} therefore derives the classpath from the mirror
 * itself, by asking every mirrored type where its own class file came from. That covers the domain
 * classes as well as the DLC base types the domain inherits from.
 * <p>
 * Entries that cannot be located - types not loadable by the given class loader, types without a
 * code source (JDK classes), or locations that are not files - are skipped and logged. Use
 * {@link #ofTypes(Class[])} to add anchors for anything the mirror does not cover.
 *
 * @author Mario Herb
 */
public final class DomainClasspath {

    private static final Logger log = LoggerFactory.getLogger(DomainClasspath.class);

    private DomainClasspath() {
    }

    /**
     * Derives the classpath from the code sources of all mirrored types, resolved with the class
     * loader of this class.
     *
     * @param domainMirror the mirror of the domain to analyze, must not be {@code null}
     * @return the deduplicated, existing classpath entries
     */
    public static List<Path> ofMirroredTypes(DomainMirror domainMirror) {
        return ofMirroredTypes(domainMirror, DomainClasspath.class.getClassLoader());
    }

    /**
     * Derives the classpath from the code sources of all mirrored types.
     *
     * @param domainMirror the mirror of the domain to analyze, must not be {@code null}
     * @param classLoader  the class loader to resolve the mirrored type names with, must not be
     *                     {@code null}
     * @return the deduplicated, existing classpath entries
     */
    public static List<Path> ofMirroredTypes(DomainMirror domainMirror, ClassLoader classLoader) {
        Objects.requireNonNull(domainMirror, "A DomainMirror must be given!");
        Objects.requireNonNull(classLoader, "A ClassLoader must be given!");

        Set<Path> classpath = new LinkedHashSet<>();
        for (var domainTypeMirror : domainMirror.getAllDomainTypeMirrors()) {
            loadClass(domainTypeMirror.getTypeName(), classLoader)
                .flatMap(DomainClasspath::codeSourceOf)
                .ifPresent(classpath::add);
        }
        return List.copyOf(classpath);
    }

    /**
     * Resolves the classpath entries the given types were loaded from. Useful as an addition to
     * {@link #ofMirroredTypes(DomainMirror)} for types the mirror does not know about.
     *
     * @param types the types to locate, must not be {@code null}
     * @return the deduplicated, existing classpath entries
     */
    public static List<Path> ofTypes(Class<?>... types) {
        Objects.requireNonNull(types, "The types must be given!");
        return ofTypes(Arrays.asList(types));
    }

    /**
     * Resolves the classpath entries the given types were loaded from.
     *
     * @param types the types to locate, must not be {@code null}
     * @return the deduplicated, existing classpath entries
     */
    public static List<Path> ofTypes(Collection<Class<?>> types) {
        Objects.requireNonNull(types, "The types must be given!");
        Set<Path> classpath = new LinkedHashSet<>();
        for (Class<?> type : types) {
            codeSourceOf(type).ifPresent(classpath::add);
        }
        return List.copyOf(classpath);
    }

    /**
     * Merges classpaths, keeping the order of first occurrence and dropping duplicates.
     *
     * @param classpaths the classpaths to merge, must not be {@code null}
     * @return the merged classpath
     */
    @SafeVarargs
    public static List<Path> merge(List<Path>... classpaths) {
        Objects.requireNonNull(classpaths, "The classpaths must be given!");
        Set<Path> merged = new LinkedHashSet<>();
        Arrays.stream(classpaths).forEach(merged::addAll);
        return List.copyOf(merged);
    }

    /**
     * Parses a classpath string as it is passed on a command line or by a build plugin.
     *
     * @param classpath entries separated by the platform path separator, must not be {@code null}
     * @return the existing classpath entries
     */
    public static List<Path> parse(String classpath) {
        Objects.requireNonNull(classpath, "A classpath must be given!");
        List<Path> parsed = new ArrayList<>();
        for (String entry : classpath.split(File.pathSeparator)) {
            if (entry.isBlank()) {
                continue;
            }
            Path path = Path.of(entry);
            if (!Files.exists(path)) {
                log.warn("Skipping classpath entry '{}', it does not exist.", entry);
                continue;
            }
            if (!parsed.contains(path)) {
                parsed.add(path);
            }
        }
        return List.copyOf(parsed);
    }

    private static Optional<Class<?>> loadClass(String typeName, ClassLoader classLoader) {
        try {
            // initialize = false: locating the class file must not run static initializers
            return Optional.of(Class.forName(typeName, false, classLoader));
        } catch (ClassNotFoundException | LinkageError e) {
            log.debug("Could not load mirrored type '{}' to locate its code source.", typeName, e);
            return Optional.empty();
        }
    }

    private static Optional<Path> codeSourceOf(Class<?> type) {
        try {
            CodeSource codeSource = type.getProtectionDomain().getCodeSource();
            if (codeSource == null || codeSource.getLocation() == null) {
                // JDK and other bootstrap classes have no code source
                log.debug("Type '{}' has no code source.", type.getTypeName());
                return Optional.empty();
            }
            Path path = Path.of(codeSource.getLocation().toURI());
            if (!Files.exists(path)) {
                log.debug("Code source '{}' of type '{}' does not exist.", path,
                    type.getTypeName());
                return Optional.empty();
            }
            return Optional.of(path);
        } catch (Exception e) {
            log.debug("Could not resolve the code source of type '{}'.", type.getTypeName(), e);
            return Optional.empty();
        }
    }
}
