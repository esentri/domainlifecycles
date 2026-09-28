package io.domainlifecycles.staticanalysis;

import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.staticanalysis.readmodelprovider.ComputedView;
import io.domainlifecycles.staticanalysis.readmodelprovider.ProvidedView;
import io.domainlifecycles.staticanalysis.readmodelprovider.ProvidedViewQueryHandler;
import io.domainlifecycles.staticanalysis.readmodelprovider.ReportService;
import io.domainlifecycles.staticanalysis.readmodelprovider.ViewClient;
import io.domainlifecycles.staticanalysis.readmodelprovider.ViewDriver;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A ReadModel provided by no QueryHandler is provided by the methods of service kinds and non-domain classes
 * returning it - directly, as {@code Optional} or as collection: they precede it in a backward flow and lead to it in
 * a forward flow, like a QueryHandler.
 */
public class ReadModelProviderFlowTest {

    @BeforeEach
    void initializeMirror() {
        Domain.initialize(new ReflectiveDomainMirrorFactory("io.domainlifecycles.staticanalysis.readmodelprovider"));
    }

    @Test
    void testTheMethodsReturningAReadModelWithoutQueryHandlerPrecedeIt() {
        var flow = analyzer().flowTo(type(ComputedView.class));

        assertThat(providerSteps(flow)).containsExactlyInAnyOrder(
            ViewDriver.class.getName() + "#compute",
            ViewDriver.class.getName() + "#computeOptional",
            ViewDriver.class.getName() + "#computeAll",
            ViewClient.class.getName() + "#fetch");
    }

    @Test
    void testTheBackwardFlowContinuesWithTheCallersOfAProvidingMethod() {
        var flow = analyzer().flowTo(type(ComputedView.class));

        var computeStep = flow.steps().stream()
            .filter(step -> step instanceof Step.MethodStep methodStep
                && methodStep.method().mirror().getName().equals("compute"))
            .findFirst().orElseThrow();
        var reportStep = flow.steps().stream()
            .filter(step -> step.typeName().equals(ReportService.class.getName()))
            .findFirst().orElseThrow();
        assertThat(reportStep.kind()).isEqualTo(StepKind.CALL);
        assertThat(reportStep.from()).contains(computeStep);
    }

    @Test
    void testAReadModelWithQueryHandlerIsPrecededByItOnly() {
        var flow = analyzer().flowTo(type(ProvidedView.class));

        assertThat(providerSteps(flow)).isEmpty();
        assertThat(flow.reachedTypeNames())
            .contains(ProvidedViewQueryHandler.class.getName())
            .doesNotContain(ViewDriver.class.getName());
    }

    @Test
    void testAForwardFlowReachesTheReadModelThroughAProvidingMethod() {
        var flow = analyzer().flowFrom(method(ReportService.class, "report"));

        var readModelStep = flow.steps().stream()
            .filter(step -> step.typeName().equals(ComputedView.class.getName()))
            .findFirst().orElseThrow();
        assertThat(readModelStep.kind()).isEqualTo(StepKind.PROVIDES_READ_MODEL);
        assertThat(readModelStep.from().map(Step::typeName)).contains(ViewDriver.class.getName());
    }

    @Test
    void testAMethodReturningAReadModelWithQueryHandlerDoesNotLeadToIt() {
        var flow = analyzer().flowFrom(method(ViewDriver.class, "provided"));

        assertThat(flow.reachedTypeNames()).doesNotContain(ProvidedView.class.getName());
    }

    private static DomainCallFlowAnalyzer analyzer() {
        var calls = DomainCalls.builder()
            .add(method(ReportService.class, "report"), List.of(new DomainCalls.CallSite(
                method(ViewDriver.class, "compute"), ReportService.class.getName(), 1)))
            .build();
        return new DomainCallFlowAnalyzer(Domain.getDomainMirror(), calls);
    }

    private static List<String> providerSteps(Flow flow) {
        return flow.steps().stream()
            .filter(step -> step.kind() == StepKind.PROVIDES_READ_MODEL)
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
