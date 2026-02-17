package io.domainlifecycles.autoconfig.features.multiple.events_builder_jackson_persistence_servicekind.spring;

import io.domainlifecycles.autoconfig.annotation.EnableDlc;
import io.domainlifecycles.autoconfig.configurations.DlcSpringOpenApiAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcSpringWebAutoConfiguration;
import java.util.Locale;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

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
