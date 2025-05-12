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

/**
 * The Class NomnomlParameter encapsulates all information needed for creating a diagram text for a uml
 * method parameter.
 *
 * @author Mario Herb
 */
public class NomnomlParameter implements DiagramElement {
    private final NomnomlType type;
    private final boolean required;

    /**
     * Initializes the parameter.
     *
     * @param type     of parameter
     * @param required is param required
     */
    public NomnomlParameter(NomnomlType type,
                            boolean required) {
        this.type = Objects.requireNonNull(type);
        this.required = required;
    }

    /**
     * Creates a new instance of the {@code NomnomlParameterBuilder} to construct and configure
     * instances of the {@code NomnomlParameter} class.
     *
     * @return a new {@code NomnomlParameterBuilder} instance for building {@code NomnomlParameter} objects
     */
    public static NomnomlParameterBuilder builder() {
        return new NomnomlParameterBuilder();
    }

    /**
     * Returns the Nomnoml text representation of a parameter.
     */
    @Override
    public String getDiagramText() {
        return (required ? "\\# " : "") + type.getDiagramText();
    }

    /**
     * Retrieves the type associated with this parameter.
     *
     * @return the NomnomlType representing the type of this parameter
     */
    public NomnomlType getType() {
        return this.type;
    }

    /**
     * Checks whether the parameter is required.
     *
     * @return true if the parameter is required, false otherwise
     */
    public boolean isRequired() {
        return this.required;
    }

    /**
     * Compares this NomnomlParameter object to the specified object for equality.
     * Two NomnomlParameter objects are considered equal if they meet the following conditions:
     * - They are the same instance.
     * - They belong to the same class and have the same type and required values.
     *
     * @param o the object to be compared with this NomnomlParameter for equality
     * @return true if the specified object is equal to this NomnomlParameter, false otherwise
     */
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof NomnomlParameter)) return false;
        final NomnomlParameter other = (NomnomlParameter) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        if (this$type == null ? other$type != null : !this$type.equals(other$type)) return false;
        if (this.isRequired() != other.isRequired()) return false;
        return true;
    }

    /**
     * Checks whether the provided object can be considered equal to an instance of this class.
     *
     * @param other the object to compare against
     * @return true if the provided object is an instance of NomnomlParameter, false otherwise
     */
    protected boolean canEqual(final Object other) {
        return other instanceof NomnomlParameter;
    }

    /**
     * Computes the hash code for this NomnomlParameter instance based on its type and required properties.
     *
     * @return the hash code value for this object.
     */
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $type = this.getType();
        result = result * PRIME + ($type == null ? 43 : $type.hashCode());
        result = result * PRIME + (this.isRequired() ? 79 : 97);
        return result;
    }

    /**
     * The NomnomlParameterBuilder class is a builder for constructing instances of the NomnomlParameter class.
     * It allows configuring the properties of a NomnomlParameter instance and creating it in a step-by-step manner.
     */
    public static class NomnomlParameterBuilder {
        private NomnomlType type;
        private boolean required;

        NomnomlParameterBuilder() {
        }

        /**
         * Sets the type for the NomnomlParameter being built.
         *
         * @param type the NomnomlType to define the type of the parameter
         * @return the current instance of NomnomlParameterBuilder for method chaining
         */
        public NomnomlParameterBuilder type(NomnomlType type) {
            this.type = type;
            return this;
        }

        /**
         * Sets whether the parameter being built is required or optional.
         *
         * @param required a boolean indicating if the parameter is required (true) or optional (false)
         * @return the current instance of NomnomlParameterBuilder for method chaining
         */
        public NomnomlParameterBuilder required(boolean required) {
            this.required = required;
            return this;
        }

        /**
         * Builds and returns an instance of the {@code NomnomlParameter} class using the properties
         * configured in this builder.
         *
         * @return a newly created {@code NomnomlParameter} instance with the configured type and required status
         */
        public NomnomlParameter build() {
            return new NomnomlParameter(this.type, this.required);
        }

        /**
         * Returns a string representation of the NomnomlParameterBuilder object.
         * The string includes information about the type and required status of the parameter being built.
         *
         * @return a string containing the type and required status of the NomnomlParameterBuilder
         */
        public String toString() {
            return "NomnomlParameter.NomnomlParameterBuilder(type=" + this.type + ", required=" + this.required + ")";
        }
    }
}
