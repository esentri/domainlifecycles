/*
 *
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

package io.domainlifecycles.builder;


import io.domainlifecycles.builder.innerclass.InnerClassDefaultDomainBuilderConfiguration;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InnerBuilderDefaultConfigurationTest {

    @Test
    public void testGetBuildMethodName(){
        InnerClassDefaultDomainBuilderConfiguration conf = new InnerClassDefaultDomainBuilderConfiguration();;
        assertThat(conf.buildMethodName()).isEqualTo("build");
    }

    @Test
    public void testGetBuilderMethodName(){
        InnerClassDefaultDomainBuilderConfiguration conf = new InnerClassDefaultDomainBuilderConfiguration();;
        assertThat(conf.builderMethodName()).isEqualTo("builder");
    }

    @Test
    public void testSetterFromPropertyName() {
        InnerClassDefaultDomainBuilderConfiguration conf = new InnerClassDefaultDomainBuilderConfiguration();;
        assertThat(conf.setterFromPropertyName("property")).isEqualTo("setProperty");
    }
}
