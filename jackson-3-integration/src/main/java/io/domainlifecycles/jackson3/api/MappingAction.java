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

package io.domainlifecycles.jackson3.api;

/**
 * This enum is used as return value to control mapping behaviour in the customization callbacks
 * {@link JacksonMappingCustomizer}.
 *
 * @author Mario Herb
 */
public enum MappingAction {
    /**
     * Indicates that the default mapping action for a specified step in the JSON-object transformation process
     * (e.g., reading or writing a field or object) should be skipped in user-defined customization callbacks
     * of the {@link JacksonMappingCustomizer}.
     *
     * This allows fine-grained control over the mapping process to override or bypass default behavior.
     */
    SKIP_DEFAULT_ACTION,
    /**
     * Indicates that the default mapping action for a specified step in the JSON-object transformation process
     * (e.g., reading or writing a field or object) should proceed as defined in the standard behavior.
     * This serves as a directive in customization callbacks of the {@link JacksonMappingCustomizer}
     * to allow the default behavior to continue without interruption or modification.
     */
    CONTINUE_WITH_DEFAULT_ACTION
}
