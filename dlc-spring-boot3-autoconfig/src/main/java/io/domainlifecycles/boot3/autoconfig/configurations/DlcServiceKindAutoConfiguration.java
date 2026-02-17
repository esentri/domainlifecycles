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

package io.domainlifecycles.boot3.autoconfig.configurations;

import io.domainlifecycles.domain.types.ServiceKind;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.Environment;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Autoconfiguration class for enabling service kind bean registration.
 *
 * @author Mario Herb
 */
@AutoConfiguration(after = {
    DlcDomainAutoConfiguration.class,
    DlcJooqPersistenceAutoConfiguration.class})
@ConditionalOnProperty(prefix = "dlc.features.servicekinds", name = "enabled", havingValue = "true", matchIfMissing = true)
@Deprecated
public class DlcServiceKindAutoConfiguration {

    @Bean
    static ServiceKindTwoPassPostProcessor serviceKindTwoPassPostProcessor() {
        return new ServiceKindTwoPassPostProcessor();
    }

    static final class ServiceKindTwoPassPostProcessor
        implements BeanDefinitionRegistryPostProcessor, EnvironmentAware, PriorityOrdered {

        private Environment environment;

        /**
         * A collection of class names that are considered as candidates.
         */
        private final Set<String> candidateClassNames = new LinkedHashSet<>();

        private BeanDefinitionRegistry registry;

        /**
         * Retrieves the order value of this object for sorting purposes.
         *
         * @return the lowest precedence value -1, indicating that this should be processed
         *         later than others with higher precedence values.
         *         But Spring Bus processing should run afterwards.
         */
        @Override
        public int getOrder() {
            return Ordered.LOWEST_PRECEDENCE-1;
        }

        /**
         * Sets the {@code Environment} instance for this object. This method is typically used
         * in the Spring Framework to inject the {@code Environment} during the initialization phase,
         * enabling access to environment-specific properties and profiles.
         *
         * @param environment the {@code Environment} instance to be set. Must not be null.
         *                    Provides access to property sources and active profiles.
         */
        @Override
        public void setEnvironment(@NonNull Environment environment) {
            this.environment = environment;
        }

        /**
         * Processes the BeanDefinitionRegistry to scan for candidate components that implement or extend
         * the {@link ServiceKind} interface. Identifies and registers class names of valid implementation
         * classes (non-abstract, non-interface) to be further processed or managed within the application
         * context.
         *
         * @param registry the BeanDefinitionRegistry used to register or modify bean definitions during
         *                 application context initialization. Must not be null.
         * @throws BeansException in case of errors during the processing of the BeanDefinitionRegistry.
         */
        @Override
        public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
            this.registry = registry;

            List<String> packages = Binder.get(environment)
                .bind("dlc.features.servicekinds.packages", Bindable.listOf(String.class))
                .orElse(Binder.get(environment)
                    .bind("dlc.features.mirror.base-packages", Bindable.listOf(String.class))
                    .orElse(List.of()));

            if (packages.isEmpty()) return;

            var scanner = new ClassPathScanningCandidateComponentProvider(false);
            scanner.addIncludeFilter(new AssignableTypeFilter(ServiceKind.class));

            ClassLoader cl = ClassUtils.getDefaultClassLoader();

            for (String pack : packages) {
                for (var candidate : scanner.findCandidateComponents(pack)) {
                    String className = candidate.getBeanClassName();
                    if (className == null) continue;

                    Class<?> implClass = ClassUtils.resolveClassName(className, cl);

                    if (implClass.isInterface() || Modifier.isAbstract(implClass.getModifiers())) {
                        continue;
                    }

                    candidateClassNames.add(implClass.getName());
                }
            }
        }

        /**
         * Processes the provided {@code ConfigurableListableBeanFactory} to dynamically register
         * bean definitions for classes whose fully qualified names are listed in {@code candidateClassNames}.
         * This method checks whether each candidate class is already instantiated or registered within
         * the factory before registering it, ensuring no duplicates are created.
         *
         * @param beanFactory the factory used to manage bean definitions in the application context.
         *                    Must not be null.
         * @throws BeansException in case of errors during the processing of the bean factory.
         */
        @Override
        public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
            if (candidateClassNames.isEmpty() || registry == null) return;

            ClassLoader cl = beanFactory.getBeanClassLoader();
            if (cl == null) cl = ClassUtils.getDefaultClassLoader();

            for (String implClassName : candidateClassNames) {
                Class<?> implClass = ClassUtils.resolveClassName(implClassName, cl);

                String[] existing = beanFactory.getBeanNamesForType(implClass, true, false);
                if (existing != null && existing.length > 0) {
                    continue; // The user (or someone else) has already defined this implementation as a bean
                }

                String beanName = implClass.getName();
                if (registry.containsBeanDefinition(beanName) || beanFactory.containsBean(beanName)) {
                    continue;
                }

                RootBeanDefinition bd = new RootBeanDefinition(implClass);
                registry.registerBeanDefinition(beanName, bd);
            }
        }
    }
}
