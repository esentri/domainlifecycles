package io.domainlifecycles.builder;


import io.domainlifecycles.builder.exception.DLCBuilderException;
import io.domainlifecycles.builder.helper.TestValueObject;
import io.domainlifecycles.builder.helper.TestValueOptionalObject;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainModelFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


public class InnerBuilderNoPrefixTest {

    private static final String DEFAULT_FIRST_VALUE = "a";
    private static final Long DEFAULT_SECOND_VALUE = 1L;

    @BeforeAll
    static void beforeAll() {
        Domain.initialize(new ReflectiveDomainModelFactory("tests", "io.domainlifecycles"));
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
    public void testBuild() {
        var testBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(testBuilder, config);
        var built = innerBuilder.build();
        assertThat(built).isNotNull();
    }

    @Test
    public void testSetFieldValueOk() {
        var testBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(testBuilder, config);
        innerBuilder.setFieldValue("aaa", "first");
        TestValueObject built = (TestValueObject) innerBuilder.build();
        assertThat(built.first()).isEqualTo("aaa");
    }

    @Test
    public void testSetFieldValueOkOptional() {
        var testBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(testBuilder, config);
        innerBuilder.setFieldValue(Optional.of("aaa"), "first");
        TestValueObject built = (TestValueObject) innerBuilder.build();
        assertThat(built.first()).isEqualTo("aaa");
    }

    @Test
    public void testSetFieldValueOkNull() {
        var testBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(testBuilder, config);
        innerBuilder.setFieldValue(null, "first");
        TestValueObject built = (TestValueObject) innerBuilder.build();
        assertThat(built.first()).isEqualTo(DEFAULT_FIRST_VALUE);
    }

    @Test
    public void testSetFieldValueFailMethodNotFound() {
        var testBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(testBuilder, config);
        assertThatThrownBy(() -> innerBuilder.setFieldValue("aaa", "third"))
            .isInstanceOf(DLCBuilderException.class)
            .hasMessageContaining("Was not able to set property");
    }

    @Test
    public void testSetFieldValueOptionalOk() {
        var testBuilder = preInitializedOptionalLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(testBuilder, config);
        innerBuilder.setFieldValue("aaaOptionalParam", "first");
        TestValueOptionalObject built = (TestValueOptionalObject) innerBuilder.build();
        assertThat(built.first().get()).isEqualTo("aaaOptionalParam");
    }

    @Test
    public void testSetFieldValueOkOptionalOkOptional() {
        var testBuilder = preInitializedOptionalLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(testBuilder, config);
        innerBuilder.setFieldValue(Optional.of("aaaOptionalValue"), "first");
        TestValueOptionalObject built = (TestValueOptionalObject) innerBuilder.build();
        assertThat(built.first().get()).isEqualTo("aaaOptionalValue");
    }

    @Test
    public void testGetFieldValue() {
        var testBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(testBuilder, config);
        var value = innerBuilder.getFieldValue("first");
        assertThat(value).isEqualTo(DEFAULT_FIRST_VALUE);
    }

    @Test
    public void testCanInstantiateTrue() {
        var testBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(testBuilder, config);
        assertThat(innerBuilder.canInstantiateField("first")).isTrue();
    }

    @Test
    public void testCanInstantiateFalse() {
        var testBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(testBuilder, config);
        assertThat(innerBuilder.canInstantiateField("myValue1")).isFalse();
    }

    @Test
    public void testGetBuilderInstance() {
        var testBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(testBuilder, config);
        assertThat(innerBuilder.getBuilderInstance()).isEqualTo(testBuilder);
    }

    @Test
    public void testGetInstanceType() {
        var testBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(testBuilder, config);
        assertThat(innerBuilder.instanceType()).isEqualTo(TestValueObject.class);
    }

    private TestValueObject.TestValueObjectBuilder preInitializedLombokBuilder() {
        return TestValueObject.builder()
            .first(DEFAULT_FIRST_VALUE)
            .second(DEFAULT_SECOND_VALUE);
    }

    private TestValueOptionalObject.TestValueOptionalObjectBuilder preInitializedOptionalLombokBuilder() {
        return TestValueOptionalObject.builder()
            .first(Optional.of(DEFAULT_FIRST_VALUE))
            .second(DEFAULT_SECOND_VALUE);
    }
}
