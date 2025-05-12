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

import java.util.Objects;
import java.util.Optional;

/**
 * The Class NomnomlField encapsulates all information needed for creating a diagram text for a uml
 * field of a class.
 *
 * @author Mario Herb
 */
public class NomnomlField implements DiagramElement {
    private final String visibility;
    private final String name;

    private final Optional<String> typePrefix;
    private final NomnomlType type;
    private final boolean required;

    /**
     * Initializes the field.
     *
     * @param visibility of the field
     * @param name       of field
     * @param typePrefix prefix
     * @param type       of field
     * @param required   property
     */
    public NomnomlField(String visibility,
                        String name,
                        Optional<String> typePrefix,
                        NomnomlType type,
                        boolean required) {
        this.visibility = Objects.requireNonNull(visibility);
        this.name = Objects.requireNonNull(name);
        this.typePrefix = Objects.requireNonNull(typePrefix);
        this.type = Objects.requireNonNull(type);
        this.required = required;
    }

    /**
     * Creates and returns a new {@code NomnomlFieldBuilder} instance, which can be used to construct
     * {@code NomnomlField} objects with specific attributes.
     *
     * @return a new instance of {@code NomnomlFieldBuilder}
     */
    public static NomnomlFieldBuilder builder() {
        return new NomnomlFieldBuilder();
    }

    /**
     * Returns the Nomnoml text representation of a field.
     */
    @Override
    public String getDiagramText() {
        StringBuilder builder = new StringBuilder();
        builder.append(required ? "\\# " : "o ");
        builder.append(visibility);
        builder.append(" ");
        builder.append(name);
        builder.append(":");
        typePrefix.ifPresent(s -> builder.append(s).append(" "));
        builder.append(type.getDiagramText());
        builder.append(System.lineSeparator());
        return builder.toString();
    }

    /**
     * Retrieves the visibility of the field.
     *
     * @return the visibility of the field as a String
     */
    public String getVisibility() {
        return this.visibility;
    }

    /**
     * Retrieves the name of the field.
     *
     * @return the name of the field as a String
     */
    public String getName() {
        return this.name;
    }

    /**
     * Retrieves the optional type prefix of the field.
     *
     * @return an {@link Optional} containing the type prefix if present, otherwise an empty {@link Optional}
     */
    public Optional<String> getTypePrefix() {
        return this.typePrefix;
    }

    /**
     * Retrieves the type of the field.
     *
     * @return the {@link NomnomlType} representing the type of the field
     */
    public NomnomlType getType() {
        return this.type;
    }

    /**
     * Checks if the field is marked as required.
     *
     * @return true if the field is required, false otherwise
     */
    public boolean isRequired() {
        return this.required;
    }

    /**
     * Compares this object with the specified object for equality. Provides a detailed equality check
     * by comparing the properties of this instance with the specified object.
     *
     * @param o the object to be compared for equality with this instance
     * @return true if the specified object is equal to this instance, false otherwise
     */
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof NomnomlField)) return false;
        final NomnomlField other = (NomnomlField) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$visibility = this.getVisibility();
        final Object other$visibility = other.getVisibility();
        if (this$visibility == null ? other$visibility != null : !this$visibility.equals(other$visibility))
            return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$typePrefix = this.getTypePrefix();
        final Object other$typePrefix = other.getTypePrefix();
        if (this$typePrefix == null ? other$typePrefix != null : !this$typePrefix.equals(other$typePrefix))
            return false;
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        if (this$type == null ? other$type != null : !this$type.equals(other$type)) return false;
        if (this.isRequired() != other.isRequired()) return false;
        return true;
    }

    /**
     * Checks whether the specified object can be considered equal to the current instance.
     *
     * @param other the object to compare with the current instance
     * @return true if the provided object is an instance of NomnomlField, false otherwise
     */
    protected boolean canEqual(final Object other) {
        return other instanceof NomnomlField;
    }

    /**
     * Computes the hash code for the object based on its properties.
     *
     * @return the computed hash code for the object
     */
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $visibility = this.getVisibility();
        result = result * PRIME + ($visibility == null ? 43 : $visibility.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $typePrefix = this.getTypePrefix();
        result = result * PRIME + ($typePrefix == null ? 43 : $typePrefix.hashCode());
        final Object $type = this.getType();
        result = result * PRIME + ($type == null ? 43 : $type.hashCode());
        result = result * PRIME + (this.isRequired() ? 79 : 97);
        return result;
    }

    /**
     * Builder class for constructing instances of {@link NomnomlField}.
     * <p>
     * This class provides a fluent API for setting the attributes of a {@link NomnomlField}.
     * Each method allows setting a specific field property and returns the builder instance,
     * enabling method chaining for easily constructing {@link NomnomlField} objects with the desired properties.
     */
    public static class NomnomlFieldBuilder {
        private String visibility;
        private String name;
        private Optional<String> typePrefix;
        private NomnomlType type;
        private boolean required;

        NomnomlFieldBuilder() {
        }

        /**
         * Sets the visibility of the field and returns the updated builder instance.
         *
         * @param visibility the visibility of the field (e.g., public, private, protected)
         * @return the updated {@code NomnomlFieldBuilder} instance
         */
        public NomnomlFieldBuilder visibility(String visibility) {
            this.visibility = visibility;
            return this;
        }

        /**
         * Sets the name of the field and returns the updated builder instance.
         *
         * @param name the name of the field
         * @return the updated {@code NomnomlFieldBuilder} instance
         */
        public NomnomlFieldBuilder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the type prefix of the field and returns the updated builder instance.
         *
         * @param typePrefix an {@code Optional} containing the type prefix for the field, or an empty {@code Optional} if no prefix is required
         * @return the updated {@code NomnomlFieldBuilder} instance
         */
        public NomnomlFieldBuilder typePrefix(Optional<String> typePrefix) {
            this.typePrefix = typePrefix;
            return this;
        }

        /**
         * Sets the type of the field and returns the updated builder instance.
         *
         * @param type the {@code NomnomlType} representing the type of the field
         * @return the updated {@code NomnomlFieldBuilder} instance
         */
        public NomnomlFieldBuilder type(NomnomlType type) {
            this.type = type;
            return this;
        }

        /**
         * Sets whether the field is required and returns the updated builder instance.
         *
         * @param required a boolean indicating if the field is required
         * @return the updated {@code NomnomlFieldBuilder} instance
         */
        public NomnomlFieldBuilder required(boolean required) {
            this.required = required;
            return this;
        }

        /**
         * Creates and returns a new instance of {@link NomnomlField} based on the current state
         * of this {@code NomnomlFieldBuilder}.
         *
         * @return a new instance of {@link NomnomlField} populated with the values configured
         *         in the builder
         */
        public NomnomlField build() {
            return new NomnomlField(this.visibility, this.name, this.typePrefix, this.type, this.required);
        }

        /**
         * Returns a string representation of the {@code NomnomlFieldBuilder} instance, including the
         * state of its fields such as visibility, name, type prefix, type, and required status.
         *
         * @return a string representation of this {@code NomnomlFieldBuilder} instance with its field values
         */
        public String toString() {
            return "NomnomlField.NomnomlFieldBuilder(visibility=" + this.visibility + ", name=" + this.name + ", typePrefix=" + this.typePrefix + ", type=" + this.type + ", required=" + this.required + ")";
        }
    }
}
