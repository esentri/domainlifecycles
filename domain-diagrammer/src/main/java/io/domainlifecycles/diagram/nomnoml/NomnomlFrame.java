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
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * The Class NomnomlFrame encapsulates all information needed
 * to draw a frame around some diagram elements.
 *
 * @author Mario Herb
 */
@Getter
@EqualsAndHashCode
public class NomnomlFrame implements DiagramElement {
    private final String name;

    @SuppressWarnings("all")
    private final Optional<String> comment;

    private final String type;

    private final String styleClassifier;

    private final List<DiagramElement> innerElements;

    /**
     * Initializes the frame.
     *
     * @param name of frame
     * @param comment for frame
     * @param type of frame
     * @param styleClassifier for frame
     * @param innerElements contained
     */
    @Builder
    public NomnomlFrame(String name,
                        String comment,
                        String type,
                        String styleClassifier,
                        List<DiagramElement> innerElements) {
        this.name = Objects.requireNonNull(name);
        this.comment = Optional.ofNullable(comment);
        this.type = Objects.requireNonNull(type);
        this.styleClassifier = Objects.requireNonNull(styleClassifier);
        this.innerElements = Objects.requireNonNull(innerElements);
    }

    /**
     * Returns the Nomnoml text representation of a frame.
     */
    @Override
    public String getDiagramText() {
        StringBuilder builder = new StringBuilder();
        comment.ifPresent(s -> builder.append("// ").append(s).append(System.lineSeparator()));
        builder.append("[<");
        builder.append(styleClassifier);
        builder.append("> ");
        builder.append(name);
        builder.append(" ");
        builder.append(type);
        builder.append("|");
        builder.append(System.lineSeparator());
        innerElements.forEach(ie -> builder.append(ie.getDiagramText()));
        builder.append("]");
        builder.append(System.lineSeparator());
        return builder.toString();
    }


}
