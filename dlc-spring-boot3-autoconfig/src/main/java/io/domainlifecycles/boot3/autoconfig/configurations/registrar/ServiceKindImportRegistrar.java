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

package io.domainlifecycles.boot3.autoconfig.configurations.registrar;

import io.domainlifecycles.domain.types.ServiceKind;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AssignableTypeFilter;

import org.springframework.util.ClassUtils;

import java.lang.reflect.Modifier;
import java.util.List;

/**
 * A registrar that facilitates the dynamic discovery and registration of beans
 * implementing the {@code ServiceKind} marker interface from specified base packages.
 * These services are dynamically registered into the Spring container during application startup.
 *
 * @author Mario Herb
 */
@Deprecated
public class ServiceKindImportRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private Environment environment;

    /**
     * Dynamically registers bean definitions for classes implementing the {@code ServiceKind}
     * marker interface found within the specified base packages. The base packages are
     * retrieved from configuration properties.
     *
     * @param importingClassMetadata metadata of the importing class during the
     *                               {@code @Import} processing phase.
     * @param registry               a registry object to register the discovered
     *                               bean definitions.
     */
    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata,
                                        @NonNull BeanDefinitionRegistry registry) {
        List<String> basePackages = Binder.get(environment)
            .bind("dlc.domain.base-packages", Bindable.listOf(String.class))
            .orElse(List.of());

        if (basePackages.isEmpty()) {
            return;
        }

        var scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AssignableTypeFilter(ServiceKind.class));

        for (String basePackage : basePackages) {
            for (var candidate : scanner.findCandidateComponents(basePackage)) {

                String className = candidate.getBeanClassName();
                if (className == null) continue;

                Class<?> implClass = ClassUtils.resolveClassName(className, ClassUtils.getDefaultClassLoader());

                // Skip: Interface/Abstract Class
                if (implClass.isInterface() || Modifier.isAbstract(implClass.getModifiers())) {
                    continue;
                }

                String beanName = implClass.getName();
                var bd = BeanDefinitionBuilder.genericBeanDefinition(implClass).getBeanDefinition();
                registry.registerBeanDefinition(beanName, bd);
            }
        }
    }

    /**
     * Sets the {@link Environment} object for this class. The environment provides access to
     * properties, profiles, and configuration settings that may be required during the dynamic
     * registration of bean definitions.
     *
     * @param environment the {@link Environment} instance used to access environment-specific
     *                    properties and configuration data.
     */
    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.environment = environment;
    }
}
