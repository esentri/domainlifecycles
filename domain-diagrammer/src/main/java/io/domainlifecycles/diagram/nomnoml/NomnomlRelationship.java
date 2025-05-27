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
 * The Class NomnomlRelationship encapsulates all information needed for creating a diagram text for a
 * uml relationship between classes.
 *
 * @author Mario Herb
 */
public class NomnomlRelationship implements DiagramElement {
    private final String fromName;
    private final String fromStyleClassifier;
    private final String fromMultiplicity;
    private final String toName;
    private final String toStyleClassifier;
    private final String toMultiplicity;
    private final String label;
    private final RelationshipType relationshiptype;
    private boolean transposed; //declare relationship in the transposed way (semantics stay the same)


    /**
     * Initializes the relationship.
     *
     * @param fromName            for relationship
     * @param fromStyleClassifier for relationship
     * @param fromMultiplicity    for relationship
     * @param toName              for relationship
     * @param toStyleClassifier   for relationship
     * @param toMultiplicity      for relationship
     * @param label               for relationship
     * @param relationshiptype    for relationship
     */
    public NomnomlRelationship(String fromName,
                               String fromStyleClassifier,
                               String fromMultiplicity,
                               String toName,
                               String toStyleClassifier,
                               String toMultiplicity,
                               String label,
                               RelationshipType relationshiptype) {
        this.fromName = Objects.requireNonNull(fromName);
        this.fromStyleClassifier = Objects.requireNonNull(fromStyleClassifier);
        this.fromMultiplicity = Objects.requireNonNull(fromMultiplicity);
        this.toName = Objects.requireNonNull(toName);
        this.toStyleClassifier = Objects.requireNonNull(toStyleClassifier);
        this.toMultiplicity = Objects.requireNonNull(toMultiplicity);
        this.label = Objects.requireNonNull(label);
        this.relationshiptype = Objects.requireNonNull(relationshiptype);
    }

    /**
     * Creates and returns a new instance of {@code NomnomlRelationshipBuilder}.
     *
     * The builder allows for a fluent configuration of the properties required
     * to construct a {@code NomnomlRelationship} instance, such as source and
     * target names, styles, multiplicities, label, and relationship type.
     *
     * @return a new instance of {@code NomnomlRelationshipBuilder} for constructing
     *         {@code NomnomlRelationship} objects.
     */
    public static NomnomlRelationshipBuilder builder() {
        return new NomnomlRelationshipBuilder();
    }

    /**
     * Returns the Nomnoml text representation of a relationship between classes.
     */
    @Override
    public String getDiagramText() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        if (!transposed) {
            builder.append(fromStyleClassifier);
            builder.append(fromName);
        } else {
            builder.append(toStyleClassifier);
            builder.append(toName);
        }
        builder.append("] ");
        if (!transposed) {
            builder.append(fromMultiplicity);
        } else {
            builder.append(toMultiplicity);
        }
        builder.append(" ");
        if (!transposed) {
            builder.append(relationshiptype.lineStart);
        } else {
            builder.append(relationshiptype.transposedLineStart);
        }
        if (!label.isEmpty()) {
            builder.append("[<label> ");
            builder.append(label);
            builder.append("] ");
        }
        if (!transposed) {
            builder.append(relationshiptype.lineEnd);
            builder.append(toMultiplicity);
        } else {
            builder.append(relationshiptype.transposedLineEnd);
            builder.append(fromMultiplicity);
        }

        builder.append(" ");
        builder.append("[");
        if (!transposed) {
            builder.append(toStyleClassifier);
            builder.append(toName);
        } else {
            builder.append(fromStyleClassifier);
            builder.append(fromName);
        }
        builder.append("]");
        builder.append(System.lineSeparator());
        return builder.toString();
    }

    /**
     * Retrieves the name of the source element (from side) of the relationship.
     *
     * @return the String representing the source element's name
     */
    public String getFromName() {
        return this.fromName;
    }

    /**
     * Retrieves the style classifier of the source element (from side) of the relationship.
     *
     * @return the String representing the style classifier of the source element
     */
    public String getFromStyleClassifier() {
        return this.fromStyleClassifier;
    }

    /**
     * Retrieves the multiplicity of the source element (from side) of the relationship.
     *
     * @return the String representing the source element's multiplicity
     */
    public String getFromMultiplicity() {
        return this.fromMultiplicity;
    }

    /**
     * Retrieves the name of the target element (to side) of the relationship.
     *
     * @return the String representing the target element's name
     */
    public String getToName() {
        return this.toName;
    }

    /**
     * Retrieves the style classifier of the target element (to side) of the relationship.
     *
     * @return the String representing the style classifier of the target element
     */
    public String getToStyleClassifier() {
        return this.toStyleClassifier;
    }

    /**
     * Retrieves the multiplicity of the target element (to side) of the relationship.
     *
     * @return the String representing the target element's multiplicity
     */
    public String getToMultiplicity() {
        return this.toMultiplicity;
    }

    /**
     * Retrieves the label of the relationship.
     *
     * @return the label of the relationship as a String
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Retrieves the type of relationship.
     *
     * @return the RelationshipType representing the type of the relationship
     */
    public RelationshipType getRelationshiptype() {
        return this.relationshiptype;
    }

    /**
     * Checks whether the relationship is in a transposed state.
     *
     * @return true if the relationship is transposed, false otherwise
     */
    public boolean isTransposed() {
        return this.transposed;
    }


    /**
     * The Enum RelationshipType defines all supported relationship types of this utility.
     */
    public enum RelationshipType {

        /**
         * inheritance.
         */
        INHERITANCE("<:", "-", "-", ":>"),

        /**
         * realization.
         */
        REALIZATION("--", ":>", "<:", "--"),

        /**
         * composition (not used so far).
         */
        COMPOSITION("+-", "-", "-", "-+"),

        /**
         * aggregation.
         */
        AGGREGATION("o-", "-", "-", "-o"),

        /**
         * association.
         */
        ASSOCIATION("-", "-", "-", "-"),

        /**
         * directed association.
         */
        DIRECTED_ASSOCIATION("-", "->", "<-", "-");

        private final String lineStart;
        private final String lineEnd;
        private final String transposedLineStart;
        private final String transposedLineEnd;


        RelationshipType(String lineStart, String lineEnd, String transposedLineStart, String transposedLineEnd) {
            this.lineStart = lineStart;
            this.lineEnd = lineEnd;
            this.transposedLineStart = transposedLineStart;
            this.transposedLineEnd = transposedLineEnd;
        }
    }

    /**
     * Toggles the transposed state of the relationship.
     * This method changes the orientation of the relationship by inverting its
     * current transposed state. If the relationship was not transposed, it will
     * become transposed, and vice versa.
     */
    public void transpose() {
        this.transposed = !transposed;
    }

    /**
     * StyleSettingsBuilder class for constructing instances of {@code NomnomlRelationship}.
     * This class provides a fluent interface for setting various attributes of a relationship
     * such as the source and target names, styles, multiplicities, label, and relationship type.
     */
    public static class NomnomlRelationshipBuilder {
        private String fromName;
        private String fromStyleClassifier;
        private String fromMultiplicity;
        private String toName;
        private String toStyleClassifier;
        private String toMultiplicity;
        private String label;
        private RelationshipType relationshiptype;

        NomnomlRelationshipBuilder() {
        }

        /**
         * Sets the name of the source side of the relationship being built.
         *
         * @param fromName the name to assign to the source of the relationship
         * @return the current instance of {@code NomnomlRelationshipBuilder} for method chaining
         */
        public NomnomlRelationshipBuilder fromName(String fromName) {
            this.fromName = fromName;
            return this;
        }

        /**
         * Sets the style classifier of the source side of the relationship being built.
         *
         * @param fromStyleClassifier the style classifier to assign to the source of the relationship
         * @return the current instance of {@code NomnomlRelationshipBuilder} for method chaining
         */
        public NomnomlRelationshipBuilder fromStyleClassifier(String fromStyleClassifier) {
            this.fromStyleClassifier = fromStyleClassifier;
            return this;
        }

        /**
         * Sets the multiplicity of the source side of the relationship being built.
         *
         * @param fromMultiplicity the multiplicity to assign to the source of the relationship
         * @return the current instance of {@code NomnomlRelationshipBuilder} for method chaining
         */
        public NomnomlRelationshipBuilder fromMultiplicity(String fromMultiplicity) {
            this.fromMultiplicity = fromMultiplicity;
            return this;
        }

        /**
         * Sets the name of the target side of the relationship being built.
         *
         * @param toName the name to assign to the target of the relationship
         * @return the current instance of {@code NomnomlRelationshipBuilder} for method chaining
         */
        public NomnomlRelationshipBuilder toName(String toName) {
            this.toName = toName;
            return this;
        }

        /**
         * Sets the style classifier of the target side of the relationship being built.
         *
         * @param toStyleClassifier the style classifier to assign to the target of the relationship
         * @return the current instance of {@code NomnomlRelationshipBuilder} for method chaining
         */
        public NomnomlRelationshipBuilder toStyleClassifier(String toStyleClassifier) {
            this.toStyleClassifier = toStyleClassifier;
            return this;
        }

        /**
         * Sets the multiplicity of the target side of the relationship being built.
         *
         * @param toMultiplicity the multiplicity to assign to the target of the relationship
         * @return the current instance of {@code NomnomlRelationshipBuilder} for method chaining
         */
        public NomnomlRelationshipBuilder toMultiplicity(String toMultiplicity) {
            this.toMultiplicity = toMultiplicity;
            return this;
        }

        /**
         * Sets the label for the relationship being built.
         *
         * @param label the label to be assigned to the relationship
         * @return the current instance of {@code NomnomlRelationshipBuilder} for method chaining
         */
        public NomnomlRelationshipBuilder label(String label) {
            this.label = label;
            return this;
        }

        /**
         * Sets the relationship type for this builder.
         *
         * @param relationshiptype the type of relationship to be set, represented by the {@link RelationshipType} enum
         * @return the current instance of {@code NomnomlRelationshipBuilder} for method chaining
         */
        public NomnomlRelationshipBuilder relationshiptype(RelationshipType relationshiptype) {
            this.relationshiptype = relationshiptype;
            return this;
        }

        /**
         * Constructs a new NomnomlRelationship object using the configured values
         * in the NomnomlRelationshipBuilder.
         *
         * @return a new NomnomlRelationship instance initialized with the current state
         *         of the builder.
         */
        public NomnomlRelationship build() {
            return new NomnomlRelationship(this.fromName, this.fromStyleClassifier, this.fromMultiplicity, this.toName, this.toStyleClassifier, this.toMultiplicity, this.label, this.relationshiptype);
        }

        /**
         * Returns a string representation of the NomnomlRelationshipBuilder object.
         * The string includes all the fields and their values in the builder.
         *
         * @return a string representation of the current state of the NomnomlRelationshipBuilder.
         */
        public String toString() {
            return "NomnomlRelationship.NomnomlRelationshipBuilder(fromName=" + this.fromName + ", fromStyleClassifier=" + this.fromStyleClassifier + ", fromMultiplicity=" + this.fromMultiplicity + ", toName=" + this.toName + ", toStyleClassifier=" + this.toStyleClassifier + ", toMultiplicity=" + this.toMultiplicity + ", label=" + this.label + ", relationshiptype=" + this.relationshiptype + ")";
        }
    }
}
