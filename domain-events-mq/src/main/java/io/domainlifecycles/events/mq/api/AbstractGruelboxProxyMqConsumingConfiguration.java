package io.domainlifecycles.events.mq.api;

import io.domainlifecycles.events.consume.execution.handler.HandlerExecutor;
import io.domainlifecycles.events.mq.consume.MqDomainEventConsumer;

public class AbstractGruelboxProxyMqConsumingConfiguration extends AbstractMqConsumingConfiguration{

    private final HandlerExecutor usedHandlerExecutor;

    /**
     * Constructs a new AbstractMqConsumingConfiguration with the provided MqDomainEventConsumer.
     *
     * @param mqDomainEventConsumer the MqDomainEventConsumer used for consuming domain events
     * @param usedHandlerExecutor the handler Executor instance used in this configuration
     */
    AbstractGruelboxProxyMqConsumingConfiguration(MqDomainEventConsumer mqDomainEventConsumer,
                                                  HandlerExecutor usedHandlerExecutor) {
        super(mqDomainEventConsumer);
        this.usedHandlerExecutor = usedHandlerExecutor;
    }

    /**
     * Retrieves the HandlerExecutor instance used in this configuration.
     *
     * @return The HandlerExecutor instance used in this configuration
     */
    public HandlerExecutor getUsedHandlerExecutor() {
        return usedHandlerExecutor;
    }

}
