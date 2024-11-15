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

    public List<String> getStyleDeclarations() {
        return this.styleDeclarations;
    }

    public List<DiagramElement> getDiagramElements() {
        return this.diagramElements;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof NomnomlDiagram)) return false;
        final NomnomlDiagram other = (NomnomlDiagram) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$styleDeclarations = this.getStyleDeclarations();
        final Object other$styleDeclarations = other.getStyleDeclarations();
        if (this$styleDeclarations == null ? other$styleDeclarations != null : !this$styleDeclarations.equals(other$styleDeclarations))
            return false;
        final Object this$diagramElements = this.getDiagramElements();
        final Object other$diagramElements = other.getDiagramElements();
        if (this$diagramElements == null ? other$diagramElements != null : !this$diagramElements.equals(other$diagramElements))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof NomnomlDiagram;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $styleDeclarations = this.getStyleDeclarations();
        result = result * PRIME + ($styleDeclarations == null ? 43 : $styleDeclarations.hashCode());
        final Object $diagramElements = this.getDiagramElements();
        result = result * PRIME + ($diagramElements == null ? 43 : $diagramElements.hashCode());
        return result;
    }

    public static class NomnomlDiagramBuilder {
        private List<String> styleDeclarations;
        private List<DiagramElement> diagramElements;

        NomnomlDiagramBuilder() {
        }

        public NomnomlDiagramBuilder styleDeclarations(List<String> styleDeclarations) {
            this.styleDeclarations = styleDeclarations;
            return this;
        }

        public NomnomlDiagramBuilder diagramElements(List<DiagramElement> diagramElements) {
            this.diagramElements = diagramElements;
            return this;
        }

        public NomnomlDiagram build() {
            return new NomnomlDiagram(this.styleDeclarations, this.diagramElements);
        }

        public String toString() {
            return "NomnomlDiagram.NomnomlDiagramBuilder(styleDeclarations=" + this.styleDeclarations + ", diagramElements=" + this.diagramElements + ")";
        }
    }
}
