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
import java.util.Optional;

/**
 * The Class NomnomlFrame encapsulates all information needed
 * to draw a frame around some diagram elements.
 *
 * @author Mario Herb
 */
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
     * @param name            of frame
     * @param comment         for frame
     * @param type            of frame
     * @param styleClassifier for frame
     * @param innerElements   contained
     */
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

    public static NomnomlFrameBuilder builder() {
        return new NomnomlFrameBuilder();
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


    public String getName() {
        return this.name;
    }

    public Optional<String> getComment() {
        return this.comment;
    }

    public String getType() {
        return this.type;
    }

    public String getStyleClassifier() {
        return this.styleClassifier;
    }

    public List<DiagramElement> getInnerElements() {
        return this.innerElements;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof NomnomlFrame)) return false;
        final NomnomlFrame other = (NomnomlFrame) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$comment = this.getComment();
        final Object other$comment = other.getComment();
        if (this$comment == null ? other$comment != null : !this$comment.equals(other$comment)) return false;
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        if (this$type == null ? other$type != null : !this$type.equals(other$type)) return false;
        final Object this$styleClassifier = this.getStyleClassifier();
        final Object other$styleClassifier = other.getStyleClassifier();
        if (this$styleClassifier == null ? other$styleClassifier != null : !this$styleClassifier.equals(other$styleClassifier))
            return false;
        final Object this$innerElements = this.getInnerElements();
        final Object other$innerElements = other.getInnerElements();
        if (this$innerElements == null ? other$innerElements != null : !this$innerElements.equals(other$innerElements))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof NomnomlFrame;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $comment = this.getComment();
        result = result * PRIME + ($comment == null ? 43 : $comment.hashCode());
        final Object $type = this.getType();
        result = result * PRIME + ($type == null ? 43 : $type.hashCode());
        final Object $styleClassifier = this.getStyleClassifier();
        result = result * PRIME + ($styleClassifier == null ? 43 : $styleClassifier.hashCode());
        final Object $innerElements = this.getInnerElements();
        result = result * PRIME + ($innerElements == null ? 43 : $innerElements.hashCode());
        return result;
    }

    public static class NomnomlFrameBuilder {
        private String name;
        private String comment;
        private String type;
        private String styleClassifier;
        private List<DiagramElement> innerElements;

        NomnomlFrameBuilder() {
        }

        public NomnomlFrameBuilder name(String name) {
            this.name = name;
            return this;
        }

        public NomnomlFrameBuilder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public NomnomlFrameBuilder type(String type) {
            this.type = type;
            return this;
        }

        public NomnomlFrameBuilder styleClassifier(String styleClassifier) {
            this.styleClassifier = styleClassifier;
            return this;
        }

        public NomnomlFrameBuilder innerElements(List<DiagramElement> innerElements) {
            this.innerElements = innerElements;
            return this;
        }

        public NomnomlFrame build() {
            return new NomnomlFrame(this.name, this.comment, this.type, this.styleClassifier, this.innerElements);
        }

        public String toString() {
            return "NomnomlFrame.NomnomlFrameBuilder(name=" + this.name + ", comment=" + this.comment + ", type=" + this.type + ", styleClassifier=" + this.styleClassifier + ", innerElements=" + this.innerElements + ")";
        }
    }
}
