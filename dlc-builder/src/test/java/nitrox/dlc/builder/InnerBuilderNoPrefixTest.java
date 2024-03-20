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
 *  Copyright 2019-2023 the original author or authors.
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

package nitrox.dlc.builder;


import nitrox.dlc.builder.innerclass.InnerClassDefaultDomainBuilderConfiguration;
import nitrox.dlc.builder.innerclass.InnerClassDomainObjectBuilder;
import nitrox.dlc.mirror.api.Domain;
import nitrox.dlc.mirror.reflect.ReflectiveDomainMirrorFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class InnerBuilderNoPrefixTest {


    @BeforeAll
    static void beforeAll() {
        Domain.initialize(new ReflectiveDomainMirrorFactory("tests", "nitrox"));
    }

    private static DomainBuilderConfiguration config = new DomainBuilderConfiguration() {
        @Override
        public String builderMethodName() {
            return "builder";
        }

        @Override
        public String buildMethodName() {
            return "build";
        }

        @Override
        public String setterFromPropertyName(String propertyName) {
            return propertyName;
        }
    };

    @Test
    public void testBuild(){
        var testBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(testBuilder, config);
        var built = innerBuilder.build();
        Assertions.assertThat(built).isNotNull();
    }



    @Test
    public void testSetFieldValue(){
        var testBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(testBuilder, config);
        innerBuilder.setFieldValue("aaa", "first");
        TestValueObject built = (TestValueObject) innerBuilder.build();
        Assertions.assertThat(built.first()).isEqualTo("aaa");
    }

    @Test
    public void testGetFieldValue(){
        var testBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(testBuilder, config);
        var value = innerBuilder.getFieldValue("first");
        Assertions.assertThat(value).isEqualTo("a");
    }

    @Test
    public void testCanInstantiateTrue(){
        var testBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(testBuilder, config);
        Assertions.assertThat(innerBuilder.canInstantiateField("first")).isTrue();
    }

    @Test
    public void testCanInstantiateFalse(){
        var testBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(testBuilder, config);
        Assertions.assertThat(innerBuilder.canInstantiateField("myValue1")).isFalse();
    }

    @Test
    public void testGetBuilderInstance(){
        var testBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(testBuilder, config);
        Assertions.assertThat(innerBuilder.getBuilderInstance()).isEqualTo(testBuilder);
    }

    @Test
    public void testGetInstanceType(){
        var testBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(testBuilder, config);
        Assertions.assertThat(innerBuilder.instanceType()).isEqualTo(TestValueObject.class);
    }

    private TestValueObject.TestValueObjectBuilder preInitializedLombokBuilder(){
        return TestValueObject.builder()
            .first("a")
            .second(1L);
    }


}
