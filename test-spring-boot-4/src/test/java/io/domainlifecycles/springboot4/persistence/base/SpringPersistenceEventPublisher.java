package io.domainlifecycles.springboot4.persistence.base;

import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import io.domainlifecycles.persistence.repository.actions.PersistenceAction;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import tests.shared.events.PersistenceEvent;


/**
 * The event publisher separates event dispatch mechanics
 * from the client logic that triggers a persistence event.
 */
@Slf4j
@Component
public final class SpringPersistenceEventPublisher implements PersistenceEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public SpringPersistenceEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publish(@NonNull PersistenceAction pa) {
        final PersistenceEvent event = assembleEvent(pa);
        log.debug("Publish persistence event: {}", event);
        applicationEventPublisher.publishEvent(event);
    }

    /**
     * Assembles a persistence event object for the given domain entity, tagged with the
     * given event type that refers to the kind of CUD operation that was applied on
     * the entity. The remaining event parameters are taken from the current activation
     * context.
     *
     * @return a new persistence event.
     */
    private PersistenceEvent assembleEvent(final PersistenceAction persistenceAction) {
        PersistenceEvent.PersistenceEventType eventType = PersistenceEvent.PersistenceEventType.INSERTED;
        if (persistenceAction.actionType.equals(
            PersistenceAction.ActionType.UPDATE) || persistenceAction.actionType.equals(
            PersistenceAction.ActionType.DELETE_UPDATE)) {
            eventType = PersistenceEvent.PersistenceEventType.UPDATED;
        } else if (persistenceAction.actionType.equals(PersistenceAction.ActionType.DELETE)) {
            eventType = PersistenceEvent.PersistenceEventType.DELETED;
        }

        return new PersistenceEvent(persistenceAction.instanceAccessModel, eventType);
    }
}
