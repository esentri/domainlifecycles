package tests.mirror.jmoleculesboundedcontext;

import io.domainlifecycles.mirrorjmolecules.reflect.ExtendedJMoleculesDomainMirrorFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Covers deriving Bounded Context boundaries from jMolecules' own {@code @BoundedContext} package
 * annotation, in addition to DLC's own {@code io.domainlifecycles.domain.types.BoundedContext}.
 */
public class JMoleculesBoundedContextDerivationTest {

    @Test
    void derivesBoundedContextFromJMoleculesOwnAnnotation() {
        var factory = new ExtendedJMoleculesDomainMirrorFactory("tests.mirror.jmoleculesboundedcontext");
        var domainMirror = factory.initializeDomainMirror();

        var boundedContexts = domainMirror.getAllBoundedContextMirrors();

        assertThat(boundedContexts).hasSize(1);
        var billing = boundedContexts.get(0);
        assertThat(billing.getPackageName()).isEqualTo("tests.mirror.jmoleculesboundedcontext");
        assertThat(billing.getName()).contains("Billing");
        assertThat(billing.getDomainServices())
            .extracting(dt -> dt.getTypeName())
            .containsExactly("tests.mirror.jmoleculesboundedcontext.BillingService");
    }
}
