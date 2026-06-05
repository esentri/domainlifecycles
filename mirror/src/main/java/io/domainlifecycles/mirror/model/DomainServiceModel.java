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

import io.domainlifecycles.mirror.api.DomainServiceMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Model implementation of a {@link DomainServiceMirror}.
 *
 * @author Mario Herb
 */
public class DomainServiceModel extends ServiceKindModel implements DomainServiceMirror {

    private final List<String> domainServiceInterfaceTypeNames;

    /**
     * Constructs a new instance of the DomainServiceModel.
     *
     * @param typeName the fully qualified name of the type represented by this model.
     * @param isAbstract a boolean indicating whether the represented type is abstract.
     * @param allFields a list of {@code FieldMirror} instances representing all fields in the type.
     * @param methods a list of {@code MethodMirror} instances representing all methods in the type.
     * @param domainServiceInterfaceTypeNames a list of fully qualified type names representing all
     *                                         domain service interfaces implemented by the represented type.
     * @param inheritanceHierarchyTypeNames a list of fully qualified type names representing the inheritance
     *                                       hierarchy of the represented type.
     * @param allInterfaceTypeNames a list of fully qualified type names representing all interfaces implemented
     *                               by the represented type.
     */
    public DomainServiceModel(String typeName,
                              boolean isAbstract,
                              List<FieldMirror> allFields,
                              List<MethodMirror> methods,
                              List<String> domainServiceInterfaceTypeNames,
                              List<String> inheritanceHierarchyTypeNames,
                              List<String> allInterfaceTypeNames
    ) {
        super(typeName, isAbstract, allFields, methods, inheritanceHierarchyTypeNames, allInterfaceTypeNames);
        Objects.requireNonNull(domainServiceInterfaceTypeNames);
        this.domainServiceInterfaceTypeNames = Collections.unmodifiableList(domainServiceInterfaceTypeNames);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomainType getDomainType() {
        return DomainType.DOMAIN_SERVICE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getDomainServiceInterfaceTypeNames() {
        return domainServiceInterfaceTypeNames;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "DomainServiceModel{" +
            ", domainServiceInterfaceTypeNames=" + domainServiceInterfaceTypeNames +
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
        DomainServiceModel that = (DomainServiceModel) o;
        return domainServiceInterfaceTypeNames.equals(that.domainServiceInterfaceTypeNames);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(
            super.hashCode(),
            domainServiceInterfaceTypeNames
        );
    }
}
