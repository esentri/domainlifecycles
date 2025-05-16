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

package io.domainlifecycles.diagram.nomnoml;

import io.domainlifecycles.diagram.DiagramElement;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * The Class public class NomnomlType encapsulates all information needed for creating a diagram text for a
 * type representation within a class diagram.
 *
 * @author Mario Herb
 */
public class NomnomlType implements DiagramElement {

    private final String typeName;
    private final List<String> typeAssertions;

    @SuppressWarnings("all")
    private final Optional<String> containerTypeName;
    private final List<String> containerTypeAssertions;

    /**
     * The {@code NomnomlType} constructor initializes a type with its name, type assertions,
     * container type name (if present), and container type assertions. It ensures that all
     * provided parameters are not null during initialization.
     *
     * @param typeName the name of the type to be represented
     * @param typeAssertions a list of strings representing the type assertions
     * @param containerTypeName an {@code Optional} containing the name of the container type, if specified
     * @param containerTypeAssertions a list of strings representing the container type assertions
     */
    @SuppressWarnings("all")
    public NomnomlType(String typeName,
                       List<String> typeAssertions,
                       Optional<String> containerTypeName,
                       List<String> containerTypeAssertions) {
        this.typeName = Objects.requireNonNull(typeName);
        this.typeAssertions = Objects.requireNonNull(typeAssertions);
        this.containerTypeName = Objects.requireNonNull(containerTypeName);
        this.containerTypeAssertions = Objects.requireNonNull(containerTypeAssertions);
    }

    /**
     * Creates and returns a new instance of {@code NomnomlTypeBuilder}.
     * This builder allows for constructing a {@code NomnomlType} object
     * by setting its properties in a step-by-step manner.
     *
     * @return A new {@code NomnomlTypeBuilder} instance for building {@code NomnomlType} objects.
     */
    public static NomnomlTypeBuilder builder() {
        return new NomnomlTypeBuilder();
    }

    /**
     * Returns the Nomnoml text representation of a type used in a class.
     */
    @Override
    public String getDiagramText() {
        StringBuilder builder = new StringBuilder();
        if (containerTypeName.isPresent()) {
            builder.append(containerTypeName.get());
            builder.append("<");
            builder.append(typeName
                .replaceAll("\\[", "\\\\[")
                .replaceAll("\\]", "\\\\]")
            );
            addTypeAssertions(builder);
            builder.append(">");
            addContainerTypeAssertions(builder);
        } else {
            builder.append(typeName
                .replaceAll("\\[", "\\\\[")
                .replaceAll("\\]", "\\\\]")
            );
            addTypeAssertions(builder);
        }
        return builder.toString();
    }

    private void addTypeAssertions(StringBuilder builder) {
        for (String assertion : typeAssertions) {
            builder.append(assertion);
        }
    }

    private void addContainerTypeAssertions(StringBuilder builder) {
        for (String assertion : containerTypeAssertions) {
            builder.append(assertion);
        }
    }

    /**
     * Returns the name of the type.
     *
     * @return the type name as a String.
     */
    public String getTypeName() {
        return this.typeName;
    }

    /**
     * Returns the list of type assertions associated with the type.
     *
     * @return a list of strings representing the type assertions.
     */
    public List<String> getTypeAssertions() {
        return this.typeAssertions;
    }

    /**
     * Retrieves the name of the container type, if present.
     *
     * @return an {@code Optional} containing the container type name if it exists,
     *         or an empty {@code Optional} if no container type name is specified.
     */
    public Optional<String> getContainerTypeName() {
        return this.containerTypeName;
    }

    /**
     * Retrieves the list of container type assertions associated with this type.
     *
     * @return a list of strings representing the container type assertions.
     */
    public List<String> getContainerTypeAssertions() {
        return this.containerTypeAssertions;
    }

    /**
     * Compares this instance with the specified object to determine equality.
     * The comparison includes type name, type assertions, container type name,
     * and container type assertions.
     *
     * @param o the object to compare with the current instance.
     * @return true if the specified object is equal to this instance; false otherwise.
     */
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof NomnomlType)) return false;
        final NomnomlType other = (NomnomlType) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$typeName = this.getTypeName();
        final Object other$typeName = other.getTypeName();
        if (!Objects.equals(this$typeName, other$typeName)) return false;
        final Object this$typeAssertions = this.getTypeAssertions();
        final Object other$typeAssertions = other.getTypeAssertions();
        if (!Objects.equals(this$typeAssertions, other$typeAssertions))
            return false;
        final Object this$containerTypeName = this.getContainerTypeName();
        final Object other$containerTypeName = other.getContainerTypeName();
        if (!Objects.equals(this$containerTypeName, other$containerTypeName))
            return false;
        final Object this$containerTypeAssertions = this.getContainerTypeAssertions();
        final Object other$containerTypeAssertions = other.getContainerTypeAssertions();
        if (!Objects.equals(this$containerTypeAssertions, other$containerTypeAssertions))
            return false;
        return true;
    }

    /**
     * Determines whether the specified object can be considered equal to this instance
     * by checking if it is of the same type.
     *
     * @param other the object to check for compatibility with this instance.
     * @return true if the specified object is an instance of NomnomlType; false otherwise.
     */
    protected boolean canEqual(final Object other) {
        return other instanceof NomnomlType;
    }

    /**
     * Computes the hash code for this instance of the object. The calculation
     * is based on the values of the type name, type assertions, container
     * type name, and container type assertions. This ensures consistent and
     * unique hash codes for instances with identical properties.
     *
     * @return an integer representing the hash code of this object.
     */
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $typeName = this.getTypeName();
        result = result * PRIME + ($typeName == null ? 43 : $typeName.hashCode());
        final Object $typeAssertions = this.getTypeAssertions();
        result = result * PRIME + ($typeAssertions == null ? 43 : $typeAssertions.hashCode());
        final Object $containerTypeName = this.getContainerTypeName();
        result = result * PRIME + ($containerTypeName == null ? 43 : $containerTypeName.hashCode());
        final Object $containerTypeAssertions = this.getContainerTypeAssertions();
        result = result * PRIME + ($containerTypeAssertions == null ? 43 : $containerTypeAssertions.hashCode());
        return result;
    }

    /**
     * The {@code NomnomlTypeBuilder} class provides a builder for creating instances of {@code NomnomlType}.
     * This class enables customizable construction by allowing the configuration of various properties
     * associated with a {@code NomnomlType}.
     *
     * The builder follows a step-by-step configuration pattern, allowing method chaining for setting
     * specific fields like type name, type assertions, container type name, and container type assertions.
     *
     * The constructed {@code NomnomlType} is intended to represent diagram-related types with their
     * associated metadata when working with the Nomnoml diagram generator or related workflows.
     *
     * Once all desired properties are set, the builder creates an immutable instance of {@code NomnomlType}
     * using the {@link #build()} method. The builder can also provide a string representation of its
     * current configuration state using the {@link #toString()} method.
     */
    public static class NomnomlTypeBuilder {
        private String typeName;
        private List<String> typeAssertions;
        private Optional<String> containerTypeName;
        private List<String> containerTypeAssertions;

        NomnomlTypeBuilder() {
        }

        /**
         * Sets the type name for the {@code NomnomlType} being built.
         * This method allows specifying the primary name or identifier of the type.
         *
         * @param typeName the name of the type to set
         * @return this {@code NomnomlTypeBuilder} instance for method chaining
         */
        public NomnomlTypeBuilder typeName(String typeName) {
            this.typeName = typeName;
            return this;
        }

        /**
         * Sets the type assertions for the {@code NomnomlType} being built.
         * Type assertions represent additional metadata or constraints
         * associated with the type, which can be used in the context of Nomnoml diagrams.
         *
         * @param typeAssertions a list of type assertions to set
         * @return this {@code NomnomlTypeBuilder} instance for method chaining
         */
        public NomnomlTypeBuilder typeAssertions(List<String> typeAssertions) {
            this.typeAssertions = typeAssertions;
            return this;
        }

        /**
         * Sets the container type name for the {@code NomnomlType} being built.
         * This method allows specifying the name or identifier of the container type,
         * if applicable, as an optional value.
         *
         * @param containerTypeName an {@code Optional} containing the name of the container type to set
         * @return this {@code NomnomlTypeBuilder} instance for method chaining
         */
        public NomnomlTypeBuilder containerTypeName(Optional<String> containerTypeName) {
            this.containerTypeName = containerTypeName;
            return this;
        }

        /**
         * Sets the container type assertions for the {@code NomnomlType} being built.
         * Container type assertions represent metadata or constraints associated with the container type.
         *
         * @param containerTypeAssertions a list of container type assertions to set
         * @return this {@code NomnomlTypeBuilder} instance for method chaining
         */
        public NomnomlTypeBuilder containerTypeAssertions(List<String> containerTypeAssertions) {
            this.containerTypeAssertions = containerTypeAssertions;
            return this;
        }

        /**
         * Builds and returns a new instance of {@code NomnomlType}.
         * This method gathers the provided type name, type assertions,
         * container type name, and container type assertions into a constructed
         * {@code NomnomlType} instance.
         *
         * @return a newly created {@code NomnomlType} instance configured
         *         with the specified properties in the builder.
         */
        public NomnomlType build() {
            return new NomnomlType(this.typeName, this.typeAssertions, this.containerTypeName, this.containerTypeAssertions);
        }

        /**
         * Returns a string representation of the {@code NomnomlTypeBuilder} instance.
         * The string includes the values of the type name, type assertions,
         * container type name, and container type assertions for debugging or logging purposes.
         *
         * @return a string representation of the {@code NomnomlTypeBuilder} instance
         *         with its current state.
         */
        public String toString() {
            return "NomnomlType.NomnomlTypeBuilder(typeName=" + this.typeName + ", typeAssertions=" + this.typeAssertions + ", containerTypeName=" + this.containerTypeName + ", containerTypeAssertions=" + this.containerTypeAssertions + ")";
        }
    }
}
