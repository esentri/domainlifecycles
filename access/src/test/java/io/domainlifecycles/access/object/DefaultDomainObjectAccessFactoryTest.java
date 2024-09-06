package io.domainlifecycles.access.object;

import org.junit.jupiter.api.Test;
import tests.shared.persistence.domain.simple.TestRootSimple;
import tests.shared.persistence.domain.simple.TestRootSimpleId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DefaultDomainObjectAccessFactoryTest {

    @Test
    void testObjectFactory() {
        DomainObjectAccessFactory domainObjectAccessFactory = new DefaultDomainObjectAccessFactory();
        TestRootSimple testRootSimple = new TestRootSimple(new TestRootSimpleId(1L), 1L, "simpleTest");
        DynamicDomainObjectAccessor accessor = domainObjectAccessFactory.accessorFor(testRootSimple);
        assertThat(accessor).isNotNull();
    }
}
