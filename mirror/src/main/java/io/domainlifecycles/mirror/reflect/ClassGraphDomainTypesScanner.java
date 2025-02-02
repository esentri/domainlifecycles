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
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Scans the classpath for the classes of a given model package. Based on {@link io.github.classgraph}.
 *
 * @author Mario Herb
 */
public class ClassGraphDomainTypesScanner {

    private static final Logger log = LoggerFactory.getLogger(ClassGraphDomainTypesScanner.class);


    private ClassLoader classLoader;

    /**
     * Create scanner based on dedicated ClassLoader.
     *
     * @param classLoader pass a dynamically created URLCLassLoader with dynamically loaded classes
     */
    public ClassGraphDomainTypesScanner(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * Constructor
     */
    public ClassGraphDomainTypesScanner() {
    }

    /**
     * Scans the given packages for Domain Model classes.
     *
     * @param boundedContextPackages the bounded context packages
     */
    public List<DomainTypeMirror> scan(String... boundedContextPackages) {
        List<DomainTypeMirror> domainTypes = new ArrayList<>();
        Objects.requireNonNull(boundedContextPackages, "boundedContextPackages cannot be null");
        if(boundedContextPackages.length>0){
            var packageNames = Arrays.stream(boundedContextPackages).collect(Collectors.joining(", "));
            log.info("Scanning bounded context package for domain types: {}", packageNames);
            var classGraph = new ClassGraph()
            //.verbose()               // Log to stderr
            .enableAllInfo()// Scan classes, methods, fields, annotations
            .acceptPackages(boundedContextPackages);

            if(this.classLoader != null){
                classGraph.addClassLoader(this.classLoader);
            }

            try (ScanResult scanResult = classGraph.scan()) {  // Start the scan

                scanResult.getAllEnums()
                    .stream()
                    .map(r -> (Class<? extends Enum<?>>) r.loadClass())
                    .map(dt -> new EnumMirrorBuilder(dt).build())
                    .forEach(domainTypes::add);

                scanResult.getClassesImplementing(Identity.class)
                    .stream()
                    .filter(c -> !Identity.class.getName().equals(c.getName()))
                    .map(r -> (Class<? extends Identity<?>>) r.loadClass())
                    .map(dt -> new IdentityMirrorBuilder(dt).build())
                    .forEach(domainTypes::add);

                scanResult.getClassesImplementing(ValueObject.class)
                    .stream()
                    .filter(c -> !ValueObject.class.getName().equals(c.getName()))
                    .map(r -> (Class<? extends ValueObject>) r.loadClass())
                    .map(dt -> new ValueObjectMirrorBuilder(dt).build())
                    .forEach(domainTypes::add);

                scanResult.getClassesImplementing(Entity.class)
                    .filter(f -> !f.implementsInterface(AggregateRoot.class))
                    .filter(c -> !Entity.class.getName().equals(c.getName()))
                    .filter(c -> !AggregateRoot.class.getName().equals(c.getName()))
                    .stream()
                    .map(r -> (Class<? extends Entity<?>>) r.loadClass())
                    .map(dt -> new EntityMirrorBuilder(dt).build())
                    .forEach(domainTypes::add);

                scanResult.getClassesImplementing(AggregateRoot.class)
                    .stream()
                    .filter(c -> !AggregateRoot.class.getName().equals(c.getName()))
                    .map(r -> (Class<? extends AggregateRoot<?>>) r.loadClass())
                    .map(dt -> new AggregateRootMirrorBuilder(dt).build())
                    .forEach(domainTypes::add);

                scanResult.getClassesImplementing(DomainService.class)
                    .stream()
                    .filter(c -> !DomainService.class.getName().equals(c.getName()))
                    .map(r -> (Class<? extends DomainService>) r.loadClass())
                    .map(dt -> new DomainServiceMirrorBuilder(dt).build())
                    .forEach(domainTypes::add);

                scanResult.getClassesImplementing(ApplicationService.class)
                    .stream()
                    .filter(c -> !ApplicationService.class.getName().equals(c.getName()))
                    .map(r -> (Class<? extends ApplicationService>) r.loadClass())
                    .map(dt -> new ApplicationServiceMirrorBuilder(dt).build())
                    .forEach(domainTypes::add);

                scanResult.getClassesImplementing(DomainEvent.class)
                    .stream()
                    .filter(c -> !DomainEvent.class.getName().equals(c.getName()))
                    .map(r -> (Class<? extends DomainEvent>) r.loadClass())
                    .map(dt -> new DomainEventMirrorBuilder(dt).build())
                    .forEach(domainTypes::add);

                scanResult.getClassesImplementing(Repository.class)
                    .stream()
                    .filter(c -> !Repository.class.getName().equals(c.getName()))
                    .map(r -> (Class<? extends Repository<?, ?>>) r.loadClass())
                    .map(dt -> new RepositoryMirrorBuilder(dt).build())
                    .forEach(domainTypes::add);

                scanResult.getClassesImplementing(DomainCommand.class)
                    .stream()
                    .filter(c -> !DomainCommand.class.getName().equals(c.getName()))
                    .filter(c -> !AggregateCommand.class.getName().equals(c.getName()))
                    .filter(c -> !DomainServiceCommand.class.getName().equals(c.getName()))
                    .map(r -> (Class<? extends DomainCommand>) r.loadClass())
                    .map(dt -> new DomainCommandMirrorBuilder(dt).build())
                    .forEach(domainTypes::add);

                scanResult.getClassesImplementing(ReadModel.class)
                    .stream()
                    .filter(c -> !ReadModel.class.getName().equals(c.getName()))
                    .map(r -> (Class<? extends ReadModel>) r.loadClass())
                    .map(dt -> new ReadModelMirrorBuilder(dt).build())
                    .forEach(domainTypes::add);

                scanResult.getClassesImplementing(QueryHandler.class)
                    .stream()
                    .filter(c -> !QueryHandler.class.getName().equals(c.getName()))
                    .map(r -> (Class<? extends QueryHandler<?>>) r.loadClass())
                    .map(dt -> new QueryHandlerMirrorBuilder(dt).build())
                    .forEach(domainTypes::add);

                scanResult.getClassesImplementing(OutboundService.class)
                    .stream()
                    .filter(c -> !OutboundService.class.getName().equals(c.getName()))
                    .map(r -> (Class<? extends OutboundService>) r.loadClass())
                    .map(dt -> new OutboundServiceMirrorBuilder(dt).build())
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
                    .map(r -> (Class<? extends ServiceKind>) r.loadClass())
                    .map(dt -> new ServiceKindMirrorBuilder(dt).build())
                    .forEach(domainTypes::add);
            } catch (Throwable t) {
                log.error("Scanning bounded context package '{}' failed!", packageNames);
            }
        }
        return domainTypes;
    }

}
