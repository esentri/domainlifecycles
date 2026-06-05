package io.domainlifecycles.boot3.autoconfig.features.multiple.persistence_builder;

import io.domainlifecycles.boot3.autoconfig.annotation.EnableDlc;
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
@EnableDlc(exclude = {
    DlcSpringWebAutoConfiguration.class,
    DlcSpringOpenApiAutoConfiguration.class,
    DlcJackson2AutoConfiguration.class,
    DlcSpringBusDomainEventsAutoConfiguration.class,
    DlcNoTxInMemoryDomainEventsAutoConfiguration.class,
    DlcServiceKindAutoConfiguration.class
})
public class TestApplicationPersistenceAndBuilderAutoConfig {

    /**
     * Setting the Locale to explicitly force the language in default validation error messages.
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        new SpringApplicationBuilder(TestApplicationPersistenceAndBuilderAutoConfig.class).run(args);
    }
}
