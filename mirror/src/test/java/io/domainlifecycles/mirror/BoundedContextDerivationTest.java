package io.domainlifecycles.mirror;

import io.domainlifecycles.mirror.exception.MirrorException;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Covers deriving Bounded Context boundaries from {@code @BoundedContext}-annotated packages instead of
 * requiring {@code setBoundedContextPackages(...)} to be called manually.
 */
public class BoundedContextDerivationTest {

    @Test
    void derivesOneBoundedContextPerAnnotatedPackageIncludingItsOptionalName() {
        var factory = new ReflectiveDomainMirrorFactory("tests.mirror.boundedcontext");
        var domainMirror = factory.initializeDomainMirror();

        var boundedContexts = domainMirror.getAllBoundedContextMirrors();

        assertThat(boundedContexts).hasSize(2);
        var orders = boundedContexts.stream()
            .filter(bc -> bc.getPackageName().equals("tests.mirror.boundedcontext.orders"))
            .findFirst()
            .orElseThrow();
        assertThat(orders.getName()).contains("Order Management");
        assertThat(orders.getDomainServices())
            .extracting(dt -> dt.getTypeName())
            .containsExactly("tests.mirror.boundedcontext.orders.OrderService");

        var shipping = boundedContexts.stream()
            .filter(bc -> bc.getPackageName().equals("tests.mirror.boundedcontext.shipping"))
            .findFirst()
            .orElseThrow();
        assertThat(shipping.getName())
            .as("a bare @BoundedContext without a value must not fabricate a name")
            .isEmpty();
        assertThat(shipping.getDomainServices())
            .extracting(dt -> dt.getTypeName())
            .containsExactly("tests.mirror.boundedcontext.shipping.ShippingService");
    }

    @Test
    void explicitlyConfiguredBoundedContextPackagesStillTakePrecedenceOverDerivedOnes() {
        var factory = new ReflectiveDomainMirrorFactory("tests.mirror.boundedcontext");
        factory.setBoundedContextPackages(new String[]{"tests.mirror.boundedcontext"});
        var domainMirror = factory.initializeDomainMirror();

        var boundedContexts = domainMirror.getAllBoundedContextMirrors();

        assertThat(boundedContexts).hasSize(1);
        assertThat(boundedContexts.get(0).getPackageName()).isEqualTo("tests.mirror.boundedcontext");
        assertThat(boundedContexts.get(0).getName()).isEmpty();
    }

    @Test
    void withoutAnyAnnotationTheWholeModelStaysOneBoundedContextAsBefore() {
        var factory = new ReflectiveDomainMirrorFactory("tests.mirror.nondomain");
        var domainMirror = factory.initializeDomainMirror();

        var boundedContexts = domainMirror.getAllBoundedContextMirrors();

        assertThat(boundedContexts).hasSize(1);
        assertThat(boundedContexts.get(0).getPackageName()).isEqualTo("tests.mirror.nondomain");
    }

    @Test
    void nestedBoundedContextPackagesAreRejected() {
        var factory = new ReflectiveDomainMirrorFactory("boundedcontextoverlapfixture");

        assertThatThrownBy(factory::initializeDomainMirror)
            .isInstanceOf(MirrorException.class)
            .hasMessageContaining("boundedcontextoverlapfixture.outer.inner")
            .hasMessageContaining("boundedcontextoverlapfixture.outer")
            .hasMessageContaining("must not overlap");
    }
}
