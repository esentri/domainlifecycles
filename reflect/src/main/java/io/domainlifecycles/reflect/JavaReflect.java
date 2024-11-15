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

package io.domainlifecycles.reflect;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.reflect.Modifier.ABSTRACT;
import static java.lang.reflect.Modifier.FINAL;
import static java.lang.reflect.Modifier.PRIVATE;
import static java.lang.reflect.Modifier.PROTECTED;
import static java.lang.reflect.Modifier.STATIC;
import static java.lang.reflect.Modifier.SYNCHRONIZED;
import static java.lang.reflect.Modifier.TRANSIENT;
import static java.lang.reflect.Modifier.VOLATILE;

/**
 * Simplify reflective access.
 *
 * @author Tobias Herb
 */
public enum JavaReflect {
    ;
    public static final String CLASS_FILE_EXT = ".class";

    // ----------------------------------------------------------

    // Expose the size in bytes required for value
    // encodings on Java heap storage allocations.

    /**
     * Size of a byte in bytes.
     */
    public static final int BYTE_SIZE = 1;

    /**
     * Size of a boolean in bytes.
     */
    public static final int BOOLEAN_SIZE = 1;

    /**
     * Size of a char in bytes.
     */
    public static final int CHAR_SIZE = 2;

    /**
     * Size of a short in bytes.
     */
    public static final int SHORT_SIZE = 2;

    /**
     * Size of an int in bytes.
     */
    public static final int INT_SIZE = 4;

    /**
     * Size of a float in bytes.
     */
    public static final int FLOAT_SIZE = 4;

    /**
     * Size of a long in bytes.
     */
    public static final int LONG_SIZE = 8;

    /**
     * Size of a double in bytes.
     */
    public static final int DOUBLE_SIZE = 8;

    /**
     * Size of a reference value in bytes.
     */
    public static final int REFERENCE_SIZE = 4; // TODO: Magma.virtualMachine().oopSize();

    // ----------------------------------------------------------

    /**
     * Returns the (simple) class name from the given class object.
     * If the object is null then "null" is returned.
     *
     * @param type whose class name is to be returned
     * @return the class name or "null"
     */
    public static String className(final Class<?> type) {
        return type == null ? "" : type.getSimpleName();
    }

    /**
     * Returns the full qualified class name from the given class object.
     * If the object is null then "null" is returned.
     *
     * @param type whose class name is to be returned
     * @return the class name or "null"
     */
    public static String qualifiedClassName(final Class<?> type) {
        return type == null ? "" : type.getName();
    }

    // ----------------------------------------------------------
    //  PACKAGE QUERIES.
    // ----------------------------------------------------------

    private static final Pattern packagePattern = Pattern.compile("^[a-z]+(\\.[a-zA-Z_][a-zA-Z0-9_]*)*$");

    /**
     * Determines whether the given name is a valid full qualified package name.
     *
     * @param packageName name of the package
     * @return whether the provided package name is valid and full qualified
     */
    public static boolean isValidPackage(final String packageName) {
        return packagePattern.matcher(packageName).matches();
    }

    /**
     * Returns the package name of {@code type} according to JLS §6.7.
     * Unlike {@link Class#getPackage}, this method only parses the class name,
     * without attempting to define the {@link Package} and hence load files.
     *
     * @param type the type
     * @return the package name of the provided type
     */
    public static String packageName(final Class<?> type) {
        return packageName(type.getName());
    }

    /**
     * Returns the package name of {@code classFullName} according to JLS §6.7.
     * Unlike {@link Class#getPackage}, this method only parses the class name,
     * without attempting to define the {@link Package} and hence load files.
     *
     * @param fullClassName the full name of class
     * @return package name of provided class name
     */
    public static String packageName(final String fullClassName) {
        final int lastDot = fullClassName.lastIndexOf('.');
        return (lastDot < 0) ? "" : fullClassName.substring(0, lastDot);
    }

    /**
     * Determines whether the two given types have the same class loader and
     * package qualifier.
     *
     * @param type1 the 1st type.
     * @param type2 the 2nd type.
     * @return true iff located in the same package.
     */
    public static boolean isSamePackage(final Class<?> type1, final Class<?> type2) {
        if (Objects.requireNonNull(type1) == Objects.requireNonNull(type2)) return true;
        if (type1.getClassLoader() != type2.getClassLoader()) return false;
        return Objects.equals(type1.getPackageName(), type2.getPackageName());
    }

    // ----------------------------------------------------------
    //  TYPE MODIFIER QUERIES.
    // ----------------------------------------------------------

    /**
     * Determines whether the given type is public.
     *
     * @param type the type to be checked
     * @return whether the provided type is public
     */
    public static boolean isPublic(final Class<?> type) {
        return hasModifiers(type, Modifier.PUBLIC);
    }

    /**
     * Determines whether the given member is public.
     *
     * @param member the member to be checked
     * @return whether the provided member is public
     */
    public static boolean isPublic(final Member member) {
        return hasModifiers(member, Modifier.PUBLIC);
    }

    // ----------------------------------------------------------

    /**
     * Determines whether the given type is {@code protected}.
     *
     * @param type the type to be checked
     * @return whether the provided type is protected
     */
    public static boolean isProtected(final Class<?> type) {
        return hasModifiers(type, PROTECTED);
    }

    /**
     * Determines whether the given member is {@code protected}.
     *
     * @param member the member to be checked
     * @return whether the provided member is protected
     */
    public static boolean isProtected(final Member member) {
        return hasModifiers(member, PROTECTED);
    }

    // ----------------------------------------------------------

    /**
     * Determines whether the given type is {@code private}.
     *
     * @param type the type to be checked
     * @return whether the provided type is private
     */
    public static boolean isPrivate(final Class<?> type) {
        return hasModifiers(type, PRIVATE);
    }

    /**
     * Determines whether the given member is {@code private}.
     *
     * @param member the member to be checked
     * @return whether the provided member is private
     */
    public static boolean isPrivate(final Member member) {
        return hasModifiers(member, PRIVATE);
    }

    // ----------------------------------------------------------

    /**
     * Determines whether the given type is package private.
     *
     * @param type the type to be checked
     * @return whether the provided type is package private
     */
    public static boolean isPackagePrivate(final Class<?> type) {
        return isPackagePrivate(type.getModifiers());
    }

    /**
     * Determines whether the given member is package private.
     *
     * @param member the member to be checked
     * @return whether the provided member is package private
     */
    public static boolean isPackagePrivate(final Member member) {
        return isPackagePrivate(member.getModifiers());
    }

    /**
     * Determines whether the given modifier includes no public, private, protected.
     *
     * @param mod the modifier to be checked
     * @return whether the provided modifier is package private
     */
    public static boolean isPackagePrivate(final int mod) {
        return (mod & (Modifier.PUBLIC | PROTECTED | PRIVATE)) == 0;
    }

    // ----------------------------------------------------------

    /**
     * Determines whether the given type is {@code abstract}.
     *
     * @param type the type to be checked
     * @return whether the provided type is abstract
     */
    public static boolean isAbstract(final Class<?> type) {
        return hasModifiers(type, ABSTRACT);
    }

    /**
     * Determines whether the given member is {@code abstract}.
     *
     * @param member the member to be checked
     * @return whether the provided member is abstract
     */
    public static boolean isAbstract(final Member member) {
        return hasModifiers(member, ABSTRACT);
    }

    // ----------------------------------------------------------

    /**
     * Determines whether the given type is {@code static}.
     *
     * @param type the type to be checked
     * @return whether the provided type is static
     */
    public static boolean isStatic(final Class<?> type) {
        return hasModifiers(type, STATIC);
    }

    /**
     * Determines whether the given member is {@code static}.
     *
     * @param member the member to be checked
     * @return whether the provided member is static
     */
    public static boolean isStatic(final Member member) {
        return hasModifiers(member, STATIC);
    }

    /**
     * Determines whether the given member is {@code static}.
     *
     * @param member the member to be checked
     * @return whether the provided member is static
     */
    public static boolean notStatic(final Member member) {
        return !hasModifiers(member, STATIC);
    }

    // ----------------------------------------------------------

    /**
     * Determines whether the given type is {@code final}.
     *
     * @param type the type to be checked
     * @return whether the provided type is final
     */
    public static boolean isFinal(final Class<?> type) {
        return hasModifiers(type, FINAL);
    }

    /**
     * Determines whether the given member is {@code final}.
     *
     * @param member the member to be checked
     * @return whether the provided member is final
     */
    public static boolean isFinal(final Member member) {
        return hasModifiers(member, FINAL);
    }

    // ----------------------------------------------------------

    /**
     * Determines whether the given method is {@code synchronized}.
     *
     * @param method the method to be checked
     * @return whether the provided method is synchronized
     */
    public static boolean isSynchronized(final Method method) {
        return hasModifiers(method, SYNCHRONIZED);
    }

    /**
     * Determines whether the given constructor is {@code synchronized}.
     *
     * @param ctor the constructor to be checked
     * @return whether the provided method is synchronized
     */
    public static boolean isSynchronized(final Constructor<?> ctor) {
        return hasModifiers(ctor, SYNCHRONIZED);
    }

    // ----------------------------------------------------------

    /**
     * Determines whether the given field is {@code volatile}.
     *
     * @param field the field to be checked
     * @return whether the provided field is volatile
     */
    public static boolean isVolatile(final Field field) {
        return hasModifiers(field, VOLATILE);
    }

    /**
     * Determines whether the given field is {@code transient}.
     *
     * @param field the field to be checked
     * @return whether the provided field is transient
     */
    public static boolean isTransient(final Field field) {
        return hasModifiers(field, TRANSIENT);
    }

    // ----------------------------------------------------------
    //  TYPE-LEVEL QUERIES.
    // ----------------------------------------------------------

    /// INTERFACE TYPES.

    /**
     * Determines whether the given type {@link Class#isInterface()}.
     *
     * @param type the type to be checked
     * @return whether the provided type is an interface
     */
    public static boolean isInterfaceType(final Class<?> type) {
        return type != null && type.isInterface();
    }

    /**
     * Determines whether the given type {@link Class#isAnnotation()}.
     *
     * @param type the type to be checked
     * @return whether the provided type is an annotation
     */
    public static boolean isAnnotationType(final Class<?> type) {
        return type != null && type.isAnnotation();
    }

    /**
     * Determines whether the given type is a {@link FunctionalInterface}.
     *
     * @param type the type to be checked
     * @return whether the provided type is a functional interface
     */
    public static boolean isLambdaType(final Class<?> type) {
        if (!type.isInterface()) return false;
        final long count = methods(type, MemberSelect.HIERARCHY)
            .stream()
            .filter(x -> x.getDeclaringClass() != Arities.Arity.class)
            .filter(x -> canLambda(x.getModifiers()))
            .count();
        return count == 1;
    }

    public static boolean isLambdaMethod(final Method method) {
        final Class<?> declaring = method.getDeclaringClass();
        return declaring.isInterface() && isLambdaType(declaring);
    }

    public static Optional<Method> lambdaMethod(final Class<?> type) {
        if (!isLambdaType(type)) return Optional.empty();
        return methods(type, MemberSelect.HIERARCHY)
            .stream()
            .filter(x -> x.getDeclaringClass() != Arities.Arity.class)
            .filter(x -> canLambda(x.getModifiers()))
            .findFirst();
    }

    private static boolean canLambda(final int mod) {
        return Modifier.isPublic(
            mod) && (mod & (Modifier.ABSTRACT | Modifier.PUBLIC | Modifier.STATIC)) != Modifier.PUBLIC;
    }


    /**
     * Determines whether it's a lambda expression
     *
     * @return whether it's a lambda expression
     */
    public static Predicate<Method> canLambda() {
        return method -> canLambda(method.getModifiers());
    }

    // ----------------------------------------------------------

    /// CLASS TYPES.

    /**
     * Determines whether the given type is a class.
     *
     * @param type the type to be checked
     * @return whether the provided type is a class
     */
    public static boolean isClassType(final Class<?> type) {
        return !type.isPrimitive() && !type.isArray();
    }

    /**
     * Determines whether the given type is a top-level class.
     *
     * @param type the type to be checked
     * @return whether the provided type is a top-level class
     */
    public static boolean isTopLevelType(final Class<?> type) {
        return type != null && type.getDeclaringClass() == null
            && !isAnonymousType(type) && !isLocalType(type);
    }

    /**
     * Determines whether the given type {@link Class#isMemberClass()}.
     *
     * @param type the type to be checked
     * @return whether the provided type is a member class
     */
    public static boolean isMemberType(final Class<?> type) {
        return type != null && type.isMemberClass();
    }

    /**
     * Determines whether the given type {@link Class#isEnum()}.
     *
     * @param type the type to be checked
     * @return whether the provided type is an enum
     */
    public static boolean isEnumType(final Class<?> type) {
        return type != null && type.isEnum();
    }

    /**
     * Determines whether the given type {@link Class#isAnonymousClass()};
     *
     * @param type the type to be checked
     * @return whether the provided type is anonymous
     */
    public static boolean isAnonymousType(final Class<?> type) {
        return type != null && type.isAnonymousClass();
    }

    /**
     * Determines whether the given type {@link Class#isLocalClass()};
     *
     * @param type the type to be checked
     * @return whether the provided type is local
     */
    public static boolean isLocalType(final Class<?> type) {
        return type != null && type.isLocalClass();
    }

    /**
     * Determines whether the given java class object represents a boxed type.
     *
     * @param type the type to be checked
     * @return whether the provided type is boxed
     */
    public static boolean isBoxedType(final Class<?> type) {
        return (!type.isPrimitive() && !type.isArray())
            && type == Boolean.class
            || type == Byte.class
            || type == Short.class
            || type == Character.class
            || type == Integer.class
            || type == Long.class
            || type == Float.class
            || type == Double.class
            || type == Void.class;
    }

    // ----------------------------------------------------------

    /// ARRAY TYPES.

    /**
     * Determines whether the given class object represents an array type.
     *
     * @param o the object to be checked
     * @return whether the class of the provided object is an array type
     */
    public static boolean isArray(final Object o) {
        return o != null && isArrayType(o.getClass());
    }

    /**
     * Determines whether the given class object represents an array type.
     *
     * @param type the type to be checked
     * @return whether the provided type is an array
     */
    public static boolean isArrayType(final Class<?> type) {
        return type != null && type.isArray();
    }

    /**
     * Determines whether the given type represents an primitive array type.
     *
     * @param type the type to be checked
     * @return whether the provided type is a primitive array
     */
    public static boolean isPrimitiveArrayType(final Class<?> type) {
        return type != null && type.isArray() && isPrimitiveType(type.getComponentType());
    }

    /**
     * Determines whether the given type represents an reference array type.
     *
     * @param type the type to be checked
     * @return whether the provided type is a reference array
     */
    public static boolean isReferenceArrayType(final Class<?> type) {
        return type != null && type.isArray() && !isPrimitiveType(type.getComponentType());
    }

    /**
     * Determines whether the given type represents a multidimensional array type.
     *
     * @param type the type to be checked
     * @return whether the provided type is a multidimensional array
     */
    public static boolean isMultidimensionalArrayType(final Class<?> type) {
        return type != null && type.isArray() && isArrayType(type.getComponentType());
    }

    // ----------------------------------------------------------

    /// PRIMITIVE TYPES.

    /**
     * Determines whether the given type {@link Class#isPrimitive()}.
     *
     * @param type the type to be checked
     * @return whether the provided type is primitive
     */
    public static boolean isPrimitiveType(final Class<?> type) {
        return type != null && type.isPrimitive();
    }

    /**
     * Determines whether the given type occupies a single JVM stack slot?
     *
     * @param type the type to be checked
     * @return whether the provided type occupies a single JVM stack slot
     */
    public static boolean isCategoryI(final Class<?> type) {
        return type != null && type != long.class && type != double.class;
    }

    /**
     * Determines whether a value of the given type occupies two JVM stack slots.
     *
     * @param type the tpe to be checked
     * @return whether the provided type occupies two JVM stack slots
     */
    public static boolean isCategoryII(final Class<?> type) {
        return type == long.class || type == double.class;
    }

    /**
     * Determines whether the given type is a numeric type (not void or object)?
     *
     * @param type the type to be checked
     * @return whether the provided type is numeric
     */
    public static boolean isNumericType(final Class<?> type) {
        return type != void.class && !Object.class.isAssignableFrom(type);
    }

    /**
     * Determines whether the given type is non floating numeric type.
     *
     * @param type the type to be checked
     * @return whether the provided type is non floating numeric
     */
    public static boolean isIntegralType(final Class<?> type) {
        return isNumericType(type) && !isFloatingType(type);
    }

    /**
     * Determines whether the given type is an signed integral type.
     *
     * @param type the type to be checked
     * @return whether the provided type is signed
     */
    public static boolean isSignedType(final Class<?> type) {
        return type == byte.class || type == short.class || type == int.class || type == long.class;
    }

    /**
     * Determines whether the given type is an unsigned integral type.
     *
     * @param type type to be checked
     * @return whether the provided type is unsigned
     */
    public static boolean isUnsignedType(final Class<?> type) {
        return type == boolean.class || type == char.class;
    }

    /**
     * Determines whether the given type is either float or double.
     *
     * @param type type to be checked
     * @return whether the given type if floating
     */
    public static boolean isFloatingType(final Class<?> type) {
        return type == float.class || type == double.class;
    }

    private static boolean hasModifiers(final Class<?> type, final int mod) {
        return (mod & type.getModifiers()) != 0;
    }

    private static boolean hasModifiers(final Member member, final int mod) {
        return (mod & member.getModifiers()) != 0;
    }

    // ----------------------------------------------------------
    //  ARRAY TYPE QUERIES.
    // ----------------------------------------------------------

    /**
     * Returns the optional component type of the given array type.
     *
     * @param type array type
     * @return optional component type of provided array type
     */
    public static Optional<Class<?>> componentType(final Class<?> type) {
        return Optional.ofNullable(type.getComponentType());
    }

    /**
     * Returns the optional element type of the given array type.
     *
     * @param type array type
     * @return optional element type of provided array type
     */
    public static Optional<Class<?>> elementType(final Class<?> type) {
        if (!isArrayType(Objects.requireNonNull(type))) {
            return Optional.empty();
        }
        Class<?> elementType = type;
        while (true) {
            if (elementType.getComponentType() == null) {
                return Optional.of(elementType);
            }
            elementType = elementType.getComponentType();
        }
    }

    /**
     * Returns the rank of a given array type or 0;
     *
     * @param type array type
     * @return the rank
     */
    public static int arrayRank(final Class<?> type) {
        int rank = 0;
        Class<?> elementType = type;
        while (elementType.isArray()) {
            elementType = elementType.getComponentType();
            ++rank;
        }
        return rank;
    }

    // ----------------------------------------------------------

    /// GENERIC TYPES.
    public static Optional<Type> getFirstParameterType(Type genericType) {
        if (ParameterizedType.class.isAssignableFrom(genericType.getClass())) {
            return Optional.of(((ParameterizedType) genericType).getActualTypeArguments()[0]);
        }
        return Optional.empty();
    }


    /**
     * Determines whether the given type is a generic type.
     *
     * @param type the type to be checked
     * @return whether it is generic
     */
    public static boolean isGeneric(final Class<?> type) {
        return type.getTypeParameters().length > 0;
    }

    /**
     * Determines whether the given constructor has generic parameters.
     *
     * @param ctor the constructor to be checked
     * @return whether it is generic
     */
    public static boolean isGeneric(final Constructor<?> ctor) {
        final Type[] ptypes = ctor.getGenericParameterTypes();
        for (int i = 0; i < ptypes.length; ++i) {
            if (isGenericType(ptypes[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines whether the given method has generic return type
     * and/or generic parameters.
     *
     * @param method method to be checked
     * @return whether it is generic
     */
    public static boolean isGeneric(final Method method) {
        if (isGenericType(method.getGenericReturnType())) return true;
        final Type[] ptypes = method.getGenericParameterTypes();
        for (int i = 0; i < ptypes.length; ++i) {
            if (isGenericType(ptypes[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines whether the given method has generic return type
     * and/or generic parameters.
     *
     * @param field field to be checked
     * @return whether it is generic
     */
    public static boolean isGeneric(final Field field) {
        return isGenericType(field.getGenericType());
    }

    private static boolean isGenericType(final Type type) {
        return type instanceof TypeVariable || type instanceof GenericArrayType;
    }

    // ----------------------------------------------------------
    //  SUB-TYPING RELATIONS.
    // ----------------------------------------------------------

    /// SUPERCLASSES.

    /**
     * Return the optional direct superclass of the given type. If the given
     * type is a interfaces then {@link Optional#empty()} is returned.
     *
     * @param type whose direct superclass is to be returned.
     * @return optional direct superclass.
     */
    public static Optional<Class<?>> superclass(final Class<?> type) {
        return type == Object.class ? Optional.empty() : Optional.ofNullable(type.getSuperclass());
    }

    /**
     * Returns a list containing all direct/indirect superclasses of the given type.
     * The {@code include}-flag determines whether the given type is contained.
     * <p>
     * Total ordered sequence via superclass-relation.
     * Class hierarchy root {@link Object} is excluded.
     *
     * @param type whose superclasses are to be returned.
     * @return superclass sequence.
     */
    public static List<Class<?>> allSuperclasses(final Class<?> type) {
        return allSuperclasses(type, false);
    }

    /**
     * Returns a list containing all direct/indirect superclasses of the given type.
     * The {@code include}-flag determines whether the given type is contained.
     * <p>
     * Total ordered sequence via superclass-relation.
     * Class hierarchy root {@link Object} is excluded.
     *
     * @param type    whose superclasses are to be returned.
     * @param include flag to include the given type.
     * @return superclass sequence.
     */
    public static List<Class<?>> allSuperclasses(final Class<?> type, final boolean include) {
        return new SuperclassIterator(include ? type : type.getSuperclass()).toList();
    }

    /// INTERFACE-TYPES.

    /**
     * Returns all direct interfaces of the given type.
     * Sequence is empty if the given type has no interface relations.
     *
     * @param type whose direct superclass is to be returned.
     * @return optional direct superclass.
     */
    public static List<Class<?>> interfaces(final Class<?> type) {
        return List.of(type.getInterfaces());
    }

    /**
     * Returns a sequence containing all direct and indirect interfaces of the given type,
     * i.e. the transitive closure over all (super-)interfaces.
     *
     * @param type whose interface hierarchy is to be returned.
     * @return all interfaces of the given type
     */
    public static List<Class<?>> allInterfaces(final Class<?> type) {
        return new InterfaceIterator(type).toList();
    }

    /// SUPERTYPES = SUPERCLASSES + INTERFACE-TYPES.

    /**
     * Returns a sequence containing all direct supertypes of the given type.
     * Partially ordered type sequence providing superclass then interfaces.
     *
     * @param type whose direct supertypes are to be returned.
     * @return sequence containing all superclasses.
     */
    public static List<Class<?>> supertypes(final Class<?> type) {
        List<Class<?>> classes = new ArrayList<>();
        classes.add(type.getSuperclass());
        classes.addAll(List.of(type.getInterfaces()));
        return List.of(classes.toArray(Class<?>[]::new));
    }

    /**
     * Returns a sequence containing all direct/indirect supertypes of the given type.
     * Partially ordered type sequence with superclass before interfaces relative to
     * the given type and all types contained in the sequence.
     *
     * @param type whose direct/indirect supertypes are to be returned.
     * @return sequence containing all supertypes.
     */
    public static List<Class<?>> allSupertypes(final Class<?> type) {
        return new SupertypeIterator(type).toList();
    }

    /**
     * Returns a sequence containing all direct/indirect supertypes of the given type.
     * Partially ordered type sequence with superclass before interfaces relative to
     * the given type and all types contained in the sequence.
     *
     * @param type    whose direct/indirect supertypes are to be returned.
     * @param include flag whether all supertypes should be included
     * @return sequence containing all supertypes.
     */
    public static List<Class<?>> allSupertypes(final Class<?> type, final boolean include) {
        final List<Class<?>> supertypes = new SupertypeIterator(type).toList();
        if (include) {
            List<Class<?>> allSupertypes = new ArrayList<>();
            allSupertypes.add(type);
            allSupertypes.addAll(supertypes);
            return List.of(allSupertypes.toArray(Class<?>[]::new));

        } else {
            return supertypes;
        }
    }

    /**
     * Determines the common supertype of the two given types.
     *
     * @param type1 1st type.
     * @param type2 2nd type.
     * @return common super type.
     */
    public static Class<?> commonSupertype(final Class<?> type1, final Class<?> type2) {
        if (type1 == type2) {
            return type1;
        }
        if (type1.isAssignableFrom(type2)) {
            return type1;
        } else if (type2.isAssignableFrom(type1)) {
            return type2;
        }
        return Object.class;
    }

    public static boolean isSubclassOf(Class<?> queryClass, Class<?> ofClass) {
        while (queryClass != null) {
            if (queryClass == ofClass) {
                return true;
            }
            queryClass = queryClass.getSuperclass();
        }
        return false;
    }

    // ----------------------------------------------------------
    //  LEXICAL-SCOPE RELATION.
    // ----------------------------------------------------------

    public static Optional<Class<?>> enclosingType(final Class<?> type) {
        return Optional.ofNullable(type).map(Class::getEnclosingClass);
    }

    /**
     * Returns the type sequence defining the enclosing lexical scope of the given member,
     * containing all enclosing types up to the outermost top-level type.
     *
     * @param member whose enclosing types are to be returned.
     * @return type sequence of the enclosing lexical scope.
     */
    public static List<Class<?>> allEnclosingTypes(final Member member) {
        return allEnclosingTypes(member.getDeclaringClass(), true);
    }

    /**
     * Returns the type sequence defining the enclosing lexical scope of the given type,
     * containing all enclosing types up to the outermost top-level type.
     * <p>
     * The returned sequence is empty, if the given type is at top level.
     *
     * @param type whose enclosing types are to be returned.
     * @return type sequence of the enclosing lexical scope.
     */
    public static List<Class<?>> allEnclosingTypes(final Class<?> type) {
        return allEnclosingTypes(type, false);
    }

    /**
     * Returns the type sequence defining the enclosing lexical scope of the given type,
     * containing all enclosing types up to the outermost top-level type. The given
     * {@code include} flag determines whether the given type is contained.
     * <p>
     * The returned sequence is empty, if the given type is at top level.
     *
     * @param type    whose enclosing types are to be returned.
     * @param include flag whether type is contained.
     * @return type sequence of the enclosing lexical scope.
     */
    public static List<Class<?>> allEnclosingTypes(final Class<?> type, final boolean include) {
        List<Class<?>> classes = new ArrayList<>();
        Class<?> cls = include ? type : type.getEnclosingClass();
        int ix = 0;
        while (cls != null) {
            classes.add(cls);
            cls = cls.getEnclosingClass();
        }
        return List.of(classes.toArray(Class<?>[]::new));
    }

    // ----------------------------------------------------------

    /**
     * Returns a sequence containing the declared fields of the given declaring.
     *
     * @param declaring type whose declared fields are to be returned.
     * @return sequence containing the declared fields.
     */
    public static List<Field> fields(final Class<?> declaring) {
        return fields(declaring, MemberSelect.DECLARED);
    }

    /**
     * Returns a sequence containing the selected fields of the given declaring.
     *
     * @param declaring type whose fields are to be returned.
     * @param select    criteria of the fields to be returned.
     * @return sequence containing the selected fields.
     */
    public static List<Field> fields(final Class<?> declaring, final MemberSelect select) {
        Objects.requireNonNull(declaring);
        switch (Objects.requireNonNull(select)) {
            case DECLARED:
                return List.of(declaring.getDeclaredFields());
            case ACCESSIBLE:
                return List.of(declaring.getFields());
            case VISIBLE:
                final List<Field> fields = allSupertypes(declaring)
                    .stream()
                    .flatMap(x -> Stream.of(x.getDeclaredFields()))
                    .filter(x -> isPublic(x) || isProtected(x)
                        || (isPackagePrivate(x) && isSamePackage(x.getDeclaringClass(), declaring)))
                    .collect(Collectors.toList());
                final List<Field> visibleFields = new ArrayList<>();
                visibleFields.addAll(List.of(declaring.getDeclaredFields()));
                visibleFields.addAll(fields);
                return List.of(visibleFields.toArray(Field[]::new));
            case HIERARCHY:
                return allSupertypes(declaring, true)
                    .stream()
                    .flatMap(x -> Stream.of(x.getDeclaredFields()))
                    .collect(Collectors.toList());
            default:
                throw new IllegalStateException();
        }
    }

    public static Optional<Field> findField(final Class<?> declaring, final String name) {
        return findField(declaring, MemberSelect.DECLARED, name);
    }

    public static Optional<Field> findField(final Class<?> declaring, final MemberSelect select, final String name) {
        return fields(declaring, select)
            .stream()
            .filter(x -> x.getName().equals(name))
            .findFirst();
    }

    // ----------------------------------------------------------

    /**
     * Returns a sequence containing the declared methods of the given declaring.
     *
     * @param declaring type whose declared methods are to be returned.
     * @return sequence containing the declared methods.
     */
    public static List<Method> methods(final Class<?> declaring) {
        return methods(declaring, MemberSelect.DECLARED);
    }

    /**
     * Returns a sequence containing the selected methods of the given declaring.
     *
     * @param declaring type whose methods are to be returned.
     * @param select    criteria of the methods to be returned.
     * @return sequence containing the selected methods.
     */
    public static List<Method> methods(final Class<?> declaring, final MemberSelect select) {
        Objects.requireNonNull(declaring);
        switch (Objects.requireNonNull(select)) {
            case DECLARED:
                return List.of(declaring.getDeclaredMethods());
            case ACCESSIBLE:
                return List.of(declaring.getMethods());
            case VISIBLE: {
                final List<Method> methods = allSupertypes(declaring)
                    .stream()
                    .flatMap(x -> Stream.of(x.getDeclaredMethods()))
                    .filter(x -> isPublic(x) || isProtected(x)
                        || (isPackagePrivate(x) && isSamePackage(x.getDeclaringClass(), declaring)))
                    .collect(Collectors.toList());
                var visibleMethods = new ArrayList<>();
                visibleMethods.addAll(List.of(declaring.getDeclaredMethods()));
                visibleMethods.addAll(methods);
                return List.of(visibleMethods.toArray(Method[]::new));
            }
            case HIERARCHY:
                return allSupertypes(declaring, true)
                    .stream()
                    .flatMap(x -> {
                        try {
                            return Stream.of(x.getDeclaredMethods());
                        } catch (Throwable ex) {
                            return Stream.empty();
                        }
                    }).collect(Collectors.toList());
            default:
                throw new IllegalStateException();
        }
    }

    public static Optional<Method> findMethod(final Class<?> declaring,
                                              final String name, final Class<?>... parameters) {
        return findMethod(declaring, MemberSelect.DECLARED, name, parameters);
    }

    public static Optional<Method> findMethod(final Class<?> declaring,
                                              final MemberSelect select,
                                              final String name, final Class<?>... parameters) {
        return methods(declaring, select)
            .stream()
            .filter(x -> x.getName().equals(name)
                && isAssignable(x.getParameterTypes(), parameters, false))
            .findFirst();
    }

    // ----------------------------------------------------------

    /**
     * Returns a sequence containing the declared classes of the given declaring.
     *
     * @param declaring type whose declared classes are to be returned.
     * @return sequence containing the declared classes.
     */
    public static List<Class<?>> classes(final Class<?> declaring) {
        return classes(declaring, MemberSelect.DECLARED);
    }

    /**
     * Returns a sequence containing the selected classes of the declaring class.
     *
     * @param declaring type whose classes are to be returned.
     * @param select    criteria of the classes to be returned.
     * @return sequence containing the selected classes.
     */
    public static List<Class<?>> classes(final Class<?> declaring, final MemberSelect select) {
        Objects.requireNonNull(declaring);
        switch (Objects.requireNonNull(select)) {
            case DECLARED:
                return List.of(declaring.getDeclaredClasses());
            case ACCESSIBLE:
                return List.of(declaring.getClasses());
            case VISIBLE: {
                final List<Class<?>> classes =
                    allSupertypes(declaring)
                        .stream()
                        .flatMap(x -> Stream.of(x.getDeclaredClasses()))
                        .filter(x -> isPublic(x) || isProtected(x)
                            || (isPackagePrivate(x) && isSamePackage(x.getDeclaringClass(), declaring)))
                        .collect(Collectors.toList());
                var declaredList = new ArrayList<>();
                declaredList.addAll(List.of(declaring.getDeclaredClasses()));
                declaredList.addAll(classes);
                return List.of(declaredList.toArray(Class<?>[]::new));
            }
            case HIERARCHY:
                return allSupertypes(declaring, true)
                    .stream()
                    .flatMap(x -> Stream.of(x.getDeclaredClasses()))
                    .collect(Collectors.toList());
            default:
                throw new IllegalStateException();
        }
    }

    // ----------------------------------------------------------

    /**
     * Returns a sequence containing the declared constructors of the declaring class.
     *
     * @param declaring type whose constructors are to be returned.
     * @param select    selected members
     * @return sequence containing the declaring constructors.
     */
    public static List<Constructor<?>> constructors(final Class<?> declaring, final MemberSelect select) {
        return List.of(declaring.getDeclaredConstructors());
    }

    public static Optional<Constructor<?>> findConstructor(final Class<?> declaring, final Class<?>... parameters) {
        return constructors(declaring, null)
            .stream()
            .filter(x -> isAssignable(x.getParameterTypes(), parameters, false))
            .findFirst();
    }

    // ----------------------------------------------------------
    //  BOXING/UNBOXING CONVERSION.
    // ----------------------------------------------------------

    /**
     * Returns the boxed-type for the given primitive-type. If the given type
     * has no boxed representation, it is returned. Boxing conversion treats
     * expressions of a primitive type as expressions of a reference type.
     * The following eight conversions are called boxing conversions:
     * <p>
     * {@link boolean} -> {@link Boolean}.
     * {@link byte}    -> {@link Byte}.
     * {@link short}   -> {@link Short}.
     * {@link char}    -> {@link Character}.
     * {@link int}     -> {@link Integer}.
     * {@link long}    -> {@link Long}.
     * {@link float}   -> {@link Float}.
     * {@link double}  -> {@link Double}.
     *
     * @param type class object representing the primitive type.
     * @return corresponding boxed type; otherwise the input.
     */
    public static Class<?> boxedType(final Class<?> type) {
        if (type.isPrimitive()) {
            if (type == void.class)
                return Void.class;
            else if (type == boolean.class)
                return Boolean.class;
            else if (type == byte.class)
                return Byte.class;
            else if (type == short.class)
                return Short.class;
            else if (type == char.class)
                return Character.class;
            else if (type == int.class)
                return Integer.class;
            else if (type == long.class)
                return Long.class;
            else if (type == float.class)
                return Float.class;
            else
                return Double.class;
        }
        return type;
    }

    /**
     * Returns the primitive-type for a given boxed-type (or wrapper-type).
     * If the given type has no wrapper representation, it is directly returned.
     * Unboxing treats expressions of reference types as expressions of primitive
     * types. The following eight conversions are called unboxing conversions:
     * <p>
     * {@link Boolean}   -> {@link boolean}
     * {@link Byte}      -> {@link byte}
     * {@link Short}     -> {@link short}
     * {@link Character} -> {@link char}
     * {@link Integer}   -> {@link int}
     * {@link Long}      -> {@link long}
     * {@link Float}     -> {@link float}
     * {@link Double}    -> {@link double}
     *
     * @param type class object which may represent a boxed type.
     * @return corresponding primitive type; otherwise the input.
     */
    public static Class<?> unboxedType(final Class<?> type) {
        if (!Objects.requireNonNull(type).isArray()) {
            if (type == Void.class)
                return void.class;
            else if (type == Boolean.class)
                return boolean.class;
            else if (type == Byte.class)
                return byte.class;
            else if (type == Short.class)
                return short.class;
            else if (type == Character.class)
                return char.class;
            else if (type == Integer.class)
                return int.class;
            else if (type == Long.class)
                return long.class;
            else if (type == Float.class)
                return float.class;
            else
                return double.class;
        }
        return type;
    }

    /// TYPE-ASSIGNABILITY <=> QUERY TYPE-RELATION.

    /**
     * Determines the subtype-relation between the {@code lhs}- and {@code rhs} types,
     * i.e. whether R-value with runtime-type specified by right-hand side {@code rhs}
     * can be assigned to the L-value location with runtime-type specified by right-
     * hand side {@code lhs}.
     * <p>
     * In contrast to {@link Class#isAssignableFrom(Class)} the algorithm
     * includes the following (implicit) widening conversions:
     * <ul>
     *  <li>Identity Conversion {@see JLS §5.1.1}</li>
     *  <li>Widening Primitive Conversion {@see JLS §5.1.2}</li>
     *  <li>Widening Reference Conversion {@see JLS §5.1.5}</li>
     * </ul>
     *
     * @param lhs class object to check, may be null.
     * @param rhs class object to try to assign into, returns false if null.
     * @return true if assignment possible.
     */
    public static boolean isAssignable(final Class<?> lhs, final Class<?> rhs) {
        return isAssignable(lhs, rhs, true);
    }

    /**
     * Determines the subtype-relation between the {@code lhs}- and {@code rhs} types,
     * i.e. whether R-value with runtime-type specified by right-hand side {@code rhs}
     * can be assigned to the L-value location with runtime-type specified by right-
     * hand side {@code lhs}.
     * <p>
     * In contrast to {@link Class#isAssignableFrom(Class)} the algorithm
     * includes the following (implicit) widening conversions:
     * <ul>
     *  <li>Identity Conversion {@see JLS §5.1.1}</li>
     *  <li>Widening Primitive Conversion {@see JLS §5.1.2}</li>
     *  <li>Widening Reference Conversion {@see JLS §5.1.5}</li>
     * </ul>
     *
     * @param lhs    type of L-value location being assigned.
     * @param rhs    type of R-value to be assigned.
     * @param boxing flag to allow auto boxing/unboxing.
     * @return true if assignment possible.
     */
    public static boolean isAssignable(Class<?> lhs, final Class<?> rhs, final boolean boxing) {
        if (rhs == null) return false;
        if (lhs == null) return !rhs.isPrimitive();
        if (boxing) {
            if (lhs.isPrimitive() && !rhs.isPrimitive()) {
                lhs = boxedType(lhs);
                if (lhs == null) {
                    return false;
                }
            }
            if (rhs.isPrimitive() && !lhs.isPrimitive()) {
                lhs = unboxedType(lhs);
                if (lhs == null) {
                    return false;
                }
            }
        }
        if (lhs.equals(rhs))
            return true;
        if (lhs.isPrimitive()) {
            if (!rhs.isPrimitive())
                return false;
            if (lhs == int.class)
                return rhs == long.class
                    || rhs == float.class
                    || rhs == double.class;
            if (lhs == long.class)
                return rhs == float.class
                    || rhs == double.class;
            if (lhs == boolean.class)
                return false;
            if (lhs == double.class)
                return false;
            if (lhs == float.class)
                return rhs == double.class;
            if (lhs == char.class || lhs == short.class)
                return rhs == int.class
                    || rhs == long.class
                    || rhs == float.class
                    || rhs == double.class;
            if (lhs == byte.class)
                return rhs == short.class
                    || rhs == int.class
                    || rhs == long.class
                    || rhs == float.class
                    || rhs == double.class;
            throw new IllegalStateException();
        }
        return lhs.isAssignableFrom(rhs);
    }

    public static boolean isAssignable(final Class<?>[] lhs,
                                       final Class<?>[] rhs) {
        return isAssignable(lhs, rhs, true);
    }

    public static boolean isAssignable(final Class<?>[] lhs,
                                       final Class<?>[] rhs,
                                       final boolean boxing) {
        if (lhs.length != rhs.length) return false;
        boolean canAssign = true;
        for (int i = 0; canAssign && i < lhs.length; ++i) {
            canAssign = isAssignable(lhs[i], rhs[i], boxing);
        }
        return canAssign;
    }

    // ----------------------------------------------------------
    //  SOURCE QUERY.
    // ----------------------------------------------------------

    /**
     * Return optional path to source code file of the given class.
     *
     * @param type whose path to source is to be resolved.
     * @return optional source path.
     */
    public static Optional<Path> sourceCode(final Class<?> type) {
        Objects.requireNonNull(type);
        final ProtectionDomain dom;
        if ((dom = type.getProtectionDomain()) != null) {
            final CodeSource src;
            if ((src = dom.getCodeSource()) != null) {
                final URL url = src.getLocation();
                final Path path = Paths.get(url.getFile());
                if (Files.exists(path)) {
                    return Optional.of(path);
                }
            }
        }
        return Optional.empty();
    }

    // ----------------------------------------------------------
    //  BYTE-CODE QUERY.
    // ----------------------------------------------------------

    /**
     * Returns optional byte code for given type. This works with inner
     * classes and even with bootstrap classes having a null ClassLoader.
     *
     * @param type whose bytecode is to be fetched.
     * @return optional byte code.
     */
    public static Optional<InputStream> byteCode(final Class<?> type) {
        final String rsc = '/' + type.getName().replace('.', '/') + CLASS_FILE_EXT;
        return Optional.ofNullable(type.getResourceAsStream(rsc));
    }

    // ----------------------------------------------------------
    //  SUPERTYPE ITERATOR.
    // ----------------------------------------------------------

    public static final class SuperclassIterator implements Iterator<Class<?>> {
        private Class<?> superclass;

        public SuperclassIterator(final Class<?> type) {
            this.superclass = type;
        }

        @Override
        public final boolean hasNext() {
            return null != superclass && superclass != Object.class;
        }

        @Override
        public final Class<?> next() {
            if (!hasNext()) throw new NoSuchElementException();
            final Class<?> next = superclass;
            superclass = next.getSuperclass();
            return next;
        }

        @Override
        public final void forEachRemaining(final Consumer<? super Class<?>> action) {
            Objects.requireNonNull(action);
            for (Class<?> next = superclass;
                 next != null && next != Object.class;
                 next = next.getSuperclass()) {
                action.accept(next);
            }
            superclass = null;
        }

        public List<Class<?>> toList() {
            List<Class<?>> superClasses = new ArrayList<>();
            while (hasNext()) {
                superClasses.add(next());
            }
            return superClasses;
        }
    }

    // ----------------------------------------------------------
    //  INTERFACE ITERATOR.
    // ----------------------------------------------------------

    public static final class InterfaceIterator implements Iterator<Class<?>> {
        private final LinkedList<Class<?>> stack = new LinkedList<>();
        private final HashSet<Class<?>> visited;

        public InterfaceIterator(final Class<?> type) {
            this(new HashSet<>(), type);
        }

        public InterfaceIterator(final HashSet<Class<?>> visited, final Class<?> type) {
            this.visited = Objects.requireNonNull(visited);
            Stream.of(type.getInterfaces())
                .filter(visited::add)
                .forEach(stack::push);
        }

        @Override
        public final boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public final Class<?> next() {
            if (!hasNext()) throw new NoSuchElementException();
            final Class<?> next = stack.pop();
            Stream.of(next.getInterfaces())
                .filter(visited::add)
                .forEach(stack::push);
            return next;
        }

        public List<Class<?>> toList() {
            List<Class<?>> superInterfaces = new ArrayList<>();
            while (hasNext()) {
                superInterfaces.add(next());
            }
            return superInterfaces;
        }
    }

    // ----------------------------------------------------------
    //  SUPERCLASS ITERATOR.
    // ----------------------------------------------------------

    public static final class SupertypeIterator implements Iterator<Class<?>> {
        private final Set<Class<?>> visited = new LinkedHashSet<>();
        private final Queue<Class<?>> workset = new LinkedList<>();

        public SupertypeIterator(Class<?> type) {
            if (!type.isInterface()) {
                push(type.getSuperclass());
            }
            push(type.getInterfaces());
        }

        @Override
        public final boolean hasNext() {
            return workset.size() > 0;
        }

        @Override
        public final Class<?> next() {
            if (!hasNext()) throw new NoSuchElementException();
            final Class<?> next = Objects.requireNonNull(workset.poll());
            List<Class<?>> supertypes = new ArrayList<>();
            supertypes.add(next.getSuperclass());
            supertypes.addAll(List.of(next.getInterfaces()));
            supertypes.forEach(this::push);
            return next;
        }

        private void push(final Class<?> type) {
            if (type != null && type != Object.class && visited.add(type)) {
                workset.add(type);
            }
        }

        private void push(final Class<?>... types) {
            for (int i = 0; i < types.length; ++i) {
                push(types[i]);
            }
        }

        public List<Class<?>> toList() {
            List<Class<?>> superTypes = new ArrayList<>();
            while (hasNext()) {
                superTypes.add(next());
            }
            return superTypes;
        }
    }

    // ----------------------------------------------------------

    public enum PrimitiveKind {
        PRIMITIVE, NUMERIC, INTEGRAL, FLOATING, SIGNED, UNSIGNED, DOUBLE_WORD, SINGLE_WORD, SUB_WORD, VOID
    }

    private static final Class<?>[][] PRIMITIVES = {
        {void.class, boolean.class, byte.class, short.class, char.class, int.class, long.class, float.class,
            double.class},
        {byte.class, short.class, int.class, long.class, float.class, double.class},
        {byte.class, short.class, int.class, long.class},
        {float.class, double.class},
        {byte.class, short.class, int.class, long.class, float.class, double.class},
        {boolean.class, char.class},
        {long.class, double.class},
        {int.class, float.class},
        {boolean.class, byte.class, short.class, char.class},
        {void.class}
    };


}
