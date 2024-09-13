package io.domainlifecycles.events.mq.gruelbox;

import io.domainlifecycles.events.mq.publish.MqDomainEventPublisher;
import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.gruelbox.dispatch.GruelboxDomainEventDispatcher;

/**
 * The {@code MqGruelboxDomainEventDispatcher} class is an implementation of the {@link GruelboxDomainEventDispatcher}
 * interface. It is responsible for dispatching domain events to a topic of a message broker.
 * In this case the Gruelbox acts as an outbox proxy for DomainEvents that are distributed via a message broker.
 *
 * Gruelbox ensures that the events are transactionally consistent,
 * that means no ghost events occur or events cannot be lost.
 *
 *
 * <p>This class must be extended and the {@link #dispatch(DomainEvent)} method must be implemented.
 *
 * <p><b>Note:</b> It is important to ensure that a non-null domainEvent is provided to the
 * {@link #dispatch(DomainEvent)} method.
 *
 * {@inheritDoc}
 */
public class MqGruelboxDomainEventDispatcher implements GruelboxDomainEventDispatcher {

    private final MqDomainEventPublisher mqDomainEventPublisher;

    public MqGruelboxDomainEventDispatcher(MqDomainEventPublisher mqDomainEventPublisher) {
        this.mqDomainEventPublisher = mqDomainEventPublisher;
    }

    /**
     * This method is responsible for dispatching a domain event.
     * It takes a domain event as a parameter and publishes it using the MqDomainEventPublisher.
     *
     * @param domainEvent the domain event to dispatch
     */
    @Override
    public void dispatch(DomainEvent domainEvent) {
        mqDomainEventPublisher.publish(domainEvent);
    }
}
