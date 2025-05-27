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

import io.domainlifecycles.mirror.api.DomainMirror;
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
public abstract class DomainTypeModel implements DomainTypeMirror, ProvidedDomain {

    DomainMirror domainMirror;
    private boolean domainMirrorSet = false;

    /**
     * The typeName field represents the name of the type being modeled.
     * It is a non-null, immutable string uniquely identifying a specific type.
     * This field is set during the instantiation of the DomainTypeModel and remains
     * constant throughout the lifecycle of the object.
     */
    protected final String typeName;

    /**
     * Represents a list of all fields associated with the type being modeled.
     * This list includes metadata for each field, such as its name, type, and accessibility,
     * among others. The fields represented in this list are typically used to mirror the
     * structure and characteristics of a specific domain type in the system.
     *
     * This variable is immutable and initialized during the construction of the
     * {@code DomainTypeModel} or its subclasses, ensuring consistency throughout
     * the lifecycle of the model instance.
     *
     * The {@code allFields} list is expected to not be null and provides access
     * to the underlying {@link FieldMirror} instances.
     */
    protected final List<FieldMirror> allFields;

    /**
     * A list of {@link MethodMirror} instances representing the methods defined in the type being modeled.
     * Each {@link MethodMirror} mirrors a specific Java method, providing details such as its name,
     * parameters, return type, and additional metadata, including annotations relevant to domain lifecycle events.
     *
     * This field is immutable and cannot be modified once the instance of the containing class is created.
     */
    protected final List<MethodMirror> methods;

    /**
     * Indicates whether the modeled type is abstract.
     *
     * This field is used to define if the type represented by the model
     * is abstract, which means it cannot be directly instantiated and may
     * contain abstract methods that must be implemented by concrete subclasses.
     */
    protected final boolean isAbstract;

    /**
     * A list of type names that represents the inheritance hierarchy of the type being modeled.
     * This list provides the fully qualified names of all the superclasses in the hierarchy
     * starting from the modeled type up to the root of the type hierarchy.
     *
     * The list is immutable and cannot be null. It serves as an essential component in understanding
     * the class hierarchy and relationships of the modeled type within its domain.
     */
    protected final List<String> inheritanceHierarchyTypeNames;

    /**
     * Represents a list of all interface type names implemented by the type being modeled.
     * This list includes the fully qualified names of all interfaces associated with the type.
     * It is initialized when the model is constructed and remains constant throughout the lifecycle
     * of the object to ensure immutability.
     */
    protected final List<String> allInterfaceTypeNames;

    /**
     * Constructs a new instance of the DomainTypeModel.
     *
     * @param typeName the name of the type being modeled. Must not be null.
     * @param isAbstract indicates whether the type being modeled is abstract.
     * @param allFields a list of all fields in the type being modeled. Must not be null.
     * @param methods a list of methods in the type being modeled. Must not be null.
     * @param inheritanceHierarchyTypeNames a list of type names representing the inheritance hierarchy
     *                                       of the type being modeled. Must not be null.
     * @param allInterfaceTypeNames a list of all interface type names implemented by the type being modeled. Must not be null.
     */
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
        return typeName.equals(this.typeName) || this.inheritanceHierarchyTypeNames.contains(typeName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean implementsInterface(String interfaceName) {
        return allInterfaceTypeNames.contains(interfaceName);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDomainMirror(DomainMirror domainMirror) {
        if(!domainMirrorSet) {
            this.domainMirror = domainMirror;
            this.domainMirrorSet = true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean domainMirrorSet() {
        return this.domainMirrorSet;
    }
}
