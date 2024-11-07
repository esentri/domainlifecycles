package io.domainlifecycles.events.nontransactional;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import io.domainlifecycles.events.ADomainEvent;
import io.domainlifecycles.events.ADomainService;
import io.domainlifecycles.events.AQueryClient;
import io.domainlifecycles.events.ARepository;
import io.domainlifecycles.events.AnAggregate;
import io.domainlifecycles.events.AnAggregateDomainEvent;
import io.domainlifecycles.events.AnApplicationService;
import io.domainlifecycles.events.AnOutboundService;
import io.domainlifecycles.events.UnreceivedDomainEvent;
import io.domainlifecycles.events.api.DomainEvents;
import io.domainlifecycles.events.api.DomainEventsConfiguration;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.services.Services;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;


public class DirectNonTransactionalEventHandlingTests {

    private static ADomainService domainService;
    private static ARepository repository;
    private static AnApplicationService applicationService;
    private static AQueryClient queryClient;
    private static AnOutboundService outboundService;

    @BeforeAll
    public static void init() {
        Logger rootLogger =
            (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.DEBUG);
        Domain.initialize(new ReflectiveDomainMirrorFactory("io.domainlifecycles.events"));

        domainService = new ADomainService();
        repository = new ARepository();
        applicationService = new AnApplicationService();
        queryClient = new AQueryClient();
        outboundService = new AnOutboundService();

        var services = new Services();
        services.registerServiceKindInstance(domainService);
        services.registerServiceKindInstance(repository);
        services.registerServiceKindInstance(applicationService);
        services.registerServiceKindInstance(queryClient);
        services.registerServiceKindInstance(outboundService);

        var configBuilder = new DomainEventsConfiguration.DomainEventsConfigurationBuilder();
        configBuilder.withServiceProvider(services);

        configBuilder.make();
    }

    @Test
    public void testIntegrationDomainEvent() {
        //when
        var evt = new ADomainEvent("Test");
        DomainEvents.publish(evt);
        //then
        assertThat(domainService.received).contains(evt);
        assertThat(repository.received).contains(evt);
        assertThat(applicationService.received).contains(evt);
        assertThat(queryClient.received).contains(evt);
        assertThat(outboundService.received).contains(evt);
    }

    @Test
    public void testIntegrationUnreceivedCommit() {
        //when
        var evt = new UnreceivedDomainEvent("TestUnReceivedCommit");
        DomainEvents.publish(evt);
        //then
        assertThat(domainService.received).doesNotContain(evt);
        assertThat(repository.received).doesNotContain(evt);
        assertThat(applicationService.received).doesNotContain(evt);
        assertThat(queryClient.received).doesNotContain(evt);
        assertThat(outboundService.received).doesNotContain(evt);
    }

    @Test
    public void testIntegrationAggregateDomainEvent() {
        //when
        var evt = new AnAggregateDomainEvent("Test");
        DomainEvents.publish(evt);
        //then

        assertThat(repository.received).doesNotContain(evt);
        assertThat(domainService.received).doesNotContain(evt);
        assertThat(applicationService.received).doesNotContain(evt);
        assertThat(queryClient.received).doesNotContain(evt);
        assertThat(outboundService.received).doesNotContain(evt);
        var root = repository.findById(new AnAggregate.AggregateId(1L)).orElseThrow();
        assertThat(root.received).contains(evt);
    }

    //TODO test fail non transactional
}
