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

import io.domainlifecycles.domain.types.ServiceKind;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
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
import io.domainlifecycles.domain.types.QueryClient;
import io.domainlifecycles.domain.types.ReadModel;
import io.domainlifecycles.domain.types.Repository;
import io.domainlifecycles.domain.types.ValueObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Scans the classpath for the classes of a given model package. Based on {@link io.github.classgraph}.
 *
 * @author Mario Herb
 */
public class DomainTypesScanner {

    private static final Logger log = LoggerFactory.getLogger(DomainTypesScanner.class);

    private static final Pattern packagePattern = Pattern.compile("^[a-z]+(\\.[a-zA-Z_][a-zA-Z0-9_]*)*$");

    private final List<Class<? extends Enum<?>>> enums;

    private final List<Class<? extends Identity<?>>> identities;

    private final List<Class<? extends ValueObject>> valueObjects;

    private final List<Class<? extends Entity<?>>> entities;

    private final List<Class<? extends AggregateRoot<?>>> aggregateRoots;

    private final List<Class<? extends DomainService>> domainServices;

    private final List<Class<? extends ApplicationService>> applicationServices;

    private final List<Class<? extends Repository<?, ?>>> repositories;

    private final List<Class<? extends DomainEvent>> domainEvents;

    private final List<Class<? extends DomainCommand>> domainCommands;

    private final List<Class<? extends ReadModel>> readModels;

    private final List<Class<? extends QueryClient<?>>> queryClients;

    private final List<Class<? extends OutboundService>> outboundServices;

    private final List<Class<? extends ServiceKind>> serviceKinds;


    /**
     * Constructor
     */
    public DomainTypesScanner() {
        enums = new ArrayList<>();
        identities = new ArrayList<>();
        valueObjects = new ArrayList<>();
        entities = new ArrayList<>();
        aggregateRoots = new ArrayList<>();
        domainServices = new ArrayList<>();
        applicationServices = new ArrayList<>();
        repositories = new ArrayList<>();
        domainEvents = new ArrayList<>();
        domainCommands = new ArrayList<>();
        readModels = new ArrayList<>();
        queryClients = new ArrayList<>();
        outboundServices = new ArrayList<>();
        serviceKinds = new ArrayList<>();
    }

    /**
     * Scans the given packages for Domain Model classes.
     *
     * @param boundedContextPackages the bounded context packages
     */
    @SuppressWarnings("unchecked")
    public void scan(String... boundedContextPackages) {
        for (String pack : boundedContextPackages) {
            if (isValidPackage(pack)) {
                log.info("Scanning bounded context package for domain types '{}'", pack);
                try (ScanResult scanResult =
                         new ClassGraph()
                             //.verbose()               // Log to stderr
                             .enableAllInfo()         // Scan classes, methods, fields, annotations
                             .acceptPackages(pack)     // Scan com.xyz and subpackages (omit to scan all packages)
                             .scan()) {               // Start the scan

                    enums.addAll(
                        scanResult.getAllEnums()
                            .stream()
                            .map(r -> (Class<? extends Enum<?>>) r.loadClass())
                            .toList()
                    );
                    identities.addAll(
                        scanResult.getClassesImplementing(Identity.class)
                            .stream()
                            .filter(c -> !Identity.class.getName().equals(c.getName()))
                            .map(r -> (Class<? extends Identity<?>>) r.loadClass())
                            .toList()
                    );
                    valueObjects.addAll(
                        scanResult.getClassesImplementing(ValueObject.class)
                            .stream()
                            .filter(c -> !ValueObject.class.getName().equals(c.getName()))
                            .map(r -> (Class<? extends ValueObject>) r.loadClass())
                            .toList()
                    );
                    entities.addAll(
                        scanResult.getClassesImplementing(Entity.class)
                            .filter(f -> !f.implementsInterface(AggregateRoot.class))
                            .filter(c -> !Entity.class.getName().equals(c.getName()))
                            .filter(c -> !AggregateRoot.class.getName().equals(c.getName()))
                            .stream()
                            .map(r -> (Class<? extends Entity<?>>) r.loadClass())
                            .toList()
                    );
                    aggregateRoots.addAll(
                        scanResult.getClassesImplementing(AggregateRoot.class)
                            .stream()
                            .filter(c -> !AggregateRoot.class.getName().equals(c.getName()))
                            .map(r -> (Class<? extends AggregateRoot<?>>) r.loadClass())
                            .toList()
                    );
                    domainServices.addAll(
                        scanResult.getClassesImplementing(DomainService.class)
                            .stream()
                            .filter(c -> !DomainService.class.getName().equals(c.getName()))
                            .map(r -> (Class<? extends DomainService>) r.loadClass())
                            .toList()
                    );
                    applicationServices.addAll(
                        scanResult.getClassesImplementing(ApplicationService.class)
                            .stream()
                            .filter(c -> !ApplicationService.class.getName().equals(c.getName()))
                            .map(r -> (Class<? extends ApplicationService>) r.loadClass())
                            .toList()
                    );
                    domainEvents.addAll(
                        scanResult.getClassesImplementing(DomainEvent.class)
                            .stream()
                            .filter(c -> !DomainEvent.class.getName().equals(c.getName()))
                            .map(r -> (Class<? extends DomainEvent>) r.loadClass())
                            .toList()
                    );
                    repositories.addAll(
                        scanResult.getClassesImplementing(Repository.class)
                            .stream()
                            .filter(c -> !Repository.class.getName().equals(c.getName()))
                            .map(r -> (Class<? extends Repository<?, ?>>) r.loadClass())
                            .toList()
                    );
                    domainCommands.addAll(
                        scanResult.getClassesImplementing(DomainCommand.class)
                            .stream()
                            .filter(c -> !DomainCommand.class.getName().equals(c.getName()))
                            .filter(c -> !AggregateCommand.class.getName().equals(c.getName()))
                            .filter(c -> !DomainServiceCommand.class.getName().equals(c.getName()))
                            .map(r -> (Class<? extends DomainCommand>) r.loadClass())
                            .toList()
                    );
                    readModels.addAll(
                        scanResult.getClassesImplementing(ReadModel.class)
                            .stream()
                            .filter(c -> !ReadModel.class.getName().equals(c.getName()))
                            .map(r -> (Class<? extends ReadModel>) r.loadClass())
                            .toList()
                    );
                    queryClients.addAll(
                        scanResult.getClassesImplementing(QueryClient.class)
                            .stream()
                            .filter(c -> !QueryClient.class.getName().equals(c.getName()))
                            .map(r -> (Class<? extends QueryClient<?>>) r.loadClass())
                            .toList()
                    );
                    outboundServices.addAll(
                        scanResult.getClassesImplementing(OutboundService.class)
                            .stream()
                            .filter(c -> !OutboundService.class.getName().equals(c.getName()))
                            .map(r -> (Class<? extends OutboundService>) r.loadClass())
                            .toList()
                    );

                    serviceKinds.addAll(
                        scanResult.getClassesImplementing(ServiceKind.class)
                            .stream()
                            .filter(c -> !ServiceKind.class.getName().equals(c.getName())
                                        && !ApplicationService.class.getName().equals(c.getName())
                                        && !DomainService.class.getName().equals(c.getName())
                                        && !OutboundService.class.getName().equals(c.getName())
                                        && !QueryClient.class.getName().equals(c.getName())
                                        && !Repository.class.getName().equals(c.getName()))
                            .filter(c -> !(c.implementsInterface(ApplicationService.class))
                                        && !(c.implementsInterface(DomainService.class))
                                        && !(c.implementsInterface(OutboundService.class))
                                        && !(c.implementsInterface(QueryClient.class))
                                        && !(c.implementsInterface(Repository.class)))
                            .map(r -> (Class<? extends ServiceKind>) r.loadClass())
                            .toList()
                    );

                } catch (Throwable t) {
                    log.error("Scanning bounded context package '{}' failed!", pack);
                }
            } else {
                log.error("Bounded context package is no valid package name'{}'!", pack);
            }
        }
    }

    /**
     * @return whether the given name is a valid full qualified package name.
     */
    private static boolean isValidPackage(final String packageName) {
        return packagePattern.matcher(packageName).matches();
    }

    /**
     * @return list of scanned {@link Enum} classes.
     */
    public List<Class<? extends Enum<?>>> getScannedEnums() {
        return enums;
    }

    /**
     * @return list of scanned {@link Identity} classes.
     */
    public List<Class<? extends Identity<?>>> getScannedIdentities() {
        return identities;
    }

    /**
     * @return list of scanned {@link ValueObject} classes.
     */
    public List<Class<? extends ValueObject>> getScannedValueObjects() {
        return valueObjects;
    }

    /**
     * @return the list of scanned {@link Entity} classes
     */
    public List<Class<? extends Entity<?>>> getScannedEntities() {
        return entities;
    }

    /**
     * @return the list of scanned {@link AggregateRoot} classes
     */
    public List<Class<? extends AggregateRoot<?>>> getScannedAggregateRoots() {
        return aggregateRoots;
    }

    /**
     * @return the list of scanned {@link DomainEvent} classes
     */
    public List<Class<? extends DomainEvent>> getScannedDomainEvents() {
        return domainEvents;
    }

    /**
     * @return the list of scanned {@link DomainService} classes
     */
    public List<Class<? extends DomainService>> getScannedDomainServices() {
        return domainServices;
    }

    /**
     * @return the list of scanned {@link ApplicationService} classes
     */
    public List<Class<? extends ApplicationService>> getScannedApplicationServices() {
        return applicationServices;
    }

    /**
     * @return the list of scanned {@link Repository} classes
     */
    public List<Class<? extends Repository<?, ?>>> getScannedRepositories() {
        return repositories;
    }

    /**
     * @return the list of scanned {@link DomainCommand} classes
     */
    public List<Class<? extends DomainCommand>> getScannedDomainCommands() {
        return domainCommands;
    }

    /**
     * @return the list of scanned {@link ReadModel} classes
     */
    public List<Class<? extends ReadModel>> getScannedReadModels() {
        return readModels;
    }

    /**
     * @return the list of scanned {@link QueryClient} classes
     */
    public List<Class<? extends QueryClient<?>>> getScannedQueryClients() {
        return queryClients;
    }

    /**
     * @return the list of scanned {@link OutboundService} classes
     */
    public List<Class<? extends OutboundService>> getScannedOutboundServices() {
        return outboundServices;
    }

    /**
     * @return the list of scanned {@link ServiceKind} classes
     */
    public List<Class<? extends ServiceKind>> getScannedServiceKinds() {
        return serviceKinds;
    }
}
