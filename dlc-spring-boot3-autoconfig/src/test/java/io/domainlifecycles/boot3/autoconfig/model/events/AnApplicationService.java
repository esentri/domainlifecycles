package io.domainlifecycles.boot3.autoconfig.model.events;

import io.domainlifecycles.domain.types.ApplicationService;
import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.domain.types.DomainEventListener;
import lombok.extern.slf4j.Slf4j;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
public class AnApplicationService implements ApplicationService {

    public Queue<DomainEvent> received = new ConcurrentLinkedQueue<>();

    @DomainEventListener
    public void onADomainEvent(ADomainEvent domainEvent){
        log.debug("ADomainEvent received in AnApplicationService! Message = " + domainEvent.message());
        received.add(domainEvent);
    }

}
