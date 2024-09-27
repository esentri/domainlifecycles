package io.domainlifecycles.events.mq.api;

import io.domainlifecycles.events.api.PublishingConfiguration;
import io.domainlifecycles.events.mq.publish.MqDomainEventPublisher;
import io.domainlifecycles.events.publish.DomainEventPublisher;

public class AbstractMqPublishingConfiguration implements PublishingConfiguration {

    private final MqDomainEventPublisher mqDomainEventPublisher;

    AbstractMqPublishingConfiguration(MqDomainEventPublisher mqDomainEventPublisher) {
        this.mqDomainEventPublisher = mqDomainEventPublisher;
    }

    @Override
    public DomainEventPublisher domainEventPublisher() {
        return mqDomainEventPublisher;
    }

    void close(){
        mqDomainEventPublisher.closeAll();
    }
}
