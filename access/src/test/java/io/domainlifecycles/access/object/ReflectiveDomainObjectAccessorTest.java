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

package io.domainlifecycles.access.object;

import io.domainlifecycles.domain.types.internal.DomainObject;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tests.shared.persistence.domain.optional.MySimpleValueObject;
import tests.shared.persistence.domain.valueobjects.SimpleVo;
import static org.assertj.core.api.Assertions.assertThat;

class ReflectiveDomainObjectAccessorTest {

    @BeforeAll
    static void beforeAll() {
        Domain.initialize(new ReflectiveDomainMirrorFactory("tests", "io.domainlifecycles"));
    }

    @Test
    void testPeekOk() {
        DynamicDomainObjectAccessor accessor = new ReflectiveDomainObjectAccessor(new SimpleVo("test"));

        Object value = accessor.peek("value");
        assertThat(value).isEqualTo("test");
    }

    @Test
    void testPokeOk() {
        SimpleVo vo = new SimpleVo("test");
        DynamicDomainObjectAccessor accessor = new ReflectiveDomainObjectAccessor(vo);

        accessor.poke("value", "overwrite");
        assertThat(vo.getValue()).isEqualTo("overwrite");
    }

    @Test
    void testGetAssignedOk() {
        SimpleVo vo = new SimpleVo("test");
        DynamicDomainObjectAccessor accessor = new ReflectiveDomainObjectAccessor(vo);

        DomainObject assigned = accessor.getAssigned();

        assertThat(assigned).isEqualTo(vo);
    }

    @Test
    void testAssignOk() {

        SimpleVo vo = new SimpleVo("test");
        DynamicDomainObjectAccessor accessor = new ReflectiveDomainObjectAccessor(vo);

        MySimpleValueObject simpleVo = new MySimpleValueObject("testNew");
        accessor.assign(simpleVo);

        assertThat(accessor.getAssigned()).isEqualTo(simpleVo);
    }
}
