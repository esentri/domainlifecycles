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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.IdentityMirror;
import io.domainlifecycles.mirror.api.MethodMirror;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Model implementation of a {@link IdentityMirror}.
 *
 * @author Mario Herb
 */
public class IdentityModel extends DomainTypeModel implements IdentityMirror {

    private final Optional<String> valueTypeName;

    /**
     * Constructs an instance of IdentityModel.
     *
     * @param typeName the fully qualified name of the type.
     * @param isAbstract whether the type is abstract.
     * @param allFields a list of all the fields of the type.
     * @param methods a list of all the methods of the type.
     * @param valueTypeName an optional value type name associated with the identity.
     * @param inheritanceHierarchyTypeNames a list of type names representing the inheritance hierarchy.
     * @param allInterfaceTypeNames a list of type names representing all implemented interfaces.
     */
    @JsonCreator
    public IdentityModel(@JsonProperty("typeName") String typeName,
                         @JsonProperty("abstract") boolean isAbstract,
                         @JsonProperty("allFields") List<FieldMirror> allFields,
                         @JsonProperty("methods") List<MethodMirror> methods,
                         @JsonProperty("valueTypeName") Optional<String> valueTypeName,
                         @JsonProperty("inheritanceHierarchyTypeNames") List<String> inheritanceHierarchyTypeNames,
                         @JsonProperty("allInterfaceTypeNames") List<String> allInterfaceTypeNames
    ) {
        super(typeName, isAbstract, allFields, methods, inheritanceHierarchyTypeNames, allInterfaceTypeNames);
        this.valueTypeName = Objects.requireNonNull(valueTypeName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getValueTypeName() {
        return valueTypeName;
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public DomainType getDomainType() {
        return DomainType.IDENTITY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSingledValued() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<FieldMirror> singledValuedField() {
        return this.allFields
            .stream()
            .filter(p -> p.isPublicReadable() && !p.isStatic())
            .findFirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "IdentityModel{" +
            "valueTypeName='" + valueTypeName + '\'' +
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
        IdentityModel that = (IdentityModel) o;
        return valueTypeName.equals(that.valueTypeName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), valueTypeName);
    }
}
