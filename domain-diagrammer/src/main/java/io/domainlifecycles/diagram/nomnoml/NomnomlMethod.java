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
import java.util.stream.Collectors;

/**
 * The Class NomnomlMethod encapsulates all information needed for creating a diagram text for a uml
 * method.
 *
 * @author Mario Herb
 */
public class NomnomlMethod implements DiagramElement {
    private final String visibility;
    private final String name;
    private final NomnomlType returnType;
    private final List<NomnomlParameter> parameters;

    /**
     * Initializes the method.
     *
     * @param visibility of method
     * @param name       of method
     * @param returnType of method
     * @param parameters of method
     */
    public NomnomlMethod(String visibility,
                         String name,
                         NomnomlType returnType,
                         List<NomnomlParameter> parameters) {
        this.visibility = Objects.requireNonNull(visibility);
        this.name = Objects.requireNonNull(name);
        this.returnType = Objects.requireNonNull(returnType);
        this.parameters = Objects.requireNonNull(parameters);
    }

    public static NomnomlMethodBuilder builder() {
        return new NomnomlMethodBuilder();
    }

    /**
     * Returns the Nomnoml text representation of a method.
     */
    @Override
    public String getDiagramText() {
        StringBuilder builder = new StringBuilder();
        builder.append(visibility);
        builder.append(" ");
        builder.append(returnType.getDiagramText());
        builder.append(" ");
        builder.append(name);
        builder.append("(");
        addParameters(builder);
        builder.append(")");
        builder.append(System.lineSeparator());
        return builder.toString();
    }

    private void addParameters(StringBuilder builder) {
        builder.append(
            parameters
                .stream()
                .map(NomnomlParameter::getDiagramText)
                .collect(Collectors.joining(","))
        );
    }

    public String getVisibility() {
        return this.visibility;
    }

    public String getName() {
        return this.name;
    }

    public NomnomlType getReturnType() {
        return this.returnType;
    }

    public List<NomnomlParameter> getParameters() {
        return this.parameters;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof NomnomlMethod)) return false;
        final NomnomlMethod other = (NomnomlMethod) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$visibility = this.getVisibility();
        final Object other$visibility = other.getVisibility();
        if (this$visibility == null ? other$visibility != null : !this$visibility.equals(other$visibility))
            return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$returnType = this.getReturnType();
        final Object other$returnType = other.getReturnType();
        if (this$returnType == null ? other$returnType != null : !this$returnType.equals(other$returnType))
            return false;
        final Object this$parameters = this.getParameters();
        final Object other$parameters = other.getParameters();
        if (this$parameters == null ? other$parameters != null : !this$parameters.equals(other$parameters))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof NomnomlMethod;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $visibility = this.getVisibility();
        result = result * PRIME + ($visibility == null ? 43 : $visibility.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $returnType = this.getReturnType();
        result = result * PRIME + ($returnType == null ? 43 : $returnType.hashCode());
        final Object $parameters = this.getParameters();
        result = result * PRIME + ($parameters == null ? 43 : $parameters.hashCode());
        return result;
    }

    public static class NomnomlMethodBuilder {
        private String visibility;
        private String name;
        private NomnomlType returnType;
        private List<NomnomlParameter> parameters;

        NomnomlMethodBuilder() {
        }

        public NomnomlMethodBuilder visibility(String visibility) {
            this.visibility = visibility;
            return this;
        }

        public NomnomlMethodBuilder name(String name) {
            this.name = name;
            return this;
        }

        public NomnomlMethodBuilder returnType(NomnomlType returnType) {
            this.returnType = returnType;
            return this;
        }

        public NomnomlMethodBuilder parameters(List<NomnomlParameter> parameters) {
            this.parameters = parameters;
            return this;
        }

        public NomnomlMethod build() {
            return new NomnomlMethod(this.visibility, this.name, this.returnType, this.parameters);
        }

        public String toString() {
            return "NomnomlMethod.NomnomlMethodBuilder(visibility=" + this.visibility + ", name=" + this.name + ", returnType=" + this.returnType + ", parameters=" + this.parameters + ")";
        }
    }
}
