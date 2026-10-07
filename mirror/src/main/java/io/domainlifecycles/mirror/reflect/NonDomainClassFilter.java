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
 *  Copyright 2019-2024 the original author or authors.
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
package io.domainlifecycles.mirror.reflect;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Decides which classes are left out when mirroring non-domain classes (see
 * {@link AbstractDomainMirrorFactory#setIncludeNonDomainClasses(boolean)}).
 * <p>
 * Generated code can dominate a domain model's non-domain classes by far - e.g. the table and record
 * classes jOOQ generates, which declare hundreds to thousands of methods each. Mirroring them bloats the
 * domain mirror (and the static analysis built on top of it) without adding anything a domain diagram
 * shows. Such classes are recognized by their supertypes; generation annotations like
 * {@code @Generated} are not usable for this, since they are retained in the source code only.
 * <p>
 * A class is excluded if
 * <ul>
 *     <li>its name lies within one of the {@link #excludedPackages()}, or</li>
 *     <li>one of its superclasses or implemented interfaces (direct or inherited) lies within one of the
 *     {@link #excludedSupertypePackages()} - by default {@code org.jooq}, which catches all classes jOOQ
 *     generates for tables, records, schemas and catalogs.</li>
 * </ul>
 * Package names match the package itself and all its sub-packages.
 *
 * @param excludedSupertypePackages packages whose types, as supertypes, exclude a class from being mirrored
 * @param excludedPackages          packages whose classes are not mirrored as non-domain classes
 * @author Mario Herb
 */
public record NonDomainClassFilter(List<String> excludedSupertypePackages, List<String> excludedPackages) {

    /**
     * The supertype packages excluded by default: jOOQ's generated code.
     */
    public static final List<String> DEFAULT_EXCLUDED_SUPERTYPE_PACKAGES = List.of("org.jooq");

    /**
     * The default filter: excludes jOOQ's generated code, no packages.
     */
    public static final NonDomainClassFilter DEFAULT = new NonDomainClassFilter(null, null);

    /**
     * A filter excluding nothing.
     */
    public static final NonDomainClassFilter NONE = new NonDomainClassFilter(List.of(), List.of());

    /**
     * Creates a filter. {@code null} for the supertype packages means the default
     * ({@link #DEFAULT_EXCLUDED_SUPERTYPE_PACKAGES}), an empty list switches supertype based exclusion off.
     * {@code null} for the packages means none.
     *
     * @param excludedSupertypePackages packages whose types, as supertypes, exclude a class, or {@code null} for the default
     * @param excludedPackages          packages whose classes are excluded, or {@code null} for none
     */
    public NonDomainClassFilter {
        excludedSupertypePackages = excludedSupertypePackages == null
            ? DEFAULT_EXCLUDED_SUPERTYPE_PACKAGES
            : List.copyOf(excludedSupertypePackages);
        excludedPackages = excludedPackages == null ? List.of() : List.copyOf(excludedPackages);
    }

    /**
     * Checks a class name against the excluded packages. Cheap, so it is applied before a class is loaded.
     *
     * @param typeName the full qualified class name
     * @return {@code true} if the class lies within an excluded package
     */
    public boolean isExcludedByName(String typeName) {
        return isWithin(typeName, excludedPackages);
    }

    /**
     * Checks the superclasses and interfaces (direct and inherited) of a loaded class against the excluded
     * supertype packages.
     *
     * @param type the class to check
     * @return {@code true} if one of its supertypes lies within an excluded supertype package
     */
    public boolean isExcludedBySupertype(Class<?> type) {
        if (excludedSupertypePackages.isEmpty()) {
            return false;
        }
        Deque<Class<?>> toVisit = new ArrayDeque<>();
        Set<Class<?>> visited = new HashSet<>();
        if (type.getSuperclass() != null) {
            toVisit.add(type.getSuperclass());
        }
        toVisit.addAll(List.of(type.getInterfaces()));
        while (!toVisit.isEmpty()) {
            Class<?> supertype = toVisit.poll();
            if (!visited.add(supertype)) {
                continue;
            }
            if (isWithin(supertype.getName(), excludedSupertypePackages)) {
                return true;
            }
            if (supertype.getSuperclass() != null) {
                toVisit.add(supertype.getSuperclass());
            }
            toVisit.addAll(List.of(supertype.getInterfaces()));
        }
        return false;
    }

    private static boolean isWithin(String typeName, List<String> packages) {
        for (String p : packages) {
            if (typeName.equals(p) || typeName.startsWith(p + ".")) {
                return true;
            }
        }
        return false;
    }
}
