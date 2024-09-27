package io.domainlifecycles.events.gruelbox.api;

import io.domainlifecycles.events.api.ConsumingConfiguration;
import io.domainlifecycles.events.consume.execution.handler.HandlerExecutor;
import io.domainlifecycles.events.gruelbox.dispatch.GruelboxDomainEventDispatcher;
import io.domainlifecycles.events.gruelbox.poll.GruelboxPoller;

class GruelboxConsumingConfiguration implements ConsumingConfiguration {

    private final GruelboxPoller gruelboxPoller;
    private final GruelboxDomainEventDispatcher gruelboxDomainEventDispatcher;
    private final HandlerExecutor usedHandlerExecutor;


    public GruelboxConsumingConfiguration(GruelboxPoller gruelboxPoller, GruelboxDomainEventDispatcher gruelboxDomainEventDispatcher, HandlerExecutor usedHandlerExecutor) {
        this.gruelboxPoller = gruelboxPoller;
        this.gruelboxDomainEventDispatcher = gruelboxDomainEventDispatcher;
        this.usedHandlerExecutor = usedHandlerExecutor;
    }
}
