package io.domainlifecycles.builder;


import io.domainlifecycles.builder.innerclass.InnerClassDefaultDomainBuilderConfiguration;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InnerBuilderDefaultConfigurationTest {

    @Test
    public void testGetBuildMethodName() {
        InnerClassDefaultDomainBuilderConfiguration conf = new InnerClassDefaultDomainBuilderConfiguration();
        ;
        assertThat(conf.buildMethodName()).isEqualTo("build");
    }

    @Test
    public void testGetBuilderMethodName() {
        InnerClassDefaultDomainBuilderConfiguration conf = new InnerClassDefaultDomainBuilderConfiguration();
        ;
        assertThat(conf.builderMethodName()).isEqualTo("builder");
    }

    @Test
    public void testSetterFromPropertyName() {
        InnerClassDefaultDomainBuilderConfiguration conf = new InnerClassDefaultDomainBuilderConfiguration();
        ;
        assertThat(conf.setterFromPropertyName("property")).isEqualTo("setProperty");
    }
}
