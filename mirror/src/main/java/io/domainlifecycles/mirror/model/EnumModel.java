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
import io.domainlifecycles.mirror.api.EnumMirror;
import io.domainlifecycles.mirror.api.EnumOptionMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Model implementation of a {@link EnumMirror}.
 *
 * @author Mario Herb
 */
public class EnumModel extends DomainTypeModel implements EnumMirror {

    private final List<EnumOptionMirror> enumOptions;

    @JsonCreator
    public EnumModel(@JsonProperty("typeName") String typeName,
                     @JsonProperty("abstract") boolean isAbstract,
                     @JsonProperty("allFields") List<FieldMirror> allFields,
                     @JsonProperty("methods") List<MethodMirror> methods,
                     @JsonProperty("enumOptions") List<EnumOptionMirror> enumOptions,
                     @JsonProperty("inheritanceHierarchyTypeNames") List<String> inheritanceHierarchyTypeNames,
                     @JsonProperty("allInterfaceTypeNames") List<String> allInterfaceTypeNames
    ) {
        super(typeName, isAbstract, allFields, methods, inheritanceHierarchyTypeNames, allInterfaceTypeNames);
        Objects.requireNonNull(enumOptions);
        this.enumOptions = Collections.unmodifiableList(enumOptions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSingledValued() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<FieldMirror> singledValuedField() {
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EnumOptionMirror> getEnumOptions() {
        return enumOptions;
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public DomainType getDomainType() {
        return DomainType.ENUM;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "EnumModel{" +
            "enumOptions=" + enumOptions +
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
        EnumModel enumModel = (EnumModel) o;
        return enumOptions.equals(enumModel.enumOptions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), enumOptions);
    }
}
