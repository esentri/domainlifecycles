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

import io.domainlifecycles.domain.types.QueryHandler;

import java.util.List;
import java.util.Optional;

/**
 * A QueryHandlerMirror mirrors {@link QueryHandler} instances.
 *
 * @author Mario Herb
 */
public interface QueryHandlerMirror extends ServiceKindMirror {

    /**
     * @return an Optional containing the mirror the provided ReadModel.
     * Every QueryHandler is supposed to provide exactly one ReadModel.
     * If no ReadModelMirror could be detected the Optional is empty,
     */
    Optional<ReadModelMirror> getProvidedReadModel();


    /**
     * @return the interface type name (full qualified class name) that the mirrored QueryHandler implements.
     * The interfaces therefore must extend {@link QueryHandler}.
     * If separation of concerns is respected, we have at most only one interface, which fulfills that condition.
     */
    List<String> getQueryHandlerInterfaceTypeNames();
}
