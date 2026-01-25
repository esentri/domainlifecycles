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
 *  Copyright 2019-2024
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
 */

package io.domainlifecycles.mirrorjmolecules.reflect;

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
            throw MirrorException.fail("Generic interface %s has less than %s parameters",
                interfaceType.getName(),
                parameterIndex + 1);
        }

        var filteredHierarchy = new LinkedList<>(hierarchy
            .stream()
            .filter(interfaceType::isAssignableFrom)
            .toList());

        if (filteredHierarchy.isEmpty()) {
            return null;
        }

        var variable = interfaceType.getTypeParameters()[parameterIndex];

        var iterator = filteredHierarchy.descendingIterator();
        var superElement = iterator.next();
        while (!superElement.getName().equals(interfaceType.getName()) && iterator.hasNext()) {
            superElement = iterator.next();
        }

        var subElement = subElement(iterator);

        var resolved = resolveFrom(subElement, variable, superElement);

        while (resolved instanceof TypeVariable<?>) {
            superElement = subElement;
            subElement = subElement(iterator);
            if (subElement.equals(superElement)) {
                break;
            }
            resolved = resolveFrom(subElement, (TypeVariable<?>) resolved, superElement);
        }

        if (resolved instanceof Class<?>) {
            return (Class<?>) resolved;
        }
        if (resolved instanceof TypeVariable<?> tv) {
            if (tv.getBounds()[0] instanceof ParameterizedType p) {
                return (Class<?>) p.getRawType();
            }
            return (Class<?>) tv.getBounds()[0];
        }
        return null;
    }

    private Class<?> subElement(Iterator<Class<?>> iterator) {
        return iterator.hasNext() ? iterator.next() : subType;
    }

    private int directInterfaceIndex(Class<?> sub, Class<?> iface) {
        var interfaces = sub.getInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            if (interfaces[i].getName().equals(iface.getName())) {
                return i;
            }
        }
        return -1;
    }

    private Type resolveFrom(Class<?> sub, TypeVariable<?> variable, Class<?> superType) {
        if (superType.isInterface()) {
            var index = directInterfaceIndex(sub, superType);
            if (index < 0) {
                return variable;
            }
            var gi = sub.getGenericInterfaces()[index];
            if (gi instanceof ParameterizedType p) {
                return getType(variable, superType, p);
            }
        } else {
            var gs = sub.getGenericSuperclass();
            if (gs instanceof ParameterizedType p) {
                return getType(variable, superType, p);
            }
        }
        return variable;
    }

    private Type getType(TypeVariable<?> variable, Class<?> superType, ParameterizedType p) {
        var vi = variableIndex(superType, variable.getName());
        var args = p.getActualTypeArguments();
        if (vi < 0 || vi >= args.length) {
            return variable;
        }
        return args[vi];
    }

    private int variableIndex(Class<?> type, String variableName) {
        var vars = type.getTypeParameters();
        for (int i = 0; i < vars.length; i++) {
            if (vars[i].getName().equals(variableName)) {
                return i;
            }
        }
        return -1;
    }
}