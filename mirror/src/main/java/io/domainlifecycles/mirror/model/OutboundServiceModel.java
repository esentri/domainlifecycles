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

import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.api.OutboundServiceMirror;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Model implementation of a {@link OutboundServiceMirror}.
 *
 * @author Mario Herb
 */
public class OutboundServiceModel extends ServiceKindModel implements OutboundServiceMirror {

    private final List<String> outboundServiceInterfaceTypeNames;

    /**
     * Constructs a new instance of the OutboundServiceModel, representing the model
     * for an outbound service type with its fields, methods, and type information.
     *
     * @param typeName the fully qualified name of the type represented by this model.
     * @param isAbstract a boolean indicating whether the represented type is abstract.
     * @param allFields a list of {@code FieldMirror} instances representing all fields in the type.
     * @param methods a list of {@code MethodMirror} instances representing all methods in the type.
     * @param outboundServiceInterfaceTypeNames a list of fully qualified type names representing the outbound service interfaces.
     * @param inheritanceHierarchyTypeNames a list of fully qualified type names representing the inheritance hierarchy of the represented type.
     * @param allInterfaceTypeNames a list of fully qualified type names representing all interfaces implemented by the represented type.
     */
    public OutboundServiceModel(
        String typeName,
        boolean isAbstract,
        List<FieldMirror> allFields,
        List<MethodMirror> methods,
        List<String> outboundServiceInterfaceTypeNames,
        List<String> inheritanceHierarchyTypeNames,
        List<String> allInterfaceTypeNames
    ) {
        super(typeName, isAbstract, allFields, methods, inheritanceHierarchyTypeNames, allInterfaceTypeNames);
        this.outboundServiceInterfaceTypeNames = Collections.unmodifiableList(outboundServiceInterfaceTypeNames);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getOutboundServiceInterfaceTypeNames() {
        return outboundServiceInterfaceTypeNames;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomainType getDomainType() {
        return DomainType.OUTBOUND_SERVICE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "OutboundServiceModel{" +
            "outboundServiceInterfaceTypeNames=" + outboundServiceInterfaceTypeNames +
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
        OutboundServiceModel that = (OutboundServiceModel) o;
        return outboundServiceInterfaceTypeNames.equals(that.getOutboundServiceInterfaceTypeNames());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), outboundServiceInterfaceTypeNames);
    }
}