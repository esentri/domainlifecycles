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

import io.domainlifecycles.mirror.api.AssertedContainableTypeMirror;
import io.domainlifecycles.mirror.api.AssertionMirror;
import io.domainlifecycles.mirror.api.ResolvedGenericTypeMirror;
import io.domainlifecycles.mirror.model.AssertedContainableTypeModel;
import io.domainlifecycles.reflect.JavaReflect;

import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Builder to create {@link AssertedContainableTypeMirror}. Uses Java reflection.
 *
 * @author Mario Herb
 */
public class AssertedContainableTypeMirrorBuilder {
    private final Class<?> type;
    private final AnnotatedType annotatedType;
    private final Type genericType;
    private final ResolvedGenericTypeMirror resolvedGenericType;
    private final DomainTypeDetector domainTypeDetector;


    /**
     * Initialize the builder with the corresponding class, annotated and generic type information.
     *
     * @param type                class to mirror
     * @param annotatedType       the annotated type
     * @param genericType         the generic type
     * @param resolvedGenericType the resolved generic type
     * @param domainTypeDetector  the used domain type detector
     */
    public AssertedContainableTypeMirrorBuilder(Class<?> type, AnnotatedType annotatedType, Type genericType,
                                                ResolvedGenericTypeMirror resolvedGenericType, DomainTypeDetector domainTypeDetector) {
        this.type = Objects.requireNonNull(type);
        this.annotatedType = Objects.requireNonNull(annotatedType);
        this.genericType = genericType;
        this.resolvedGenericType = resolvedGenericType;
        this.domainTypeDetector = Objects.requireNonNull(domainTypeDetector);
    }

    /**
     * Create a new {@link AssertedContainableTypeMirror} based on the given type information.
     *
     * @return new instance of AssertedContainableTypeMirror
     */
    public AssertedContainableTypeMirror build() {
        var containerType = getContainerTypeName();
        var assertions = containerType.isEmpty() ? buildAssertionMirrors() : buildContainedAssertionMirrors();
        List<AssertionMirror> containerAssertions = containerType.isPresent() ? buildAssertionMirrors() :
            Collections.emptyList();
        return new AssertedContainableTypeModel(
            getTypeName(),
            domainTypeDetector.detectDomainType(getBasicType()),
            assertions,
            Optional.class.isAssignableFrom(type),
            Collection.class.isAssignableFrom(type),
            List.class.isAssignableFrom(type),
            Set.class.isAssignableFrom(type),
            Stream.class.isAssignableFrom(type),
            type.isArray(),
            containerType.orElse(null),
            containerAssertions,
            resolvedGenericType
        );
    }

    private List<AssertionMirror> buildAssertionMirrors() {
        return Arrays.stream(annotatedType.getAnnotations())
            .map(a -> {
                var assertionMirrorBuilder = new AssertionMirrorBuilder(getBasicType(), a, false,
                    Collection.class.isAssignableFrom(type));
                return assertionMirrorBuilder.build();
            })
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }

    private List<AssertionMirror> buildContainedAssertionMirrors() {
        if (AnnotatedParameterizedType.class.isAssignableFrom(annotatedType.getClass())) {
            var actualTypeArguments = ((AnnotatedParameterizedType) annotatedType).getAnnotatedActualTypeArguments();
            if (actualTypeArguments.length > 0) {
                return Arrays.stream(actualTypeArguments[0].getAnnotations())
                    .map(a -> {
                        var assertionMirrorBuilder = new AssertionMirrorBuilder(getBasicType(), a, true,
                            Collection.class.isAssignableFrom(type));
                        return assertionMirrorBuilder.build();
                    })
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
            }
        }
        return List.of();
    }

    private Optional<String> getContainerTypeName() {
        if (Optional.class.isAssignableFrom(type) || Collection.class.isAssignableFrom(
            type) || Stream.class.isAssignableFrom(type)) {
            return Optional.of(type.getName());
        }
        return Optional.empty();
    }

    /**
     * @return the basic type (the contained type).
     */
    public Type getBasicType() {
        if (Optional.class.isAssignableFrom(type) || Collection.class.isAssignableFrom(
            type) || Stream.class.isAssignableFrom(type)) {
            return JavaReflect.getFirstParameterType(genericType)
                .orElse(Object.class);
        } else if (type.isArray()) {
            return type.componentType();
        }
        return type;
    }

    private String getTypeName() {
        if (resolvedGenericType == null) {
            return getBasicType().getTypeName();
        } else {
            if (Optional.class.isAssignableFrom(type) || Collection.class.isAssignableFrom(
                type) || Stream.class.isAssignableFrom(type)) {
                return resolveOrFallBackToBasicType(resolvedGenericType.genericTypes().get(0).typeName());
            }
            return resolveOrFallBackToBasicType(resolvedGenericType.typeName());
        }
    }

    /**
     * A self-referential (F-bounded) type variable - e.g. a builder's own
     * {@code <C extends X, B extends Builder<C, B>>} pattern, as generated by Lombok's {@code @SuperBuilder} -
     * can be unresolvable for a generic type resolver in the absence of a concrete usage context, which then
     * falls back to {@link Object}. Reflection already knows a more specific type in that case (the type
     * variable's erased bound), so that fallback must never be trusted over it: it would otherwise disagree
     * with {@link #getBasicType()} - which {@link #build()} uses to detect the domain type - leaving the built
     * mirror with a domain type and a type name that describe two different classes.
     */
    private String resolveOrFallBackToBasicType(String resolvedTypeName) {
        if (Object.class.getTypeName().equals(resolvedTypeName) && !Object.class.equals(getBasicType())) {
            return getBasicType().getTypeName();
        }
        return resolvedTypeName;
    }
}
