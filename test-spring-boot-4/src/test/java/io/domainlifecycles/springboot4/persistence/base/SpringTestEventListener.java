package io.domainlifecycles.springboot4.persistence.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import tests.shared.events.PersistenceEvent;


@Slf4j
@Component
public class SpringTestEventListener {


    public SpringTestEventListener() {
    }

    @EventListener
    public void event(PersistenceEvent event) {
        log.debug("Event received: " + event);
    }

}
