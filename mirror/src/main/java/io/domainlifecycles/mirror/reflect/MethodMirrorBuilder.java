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
import io.domainlifecycles.domain.types.ListensTo;
import io.domainlifecycles.domain.types.Publishes;
import io.domainlifecycles.mirror.api.AccessLevel;
import io.domainlifecycles.mirror.api.AssertedContainableTypeMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.api.ParamMirror;
import io.domainlifecycles.mirror.api.ResolvedGenericTypeMirror;
import io.domainlifecycles.mirror.model.MethodModel;
import io.domainlifecycles.mirror.model.ParamModel;
import io.domainlifecycles.mirror.resolver.GenericTypeResolver;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Builder to create {@link MethodMirror}. Uses Java reflection.
 *
 * @author Mario Herb
 */
public class MethodMirrorBuilder {
    private final Method m;

    private final Class<?> topLevelClass;

    private final boolean overridden;

    private final GenericTypeResolver genericTypeResolver;

    /**
     * Constructor
     *
     * @param m Method being mirrored
     * @param topLevelClass most specific class containing this method
     * @param overridden boolean stating the fact, if the method is overridden by the given top-level class
     * @param genericTypeResolver type Resolver implementation, that resolves generics and type arguments
     */
    public MethodMirrorBuilder(Method m, Class<?> topLevelClass, boolean overridden, GenericTypeResolver genericTypeResolver) {
        this.m = Objects.requireNonNull(m);
        this.topLevelClass = Objects.requireNonNull(topLevelClass, "The corresponding top level class cannot be null!");
        this.overridden = overridden;
        this.genericTypeResolver = Objects.requireNonNull(genericTypeResolver, "The generic type resolver cannot be null!");
    }

    /**
     * Creates a new {@link MethodMirror}.
     *
     * @return new instance of method mirror
     */
    public MethodMirror build() {
        return new MethodModel(
            m.getName(),
            m.getDeclaringClass().getName(),
            AccessLevel.of(m),
            getParameters(),
            getReturnType(),
            overridden,
            publishedEventTypeNames(),
            listenedEventTypeName()
        );
    }

    private AssertedContainableTypeMirror getReturnType() {
        var builder = new AssertedContainableTypeMirrorBuilder(
            m.getReturnType(),
            m.getAnnotatedReturnType(),
            m.getGenericReturnType(),
            genericTypeResolver.resolveExecutableReturnType(m, topLevelClass));
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
                resolved
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
        return Optional.empty();
    }


}
