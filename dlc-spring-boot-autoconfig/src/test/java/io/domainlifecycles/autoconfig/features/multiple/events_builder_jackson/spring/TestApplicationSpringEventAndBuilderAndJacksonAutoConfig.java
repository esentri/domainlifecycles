package io.domainlifecycles.autoconfig.features.multiple.events_builder_jackson.spring;

import io.domainlifecycles.autoconfig.annotation.EnableDlc;
import io.domainlifecycles.autoconfig.configurations.DlcJooqPersistenceAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcServiceKindAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcSpringOpenApiAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcSpringWebAutoConfiguration;
import io.domainlifecycles.autoconfig.model.events.ADomainService;
import io.domainlifecycles.autoconfig.model.events.AQueryHandler;
import io.domainlifecycles.autoconfig.model.events.ARepository;
import io.domainlifecycles.autoconfig.model.events.AnApplicationService;
import io.domainlifecycles.autoconfig.model.events.AnOutboundService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import java.util.Locale;

@SpringBootApplication
@EnableDlc(exclude = {
    DlcSpringWebAutoConfiguration.class,
    DlcJooqPersistenceAutoConfiguration.class,
    DlcSpringOpenApiAutoConfiguration.class,
    DlcServiceKindAutoConfiguration.class

})
public class TestApplicationSpringEventAndBuilderAndJacksonAutoConfig {

    /**
     * Setting the Locale to explicitly force the language in default validation error messages.
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        new SpringApplicationBuilder(TestApplicationSpringEventAndBuilderAndJacksonAutoConfig.class).run(args);
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
