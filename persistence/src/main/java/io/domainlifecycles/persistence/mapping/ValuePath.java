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

package io.domainlifecycles.persistence.mapping;

import io.domainlifecycles.mirror.api.FieldMirror;

import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents the path to a final value field as a linear collection of {@link FieldMirror} instances ({@code
 * pathElements}).
 *
 * @author Mario Herb
 */
public class ValuePath {

    private final Deque<FieldMirror> pathElements;

    /**
     * Constructs a new ValuePath object with the specified collection of path elements.
     *
     * @param pathElements the collection of {@link FieldMirror} instances that represents
     *                     the path to a final value field in a linear sequence
     */
    public ValuePath(Collection<FieldMirror> pathElements) {
        this.pathElements = new LinkedList<>(pathElements);
    }

    /**
     * Retrieves the collection of {@link FieldMirror} instances that represent the path
     * to a final value field in a linear sequence.
     *
     * @return a deque of {@link FieldMirror} instances representing the path
     */
    protected Deque<FieldMirror> pathElements() {
        return pathElements;
    }

    /**
     * Constructs the path as a dot-separated string representation. Each element in the path
     * corresponds to the name of a {@link FieldMirror} in the order defined by {@code pathElements}.
     *
     * @return a string representation of the path, with each field name joined by a dot
     */
    protected String path() {
        return pathElements.stream().map(FieldMirror::getName).collect(Collectors.joining("."));
    }

    /**
     * Gets the final field mirror of this path.
     *
     * @return the final field mirror
     */
    protected FieldMirror getFinalFieldMirror() {
        return pathElements.getLast();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValuePath valuePath = (ValuePath) o;
        return pathElements.equals(valuePath.pathElements);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(pathElements);
    }

    /**
     * Returns the container path in a String representation. Each path element is separated by a '.'.
     *
     * @return path as dot separated String
     */
    public String containerPath() {
        StringBuilder container = new StringBuilder();
        if (pathElements.size() > 0) {
            pathElements.stream().toList().subList(0, pathElements.size() - 1).forEach(
                p -> container.append(p.getName()).append(".")
            );
        }
        return container.toString();
    }

    /**
     * Returns an {@link ValuePath} instance, that represents the container of a
     * ValueObject of this ValuePath.
     *
     * @return value path container
     */
    public ValuePath containerValuePath() {
        var list = pathElements.stream().toList();
        return new ValuePath(
            list.subList(0, list.size() - 1)
        );
    }
}
