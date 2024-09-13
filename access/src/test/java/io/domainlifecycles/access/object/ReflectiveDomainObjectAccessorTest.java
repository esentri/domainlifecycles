package io.domainlifecycles.access.object;

import io.domainlifecycles.domain.types.internal.DomainObject;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tests.shared.persistence.domain.optional.MySimpleValueObject;
import tests.shared.persistence.domain.valueobjects.SimpleVo;

class ReflectiveDomainObjectAccessorTest {

    @BeforeAll
    static void beforeAll() {
        Domain.initialize(new ReflectiveDomainMirrorFactory("tests", "io.domainlifecycles"));
    }

    @Test
    void testPeekOk() {

        DynamicDomainObjectAccessor accessor = new ReflectiveDomainObjectAccessor(new SimpleVo("test"));

        Object value = accessor.peek("value");
        Assertions.assertThat(value).isEqualTo("test");
    }

    @Test
    void testPokeOk() {

        SimpleVo vo = new SimpleVo("test");
        DynamicDomainObjectAccessor accessor = new ReflectiveDomainObjectAccessor(vo);

        accessor.poke("value", "overwrite");
        Assertions.assertThat(vo.getValue()).isEqualTo("overwrite");
    }

    @Test
    void testGetAssignedOk() {

        SimpleVo vo = new SimpleVo("test");
        DynamicDomainObjectAccessor accessor = new ReflectiveDomainObjectAccessor(vo);

        DomainObject assigned = accessor.getAssigned();

        Assertions.assertThat(assigned).isEqualTo(vo);
    }

    @Test
    void testAssignOk() {

        SimpleVo vo = new SimpleVo("test");
        DynamicDomainObjectAccessor accessor = new ReflectiveDomainObjectAccessor(vo);

        MySimpleValueObject simpleVo = new MySimpleValueObject("testNew");
        accessor.assign(simpleVo);

        Assertions.assertThat(accessor.getAssigned()).isEqualTo(simpleVo);
    }
}
