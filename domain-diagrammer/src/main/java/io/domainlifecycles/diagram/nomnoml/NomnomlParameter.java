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

    public NomnomlType getType() {
        return this.type;
    }

    public boolean isRequired() {
        return this.required;
    }

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

    protected boolean canEqual(final Object other) {
        return other instanceof NomnomlParameter;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $type = this.getType();
        result = result * PRIME + ($type == null ? 43 : $type.hashCode());
        result = result * PRIME + (this.isRequired() ? 79 : 97);
        return result;
    }

    public static class NomnomlParameterBuilder {
        private NomnomlType type;
        private boolean required;

        NomnomlParameterBuilder() {
        }

        public NomnomlParameterBuilder type(NomnomlType type) {
            this.type = type;
            return this;
        }

        public NomnomlParameterBuilder required(boolean required) {
            this.required = required;
            return this;
        }

        public NomnomlParameter build() {
            return new NomnomlParameter(this.type, this.required);
        }

        public String toString() {
            return "NomnomlParameter.NomnomlParameterBuilder(type=" + this.type + ", required=" + this.required + ")";
        }
    }
}
