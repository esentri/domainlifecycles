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
 * The Class public class NomnomlType encapsulates all information needed for creating a diagram text for a
 * type representation within a class diagram.
 *
 * @author Mario Herb
 */
@Builder
@Getter
@EqualsAndHashCode
public class NomnomlType implements DiagramElement {

    private final String typeName;
    private final List<String> typeAssertions;

    @SuppressWarnings("all")
    private final Optional<String> containerTypeName;
    private final List<String> containerTypeAssertions;

    @SuppressWarnings("all")
    /**
     * Initializes the type.
     */
    public NomnomlType(String typeName,
                       List<String> typeAssertions,
                       Optional<String> containerTypeName,
                       List<String> containerTypeAssertions) {
        this.typeName = Objects.requireNonNull(typeName);
        this.typeAssertions = Objects.requireNonNull(typeAssertions);
        this.containerTypeName = Objects.requireNonNull(containerTypeName);
        this.containerTypeAssertions = Objects.requireNonNull(containerTypeAssertions);
    }

    /**
     * Returns the Nomnoml text representation of a type used in a class.
     */
    @Override
    public String getDiagramText() {
        StringBuilder builder = new StringBuilder();
        if (containerTypeName.isPresent()) {
            builder.append(containerTypeName.get());
            builder.append("<");
            builder.append(typeName
                .replaceAll("\\[", "\\\\[")
                .replaceAll("\\]", "\\\\]")
            );
            addTypeAssertions(builder);
            builder.append(">");
            addContainerTypeAssertions(builder);
        } else {
            builder.append(typeName
                .replaceAll("\\[", "\\\\[")
                .replaceAll("\\]", "\\\\]")
            );
            addTypeAssertions(builder);
        }
        return builder.toString();
    }

    private void addTypeAssertions(StringBuilder builder) {
        for (String assertion : typeAssertions) {
            builder.append(assertion);
        }
    }

    private void addContainerTypeAssertions(StringBuilder builder) {
        for (String assertion : containerTypeAssertions) {
            builder.append(assertion);
        }
    }


}
