package io.domainlifecycles.autoconfig.features.single.persistence.config.missing.recordpackage;

import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
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
public class PersistenceAutoConfigMissingRecordPackageValueTest {

    @Autowired
    JooqDomainPersistenceProvider jooqDomainPersistenceProvider;

    @Test
    @DirtiesContext(methodMode = BEFORE_METHOD)
    public void testThrowExceptionOnMissingRecordPackageConfiguration() {
        assertThatThrownBy(() ->
                SpringApplication
                    .from(TestApplicationPersistenceMissingRecordPackageValueAutoConfig::main)
                        .run("--debug=true")
            )
            .isInstanceOf(BeanCreationException.class)
            .hasRootCauseMessage("Property 'jooqRecordPackage' is missing. Make sure you specified a property called 'dlc.persistence.jooqRecordPackage' or add a 'jooqRecordPackage' value on the @EnableDLC annotation.");
    }
}
