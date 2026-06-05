package io.domainlifecycles.autoconfig.features.single.servicekind.regular;

import io.domainlifecycles.autoconfig.annotation.EnableDlc;
import io.domainlifecycles.autoconfig.configurations.DlcBuilderAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcJackson2AutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcNoTxInMemoryDomainEventsAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcSpringBusDomainEventsAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcSpringOpenApiAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcSpringWebAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Locale;

@SpringBootApplication
@EnableDlc(exclude = {
    DlcBuilderAutoConfiguration.class,
    DlcSpringWebAutoConfiguration.class,
    DlcSpringOpenApiAutoConfiguration.class,
    DlcJackson2AutoConfiguration.class,
    DlcSpringBusDomainEventsAutoConfiguration.class,
    DlcNoTxInMemoryDomainEventsAutoConfiguration.class
})
public class TestApplicationServiceKindSingleAutoConfig {

    /**
     * Setting the Locale to explicitly force the language in default validation error messages.
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        new SpringApplicationBuilder(TestApplicationServiceKindSingleAutoConfig.class).run(args);
    }
}
