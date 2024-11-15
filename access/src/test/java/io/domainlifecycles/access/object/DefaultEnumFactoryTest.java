package io.domainlifecycles.access.object;

import io.domainlifecycles.access.classes.DefaultClassProvider;
import org.junit.jupiter.api.Test;
import tests.shared.persistence.domain.valueobjectsNested.MyEnum;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultEnumFactoryTest {

    @Test
    void testEnumFactorySuccess() {
        EnumFactory enumFactory = new DefaultEnumFactory(new DefaultClassProvider());

        MyEnum myEnum = enumFactory.newInstance("A", "tests.shared.persistence.domain.valueobjectsNested.MyEnum");

        assertThat(myEnum).isNotNull();
        assertThat(myEnum.equals(MyEnum.A)).isTrue();
    }
}
