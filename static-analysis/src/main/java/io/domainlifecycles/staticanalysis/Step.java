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
import io.domainlifecycles.mirror.api.MethodMirror;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * One step of a {@link Flow}: a node the flow reached, together with how it got there.
 * <p>
 * A step is therefore both a node and an edge. It names what was reached
 * ({@link MethodStep}, {@link EventStep}, {@link CommandStep}), and it links back to the step it
 * was reached from ({@link #from()}) via a given mechanism ({@link #kind()}). Walking
 * {@link #path()} yields the full chain from the start of the flow down to this step, which is
 * what makes the flat step list of a {@link Flow} sufficient to reconstruct the branching call
 * tree.
 * <p>
 * A {@link MethodStep} carries a {@link DomainMethod}, not a bare {@link MethodMirror},
 * so that the concrete owner type resolved by the {@link StaticAnalyzer} is preserved. Using the
 * mirror's declaring type instead would report a base class where the analysis correctly
 * determined the overriding subclass.
 *
 * @author Mario Herb
 */
public sealed interface Step {

    /**
     * @return the step this one was reached from, empty for the start of the flow
     */
    Optional<Step> from();

    /**
     * @return the mechanism that carried the flow from {@link #from()} to this step
     */
    StepKind kind();

    /**
     * @return the number of steps between the start of the flow and this step, {@code 0} for the
     *     start itself
     */
    int depth();

    /**
     * Whether this step closes a cycle, i.e. what it reached already occurs among its own
     * predecessors. A cyclic step is reported but not expanded any further.
     *
     * @return whether this step closes a cycle
     */
    boolean cyclic();

    /**
     * @return the full qualified name of the domain type this step is about
     */
    String typeName();

    /**
     * A stable identifier of what this step reached, independent of how it was reached. Two steps
     * with the same node key denote the same domain method, event or command.
     *
     * @return the identifier of the reached node
     */
    String nodeKey();

    /**
     * @return a short human readable description of the reached node
     */
    String describe();

    /**
     * @return whether this step is the start of the flow
     */
    default boolean isStart() {
        return from().isEmpty();
    }

    /**
     * @return the chain of steps from the start of the flow up to and including this one
     */
    default List<Step> path() {
        List<Step> path = new ArrayList<>();
        Optional<Step> current = Optional.of(this);
        while (current.isPresent()) {
            path.add(current.get());
            current = current.get().from();
        }
        Collections.reverse(path);
        return List.copyOf(path);
    }

    /**
     * The {@link #nodeKey()} a {@link MethodStep} for the given method would have. Allows checking
     * a node against a path before a step for it is created.
     *
     * @param method the method, must not be {@code null}
     * @return the node key
     */
    static String nodeKeyOf(DomainMethod method) {
        return "M:" + method.typeName() + "#" + method.signature();
    }

    /**
     * The {@link #nodeKey()} an {@link EventStep} for the given event would have.
     *
     * @param event the event, must not be {@code null}
     * @return the node key
     */
    static String nodeKeyOf(DomainEventMirror event) {
        return "E:" + event.getTypeName();
    }

    /**
     * The {@link #nodeKey()} a {@link CommandStep} for the given command would have.
     *
     * @param command the command, must not be {@code null}
     * @return the node key
     */
    static String nodeKeyOf(DomainCommandMirror command) {
        return "C:" + command.getTypeName();
    }

    /**
     * Creates the starting step of a flow beginning at a domain method.
     *
     * @param method the method to start from, must not be {@code null}
     * @return the starting step
     */
    static MethodStep start(DomainMethod method) {
        return new MethodStep(Optional.empty(), StepKind.START, 0, false, method);
    }

    /**
     * Creates the starting step of a flow beginning at a domain event.
     *
     * @param event the event to start from, must not be {@code null}
     * @return the starting step
     */
    static EventStep start(DomainEventMirror event) {
        return new EventStep(Optional.empty(), StepKind.START, 0, false, event);
    }

    /**
     * Creates the starting step of a flow beginning at a domain command.
     *
     * @param command the command to start from, must not be {@code null}
     * @return the starting step
     */
    static CommandStep start(DomainCommandMirror command) {
        return new CommandStep(Optional.empty(), StepKind.START, 0, false, command);
    }

    /**
     * Creates a step for a method called from the given predecessor.
     *
     * @param from   the calling step, must not be {@code null}
     * @param called the called method, must not be {@code null}
     * @param cyclic whether the called method already occurs among the predecessors
     * @return the step
     */
    static MethodStep called(Step from, DomainMethod called, boolean cyclic) {
        return new MethodStep(Optional.of(from), StepKind.CALL, from.depth() + 1, cyclic, called);
    }

    /**
     * Creates a step for an event published by the given predecessor.
     *
     * @param from   the publishing step, must not be {@code null}
     * @param event  the published event, must not be {@code null}
     * @param cyclic whether the event already occurs among the predecessors
     * @return the step
     */
    static EventStep published(Step from, DomainEventMirror event, boolean cyclic) {
        return new EventStep(Optional.of(from), StepKind.EVENT_PUBLISH, from.depth() + 1, cyclic,
            event);
    }

    /**
     * Creates a step for a method listening to the event of the given predecessor.
     *
     * @param from     the event step, must not be {@code null}
     * @param listener the listening method, must not be {@code null}
     * @param cyclic   whether the listening method already occurs among the predecessors
     * @return the step
     */
    static MethodStep listening(Step from, DomainMethod listener, boolean cyclic) {
        return new MethodStep(Optional.of(from), StepKind.EVENT_LISTEN, from.depth() + 1, cyclic,
            listener);
    }

    /**
     * Creates a step for a method processing the command of the given predecessor.
     *
     * @param from      the command step, must not be {@code null}
     * @param processor the processing method, must not be {@code null}
     * @param cyclic    whether the processing method already occurs among the predecessors
     * @return the step
     */
    static MethodStep processing(Step from, DomainMethod processor, boolean cyclic) {
        return new MethodStep(Optional.of(from), StepKind.COMMAND_PROCESS, from.depth() + 1,
            cyclic, processor);
    }

    /**
     * A domain method reached by the flow, identified by its concrete owner type and the mirrored
     * method.
     *
     * @param from   the step this one was reached from, empty for the start of the flow
     * @param kind   the mechanism that carried the flow to this step
     * @param depth  the distance from the start of the flow
     * @param cyclic whether this step closes a cycle
     * @param method the reached method together with its concrete owner type
     */
    record MethodStep(Optional<Step> from, StepKind kind, int depth, boolean cyclic,
                      DomainMethod method) implements Step {

        public MethodStep {
            Objects.requireNonNull(from, "A from Optional must be given!");
            Objects.requireNonNull(kind, "A StepKind must be given!");
            Objects.requireNonNull(method, "A DomainMethod must be given!");
        }

        @Override
        public String typeName() {
            return method.typeName();
        }

        @Override
        public String nodeKey() {
            return nodeKeyOf(method);
        }

        @Override
        public String describe() {
            return method.typeName() + "." + method.signature();
        }

        @Override
        public String toString() {
            return renderStep(this);
        }
    }

    /**
     * A domain event reached by the flow, because a method of the preceding step publishes it.
     *
     * @param from   the step this one was reached from, empty for the start of the flow
     * @param kind   the mechanism that carried the flow to this step
     * @param depth  the distance from the start of the flow
     * @param cyclic whether this step closes a cycle
     * @param event  the reached domain event
     */
    record EventStep(Optional<Step> from, StepKind kind, int depth, boolean cyclic,
                     DomainEventMirror event) implements Step {

        public EventStep {
            Objects.requireNonNull(from, "A from Optional must be given!");
            Objects.requireNonNull(kind, "A StepKind must be given!");
            Objects.requireNonNull(event, "A DomainEventMirror must be given!");
        }

        @Override
        public String typeName() {
            return event.getTypeName();
        }

        @Override
        public String nodeKey() {
            return nodeKeyOf(event);
        }

        @Override
        public String describe() {
            return event.getTypeName();
        }

        @Override
        public String toString() {
            return renderStep(this);
        }
    }

    /**
     * A domain command reached by the flow. Commands are the trigger of a flow, so a command step
     * normally is the start of one.
     *
     * @param from    the step this one was reached from, empty for the start of the flow
     * @param kind    the mechanism that carried the flow to this step
     * @param depth   the distance from the start of the flow
     * @param cyclic  whether this step closes a cycle
     * @param command the reached domain command
     */
    record CommandStep(Optional<Step> from, StepKind kind, int depth, boolean cyclic,
                       DomainCommandMirror command) implements Step {

        public CommandStep {
            Objects.requireNonNull(from, "A from Optional must be given!");
            Objects.requireNonNull(kind, "A StepKind must be given!");
            Objects.requireNonNull(command, "A DomainCommandMirror must be given!");
        }

        @Override
        public String typeName() {
            return command.getTypeName();
        }

        @Override
        public String nodeKey() {
            return nodeKeyOf(command);
        }

        @Override
        public String describe() {
            return command.getTypeName();
        }

        @Override
        public String toString() {
            return renderStep(this);
        }
    }

    /**
     * Renders a step without following {@link #from()}, which the generated record
     * {@code toString} would do recursively for the whole predecessor chain.
     */
    private static String renderStep(Step step) {
        return step.depth() + " " + step.kind() + " " + step.describe()
            + (step.cyclic() ? " (cycle)" : "");
    }
}
