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

package io.domainlifecycles.events.spring.listeners;

import io.domainlifecycles.domain.types.AggregateDomainEvent;
import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.domain.types.DomainEventListener;
import io.domainlifecycles.domain.types.ListensTo;
import io.domainlifecycles.domain.types.ServiceKind;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.modifier.FieldManifestation;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.MethodCall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.PriorityOrdered;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * A {@code BeanDefinitionRegistryPostProcessor} implementation designed to scan for service-kind
 * beans and their associated listeners during the Spring container lifecycle.
 * This class facilitates the dynamic registration of listener beans for methods annotated with
 * custom listener-related annotations (@see DomainEventListener).
 *
 * It also implements {@code ApplicationContextAware} to gain access to the Spring application
 * context and {@code PriorityOrdered} to establish its execution precedence.
 */
public class ServiceKindListenerPostProcessor implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware, PriorityOrdered {

    /**
     * A constant representing the field name used to identify the target
     * for the listener or service processing.
     */
    public static final String TARGET_FIELD = "target";

    /**
     * A constant representing the property name "propagation" in the context of the
     * {@code ServiceKindListenerPostProcessor} class.
     */
    public static final String PROPAGATION_PROPERTY = "propagation";

    private final Logger log = LoggerFactory.getLogger(ServiceKindListenerPostProcessor.class);

    private BeanDefinitionRegistry registry;
    private ConfigurableApplicationContext context;

    /**
     * Sets the application context for the current instance. This method is invoked
     * when the ApplicationContext is available and allows the instance to interact
     * with the Spring container by holding a reference to the application context.
     *
     * @param applicationContext the ApplicationContext instance to set, typically
     *                           provided by the Spring framework at runtime. Must
     *                           not be null.
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.context = (ConfigurableApplicationContext) applicationContext;
    }

    /**
     * Post-processes the BeanDefinitionRegistry within the Spring context. This method allows
     * modifications, additions, or removal of bean definitions before the BeanFactory is created
     * and the beans are instantiated.
     *
     * @param registry the BeanDefinitionRegistry instance being processed, enabling bean
     *                 definitions to be registered or manipulated. Must not be null.
     * @throws BeansException if an error occurs during the post-processing of the registry.
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        this.registry = registry;
    }

    /**
     * Post-processes the given {@code ConfigurableListableBeanFactory} after its standard initialization.
     * This method scans for beans of type {@code ServiceKind}, logs their detection,
     * and initiates a listener registration process for each matching bean.
     *
     * @param beanFactory the {@code ConfigurableListableBeanFactory} instance being processed. This allows
     *                    access to all bean definitions and enables custom modifications or additional
     *                    registrations during the post-processing phase.
     * @throws BeansException if an error occurs during the processing of the bean factory.
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (String beanName : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition def = beanFactory.getBeanDefinition(beanName);

            if (isServiceKind(beanName, def, beanFactory)) {
                log.debug("Found ServiceKind bean {}. Scanning for listeners.", beanName);
                scanAndRegisterListeners(beanName, beanFactory);
            }
        }
    }

    boolean isServiceKind(String beanName, BeanDefinition bd, BeanFactory beanFactory) {
        if (bd.getRole() != BeanDefinition.ROLE_APPLICATION) {
            return false;
        }

        Class<?> type = beanFactory.getType(beanName, false);
        if (type != null) {
            return ServiceKind.class.isAssignableFrom(type);
        }

        type = bd.getResolvableType().getRawClass();
        if (type != null) {
            return ServiceKind.class.isAssignableFrom(type);
        }

        String className = bd.getBeanClassName();
        if (className == null) return false;

        try {
            Class<?> raw = ClassUtils.forName(className, null);
            return ServiceKind.class.isAssignableFrom(raw);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private void scanAndRegisterListeners(
        String beanName,
        BeanFactory beanFactory
    ) {
        Class<?> beanType = beanFactory.getType(beanName, false);
        if (beanType == null) return;

        Class<?> userType = ClassUtils.getUserClass(beanType);

        Set<ListenerSignature> seen = new HashSet<>();

        for (Method m : userType.getMethods()) {
            if ((!m.isAnnotationPresent(ListensTo.class)
                && !m.isAnnotationPresent(DomainEventListener.class))
                || m.isAnnotationPresent(EventListener.class)
                || m.isAnnotationPresent(TransactionalEventListener.class)
                || Arrays.stream(m.getAnnotations()).anyMatch(
                    a -> a.annotationType()
                        .getName()
                        .equals("org.springframework.modulith.events.ApplicationModuleListener")
                )
            ) {
                log.trace("Skipping method {} on bean {}. Not a listener. Not annotated adequately.",
                    m.getName(), beanName);
                continue;
            }

            if (m.getParameterCount() != 1) {
                log.trace("Skipping method {} on bean {}. Not a listener. Parameter count not 1.",
                    m.getName(), beanName);
                continue;
            }

            Class<? extends DomainEvent> eventType = (Class<? extends DomainEvent>)m.getParameterTypes()[0];

            if(AggregateDomainEvent.class.isAssignableFrom(eventType)){
                log.trace("Skipping method {} on bean {}. Not a listener. AggregateDomainEvent.",
                    m.getName(), beanName);
                continue;
            }

            ListenerSignature sig = new ListenerSignature(m.getName(), eventType);
            if (!seen.add(sig)) continue; // no double registration

            registerListenerBean(beanName, userType, m, eventType, registry);
        }
    }

    private void registerListenerBean(
        String targetBeanName,
        Class<?> targetBeanType,
        Method method,
        Class<? extends DomainEvent> eventType,
        BeanDefinitionRegistry registry
    ) {

        Class<?> generated = createProxy(targetBeanType, method, eventType);

        RootBeanDefinition bd = new RootBeanDefinition(generated);
        bd.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        String name = targetBeanName + "_" + method.getName() + "_" + eventType.getSimpleName() + "$Proxy-" + UUID.randomUUID();
        registry.registerBeanDefinition(name, bd);
        log.debug("Registered listener bean {} for method {} on bean {}.", name, method.getName(), targetBeanName);
    }

    /**
     * Returns the order value of this post-processor, indicating the priority
     * with which it should be executed relative to other post-processors
     * within the Spring container lifecycle.
     *
     * @return the lowest precedence value, ensuring that this post-processor
     *         is executed after others with higher precedence.
     */
    @Override
    public int getOrder() {
        return PriorityOrdered.LOWEST_PRECEDENCE;
    }

    record ListenerSignature(String methodName, Class<?> eventType) {}

    private Class<?> createProxy(Class<?> targetBeanType, Method m, Class<? extends DomainEvent> eventType){
        Class<?> proxyType =
            null;
        try {
            proxyType = new ByteBuddy()
                .subclass(Object.class)
                .name(targetBeanType.getSimpleName() + "_" + m.getName() + "_" + eventType.getSimpleName() + "__Proxy" + UUID.randomUUID().toString().replace('-', '_'))
                // private final ListenerClass target;
                .defineField(TARGET_FIELD, targetBeanType, Visibility.PRIVATE, FieldManifestation.FINAL)
                // public Proxy(ListenerClass target) { this.target = target; }
                .defineConstructor(Visibility.PUBLIC)
                .withParameter(targetBeanType)
                .intercept(
                    MethodCall.invoke(Object.class.getConstructor())
                        .andThen(FieldAccessor.ofField(TARGET_FIELD).setsArgumentAt(0))
                )
                .annotateMethod(
                    AnnotationDescription.Builder.ofType(Autowired.class).build()
                )
                // -> target.listener(event)
                .defineMethod(m.getName(), void.class, Visibility.PUBLIC)
                .withParameter(eventType)
                .intercept(
                    MethodCall.invoke(
                        m
                    ).onField(TARGET_FIELD).withAllArguments()
                )
                .annotateMethod(
                    AnnotationDescription.Builder.ofType(TransactionalEventListener.class).build(),
                    AnnotationDescription.Builder.ofType(Transactional.class)
                        .define(PROPAGATION_PROPERTY, Propagation.REQUIRES_NEW).build(),
                    AnnotationDescription.Builder.ofType(Async.class).build()
                )
                .make()
                .load(
                    context.getClassLoader(),
                    ClassLoadingStrategy.Default.INJECTION
                )
                .getLoaded();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return proxyType;
    }

}

