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

package io.domainlifecycles.services;

import io.domainlifecycles.domain.types.ApplicationService;
import io.domainlifecycles.domain.types.DomainService;
import io.domainlifecycles.domain.types.OutboundService;
import io.domainlifecycles.domain.types.QueryClient;
import io.domainlifecycles.domain.types.Repository;
import io.domainlifecycles.domain.types.ServiceKind;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.reflect.JavaReflect;
import io.domainlifecycles.services.api.ServiceProvider;
import io.domainlifecycles.services.api.ServiceRegistrator;

import io.domainlifecycles.services.exception.ServiceRegistryException;
import java.util.HashMap;
import java.util.Map;

/**
 * The Services class is responsible for registering and providing instances of various types of services.
 * It implements the ServiceRegistrator and ServiceProvider interfaces.
 *
 * @author Mario Herb
 */
public class Services implements ServiceRegistrator, ServiceProvider {

    private final Map<String, ServiceKind> serviceKinds = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationService getApplicationServiceInstance(String typeName) {
        ServiceKind foundServiceByTypeName = serviceKinds.get(typeName);

        if(!(foundServiceByTypeName instanceof ApplicationService)) {
            throw ServiceRegistryException.fail(String.format("No ApplicationService registered with name '%s'", typeName));
        }

        return (ApplicationService) foundServiceByTypeName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Repository<?, ?> getRepositoryInstance(String typeName) {
        ServiceKind foundServiceByTypeName = serviceKinds.get(typeName);

        if(!(foundServiceByTypeName instanceof Repository)) {
            throw ServiceRegistryException.fail(String.format("No Repository registered with name '%s'", typeName));
        }

        return (Repository<?, ?>) foundServiceByTypeName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomainService getDomainServiceInstance(String typeName) {
        ServiceKind foundServiceByTypeName = serviceKinds.get(typeName);

        if(!(foundServiceByTypeName instanceof DomainService)) {
            throw ServiceRegistryException.fail(String.format("No DomainService registered with name '%s'", typeName));
        }

        return (DomainService) foundServiceByTypeName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QueryClient<?> getQueryClientInstance(String typeName) {
        ServiceKind foundServiceByTypeName = serviceKinds.get(typeName);

        if(!(foundServiceByTypeName instanceof QueryClient)) {
            throw ServiceRegistryException.fail(String.format("No QueryClient registered with name '%s'", typeName));
        }

        return (QueryClient<?>) foundServiceByTypeName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OutboundService getOutboundServiceInstance(String typeName) {
        ServiceKind foundServiceByTypeName = serviceKinds.get(typeName);

        if(!(foundServiceByTypeName instanceof OutboundService)) {
            throw ServiceRegistryException.fail(String.format("No OutboundService registered with name '%s'", typeName));
        }

        return (OutboundService) foundServiceByTypeName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServiceKind getServiceKindInstance(String typeName) {
        return serviceKinds.get(typeName);
    }

    @Override
    public void registerServiceKindInstance(ServiceKind serviceKind) {
        //check of mirrored types is needed, because in some frameworks like spring the concrete service instances
        // are extended and so the class of the concrete instance at runtime is not known by the mirror
        JavaReflect.allSupertypes(serviceKind.getClass(), true)
            .stream()
            .filter(superType -> Domain.typeMirror(superType.getName()).isPresent())
            .findFirst()
            .ifPresent(mirroredType -> serviceKinds.put(mirroredType.getName(), serviceKind));
    }
}
