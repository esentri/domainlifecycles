package io.domainlifecycles.events.springbus;

import io.domainlifecycles.events.ADomainEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ListenerService {

    private int cnt = 0;

    @EventListener
    public void listen(ADomainEvent event) {
        log.debug("Received ADomainEvent: {}", event);
        cnt++;
    }

    public int getCnt() {
        return cnt;
    }
}
