package io.domainlifecycles.events.spring.outbox.api;

import io.domainlifecycles.events.api.ConsumingConfiguration;
import io.domainlifecycles.events.spring.outbox.poll.AbstractOutboxPoller;

public class SpringOutboxConsumingConfiguration implements ConsumingConfiguration {

    private final AbstractOutboxPoller outboxPoller;

    SpringOutboxConsumingConfiguration(AbstractOutboxPoller outboxPoller) {
        this.outboxPoller = outboxPoller;
    }

    public AbstractOutboxPoller getOutboxPoller() {
        return outboxPoller;
    }
}
