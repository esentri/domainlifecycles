package io.domainlifecycles.events.springoutbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.domainlifecycles.domain.types.ServiceKind;
import io.domainlifecycles.events.ADomainService;
import io.domainlifecycles.events.AQueryHandler;
import io.domainlifecycles.events.ARepository;
import io.domainlifecycles.events.AnApplicationService;
import io.domainlifecycles.events.AnOutboundService;
import io.domainlifecycles.events.api.ChannelRoutingConfiguration;
import io.domainlifecycles.events.api.DomainEventTypeBasedRouter;
import io.domainlifecycles.events.api.ProcessingChannel;
import io.domainlifecycles.events.api.PublishingChannel;
import io.domainlifecycles.events.spring.outbox.api.SpringTransactionalOutboxChannelFactory;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.services.Services;
import io.domainlifecycles.services.api.ServiceProvider;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.List;
import java.util.Locale;

@SpringBootApplication
public class OutboxTestApplication {

    static {
        Domain.initialize(new ReflectiveDomainMirrorFactory("io.domainlifecycles.events"));
    }

    /**
     * Setting the Locale to explicitly force the language in default validation error messages.
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        new SpringApplicationBuilder(OutboxTestApplication.class).run(args);
    }

    @Bean
    public AnApplicationService anApplicationService() {
        return new AnApplicationService();
    }

    @Bean
    public ADomainService aDomainService() {
        return new ADomainService();
    }

    @Bean
    public ARepository aRepository() {
        return new ARepository();
    }

    @Bean
    public AnOutboundService anOutboundService() {
        return new AnOutboundService();
    }

    @Bean
    public AQueryHandler aQueryHandler() {
        return new AQueryHandler();
    }

    /**
     * This method creates and configures a ServiceProvider instance, which is responsible for providing instances of
     * various types of services.
     */
    @Bean
    public ServiceProvider serviceProvider(List<ServiceKind> serviceKinds) {
        var services = new Services();
        serviceKinds.forEach(services::registerServiceKindInstance);
        return services;
    }

    /**
     * Using DLC Events to publish DLC domain events.
     */
    @Bean
    public SpringTransactionalOutboxChannelFactory springOutbox(DataSource dataSource,
                                                                ObjectMapper objectMapper,
                                                                PlatformTransactionManager platformTransactionManager,
                                                                ServiceProvider serviceProvider) {
        return  new SpringTransactionalOutboxChannelFactory(
            platformTransactionManager,
            objectMapper,
            dataSource,
            serviceProvider
        );
    }

    @Bean
    public ProcessingChannel channel(SpringTransactionalOutboxChannelFactory factory){
        return factory.processingChannel("channel");
    }

    @Bean
    public ChannelRoutingConfiguration channelConfiguration(List<PublishingChannel> publishingChannels){
        var router = new DomainEventTypeBasedRouter(publishingChannels);
        router.defineDefaultChannel("channel");
        return new ChannelRoutingConfiguration(router);
    }

}
