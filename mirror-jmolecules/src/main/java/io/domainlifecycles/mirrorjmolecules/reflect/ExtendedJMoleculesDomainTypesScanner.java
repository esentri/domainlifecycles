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

package io.domainlifecycles.mirrorjmolecules.reflect;

import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.api.EntityMirror;
import io.domainlifecycles.mirror.reflect.ClassGraphDomainTypesScanner;
import io.domainlifecycles.mirror.reflect.DomainTypeDetector;
import io.domainlifecycles.mirror.resolver.GenericTypeResolver;
import io.github.classgraph.ScanResult;
import java.util.List;
import java.util.Objects;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.Repository;
import org.jmolecules.ddd.annotation.Service;
import org.jmolecules.ddd.annotation.ValueObject;
import org.jmolecules.ddd.types.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Scans the classpath for the classes of a given model package. Based on {@link io.github.classgraph}.
 *
 * @author Mario Herb
 */
public class ExtendedJMoleculesDomainTypesScanner extends ClassGraphDomainTypesScanner {

    private static final Logger log = LoggerFactory.getLogger(ExtendedJMoleculesDomainTypesScanner.class);

    /**
     * Constructs a new instance of the ExtendedJMoleculesDomainTypesScanner with the provided ClassLoader
     * and GenericTypeResolver. This scanner utilizes ClassGraph to discover domain-related
     * types within specified packages.
     *
     * @param classLoader the ClassLoader to be used for loading classes during scanning. It must not be null.
     * @param genericTypeResolver the GenericTypeResolver to resolve generic type details during scanning. It must not be null.
     * @param domainTypeDetector detector to identify domain types
     * @throws NullPointerException if either the classLoader or genericTypeResolver is null.
     */
    public ExtendedJMoleculesDomainTypesScanner(ClassLoader classLoader, GenericTypeResolver genericTypeResolver, DomainTypeDetector domainTypeDetector ) {
        super(classLoader, genericTypeResolver, domainTypeDetector);
    }

    /**
     * Constructs a ExtendedJMoleculesDomainTypesScanner with the specified GenericTypeResolver.
     *
     * @param genericTypeResolver the GenericTypeResolver to be used by the scanner. It must not be null.
     *                            This resolver is responsible for determining generic type information.
     * @param domainTypeDetector detector to identify domain types
     * @throws NullPointerException if the provided genericTypeResolver is null.
     */
    public ExtendedJMoleculesDomainTypesScanner(GenericTypeResolver genericTypeResolver, DomainTypeDetector domainTypeDetector) {
        super(genericTypeResolver, domainTypeDetector);
    }

    protected List<DomainTypeMirror> buildMirrorsFromScanResult(ScanResult scanResult) {
        List<DomainTypeMirror> domainTypes = super.buildMirrorsFromScanResult(scanResult);
        buildDomainMirrorsWithJMoleculesAnnotations(scanResult, domainTypes);
        buildDomainMirrorsWithJMoleculesInterfaces(scanResult, domainTypes);
        return domainTypes;
    }


    private void buildDomainMirrorsWithJMoleculesAnnotations(ScanResult scanResult, List<DomainTypeMirror> domainTypes) {
        scanResult.getClassesWithAnnotation(AggregateRoot.class.getName())
            .stream()
            .map(this::loadClass)
            .filter(Objects::nonNull)
            .filter(dt -> dt.isAnnotationPresent(AggregateRoot.class))
            .map(dt -> build(new AggregateRootMirrorBuilder(dt, genericTypeResolver, domainTypeDetector)))
            .filter(Objects::nonNull)
            .forEach(domainTypes::add);

        scanResult.getClassesWithAnnotation(Repository.class.getName())
            .stream()
            .map(this::loadClass)
            .filter(Objects::nonNull)
            .map(dt -> build(new RepositoryMirrorBuilder(dt, genericTypeResolver, domainTypeDetector)))
            .filter(Objects::nonNull)
            .forEach(domainTypes::add);

        scanResult.getClassesWithAnnotation(Entity.class.getName())
            .stream()
            .filter(classInfo -> !classInfo.isAnnotation())
            .map(this::loadClass)
            .filter(Objects::nonNull)
            .filter(dt -> dt.isAnnotationPresent(Entity.class))
            .filter(dt -> !dt.isAnnotationPresent(AggregateRoot.class))
            .map(dt -> (EntityMirror) build(new EntityMirrorBuilder<>(dt, genericTypeResolver, domainTypeDetector)))
            .filter(Objects::nonNull)
            .forEach(domainTypes::add);

        scanResult.getClassesWithAnnotation(ValueObject.class.getName())
            .stream()
            .map(this::loadClass)
            .filter(Objects::nonNull)
            .map(dt -> build(new ValueObjectMirrorBuilder(dt, genericTypeResolver, domainTypeDetector)))
            .filter(Objects::nonNull)
            .forEach(domainTypes::add);

        scanResult.getClassesWithAnnotation(org.jmolecules.event.annotation.DomainEvent.class.getName())
            .stream()
            .map(this::loadClass)
            .filter(Objects::nonNull)
            .map(dt -> build(new DomainEventMirrorBuilder(dt, genericTypeResolver, domainTypeDetector)))
            .filter(Objects::nonNull)
            .forEach(domainTypes::add);

        scanResult.getClassesWithAnnotation(Service.class.getName())
            .stream()
            .map(this::loadClass)
            .filter(Objects::nonNull)
            .map(dt -> build(new DomainServiceMirrorBuilder(dt, genericTypeResolver, domainTypeDetector)))
            .filter(Objects::nonNull)
            .forEach(domainTypes::add);
    }

    private void buildDomainMirrorsWithJMoleculesInterfaces(ScanResult scanResult, List<DomainTypeMirror> domainTypes) {
        scanResult.getClassesImplementing(Identifier.class)
            .stream()
            .map(this::loadClass)
            .filter(Objects::nonNull)
            .map(dt -> build(new IdentityMirrorBuilder(dt, genericTypeResolver, domainTypeDetector)))
            .filter(Objects::nonNull)
            .forEach(domainTypes::add);

        scanResult.getClassesImplementing(org.jmolecules.ddd.types.AggregateRoot.class)
            .stream()
            .map(this::loadClass)
            .filter(Objects::nonNull)
            .map(dt -> build(new AggregateRootMirrorBuilder(dt, genericTypeResolver, domainTypeDetector)))
            .filter(Objects::nonNull)
            .forEach(domainTypes::add);

        scanResult.getClassesImplementing(org.jmolecules.ddd.types.Repository.class)
            .stream()
            .map(this::loadClass)
            .filter(Objects::nonNull)
            .map(dt -> build(new RepositoryMirrorBuilder(dt, genericTypeResolver, domainTypeDetector)))
            .filter(Objects::nonNull)
            .forEach(domainTypes::add);

        scanResult.getClassesImplementing(org.jmolecules.ddd.types.Entity.class)
            .stream()
            .filter(classInfo -> !org.jmolecules.ddd.types.AggregateRoot.class.getName().equals(classInfo.getName()))
            .filter(classInfo -> !classInfo.implementsInterface(org.jmolecules.ddd.types.AggregateRoot.class.getName()))
            .map(this::loadClass)
            .filter(Objects::nonNull)
            .map(dt -> (EntityMirror) build(new EntityMirrorBuilder<>(dt, genericTypeResolver, domainTypeDetector)))
            .filter(Objects::nonNull)
            .forEach(domainTypes::add);

        scanResult.getClassesImplementing(org.jmolecules.ddd.types.ValueObject.class)
            .stream()
            .map(this::loadClass)
            .filter(Objects::nonNull)
            .map(dt -> build(new ValueObjectMirrorBuilder(dt, genericTypeResolver, domainTypeDetector)))
            .filter(Objects::nonNull)
            .forEach(domainTypes::add);

        scanResult.getClassesImplementing(org.jmolecules.event.types.DomainEvent.class)
            .stream()
            .map(this::loadClass)
            .filter(Objects::nonNull)
            .map(dt -> build(new DomainEventMirrorBuilder(dt, genericTypeResolver, domainTypeDetector)))
            .filter(Objects::nonNull)
            .forEach(domainTypes::add);
    }

}
