package io.domainlifecycles.boot3.autoconfig.features.multiple.events_builder_persistence_servicekind.spring;

import io.domainlifecycles.boot3.autoconfig.annotation.EnableDlc;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcSpringOpenApiAutoConfiguration;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcSpringWebAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Locale;

@SpringBootApplication
@EnableDlc(exclude = {
    DlcSpringWebAutoConfiguration.class,
    DlcSpringOpenApiAutoConfiguration.class
})
public class TestApplicationSpringEventAndBuilderAndJacksonAndPersistenceAndServiceKindAutoConfig {

    /**
     * Setting the Locale to explicitly force the language in default validation error messages.
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        new SpringApplicationBuilder(
            TestApplicationSpringEventAndBuilderAndJacksonAndPersistenceAndServiceKindAutoConfig.class).run(args);
    }
}
