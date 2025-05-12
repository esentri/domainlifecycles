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

import io.domainlifecycles.mirror.exception.MirrorException;
import io.domainlifecycles.reflect.JavaReflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A utility class for resolving actual class types of generic type parameters for interfaces,
 * given a specific concrete subclass and its type hierarchy.
 * <p>
 * This class traverses the class hierarchy of a specified subtype to determine the resolved
 * class type for a generic type parameter defined in an interface. It provides methods to analyze the
 * relationships between the subtype and its implemented interfaces or extended classes and extract
 * the required type information.
 *
 * @author Mario Herb
 */
public class GenericInterfaceTypeResolver {
    private final Class<?> subType;
    private final List<Class<?>> hierarchy;

    /**
     * Constructs a GenericInterfaceTypeResolver instance for the specified subtype.
     *
     * @param subType the concrete subclass type used for resolving generic type information
     */
    public GenericInterfaceTypeResolver(Class<?> subType) {
        this.subType = subType;
        this.hierarchy = JavaReflect.allSupertypes(subType);
    }

    /**
     * Resolves the actual class type for a specified generic type parameter of a given interface type.
     *
     * @param interfaceType the interface type for which the generic type parameter is resolved
     * @param parameterIndex the index of the generic type parameter in the interface type
     * @return the resolved class type of the specified generic type parameter, or null if it cannot be resolved
     * @throws MirrorException if the provided interface type is not an interface, is not generic, or if the parameter index is invalid
     */
    public Class<?> resolveFor(Class<?> interfaceType, int parameterIndex) {
        if (!interfaceType.isInterface()) {
            throw MirrorException.fail("Tried to resolve generic type parameter for non interface type %s",
                interfaceType.getName());
        }
        if (!JavaReflect.isGeneric(interfaceType)) {
            throw MirrorException.fail("Tried to resolve generic type parameter for non generic interface type %s",
                interfaceType.getName());
        }
        if (interfaceType.getTypeParameters().length <= parameterIndex) {
            throw MirrorException.fail("Generic interface type %s has less than %s parameters", interfaceType.getName(),
                (parameterIndex + 1));
        }
        var filteredTypeHierarchy = new LinkedList<>(hierarchy
            .stream()
            .filter(c -> interfaceType.isAssignableFrom(c))
            .toList());

        var variable = interfaceType.getTypeParameters()[parameterIndex];

        var hierarchyIterator = filteredTypeHierarchy.descendingIterator();
        var superElement = hierarchyIterator.next();
        while (!superElement.getName().equals(interfaceType.getName()) && hierarchyIterator.hasNext()) {
            superElement = hierarchyIterator.next();
        }

        var subElement = subElement(hierarchyIterator);

        var resolved = resolveActualTypeByVariableName(subElement, variable, superElement);

        while (resolved instanceof TypeVariable<?>) {
            superElement = subElement;
            subElement = subElement(hierarchyIterator);
            if (subElement.equals(superElement)) {
                break;
            }
            resolved = resolveActualTypeByVariableName(subElement, ((TypeVariable<?>) resolved), superElement);
        }

        if (resolved instanceof Class<?>) {
            return (Class<?>) resolved;
        } else if (resolved instanceof TypeVariable<?>) {
            var tv = (TypeVariable<?>) resolved;
            if (tv.getBounds()[0] instanceof ParameterizedType) {
                var param = (ParameterizedType) tv.getBounds()[0];
                return (Class<?>) param.getRawType();
            }
            return (Class<?>) tv.getBounds()[0];
        }
        return null;
    }

    private Class<?> subElement(Iterator<Class<?>> iterator) {
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return subType;
    }

    private int implementsDirectlyIndex(Class<?> subType, Class<?> interfaceType) {
        var implementingInterfaces = subType.getInterfaces();
        for (int i = 0; i < implementingInterfaces.length; i++) {
            if (implementingInterfaces[i].getName().equals(interfaceType.getName())) {
                return i;
            }
        }
        return -1;
    }

    private Type resolveActualTypeByVariableName(Class<?> subType, TypeVariable<?> variable, Class<?> superType) {
        if (superType.isInterface()) {
            var interfaceIndex = implementsDirectlyIndex(subType, superType);
            if (interfaceIndex < 0) {
                return variable;
            }
            var genericInterface = subType.getGenericInterfaces()[interfaceIndex];
            if (genericInterface instanceof ParameterizedType) {
                var variableIndex = resolveVariableIndexByName(superType, variable.getName());
                return ((ParameterizedType) genericInterface).getActualTypeArguments()[variableIndex];
            }
        } else {
            var genericSuperClass = subType.getGenericSuperclass();
            if (genericSuperClass instanceof ParameterizedType) {
                var variableIndex = resolveVariableIndexByName(superType, variable.getName());
                return ((ParameterizedType) genericSuperClass).getActualTypeArguments()[variableIndex];
            }
        }
        return variable;
    }

    private int resolveVariableIndexByName(Class<?> genericType, String variableName) {
        var params = genericType.getTypeParameters();
        for (int i = 0; i < params.length; i++) {
            if (params[i].getName().equals(variableName)) {
                return i;
            }
        }
        return -1;
    }
}

