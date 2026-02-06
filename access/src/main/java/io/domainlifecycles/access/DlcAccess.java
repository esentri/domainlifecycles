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

package io.domainlifecycles.access;

import io.domainlifecycles.access.classes.ClassProvider;
import io.domainlifecycles.access.classes.DefaultClassProvider;
import io.domainlifecycles.access.object.DefaultDomainObjectAccessFactory;
import io.domainlifecycles.access.object.DefaultEnumFactory;
import io.domainlifecycles.access.object.DefaultIdentityFactory;
import io.domainlifecycles.access.object.DefaultSingleValuedValueObjectFactory;
import io.domainlifecycles.access.object.DomainObjectAccessFactory;
import io.domainlifecycles.access.object.DynamicDomainObjectAccessor;
import io.domainlifecycles.access.object.EnumFactory;
import io.domainlifecycles.access.object.IdentityFactory;
import io.domainlifecycles.access.object.SingleValuedValueObjectFactory;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.ValueObject;
import io.domainlifecycles.domain.types.internal.DomainObject;

/**
 * This class provides access to classes {@link Class} as well as to new instances of {@link Enum} or {@link Identity}
 * or even access to fields of {@link DomainObject} instances.
 * <p>
 * These accesses need to access the class loader or need to work with reflection by default.
 * This class provides a customizing mechanism to use other means, if, for example, reflection is not appropriate (e.g.
 * Graal).
 *
 * @author Mario Herb
 */
public class DlcAccess {

    private static ClassProvider classProvider;

    private static EnumFactory enumFactory;

    private static IdentityFactory identityFactory;

    private static SingleValuedValueObjectFactory singleValuedValueObjectFactory;

    private static DomainObjectAccessFactory domainObjectAccessFactory;

    static {
        classProvider = new DefaultClassProvider();
        enumFactory = new DefaultEnumFactory(classProvider);
        identityFactory = new DefaultIdentityFactory(classProvider);
        singleValuedValueObjectFactory = new DefaultSingleValuedValueObjectFactory(classProvider);
        domainObjectAccessFactory = new DefaultDomainObjectAccessFactory();
    }


    /**
     * To customize any access behaviour, this method must be called before any other module of dlc is initialized.
     *
     * @param customClassProvider             class provider
     * @param customEnumFactory               enum factory
     * @param customIdentityFactory           identity factory
     * @param customDomainObjectAccessFactory domain object access factory
     */
    public static void customize(
        ClassProvider customClassProvider,
        EnumFactory customEnumFactory,
        IdentityFactory customIdentityFactory,
        DomainObjectAccessFactory customDomainObjectAccessFactory) {
        classProvider = customClassProvider;
        enumFactory = customEnumFactory;
        identityFactory = customIdentityFactory;
        domainObjectAccessFactory = customDomainObjectAccessFactory;
    }

    /**
     * @param className full qualified class name
     * @return {@link Class} instance for a given full qualified class name.
     */
    public static Class<?> getClassForName(String className) {
        return classProvider.getClassForName(className);
    }

    /**
     * @param <E>          type of enum
     * @param value        enum value
     * @param enumTypeName full qualified name of enum
     * @return a new {@link Enum} instance by its value (Enum option) and its full qualified type name.
     */
    public static <E extends Enum<E>> E newEnumInstance(String value, String enumTypeName) {
        return enumFactory.newInstance(value, enumTypeName);
    }

    /**
     * @param <I>              type of Identity
     * @param <V>              type of value
     * @param value            the value for the Identity instance
     * @param identityTypeName full qualified Identity type name
     * @return a new {@link Identity} instance by its value and its full qualified type name.
     */
    public static <V, I extends Identity<V>> I newIdentityInstance(V value, String identityTypeName) {
        return identityFactory.newInstance(value, identityTypeName);
    }

    /**
     * @param <VO>              type of single valued ValueObject
     * @param <V>              type of value
     * @param value            the value for the ValueObject instance
     * @param valueObjectTypeName full qualified ValueObject type name
     * @return a new {@link ValueObject} instance by its value and its full qualified type name.
     */
    public static <V, VO extends ValueObject> VO newSingleValuedValueObjectInstance(V value, String valueObjectTypeName) {
        return singleValuedValueObjectFactory.newInstance(value, valueObjectTypeName);
    }

    /**
     * @param domainObject DomainObject to return an accessor for
     * @return {@link DynamicDomainObjectAccessor} instance to access the fields of a {@link DomainObject}
     */
    public static DynamicDomainObjectAccessor accessorFor(DomainObject domainObject) {
        return domainObjectAccessFactory.accessorFor(domainObject);
    }
}
