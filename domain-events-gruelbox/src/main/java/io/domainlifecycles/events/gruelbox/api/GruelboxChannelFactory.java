package io.domainlifecycles.events.gruelbox.api;

import com.gruelbox.transactionoutbox.TransactionOutbox;
import io.domainlifecycles.events.api.ConsumingOnlyChannel;
import io.domainlifecycles.events.api.ProcessingChannel;
import io.domainlifecycles.events.api.ChannelFactory;
import io.domainlifecycles.events.api.PublishingOnlyChannel;
import io.domainlifecycles.events.consume.execution.handler.TransactionalHandlerExecutor;
import io.domainlifecycles.events.gruelbox.idempotent.IdempotencyConfiguration;
import io.domainlifecycles.services.api.ServiceProvider;
import java.util.Objects;

public class GruelboxChannelFactory implements ChannelFactory {

    private final ServiceProvider serviceProvider;
    private final TransactionOutbox transactionOutbox;
    private final TransactionalHandlerExecutor transactionalHandlerExecutor;
    private final DomainEventsInstantiator domainEventsInstantiator;
    private final IdempotencyConfiguration idempotencyConfiguration;
    private final PollerConfiguration pollerConfiguration;
    private final PublishingSchedulerConfiguration publishingSchedulerConfiguration;

    public GruelboxChannelFactory(ServiceProvider serviceProvider,
                                  TransactionOutbox transactionOutbox,
                                  TransactionalHandlerExecutor transactionalHandlerExecutor,
                                  DomainEventsInstantiator domainEventsInstantiator,
                                  IdempotencyConfiguration idempotencyConfiguration,
                                  PollerConfiguration pollerConfiguration,
                                  PublishingSchedulerConfiguration publishingSchedulerConfiguration) {
        this.serviceProvider = serviceProvider;
        this.transactionOutbox = Objects.requireNonNull(transactionOutbox, "A TransactionOutbox is required!");
        this.transactionalHandlerExecutor = transactionalHandlerExecutor;
        this.domainEventsInstantiator = Objects.requireNonNull(domainEventsInstantiator, "A");
        this.idempotencyConfiguration = idempotencyConfiguration;
        this.pollerConfiguration = (pollerConfiguration == null ?
            new PollerConfiguration() : pollerConfiguration);
        this.publishingSchedulerConfiguration = (publishingSchedulerConfiguration == null ?
            new PublishingSchedulerConfiguration() : publishingSchedulerConfiguration);
    }

    public GruelboxChannelFactory(ServiceProvider serviceProvider,
                                  TransactionOutbox transactionOutbox,
                                  TransactionalHandlerExecutor transactionalHandlerExecutor,
                                  DomainEventsInstantiator domainEventsInstantiator,
                                  IdempotencyConfiguration idempotencyConfiguration) {
        this(
            serviceProvider,
            transactionOutbox,
            transactionalHandlerExecutor,
            domainEventsInstantiator,
            idempotencyConfiguration,
            null,
            null
        );
    }

    public GruelboxChannelFactory(ServiceProvider serviceProvider,
                                  TransactionOutbox transactionOutbox,
                                  TransactionalHandlerExecutor transactionalHandlerExecutor,
                                  DomainEventsInstantiator domainEventsInstantiator) {
        this(
            serviceProvider,
            transactionOutbox,
            transactionalHandlerExecutor,
            domainEventsInstantiator,
            null,
            null,
            null
        );
    }

    @Override
    public ConsumingOnlyChannel consumeOnlyChannel(String channelName) {
        return new ConsumingOnlyChannel(channelName, createGruelboxConsumingConfiguration());
    }

    @Override
    public PublishingOnlyChannel publishOnlyChannel(String channelName) {
        return new PublishingOnlyChannel(channelName, createGruelboxPublishingConfiguration());
    }

    @Override
    public ProcessingChannel processingChannel(String channelName) {
        return new ProcessingChannel(channelName, createGruelboxPublishingConfiguration(), createGruelboxConsumingConfiguration());
    }

    private GruelboxConsumingConfiguration createGruelboxConsumingConfiguration(){
        var factory = new GruelboxConsumingConfigurationFactory(
            this.serviceProvider,
            this.transactionOutbox,
            this.pollerConfiguration,
            this.domainEventsInstantiator
        );
        if(idempotencyConfiguration != null){
            return factory.idempotentConsumingConfiguration(idempotencyConfiguration, transactionalHandlerExecutor);
        }else{
            return factory.consumingConfiguration(transactionalHandlerExecutor);
        }
    }

    private GruelboxPublishingConfiguration createGruelboxPublishingConfiguration(){
        return new GruelboxPublishingConfiguration(transactionOutbox, publishingSchedulerConfiguration);
    }

}
