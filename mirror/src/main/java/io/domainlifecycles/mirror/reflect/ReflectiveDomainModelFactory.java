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

import io.domainlifecycles.mirror.api.BoundedContextMirror;
import io.domainlifecycles.mirror.api.DomainModel;
import io.domainlifecycles.mirror.api.DomainModelFactory;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.exception.MirrorException;
import io.domainlifecycles.mirror.model.BoundedContextModel;
import io.domainlifecycles.mirror.resolver.DefaultEmptyGenericTypeResolver;
import io.domainlifecycles.mirror.resolver.GenericTypeResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Uses Java reflection to initialize a mirror of the domain by analyzing the given bounded context packages.
 *
 * @author Mario Herb
 */
public class ReflectiveDomainModelFactory implements DomainModelFactory {
    private static final Logger log = LoggerFactory.getLogger(ReflectiveDomainModelFactory.class);
    private final ClassGraphDomainTypesScanner classGraphDomainTypesScanner;
    private final String[] boundedContextPackages;
    private final GenericTypeResolver genericTypeResolver;

    private static final Pattern packagePattern = Pattern.compile("^[a-z]+(\\.[a-zA-Z_][a-zA-Z0-9_]*)*$");


    /**
     * Initialize the factory with the boundedContextPackages to be scanned.
     *
     * @param genericTypeResolver type Resolver implementation, that resolves generics and type arguments
     * @param boundedContextPackages the bounded context packages
     */
    public ReflectiveDomainModelFactory(GenericTypeResolver genericTypeResolver, String... boundedContextPackages) {
        this.boundedContextPackages = boundedContextPackages;
        this.genericTypeResolver = genericTypeResolver == null ? new DefaultEmptyGenericTypeResolver() : genericTypeResolver;
        validatePackages(boundedContextPackages);
        this.classGraphDomainTypesScanner = new ClassGraphDomainTypesScanner(genericTypeResolver);
    }

    /**
     * Initialize the factory with the boundedContextPackages to be scanned.
     *
     * @param boundedContextPackages the bounded context packages
     */
    public ReflectiveDomainModelFactory(String... boundedContextPackages) {
        this.boundedContextPackages = boundedContextPackages;
        this.genericTypeResolver = new DefaultEmptyGenericTypeResolver();
        validatePackages(boundedContextPackages);
        this.classGraphDomainTypesScanner = new ClassGraphDomainTypesScanner(genericTypeResolver);
    }

    /**
     * Initialize the factory with the boundedContextPackages to be scanned.
     * And a dedicated class loader (potentially a sub classloader that provides access to dynamically loaded classes)
     *
     * @param classLoader pass a dynamically created URLCLassLoader with dynamically loaded classes
     * @param genericTypeResolver type Resolver implementation, that resolves generics and type arguments
     * @param boundedContextPackages the bounded context packages
     */
    public ReflectiveDomainModelFactory(ClassLoader classLoader, GenericTypeResolver genericTypeResolver, String... boundedContextPackages) {
        this.boundedContextPackages = boundedContextPackages;
        this.genericTypeResolver = genericTypeResolver == null ? new DefaultEmptyGenericTypeResolver() : genericTypeResolver;
        validatePackages(boundedContextPackages);
        this.classGraphDomainTypesScanner = new ClassGraphDomainTypesScanner(classLoader, genericTypeResolver);
    }

    /**
     * Initialize the factory with the boundedContextPackages to be scanned.
     * And a dedicated class loader (potentially a sub classloader that provides access to dynamically loaded classes)
     *
     * @param classLoader pass a dynamically created URLCLassLoader with dynamically loaded classes
     * @param boundedContextPackages the bounded context packages
     */
    public ReflectiveDomainModelFactory(ClassLoader classLoader,String... boundedContextPackages) {
        this.boundedContextPackages = boundedContextPackages;
        this.genericTypeResolver = new DefaultEmptyGenericTypeResolver();
        validatePackages(boundedContextPackages);
        this.classGraphDomainTypesScanner = new ClassGraphDomainTypesScanner(classLoader, genericTypeResolver);
    }

    /**
     * Initializes the domain with the scanned classes.
     *
     * @return DomainModel - a container for all mirrors that are available in the analyzed bounded contexts.
     */
    @Override
    public DomainModel initializeDomainModel() {
        Map<String, ? extends DomainTypeMirror> builtTypeMirrors =
            classGraphDomainTypesScanner
                .scan(boundedContextPackages)
                .stream()
                .collect(
                    Collectors.toMap(
                        DomainTypeMirror::getTypeName,
                        Function.identity()
                    )
                );

        builtTypeMirrors
            .values()
            .forEach(m -> {
                log.debug("Created Mirror:" + m);

            });

        return new DomainModel(builtTypeMirrors, buildBoundedContextMirrors());

    }

    private List<BoundedContextMirror> buildBoundedContextMirrors() {
        return Arrays.stream(boundedContextPackages)
            .map(BoundedContextModel::new
            )
            .collect(Collectors.toList());
    }

    /**
     * @return whether the given name is a valid full qualified package name.
     */
    private static void validatePackages(final String... packageNames) {
        for (String packageName : packageNames) {
            if(!packagePattern.matcher(packageName).matches()){
                throw MirrorException.fail("Invalid package name: " + packageName);
            }
        }
    }

}
