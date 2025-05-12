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

    /**
     * Creates a new instance of the NomnomlFrameBuilder. The builder provides a
     * convenient way to construct a fully configured instance of NomnomlFrame
     * with specified properties such as name, comment, type, styleClassifier,
     * and innerElements.
     *
     * @return a new instance of NomnomlFrameBuilder
     */
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

    /**
     * Retrieves the name associated with this frame.
     *
     * @return the name of the frame
     */
    public String getName() {
        return this.name;
    }

    /**
     * Retrieves the optional comment associated with this frame.
     *
     * @return an Optional containing the comment if present, or an empty Optional if no comment is set
     */
    public Optional<String> getComment() {
        return this.comment;
    }

    /**
     * Retrieves the type associated with this frame.
     *
     * @return the type of the frame
     */
    public String getType() {
        return this.type;
    }

    /**
     * Retrieves the style classifier associated with this frame.
     *
     * @return the style classifier as a string
     */
    public String getStyleClassifier() {
        return this.styleClassifier;
    }

    /**
     * Retrieves the list of inner elements contained within this frame.
     *
     * @return a list of DiagramElement instances representing the inner elements of the frame
     */
    public List<DiagramElement> getInnerElements() {
        return this.innerElements;
    }

    /**
     * Compares this object with the specified object for equality. Returns true
     * if the specified object is also an instance of NomnomlFrame, and all
     * relevant fields match between the two objects.
     *
     * @param o the object to be compared for equality with this instance
     * @return true if the specified object is equal to this instance; false otherwise
     */
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

    /**
     * Determines whether the specified object can be considered equal to this instance
     * based on type compatibility. This method is used in the equality check logic
     * to ensure that only objects of the same type are considered for detailed comparison.
     *
     * @param other the object to check for type compatibility
     * @return true if the specified object is an instance of NomnomlFrame; false otherwise
     */
    protected boolean canEqual(final Object other) {
        return other instanceof NomnomlFrame;
    }

    /**
     * Computes the hash code for this instance. The hash code is calculated based on
     * the values of the frame's attributes including name, comment, type, styleClassifier,
     * and innerElements. The hash code provides a unique numerical representation of
     * the object's state, which is used in hash-based collections like HashMap.
     *
     * @return an integer value representing the hash code of this instance
     */
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

    /**
     * The NomnomlFrameBuilder class is a builder utility for creating instances of the
     * NomnomlFrame class. This builder follows the builder design pattern, allowing for
     * step-by-step configuration of a NomnomlFrame object with a fluent API.
     *
     * The builder provides methods to set the properties of the NomnomlFrame, including
     * name, comment, type, styleClassifier, and innerElements. Once all desired
     * properties have been configured, the {@code build()} method can be called to
     * create a fully configured instance of NomnomlFrame.
     */
    public static class NomnomlFrameBuilder {
        private String name;
        private String comment;
        private String type;
        private String styleClassifier;
        private List<DiagramElement> innerElements;

        NomnomlFrameBuilder() {
        }

        /**
         * Sets the name for the NomnomlFrame being built.
         * This value will be assigned to the name property of the resulting NomnomlFrame instance.
         *
         * @param name the name of the frame
         * @return the current instance of NomnomlFrameBuilder, allowing for method chaining
         */
        public NomnomlFrameBuilder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the comment for the NomnomlFrame being built.
         * This value will be assigned to the comment property of the resulting NomnomlFrame instance.
         *
         * @param comment the comment of the frame
         * @return the current instance of NomnomlFrameBuilder, allowing for method chaining
         */
        public NomnomlFrameBuilder comment(String comment) {
            this.comment = comment;
            return this;
        }

        /**
         * Sets the type for the NomnomlFrame being built.
         * This value will be assigned to the type property of the resulting NomnomlFrame instance.
         *
         * @param type the type of the frame
         * @return the current instance of NomnomlFrameBuilder, allowing for method chaining
         */
        public NomnomlFrameBuilder type(String type) {
            this.type = type;
            return this;
        }

        /**
         * Sets the style classifier for the NomnomlFrame being built.
         * This value will be assigned to the styleClassifier property of the resulting NomnomlFrame instance.
         *
         * @param styleClassifier the style classifier to apply to the frame
         * @return the current instance of NomnomlFrameBuilder, allowing for method chaining
         */
        public NomnomlFrameBuilder styleClassifier(String styleClassifier) {
            this.styleClassifier = styleClassifier;
            return this;
        }

        /**
         * Sets the inner elements for the NomnomlFrame being built.
         * This value will be assigned to the innerElements property of the resulting NomnomlFrame instance.
         *
         * @param innerElements the list of inner elements to include in the frame
         * @return the current instance of NomnomlFrameBuilder, allowing for method chaining
         */
        public NomnomlFrameBuilder innerElements(List<DiagramElement> innerElements) {
            this.innerElements = innerElements;
            return this;
        }

        /**
         * Builds and returns a new instance of NomnomlFrame based on the properties set
         * in the NomnomlFrameBuilder instance.
         *
         * @return a new NomnomlFrame instance configured with the specified properties
         */
        public NomnomlFrame build() {
            return new NomnomlFrame(this.name, this.comment, this.type, this.styleClassifier, this.innerElements);
        }

        /**
         * Provides a string representation of the NomnomlFrameBuilder object, including
         * its properties such as name, comment, type, styleClassifier, and innerElements.
         *
         * @return a string describing the current state of the NomnomlFrameBuilder instance
         */
        public String toString() {
            return "NomnomlFrame.NomnomlFrameBuilder(name=" + this.name + ", comment=" + this.comment + ", type=" + this.type + ", styleClassifier=" + this.styleClassifier + ", innerElements=" + this.innerElements + ")";
        }
    }
}
