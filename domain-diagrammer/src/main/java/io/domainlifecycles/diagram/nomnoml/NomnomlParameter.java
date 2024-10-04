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

import java.util.Objects;

/**
 * The Class NomnomlParameter encapsulates all information needed for creating a diagram text for a uml
 * method parameter.
 *
 * @author Mario Herb
 */
@Builder
@Getter
@EqualsAndHashCode
public class NomnomlParameter implements DiagramElement {
    private final NomnomlType type;
    private final boolean required;

    /**
     * Initializes the parameter.
     *
     * @param type     of parameter
     * @param required is param required
     */
    public NomnomlParameter(NomnomlType type,
                            boolean required) {
        this.type = Objects.requireNonNull(type);
        this.required = required;
    }

    /**
     * Returns the Nomnoml text representation of a parameter.
     */
    @Override
    public String getDiagramText() {
        return (required ? "\\# " : "") + type.getDiagramText();
    }
}
