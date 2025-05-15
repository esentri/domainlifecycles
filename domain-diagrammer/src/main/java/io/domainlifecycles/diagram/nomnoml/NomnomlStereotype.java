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
 * The Class NomnomlStereotype encapsulates all information needed for creating a diagram text for a
 * uml stereotyp of an object.
 *
 * @author Mario Herb
 */
public class NomnomlStereotype implements DiagramElement {

    private final String name;

    /**
     * Initializes the stereotype.
     *
     * @param name of stereoptype
     */
    public NomnomlStereotype(String name) {
        this.name = Objects.requireNonNull(name);
    }

    /**
     * Creates and returns a new instance of {@link NomnomlStereotypeBuilder}.
     * The builder provides a fluent API for configuring and constructing instances
     * of {@link NomnomlStereotype}.
     *
     * @return a new {@link NomnomlStereotypeBuilder} instance for constructing {@link NomnomlStereotype} objects
     */
    public static NomnomlStereotypeBuilder builder() {
        return new NomnomlStereotypeBuilder();
    }

    /**
     * Returns the Nomnoml text representation of a stereotype for a class.
     */
    @Override
    public String getDiagramText() {
        return "<<" +
            name +
            ">>";
    }

    /**
     * Retrieves the name of the stereotype associated with this instance.
     *
     * @return the name of the stereotype
     */
    public String getName() {
        return this.name;
    }

    /**
     * Compares the specified object with this one to determine equality.
     * The comparison is based on the `name` property of the `NomnomlStereotype`
     * class and ensures type compatibility via the `canEqual` method.
     *
     * @param o the object to be compared for equality with this instance
     * @return true if the specified object*/
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof NomnomlStereotype)) return false;
        final NomnomlStereotype other = (NomnomlStereotype) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        return true;
    }

    /**
     * Determines if the provided object is eligible for equality comparison with this instance.
     *
     * @param other the object to check for compatibility with this instance
     * @return true if the provided object is an instance of NomnomlStereotype, false otherwise
     */
    protected boolean canEqual(final Object other) {
        return other instanceof NomnomlStereotype;
    }

    /**
     * Computes the hash code for this object based on its name property.
     *
     * @return an integer hash code value for this object
     */
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        return result;
    }

    /**
     * Builder class for creating instances of {@link NomnomlStereotype}.
     * Provides a fluent API for setting the properties of a {@link NomnomlStereotype}
     * and constructing a new instance.
     */
    public static class NomnomlStereotypeBuilder {
        private String name;

        NomnomlStereotypeBuilder() {
        }

        /**
         * Sets the name of the stereotype and returns the builder instance.
         *
         * @param name the name of the stereotype
         * @return this builder instance for method chaining
         */
        public NomnomlStereotypeBuilder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Builds and returns a new instance of {@link NomnomlStereotype} using the properties
         * set in this builder.
         *
         * @return a new {@link NomnomlStereotype} instance
         */
        public NomnomlStereotype build() {
            return new NomnomlStereotype(this.name);
        }

        /**
         * Returns a string representation of the NomnomlStereotypeBuilder object.
         * The returned string includes the value of the name property.
         *
         * @return a string representation of the NomnomlStereotypeBuilder instance
         */
        public String toString() {
            return "NomnomlStereotype.NomnomlStereotypeBuilder(name=" + this.name + ")";
        }
    }
}
