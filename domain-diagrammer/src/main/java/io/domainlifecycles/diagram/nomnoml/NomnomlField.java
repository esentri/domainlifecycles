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


    public String getVisibility() {
        return this.visibility;
    }

    public String getName() {
        return this.name;
    }

    public Optional<String> getTypePrefix() {
        return this.typePrefix;
    }

    public NomnomlType getType() {
        return this.type;
    }

    public boolean isRequired() {
        return this.required;
    }

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

    protected boolean canEqual(final Object other) {
        return other instanceof NomnomlField;
    }

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

    public static class NomnomlFieldBuilder {
        private String visibility;
        private String name;
        private Optional<String> typePrefix;
        private NomnomlType type;
        private boolean required;

        NomnomlFieldBuilder() {
        }

        public NomnomlFieldBuilder visibility(String visibility) {
            this.visibility = visibility;
            return this;
        }

        public NomnomlFieldBuilder name(String name) {
            this.name = name;
            return this;
        }

        public NomnomlFieldBuilder typePrefix(Optional<String> typePrefix) {
            this.typePrefix = typePrefix;
            return this;
        }

        public NomnomlFieldBuilder type(NomnomlType type) {
            this.type = type;
            return this;
        }

        public NomnomlFieldBuilder required(boolean required) {
            this.required = required;
            return this;
        }

        public NomnomlField build() {
            return new NomnomlField(this.visibility, this.name, this.typePrefix, this.type, this.required);
        }

        public String toString() {
            return "NomnomlField.NomnomlFieldBuilder(visibility=" + this.visibility + ", name=" + this.name + ", typePrefix=" + this.typePrefix + ", type=" + this.type + ", required=" + this.required + ")";
        }
    }
}
