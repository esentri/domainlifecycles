/*
 *     ___
 *     │   ╲                 _
 *     │    ╲ ___ _ __  __ _(_)_ _
 *     |     ╲ _ ╲ '  ╲╱ _` │ │ ' ╲
 *     |_____╱___╱_│_│_╲__,_│_│_||_|
 *     │ │  (_)╱ _│___ __ _  _ __│ |___ ___
 *     │ │__│ │  _╱ -_) _│ ││ ╱ _│ ╱ -_|_-<
 *     │____│_│_│ ╲___╲__│╲_, ╲__│_╲___╱__╱
 *                      |__╱
 *
 *  Copyright 2019-2024 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.domainlifecycles.access;

import io.domainlifecycles.access.classes.DefaultClassProvider;
import io.domainlifecycles.access.object.DefaultDomainObjectAccessFactory;
import io.domainlifecycles.access.object.DefaultEnumFactory;
import io.domainlifecycles.access.object.DefaultIdentityFactory;
import io.domainlifecycles.access.object.DynamicDomainObjectAccessor;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
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
        Domain.initialize(new ReflectiveDomainMirrorFactory("tests", "io.domainlifecycles"));
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
