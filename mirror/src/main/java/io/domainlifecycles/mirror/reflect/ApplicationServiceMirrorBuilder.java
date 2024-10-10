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

package io.domainlifecycles.mirror.reflect;

import io.domainlifecycles.domain.types.AggregateRoot;
import io.domainlifecycles.domain.types.ApplicationService;
import io.domainlifecycles.domain.types.DomainService;
import io.domainlifecycles.domain.types.Driver;
import io.domainlifecycles.domain.types.OutboundService;
import io.domainlifecycles.domain.types.QueryClient;
import io.domainlifecycles.domain.types.Repository;
import io.domainlifecycles.mirror.api.ApplicationServiceMirror;
import io.domainlifecycles.mirror.model.ApplicationServiceModel;
import io.domainlifecycles.reflect.JavaReflect;
import io.domainlifecycles.reflect.MemberSelect;

import java.util.Arrays;
import java.util.List;

/**
 * Builder to create {@link ApplicationServiceMirror}. Uses Java reflection.
 *
 * @author Mario Herb
 */
public class ApplicationServiceMirrorBuilder extends DomainTypeMirrorBuilder {
    private final Class<? extends ApplicationService> applicationServiceClass;

    /**
     * Initialize the builder with the corresponding {@link AggregateRoot} class.
     *
     * @param applicationServiceClass the application service type
     */
    public ApplicationServiceMirrorBuilder(Class<? extends ApplicationService> applicationServiceClass) {
        super(applicationServiceClass);
        this.applicationServiceClass = applicationServiceClass;
    }

    /**
     * Builds a new {@link ApplicationServiceMirror} instance by reflectively analyzing the applicationServiceClass.
     *
     * @return new instance of ApplicationServiceMirror
     */
    public ApplicationServiceMirror build() {
        return new ApplicationServiceModel(
            getTypeName(),
            isAbstract(),
            buildFields(),
            buildMethods(),
            applicationServiceInterfaceTypeNames(),
            buildInheritanceHierarchy(),
            buildInterfaceTypes()
        );
    }

    private List<String> getReferencedRepositoryNames() {
        return JavaReflect
            .fields(this.applicationServiceClass, MemberSelect.HIERARCHY)
            .stream()
            .filter(f -> isRepository(f.getType()))
            .map(f -> f.getType().getName())
            .toList();
    }

    private List<String> getReferencedDomainServiceNames() {
        return JavaReflect
            .fields(this.applicationServiceClass, MemberSelect.HIERARCHY)
            .stream()
            .filter(f -> isDomainService(f.getType()))
            .map(f -> f.getType().getName())
            .toList();
    }

    private List<String> getReferencedOutboundServiceNames() {
        return JavaReflect
            .fields(this.applicationServiceClass, MemberSelect.HIERARCHY)
            .stream()
            .filter(f -> isOutboundService(f.getType()))
            .map(f -> f.getType().getName())
            .toList();
    }

    private List<String> getReferencedQueryClientNames() {
        return JavaReflect
            .fields(this.applicationServiceClass, MemberSelect.HIERARCHY)
            .stream()
            .filter(f -> isQueryClientProvider(f.getType()))
            .map(f -> f.getType().getName())
            .toList();
    }

    private boolean isRepository(Class<?> fieldClass) {
        return Repository.class.isAssignableFrom(fieldClass);
    }

    private boolean isDomainService(Class<?> fieldClass) {
        return DomainService.class.isAssignableFrom(fieldClass);
    }

    private boolean isOutboundService(Class<?> fieldClass) {
        return OutboundService.class.isAssignableFrom(fieldClass);
    }

    private boolean isQueryClientProvider(Class<?> fieldClass) {
        return QueryClient.class.isAssignableFrom(fieldClass);
    }

    private List<String> applicationServiceInterfaceTypeNames() {
        return Arrays.stream(applicationServiceClass.getInterfaces())
            .filter(i -> (ApplicationService.class.isAssignableFrom(i) ||
                Driver.class.isAssignableFrom(i))
                && !i.getName().equals(ApplicationService.class.getName())
                && !i.getName().equals(Driver.class.getName())
            )
            .map(Class::getName)
            .toList();
    }
}
