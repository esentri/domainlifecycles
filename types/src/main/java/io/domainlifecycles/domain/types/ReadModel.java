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

package io.domainlifecycles.domain.types;

/**
 * This is the common supertype (marker interface) to represent ReadModels.
 * <p>Sometimes it is convenient to load only parts of an Aggregate or a coherent model of data that spans
 * multiple Aggregates. This is normally the case, when data is presented in a user interface. ReadModels
 * can be useful, if the Aggregate boundaries are too wide or too narrow. They are not really part of the inner core
 * domain,
 * but it helps to know which ReadModels are used/available in the application.
 * </p>
 * <p>
 * Further information:
 * <a href="http://gorodinski.com/blog/2012/04/25/read-models-as-a-tactical-pattern-in-domain-driven-design-ddd/">link</a>
 * </p>
 *
 * @author Mario Herb
 */
public interface ReadModel {
}
