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
 * The Class NomnomlClass encapsulates all information needed for creating a diagram text for a uml
 * class.
 *
 * @author Mario Herb
 */
public class NomnomlClass implements DiagramElement {

    @SuppressWarnings("all")
    private final Optional<String> comment;
    private final String name;
    private final String styleClassifier;
    private final List<NomnomlField> fields;
    private final List<NomnomlMethod> methods;
    private final List<NomnomlStereotype> stereotypes;

    private final boolean showFields;

    private final boolean showMethods;

    /**
     * Constructor
     *
     * @param name            of the class
     * @param comment         to be added in diagram
     * @param styleClassifier for class
     * @param fields          contained
     * @param methods         contained
     * @param stereotypes     to be used
     * @param showFields      show fields or exclude them
     * @param showMethods     show methods or exclude them
     */
    public NomnomlClass(String name,
                        String comment,
                        String styleClassifier,
                        List<NomnomlField> fields,
                        List<NomnomlMethod> methods,
                        List<NomnomlStereotype> stereotypes,
                        boolean showFields,
                        boolean showMethods) {

        this.name = Objects.requireNonNull(name);
        this.comment = Optional.ofNullable(comment);
        this.styleClassifier = Objects.requireNonNull(styleClassifier);
        this.fields = Objects.requireNonNull(fields);
        this.methods = Objects.requireNonNull(methods);
        this.stereotypes = Objects.requireNonNull(stereotypes);
        this.showFields = showFields;
        this.showMethods = showMethods;
    }

    public static NomnomlClassBuilder builder() {
        return new NomnomlClassBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDiagramText() {
        StringBuilder builder = new StringBuilder();
        comment.ifPresent(s -> builder.append("// ").append(s).append(System.lineSeparator()));
        builder.append("[");
        builder.append(styleClassifier);
        builder.append(" ");
        builder.append(name);
        addStereotypes(builder);
        if (showFields) {
            builder.append("|");
            builder.append(System.lineSeparator());
            addFields(builder);
        }
        if (showMethods) {
            builder.append("|");
            builder.append(System.lineSeparator());
            addMethods(builder);
        }
        builder.append("]");
        builder.append(System.lineSeparator());
        return builder.toString();
    }

    private void addStereotypes(StringBuilder builder) {
        stereotypes.forEach(s -> {
            builder.append(" ");
            builder.append(s.getDiagramText());
            builder.append(" ");
        });
    }

    private void addFields(StringBuilder builder) {
        fields.forEach(f -> builder.append(f.getDiagramText()));
    }

    private void addMethods(StringBuilder builder) {
        methods.forEach(f -> builder.append(f.getDiagramText()));
    }

    public Optional<String> getComment() {
        return this.comment;
    }

    public String getName() {
        return this.name;
    }

    public String getStyleClassifier() {
        return this.styleClassifier;
    }

    public List<NomnomlField> getFields() {
        return this.fields;
    }

    public List<NomnomlMethod> getMethods() {
        return this.methods;
    }

    public List<NomnomlStereotype> getStereotypes() {
        return this.stereotypes;
    }

    public boolean isShowFields() {
        return this.showFields;
    }

    public boolean isShowMethods() {
        return this.showMethods;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof NomnomlClass)) return false;
        final NomnomlClass other = (NomnomlClass) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$comment = this.getComment();
        final Object other$comment = other.getComment();
        if (this$comment == null ? other$comment != null : !this$comment.equals(other$comment)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$styleClassifier = this.getStyleClassifier();
        final Object other$styleClassifier = other.getStyleClassifier();
        if (this$styleClassifier == null ? other$styleClassifier != null : !this$styleClassifier.equals(other$styleClassifier))
            return false;
        final Object this$fields = this.getFields();
        final Object other$fields = other.getFields();
        if (this$fields == null ? other$fields != null : !this$fields.equals(other$fields)) return false;
        final Object this$methods = this.getMethods();
        final Object other$methods = other.getMethods();
        if (this$methods == null ? other$methods != null : !this$methods.equals(other$methods)) return false;
        final Object this$stereotypes = this.getStereotypes();
        final Object other$stereotypes = other.getStereotypes();
        if (this$stereotypes == null ? other$stereotypes != null : !this$stereotypes.equals(other$stereotypes))
            return false;
        if (this.isShowFields() != other.isShowFields()) return false;
        if (this.isShowMethods() != other.isShowMethods()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof NomnomlClass;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $comment = this.getComment();
        result = result * PRIME + ($comment == null ? 43 : $comment.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $styleClassifier = this.getStyleClassifier();
        result = result * PRIME + ($styleClassifier == null ? 43 : $styleClassifier.hashCode());
        final Object $fields = this.getFields();
        result = result * PRIME + ($fields == null ? 43 : $fields.hashCode());
        final Object $methods = this.getMethods();
        result = result * PRIME + ($methods == null ? 43 : $methods.hashCode());
        final Object $stereotypes = this.getStereotypes();
        result = result * PRIME + ($stereotypes == null ? 43 : $stereotypes.hashCode());
        result = result * PRIME + (this.isShowFields() ? 79 : 97);
        result = result * PRIME + (this.isShowMethods() ? 79 : 97);
        return result;
    }

    public static class NomnomlClassBuilder {
        private String name;
        private String comment;
        private String styleClassifier;
        private List<NomnomlField> fields;
        private List<NomnomlMethod> methods;
        private List<NomnomlStereotype> stereotypes;
        private boolean showFields;
        private boolean showMethods;

        NomnomlClassBuilder() {
        }

        public NomnomlClassBuilder name(String name) {
            this.name = name;
            return this;
        }

        public NomnomlClassBuilder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public NomnomlClassBuilder styleClassifier(String styleClassifier) {
            this.styleClassifier = styleClassifier;
            return this;
        }

        public NomnomlClassBuilder fields(List<NomnomlField> fields) {
            this.fields = fields;
            return this;
        }

        public NomnomlClassBuilder methods(List<NomnomlMethod> methods) {
            this.methods = methods;
            return this;
        }

        public NomnomlClassBuilder stereotypes(List<NomnomlStereotype> stereotypes) {
            this.stereotypes = stereotypes;
            return this;
        }

        public NomnomlClassBuilder showFields(boolean showFields) {
            this.showFields = showFields;
            return this;
        }

        public NomnomlClassBuilder showMethods(boolean showMethods) {
            this.showMethods = showMethods;
            return this;
        }

        public NomnomlClass build() {
            return new NomnomlClass(this.name, this.comment, this.styleClassifier, this.fields, this.methods, this.stereotypes, this.showFields, this.showMethods);
        }

        public String toString() {
            return "NomnomlClass.NomnomlClassBuilder(name=" + this.name + ", comment=" + this.comment + ", styleClassifier=" + this.styleClassifier + ", fields=" + this.fields + ", methods=" + this.methods + ", stereotypes=" + this.stereotypes + ", showFields=" + this.showFields + ", showMethods=" + this.showMethods + ")";
        }
    }
}
