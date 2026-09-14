package io.domainlifecycles.staticanalysis;

import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.mirror.resolver.TypeMetaResolver;
import io.domainlifecycles.staticanalysis.fixture.TestAggregate;
import io.domainlifecycles.staticanalysis.fixture.TestQueryHandlerImpl;
import io.domainlifecycles.staticanalysis.fixture.TestReadModel;
import io.domainlifecycles.staticanalysis.fixture.TestRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Regression tests for a bug where restricting a diagram to a flow reaching a QueryHandler or
 * Repository method silently dropped the ReadModel it provides / the Aggregate it manages.
 * <p>
 * That relationship is a structural fact of the domain ({@link
 * io.domainlifecycles.mirror.api.RepositoryMirror#getManagedAggregate()} / {@link
 * io.domainlifecycles.mirror.api.QueryHandlerMirror#getProvidedReadModel()}), not something the
 * flow can discover from the reached method's body - which is exactly why it needs its own edge in
 * {@link DomainCallFlowAnalyzer}, the same way a published domain event needs {@link
 * StepKind#EVENT_PUBLISH} instead of being found by following calls.
 */
public class DomainCallFlowAnalyzerTest {

    @BeforeEach
    void initializeMirror() {
        var factory = new ReflectiveDomainMirrorFactory("io.domainlifecycles.staticanalysis.fixture");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        Domain.initialize(factory);
    }

    @Test
    void testReachingARepositoryMethodAlwaysIncludesItsManagedAggregate() {
        var analyzer = new DomainCallFlowAnalyzer(Domain.getDomainMirror(), DomainCalls.builder().build());

        var flow = analyzer.flowFrom(domainMethod(TestRepositoryImpl.class, "findById"));

        assertThat(flow.reachedTypeNames()).contains(TestAggregate.class.getName());
    }

    @Test
    void testReachingAnyMethodOfARepositoryIncludesItsAggregateNotJustTheOneReturningIt() {
        // deleteById's body never touches TestAggregate at all - the relationship must not depend
        // on which method happened to be reached, or on that method's body naming the type.
        var analyzer = new DomainCallFlowAnalyzer(Domain.getDomainMirror(), DomainCalls.builder().build());

        var flow = analyzer.flowFrom(domainMethod(TestRepositoryImpl.class, "deleteById"));

        assertThat(flow.reachedTypeNames()).contains(TestAggregate.class.getName());
    }

    @Test
    void testReachingAQueryHandlerMethodAlwaysIncludesItsProvidedReadModel() {
        var analyzer = new DomainCallFlowAnalyzer(Domain.getDomainMirror(), DomainCalls.builder().build());

        var flow = analyzer.flowFrom(domainMethod(TestQueryHandlerImpl.class, "query"));

        assertThat(flow.reachedTypeNames()).contains(TestReadModel.class.getName());
    }

    @Test
    void testTheManagedAggregateStepIsReportedWithItsOwnKindAndIsNotExpandedFurther() {
        var analyzer = new DomainCallFlowAnalyzer(Domain.getDomainMirror(), DomainCalls.builder().build());

        var flow = analyzer.flowFrom(domainMethod(TestRepositoryImpl.class, "findById"));

        var aggregateStep = flow.steps().stream()
            .filter(step -> step.typeName().equals(TestAggregate.class.getName()))
            .findFirst()
            .orElseThrow();
        assertThat(aggregateStep.kind()).isEqualTo(StepKind.MANAGES_AGGREGATE);

        var successorsOfAggregateStep = flow.steps().stream()
            .filter(step -> step.from().filter(from -> from == aggregateStep).isPresent())
            .toList();
        assertThat(successorsOfAggregateStep).isEmpty();
    }

    @Test
    void testTheProvidedReadModelStepIsReportedWithItsOwnKind() {
        var analyzer = new DomainCallFlowAnalyzer(Domain.getDomainMirror(), DomainCalls.builder().build());

        var flow = analyzer.flowFrom(domainMethod(TestQueryHandlerImpl.class, "query"));

        var readModelStep = flow.steps().stream()
            .filter(step -> step.typeName().equals(TestReadModel.class.getName()))
            .findFirst()
            .orElseThrow();
        assertThat(readModelStep.kind()).isEqualTo(StepKind.PROVIDES_READ_MODEL);
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
