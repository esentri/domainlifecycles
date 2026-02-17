package io.domainlifecycles.boot3.autoconfig.features.single.persistence.config.annotation;

import io.domainlifecycles.boot3.autoconfig.annotation.EnableDlc;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcBuilderAutoConfiguration;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcJackson2AutoConfiguration;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcNoTxInMemoryDomainEventsAutoConfiguration;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcServiceKindAutoConfiguration;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcSpringBusDomainEventsAutoConfiguration;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcSpringOpenApiAutoConfiguration;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcSpringWebAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Locale;

@SpringBootApplication
@EnableDlc(
    exclude = {
        DlcBuilderAutoConfiguration.class,
        DlcJackson2AutoConfiguration.class,
        DlcSpringOpenApiAutoConfiguration.class,
        DlcSpringWebAutoConfiguration.class,
        DlcSpringBusDomainEventsAutoConfiguration.class,
        DlcNoTxInMemoryDomainEventsAutoConfiguration.class,
        DlcServiceKindAutoConfiguration.class
    },
    dlcMirrorBasePackages = "io.domainlifecycles.autoconfig",
    jooqRecordPackage = "io.domainlifecycles.test.autoconfig",
    jooqSqlDialect = "H2"
)
public class TestApplicationPersistenceAnnotationValuesAutoConfig {

    /**
     * Setting the Locale to explicitly force the language in default validation error messages.
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        new SpringApplicationBuilder(TestApplicationPersistenceAnnotationValuesAutoConfig.class).run(args);
    }
}
