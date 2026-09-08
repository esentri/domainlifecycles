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
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.domain.MyAggregateRoot;
import test.domain.MyApplicationService;
import test.domain.MyBaseService;
import test.domain.MyDomainCommand;
import test.domain.MyDomainEvent;
import test.domain.MyDomainService;
import test.domain.MyOutboundService;
import test.domain.MyOverridingService;
import test.domain.MyRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class TestFlowAnalysis {

    private static DomainMirror domainMirror;

    private static DomainCalls calls;

    private static FlowAnalyzer analyzer;

    @BeforeAll
    public static void before() {
        calls = AnalyzedTestDomain.domainCalls();
        domainMirror = AnalyzedTestDomain.domainMirror();
        analyzer = new DomainCallFlowAnalyzer(domainMirror, calls);
    }

    // ---------------------------------------------------------------------
    // Plain call flow
    // ---------------------------------------------------------------------

    @Test
    public void testDirectCallsAreDepthOne() {
        var flow = analyzer.flowFrom(methodCall(MyApplicationService.class, "doSomething"));

        assertThat(flow.start().isStart()).isTrue();
        assertThat(flow.start().kind()).isEqualTo(StepKind.START);
        assertThat(flow.start().depth()).isZero();
        assertThat(flow.steps()).element(0).isSameAs(flow.start());

        // everything doSomething calls directly sits at depth 1
        assertThat(methodStep(flow, MyRepository.class, "findById")).isPresent();
        assertThat(methodStep(flow, MyRepository.class, "findById").get().depth()).isEqualTo(1);
        assertThat(methodStep(flow, MyRepository.class, "update").get().depth()).isEqualTo(1);
        assertThat(methodStep(flow, MyAggregateRoot.class, "doSomething").get().depth()).isEqualTo(1);
        assertThat(methodStep(flow, MyDomainCommand.class, "id").get().depth()).isEqualTo(1);

        assertThat(methodStep(flow, MyAggregateRoot.class, "doSomething").get().kind())
            .isEqualTo(StepKind.CALL);
    }

    @Test
    public void testFlowOfAMethodWithoutDomainCallsIsEmpty() {
        var flow = analyzer.flowFrom(methodCall(MyBaseService.class, "process"));

        assertThat(flow.isEmpty()).isTrue();
        assertThat(flow.steps()).hasSize(1);
        assertThat(flow.maxDepth()).isZero();
        assertThat(flow.truncated()).isFalse();
    }

    @Test
    public void testStepKeepsTheConcreteOwnerTypeNotTheDeclaringType() {
        // MyOverridingService.process calls super.process, so the flow must continue on
        // MyBaseService - the concrete owner the static analysis resolved. Deriving the type from
        // the MethodMirror's declaring type would be wrong for the start step in the other
        // direction, so both ends are checked here.
        var flow = analyzer.flowFrom(methodCall(MyOverridingService.class, "process"));

        assertThat(flow.start().typeName()).isEqualTo(MyOverridingService.class.getTypeName());

        var called = methodStep(flow, MyBaseService.class, "process");
        assertThat(called).isPresent();
        assertThat(called.get().depth()).isEqualTo(1);
        assertThat(flow.reachedTypeNames())
            .containsExactly(MyOverridingService.class.getTypeName(),
                MyBaseService.class.getTypeName());
    }

    @Test
    public void testPathReconstructsTheBranchOfTheTree() {
        var flow = analyzer.flowFrom(methodCall(MyApplicationService.class, "doSomething"));

        var aggregateStep = methodStep(flow, MyAggregateRoot.class, "doSomething");
        assertThat(aggregateStep).isPresent();

        var path = aggregateStep.get().path();
        assertThat(path).hasSize(2);
        assertThat(path.get(0)).isSameAs(flow.start());
        assertThat(path.get(1)).isSameAs(aggregateStep.get());
    }

    // ---------------------------------------------------------------------
    // Event join - the part no call analysis can see
    // ---------------------------------------------------------------------

    @Test
    public void testFlowContinuesAcrossAPublishedEvent() {
        var flow = analyzer.flowFrom(methodCall(MyApplicationService.class, "doSomething"));

        // MyAggregateRoot.doSomething is annotated with @Publishes(MyDomainEvent.class)
        var eventStep = eventStep(flow, MyDomainEvent.class);
        assertThat(eventStep).isPresent();
        assertThat(eventStep.get().kind()).isEqualTo(StepKind.EVENT_PUBLISH);
        assertThat(eventStep.get().depth()).isEqualTo(2);
        assertThat(eventStep.get().from()).contains(
            methodStep(flow, MyAggregateRoot.class, "doSomething").get());

        // MyDomainService.onMyDomainEvent listens to it - the asynchronous continuation
        var listenerStep = methodStep(flow, MyDomainService.class, "onMyDomainEvent");
        assertThat(listenerStep).isPresent();
        assertThat(listenerStep.get().kind()).isEqualTo(StepKind.EVENT_LISTEN);
        assertThat(listenerStep.get().depth()).isEqualTo(3);

        // and its own calls continue the flow one step further
        var outboundStep = methodStep(flow, MyOutboundService.class, "doSomething");
        assertThat(outboundStep).isPresent();
        assertThat(outboundStep.get().kind()).isEqualTo(StepKind.CALL);
        assertThat(outboundStep.get().depth()).isEqualTo(4);

        // the full chain, which is the question the flow analysis exists to answer
        assertThat(outboundStep.get().path().stream().map(Step::describe))
            .containsExactly(
                MyApplicationService.class.getTypeName()
                    + ".doSomething(" + MyDomainCommand.class.getTypeName() + ")",
                MyAggregateRoot.class.getTypeName()
                    + ".doSomething(" + MyDomainCommand.class.getTypeName() + ")",
                MyDomainEvent.class.getTypeName(),
                MyDomainService.class.getTypeName()
                    + ".onMyDomainEvent(" + MyDomainEvent.class.getTypeName() + ")",
                MyOutboundService.class.getTypeName()
                    + ".doSomething(" + MyAggregateRoot.class.getTypeName() + ")");
    }

    @Test
    public void testEventsCanBeSwitchedOff() {
        var callsOnly = new DomainCallFlowAnalyzer(domainMirror, calls,
            FlowConfig.defaults().withFollowEvents(false));

        var flow = callsOnly.flowFrom(methodCall(MyApplicationService.class, "doSomething"));

        assertThat(flow.steps()).noneMatch(step -> step instanceof Step.EventStep);
        assertThat(flow.reachedTypeNames())
            .doesNotContain(MyDomainEvent.class.getTypeName(),
                MyDomainService.class.getTypeName(),
                MyOutboundService.class.getTypeName());
    }

    @Test
    public void testFlowFromAnEventStartsAtItsListeners() {
        var flow = analyzer.flowFrom(eventMirror(MyDomainEvent.class));

        assertThat(flow.start()).isInstanceOf(Step.EventStep.class);
        assertThat(flow.start().typeName()).isEqualTo(MyDomainEvent.class.getTypeName());

        var listenerStep = methodStep(flow, MyDomainService.class, "onMyDomainEvent");
        assertThat(listenerStep).isPresent();
        assertThat(listenerStep.get().kind()).isEqualTo(StepKind.EVENT_LISTEN);
        assertThat(listenerStep.get().depth()).isEqualTo(1);

        assertThat(methodStep(flow, MyOutboundService.class, "doSomething").get().depth())
            .isEqualTo(2);
    }

    @Test
    public void testFlowFromACommandStartsAtItsProcessors() {
        var flow = analyzer.flowFrom(commandMirror(MyDomainCommand.class));

        assertThat(flow.start()).isInstanceOf(Step.CommandStep.class);

        var processorStep = methodStep(flow, MyApplicationService.class, "doSomething");
        assertThat(processorStep).isPresent();
        assertThat(processorStep.get().kind()).isEqualTo(StepKind.COMMAND_PROCESS);
        assertThat(processorStep.get().depth()).isEqualTo(1);

        // MyAggregateRoot.doSomething takes the command as well, so it is a processor too
        assertThat(methodStep(flow, MyAggregateRoot.class, "doSomething").get().depth())
            .isEqualTo(1);
    }

    // ---------------------------------------------------------------------
    // Termination
    // ---------------------------------------------------------------------

    @Test
    @org.junit.jupiter.api.Timeout(value = 30, unit = TimeUnit.SECONDS)
    public void testMutualRecursionIsMarkedAndTerminates() {
        var flow = analyzer.flowFrom(methodCall(MyApplicationService.class, "mutualA"));

        var mutualB = methodStep(flow, MyApplicationService.class, "mutualB");
        assertThat(mutualB).isPresent();
        assertThat(mutualB.get().cyclic()).isFalse();

        // mutualB calls mutualA again: the edge is reported, marked as a cycle, and not expanded
        var backToA = flow.steps().stream()
            .filter(step -> step.typeName().equals(MyApplicationService.class.getTypeName()))
            .filter(step -> step instanceof Step.MethodStep)
            .map(step -> (Step.MethodStep) step)
            .filter(step -> step.method().name().equals("mutualA"))
            .filter(step -> !step.isStart())
            .findFirst();
        assertThat(backToA).isPresent();
        assertThat(backToA.get().cyclic()).isTrue();
        assertThat(backToA.get().depth()).isEqualTo(2);

        // nothing was expanded below the cyclic step
        assertThat(flow.steps()).noneMatch(step -> step.from()
            .filter(from -> from == backToA.get()).isPresent());
    }

    @Test
    public void testNodeReachedTwiceIsReportedTwiceButExpandedOnce() {
        // findById is called by doSomething directly and again by the event listener further down
        var flow = analyzer.flowFrom(methodCall(MyApplicationService.class, "doSomething"));

        var findByIdSteps = flow.steps().stream()
            .filter(step -> step instanceof Step.MethodStep)
            .map(step -> (Step.MethodStep) step)
            .filter(step -> step.method().name().equals("findById"))
            .toList();

        assertThat(findByIdSteps).hasSizeGreaterThan(1);
        assertThat(findByIdSteps).extracting(Step.MethodStep::nodeKey)
            .containsOnly(findByIdSteps.get(0).nodeKey());
    }

    @Test
    public void testMaxDepthTruncates() {
        var shallow = new DomainCallFlowAnalyzer(domainMirror, calls,
            FlowConfig.defaults().withMaxDepth(1));

        var flow = shallow.flowFrom(methodCall(MyApplicationService.class, "doSomething"));

        assertThat(flow.maxDepth()).isEqualTo(1);
        assertThat(flow.truncated()).isTrue();
        assertThat(flow.steps()).allMatch(step -> step.depth() <= 1);
        assertThat(flow.reachedTypeNames()).doesNotContain(MyDomainEvent.class.getTypeName());
    }

    @Test
    public void testUntruncatedFlowSaysSo() {
        var flow = analyzer.flowFrom(methodCall(MyApplicationService.class, "doSomething"));
        assertThat(flow.truncated()).isFalse();
    }

    // ---------------------------------------------------------------------
    // Filtering
    // ---------------------------------------------------------------------

    @Test
    public void testAccessorsCanBeExcluded() {
        var withoutAccessors = new DomainCallFlowAnalyzer(domainMirror, calls,
            FlowConfig.defaults().excludingAccessors());

        var flow = withoutAccessors.flowFrom(
            methodCall(MyApplicationService.class, "doSomething"));

        // command.id() is a record accessor and carries no domain behaviour
        assertThat(methodStep(flow, MyDomainCommand.class, "id")).isEmpty();
        // while the behavioural calls are untouched
        assertThat(methodStep(flow, MyAggregateRoot.class, "doSomething")).isPresent();
        assertThat(methodStep(flow, MyRepository.class, "findById")).isPresent();
    }

    // ---------------------------------------------------------------------
    // Rendering
    // ---------------------------------------------------------------------

    @Test
    public void testRenderingShowsTheTreeStructure() {
        var flow = analyzer.flowFrom(methodCall(MyOverridingService.class, "process"));

        assertThat(flow.toString()).isEqualTo(
            MyOverridingService.class.getTypeName()
                + ".process(" + MyDomainCommand.class.getTypeName() + ")\n"
                + "  CALL -> " + MyBaseService.class.getTypeName()
                + ".process(" + MyDomainCommand.class.getTypeName() + ")\n");
    }

    @Test
    public void testRenderingOfAnEventCrossingFlow() {
        var flow = analyzer.flowFrom(methodCall(MyApplicationService.class, "doSomething"));

        System.out.println(flow);

        assertThat(flow.toString().lines())
            .anyMatch(line -> line.startsWith("      EVENT_LISTEN -> "));
    }

    // ---------------------------------------------------------------------
    // Lookup by name
    // ---------------------------------------------------------------------

    @Test
    public void testFlowFromTypeAndMethodName() {
        var flow = analyzer.flowFrom(MyApplicationService.class.getTypeName(), "doSomething");
        assertThat(flow).isPresent();
        assertThat(flow.get().start().typeName())
            .isEqualTo(MyApplicationService.class.getTypeName());

        assertThat(analyzer.flowFrom(MyApplicationService.class.getTypeName(), "notThere"))
            .isEmpty();
        assertThat(analyzer.flowFrom("does.not.Exist", "doSomething")).isEmpty();
    }

    // ---------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------

    private static DomainMethod methodCall(Class<?> type, String methodName) {
        Optional<DomainTypeMirror> typeMirror =
            domainMirror.getDomainTypeMirror(type.getTypeName());
        assertThat(typeMirror).isPresent();
        var method = typeMirror.get().getMethods().stream()
            .filter(m -> m.getName().equals(methodName))
            .findFirst();
        assertThat(method).isPresent();
        return new DomainMethod(typeMirror.get().getTypeName(), method.get());
    }

    private static Optional<Step.MethodStep> methodStep(Flow flow, Class<?> type,
                                                        String methodName) {
        return flow.steps().stream()
            .filter(step -> step instanceof Step.MethodStep)
            .map(step -> (Step.MethodStep) step)
            .filter(step -> step.typeName().equals(type.getTypeName()))
            .filter(step -> step.method().name().equals(methodName))
            .findFirst();
    }

    private static Optional<Step.EventStep> eventStep(Flow flow, Class<?> eventType) {
        return flow.steps().stream()
            .filter(step -> step instanceof Step.EventStep)
            .map(step -> (Step.EventStep) step)
            .filter(step -> step.typeName().equals(eventType.getTypeName()))
            .findFirst();
    }

    private static DomainEventMirror eventMirror(Class<?> eventType) {
        List<DomainEventMirror> mirrors = domainMirror.getAllDomainEventMirrors().stream()
            .filter(m -> m.getTypeName().equals(eventType.getTypeName()))
            .toList();
        assertThat(mirrors).hasSize(1);
        return mirrors.get(0);
    }

    private static DomainCommandMirror commandMirror(Class<?> commandType) {
        List<DomainCommandMirror> mirrors = domainMirror.getAllDomainCommandMirrors().stream()
            .filter(m -> m.getTypeName().equals(commandType.getTypeName()))
            .toList();
        assertThat(mirrors).hasSize(1);
        return mirrors.get(0);
    }
}
