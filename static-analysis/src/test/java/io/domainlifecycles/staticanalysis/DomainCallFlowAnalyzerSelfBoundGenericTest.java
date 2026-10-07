package io.domainlifecycles.staticanalysis;

import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.DomainCommandMirror;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.mirror.resolver.TypeMetaResolver;
import io.domainlifecycles.staticanalysis.fixture.TestCommand;
import io.domainlifecycles.staticanalysis.fixture.TestCommandBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * Regression test for a bug where a domain model containing a Lombok {@code @SuperBuilder}-style
 * self-referential generic builder (mirrored as a non-domain class since the non-domain scan started
 * mirroring every unmarked class) made {@link DomainCallFlowAnalyzer}'s constructor throw
 * {@code DomainCommandMirror not found for 'java.lang.Object'} while indexing commands/events across
 * the whole domain model - breaking every flow analysis on the model, not just one reaching the
 * builder itself.
 */
public class DomainCallFlowAnalyzerSelfBoundGenericTest {

    @BeforeEach
    void initializeMirror() {
        var factory = new ReflectiveDomainMirrorFactory("io.domainlifecycles.staticanalysis.fixture");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        Domain.initialize(factory);
    }

    @Test
    void constructingTheAnalyzerDoesNotThrowForASelfBoundGenericBuilder() {
        assertThatCode(() -> new DomainCallFlowAnalyzer(Domain.getDomainMirror(), DomainCalls.builder().build()))
            .doesNotThrowAnyException();
    }

    @Test
    void theBuilderMethodsProcessedCommandIsCorrectlyResolvedNotSkipped() {
        var analyzer = new DomainCallFlowAnalyzer(Domain.getDomainMirror(), DomainCalls.builder().build());
        var command = (DomainCommandMirror) Domain.getDomainMirror()
            .getDomainTypeMirror(TestCommand.class.getName())
            .orElseThrow();

        var flow = analyzer.flowFrom(command);

        // reached via the COMMAND_PROCESS index built in the analyzer's constructor - built from
        // TestCommandBuilder.fillValuesFrom's own getProcessedCommands(), the exact call that used
        // to throw before the fix, so a correctly resolved (not silently skipped) command reaches it
        assertThat(flow.reachedTypeNames()).contains(TestCommandBuilder.class.getName());
    }
}
