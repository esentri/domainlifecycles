package io.domainlifecycles.autoconfig.features.no_autoconfig;

import io.domainlifecycles.autoconfig.annotation.EnableDlc;
import io.domainlifecycles.autoconfig.configurations.DlcBuilderAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcDomainAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcJackson2AutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcJacksonAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcJooqPersistenceAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcSpringBusDomainEventsAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcServiceKindAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcSpringOpenApiAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcSpringWebAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Locale;

@SpringBootApplication
@EnableDlc(exclude = {
    DlcDomainAutoConfiguration.class,
    DlcJacksonAutoConfiguration.class,
    DlcJackson2AutoConfiguration.class,
    DlcBuilderAutoConfiguration.class,
    DlcJooqPersistenceAutoConfiguration.class,
    DlcSpringOpenApiAutoConfiguration.class,
    DlcSpringWebAutoConfiguration.class,
    DlcSpringBusDomainEventsAutoConfiguration.class,
    DlcServiceKindAutoConfiguration.class
})
public class TestApplicationNoFeaturesAutoConfig {

    /**
     * Setting the Locale to explicitly force the language in default validation error messages.
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        new SpringApplicationBuilder(TestApplicationNoFeaturesAutoConfig.class).run(args);
    }
}
