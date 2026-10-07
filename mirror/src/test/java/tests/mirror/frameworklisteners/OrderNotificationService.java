package tests.mirror.frameworklisteners;

import io.domainlifecycles.domain.types.ApplicationService;
import org.springframework.context.event.EventListener;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.transaction.event.TransactionalEventListener;

public class OrderNotificationService implements ApplicationService {

    @EventListener
    public void onEvent(OrderPlaced event) {
    }

    @TransactionalEventListener
    public void afterCommit(OrderPlaced event) {
    }

    @ApplicationModuleListener
    public void inModule(OrderPlaced event) {
    }

    @AsyncOrderListener
    public void composed(OrderPlaced event) {
    }

    @EventListener(classes = OrderPlaced.class)
    public void eventNamedByAnnotation() {
    }

    /** Not a domain event - Spring may still deliver it, but it is no part of the domain's flows. */
    @EventListener
    public void onText(String text) {
    }

    /** No listener at all. */
    public void notify(OrderPlaced event) {
    }
}
