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

import io.domainlifecycles.diagram.Diagram;
import io.domainlifecycles.diagram.DiagramElement;

import java.util.List;
import java.util.Objects;

/**
 * A NomnomlDiagram is composed by several {@link DiagramElement}s and additional style declarations.
 *
 * @author Mario Herb
 */
public class NomnomlDiagram implements Diagram {

    private final List<String> styleDeclarations;
    private final List<DiagramElement> diagramElements;

    /**
     * Initializes the NomnomlDiagram.
     *
     * @param styleDeclarations for this diagram
     * @param diagramElements   contained
     */
    public NomnomlDiagram(List<String> styleDeclarations,
                          List<DiagramElement> diagramElements) {
        this.styleDeclarations = Objects.requireNonNull(styleDeclarations);
        this.diagramElements = Objects.requireNonNull(diagramElements);
    }

    /**
     * Creates a new instance of {@link NomnomlDiagramBuilder} to construct a {@link NomnomlDiagram}.
     *
     * @return a new instance of {@link NomnomlDiagramBuilder} for building a {@link NomnomlDiagram}.
     */
    public static NomnomlDiagramBuilder builder() {
        return new NomnomlDiagramBuilder();
    }

    /**
     * Generates the diagram text containing all style declarations and diagram elements.
     */
    @Override
    public String generateDiagramText() {
        var builder = new StringBuilder();
        styleDeclarations.forEach(s -> builder.append(s).append(System.lineSeparator()));
        diagramElements.forEach(e -> builder
            .append(e.getDiagramText())
            .append(System.lineSeparator())
            .append(System.lineSeparator())
        );
        return builder.toString();
    }

    /**
     * Retrieves the list of style declarations associated with this diagram.
     *
     * @return a list of style declarations as strings
     */
    public List<String> getStyleDeclarations() {
        return this.styleDeclarations;
    }

    /**
     * Retrieves the list of diagram elements associated with this diagram.
     *
     * @return a list of {@link DiagramElement} instances representing the elements in the diagram
     */
    public List<DiagramElement> getDiagramElements() {
        return this.diagramElements;
    }

    /**
     * Compares this object with the given object to determine equality.
     *
     * @param o the object to be compared with this instance
     * @return true if the given object is equal to this instance; otherwise, false
     */
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof NomnomlDiagram)) return false;
        final NomnomlDiagram other = (NomnomlDiagram) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$styleDeclarations = this.getStyleDeclarations();
        final Object other$styleDeclarations = other.getStyleDeclarations();
        if (!Objects.equals(this$styleDeclarations, other$styleDeclarations))
            return false;
        final Object this$diagramElements = this.getDiagramElements();
        final Object other$diagramElements = other.getDiagramElements();
        if (!Objects.equals(this$diagramElements, other$diagramElements))
            return false;
        return true;
    }

    /**
     * Determines if the provided object can be considered equal to this object.
     *
     * @param other the object to be compared
     * @return true if the provided object is an instance of NomnomlDiagram, otherwise false
     */
    protected boolean canEqual(final Object other) {
        return other instanceof NomnomlDiagram;
    }

    /**
     * Computes the hash code for this object based on its style declarations and diagram elements.
     *
     * @return the hash code value for this object
     */
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $styleDeclarations = this.getStyleDeclarations();
        result = result * PRIME + ($styleDeclarations == null ? 43 : $styleDeclarations.hashCode());
        final Object $diagramElements = this.getDiagramElements();
        result = result * PRIME + ($diagramElements == null ? 43 : $diagramElements.hashCode());
        return result;
    }

    /**
     * StyleSettingsBuilder class for constructing instances of {@link NomnomlDiagram}.
     * This builder provides methods to set style declarations and diagram elements
     * that make up the diagram.
     */
    public static class NomnomlDiagramBuilder {
        private List<String> styleDeclarations;
        private List<DiagramElement> diagramElements;

        NomnomlDiagramBuilder() {
        }

        /**
         * Sets the style declarations for the Nomnoml diagram builder.
         * Style declarations are used to define the visual appearance and formatting
         * of diagram elements.
         *
         * @param styleDeclarations a list of strings representing style declarations
         *                          to be applied to the diagram
         * @return the current instance of {@code NomnomlDiagramBuilder} for method chaining
         */
        public NomnomlDiagramBuilder styleDeclarations(List<String> styleDeclarations) {
            this.styleDeclarations = styleDeclarations;
            return this;
        }

        /**
         * Sets the diagram elements for the Nomnoml diagram builder.
         * Diagram elements define the structure of the diagram, including nodes and relationships.
         *
         * @param diagramElements a list of {@link DiagramElement} objects that represent the components of the diagram
         * @return the current instance of {@code NomnomlDiagramBuilder} for method chaining
         */
        public NomnomlDiagramBuilder diagramElements(List<DiagramElement> diagramElements) {
            this.diagramElements = diagramElements;
            return this;
        }

        /**
         * Builds and returns a new {@link NomnomlDiagram} instance populated with the
         * current style declarations and diagram elements set on the builder.
         *
         * @return a new {@link NomnomlDiagram} containing the configured style declarations
         *         and diagram elements
         */
        public NomnomlDiagram build() {
            return new NomnomlDiagram(this.styleDeclarations, this.diagramElements);
        }

        /**
         * Returns a string representation of the {@code NomnomlDiagramBuilder} instance.
         * The string includes the style declarations and diagram elements configured
         * in this builder.
         *
         * @return a string representation of the {@code NomnomlDiagramBuilder} object
         */
        public String toString() {
            return "NomnomlDiagram.NomnomlDiagramBuilder(styleDeclarations=" + this.styleDeclarations + ", diagramElements=" + this.diagramElements + ")";
        }
    }
}
