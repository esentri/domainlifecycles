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
 *  Copyright 2019-2025 the original author or authors.
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
 * The Class NomnomlNote encapsulates all information needed for creating a diagram text
 * representing a note in an uml diagram
 *
 * @author Mario Herb
 */
public class NomnomlNote implements DiagramElement {

    private final String text;
    private final String className;
    private final String classStyleClassifier;
    private final List<NomnomlStereotype> classStereotypes;

    /**
     * Constructor for Nomnoml note.
     * @param text the note text
     * @param className the class name of the class the not is attached to
     * @param classStyleClassifier the style classifier of the class
     * @param classStereotypes the stereotypes of the class
     */
    public NomnomlNote(String text, String className, String classStyleClassifier, List<NomnomlStereotype> classStereotypes) {
        this.text = text;
        this.className = className;
        this.classStyleClassifier = classStyleClassifier;
        this.classStereotypes = classStereotypes;
    }

    @Override
    public String getDiagramText() {
        return "[<note> " +
            text +
            "]--[" +
            classStyleClassifier +
            " " +
            className +
            addStereotypes() +
            "]"
            + System.lineSeparator();
    }

    private String addStereotypes() {
        return classStereotypes.stream()
            .map(s -> " " + s.getDiagramText() + " ")
            .collect(Collectors.joining());
    }

    public String getText() {
        return text;
    }

    public String getClassName() {
        return className;
    }

    public String getClassStyleClassifier() {
        return classStyleClassifier;
    }

    public List<NomnomlStereotype> getClassStereotypes() {
        return classStereotypes;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        NomnomlNote that = (NomnomlNote) o;
        return Objects.equals(text, that.text) && Objects.equals(className, that.className) && Objects.equals(classStyleClassifier, that.classStyleClassifier) && Objects.equals(classStereotypes, that.classStereotypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, className, classStyleClassifier, classStereotypes);
    }

    @Override
    public String toString() {
        return "NomnomlNote{" +
            "text='" + text + '\'' +
            ", className='" + className + '\'' +
            ", classStyleClassifier='" + classStyleClassifier + '\'' +
            ", classStereotypes=" + classStereotypes +
            '}';
    }
}
