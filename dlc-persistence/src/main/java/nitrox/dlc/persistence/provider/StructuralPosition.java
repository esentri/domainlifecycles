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

package nitrox.dlc.persistence.provider;

import nitrox.dlc.domain.types.internal.DomainObject;
import nitrox.dlc.persistence.exception.DLCPersistenceException;

import java.util.LinkedList;
import java.util.Objects;

/**
 * Represents the structural position of a domain object instance in a domain object instance access model.
 *
 * @author Mario Herb
 */
public class StructuralPosition {

    /**
     * The {@link DomainObject} instance.
     */
    public final DomainObject instance;

    /**
     * The access path from the root {@link DomainObject} instance to the {@link DomainObject} instance.
     */
    public final LinkedList<AccessPathElement> accessPathFromRoot;

    /**
     * Whether the {@link DomainObject} instance is a back reference or not.
     */
    public final boolean isBackReference;
    protected final StructuralPosition parentStructuralPosition;

    private StructuralPosition(DomainObject instance,
                               LinkedList<AccessPathElement> accessPathFromRoot,
                               boolean isBackReference,
                               StructuralPosition parentStructuralPosition) {
        this.instance = instance;
        this.accessPathFromRoot = accessPathFromRoot;
        this.isBackReference = isBackReference;
        this.parentStructuralPosition = parentStructuralPosition;
    }

    /**
     * Returns the parent {@link StructuralPosition}.
     *
     * @return the parent {@link StructuralPosition}
     */
    public static StructuralPositionBuilder builder() {
        return new StructuralPositionBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StructuralPosition that)) return false;
        return instance.equals(that.instance) && accessPathFromRoot.equals(that.accessPathFromRoot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instance, accessPathFromRoot);
    }

    @Override
    public String toString() {
        return "StructuralPosition{" +
                "instance=" + instance +
                ", accessPathFromRoot=" + accessPathFromRoot +
                ", isBackReference=" + isBackReference +
                '}';
    }

    /**
     * The builder for {@link StructuralPosition}.
     */
    public static class StructuralPositionBuilder {

        private DomainObject instance;
        private StructuralPosition parentStructuralPosition;
        private String accessorFromParent;

        /**
         * Sets the {@link DomainObject} instance.
         *
         * @param instance the {@link DomainObject} instance
         * @return the builder
         */
        public StructuralPositionBuilder withInstance(DomainObject instance) {
            this.instance = instance;
            return this;
        }

        /**
         * Sets the parent {@link StructuralPosition}.
         *
         * @param parentStructuralPosition the parent {@link StructuralPosition}
         * @return the builder
         */
        public StructuralPositionBuilder withParentStructuralPosition(StructuralPosition parentStructuralPosition) {
            this.parentStructuralPosition = parentStructuralPosition;
            return this;
        }

        /**
         * Sets the field name that provides access from parent {@link DomainObject} instance.
         *
         * @param accessor field name of parent field to access this instance
         * @return the builder
         */
        public StructuralPositionBuilder withAccessorFromParent(String accessor) {
            this.accessorFromParent = accessor;
            return this;
        }

        /**
         * Builds the {@link StructuralPosition}.
         *
         * @return the {@link StructuralPosition}
         */
        public StructuralPosition build() {
            Objects.requireNonNull(this.instance, "We need an instance to have a valid structural position model!");
            LinkedList<AccessPathElement> extendedAccessPath;
            boolean backReference = false;
            if (this.parentStructuralPosition == null) {
                extendedAccessPath = new LinkedList<>();
            } else {
                backReference = this.parentStructuralPosition.accessPathFromRoot
                        .stream()
                        .anyMatch(pe -> pe.domainObject.equals(this.instance));

                extendedAccessPath = new LinkedList<>(this.parentStructuralPosition.accessPathFromRoot);
                if (this.accessorFromParent == null) {
                    throw DLCPersistenceException.fail("We need the accessorFromParent to be able to provide a valid access model");
                } else {
                    AccessPathElement pathElement = new AccessPathElement(this.parentStructuralPosition.instance, this.accessorFromParent);
                    extendedAccessPath.add(pathElement);
                }
            }

            return new StructuralPosition(
                    this.instance,
                    extendedAccessPath,
                    backReference,
                    this.parentStructuralPosition
            );
        }


    }

    /**
     * Represents an element of the access path.
     */
    public static class AccessPathElement {

        /**
         * The {@link DomainObject} instance.
         */
        public final DomainObject domainObject;

        /**
         * The field name providing access to the next {@link DomainObject} instance in the access path.
         */
        public final String accessorToNextElement;

        /**
         * Creates a new {@link AccessPathElement}.
         *
         * @param domainObject          the {@link DomainObject} instance
         * @param accessorToNextElement the field name for access to the next {@link DomainObject} instance in the access path
         */
        public AccessPathElement(DomainObject domainObject, String accessorToNextElement) {
            this.domainObject = domainObject;
            this.accessorToNextElement = accessorToNextElement;
        }

        /**
         * {@inheritDoc}
         * @return
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof AccessPathElement that)) return false;
            return domainObject.equals(that.domainObject) && accessorToNextElement.equals(that.accessorToNextElement);
        }

        /**
         * {@inheritDoc}
         * @return
         */
        @Override
        public int hashCode() {
            return Objects.hash(domainObject, accessorToNextElement);
        }

        /**
         * {@inheritDoc}
         * @return
         */
        @Override
        public String toString() {
            return "AccessPathElement{" +
                    "domainObject=" + domainObject +
                    ", accessorToNextElement=" + accessorToNextElement +
                    '}';
        }
    }
}
