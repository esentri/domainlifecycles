package io.domainlifecycles.events.mq.api;

import io.domainlifecycles.events.api.ConsumingConfiguration;
import io.domainlifecycles.events.mq.consume.MqDomainEventConsumer;

public class AbstractMqConsumingConfiguration implements ConsumingConfiguration {

    private final MqDomainEventConsumer mqDomainEventConsumer;

    AbstractMqConsumingConfiguration(MqDomainEventConsumer mqDomainEventConsumer) {
        this.mqDomainEventConsumer = mqDomainEventConsumer;
    }

    void close(){
        mqDomainEventConsumer.closeAll();
    }

    public MqDomainEventConsumer getMqDomainEventConsumer() {
        return mqDomainEventConsumer;
    }
}
