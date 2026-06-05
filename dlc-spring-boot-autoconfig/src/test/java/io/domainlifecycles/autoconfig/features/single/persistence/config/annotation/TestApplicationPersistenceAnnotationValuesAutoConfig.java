package io.domainlifecycles.autoconfig.features.single.persistence.config.annotation;

import io.domainlifecycles.autoconfig.annotation.EnableDlc;
import io.domainlifecycles.autoconfig.configurations.DlcBuilderAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcJacksonAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcNoTxInMemoryDomainEventsAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcSpringBusDomainEventsAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcSpringOpenApiAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcSpringWebAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcServiceKindAutoConfiguration;
import java.util.Locale;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
@EnableDlc(
    exclude = {
        DlcBuilderAutoConfiguration.class,
        DlcJacksonAutoConfiguration.class,
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
