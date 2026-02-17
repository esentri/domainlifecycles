package io.domainlifecycles.boot3.autoconfig.features.single.domain.missing.basepackage;

import io.domainlifecycles.boot3.autoconfig.annotation.EnableDlc;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Locale;

@SpringBootApplication
@EnableDlc(jooqSqlDialect = "H2")
public class TestApplicationDomainMissingBasePackageValueAutoConfig {

    /**
     * Setting the Locale to explicitly force the language in default validation error messages.
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        new SpringApplicationBuilder(TestApplicationDomainMissingBasePackageValueAutoConfig.class).run(args);
    }
}
