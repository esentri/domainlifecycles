package io.domainlifecycles.boot3.autoconfig.features.no_autoconfig;

import io.domainlifecycles.boot3.autoconfig.annotation.EnableDlc;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcBuilderAutoConfiguration;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcDomainAutoConfiguration;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcJackson2AutoConfiguration;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcJooqPersistenceAutoConfiguration;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcServiceKindAutoConfiguration;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcSpringBusDomainEventsAutoConfiguration;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcSpringOpenApiAutoConfiguration;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcSpringWebAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Locale;

@SpringBootApplication
@EnableDlc(exclude = {
    DlcDomainAutoConfiguration.class,
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
