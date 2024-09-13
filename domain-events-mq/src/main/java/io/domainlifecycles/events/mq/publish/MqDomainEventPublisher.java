package io.domainlifecycles.events.mq.publish;

import io.domainlifecycles.events.publish.DomainEventPublisher;

public interface MqDomainEventPublisher extends DomainEventPublisher {

    void closeAll();
}
