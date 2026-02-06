package io.domainlifecycles.boot3.autoconfig.features.single.domain.missing.basepackage;

import io.domainlifecycles.mirror.api.Domain;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.SpringApplication;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;
import static org.springframework.test.annotation.DirtiesContext.MethodMode.BEFORE_METHOD;

@ActiveProfiles({"test"})
@DirtiesContext(classMode = BEFORE_CLASS)
@Execution(SAME_THREAD)
public class DomainAutoConfigMissingBasePackageValueTest {

    @BeforeAll
    public static void beforeAll() {
        Domain.unInitialize();
    }
    @Test
    @DirtiesContext(methodMode = BEFORE_METHOD)
    public void testThrowExceptionOnMissingBasePackageConfiguration() {
        assertThatThrownBy(() -> SpringApplication.run(TestApplicationDomainMissingBasePackageValueAutoConfig.class))
            .isInstanceOf(BeanCreationException.class)
            .hasRootCauseMessage("Property 'basePackages' is missing. Make sure you specified a property called 'dlc.domain.basePackages' or add a 'dlcDomainBasePackages' value on the @EnableDLC annotation.");
    }
}
