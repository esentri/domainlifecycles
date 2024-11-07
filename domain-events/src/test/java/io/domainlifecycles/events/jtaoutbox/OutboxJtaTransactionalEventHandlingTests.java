package io.domainlifecycles.events.jtaoutbox;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.atomikos.icatch.jta.UserTransactionManager;
import io.domainlifecycles.domain.types.DomainEvent;
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
import io.domainlifecycles.events.publish.outbox.api.ProcessingResult;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.services.Services;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;


public class OutboxJtaTransactionalEventHandlingTests {

    private static ADomainService domainService;
    private static ARepository repository;
    private static AnApplicationService applicationService;
    private static AQueryClient queryClient;
    private static AnOutboundService outboundService;
    private static UserTransactionManager userTransactionManager;
    private static MockOutbox transactionalOutbox;


    @BeforeAll
    public static void init() {
        Logger rootLogger =
            (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.DEBUG);

        transactionalOutbox = new MockOutbox();

        userTransactionManager = new UserTransactionManager();

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
        services.registerServiceKindInstance(outboundService);
        services.registerServiceKindInstance(queryClient);

        var configBuilder = new DomainEventsConfiguration.DomainEventsConfigurationBuilder();

        configBuilder
            .withServiceProvider(services)
            .withJtaTransactionManager(userTransactionManager)
            .withTransactionalOutbox(transactionalOutbox);
        var config = configBuilder.make();
        config.outboxPoller.setPollingDelayMilliseconds(1000);
        config.outboxPoller.setPollingPeriodMilliseconds(1000);
    }

    private MockOutboxEntry outboxContainsEventWithStatus(DomainEvent domainEvent) {
        return transactionalOutbox.entries
            .stream()
            .filter(entry -> entry.domainEvent.equals(domainEvent) && entry.processingResult != null)
            .findFirst().orElse(null);
    }

    @Test
    public void testIntegrationCommit() throws Exception {
        userTransactionManager.begin();
        //when
        var evt = new ADomainEvent("TestCommit");
        DomainEvents.publish(evt);

        userTransactionManager.commit();

        var outboxEvents = transactionalOutbox.entries.stream()
            .map(entry -> entry.domainEvent).toList();

        //then
        assertThat(outboxEvents).contains(evt);

        await().atMost(5, SECONDS).until(() -> outboxContainsEventWithStatus(evt) != null);

        var outboxEntry = outboxContainsEventWithStatus(evt);
        assertThat(outboxEntry.processingResult).isEqualTo(ProcessingResult.OK);


        assertThat(domainService.received).contains(evt);
        assertThat(repository.received).contains(evt);
        assertThat(applicationService.received).contains(evt);
        assertThat(outboundService.received).contains(evt);
        assertThat(queryClient.received).contains(evt);
    }

    @Test
    public void testIntegrationUnreceivedCommit() throws Exception {
        userTransactionManager.begin();
        //when
        var evt = new UnreceivedDomainEvent("TestUnReceivedCommit");
        DomainEvents.publish(evt);

        userTransactionManager.commit();

        var outboxEvents = transactionalOutbox.entries.stream()
            .map(entry -> entry.domainEvent).toList();

        //then
        assertThat(outboxEvents).contains(evt);

        await().atMost(5, SECONDS).until(() -> outboxContainsEventWithStatus(evt) != null);

        var outboxEntry = outboxContainsEventWithStatus(evt);
        assertThat(outboxEntry.processingResult).isEqualTo(ProcessingResult.OK);
        //then
        assertThat(domainService.received).doesNotContain(evt);
        assertThat(repository.received).doesNotContain(evt);
        assertThat(applicationService.received).doesNotContain(evt);
        assertThat(outboundService.received).doesNotContain(evt);
        assertThat(queryClient.received).doesNotContain(evt);
    }

    @Test
    public void testIntegrationNoTransaction() {

        //when
        var evt = new ADomainEvent("TestNoTrans");
        DomainEvents.publish(evt);

        var outboxEvents = transactionalOutbox.entries.stream()
            .map(entry -> entry.domainEvent).toList();
        //then
        assertThat(outboxEvents).contains(evt);

        await().atMost(5, SECONDS).until(() -> outboxContainsEventWithStatus(evt) != null);

        var outboxEntry = outboxContainsEventWithStatus(evt);
        assertThat(outboxEntry.processingResult).isEqualTo(ProcessingResult.OK);

        assertThat(domainService.received).contains(evt);
        assertThat(repository.received).contains(evt);
        assertThat(applicationService.received).contains(evt);
        assertThat(outboundService.received).contains(evt);
        assertThat(queryClient.received).contains(evt);

    }

    @Test
    public void testIntegrationAggregateDomainEventCommit() throws Exception {
        //when
        userTransactionManager.begin();
        var evt = new AnAggregateDomainEvent("TestAggregateDomainEventCommit");
        DomainEvents.publish(evt);
        userTransactionManager.commit();

        var outboxEvents = transactionalOutbox.entries.stream()
            .map(entry -> entry.domainEvent).toList();
        //then
        assertThat(outboxEvents).contains(evt);

        await().atMost(5, SECONDS).until(() -> outboxContainsEventWithStatus(evt) != null);
        var outboxEntry = outboxContainsEventWithStatus(evt);
        assertThat(outboxEntry.processingResult).isEqualTo(ProcessingResult.OK);

        assertThat(repository.received).doesNotContain(evt);
        assertThat(domainService.received).doesNotContain(evt);
        assertThat(applicationService.received).doesNotContain(evt);
        assertThat(outboundService.received).doesNotContain(evt);
        assertThat(queryClient.received).doesNotContain(evt);
        var root = repository.findById(new AnAggregate.AggregateId(1L)).orElseThrow();
        assertThat(root.received).contains(evt);
    }

    @Test
    public void testIntegrationAggregateDomainEventRollbackExceptionOnHandler() throws Exception {
        //when
        userTransactionManager.begin();
        var evt = new AnAggregateDomainEvent("TestAggregateDomainWithException");
        DomainEvents.publish(evt);
        userTransactionManager.commit();

        var outboxEvents = transactionalOutbox.entries.stream()
            .map(entry -> entry.domainEvent).toList();
        //then
        assertThat(outboxEvents).contains(evt);

        await().atMost(5, SECONDS).until(() -> outboxContainsEventWithStatus(evt) != null);
        var outboxEntry = outboxContainsEventWithStatus(evt);
        assertThat(outboxEntry.processingResult).isEqualTo(ProcessingResult.FAILED);

        assertThat(repository.received).doesNotContain(evt);
        assertThat(domainService.received).doesNotContain(evt);
        assertThat(applicationService.received).doesNotContain(evt);
        assertThat(outboundService.received).doesNotContain(evt);
        assertThat(queryClient.received).doesNotContain(evt);
        var root = repository.findById(new AnAggregate.AggregateId(1L)).orElseThrow();
        assertThat(root.received).doesNotContain(evt);
    }

    @Test
    public void testIntegrationDomainServiceExceptionRollback() throws Exception {
        //when
        userTransactionManager.begin();
        var evt = new ADomainEvent("TestDomainServiceRollback");
        DomainEvents.publish(evt);
        userTransactionManager.commit();

        var outboxEvents = transactionalOutbox.entries.stream()
            .map(entry -> entry.domainEvent).toList();
        //then
        assertThat(outboxEvents).contains(evt);

        await().atMost(5, SECONDS).until(() -> outboxContainsEventWithStatus(evt) != null);
        var outboxEntry = outboxContainsEventWithStatus(evt);
        assertThat(outboxEntry.processingResult).isEqualTo(ProcessingResult.FAILED_PARTIALLY);

        assertThat(repository.received).contains(evt);
        assertThat(domainService.received).doesNotContain(evt);
        assertThat(applicationService.received).contains(evt);
        assertThat(outboundService.received).contains(evt);
        assertThat(queryClient.received).contains(evt);
        var root = repository.findById(new AnAggregate.AggregateId(1L)).orElseThrow();
        assertThat(root.received).doesNotContain(evt);
    }


}
