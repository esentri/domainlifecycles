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

package io.domainlifecycles.mirror.api;

import java.util.List;

/**
 * Base mirror for all mirrored Domain types.
 *
 * @author Mario Herb
 */
public interface DomainTypeMirror extends Mirror {

    /**
     * @return the full qualified type name of the mirrored type.
     */
    String getTypeName();

    /**
     * @return the {@link DomainType} of the mirrored type.
     */
    DomainType getDomainType();

    /**
     * @return a list mirrored fields of the mirrored type (including all special references to other DomainTypes).
     */
    List<FieldMirror> getAllFields();

    /**
     * @return a list of mirrored methods.
     */
    List<MethodMirror> getMethods();

    /**
     * @param methodName the method's name
     * @return a mirrored method by the method name.
     */
    MethodMirror methodByName(String methodName);

    /**
     * @param fieldName the field name to return the field for
     * @return a mirrored field by the field name (excluding possible hidden fields).
     */
    FieldMirror fieldByName(String fieldName);

    /**
     * @return true, if the mirrored type is abstract.
     */
    boolean isAbstract();

    /**
     * @return a list of full qualified type names, that represent the
     * inheritance hierarchy of this type (extends). It is ordered according to the natural order
     * of {@code extends} from the direct superclass of this type until {@link java.lang.Object}.
     */
    List<String> getInheritanceHierarchyTypeNames();

    /**
     * @param typeName the type name to check
     * @return whether the passed typeName is contained in the direct inheritance hierarchy.
     * This does not work with interface type names, only the "extends" hierarchy is checked.
     */
    boolean isSubClassOf(String typeName);

    /**
     * Determines whether the mirrored type implements the specified interface.
     *
     * @param interfaceName the full qualified name of the interface to check
     * @return true if the mirrored type implements the specified interface, false otherwise
     */
    boolean implementsInterface(String interfaceName);

    /**
     * @return a list of full qualified type names, that represent the
     * interface list of this type (implements). It is ordered according to the definition order
     * of {@code implements}.
     */
    List<String> getAllInterfaceTypeNames();

}


