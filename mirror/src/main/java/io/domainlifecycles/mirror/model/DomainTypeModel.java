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
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.exception.MirrorException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Model implementation of a {@link DomainTypeMirror}.
 *
 * @author Mario Herb
 */
public abstract class DomainTypeModel implements DomainTypeMirror {

    protected final String typeName;
    protected final List<FieldMirror> allFields;
    protected final List<MethodMirror> methods;
    protected final boolean isAbstract;
    protected final List<String> inheritanceHierarchyTypeNames;
    protected final List<String> allInterfaceTypeNames;

    public DomainTypeModel(String typeName,
                           boolean isAbstract,
                           List<FieldMirror> allFields,
                           List<MethodMirror> methods,
                           List<String> inheritanceHierarchyTypeNames,
                           List<String> allInterfaceTypeNames) {
        this.typeName = Objects.requireNonNull(typeName);
        this.isAbstract = isAbstract;
        Objects.requireNonNull(allFields);
        this.allFields = Collections.unmodifiableList(allFields);
        Objects.requireNonNull(methods);
        this.methods = Collections.unmodifiableList(methods);
        Objects.requireNonNull(inheritanceHierarchyTypeNames);
        this.inheritanceHierarchyTypeNames = Collections.unmodifiableList(inheritanceHierarchyTypeNames);
        Objects.requireNonNull(allInterfaceTypeNames);
        this.allInterfaceTypeNames = Collections.unmodifiableList(allInterfaceTypeNames);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTypeName() {
        return typeName;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public abstract DomainType getDomainType();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FieldMirror> getAllFields() {
        return allFields;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MethodMirror> getMethods() {
        return methods;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MethodMirror methodByName(String methodName) {
        return methods.stream().filter(m -> m.getName().equals(methodName))
            .findFirst()
            .orElseThrow(() -> MirrorException.fail(
                String.format("MethodMirror not found for method name '%s' within '%s'", methodName, typeName)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FieldMirror fieldByName(String fieldName) {
        return allFields.stream()
            .filter(f -> f.getName().equals(fieldName) && !f.isHidden()).findFirst()
            .orElseThrow(() -> MirrorException.fail(
                String.format("FieldMirror not found for field name '%s' within '%s'", fieldName, typeName)));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAbstract() {
        return isAbstract;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getInheritanceHierarchyTypeNames() {
        return inheritanceHierarchyTypeNames;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSubClassOf(String typeName) {
        return inheritanceHierarchyTypeNames.contains(typeName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAllInterfaceTypeNames() {
        return allInterfaceTypeNames;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "DomainTypeModel{" +
            "typeName='" + typeName + '\'' +
            ", allFields=" + allFields +
            ", methods=" + methods +
            ", isAbstract=" + isAbstract +
            ", inheritanceHierarchyTypeNames=" + inheritanceHierarchyTypeNames +
            ", allInterfaceTypeNames=" + allInterfaceTypeNames +
            '}';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomainTypeModel that = (DomainTypeModel) o;
        return isAbstract == that.isAbstract && typeName.equals(that.typeName) && allFields.equals(
            that.allFields) && methods.equals(that.methods) && inheritanceHierarchyTypeNames.equals(
            that.inheritanceHierarchyTypeNames) && allInterfaceTypeNames.equals(that.allInterfaceTypeNames);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(typeName, allFields, methods, isAbstract, inheritanceHierarchyTypeNames,
            allInterfaceTypeNames);
    }
}
