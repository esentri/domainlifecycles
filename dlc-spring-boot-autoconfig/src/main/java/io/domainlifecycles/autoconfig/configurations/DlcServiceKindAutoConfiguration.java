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
 *  Copyright 2019-2025 the original author or authors.
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

package io.domainlifecycles.autoconfig.configurations;

import io.domainlifecycles.autoconfig.exception.DLCAutoConfigException;
import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.api.ServiceKindMirror;
import java.beans.Introspector;
import java.lang.reflect.Modifier;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(after = {DlcDomainAutoConfiguration.class})
public class DlcServiceKindAutoConfiguration {

    /**
     * Publishes all {@link ServiceKindMirror}'s as a Singleton Bean.
     * The reason why {@link SmartInitializingSingleton} and not {@link BeanDefinitionRegistryPostProcessor} is used here,
     * is because it runs <b>after</b> all other Singletons have been created (i.e. Spring individual beans),
     * which allows for a hassle-free instantiation of the DLC mirror beans, since these often times require constructor
     * injection of other beans.
     *
     * @param domainMirror the domain mirror
     * @param beanFactory Spring bean factory
     * @param autowire autowire factory
     * @return a callback which instantiates all ServiceKind beans
     */
    @Bean
    @ConditionalOnBean(DomainMirror.class)
    public SmartInitializingSingleton serviceKindRegistrar(
        DomainMirror domainMirror,
        ConfigurableListableBeanFactory beanFactory,
        AutowireCapableBeanFactory autowire) {

        return () -> registerAllServiceKindBeans(domainMirror, beanFactory, autowire);
    }

    private void registerAllServiceKindBeans(DomainMirror domainMirror, ConfigurableListableBeanFactory beanFactory, AutowireCapableBeanFactory autowire) {
        domainMirror.getAllServiceKindMirrors().forEach(m -> registerServiceKindBean(beanFactory, autowire, m));
    }

    private void registerServiceKindBean(ConfigurableListableBeanFactory beanFactory, AutowireCapableBeanFactory autowire, ServiceKindMirror m) {
        Class<?> clazz;
        try {
            clazz = Class.forName(m.getTypeName());
        } catch (ClassNotFoundException e) {
            throw DLCAutoConfigException.fail(String.format("Mirror %s class not found.", m.getTypeName()), e);
        }

        if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())
            || clazz.equals(JooqAggregateRepository.class)) return;

        Object instance = autowire.createBean(clazz);
        String beanName = Introspector.decapitalize(clazz.getSimpleName());

        if (!beanFactory.containsSingleton(beanName)) {
            beanFactory.registerSingleton(beanName, instance);
        }
    }
}
