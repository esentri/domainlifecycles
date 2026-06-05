package io.domainlifecycles.autoconfig.features.single.jackson;

import io.domainlifecycles.autoconfig.annotation.EnableDlc;
import io.domainlifecycles.autoconfig.configurations.DlcBuilderAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcJooqPersistenceAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcNoTxInMemoryDomainEventsAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcSpringBusDomainEventsAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcServiceKindAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcSpringOpenApiAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcSpringWebAutoConfiguration;
import java.util.Locale;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
@EnableDlc(
    exclude = {
        DlcBuilderAutoConfiguration.class,
        DlcSpringWebAutoConfiguration.class,
        DlcSpringOpenApiAutoConfiguration.class,
        DlcJooqPersistenceAutoConfiguration.class,
        DlcSpringBusDomainEventsAutoConfiguration.class,
        DlcNoTxInMemoryDomainEventsAutoConfiguration.class,
        DlcJooqPersistenceAutoConfiguration.class,
        DlcServiceKindAutoConfiguration.class
    },
    dlcMirrorBasePackages = "io.domainlifecycles.autoconfig"
)
public class TestApplicationJacksonSingleAutoConfig {

    /**
     * Setting the Locale to explicitly force the language in default validation error messages.
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        new SpringApplicationBuilder(TestApplicationJacksonSingleAutoConfig.class).run(args);
    }
}
