package io.domainlifecycles.builder;


import io.domainlifecycles.builder.exception.DLCBuilderException;
import helper.TestValueObject;
import helper.TestValueObjectNoBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilderProvider;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class InnerBuilderProviderTest {

    @BeforeAll
    static void beforeAll() {
        Domain.initialize(new ReflectiveDomainMirrorFactory("tests", "helper"));
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
    void testProviderOk() {
        var provider = new InnerClassDomainObjectBuilderProvider(config);
        var builder = provider.provide(TestValueObject.class.getName());
        assertThat(builder).isNotNull();
    }

    @Test
    void testProviderErrorNoBuilder() {
        var provider = new InnerClassDomainObjectBuilderProvider();
        assertThatThrownBy(() -> provider.provide(TestValueObjectNoBuilder.class.getName()))
            .isInstanceOf(DLCBuilderException.class)
            .hasMessageContaining("Couldn't provide Builder instance for class: 'helper.TestValueObjectNoBuilder'.");
    }
}
