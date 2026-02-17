package io.domainlifecycles.autoconfig.features.multiplechannels;

import io.domainlifecycles.autoconfig.annotation.EnableDlc;
import io.domainlifecycles.autoconfig.model.events.AnAggregateDomainEvent;
import io.domainlifecycles.events.api.DomainEventTypeBasedRouter;
import io.domainlifecycles.events.api.ProcessingChannel;
import io.domainlifecycles.events.api.PublishingChannel;
import io.domainlifecycles.events.api.PublishingRouter;
import io.domainlifecycles.events.inmemory.InMemoryChannelFactory;
import io.domainlifecycles.events.spring.SpringApplicationEventsPublishingChannelFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Locale;

@SpringBootApplication()
@EnableDlc()
public class TestApplicationEvents {
    /**
     * Setting the Locale to explicitly force the language in default validation error messages.
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        new SpringApplicationBuilder(TestApplicationEvents.class).
            run(args);
    }

    @Bean
    public ProcessingChannel inMemoryChannel(InMemoryChannelFactory factory){
        return factory.processingChannel("inMemory");
    }

    @Bean
    public PublishingChannel springChannel(SpringApplicationEventsPublishingChannelFactory factory){
        return factory.publishOnlyChannel("springTx");
    }

    @Bean
    public PublishingRouter router(List<PublishingChannel> channels ){
        var router = new DomainEventTypeBasedRouter(channels);
        router.defineDefaultChannel("inMemory");
        router.defineExplicitRoute(AnAggregateDomainEvent.class, "springTx");
        return router;
    }
}
