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

package io.domainlifecycles.mirror.resolver;

import com.github.vladislavsevruk.resolver.resolver.executable.ExecutableTypeMetaResolver;
import com.github.vladislavsevruk.resolver.resolver.executable.ExecutableTypeResolver;
import com.github.vladislavsevruk.resolver.resolver.field.FieldTypeMetaResolver;
import com.github.vladislavsevruk.resolver.resolver.field.FieldTypeResolver;
import com.github.vladislavsevruk.resolver.type.TypeMeta;
import io.domainlifecycles.mirror.api.ResolvedGenericTypeMirror;
import io.domainlifecycles.mirror.api.WildcardBound;
import io.domainlifecycles.mirror.model.ResolvedGenericTypeModel;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * TypeMetaResolver is a concrete implementation of the GenericTypeResolver interface.
 * It provides methods for resolving generic types in fields and executable elements (methods, constructors).
 *
 * <p>
 * To enable generic type resolving in the DomainModel, set the TypeMetaResolver before initializing the domain:
 * <pre>{@code
 *  ReflectiveDomainModelFactory factory = new ReflectiveDomainModelFactory("tests");
 *  DomainModel dm = factory.initializeDomainModel(factory, new TypeMetaResolver());
 * }</pre>
 * <p>
 * A current use case is the DomainDiagrammer {@see io.domainlifecycles.diagram.domain.DomainDiagramGenerator}.
 *
 * @author Mario Herb
 */
public class TypeMetaResolver implements GenericTypeResolver {

    private final FieldTypeResolver<TypeMeta<?>> fieldTypeResolver = new FieldTypeMetaResolver();

    ExecutableTypeResolver<TypeMeta<?>> executableTypeResolver = new ExecutableTypeMetaResolver();

    /**
     * {@inheritDoc}
     */
    @Override
    public ResolvedGenericTypeMirror resolveFieldType(Field f, Class<?> contextClass) {
        TypeMeta<?> fieldTypeMeta = fieldTypeResolver.resolveField(contextClass, f);
        return map(fieldTypeMeta);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ResolvedGenericTypeMirror> resolveExecutableParameters(Method m, Class<?> contextClass) {
        return executableTypeResolver.getParameterTypes(contextClass, m)
            .stream()
            .map(this::map)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResolvedGenericTypeMirror resolveExecutableReturnType(Method m, Class<?> contextClass) {
        TypeMeta<?> methodReturnTypeMeta = executableTypeResolver.getReturnType(contextClass, m);
        return map(methodReturnTypeMeta);
    }

    private ResolvedGenericTypeMirror map(TypeMeta<?> typeMeta) {
        var typeName = typeMeta.getType().getName();
        if (typeMeta.getType().isArray()) {
            typeName = typeMeta.getType().getComponentType().getName();
        }
        return new ResolvedGenericTypeModel(typeName, typeMeta.getType().isArray(),
            Arrays.stream(typeMeta.getGenericTypes()).map(tm -> map(tm)).toList(), map(typeMeta.getWildcardBound()));
    }

    private Optional<WildcardBound> map(com.github.vladislavsevruk.resolver.type.WildcardBound metaWildcardBound) {
        if (metaWildcardBound == null) {
            return Optional.empty();
        }
        return metaWildcardBound.equals(com.github.vladislavsevruk.resolver.type.WildcardBound.UPPER) ? Optional.of(
            WildcardBound.UPPER) : Optional.of(WildcardBound.LOWER);
    }
}
