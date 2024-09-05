package io.domainlifecycles.events.gruelbox.idempotent;

import io.domainlifecycles.domain.types.DomainEvent;

import java.util.Objects;

public record IdempotencyConfigurationEntry(Class<?> handlerClass,
                                            String methodName,
                                            Class<? extends DomainEvent> domainEventClass,
                                            IdempotencyFunction idempotencyFunction)
{
    public IdempotencyConfigurationEntry(Class<?> handlerClass, String methodName, Class<? extends DomainEvent> domainEventClass, IdempotencyFunction idempotencyFunction) {
        this.handlerClass = Objects.requireNonNull(handlerClass, "A handlerClass is required!");
        this.methodName = Objects.requireNonNull(methodName, "A handlerMethod is required!");
        this.domainEventClass = Objects.requireNonNull(domainEventClass, "A Domain Event class is required!");
        this.idempotencyFunction = Objects.requireNonNull(idempotencyFunction, "An idempotency function must be defined!");
    }
}
