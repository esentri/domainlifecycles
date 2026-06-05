package io.domainlifecycles.autoconfig.features.single.persistence.config.property;

import io.domainlifecycles.autoconfig.annotation.EnableDlc;
import io.domainlifecycles.autoconfig.configurations.DlcBuilderAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcJacksonAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcSpringBusDomainEventsAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcServiceKindAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcSpringOpenApiAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcSpringWebAutoConfiguration;
import java.util.Locale;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
@EnableDlc(exclude = {
    DlcBuilderAutoConfiguration.class,
    DlcJacksonAutoConfiguration.class,
    DlcSpringOpenApiAutoConfiguration.class,
    DlcSpringWebAutoConfiguration.class,
    DlcSpringBusDomainEventsAutoConfiguration.class,
    DlcServiceKindAutoConfiguration.class

})
public class TestApplicationPersistencePropertyValuesAutoConfig {

    /**
     * Setting the Locale to explicitly force the language in default validation error messages.
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        new SpringApplicationBuilder(TestApplicationPersistencePropertyValuesAutoConfig.class).run(args);
    }
}
