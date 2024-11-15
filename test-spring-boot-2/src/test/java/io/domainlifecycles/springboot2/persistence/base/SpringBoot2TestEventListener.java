package io.domainlifecycles.springboot2.persistence.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import tests.shared.events.PersistenceEvent;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
public class SpringBoot2TestEventListener {

    public List<PersistenceEvent> received = new ArrayList<>();

    public SpringBoot2TestEventListener() {
    }

    @EventListener
    public void event(PersistenceEvent event) {
        log.debug("Event received: " + event);
        received.add(event);
    }

}
