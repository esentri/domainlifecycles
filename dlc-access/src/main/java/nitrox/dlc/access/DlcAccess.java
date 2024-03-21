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

package nitrox.dlc.access;

import nitrox.dlc.access.classes.ClassProvider;
import nitrox.dlc.access.classes.DefaultClassProvider;
import nitrox.dlc.access.object.DefaultDomainObjectAccessFactory;
import nitrox.dlc.access.object.DefaultEnumFactory;
import nitrox.dlc.access.object.DefaultIdentityFactory;
import nitrox.dlc.access.object.DomainObjectAccessFactory;
import nitrox.dlc.access.object.DynamicDomainObjectAccessor;
import nitrox.dlc.access.object.EnumFactory;
import nitrox.dlc.access.object.IdentityFactory;
import nitrox.dlc.domain.types.Identity;
import nitrox.dlc.domain.types.internal.DomainObject;

/**
 * This class provides access to classes {@link Class} as well as to new instances of {@link Enum} or {@link Identity}
 * or even access to fields of {@link DomainObject} instances.
 *
 * These accesses need to access the class loader or need to work with reflection by default.
 * This class provides a customizing mechanism to use other means, if for example reflection is not appropriate (e.g. Graal).
 *
 * @author Mario Herb
 */
public class DlcAccess {

    private static ClassProvider classProvider;

    private static EnumFactory enumFactory;

    private static IdentityFactory identityFactory;

    private static DomainObjectAccessFactory domainObjectAccessFactory;

    static {
        classProvider = new DefaultClassProvider();
        enumFactory = new DefaultEnumFactory(classProvider);
        identityFactory = new DefaultIdentityFactory(classProvider);
        domainObjectAccessFactory = new DefaultDomainObjectAccessFactory();
    }


    /**
     * To customize any access behaviour, this method must be called before any other module of dlc is initialized.
     */
    public static void customize(
        ClassProvider customClassProvider,
        EnumFactory customEnumFactory,
        IdentityFactory customIdentityFactory,
        DomainObjectAccessFactory customDomainObjectAccessFactory){
        classProvider = customClassProvider;
        enumFactory = customEnumFactory;
        identityFactory = customIdentityFactory;
        domainObjectAccessFactory = customDomainObjectAccessFactory;
    }

    /**
     * Returns a {@link Class} instance for a given full qualified class name.
     */
    public static Class<?> getClassForName(String className){
        return classProvider.getClassForName(className);
    }

    /**
     * Returns a new {@link Enum} instance by its value (Enum option) and its full qualified type name.
     */
    public static <E extends Enum<E>> E newEnumInstance(String value, String enumTypeName){
        return enumFactory.newInstance(value, enumTypeName);
    }

    /**
     * Returns a new {@link Identity} instance by its value and its full qualified type name.
     */
    public static <V, I extends Identity<V>> I newIdentityInstance(V value, String identityTypeName){
        return identityFactory.newInstance(value, identityTypeName);
    }

    /**
     * Returns {@link DynamicDomainObjectAccessor} instance to access the fields of a {@link DomainObject}
     */
    public static DynamicDomainObjectAccessor accessorFor(DomainObject domainObject) {
        return domainObjectAccessFactory.accessorFor(domainObject);
    }
}
