package io.domainlifecycles.events.gruelbox.idempotent;

import io.domainlifecycles.domain.types.DomainEvent;

import java.util.Objects;

public record IdempotentExecutionContext(Class<?> handlerClass,
                                         String handlerMethod,
                                         DomainEvent domainEvent) {
    public IdempotentExecutionContext(Class<?> handlerClass, String handlerMethod, DomainEvent domainEvent) {
        this.handlerClass = Objects.requireNonNull(handlerClass, "handlerClass required!");
        this.handlerMethod = Objects.requireNonNull(handlerMethod, "handlerMethod required!");
        this.domainEvent = Objects.requireNonNull(domainEvent, "DomainEvent required!");

    }


}
