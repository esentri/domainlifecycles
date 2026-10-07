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

import io.domainlifecycles.mirror.api.DomainCommandMirror;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.DomainTypeMirror;

import java.util.Optional;

/**
 * Answers which domain types and methods are reached, in how many steps, starting from a given
 * point in the domain - and, symmetrically, which domain types and methods lead INTO a given
 * point ({@code flowTo}), i.e. the entry channels through which it is reached.
 * <p>
 * The analyzer works on top of an already computed {@link DomainCalls} graph and joins it with the
 * event and command information of the mirror. That join is the point: the static call graph ends
 * where a domain event is published, while the flow continues in the event's listeners.
 * <p>
 * There is no {@code flowTo(DomainCommandMirror)}: nothing in the analyzed data models where a
 * command originates, only which methods process it, so a command can only ever appear as a
 * (terminal) step reached by a {@code flowTo} query, never as its starting point.
 * <p>
 * Implementations are queried repeatedly for different starting points, so they are expected to be
 * immutable and cheap to call.
 *
 * @author Mario Herb
 */
public interface FlowAnalyzer {

    /**
     * Analyzes the flow starting at a domain method.
     *
     * @param start the method to start from, must not be {@code null}
     * @return the flow reachable from the given method
     */
    Flow flowFrom(DomainMethod start);

    /**
     * Analyzes the flow starting at a domain event, i.e. beginning with all methods listening to
     * it.
     *
     * @param event the event to start from, must not be {@code null}
     * @return the flow reachable from the given event
     */
    Flow flowFrom(DomainEventMirror event);

    /**
     * Analyzes the flow starting at a domain command, i.e. beginning with all methods processing
     * it.
     *
     * @param command the command to start from, must not be {@code null}
     * @return the flow reachable from the given command
     */
    Flow flowFrom(DomainCommandMirror command);

    /**
     * Convenience lookup of a starting method in the mirror. If the type declares several
     * overloads of the name, the first one is used.
     *
     * @param typeName   the full qualified name of the owning domain type
     * @param methodName the method name to start from
     * @return the flow, empty if the type or the method is unknown to the mirror
     */
    Optional<Flow> flowFrom(String typeName, String methodName);

    /**
     * Analyzes the flow leading into a domain method: the callers, direct and transitive, that
     * eventually reach it.
     *
     * @param target the method to find the entry channels into, must not be {@code null}
     * @return the flow reaching the given method
     */
    Flow flowTo(DomainMethod target);

    /**
     * Analyzes the flow leading into a domain event, i.e. the methods publishing it.
     *
     * @param event the event to find the entry channels into, must not be {@code null}
     * @return the flow reaching the given event
     */
    Flow flowTo(DomainEventMirror event);

    /**
     * Analyzes the flow leading into a domain type, i.e. into any of its methods, unioned with -
     * for an Aggregate or ReadModel - the Repository or QueryHandler exposing it.
     * <p>
     * Passing a {@link DomainEventMirror} here (or to the {@code String}/{@code String} overload)
     * would resolve to this method by its static type and be treated as a plain type with no
     * methods and no such structural counterpart, silently returning an empty flow - call
     * {@link #flowTo(DomainEventMirror)} directly for an event.
     *
     * @param target the domain type to find the entry channels into, must not be {@code null}
     * @return the flow reaching the given type
     */
    Flow flowTo(DomainTypeMirror target);

    /**
     * Convenience lookup of a target method in the mirror. If the type declares several overloads
     * of the name, the first one is used.
     *
     * @param typeName   the full qualified name of the owning domain type
     * @param methodName the method name to find the entry channels into
     * @return the flow, empty if the type or the method is unknown to the mirror
     */
    Optional<Flow> flowTo(String typeName, String methodName);
}
