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

package io.domainlifecycles.mirror.model;

import io.domainlifecycles.mirror.api.ApplicationServiceMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Model implementation of an {@link ApplicationServiceMirror}.
 *
 * @author Mario Herb
 */
public class ApplicationServiceModel extends ServiceKindModel implements ApplicationServiceMirror {

    private final List<String> applicationServiceInterfaceTypeNames;

    /**
     * Constructs an instance of ApplicationServiceModel, representing an application service type
     * with details about its fields, methods, and interfaces.
     *
     * @param typeName the fully qualified name of the application service type being represented.
     * @param isAbstract a boolean indicating whether the application service type is abstract.
     * @param allFields a list of {@code FieldMirror} instances representing all fields declared
     *                  in the application service type.
     * @param methods a list of {@code MethodMirror} instances representing all methods declared
     *                in the application service type.
     * @param applicationServiceInterfaceTypeNames a list of fully qualified names of interfaces
     *                                             that this application service type implements or uses
     *                                             for its operations.
     * @param inheritanceHierarchyTypeNames a list of fully qualified type names representing the
     *                                      inheritance hierarchy of the application service type.
     * @param allInterfaceTypeNames a list of fully qualified type names for all interfaces implemented
     *                               by the application service type or its ancestors.
     */
    public ApplicationServiceModel(String typeName,
                                   boolean isAbstract,
                                   List<FieldMirror> allFields,
                                   List<MethodMirror> methods,
                                   List<String> applicationServiceInterfaceTypeNames,
                                   List<String> inheritanceHierarchyTypeNames,
                                   List<String> allInterfaceTypeNames
    ) {
        super(typeName, isAbstract, allFields, methods, inheritanceHierarchyTypeNames, allInterfaceTypeNames);
        this.applicationServiceInterfaceTypeNames = Collections.unmodifiableList(applicationServiceInterfaceTypeNames);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomainType getDomainType() {
        return DomainType.APPLICATION_SERVICE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getApplicationServiceInterfaceTypeNames() {
        return applicationServiceInterfaceTypeNames;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ApplicationServiceModel{" +
            ", applicationServiceInterfaceTypeNames=" + applicationServiceInterfaceTypeNames +
            "} " + super.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ApplicationServiceModel that = (ApplicationServiceModel) o;
        return applicationServiceInterfaceTypeNames.equals(that.applicationServiceInterfaceTypeNames);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(
            super.hashCode(),
            applicationServiceInterfaceTypeNames
        );
    }
}