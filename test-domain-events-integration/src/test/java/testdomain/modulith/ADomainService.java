package testdomain.modulith;

import io.domainlifecycles.domain.types.DomainEventListener;
import io.domainlifecycles.domain.types.DomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;


@Slf4j
public class ADomainService implements DomainService {

    public static volatile int cnt=0;

    @DomainEventListener
    public void onDomainEvent(ADomainEvent domainEvent) {
        if (domainEvent.message().startsWith("DomainEventWithException")) {
            throw new RuntimeException("Provoked runtime error!");
        }
        log.debug("ADomainEvent received: {}", domainEvent);
        cnt++;
    }

    @DomainEventListener
    public void onDomainEvent(MulticastedDomainEvent domainEvent) {
        if (domainEvent.message().startsWith("DomainEventWithException")) {
            throw new RuntimeException("Provoked runtime error!");
        }
        log.debug("MulticastedDomainEvent received: {}", domainEvent);
        cnt++;
    }

    @EventListener
    @DomainEventListener
    public void onDomainEvent(A2ndDomainEvent domainEvent) {
        log.debug("A2ndDomainEvent received: {}", domainEvent);
        cnt++;
    }



}
