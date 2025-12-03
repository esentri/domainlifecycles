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

import io.domainlifecycles.mirror.api.AccessLevel;
import io.domainlifecycles.mirror.api.AssertedContainableTypeMirror;
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.EntityMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.exception.MirrorException;

import java.util.Objects;

/**
 * Model implementation of a {@link FieldMirror}.
 *
 * @author Mario Herb
 */
public class FieldModel implements FieldMirror, ProvidedDomain {

    /**
     * The name of the field represented in the model. This value is immutable and
     * uniquely identifies the field within the containing class or context.
     */
    protected final String name;
    /**
     * Represents the type of the field in the FieldModel.
     * This type is encapsulated within an {@link AssertedContainableTypeMirror},
     * which provides detailed information about constraints, assertions, and container types
     * associated with the mirrored type.
     *
     * It is a protected and final field, ensuring immutability for the field's type definition.
     */
    protected final AssertedContainableTypeMirror type;
    /**
     * Represents the access level of the field, as defined by the {@link AccessLevel} enum.
     *
     * This field is immutable, meaning its value is set at the time of object construction
     * and cannot be modified afterwards. The access level can be used to determine
     * the visibility and accessibility of the field in relation to other classes and
     * packages in compliance with Java's access control rules.
     *
     * Possible values include:
     * - {@code PUBLIC}: Accessible from any class.
     * - {@code PROTECTED}: Accessible within the same package and subclasses.
     * - {@code PRIVATE}: Accessible only within the declaring class.
     * - {@code PACKAGE}: Accessible only within the same package.
     *
     * This field cannot be null.
     */
    protected final AccessLevel accessLevel;
    /**
     * The fully qualified name of the type that declares the field.
     * Represents the owning type that contains the field definition.
     * This value is immutable and must not be null.
     */
    protected final String declaredByTypeName;
    /**
     * Indicates whether the field is publicly readable.
     * If true, the field can be accessed publicly, regardless of other constraints.
     */
    protected final boolean publicReadable;
    /**
     * Indicates whether the field is publicly writable.
     * If set to {@code true}, the field can be modified externally.
     */
    protected final boolean publicWriteable;
    /**
     * Indicates whether the field represented by this instance is modifiable.
     * A field is considered modifiable if its value can be changed after the field's
     * initial definition. This is typically used to control write access or variability
     * within the field's associated context or model.
     */
    protected final boolean modifiable;
    /**
     * Indicates whether the field is static.
     *
     * The value of this variable is true if the field is declared as static, meaning it belongs
     * to the class rather than any specific instance of the class. A static field is shared among
     * all instances of the class and can be accessed without creating an instance of the class.
     */
    protected final boolean isStatic;
    /**
     * Indicates whether the field is hidden.
     *
     * This flag is used to specify if the field is intentionally hidden from common access or visibility
     * in certain contexts. It determines whether the field should be regarded as non-exposed or omitted
     * from typical processing or presentation logic.
     */
    protected final boolean hidden;

    DomainMirror domainMirror;
    private boolean domainMirrorSet = false;

    /**
     * Constructs a new instance of the FieldModel class.
     *
     * @param name the name of the field; must not be null.
     * @param type the type of the field, represented by an AssertedContainableTypeMirror; must not be null.
     * @param accessLevel the access level of the field, as specified by the AccessLevel enum; must not be null.
     * @param declaredByTypeName the fully qualified name of the type declaring the field; must not be null.
     * @param modifiable a boolean indicating whether the field is modifiable.
     * @param publicReadable a boolean indicating whether the field is publicly readable.
     * @param publicWriteable a boolean indicating whether the field is publicly writable.
     * @param isStatic a boolean indicating whether the field is static.
     * @param hidden a boolean indicating whether the field is hidden.
     */
    public FieldModel(String name,
                      AssertedContainableTypeMirror type,
                      AccessLevel accessLevel,
                      String declaredByTypeName,
                      boolean modifiable,
                      boolean publicReadable,
                      boolean publicWriteable,
                      boolean isStatic,
                      boolean hidden) {
        this.name = Objects.requireNonNull(name);
        this.type = Objects.requireNonNull(type);
        this.accessLevel = Objects.requireNonNull(accessLevel);
        this.declaredByTypeName = Objects.requireNonNull(declaredByTypeName);
        this.modifiable = modifiable;
        this.publicReadable = publicReadable;
        this.publicWriteable = publicWriteable;
        this.isStatic = isStatic;
        this.hidden = hidden;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AssertedContainableTypeMirror getType() {
        return type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDeclaredByTypeName() {
        return declaredByTypeName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isModifiable() {
        return modifiable;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPublicReadable() {
        return publicReadable;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPublicWriteable() {
        return publicWriteable;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isStatic() {
        return isStatic;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isHidden() {
        return hidden;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isIdentityField() {
        var domainTypeMirrorOptional = domainMirror.getDomainTypeMirror(declaredByTypeName);
        if (domainTypeMirrorOptional.isPresent()) {
            var domainTypeMirror = domainTypeMirrorOptional.get();
            if (domainTypeMirror.getDomainType().equals(DomainType.AGGREGATE_ROOT)
                || domainTypeMirror.getDomainType().equals(DomainType.ENTITY)) {
                var entityMirror = (EntityMirror) domainTypeMirror;
                if (entityMirror.getIdentityField().isPresent()) {
                    return entityMirror.getIdentityField().get().equals(this);
                }
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "FieldModel{" +
            "name='" + name + '\'' +
            ", type=" + type +
            ", accessLevel=" + accessLevel +
            ", declaredByTypeName='" + declaredByTypeName + '\'' +
            ", publicReadable=" + publicReadable +
            ", publicWriteable=" + publicWriteable +
            ", modifiable=" + modifiable +
            ", static=" + isStatic +
            ", hidden=" + hidden +
            '}';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldModel that = (FieldModel) o;
        return publicReadable == that.publicReadable && publicWriteable == that.publicWriteable && modifiable == that.modifiable && name.equals(
            that.name) && type.equals(that.type) && accessLevel == that.accessLevel && declaredByTypeName.equals(
            that.declaredByTypeName) && isStatic == that.isStatic && hidden == that.hidden;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, type, accessLevel, declaredByTypeName, publicReadable, publicWriteable, modifiable,
            isStatic, hidden);
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