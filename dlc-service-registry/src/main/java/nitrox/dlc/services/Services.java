/*
 *
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

package nitrox.dlc.services;

import nitrox.dlc.domain.types.ApplicationService;
import nitrox.dlc.domain.types.DomainService;
import nitrox.dlc.domain.types.OutboundService;
import nitrox.dlc.domain.types.ReadModelProvider;
import nitrox.dlc.domain.types.Repository;
import nitrox.dlc.mirror.api.Domain;
import nitrox.dlc.reflect.JavaReflect;
import nitrox.dlc.services.api.ServiceProvider;
import nitrox.dlc.services.api.ServiceRegistrator;

import java.util.HashMap;
import java.util.Map;

/**
 * The Services class is responsible for registering and providing instances of various types of services.
 * It implements the ServiceRegistrator and ServiceProvider interfaces.
 *
 * @author Mario Herb
 */
public class Services implements ServiceRegistrator, ServiceProvider {

    private final Map<String, ApplicationService> applicationServices = new HashMap<>();
    private final Map<String, DomainService> domainServices = new HashMap<>();
    private final Map<String, Repository<?, ?>> repositories = new HashMap<>();

    private final Map<String, ReadModelProvider<?>> readModelProviders = new HashMap<>();

    private final Map<String, OutboundService> outboundServices  = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationService getApplicationServiceInstance(String typeName) {
        return applicationServices.get(typeName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Repository<?, ?> getRepositoryInstance(String typeName) {
        return repositories.get(typeName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomainService getDomainServiceInstance(String typeName) {
        return domainServices.get(typeName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReadModelProvider<?> getReadModelProviderInstance(String typeName) {
        return readModelProviders.get(typeName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OutboundService getOutboundServiceInstance(String typeName) {
        return outboundServices.get(typeName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerApplicationServiceInstance(ApplicationService applicationService) {
        //check of mirrored types is needed, because in some frameworks like spring the concrete service instances
        // are extended and so the class of the concrete instance at runtime is not known by the mirror
        JavaReflect.allSupertypes(applicationService.getClass(), true)
            .stream()
            .filter(superType -> Domain.typeMirror(superType.getName()).isPresent())
            .findFirst()
            .ifPresent(mirroredType -> applicationServices.put(mirroredType.getName(), applicationService));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerDomainServiceInstance(DomainService domainService) {
        //check of mirrored types is needed, because in some frameworks like spring the concrete service instances
        // are extended and so the class of the concrete instance at runtime is not known by the mirror
        JavaReflect.allSupertypes(domainService.getClass(), true)
            .stream()
            .filter(superType -> Domain.typeMirror(superType.getName()).isPresent())
            .findFirst()
            .ifPresent(mirroredType -> domainServices.put(mirroredType.getName(), domainService));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerRepositoryInstance(Repository<?, ?> repository) {
        //check of mirrored types is needed, because in some frameworks like spring the concrete service instances
        // are extended and so the class of the concrete instance at runtime is not known by the mirror
        JavaReflect.allSupertypes(repository.getClass(), true)
            .stream()
            .filter(superType -> Domain.typeMirror(superType.getName()).isPresent())
            .findFirst()
            .ifPresent(mirroredType -> repositories.put(mirroredType.getName(), repository));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerReadModelProviderInstance(ReadModelProvider<?> readModelProvider) {
        //check of mirrored types is needed, because in some frameworks like spring the concrete service instances
        // are extended and so the class of the concrete instance at runtime is not known by the mirror
        JavaReflect.allSupertypes(readModelProvider.getClass(), true)
            .stream()
            .filter(superType -> Domain.typeMirror(superType.getName()).isPresent())
            .findFirst()
            .ifPresent(mirroredType -> readModelProviders.put(mirroredType.getName(), readModelProvider));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerOutboundServiceInstance(OutboundService outboundService) {
        //check of mirrored types is needed, because in some frameworks like spring the concrete service instances
        // are extended and so the class of the concrete instance at runtime is not known by the mirror
        JavaReflect.allSupertypes(outboundService.getClass(), true)
            .stream()
            .filter(superType -> Domain.typeMirror(superType.getName()).isPresent())
            .findFirst()
            .ifPresent(mirroredType -> outboundServices.put(mirroredType.getName(), outboundService));
    }
}
