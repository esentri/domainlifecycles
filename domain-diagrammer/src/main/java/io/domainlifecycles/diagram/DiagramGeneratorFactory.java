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

package io.domainlifecycles.diagram;

import io.domainlifecycles.diagram.domain.DomainDiagramGenerator;
import io.domainlifecycles.diagram.domain.config.DomainDiagramConfig;
import io.domainlifecycles.mirror.api.DomainModel;

/**
 * Factory to create new {@link Diagram}a by a given {@link DiagramConfig}.
 *
 * @author Mario Herb
 */
public class DiagramGeneratorFactory {

    /**
     * Creates a new DomainDiagramGenerator
     *
     * @param diagramConfig diagram generator configuration
     * @param domainModel the Domain Model operated on
     * @return new instance of DomainDiagramGenerator
     */
    public static Diagram getDomainDiagramGenerator(DiagramConfig diagramConfig, DomainModel domainModel) {
        if (diagramConfig instanceof DomainDiagramConfig) {
            return new DomainDiagramGenerator((DomainDiagramConfig) diagramConfig, domainModel);
        }
        return null;
    }
}
