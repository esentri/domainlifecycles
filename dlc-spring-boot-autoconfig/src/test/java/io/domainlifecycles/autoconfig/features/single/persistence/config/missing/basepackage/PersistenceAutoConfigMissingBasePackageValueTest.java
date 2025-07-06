package io.domainlifecycles.autoconfig.features.single.persistence.config.missing.basepackage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles({"test"})
public class PersistenceAutoConfigMissingBasePackageValueTest {

    @Test
    public void testThrowExceptionOnMissingBasePackageConfiguration() {
        assertThatThrownBy(() -> SpringApplication.run(TestApplicationPersistenceMissingBasePackageValueAutoConfig.class))
            .isInstanceOf(BeanCreationException.class)
            .hasRootCauseMessage("Property 'basePackages' is missing. Make sure you specified a property called 'dlc.domain.basePackages' or add a 'dlcDomainBasePackages' value on the @EnableDLC annotation.");
    }
}
