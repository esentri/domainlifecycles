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
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * The Class NomnomlClass encapsulates all information needed for creating a diagram text for a uml
 * class.
 *
 * @author Mario Herb
 */
@Getter
@EqualsAndHashCode
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
    @Builder
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
}
