package io.domainlifecycles.staticanalysis;

import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.mirror.resolver.TypeMetaResolver;
import io.domainlifecycles.staticanalysis.fixture.TestAggregate;
import io.domainlifecycles.staticanalysis.fixture.TestCallerService;
import io.domainlifecycles.staticanalysis.fixture.TestCommand;
import io.domainlifecycles.staticanalysis.fixture.TestEvent;
import io.domainlifecycles.staticanalysis.fixture.TestPolymorphicService;
import io.domainlifecycles.staticanalysis.fixture.TestPolymorphicServiceImpl;
import io.domainlifecycles.staticanalysis.fixture.TestPublisherService;
import io.domainlifecycles.staticanalysis.fixture.TestQueryHandlerImpl;
import io.domainlifecycles.staticanalysis.fixture.TestReadModel;
import io.domainlifecycles.staticanalysis.fixture.TestRepositoryImpl;
import io.domainlifecycles.staticanalysis.fixture.TestTargetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * {@link FlowAnalyzer#flowTo(DomainMethod)} / {@link FlowAnalyzer#flowTo(DomainTypeMirror)}: the
 * backward counterpart of {@code flowFrom(...)}, answering "what leads into this node" instead of
 * "what does this node lead to".
 * <p>
 * {@link DomainCalls} is hand-built for the CALL edges, exactly as {@link DomainCallFlowAnalyzerTest}
 * already does for the forward direction - neither test needs a real bytecode analysis, since the
 * analyzer only ever reads the {@link DomainCalls} result it is given.
 */
public class DomainCallFlowAnalyzerBackwardTest {

    @BeforeEach
    void initializeMirror() {
        var factory = new ReflectiveDomainMirrorFactory("io.domainlifecycles.staticanalysis.fixture");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        Domain.initialize(factory);
    }

    @Test
    void testCallReversalFindsTheCaller() {
        var analyzer = new DomainCallFlowAnalyzer(Domain.getDomainMirror(), callerCallsTarget());

        var flow = analyzer.flowTo(domainMethod(TestTargetService.class, "target"));

        assertThat(flow.reachedTypeNames()).contains(TestCallerService.class.getName());
        assertThat(kindOf(flow, TestCallerService.class)).isEqualTo(StepKind.CALL);
    }

    @Test
    void testImplementationReversalChainsIntoTheCallerOfTheInterfaceMethod() {
        // the call is written against the interface, as a real analysis would report it for a
        // repository or outbound service call
        var caller = domainMethod(TestCallerService.class, "callTarget");
        var interfaceMethod = domainMethod(TestPolymorphicService.class, "polymorphicMethod");
        var calls = DomainCalls.builder()
            .add(caller, java.util.List.of(new DomainCalls.CallSite(
                interfaceMethod, TestCallerService.class.getName(), 1)))
            .build();
        var analyzer = new DomainCallFlowAnalyzer(Domain.getDomainMirror(), calls);

        var flow = analyzer.flowTo(domainMethod(TestPolymorphicServiceImpl.class, "polymorphicMethod"));

        assertThat(flow.reachedTypeNames())
            .contains(TestPolymorphicService.class.getName(), TestCallerService.class.getName());
        assertThat(kindOf(flow, TestPolymorphicService.class)).isEqualTo(StepKind.IMPLEMENTATION);
        assertThat(kindOf(flow, TestCallerService.class)).isEqualTo(StepKind.CALL);

        // the caller must chain off the interface-method step, not directly off the start
        var interfaceStep = flow.steps().stream()
            .filter(step -> step.typeName().equals(TestPolymorphicService.class.getName()))
            .findFirst().orElseThrow();
        var callerStep = flow.steps().stream()
            .filter(step -> step.typeName().equals(TestCallerService.class.getName()))
            .findFirst().orElseThrow();
        assertThat(callerStep.from()).contains(interfaceStep);
    }

    @Test
    void testEventListenReversalChainsIntoThePublisher() {
        var analyzer = new DomainCallFlowAnalyzer(Domain.getDomainMirror(), DomainCalls.builder().build());

        var flow = analyzer.flowTo(domainMethod(TestTargetService.class, "onEvent"));

        assertThat(flow.reachedTypeNames())
            .contains(TestEvent.class.getName(), TestPublisherService.class.getName());
        assertThat(kindOf(flow, TestEvent.class)).isEqualTo(StepKind.EVENT_LISTEN);
        assertThat(kindOf(flow, TestPublisherService.class)).isEqualTo(StepKind.EVENT_PUBLISH);
    }

    @Test
    void testFlowToAnEventReversesToItsPublishers() {
        var analyzer = new DomainCallFlowAnalyzer(Domain.getDomainMirror(), DomainCalls.builder().build());

        var eventType = (io.domainlifecycles.mirror.api.DomainEventMirror) Domain.getDomainMirror()
            .getDomainTypeMirror(TestEvent.class.getName()).orElseThrow();
        var flow = analyzer.flowTo(eventType);

        assertThat(flow.reachedTypeNames()).contains(TestPublisherService.class.getName());
        assertThat(kindOf(flow, TestPublisherService.class)).isEqualTo(StepKind.EVENT_PUBLISH);
    }

    @Test
    void testCommandProcessReversalReportsTheCommandAsALeaf() {
        var analyzer = new DomainCallFlowAnalyzer(Domain.getDomainMirror(), DomainCalls.builder().build());

        var flow = analyzer.flowTo(domainMethod(TestTargetService.class, "process"));

        assertThat(flow.reachedTypeNames()).contains(TestCommand.class.getName());
        var commandStep = flow.steps().stream()
            .filter(step -> step.typeName().equals(TestCommand.class.getName()))
            .findFirst().orElseThrow();
        assertThat(commandStep.kind()).isEqualTo(StepKind.COMMAND_PROCESS);

        // nothing in the model knows where a command originates: the branch ends there
        var successorsOfCommandStep = flow.steps().stream()
            .filter(step -> step.from().filter(from -> from == commandStep).isPresent())
            .toList();
        assertThat(successorsOfCommandStep).isEmpty();
    }

    @Test
    void testFlowToATypeUnionsTheBackwardFlowsOfAllItsOwnMethods() {
        var analyzer = new DomainCallFlowAnalyzer(Domain.getDomainMirror(), callerCallsTarget());

        var targetType = Domain.getDomainMirror()
            .getDomainTypeMirror(TestTargetService.class.getName()).orElseThrow();
        var flow = analyzer.flowTo(targetType);

        // reached via target(): CALL; via onEvent(): EVENT_LISTEN -> EVENT_PUBLISH;
        // via process(): COMMAND_PROCESS
        assertThat(flow.reachedTypeNames()).contains(
            TestCallerService.class.getName(),
            TestEvent.class.getName(), TestPublisherService.class.getName(),
            TestCommand.class.getName());
    }

    @Test
    void testFlowToAnAggregateReversesTheManagingRepository() {
        var analyzer = new DomainCallFlowAnalyzer(Domain.getDomainMirror(), DomainCalls.builder().build());

        var aggregateType = Domain.getDomainMirror()
            .getDomainTypeMirror(TestAggregate.class.getName()).orElseThrow();
        var flow = analyzer.flowTo(aggregateType);

        assertThat(flow.reachedTypeNames()).contains(TestRepositoryImpl.class.getName());
        assertThat(kindOf(flow, TestRepositoryImpl.class)).isEqualTo(StepKind.MANAGES_AGGREGATE);
    }

    @Test
    void testFlowToAReadModelReversesTheProvidingQueryHandler() {
        var analyzer = new DomainCallFlowAnalyzer(Domain.getDomainMirror(), DomainCalls.builder().build());

        var readModelType = Domain.getDomainMirror()
            .getDomainTypeMirror(TestReadModel.class.getName()).orElseThrow();
        var flow = analyzer.flowTo(readModelType);

        assertThat(flow.reachedTypeNames()).contains(TestQueryHandlerImpl.class.getName());
        assertThat(kindOf(flow, TestQueryHandlerImpl.class)).isEqualTo(StepKind.PROVIDES_READ_MODEL);
    }

    @Test
    void testMutualCallersAreMarkedCyclicAndDoNotLoopForever() {
        var a = domainMethod(TestCallerService.class, "callTarget");
        var b = domainMethod(TestTargetService.class, "target");
        var calls = DomainCalls.builder()
            .add(a, java.util.List.of(new DomainCalls.CallSite(b, TestCallerService.class.getName(), 1)))
            .add(b, java.util.List.of(new DomainCalls.CallSite(a, TestTargetService.class.getName(), 1)))
            .build();
        var analyzer = new DomainCallFlowAnalyzer(Domain.getDomainMirror(), calls);

        var flow = analyzer.flowTo(b);

        assertThat(flow.reachedTypeNames())
            .contains(TestCallerService.class.getName(), TestTargetService.class.getName());
        assertThat(flow.steps().stream().anyMatch(Step::cyclic)).isTrue();
    }

    private static DomainCalls callerCallsTarget() {
        var caller = domainMethod(TestCallerService.class, "callTarget");
        var target = domainMethod(TestTargetService.class, "target");
        return DomainCalls.builder()
            .add(caller, java.util.List.of(
                new DomainCalls.CallSite(target, TestCallerService.class.getName(), 1)))
            .build();
    }

    private static StepKind kindOf(Flow flow, Class<?> typeName) {
        return flow.steps().stream()
            .filter(step -> step.typeName().equals(typeName.getName()))
            .findFirst()
            .orElseThrow()
            .kind();
    }

    private static DomainMethod domainMethod(Class<?> ownerClass, String methodName) {
        var typeMirror = Domain.getDomainMirror().getDomainTypeMirror(ownerClass.getName()).orElseThrow();
        var method = typeMirror.getMethods().stream()
            .filter(m -> m.getName().equals(methodName))
            .findFirst()
            .orElseThrow();
        return new DomainMethod(typeMirror.getTypeName(), method);
    }
}
