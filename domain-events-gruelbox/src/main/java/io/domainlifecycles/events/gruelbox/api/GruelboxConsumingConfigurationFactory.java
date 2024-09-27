package io.domainlifecycles.events.gruelbox.api;

import com.gruelbox.transactionoutbox.TransactionOutbox;
import io.domainlifecycles.events.consume.GeneralDomainEventConsumer;
import io.domainlifecycles.events.consume.execution.detector.MirrorBasedExecutionContextDetector;
import io.domainlifecycles.events.consume.execution.handler.HandlerExecutor;
import io.domainlifecycles.events.consume.execution.handler.ReflectiveHandlerExecutor;
import io.domainlifecycles.events.consume.execution.handler.TransactionalHandlerExecutor;
import io.domainlifecycles.events.consume.execution.processor.SimpleExecutionContextProcessor;
import io.domainlifecycles.events.gruelbox.dispatch.DirectGruelboxDomainEventDispatcher;
import io.domainlifecycles.events.gruelbox.idempotent.IdempotencyAwareHandlerExecutorProxy;
import io.domainlifecycles.events.gruelbox.idempotent.IdempotencyConfiguration;
import io.domainlifecycles.events.gruelbox.idempotent.IdempotentExecutor;
import io.domainlifecycles.events.gruelbox.poll.GruelboxPoller;
import io.domainlifecycles.services.api.ServiceProvider;

import java.util.Objects;

class GruelboxConsumingConfigurationFactory {

    private final ServiceProvider serviceProvider;
    private final TransactionOutbox transactionOutbox;
    private final PollerConfiguration pollerConfiguration;
    private final DomainEventsInstantiator domainEventsInstantiator;

    GruelboxConsumingConfigurationFactory(
        ServiceProvider serviceProvider,
        TransactionOutbox transactionOutbox,
        PollerConfiguration pollerConfiguration,
        DomainEventsInstantiator domainEventsInstantiator
        ) {
        this.serviceProvider = Objects.requireNonNull(serviceProvider, "A ServiceProvider is required!");
        this.transactionOutbox = Objects.requireNonNull(transactionOutbox, "A TransactionOutbox is required!");
        this.domainEventsInstantiator = Objects.requireNonNull(domainEventsInstantiator, "A DomainEventsInstantiator is required!");
        this.pollerConfiguration = Objects.requireNonNull(pollerConfiguration, "A PollerConfiguration is required!");
    }

    GruelboxConsumingConfiguration consumingConfiguration(HandlerExecutor handlerExecutor){
        Objects.requireNonNull(handlerExecutor, "HandlerExecutor is required!");
        var poller = new GruelboxPoller(transactionOutbox, pollerConfiguration);
        var executionContextDetector = new MirrorBasedExecutionContextDetector(this.serviceProvider);
        var executionContextProcessor = new SimpleExecutionContextProcessor(handlerExecutor);
        var domainEventConsumer = new GeneralDomainEventConsumer(executionContextDetector, executionContextProcessor);
        var gruelboxDomainEventDispatcher = new DirectGruelboxDomainEventDispatcher(domainEventConsumer);
        domainEventsInstantiator.registerGruelboxDomainEventDispatcher(gruelboxDomainEventDispatcher);
        return new GruelboxConsumingConfiguration(poller, gruelboxDomainEventDispatcher, handlerExecutor);
    }

    GruelboxConsumingConfiguration idempotentConsumingConfiguration(IdempotencyConfiguration idempotencyConfiguration,
                                                                    TransactionalHandlerExecutor transactionalHandlerExecutor){
        Objects.requireNonNull(idempotencyConfiguration, "IdempotencyConfiguration is required!");
        Objects.requireNonNull(transactionalHandlerExecutor, "TransactionalHandlerExecutor is required!");
        var usedHandlerExecutor = new IdempotencyAwareHandlerExecutorProxy(
            transactionalHandlerExecutor,
            idempotencyConfiguration,
            transactionOutbox
        );
        domainEventsInstantiator.registerIdempotentExecutor(new IdempotentExecutor(serviceProvider, new ReflectiveHandlerExecutor()));
        return consumingConfiguration(usedHandlerExecutor);
    }

}
