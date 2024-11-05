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

    @SuppressWarnings("all")
    /**
     * Initializes the type.
     */
    public NomnomlType(String typeName,
                       List<String> typeAssertions,
                       Optional<String> containerTypeName,
                       List<String> containerTypeAssertions) {
        this.typeName = Objects.requireNonNull(typeName);
        this.typeAssertions = Objects.requireNonNull(typeAssertions);
        this.containerTypeName = Objects.requireNonNull(containerTypeName);
        this.containerTypeAssertions = Objects.requireNonNull(containerTypeAssertions);
    }

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


    public String getTypeName() {
        return this.typeName;
    }

    public List<String> getTypeAssertions() {
        return this.typeAssertions;
    }

    public Optional<String> getContainerTypeName() {
        return this.containerTypeName;
    }

    public List<String> getContainerTypeAssertions() {
        return this.containerTypeAssertions;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof NomnomlType)) return false;
        final NomnomlType other = (NomnomlType) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$typeName = this.getTypeName();
        final Object other$typeName = other.getTypeName();
        if (this$typeName == null ? other$typeName != null : !this$typeName.equals(other$typeName)) return false;
        final Object this$typeAssertions = this.getTypeAssertions();
        final Object other$typeAssertions = other.getTypeAssertions();
        if (this$typeAssertions == null ? other$typeAssertions != null : !this$typeAssertions.equals(other$typeAssertions))
            return false;
        final Object this$containerTypeName = this.getContainerTypeName();
        final Object other$containerTypeName = other.getContainerTypeName();
        if (this$containerTypeName == null ? other$containerTypeName != null : !this$containerTypeName.equals(other$containerTypeName))
            return false;
        final Object this$containerTypeAssertions = this.getContainerTypeAssertions();
        final Object other$containerTypeAssertions = other.getContainerTypeAssertions();
        if (this$containerTypeAssertions == null ? other$containerTypeAssertions != null : !this$containerTypeAssertions.equals(other$containerTypeAssertions))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof NomnomlType;
    }

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

    public static class NomnomlTypeBuilder {
        private String typeName;
        private List<String> typeAssertions;
        private Optional<String> containerTypeName;
        private List<String> containerTypeAssertions;

        NomnomlTypeBuilder() {
        }

        public NomnomlTypeBuilder typeName(String typeName) {
            this.typeName = typeName;
            return this;
        }

        public NomnomlTypeBuilder typeAssertions(List<String> typeAssertions) {
            this.typeAssertions = typeAssertions;
            return this;
        }

        public NomnomlTypeBuilder containerTypeName(Optional<String> containerTypeName) {
            this.containerTypeName = containerTypeName;
            return this;
        }

        public NomnomlTypeBuilder containerTypeAssertions(List<String> containerTypeAssertions) {
            this.containerTypeAssertions = containerTypeAssertions;
            return this;
        }

        public NomnomlType build() {
            return new NomnomlType(this.typeName, this.typeAssertions, this.containerTypeName, this.containerTypeAssertions);
        }

        public String toString() {
            return "NomnomlType.NomnomlTypeBuilder(typeName=" + this.typeName + ", typeAssertions=" + this.typeAssertions + ", containerTypeName=" + this.containerTypeName + ", containerTypeAssertions=" + this.containerTypeAssertions + ")";
        }
    }
}
