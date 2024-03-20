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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import nitrox.dlc.diagram.DiagramElement;

import java.util.Objects;
import java.util.Optional;

/**
 * The Class NomnomlField encapsulates all information needed for creating a diagram text for a uml
 * field of a class.
 *
 * @author Mario Herb
 */
@Builder
@Getter
@EqualsAndHashCode
public class NomnomlField implements DiagramElement {
    private final String visibility;
    private final String name;

    private final Optional<String> typePrefix;
    private final NomnomlType type;
    private final boolean required;

    /**
     * Initializes the field.
     *
     * @param visibility of the field
     * @param name of field
     * @param typePrefix prefix
     * @param type of field
     * @param required property
     */
    public NomnomlField(String visibility,
                        String name,
                        Optional<String> typePrefix,
                        NomnomlType type,
                        boolean required) {
        this.visibility = Objects.requireNonNull(visibility);
        this.name = Objects.requireNonNull(name);
        this.typePrefix = Objects.requireNonNull(typePrefix);
        this.type = Objects.requireNonNull(type);
        this.required = required;
    }

    /**
     * Returns the Nomnoml text representation of a field.
     */
    @Override
    public String getDiagramText() {
        StringBuilder builder = new StringBuilder();
        builder.append(required ? "\\# " : "o ");
        builder.append(visibility);
        builder.append(" ");
        builder.append(name);
        builder.append(":");
        typePrefix.ifPresent(s -> builder.append(s).append(" "));
        builder.append(type.getDiagramText());
        builder.append(System.lineSeparator());
        return builder.toString();
    }


}
