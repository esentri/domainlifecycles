package io.domainlifecycles.boot3.autoconfig.features.single.servicekind.override;

import io.domainlifecycles.boot3.autoconfig.annotation.EnableDlc;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcBuilderAutoConfiguration;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcJackson2AutoConfiguration;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcNoTxInMemoryDomainEventsAutoConfiguration;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcSpringBusDomainEventsAutoConfiguration;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcSpringOpenApiAutoConfiguration;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcSpringWebAutoConfiguration;
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
public class TestApplicationServiceKindSingleOverAutoConfig {

    /**
     * Setting the Locale to explicitly force the language in default validation error messages.
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        new SpringApplicationBuilder(TestApplicationServiceKindSingleOverAutoConfig.class).run(args);
    }
}
