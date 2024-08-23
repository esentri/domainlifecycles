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


import io.domainlifecycles.builder.exception.DLCBuilderException;
import io.domainlifecycles.builder.helper.TestValueObject;
import io.domainlifecycles.builder.helper.TestValueOptionalObject;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class InnerBuilderNoPrefixTest {

    private static final String DEFAULT_FIRST_VALUE = "a";
    private static final Long DEFAULT_SECOND_VALUE = 1L;

    @BeforeAll
    static void beforeAll() {
        Domain.initialize(new ReflectiveDomainMirrorFactory("tests", "io.domainlifecycles"));
    }

    private static final DomainBuilderConfiguration config = new DomainBuilderConfiguration() {
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
        assertThat(built).isNotNull();
    }

    @Test
    public void testSetFieldValueOk(){
        var testBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(testBuilder, config);
        innerBuilder.setFieldValue("aaa", "first");
        TestValueObject built = (TestValueObject) innerBuilder.build();
        assertThat(built.first()).isEqualTo("aaa");
    }

    @Test
    public void testSetFieldValueOkOptional(){
        var testBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(testBuilder, config);
        innerBuilder.setFieldValue(Optional.of("aaa"), "first");
        TestValueObject built = (TestValueObject) innerBuilder.build();
        assertThat(built.first()).isEqualTo("aaa");
    }

    @Test
    public void testSetFieldValueOkNull(){
        var testBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(testBuilder, config);
        innerBuilder.setFieldValue(null, "first");
        TestValueObject built = (TestValueObject) innerBuilder.build();
        assertThat(built.first()).isEqualTo(DEFAULT_FIRST_VALUE);
    }

    @Test
    public void testSetFieldValueFailMethodNotFound(){
        var testBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(testBuilder, config);
        DLCBuilderException exception = assertThrows(DLCBuilderException.class, () -> innerBuilder.setFieldValue("aaa", "third"));
        assertThat(exception).hasMessageContaining("Was not able to set property");
    }

    @Test
    @Disabled
    public void testSetFieldValueFailMultipleMethods(){
        var testBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(testBuilder, config);
        DLCBuilderException exception = assertThrows(DLCBuilderException.class, () -> innerBuilder.setFieldValue("aaa", "fourth"));
        assertThat(exception).hasMessageContaining("Multiple setters found in Lombok builder");
    }

    @Test
    public void testSetFieldValueOptionalOk(){
        var testBuilder = preInitializedOptionalLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(testBuilder, config);
        innerBuilder.setFieldValue("aaaOptionalParam", "first");
        TestValueOptionalObject built = (TestValueOptionalObject) innerBuilder.build();
        assertThat(built.first().get()).isEqualTo("aaaOptionalParam");
    }

    @Test
    public void testSetFieldValueOkOptionalOkOptional(){
        var testBuilder = preInitializedOptionalLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(testBuilder, config);
        innerBuilder.setFieldValue(Optional.of("aaaOptionalValue"), "first");
        TestValueOptionalObject built = (TestValueOptionalObject) innerBuilder.build();
        assertThat(built.first().get()).isEqualTo("aaaOptionalValue");
    }

    @Test
    public void testGetFieldValue(){
        var testBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(testBuilder, config);
        var value = innerBuilder.getFieldValue("first");
        assertThat(value).isEqualTo(DEFAULT_FIRST_VALUE);
    }

    @Test
    public void testCanInstantiateTrue(){
        var testBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(testBuilder, config);
        assertThat(innerBuilder.canInstantiateField("first")).isTrue();
    }

    @Test
    public void testCanInstantiateFalse(){
        var testBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(testBuilder, config);
        assertThat(innerBuilder.canInstantiateField("myValue1")).isFalse();
    }

    @Test
    public void testGetBuilderInstance(){
        var testBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(testBuilder, config);
        assertThat(innerBuilder.getBuilderInstance()).isEqualTo(testBuilder);
    }

    @Test
    public void testGetInstanceType(){
        var testBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(testBuilder, config);
        assertThat(innerBuilder.instanceType()).isEqualTo(TestValueObject.class);
    }

    private TestValueObject.TestValueObjectBuilder preInitializedLombokBuilder(){
        return TestValueObject.builder()
            .first(DEFAULT_FIRST_VALUE)
            .second(DEFAULT_SECOND_VALUE);
    }

    private TestValueOptionalObject.TestValueOptionalObjectBuilder preInitializedOptionalLombokBuilder(){
        return TestValueOptionalObject.builder()
            .first(Optional.of(DEFAULT_FIRST_VALUE))
            .second(DEFAULT_SECOND_VALUE);
    }
}
