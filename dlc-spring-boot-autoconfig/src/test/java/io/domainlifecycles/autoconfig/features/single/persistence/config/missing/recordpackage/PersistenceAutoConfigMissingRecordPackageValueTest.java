package io.domainlifecycles.autoconfig.features.single.persistence.config.missing.recordpackage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.SpringApplication;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles({"test"})
public class PersistenceAutoConfigMissingRecordPackageValueTest {

    @Test
    public void testThrowExceptionOnMissingBasePackageConfiguration() {
        assertThatThrownBy(() -> SpringApplication.run(TestApplicationPersistenceMissingRecordPackageValueAutoConfig.class))
            .isInstanceOf(BeanCreationException.class)
            .hasRootCauseMessage("Property 'jooqRecordPackage' is missing. Make sure you specified a property called 'dlc.persistence.jooqRecordPackage' or add a 'jooqRecordPackage' value on the @EnableDLC annotation.");
    }
}
