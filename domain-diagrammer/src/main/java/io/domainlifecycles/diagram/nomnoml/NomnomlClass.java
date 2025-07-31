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

    /**
     * Creates and returns a new instance of the {@code NomnomlClassBuilder}.
     * The builder allows for constructing instances of {@code NomnomlClass}.
     *
     * @return a new {@code NomnomlClassBuilder} instance to construct a {@code NomnomlClass}.
     */
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

    /**
     * Retrieves the optional comment associated with the Nomnoml class.
     *
     * @return an {@code Optional<String>} representing the comment, if it exists.
     */
    public Optional<String> getComment() {
        return this.comment;
    }

    /**
     * Retrieves the name of the Nomnoml class.
     *
     * @return a {@code String} representing the name of this class.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Retrieves the style classifier associated with the Nomnoml class.
     *
     * @return a {@code String} value representing the style classifier of the class.
     */
    public String getStyleClassifier() {
        return this.styleClassifier;
    }

    /**
     * Retrieves the list of fields associated with the Nomnoml class.
     *
     * @return a list of {@code NomnomlField} objects that represent the fields defined for this class.
     */
    public List<NomnomlField> getFields() {
        return this.fields;
    }

    /**
     * Retrieves the list of methods associated with the Nomnoml class.
     *
     * @return a list of {@code NomnomlMethod} objects that represent the methods
     *         defined for this class.
     */
    public List<NomnomlMethod> getMethods() {
        return this.methods;
    }

    /**
     * Retrieves the list of stereotypes associated with the Nomnoml class.
     *
     * @return a list of {@code NomnomlStereotype} objects that represent the stereotypes
     *         defined for this class.
     */
    public List<NomnomlStereotype> getStereotypes() {
        return this.stereotypes;
    }

    /**
     * Determines whether fields are set to be displayed in the Nomnoml class diagram.
     *
     * @return true if fields are set to be displayed, false otherwise
     */
    public boolean isShowFields() {
        return this.showFields;
    }

    /**
     * Determines whether methods are set to be displayed in the Nomnoml class diagram.
     *
     * @return true if methods are set to be displayed, false otherwise
     */
    public boolean isShowMethods() {
        return this.showMethods;
    }

    /**
     * Compares this object with the specified object for equality. This method checks if the given
     * object is an instance of NomnomlClass and compares all relevant fields including comment, name,
     * styleClassifier, fields, methods, stereotypes, showFields, and showMethods for equivalence.
     *
     * @param o the object to compare for equality
     * @return true if the specified object is equal to this instance, false otherwise
     */
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof NomnomlClass)) return false;
        final NomnomlClass other = (NomnomlClass) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$comment = this.getComment();
        final Object other$comment = other.getComment();
        if (!Objects.equals(this$comment, other$comment)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (!Objects.equals(this$name, other$name)) return false;
        final Object this$styleClassifier = this.getStyleClassifier();
        final Object other$styleClassifier = other.getStyleClassifier();
        if (!Objects.equals(this$styleClassifier, other$styleClassifier))
            return false;
        final Object this$fields = this.getFields();
        final Object other$fields = other.getFields();
        if (!Objects.equals(this$fields, other$fields)) return false;
        final Object this$methods = this.getMethods();
        final Object other$methods = other.getMethods();
        if (!Objects.equals(this$methods, other$methods)) return false;
        final Object this$stereotypes = this.getStereotypes();
        final Object other$stereotypes = other.getStereotypes();
        if (!Objects.equals(this$stereotypes, other$stereotypes))
            return false;
        if (this.isShowFields() != other.isShowFields()) return false;
        if (this.isShowMethods() != other.isShowMethods()) return false;
        return true;
    }

    /**
     * Checks if the given object can be considered equal to this instance, specifically verifying
     * if the provided object is an instance of NomnomlClass.
     *
     * @param other the object to check equality against
     * @return true if the given object is an instance of NomnomlClass, false otherwise
     */
    protected boolean canEqual(final Object other) {
        return other instanceof NomnomlClass;
    }

    /**
     * Computes the hash code for this object based on its fields.
     * The hash code is calculated using the values of comment, name, styleClassifier,
     * fields, methods, stereotypes, showFields, and showMethods.
     *
     * @return the hash code of this object
     */
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

    /**
     * The NomnomlClassBuilder provides a builder pattern implementation to create and configure
     * instances of the NomnomlClass.
     *
     * It allows setting various attributes such as:
     * - The name of the class
     * - An optional comment for the class
     * - A style classifier for visual representation
     * - A list of fields and methods
     * - Stereotypes and visibility of fields or methods
     *
     * Each method in the builder returns the builder itself, supporting a fluent and chainable API.
     * Once configured, the `build` method can be invoked to generate the final NomnomlClass instance.
     */
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

        /**
         * Sets the name of the class being built and returns the builder instance to allow
         * for a fluent and chainable API.
         *
         * @param name the name of the class
         * @return the current instance of the NomnomlClassBuilder for method chaining
         */
        public NomnomlClassBuilder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the comment of the class being built and returns the builder instance to allow
         * for a fluent and chainable API.
         *
         * @param comment the comment to associate with the class
         * @return the current instance of the NomnomlClassBuilder for method chaining
         */
        public NomnomlClassBuilder comment(String comment) {
            this.comment = comment;
            return this;
        }

        /**
         * Sets the style classifier of the class being built and returns the builder instance
         * to allow for a fluent and chainable API.
         *
         * @param styleClassifier the style classifier to apply to the class
         * @return the current instance of the NomnomlClassBuilder for method chaining
         */
        public NomnomlClassBuilder styleClassifier(String styleClassifier) {
            this.styleClassifier = styleClassifier;
            return this;
        }

        /**
         * Sets the fields of the class being built and returns the builder instance to allow
         * for a fluent and chainable API.
         *
         * @param fields the list of fields to be assigned to the class
         * @return the current instance of the NomnomlClassBuilder for method chaining
         */
        public NomnomlClassBuilder fields(List<NomnomlField> fields) {
            this.fields = fields;
            return this;
        }

        /**
         * Sets the methods of the class being built and returns the builder instance
         * to allow for a fluent and chainable API.
         *
         * @param methods the list of methods to be assigned to the class
         * @return the current instance of the NomnomlClassBuilder for method chaining
         */
        public NomnomlClassBuilder methods(List<NomnomlMethod> methods) {
            this.methods = methods;
            return this;
        }

        /**
         * Sets the stereotypes of the class being built and returns the builder instance
         * to allow for a fluent and chainable API.
         *
         * @param stereotypes the list of stereotypes to be assigned to the class
         * @return the current instance of the NomnomlClassBuilder for method chaining
         */

        public NomnomlClassBuilder stereotypes(List<NomnomlStereotype> stereotypes) {
            this.stereotypes = stereotypes;
            return this;
        }

        /**
         * Sets whether the fields of the class should be displayed and returns the builder instance
         * to allow for a fluent and chainable API.
         *
         * @param showFields a boolean indicating whether to display the fields
         * @return the current instance of the NomnomlClassBuilder for method chaining
         */
        public NomnomlClassBuilder showFields(boolean showFields) {
            this.showFields = showFields;
            return this;
        }

        /**
         * Sets whether the methods of the class should be displayed and returns the builder
         * instance to allow for a fluent and chainable API.
         *
         * @param showMethods a boolean indicating whether to display the methods
         * @return the current instance of the NomnomlClassBuilder for method chaining
         */
        public NomnomlClassBuilder showMethods(boolean showMethods) {
            this.showMethods = showMethods;
            return this;
        }

        /**
         * Builds and returns a new instance of the NomnomlClass based on the current state of the builder.
         *
         * @return a new NomnomlClass instance initialized with the values provided to the builder
         */
        public NomnomlClass build() {
            return new NomnomlClass(
                this.name,
                this.comment,
                this.styleClassifier,
                this.fields,
                this.methods,
                this.stereotypes,
                this.showFields,
                this.showMethods
            );
        }

        /**
         * Returns a string representation of the NomnomlClassBuilder instance, including its current state
         * with details about its fields, methods, and other attributes.
         *
         * @return a string representation of the NomnomlClassBuilder instance
         */
        public String toString() {
            return "NomnomlClass.NomnomlClassBuilder(name=" + this.name + ", comment=" + this.comment + ", styleClassifier=" + this.styleClassifier + ", fields=" + this.fields + ", methods=" + this.methods + ", stereotypes=" + this.stereotypes + ", showFields=" + this.showFields + ", showMethods=" + this.showMethods + ")";
        }
    }
}
