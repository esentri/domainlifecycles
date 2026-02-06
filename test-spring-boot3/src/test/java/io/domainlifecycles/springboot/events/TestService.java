package io.domainlifecycles.springboot.events;

import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.domain.types.DomainEventListener;
import io.domainlifecycles.domain.types.DomainService;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TestService implements DomainService {

    public Queue<DomainEvent> received = new ConcurrentLinkedQueue<>();

    @DomainEventListener
    public void doSomething(TestEvent event) {
        received.add(event);
    }

}
