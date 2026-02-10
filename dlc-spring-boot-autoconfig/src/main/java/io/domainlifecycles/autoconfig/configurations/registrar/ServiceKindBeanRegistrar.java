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

package io.domainlifecycles.autoconfig.configurations.registrar;

import io.domainlifecycles.domain.types.ServiceKind;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.BeanRegistrar;
import org.springframework.beans.factory.BeanRegistry;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.Environment;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Modifier;
import java.util.List;

/**
 * A registrar that scans and registers all beans of type {@link ServiceKind}
 * from configured base packages into the Spring application context. This class
 * allows dynamic discovery and registration of service-like components.
 *
 * The base packages to scan for {@link ServiceKind} implementations are
 * specified via the configuration property `dlc.domain.base-packages`.
 * If no base packages are configured, registration is skipped.
 *
 * This registrar ensures that only concrete, non-abstract, and non-interface
 * classes implementing {@link ServiceKind} are registered as beans. Furthermore,
 * the fully qualified class name of the implementation is used as the bean name
 * during registration.
 *
 * @author Mario Herb
 */
public class ServiceKindBeanRegistrar implements BeanRegistrar {

    /**
     * Registers all concrete implementations of {@link ServiceKind} found in the base packages
     * specified in the application's environment configuration. This method scans the given
     * base packages to discover candidate components, filters them to include only
     * implementations of {@link ServiceKind}, and registers them as beans in the
     * given {@link BeanRegistry}.
     *
     * @param registry the {@link BeanRegistry} where discovered beans are registered.
     *                 The fully qualified class name of each implementation is used as the bean name.
     * @param env the {@link Environment} from which configuration properties, such as
     *            the list of base packages to scan (`dlc.domain.base-packages`), are retrieved.
     */
    @Override
    public void register(@NonNull BeanRegistry registry, @NonNull Environment env) {
        List<String> basePackages = Binder.get(env)
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
                registry.registerBean(beanName, implClass);
            }
        }
    }

}
