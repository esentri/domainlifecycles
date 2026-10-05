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

import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.domain.types.DomainEventListener;
import io.domainlifecycles.domain.types.FactoryMethod;
import io.domainlifecycles.domain.types.ListensTo;
import io.domainlifecycles.domain.types.Publishes;
import io.domainlifecycles.mirror.api.AccessLevel;
import io.domainlifecycles.mirror.api.AssertedContainableTypeMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.api.ParamMirror;
import io.domainlifecycles.mirror.api.ResolvedGenericTypeMirror;
import io.domainlifecycles.mirror.model.MethodModel;
import io.domainlifecycles.mirror.model.ParamModel;
import io.domainlifecycles.mirror.resolver.GenericTypeResolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Builder to create {@link MethodMirror}. Uses Java reflection.
 *
 * @author Mario Herb
 */
public class MethodMirrorBuilder {

    /**
     * The event listener annotations of Spring and Spring Modulith, by name - see {@link #isFrameworkEventListener}.
     */
    static final Set<String> FRAMEWORK_EVENT_LISTENER_ANNOTATIONS = Set.of(
        "org.springframework.context.event.EventListener",
        "org.springframework.transaction.event.TransactionalEventListener",
        "org.springframework.modulith.events.ApplicationModuleListener");

    private final Method m;

    private final Class<?> topLevelClass;

    private final boolean overridden;

    private final GenericTypeResolver genericTypeResolver;

    private final DomainTypeDetector domainTypeDetector;

    private final boolean declaredInFactory;

    /**
     * The domain types a factory creates.
     */
    private static final Set<DomainType> DOMAIN_OBJECT_TYPES = EnumSet.of(
        DomainType.AGGREGATE_ROOT,
        DomainType.ENTITY,
        DomainType.VALUE_OBJECT,
        DomainType.READ_MODEL,
        DomainType.DOMAIN_COMMAND,
        DomainType.DOMAIN_EVENT
    );

    /**
     * Constructor
     *
     * @param m Method being mirrored
     * @param topLevelClass most specific class containing this method
     * @param overridden boolean stating the fact, if the method is overridden by the given top-level class
     * @param genericTypeResolver type Resolver implementation, that resolves generics and type arguments
     * @param domainTypeDetector the domain type detector used
     */
    public MethodMirrorBuilder(Method m, Class<?> topLevelClass, boolean overridden,
                               GenericTypeResolver genericTypeResolver, DomainTypeDetector domainTypeDetector) {
        this(m, topLevelClass, overridden, genericTypeResolver, domainTypeDetector, false);
    }

    /**
     * Constructor
     *
     * @param m                   the method to mirror
     * @param topLevelClass       the class the method is mirrored for
     * @param overridden          whether the method is overridden
     * @param genericTypeResolver type resolver implementation, that resolves generics and type arguments
     * @param domainTypeDetector  domain type detector implementation, that detects domain types
     * @param declaredInFactory   whether the class is a factory, whose public methods returning a domain object are
     *                            factory methods
     */
    public MethodMirrorBuilder(Method m, Class<?> topLevelClass, boolean overridden,
                               GenericTypeResolver genericTypeResolver, DomainTypeDetector domainTypeDetector,
                               boolean declaredInFactory) {
        this.declaredInFactory = declaredInFactory;
        this.m = Objects.requireNonNull(m);
        this.topLevelClass = Objects.requireNonNull(topLevelClass, "The corresponding top level class cannot be null!");
        this.overridden = overridden;
        this.domainTypeDetector = Objects.requireNonNull(domainTypeDetector, "A domain type detector must be provided!");
        this.genericTypeResolver = Objects.requireNonNull(genericTypeResolver, "The generic type resolver cannot be null!");
    }

    /**
     * Creates a new {@link MethodMirror}.
     *
     * @return new instance of method mirror
     */
    public MethodMirror build() {
        var returnType = getReturnType();
        return new MethodModel(
            m.getName(),
            m.getDeclaringClass().getName(),
            AccessLevel.of(m),
            getParameters(),
            returnType,
            overridden,
            publishedEventTypeNames(),
            listenedEventTypeName(),
            isFactoryMethod(returnType)
        );
    }

    /**
     * A method marked as {@link FactoryMethod}, or a public method of a factory returning a domain object - a builder
     * is no factory method.
     */
    private boolean isFactoryMethod(AssertedContainableTypeMirror returnType) {
        if (m.isAnnotationPresent(FactoryMethod.class)) {
            return true;
        }
        return declaredInFactory
            && Modifier.isPublic(m.getModifiers())
            && m.getDeclaringClass() != Object.class
            && DOMAIN_OBJECT_TYPES.contains(returnType.getDomainType());
    }

    private AssertedContainableTypeMirror getReturnType() {
        var builder = new AssertedContainableTypeMirrorBuilder(
            m.getReturnType(),
            m.getAnnotatedReturnType(),
            m.getGenericReturnType(),
            genericTypeResolver.resolveExecutableReturnType(m, topLevelClass),
            domainTypeDetector);
        return builder.build();
    }

    private List<ParamMirror> getParameters() {
        var resolvedParameters = genericTypeResolver.resolveExecutableParameters(m, topLevelClass);
        List<ParamMirror> mirroredParams = new ArrayList<>();
        int i = 0;
        for (Parameter p : m.getParameters()) {
            ResolvedGenericTypeMirror resolved = null;
            if (resolvedParameters != null) {
                resolved = resolvedParameters.get(i);
            }
            var typeMirrorBuilder = new AssertedContainableTypeMirrorBuilder(
                p.getType(),
                p.getAnnotatedType(),
                p.getParameterizedType(),
                resolved,
                domainTypeDetector
            );
            mirroredParams.add(new ParamModel(p.getName(), typeMirrorBuilder.build()));
            i++;
        }
        return mirroredParams;
    }

    private List<String> publishedEventTypeNames() {
        var publishesAnnotation = m.getAnnotation(Publishes.class);
        if (publishesAnnotation != null && publishesAnnotation.domainEventTypes() != null) {
            return Arrays.stream(publishesAnnotation.domainEventTypes()).map(Class::getName).toList();
        }
        return Collections.emptyList();
    }

    private Optional<String> listenedEventTypeName() {
        var listensAnnotation = m.getAnnotation(ListensTo.class);
        var domainEventListenerAnnotation = m.getAnnotation(DomainEventListener.class);
        var domainEventTypeName = Arrays.stream(m.getParameters())
            .filter(p -> DomainEvent.class.isAssignableFrom(p.getType()))
            .findFirst()
            .map(p -> p.getType().getName())
            .orElse(null);
        if (domainEventListenerAnnotation != null && domainEventTypeName != null) {
            return Optional.of(domainEventTypeName);
        }
        if (listensAnnotation != null && domainEventTypeName != null) {
            return Optional.of(domainEventTypeName);
        }
        if (isFrameworkEventListener(m)) {
            return domainEventTypeName != null
                ? Optional.of(domainEventTypeName)
                : domainEventTypeNamedByAnnotation(m);
        }
        return Optional.empty();
    }

    /**
     * Whether the method is an event listener of Spring or Spring Modulith - {@code @EventListener},
     * {@code @TransactionalEventListener}, {@code @ApplicationModuleListener} or an own annotation composed of one of
     * them. The annotations are recognized by name, so DLC does not depend on Spring; annotations whose class is not on
     * the classpath are not visible via reflection anyway.
     */
    static boolean isFrameworkEventListener(Method method) {
        return Arrays.stream(method.getAnnotations())
            .anyMatch(annotation -> isEventListenerAnnotation(annotation.annotationType(), new HashSet<>()));
    }

    private static boolean isEventListenerAnnotation(Class<? extends Annotation> annotationType,
                                                     Set<Class<? extends Annotation>> visited) {
        if (!visited.add(annotationType)) {
            return false;
        }
        if (FRAMEWORK_EVENT_LISTENER_ANNOTATIONS.contains(annotationType.getName())) {
            return true;
        }
        return Arrays.stream(annotationType.getAnnotations())
            .map(Annotation::annotationType)
            .filter(meta -> !meta.getName().startsWith("java.lang.annotation."))
            .anyMatch(meta -> isEventListenerAnnotation(meta, visited));
    }

    /**
     * Spring listeners may name the event in the annotation ({@code classes} or {@code value}) instead of taking it
     * as a parameter. Only a single domain event is taken, since a mirrored method listens to one event.
     */
    private static Optional<String> domainEventTypeNamedByAnnotation(Method method) {
        for (Annotation annotation : method.getAnnotations()) {
            for (String attribute : List.of("classes", "value")) {
                try {
                    Object value = annotation.annotationType().getMethod(attribute).invoke(annotation);
                    if (value instanceof Class<?>[] classes) {
                        List<Class<?>> domainEvents = Arrays.stream(classes)
                            .filter(DomainEvent.class::isAssignableFrom)
                            .toList();
                        if (domainEvents.size() == 1) {
                            return Optional.of(domainEvents.get(0).getName());
                        }
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    // the annotation has no such attribute
                }
            }
        }
        return Optional.empty();
    }


}
