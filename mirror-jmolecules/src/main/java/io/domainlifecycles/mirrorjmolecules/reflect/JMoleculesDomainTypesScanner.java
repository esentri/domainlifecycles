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

package io.domainlifecycles.mirrorjmolecules.reflect;

import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.api.EntityMirror;
import io.domainlifecycles.mirror.resolver.GenericTypeResolver;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.Identity;
import org.jmolecules.ddd.annotation.Repository;
import org.jmolecules.ddd.annotation.Service;
import org.jmolecules.ddd.annotation.ValueObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Scans the classpath for the classes of a given model package. Based on {@link io.github.classgraph}.
 *
 * @author Mario Herb
 */
public class JMoleculesDomainTypesScanner {

    private static final Logger log = LoggerFactory.getLogger(JMoleculesDomainTypesScanner.class);

    private final ClassLoader classLoader;
    private final GenericTypeResolver genericTypeResolver;

    /**
     * Constructs a new instance of the ClassGraphDomainTypesScanner with the provided ClassLoader
     * and GenericTypeResolver. This scanner utilizes ClassGraph to discover domain-related
     * types within specified packages.
     *
     * @param classLoader the ClassLoader to be used for loading classes during scanning. It must not be null.
     * @param genericTypeResolver the GenericTypeResolver to resolve generic type details during scanning. It must not be null.
     * @throws NullPointerException if either the classLoader or genericTypeResolver is null.
     */
    public JMoleculesDomainTypesScanner(ClassLoader classLoader, GenericTypeResolver genericTypeResolver) {
        this.classLoader = Objects.requireNonNull(classLoader, "Please provide a ClassLoader.");
        this.genericTypeResolver = Objects.requireNonNull(genericTypeResolver, "A GenericTypeResolver must be provided!");

    }

    /**
     * Constructs a ClassGraphDomainTypesScanner with the specified GenericTypeResolver.
     *
     * @param genericTypeResolver the GenericTypeResolver to be used by the scanner. It must not be null.
     *                            This resolver is responsible for determining generic type information.
     * @throws NullPointerException if the provided genericTypeResolver is null.
     */
    public JMoleculesDomainTypesScanner(GenericTypeResolver genericTypeResolver) {
        this.genericTypeResolver = Objects.requireNonNull(genericTypeResolver, "A GenericTypeResolver must be provided!");
        this.classLoader = null;
    }

    /**
     * Scans the specified package(s) to discover domain types, including enums, interfaces,
     * and classes implementing or extending various domain-related interfaces and types.
     * This method uses ClassGraph to perform the scanning.
     *
     * @param packages the array of package names to scan. If no packages are provided, the scan will include all available packages.
     * @return a list of discovered domain types represented as {@code DomainTypeMirror} instances.
     */
    public List<DomainTypeMirror> scan(String... packages) {
        List<DomainTypeMirror> domainTypes = new ArrayList<>();
        if(packages.length>0) {
            var packageNames = String.join(", ", packages);
            log.info("Scanning packages for domain types: {}", packageNames);
        }
        var classGraph = new ClassGraph()
        //.verbose()
        .enableAllInfo();
        if(packages.length>0){
            classGraph.acceptPackages(packages);
        }

        if(this.classLoader != null){
            classGraph.addClassLoader(this.classLoader);
        }

        try (ScanResult scanResult = classGraph.scan()) {  // Start the scan

            scanResult.getClassesWithAnnotation(Identity.class.getName())
                .stream()
                .map(this::loadClass)
                .filter(Objects::nonNull)
                .map(dt -> build(new IdentityMirrorBuilder(dt, genericTypeResolver)))
                .filter(Objects::nonNull)
                .forEach(domainTypes::add);

            scanResult.getClassesWithAnnotation(AggregateRoot.class.getName())
                .stream()
                .map(this::loadClass)
                .filter(Objects::nonNull)
                .filter(dt -> dt.isAnnotationPresent(AggregateRoot.class))
                .map(dt -> build(new AggregateRootMirrorBuilder(dt, genericTypeResolver)))
                .filter(Objects::nonNull)
                .forEach(domainTypes::add);

            scanResult.getClassesWithAnnotation(Service.class.getName())
                .stream()
                .map(this::loadClass)
                .filter(Objects::nonNull)
                .map(dt -> build(new ServiceMirrorBuilder(dt, genericTypeResolver)))
                .filter(Objects::nonNull)
                .forEach(domainTypes::add);

            scanResult.getClassesWithAnnotation(Repository.class.getName())
                .stream()
                .map(this::loadClass)
                .filter(Objects::nonNull)
                .map(dt -> build(new RepositoryMirrorBuilder(dt, genericTypeResolver)))
                .filter(Objects::nonNull)
                .forEach(domainTypes::add);

            scanResult.getClassesWithAnnotation(Entity.class.getName())
                .stream()
                .filter(classInfo -> !classInfo.isAnnotation())
                .map(this::loadClass)
                .filter(Objects::nonNull)
                .filter(dt -> dt.isAnnotationPresent(Entity.class))
                .filter(dt -> !dt.isAnnotationPresent(AggregateRoot.class))
                .map(dt -> (EntityMirror) build(new EntityMirrorBuilder<>(dt, genericTypeResolver)))
                .filter(Objects::nonNull)
                .forEach(domainTypes::add);

            scanResult.getClassesWithAnnotation(ValueObject.class.getName())
                .stream()
                .map(this::loadClass)
                .filter(Objects::nonNull)
                .map(dt -> build(new ValueObjectMirrorBuilder(dt, genericTypeResolver)))
                .filter(Objects::nonNull)
                .forEach(domainTypes::add);


        } catch (Throwable t) {
            log.error("Scanning packages '{}' failed!", packages, t);
        }
        return domainTypes;
    }

    private Class<?> loadClass(ClassInfo classInfo) {
        try {
            return classInfo.loadClass();
        }catch (Throwable t) {
            log.error("Loading class '{}' failed!", classInfo.getName(), t);
        }
        return null;
    }


    private <T extends DomainTypeMirror> T build(DomainTypeMirrorBuilder<T> builder) {
        try{
            return builder.build();
        } catch (Throwable t) {
            log.error("Building mirror failed! {}",  builder.domainClass.getName(), t);
        }
        return null;
    }

}
