package io.domainlifecycles.autoconfig.features.single.domain.missing.basepackage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.SpringApplication;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles({"test"})
public class DomainAutoConfigMissingBasePackageValueTest {

    @Test
    @DirtiesContext
    public void testThrowExceptionOnMissingBasePackageConfiguration() {
        assertThatThrownBy(() -> SpringApplication.run(TestApplicationDomainMissingBasePackageValueAutoConfig.class))
            .isInstanceOf(BeanCreationException.class)
            .hasRootCauseMessage("Property 'basePackages' is missing. Make sure you specified a property called 'dlc.domain.basePackages' or add a 'dlcDomainBasePackages' value on the @EnableDLC annotation.");
    }
}
