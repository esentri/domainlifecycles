package io.domainlifecycles.access.object;

import io.domainlifecycles.access.classes.ClassProvider;
import io.domainlifecycles.access.classes.DefaultClassProvider;
import org.junit.jupiter.api.Test;
import tests.shared.persistence.domain.simple.TestRootSimple;
import tests.shared.persistence.domain.simple.TestRootSimpleId;
import tests.shared.persistence.domain.valueobjectsNested.MyEnum;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DefaultEnumFactoryTest {

    @Test
    void testEnumFactorySuccess() {
        EnumFactory enumFactory = new DefaultEnumFactory(new DefaultClassProvider());

        MyEnum myEnum = enumFactory.newInstance("A", "tests.shared.persistence.domain.valueobjectsNested.MyEnum");

        assertThat(myEnum).isNotNull();
        assertThat(myEnum.equals(MyEnum.A)).isTrue();
    }
}
