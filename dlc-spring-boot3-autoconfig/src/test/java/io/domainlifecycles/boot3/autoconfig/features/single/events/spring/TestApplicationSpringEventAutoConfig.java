package io.domainlifecycles.boot3.autoconfig.features.single.events.spring;

import io.domainlifecycles.boot3.autoconfig.annotation.EnableDlc;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcBuilderAutoConfiguration;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcJooqPersistenceAutoConfiguration;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcSpringOpenApiAutoConfiguration;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcSpringWebAutoConfiguration;
import io.domainlifecycles.boot3.autoconfig.model.events.ADomainService;
import io.domainlifecycles.boot3.autoconfig.model.events.AQueryHandler;
import io.domainlifecycles.boot3.autoconfig.model.events.ARepository;
import io.domainlifecycles.boot3.autoconfig.model.events.AnApplicationService;
import io.domainlifecycles.boot3.autoconfig.model.events.AnOutboundService;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcServiceKindAutoConfiguration;
import java.util.Locale;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
@EnableDlc(
    exclude = {
        DlcBuilderAutoConfiguration.class,
        DlcSpringWebAutoConfiguration.class,
        DlcSpringOpenApiAutoConfiguration.class,
        DlcJooqPersistenceAutoConfiguration.class,
        DlcServiceKindAutoConfiguration.class
    }
)
public class TestApplicationSpringEventAutoConfig {

    /**
     * Setting the Locale to explicitly force the language in default validation error messages.
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        new SpringApplicationBuilder(TestApplicationSpringEventAutoConfig.class).run(args);
    }

    @Bean
    public AnApplicationService anApplicationService(){
        return new AnApplicationService();
    }

    @Bean
    public ADomainService aDomainService(){
        return new ADomainService();
    }

    @Bean
    public ARepository aRepository(){
        return new ARepository();
    }

    @Bean
    public AQueryHandler aQueryHandler(){
        return new AQueryHandler();
    }

    @Bean
    public AnOutboundService anOutboundService(){
        return new AnOutboundService();
    }
}
