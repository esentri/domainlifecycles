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

import io.domainlifecycles.domain.types.AggregateCommand;
import io.domainlifecycles.domain.types.AggregateRoot;
import io.domainlifecycles.domain.types.ApplicationService;
import io.domainlifecycles.domain.types.DomainCommand;
import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.domain.types.DomainService;
import io.domainlifecycles.domain.types.DomainServiceCommand;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.OutboundService;
import io.domainlifecycles.domain.types.QueryHandler;
import io.domainlifecycles.domain.types.ReadModel;
import io.domainlifecycles.domain.types.Repository;
import io.domainlifecycles.domain.types.ServiceKind;
import io.domainlifecycles.domain.types.ValueObject;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.api.EntityMirror;
import io.domainlifecycles.mirror.api.ServiceKindMirror;
import io.domainlifecycles.mirror.resolver.GenericTypeResolver;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Scans the classpath for the classes of a given model package. Based on {@link io.github.classgraph}.
 *
 * @author Mario Herb
 */
public class ClassGraphDomainTypesScanner {

    private static final Logger log = LoggerFactory.getLogger(ClassGraphDomainTypesScanner.class);

    private final ClassLoader classLoader;
    private final GenericTypeResolver genericTypeResolver;

    /**
     * Create scanner based on dedicated ClassLoader.
     *
     * @param classLoader pass a dynamically created URLCLassLoader with dynamically loaded classes
     */
    public ClassGraphDomainTypesScanner(ClassLoader classLoader, GenericTypeResolver genericTypeResolver) {
        this.classLoader = Objects.requireNonNull(classLoader, "Please provide a ClassLoader.");
        this.genericTypeResolver = Objects.requireNonNull(genericTypeResolver, "A GenericTypeResolver must be provided!");

    }

    /**
     * Constructor
     */
    public ClassGraphDomainTypesScanner(GenericTypeResolver genericTypeResolver) {
        this.genericTypeResolver = Objects.requireNonNull(genericTypeResolver, "A GenericTypeResolver must be provided!");
        this.classLoader = null;
    }

    /**
     * Scans the given packages for Domain Model classes.
     *
     * @param packages the packages to be scanned
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

            scanResult.getAllEnums()
                .stream()
                .map(r -> (Class<? extends Enum<?>>) loadClass(r))
                .filter(Objects::nonNull)
                .map(dt -> build(new EnumMirrorBuilder(dt, genericTypeResolver)))
                .filter(Objects::nonNull)
                .forEach(domainTypes::add);

            scanResult.getClassesImplementing(Identity.class)
                .stream()
                .filter(c -> !Identity.class.getName().equals(c.getName()))
                .map(r -> (Class<? extends Identity<?>>) loadClass(r))
                .filter(Objects::nonNull)
                .map(dt -> build(new IdentityMirrorBuilder(dt, genericTypeResolver)))
                .filter(Objects::nonNull)
                .forEach(domainTypes::add);

            scanResult.getClassesImplementing(ValueObject.class)
                .stream()
                .filter(c -> !ValueObject.class.getName().equals(c.getName()))
                .map(r -> (Class<? extends ValueObject>) loadClass(r))
                .filter(Objects::nonNull)
                .map(dt -> build(new ValueObjectMirrorBuilder(dt, genericTypeResolver)))
                .filter(Objects::nonNull)
                .forEach(domainTypes::add);

            scanResult.getClassesImplementing(Entity.class)
                .filter(f -> !f.implementsInterface(AggregateRoot.class))
                .filter(c -> !Entity.class.getName().equals(c.getName()))
                .filter(c -> !AggregateRoot.class.getName().equals(c.getName()))
                .stream()
                .map(r -> (Class<? extends Entity<?>>) loadClass(r))
                .filter(Objects::nonNull)
                .map(dt -> build(new EntityMirrorBuilder<EntityMirror>(dt, genericTypeResolver)))
                .filter(Objects::nonNull)
                .forEach(domainTypes::add);

            scanResult.getClassesImplementing(AggregateRoot.class)
                .stream()
                .filter(c -> !AggregateRoot.class.getName().equals(c.getName()))
                .map(r -> (Class<? extends AggregateRoot<?>>) loadClass(r))
                .filter(Objects::nonNull)
                .map(dt -> build(new AggregateRootMirrorBuilder(dt, genericTypeResolver)))
                .filter(Objects::nonNull)
                .forEach(domainTypes::add);

            scanResult.getClassesImplementing(DomainService.class)
                .stream()
                .filter(c -> !DomainService.class.getName().equals(c.getName()))
                .map(r -> (Class<? extends DomainService>) loadClass(r))
                .filter(Objects::nonNull)
                .map(dt -> build(new DomainServiceMirrorBuilder(dt, genericTypeResolver)))
                .filter(Objects::nonNull)
                .forEach(domainTypes::add);

            scanResult.getClassesImplementing(ApplicationService.class)
                .stream()
                .filter(c -> !ApplicationService.class.getName().equals(c.getName()))
                .map(r -> (Class<? extends ApplicationService>) loadClass(r))
                .filter(Objects::nonNull)
                .map(dt -> build(new ApplicationServiceMirrorBuilder(dt, genericTypeResolver)))
                .filter(Objects::nonNull)
                .forEach(domainTypes::add);

            scanResult.getClassesImplementing(DomainEvent.class)
                .stream()
                .filter(c -> !DomainEvent.class.getName().equals(c.getName()))
                .map(r -> (Class<? extends DomainEvent>) loadClass(r))
                .filter(Objects::nonNull)
                .map(dt -> build(new DomainEventMirrorBuilder(dt, genericTypeResolver)))
                .filter(Objects::nonNull)
                .forEach(domainTypes::add);

            scanResult.getClassesImplementing(Repository.class)
                .stream()
                .filter(c -> !Repository.class.getName().equals(c.getName()))
                .map(r -> (Class<? extends Repository<?, ?>>) loadClass(r))
                .filter(Objects::nonNull)
                .map(dt -> build(new RepositoryMirrorBuilder(dt, genericTypeResolver)))
                .filter(Objects::nonNull)
                .forEach(domainTypes::add);

            scanResult.getClassesImplementing(DomainCommand.class)
                .stream()
                .filter(c -> !DomainCommand.class.getName().equals(c.getName()))
                .filter(c -> !AggregateCommand.class.getName().equals(c.getName()))
                .filter(c -> !DomainServiceCommand.class.getName().equals(c.getName()))
                .map(r -> (Class<? extends DomainCommand>) loadClass(r))
                .filter(Objects::nonNull)
                .map(dt -> build(new DomainCommandMirrorBuilder(dt, genericTypeResolver)))
                .filter(Objects::nonNull)
                .forEach(domainTypes::add);

            scanResult.getClassesImplementing(ReadModel.class)
                .stream()
                .filter(c -> !ReadModel.class.getName().equals(c.getName()))
                .map(r -> (Class<? extends ReadModel>) loadClass(r))
                .filter(Objects::nonNull)
                .map(dt -> build(new ReadModelMirrorBuilder(dt, genericTypeResolver)))
                .filter(Objects::nonNull)
                .forEach(domainTypes::add);

            scanResult.getClassesImplementing(QueryHandler.class)
                .stream()
                .filter(c -> !QueryHandler.class.getName().equals(c.getName()))
                .map(r -> (Class<? extends QueryHandler<?>>) loadClass(r))
                .filter(Objects::nonNull)
                .map(dt -> build(new QueryHandlerMirrorBuilder(dt, genericTypeResolver)))
                .filter(Objects::nonNull)
                .forEach(domainTypes::add);

            scanResult.getClassesImplementing(OutboundService.class)
                .stream()
                .filter(c -> !OutboundService.class.getName().equals(c.getName()))
                .map(r -> (Class<? extends OutboundService>) loadClass(r))
                .filter(Objects::nonNull)
                .map(dt -> build(new OutboundServiceMirrorBuilder(dt, genericTypeResolver)))
                .filter(Objects::nonNull)
                .forEach(domainTypes::add);

            scanResult.getClassesImplementing(ServiceKind.class)
                .stream()
                .filter(c -> !ServiceKind.class.getName().equals(c.getName())
                            && !ApplicationService.class.getName().equals(c.getName())
                            && !DomainService.class.getName().equals(c.getName())
                            && !OutboundService.class.getName().equals(c.getName())
                            && !QueryHandler.class.getName().equals(c.getName())
                            && !Repository.class.getName().equals(c.getName()))
                .filter(c -> !(c.implementsInterface(ApplicationService.class))
                            && !(c.implementsInterface(DomainService.class))
                            && !(c.implementsInterface(OutboundService.class))
                            && !(c.implementsInterface(QueryHandler.class))
                            && !(c.implementsInterface(Repository.class)))
                .map(r -> (Class<? extends ServiceKind>) loadClass(r))
                .filter(Objects::nonNull)
                .map(dt -> build(new ServiceKindMirrorBuilder<ServiceKindMirror>(dt, genericTypeResolver)))
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
        }catch (Throwable t) {
            log.error("Building mirror failed! {}",  builder.domainClass.getName(), t);
        }
        return null;
    }

}
