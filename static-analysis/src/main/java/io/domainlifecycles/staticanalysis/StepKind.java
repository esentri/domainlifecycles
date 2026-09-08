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
 *  Copyright 2019-2026 the original author or authors.
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

package io.domainlifecycles.staticanalysis;

/**
 * How a {@link Step} was reached from its predecessor.
 * <p>
 * A domain flow is not made of method calls alone: it continues across the asynchronous boundary
 * of a domain event, which no static call analysis can see. The kind makes explicit which
 * mechanism carried the flow from one step to the next.
 *
 * @author Mario Herb
 */
public enum StepKind {

    /**
     * The step is the starting point of the flow and has no predecessor.
     */
    START,

    /**
     * The predecessor method calls the step's method. Taken from {@link DomainCalls}.
     */
    CALL,

    /**
     * The predecessor method publishes the step's domain event. Taken from the mirror
     * ({@code @Publishes}).
     */
    EVENT_PUBLISH,

    /**
     * The step's method listens to the predecessor's domain event. Taken from the mirror
     * ({@code @ListensTo} / {@code @DomainEventListener}). This is the asynchronous continuation
     * of the flow, not a method call.
     */
    EVENT_LISTEN,

    /**
     * The step's method processes the predecessor's domain command, i.e. it takes the command as
     * a parameter. Taken from the mirror.
     */
    COMMAND_PROCESS
}
