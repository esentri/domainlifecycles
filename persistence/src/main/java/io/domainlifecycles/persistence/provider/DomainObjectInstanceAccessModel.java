/*
 *
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

package io.domainlifecycles.persistence.provider;

import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.ValueObject;
import io.domainlifecycles.domain.types.internal.DomainObject;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.mirror.api.RecordMirror;
import org.apache.commons.collections.list.UnmodifiableList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a domain object instance access model.
 *
 * @param <RECORD> the record type
 *
 * @author Mario Herb
 */
public class DomainObjectInstanceAccessModel<RECORD> {

    /**
     * the structural position of the domain object instance in the domain object instance access model
     */
    public final StructuralPosition structuralPosition;

    /**
     * the record mirror that is used to map the domain object instance to a record
     */
    public final RecordMirror<RECORD> recordMirror;

    /**
     * the child instances of the domain object instance
     */
    public final List<DomainObjectInstanceAccessModel<RECORD>> children;

    /**
     * Whether the record can be mapped to a domain object instance or not
     *
     * @return true if the record is mapped to a domain object instance, false otherwise
     */
    public boolean isRecordMapped() {
        return this.recordMirror != null;
    }

    /**
     * Whether the domain object instance is an entity or not
     *
     * @return true if the domain object instance is an entity, false otherwise
     */
    public boolean isEntity() {
        return (domainObject() instanceof Entity);
    }

    /**
     * Whether the domain object instance is a value object or not
     *
     * @return true if the domain object instance is a value object, false otherwise
     */
    public boolean isValueObject() {
        return (domainObject() instanceof ValueObject);
    }

    /**
     * Gets the instance type of the domain object instance
     *
     * @return the instance type of the domain object instance
     */
    public Class<? extends DomainObject> instanceType() {
        return this.structuralPosition.instance.getClass();
    }

    /**
     * Gets the {@link DomainObject} instance
     *
     * @return  the {@link DomainObject} instance
     */
    public DomainObject domainObject() {
        return this.structuralPosition.instance;
    }

    /**
     * Clones the provided domain object and replaces its instance.
     *
     * @param p the domain object to be cloned and replaced
     * @return the cloned domain object with the replaced instance
     */
    public DomainObjectInstanceAccessModel<RECORD> cloneWithReplacement(DomainObject p) {
        if (p == null || !p.equals(this.structuralPosition.instance)) {
            throw DLCPersistenceException.fail("It's not allowed to call 'cloneWithReplacement' with another domainObject than "
                + this.structuralPosition.instance + ". Was called with " + p);
        }

        StructuralPosition.StructuralPositionBuilder replacedStructuralPosition = StructuralPosition.builder()
            .withParentStructuralPosition(this.structuralPosition.parentStructuralPosition)
            .withInstance(p);
        if (!this.structuralPosition.accessPathFromRoot.isEmpty()) {
            var accessor = this.structuralPosition.accessPathFromRoot.getLast().accessorToNextElement;
            replacedStructuralPosition.withAccessorFromParent(accessor);
        }
        DomainObjectInstanceAccessModelBuilder<RECORD> cloneBuilder = DomainObjectInstanceAccessModel.builder()
            .withStructuralPosition(replacedStructuralPosition.build())
            .withRecordMirror(this.recordMirror);
        this.children.forEach(cloneBuilder::withChildInstance);
        return cloneBuilder.build();
    }

    /**
     * Gets the child instances of the domain object instance
     *
     * @return the child instances of the domain object instance
     */
    public Set<DomainObjectInstanceAccessModel<RECORD>> getAllContainedInstances() {
        Set<DomainObjectInstanceAccessModel<RECORD>> contained = new HashSet<>();
        contained.add(this);
        for (DomainObjectInstanceAccessModel<RECORD> child : children) {
            contained.add(child);
            contained.addAll(child.getAllContainedInstances());
        }
        return contained;
    }

    private DomainObjectInstanceAccessModel(StructuralPosition structuralPosition,
                                            RecordMirror<RECORD> recordMirror,
                                            List<DomainObjectInstanceAccessModel<RECORD>> children
    ) {
        this.structuralPosition = structuralPosition;
        this.recordMirror = recordMirror;
        this.children = children;
    }

    /**
     * Creates a new {@link DomainObjectInstanceAccessModelBuilder}
     *
     * @return a new {@link DomainObjectInstanceAccessModelBuilder}
     */
    public static DomainObjectInstanceAccessModelBuilder builder() {
        return new DomainObjectInstanceAccessModelBuilder<>();
    }

    /**
     * Builder for {@link DomainObjectInstanceAccessModel}
     *
     * @param <RECORD> the record type
     */
    public static class DomainObjectInstanceAccessModelBuilder<RECORD> {
        private StructuralPosition structuralPosition;
        private RecordMirror<RECORD> recordMirror;
        private final List<DomainObjectInstanceAccessModel<RECORD>> children = new ArrayList<>();

        /**
         * Sets the record mirror
         *
         * @param recordMirror the record mirror
         * @return this builder
         */
        public DomainObjectInstanceAccessModelBuilder<RECORD> withRecordMirror(RecordMirror<RECORD> recordMirror) {
            this.recordMirror = recordMirror;
            return this;
        }

        /**
         * Sets the structural position
         *
         * @param structuralPosition the structural position
         * @return this builder
         */
        public DomainObjectInstanceAccessModelBuilder<RECORD> withStructuralPosition(StructuralPosition structuralPosition) {
            this.structuralPosition = structuralPosition;
            return this;
        }

        /**
         * Adds a child instance
         *
         * @param childInstance the child instance
         * @return this builder
         */
        public DomainObjectInstanceAccessModelBuilder<RECORD> withChildInstance(DomainObjectInstanceAccessModel<RECORD> childInstance) {
            this.children.add(childInstance);
            return this;
        }

        /**
         * Builds the {@link DomainObjectInstanceAccessModel}
         *
         * @return the {@link DomainObjectInstanceAccessModel}
         */
        public DomainObjectInstanceAccessModel<RECORD> build() {
            Objects.requireNonNull(this.structuralPosition, "We need a structural position to have a valid access model!");

            return new DomainObjectInstanceAccessModel<RECORD>(
                this.structuralPosition,
                this.recordMirror,
                UnmodifiableList.decorate(this.children)
            );
        }

    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainObjectInstanceAccessModel that)) return false;
        return structuralPosition.equals(that.structuralPosition);
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(structuralPosition);
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public String toString() {
        return "DomainObjectInstanceAccessModel{" +
            "structuralPosition=" + structuralPosition.toString() +
            '}';
    }
}
