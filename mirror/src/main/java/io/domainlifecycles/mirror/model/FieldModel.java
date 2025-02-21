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
import io.domainlifecycles.mirror.api.AccessLevel;
import io.domainlifecycles.mirror.api.AssertedContainableTypeMirror;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.DomainModel;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.EntityMirror;
import io.domainlifecycles.mirror.api.FieldMirror;

import java.util.Objects;

/**
 * Model implementation of a {@link FieldMirror}.
 *
 * @author Mario Herb
 */
public class FieldModel implements FieldMirror {

    protected final String name;
    protected final AssertedContainableTypeMirror type;
    protected final AccessLevel accessLevel;
    protected final String declaredByTypeName;
    protected final boolean publicReadable;
    protected final boolean publicWriteable;
    protected final boolean modifiable;
    protected final boolean isStatic;
    protected final boolean hidden;

    DomainModel domainModel;
    private boolean domainModelSet = false;

    @JsonCreator
    public FieldModel(@JsonProperty("name") String name,
                      @JsonProperty("type") AssertedContainableTypeMirror type,
                      @JsonProperty("accessLevel") AccessLevel accessLevel,
                      @JsonProperty("declaredByTypeName") String declaredByTypeName,
                      @JsonProperty("modifiable") boolean modifiable,
                      @JsonProperty("publicReadable") boolean publicReadable,
                      @JsonProperty("publicWriteable") boolean publicWriteable,
                      @JsonProperty("static") boolean isStatic,
                      @JsonProperty("hidden") boolean hidden
    ) {
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
     *
     * @return
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
     *
     * @return
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
    @JsonIgnore
    @Override
    public boolean isIdentityField() {
        var domainTypeMirrorOptional = domainModel.getDomainTypeMirror(declaredByTypeName);
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

    public void setDomainModel(DomainModel domainModel) {
        if(!domainModelSet) {
            this.domainModel = domainModel;
            this.domainModelSet = true;
        }
    }

    public DomainModel innerDomainModelReference() {return this.domainModel;}
}
