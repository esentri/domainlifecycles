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

    public String getName() {
        return this.name;
    }

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

    protected boolean canEqual(final Object other) {
        return other instanceof NomnomlStereotype;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        return result;
    }

    public static class NomnomlStereotypeBuilder {
        private String name;

        NomnomlStereotypeBuilder() {
        }

        public NomnomlStereotypeBuilder name(String name) {
            this.name = name;
            return this;
        }

        public NomnomlStereotype build() {
            return new NomnomlStereotype(this.name);
        }

        public String toString() {
            return "NomnomlStereotype.NomnomlStereotypeBuilder(name=" + this.name + ")";
        }
    }
}
