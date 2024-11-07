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

package io.domainlifecycles.services;

import io.domainlifecycles.domain.types.ServiceKind;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.reflect.JavaReflect;
import io.domainlifecycles.services.api.ServiceProvider;
import io.domainlifecycles.services.api.ServiceRegistrator;
import io.domainlifecycles.services.exception.ServiceRegistryException;

import java.util.Collection;
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

    public Services() {
    }

    public Services(Collection<ServiceKind> serviceKinds) {
        registerServiceKindInstances(serviceKinds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends ServiceKind> S getServiceKindInstance(String typeName) {
        var s = (S)serviceKinds.get(typeName);
        if (s == null){
            throw ServiceRegistryException.fail("ServiceKindInstance with type '%s' not found", typeName);
        }
        return s;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerServiceKindInstances(Collection<ServiceKind> serviceKinds) {
        serviceKinds.forEach(this::registerServiceKindInstance);
    }
}
