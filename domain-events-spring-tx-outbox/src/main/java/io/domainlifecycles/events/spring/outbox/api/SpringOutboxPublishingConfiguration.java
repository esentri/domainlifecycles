package io.domainlifecycles.events.spring.outbox.api;

import io.domainlifecycles.events.api.PublishingConfiguration;
import io.domainlifecycles.events.publish.DomainEventPublisher;
import io.domainlifecycles.events.spring.outbox.SpringOutboxDomainEventPublisher;

import java.util.Objects;

public class SpringOutboxPublishingConfiguration implements PublishingConfiguration {

    private final SpringOutboxDomainEventPublisher springOutboxDomainEventPublisher;

    SpringOutboxPublishingConfiguration(SpringOutboxDomainEventPublisher springOutboxDomainEventPublisher) {
        this.springOutboxDomainEventPublisher = Objects.requireNonNull(springOutboxDomainEventPublisher, "SpringOutboxDomainEventPublisher is required!");
    }

    @Override
    public DomainEventPublisher domainEventPublisher() {
        return springOutboxDomainEventPublisher;
    }
}
