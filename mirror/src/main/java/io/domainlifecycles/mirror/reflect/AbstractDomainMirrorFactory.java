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

package io.domainlifecycles.mirror.reflect;

import io.domainlifecycles.mirror.api.BoundedContextMirror;
import io.domainlifecycles.mirror.exception.MirrorException;
import io.domainlifecycles.mirror.model.BoundedContextModel;
import io.domainlifecycles.mirror.resolver.GenericTypeResolver;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public abstract class AbstractDomainMirrorFactory {

    protected final String[] domainModelPackages;
    protected String[] boundedContextPackages;
    protected GenericTypeResolver genericTypeResolver;
    protected ClassLoader externalClassLoader;
    protected DomainTypeDetector domainTypeDetector;
    protected boolean includeNonDomainClasses = true;
    protected List<String> nonDomainExcludedSupertypePackages = NonDomainClassFilter.DEFAULT_EXCLUDED_SUPERTYPE_PACKAGES;
    protected List<String> nonDomainExcludedPackages = List.of();

    private static final Pattern packagePattern = Pattern.compile("^[a-z]+(\\.[a-zA-Z_][a-zA-Z0-9_]*)*$");

    /**
     * Initialize the factory with the domainModelPackages to be scanned.
     *
     * @param domainModelPackages the packages containing the domain model classes
     */
    public AbstractDomainMirrorFactory(String... domainModelPackages) {
        this.domainModelPackages = domainModelPackages;
        if (domainModelPackages == null || domainModelPackages.length == 0){
            throw MirrorException.fail("No domain model package defined!");
        }
        validatePackages(domainModelPackages);
    }

    protected static void validatePackages(final String... packageNames) {
        for (String packageName : packageNames) {
            if(!packagePattern.matcher(packageName).matches()){
                throw MirrorException.fail("Invalid package name: " + packageName);
            }
        }
    }

    /**
     * Sets the bounded context packages.
     *
     * @param boundedContextPackages an array of package names representing the bounded context boundaries int he domain model
     */
    public void setBoundedContextPackages(String[] boundedContextPackages) {
        this.boundedContextPackages = boundedContextPackages;
    }

    /**
     * Sets the GenericTypeResolver to be used for resolving generic types
     * within the context of this factory.
     *
     * @param genericTypeResolver an implementation of the GenericTypeResolver interface,
     *                            responsible for resolving generic type information
     *                            for fields, methods, and constructors.
     */
    public void setGenericTypeResolver(GenericTypeResolver genericTypeResolver) {
        this.genericTypeResolver = genericTypeResolver;
    }

    /**
     * Sets the external class loader to be used by this factory. This allows
     * the factory to utilize a custom class loader for loading and scanning
     * classes, typically useful for dynamically loaded classes or isolated
     * class loading environments.
     *
     * @param externalClassLoader the custom class loader to be used by the factory
     */
    public void setExternalClassLoader(ClassLoader externalClassLoader) {
        this.externalClassLoader = externalClassLoader;
    }

    /**
     * Sets the DomainTypeDetector instance to be used by this factory. The DomainTypeDetector
     * is responsible for determining the domain type of a given Type.
     *
     * @param domainTypeDetector an implementation of the DomainTypeDetector interface,
     *                           responsible for detecting and resolving the domain type
     *                           for a provided Type object.
     */
    public void setDomainTypeDetector(DomainTypeDetector domainTypeDetector) {
        this.domainTypeDetector = domainTypeDetector;
    }

    /**
     * Controls whether classes within {@code domainModelPackages} that do not implement any domain
     * marker interface are also mirrored (tagged with {@code DomainType.NON_DOMAIN}). Defaults to
     * {@code true}.
     *
     * @param includeNonDomainClasses whether non-domain classes should also be mirrored
     */
    public void setIncludeNonDomainClasses(boolean includeNonDomainClasses) {
        this.includeNonDomainClasses = includeNonDomainClasses;
    }

    /**
     * Sets the packages whose types, as superclass or interface (direct or inherited), exclude a class from
     * being mirrored as non-domain class. Defaults to {@code org.jooq}, which leaves out the table, record,
     * schema and catalog classes jOOQ generates - generated code that can make up the vast majority of a
     * domain model's non-domain classes without adding anything a domain diagram shows. An empty list
     * switches the supertype based exclusion off, {@code null} restores the default. See
     * {@link NonDomainClassFilter}.
     *
     * @param nonDomainExcludedSupertypePackages the excluded supertype packages
     */
    public void setNonDomainExcludedSupertypePackages(List<String> nonDomainExcludedSupertypePackages) {
        this.nonDomainExcludedSupertypePackages = nonDomainExcludedSupertypePackages == null
            ? NonDomainClassFilter.DEFAULT_EXCLUDED_SUPERTYPE_PACKAGES
            : List.copyOf(nonDomainExcludedSupertypePackages);
    }

    /**
     * Sets packages whose classes are not mirrored as non-domain classes, e.g. packages of generated code
     * whose classes share no common supertype. Defaults to none. See {@link NonDomainClassFilter}.
     *
     * @param nonDomainExcludedPackages the excluded packages
     */
    public void setNonDomainExcludedPackages(List<String> nonDomainExcludedPackages) {
        this.nonDomainExcludedPackages = nonDomainExcludedPackages == null ? List.of() : List.copyOf(nonDomainExcludedPackages);
    }

    /**
     * @return the filter for non-domain classes built from the configured exclusions
     */
    protected NonDomainClassFilter nonDomainClassFilter() {
        return new NonDomainClassFilter(nonDomainExcludedSupertypePackages, nonDomainExcludedPackages);
    }

    /**
     * Resolves the effective Bounded Contexts for this factory, in order of precedence:
     * <ol>
     *     <li>explicitly configured via {@link #setBoundedContextPackages(String[])} - always wins,
     *     regardless of what (if anything) was derived from package annotations</li>
     *     <li>otherwise, {@code derived} (from {@code @BoundedContext}-annotated packages found during
     *     scanning), if not empty</li>
     *     <li>otherwise, the previous default behavior: the whole {@code domainModelPackages} as a
     *     single Bounded Context</li>
     * </ol>
     * Either way, no two effective Bounded Context packages may be nested within one another (a Bounded
     * Context is expected to be a clean partition, not an overlapping one - see {@link
     * BoundedContextMirror}'s own class-level javadoc); a nested pair throws a {@link MirrorException}.
     *
     * @param derived the Bounded Contexts derived from package annotations during scanning, possibly empty
     * @return the effective, validated list of Bounded Context mirrors
     */
    protected List<BoundedContextMirror> resolveBoundedContexts(List<BoundedContextMirror> derived) {
        List<BoundedContextMirror> effective;
        if (this.boundedContextPackages != null) {
            validatePackages(this.boundedContextPackages);
            effective = toBoundedContextMirrors(this.boundedContextPackages);
        } else if (derived != null && !derived.isEmpty()) {
            effective = derived;
        } else {
            effective = toBoundedContextMirrors(this.domainModelPackages);
        }
        validateNoOverlap(effective);
        return effective;
    }

    private static List<BoundedContextMirror> toBoundedContextMirrors(String[] packageNames) {
        List<BoundedContextMirror> mirrors = new ArrayList<>();
        for (String packageName : packageNames) {
            mirrors.add(new BoundedContextModel(packageName));
        }
        return mirrors;
    }

    private static void validateNoOverlap(List<BoundedContextMirror> boundedContexts) {
        for (BoundedContextMirror outer : boundedContexts) {
            for (BoundedContextMirror inner : boundedContexts) {
                if (outer == inner) {
                    continue;
                }
                if (inner.getPackageName().startsWith(outer.getPackageName() + ".")) {
                    throw MirrorException.fail(
                        "Bounded Context package '%s' is nested within Bounded Context package '%s' - "
                            + "Bounded Context packages must not overlap.",
                        inner.getPackageName(), outer.getPackageName());
                }
            }
        }
    }

}
