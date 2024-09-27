package io.domainlifecycles.events.spring.outbox.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.domainlifecycles.events.api.ChannelFactory;
import io.domainlifecycles.events.api.ConsumingOnlyChannel;
import io.domainlifecycles.events.api.ProcessingChannel;
import io.domainlifecycles.events.api.PublishingOnlyChannel;
import io.domainlifecycles.events.consume.GeneralDomainEventConsumer;
import io.domainlifecycles.events.consume.execution.detector.MirrorBasedExecutionContextDetector;
import io.domainlifecycles.events.consume.execution.processor.SimpleExecutionContextProcessor;
import io.domainlifecycles.events.spring.outbox.SpringOutboxDomainEventPublisher;
import io.domainlifecycles.events.spring.outbox.impl.SpringJdbcOutbox;
import io.domainlifecycles.events.spring.outbox.poll.DirectOutboxPoller;
import io.domainlifecycles.events.spring.receive.execution.handler.SpringTransactionalHandlerExecutor;
import io.domainlifecycles.services.api.ServiceProvider;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Objects;

public class SpringTransactionalOutboxChannelFactory implements ChannelFactory {

    private final PlatformTransactionManager platformTransactionManager;
    private final ObjectMapper objectMapper;
    private final DataSource dataSource;
    private final ServiceProvider serviceProvider;

    private final TransactionalOutbox transactionalOutbox;

    public SpringTransactionalOutboxChannelFactory(PlatformTransactionManager platformTransactionManager, ObjectMapper objectMapper, DataSource dataSource, ServiceProvider serviceProvider) {
        this.dataSource = Objects.requireNonNull(dataSource, "A DataSource is required!");
        this.platformTransactionManager = Objects.requireNonNull(platformTransactionManager, "A PlatformTransactionManager is required!");
        this.objectMapper = Objects.requireNonNull(objectMapper, "An ObjectMapper is required!");
        this.serviceProvider = serviceProvider;
        this.transactionalOutbox = new SpringJdbcOutbox(dataSource, objectMapper, platformTransactionManager);
    }

    public SpringTransactionalOutboxChannelFactory(PlatformTransactionManager platformTransactionManager, ObjectMapper objectMapper, DataSource dataSource) {
        this(platformTransactionManager, objectMapper, dataSource, null);
    }

    @Override
    public ConsumingOnlyChannel consumeOnlyChannel(String channelName) {
        return new ConsumingOnlyChannel(channelName, consumingConfiguration());
    }

    @Override
    public PublishingOnlyChannel publishOnlyChannel(String channelName) {
        return new PublishingOnlyChannel(channelName, publishingConfiguration());
    }

    @Override
    public ProcessingChannel processingChannel(String channelName) {
        return new ProcessingChannel(channelName, publishingConfiguration(), consumingConfiguration());
    }

    SpringOutboxConsumingConfiguration consumingConfiguration(){
        var handlerExecutor = new SpringTransactionalHandlerExecutor(this.platformTransactionManager);
        var sp = Objects.requireNonNull(this.serviceProvider,"A ServiceProvider is required!");
        var executionContextDetector = new MirrorBasedExecutionContextDetector(sp);
        var executionContextProcessor = new SimpleExecutionContextProcessor(handlerExecutor);
        var domainEventConsumer = new GeneralDomainEventConsumer(executionContextDetector, executionContextProcessor);
        var poller = new DirectOutboxPoller(this.transactionalOutbox, domainEventConsumer);
        return new SpringOutboxConsumingConfiguration(poller);
    }

    SpringOutboxPublishingConfiguration publishingConfiguration(){
        return new SpringOutboxPublishingConfiguration(
            new SpringOutboxDomainEventPublisher(
                this.transactionalOutbox,
                this.platformTransactionManager
            )
        );
    }

    public TransactionalOutbox getTransactionalOutbox() {
        return transactionalOutbox;
    }
}
