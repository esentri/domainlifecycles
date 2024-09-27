package io.domainlifecycles.events.api;

import io.domainlifecycles.events.publish.DomainEventPublisher;

public interface PublishingConfiguration {

    DomainEventPublisher domainEventPublisher();
}
