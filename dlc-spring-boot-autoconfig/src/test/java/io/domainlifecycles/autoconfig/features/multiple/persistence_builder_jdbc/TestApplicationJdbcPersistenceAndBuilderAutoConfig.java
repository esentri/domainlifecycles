package io.domainlifecycles.autoconfig.features.multiple.persistence_builder_jdbc;

import io.domainlifecycles.autoconfig.annotation.EnableDlc;
import io.domainlifecycles.autoconfig.configurations.DlcJacksonAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcJooqPersistenceAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcNoTxInMemoryDomainEventsAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcSpringBusDomainEventsAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcSpringOpenApiAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcSpringWebAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcServiceKindAutoConfiguration;
import java.util.Locale;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Mirrors {@code TestApplicationPersistenceAndBuilderAutoConfig}, excluding
 * {@link DlcJooqPersistenceAutoConfiguration} instead so that {@code DlcJdbcPersistenceAutoConfiguration}
 * (the only persistence autoconfig left active) is the one exercised - both would otherwise activate
 * side by side, since jOOQ is also on this module's test classpath.
 */
@SpringBootApplication
@EnableDlc(exclude = {
    DlcJooqPersistenceAutoConfiguration.class,
    DlcSpringWebAutoConfiguration.class,
    DlcSpringOpenApiAutoConfiguration.class,
    DlcJacksonAutoConfiguration.class,
    DlcSpringBusDomainEventsAutoConfiguration.class,
    DlcNoTxInMemoryDomainEventsAutoConfiguration.class,
    DlcServiceKindAutoConfiguration.class

})
public class TestApplicationJdbcPersistenceAndBuilderAutoConfig {

    /**
     * Setting the Locale to explicitly force the language in default validation error messages.
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        new SpringApplicationBuilder(TestApplicationJdbcPersistenceAndBuilderAutoConfig.class).run(args);
    }
}
