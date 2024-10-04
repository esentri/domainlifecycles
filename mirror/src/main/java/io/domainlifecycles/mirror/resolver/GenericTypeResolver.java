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

package io.domainlifecycles.mirror.resolver;

import io.domainlifecycles.mirror.api.ResolvedGenericTypeMirror;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * This interface provides methods for resolving generic types in fields and executable elements (methods,
 * constructors).
 *
 * @author Mario Herb
 */
public interface GenericTypeResolver {

    /**
     * Resolves the generic type of a field in the given context class.
     *
     * @param f            The field to resolve the generic type for.
     * @param contextClass The context class to resolve the generic type in.
     * @return The ResolvedGenericTypeMirror representing the resolved generic type of the field.
     */
    ResolvedGenericTypeMirror resolveFieldType(Field f, Class<?> contextClass);

    /**
     * Resolves the generic type of parameters in an executable element (method or constructor) based on the provided
     * context class.
     *
     * @param m            The Method object representing the executable element to resolve the parameters for.
     * @param contextClass The context class to resolve the generic type in.
     * @return A List of ResolvedGenericTypeMirror objects, each representing the resolved generic type of a parameter.
     * The order of the objects is the same as the natural parameter order within the given method.
     */
    List<ResolvedGenericTypeMirror> resolveExecutableParameters(Method m, Class<?> contextClass);

    /**
     * Resolves the generic type of the return type of a method.
     *
     * @param m            The Method object representing the method.
     * @param contextClass The context class to resolve the generic type in.
     * @return The ResolvedGenericTypeMirror representing the resolved generic type of the return type.
     */
    ResolvedGenericTypeMirror resolveExecutableReturnType(Method m, Class<?> contextClass);

}
