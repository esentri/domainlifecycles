package io.domainlifecycles.access.object;

import io.domainlifecycles.access.classes.DefaultClassProvider;
import org.junit.jupiter.api.Test;
import tests.shared.persistence.domain.oneToManyIdentityEnum.MyId;
import tests.shared.persistence.domain.valueobjects.SimpleVo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DefaultSingleValuedValueObjectFactoryTest {

    @Test
    void testSingleValuedValueObjectFactorySuccess() {
        SingleValuedValueObjectFactory singleValuedValueObjectFactory = new DefaultSingleValuedValueObjectFactory(new DefaultClassProvider());
        SimpleVo vo = singleValuedValueObjectFactory.newInstance("blubb",
            SimpleVo.class.getName());

        assertThat(vo).isNotNull();
        assertThat(vo.getValue()).isEqualTo("blubb");
    }

    @Test
    void testSingleValuedValueObjectFactoryFailNotInstantiableNoValueObject() {
        SingleValuedValueObjectFactory singleValuedValueObjectFactory = new DefaultSingleValuedValueObjectFactory(new DefaultClassProvider());
        assertThatThrownBy(() -> singleValuedValueObjectFactory.newInstance(1l, MyId.class.getName()))
            .isInstanceOf(ClassCastException.class);
    }
}
