package io.domainlifecycles.staticanalysis;

import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.staticanalysis.factory.Order;
import io.domainlifecycles.staticanalysis.factory.OrderFactory;
import io.domainlifecycles.staticanalysis.factory.OrderLine;
import io.domainlifecycles.staticanalysis.factory.OrderService;
import io.domainlifecycles.staticanalysis.factory.OrderSummary;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A flow follows a factory method to the domain type it creates, and backwards from a domain type to the factory
 * methods creating it ({@link StepKind#CREATES}) - the static analysis leaves out the constructors doing it.
 * <p>
 * The fixture in {@code io.domainlifecycles.staticanalysis.factory}, with hand-built calls:
 * <pre>
 * OrderService.place  --&gt; OrderFactory.create (creates Order)
 * OrderFactory.summarize  returns the ReadModel OrderSummary, provided by no query handler
 * Order.open          &#64;FactoryMethod creating an Order itself
 * Order.addLine       &#64;FactoryMethod creating the entity OrderLine
 * </pre>
 */
public class FactoryFlowTest {

    @BeforeEach
    void initializeMirror() {
        Domain.initialize(new ReflectiveDomainMirrorFactory("io.domainlifecycles.staticanalysis.factory"));
    }

    @Test
    void testAForwardFlowReachesTheAggregateAFactoryMethodCreates() {
        var flow = analyzer().flowFrom(method(OrderService.class, "place"));

        var orderStep = step(flow, Order.class);
        assertThat(orderStep).isInstanceOf(Step.TypeStep.class);
        assertThat(orderStep.kind()).isEqualTo(StepKind.CREATES);
        assertThat(orderStep.from().map(Step::typeName)).contains(OrderFactory.class.getName());
    }

    @Test
    void testAForwardFlowReachesTheEntityAFactoryMethodOfItsAggregateCreates() {
        var flow = analyzer().flowFrom(method(Order.class, "addLine"));

        assertThat(step(flow, OrderLine.class).kind()).isEqualTo(StepKind.CREATES);
    }

    @Test
    void testAFactoryMethodCreatingItsOwnTypeLeadsNowhere() {
        var flow = analyzer().flowFrom(method(Order.class, "open"));

        assertThat(flow.steps()).noneMatch(step -> step.kind() == StepKind.CREATES);
    }

    @Test
    void testAFactoryMethodProvidingAReadModelLeadsToItAsProviderOnly() {
        var flow = analyzer().flowFrom(method(OrderFactory.class, "summarize"));

        assertThat(flow.steps()).filteredOn(step -> step.typeName().equals(OrderSummary.class.getName()))
            .extracting(Step::kind)
            .containsExactly(StepKind.PROVIDES_READ_MODEL);
    }

    @Test
    void testTheFactoryMethodsCreatingADomainTypePrecedeIt() {
        var flow = analyzer().flowTo(type(Order.class));

        assertThat(creatorSteps(flow)).containsExactly(OrderFactory.class.getName() + "#create");
    }

    @Test
    void testTheBackwardFlowContinuesWithTheCallersOfAFactoryMethod() {
        var flow = analyzer().flowTo(type(Order.class));

        var createStep = flow.steps().stream()
            .filter(step -> step.kind() == StepKind.CREATES)
            .findFirst().orElseThrow();
        var placeStep = step(flow, OrderService.class);
        assertThat(placeStep.kind()).isEqualTo(StepKind.CALL);
        assertThat(placeStep.from()).contains(createStep);
    }

    @Test
    void testTheFactoryMethodOfAnAggregateCreatingAnEntityPrecedesIt() {
        var flow = analyzer().flowTo(type(OrderLine.class));

        assertThat(creatorSteps(flow)).containsExactly(Order.class.getName() + "#addLine");
    }

    private static DomainCallFlowAnalyzer analyzer() {
        var calls = DomainCalls.builder()
            .add(method(OrderService.class, "place"), List.of(new DomainCalls.CallSite(
                method(OrderFactory.class, "create"), OrderService.class.getName(), 1)))
            .build();
        return new DomainCallFlowAnalyzer(Domain.getDomainMirror(), calls);
    }

    private static Step step(Flow flow, Class<?> type) {
        return flow.steps().stream()
            .filter(step -> step.typeName().equals(type.getName()))
            .filter(step -> step.kind() != StepKind.START)
            .findFirst().orElseThrow();
    }

    private static List<String> creatorSteps(Flow flow) {
        return flow.steps().stream()
            .filter(step -> step.kind() == StepKind.CREATES)
            .filter(Step.MethodStep.class::isInstance)
            .map(Step.MethodStep.class::cast)
            .map(step -> step.method().typeName() + "#" + step.method().mirror().getName())
            .toList();
    }

    private static DomainTypeMirror type(Class<?> type) {
        return Domain.getDomainMirror().getDomainTypeMirror(type.getName()).orElseThrow();
    }

    private static DomainMethod method(Class<?> ownerClass, String methodName) {
        var typeMirror = type(ownerClass);
        var method = typeMirror.getMethods().stream()
            .filter(m -> m.getName().equals(methodName))
            .findFirst()
            .orElseThrow();
        return new DomainMethod(typeMirror.getTypeName(), method);
    }
}
