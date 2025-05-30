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
import io.domainlifecycles.mirror.api.DomainObjectMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.api.ValueObjectMirror;

import java.util.List;
import java.util.Optional;

/**
 * Model implementation of a {@link ValueObjectMirror}.
 *
 * @author Mario Herb
 */
public class ValueObjectModel extends DomainObjectModel implements ValueObjectMirror, DomainObjectMirror {

    /**
     * Constructs a new instance of ValueObjectModel.
     *
     * @param typeName the name of the type that this value object represents
     * @param isAbstract indicates whether the type is abstract
     * @param allFields a list of all fields mirrored by this value object
     * @param methods a list of methods mirrored by this value object
     * @param inheritanceHierarchyTypeNames a list of type names representing the inheritance hierarchy of the type
     * @param allInterfaceTypeNames a list of interface type names implemented by the type
     */
    @JsonCreator
    public ValueObjectModel(
        @JsonProperty("typeName") String typeName,

        @JsonProperty("abstract") boolean isAbstract,
        @JsonProperty("allFields") List<FieldMirror> allFields,
        @JsonProperty("methods") List<MethodMirror> methods,
        @JsonProperty("inheritanceHierarchyTypeNames") List<String> inheritanceHierarchyTypeNames,
        @JsonProperty("allInterfaceTypeNames") List<String> allInterfaceTypeNames
    ) {
        super(typeName, isAbstract, allFields, methods, inheritanceHierarchyTypeNames, allInterfaceTypeNames);
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public boolean isSingledValued() {
        var valueReferences = getValueReferences()
            .stream().filter(vr -> !vr.isStatic() && !vr.isHidden()).toList();

        var basicReferencesCount = getBasicFields()
            .stream()
            .filter(f -> !f.isStatic() && !f.isHidden())
            .count();

        if (valueReferences.isEmpty()) {
            return basicReferencesCount == 1;
        } else {
            if (valueReferences.size() == 1 && basicReferencesCount == 0) {
                var value = valueReferences.get(0).getValue();
                return value.isSingledValued() || value.isEnum() || value.isIdentity();
            } else {
                return false;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public Optional<FieldMirror> singledValuedField() {
        var valueReferences = getValueReferences()
            .stream().filter(vr -> !vr.isStatic() && !vr.isHidden()).toList();

        var basicReferencesCount = getBasicFields()
            .stream()
            .filter(f -> !f.isStatic() && !f.isHidden())
            .count();
        if (valueReferences.isEmpty() && basicReferencesCount == 1) {
            return
                getBasicFields()
                    .stream()
                    .filter(f -> !f.isStatic() && !f.isHidden())
                    .findFirst();
        } else {
            if (valueReferences.size() == 1 && basicReferencesCount == 0) {
                return Optional.of(valueReferences.get(0));
            } else {
                return Optional.empty();
            }
        }

    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public DomainType getDomainType() {
        return DomainType.VALUE_OBJECT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ValueObjectModel{} " + super.toString();
    }
}
