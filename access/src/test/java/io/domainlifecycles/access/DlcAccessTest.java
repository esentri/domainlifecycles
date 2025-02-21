package io.domainlifecycles.access;

import io.domainlifecycles.access.classes.DefaultClassProvider;
import io.domainlifecycles.access.object.DefaultDomainObjectAccessFactory;
import io.domainlifecycles.access.object.DefaultEnumFactory;
import io.domainlifecycles.access.object.DefaultIdentityFactory;
import io.domainlifecycles.access.object.DynamicDomainObjectAccessor;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainModelFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tests.shared.persistence.domain.inheritance.Car;
import tests.shared.persistence.domain.inheritance.Car.Brand;
import tests.shared.persistence.domain.inheritance.VehicleId;
import tests.shared.persistence.domain.oneToManyIdentityEnum.MyEnum;

import static org.assertj.core.api.Assertions.assertThat;

class DlcAccessTest {

    @BeforeAll
    static void beforeAll() {
        Domain.initialize(new ReflectiveDomainModelFactory("tests", "io.domainlifecycles"));
    }

    @Test
    void testCustomizeOk() {

        DefaultClassProvider provider = new DefaultClassProvider();
        DlcAccess.customize(provider, new DefaultEnumFactory(provider),
            new DefaultIdentityFactory(provider), new DefaultDomainObjectAccessFactory());
    }

    @Test
    void testGetClassOk() {

        Class<?> aClass = DlcAccess.getClassForName("tests.shared.persistence.domain.inheritance.Car");

        assertThat(aClass).isNotNull();
        assertThat(aClass).isEqualTo(Car.class);
    }

    @Test
    void testNewEnumInstanceOk() {

        MyEnum anEnum = DlcAccess.newEnumInstance("ONE",
            "tests.shared.persistence.domain.oneToManyIdentityEnum.MyEnum");

        assertThat(anEnum).isNotNull();
        assertThat(anEnum.name()).isEqualTo("ONE");
    }

    @Test
    void testNewIdentityOk() {

        Identity<Long> anIdentity = DlcAccess.newIdentityInstance(1L,
            "tests.shared.persistence.domain.inheritance.VehicleId");

        assertThat(anIdentity).isNotNull();
        assertThat(anIdentity.value()).isEqualTo(1L);
    }

    @Test
    void testNewAccessorOk() {

        Car car = Car.builder()
            .setId(new VehicleId(1L))
            .setBrand(Brand.AUDI)
            .setLengthCm(390)
            .build();

        DynamicDomainObjectAccessor accessor = DlcAccess.accessorFor(car);

        assertThat(accessor.getAssigned()).isEqualTo(car);
    }
}
