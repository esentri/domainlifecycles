package io.domainlifecycles.autoconfig.model.events;

import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.domain.types.DomainEventListener;
import io.domainlifecycles.domain.types.QueryHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
public class AQueryHandler implements QueryHandler {

    public Queue<DomainEvent> received = new ConcurrentLinkedQueue<>();

    @DomainEventListener
    public void onADomainEvent(ADomainEvent domainEvent) {
       log.debug("ADomainEvent received in AQueryHandler! Message = " + domainEvent.message());
        received.add(domainEvent);
    }

}
