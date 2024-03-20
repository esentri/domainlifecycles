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

package nitrox.dlc.diagram.nomnoml;

import lombok.Builder;
import lombok.Getter;
import nitrox.dlc.diagram.DiagramElement;

import java.util.Objects;

/**
 * The Class NomnomlRelationship encapsulates all information needed for creating a diagram text for a
 * uml relationship between classes.
 *
 * @author Mario Herb
 */
@Builder
@Getter
public class NomnomlRelationship implements DiagramElement {
    private final String fromName;
    private final String fromStyleClassifier;
    private final String fromMultiplicity;
    private final String toName;
    private final String toStyleClassifier;
    private final String toMultiplicity;
    private final String label;
    private final RelationshipType relationshiptype;

    /**
     * Initializes the relationship.
     *
     * @param fromName for relationship
     * @param fromStyleClassifier for relationship
     * @param fromMultiplicity for relationship
     * @param toName for relationship
     * @param toStyleClassifier for relationship
     * @param toMultiplicity for relationship
     * @param label for relationship
     * @param relationshiptype for relationship
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
     * Returns the Nomnoml text representation of a relationship between classes.
     */
    @Override
    public String getDiagramText() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        builder.append(fromStyleClassifier);
        builder.append(fromName);
        builder.append("] ");
        builder.append(fromMultiplicity);
        builder.append(" ");
        builder.append(relationshiptype.lineStart);
        if(!"".equals(label)){
            builder.append("[<label> ");
            builder.append(label);
            builder.append("] ");
        }
        builder.append(relationshiptype.lineEnd);
        builder.append(toMultiplicity);
        builder.append(" ");
        builder.append("[");
        builder.append(toStyleClassifier);
        builder.append(toName);
        builder.append("]");
        builder.append(System.lineSeparator());
        return builder.toString();
    }


    /**
     * The Enum RelationshipType defines all supported relationship types of this utility.
     */
    public enum RelationshipType {

        /**
         * inheritance.
         */
        INHERITANCE("<:", "-"),

        /**
         * realization.
         */
        REALIZATION("--", ":>"),

        /**
         * composition (not used so far).
         */
        COMPOSITION("+-", "-"),

        /**
         * aggregation.
         */
        AGGREGATION("o-", "-"),

        /**
         * association.
         */
        ASSOCIATION("-", "-"),

        /**
         * directed association.
         */
        DIRECTED_ASSOCIATION("-", "->");

        private final String lineStart;
        private final String lineEnd;


        RelationshipType(String lineStart, String lineEnd) {
            this.lineStart = lineStart;
            this.lineEnd = lineEnd;
        }
    }
}
