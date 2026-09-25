package io.domainlifecycles.staticanalysis.fixture;

import io.domainlifecycles.domain.types.DomainService;
import io.domainlifecycles.domain.types.Publishes;

public class TestPublisherService implements DomainService {

    @Publishes(domainEventTypes = TestEvent.class)
    public void publish() {
    }
}
