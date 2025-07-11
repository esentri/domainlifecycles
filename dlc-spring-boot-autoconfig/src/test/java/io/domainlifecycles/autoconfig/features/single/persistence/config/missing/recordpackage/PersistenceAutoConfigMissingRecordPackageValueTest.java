package io.domainlifecycles.autoconfig.features.single.persistence.config.missing.recordpackage;

import io.domainlifecycles.autoconfig.features.single.persistence.PersistenceAutoConfigTestConfiguration;
import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilderProvider;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.provider.EntityIdentityProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles({"test"})
public class PersistenceAutoConfigMissingRecordPackageValueTest {

    @Autowired
    JooqDomainPersistenceProvider jooqDomainPersistenceProvider;

    @Test
    public void testThrowExceptionOnMissingRecordPackageConfiguration() {
        assertThatThrownBy(() ->
            SpringApplication
                .from(TestApplicationPersistenceMissingRecordPackageValueAutoConfig::main)
                .run())
            .isInstanceOf(BeanCreationException.class)
            .hasRootCauseMessage("Property 'jooqRecordPackage' is missing. Make sure you specified a property called 'dlc.persistence.jooqRecordPackage' or add a 'jooqRecordPackage' value on the @EnableDLC annotation.");
    }
}
