package io.domainlifecycles.autoconfig.features.single.persistence.config.missing.recordpackage;

import io.domainlifecycles.autoconfig.annotation.EnableDlc;
import io.domainlifecycles.autoconfig.features.single.persistence.PersistenceAutoConfigTestConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Import;

import java.util.Locale;

@SpringBootApplication()
@Import(PersistenceAutoConfigTestConfiguration.class)
@EnableDlc(
    dlcMirrorBasePackages = "io.domainlifecycles.autoconfig",
    jooqSqlDialect = "H2"
)
public class TestApplicationPersistenceMissingRecordPackageValueAutoConfig {

    /**
     * Setting the Locale to explicitly force the language in default validation error messages.
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        new SpringApplicationBuilder(TestApplicationPersistenceMissingRecordPackageValueAutoConfig.class).run(args);
    }
}
