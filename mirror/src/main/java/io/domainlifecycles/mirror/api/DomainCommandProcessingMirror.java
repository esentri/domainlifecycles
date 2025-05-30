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

package io.domainlifecycles.mirror.api;

import io.domainlifecycles.domain.types.DomainCommand;

import java.util.List;

/**
 * All domain types mirrored, that can process {@link DomainCommand} instances, implement this interface.
 *
 * @author Mario Herb
 */
public interface DomainCommandProcessingMirror {

    /**
     * Query, if a given DomainCommand represented by the specified {@link DomainCommandMirror} is processed by this
     * mirror.
     * The mirrored class therefore must declare a method annotated with the specified DomainCommand as the only
     * parameter.
     *
     * @param command the command to process
     * @return true, if the given command is processed.
     */
    boolean processes(DomainCommandMirror command);

    /**
     * Retrieves the list of domain commands processed by the implementing class.
     *
     * @return a list of {@link DomainCommandMirror} instances that are processed by this mirror.
     */
    List<DomainCommandMirror> processedDomainCommands();


}
