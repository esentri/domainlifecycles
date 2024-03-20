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

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The Class NomnomlMethod encapsulates all information needed for creating a diagram text for a uml
 * method.
 *
 * @author Mario Herb
 */
@Builder
@Getter
@EqualsAndHashCode
public class NomnomlMethod implements DiagramElement {
    private final String visibility;
    private final String name;
    private final NomnomlType returnType;
    private final List<NomnomlParameter> parameters;

    /**
     * Initializes the method.
     *
     * @param visibility of method
     * @param name of method
     * @param returnType of method
     * @param parameters of method
     */
    public NomnomlMethod(String visibility,
                         String name,
                         NomnomlType returnType,
                         List<NomnomlParameter> parameters) {
        this.visibility = Objects.requireNonNull(visibility);
        this.name = Objects.requireNonNull(name);
        this.returnType = Objects.requireNonNull(returnType);
        this.parameters = Objects.requireNonNull(parameters);
    }

    /**
     * Returns the Nomnoml text representation of a method.
     */
    @Override
    public String getDiagramText() {
        StringBuilder builder = new StringBuilder();
        builder.append(visibility);
        builder.append(" ");
        builder.append(returnType.getDiagramText());
        builder.append(" ");
        builder.append(name);
        builder.append("(");
        addParameters(builder);
        builder.append(")");
        builder.append(System.lineSeparator());
        return builder.toString();
    }

    private void addParameters(StringBuilder builder) {
        builder.append(
            parameters
                .stream()
                .map(NomnomlParameter::getDiagramText)
                .collect(Collectors.joining(","))
        );
    }

}
