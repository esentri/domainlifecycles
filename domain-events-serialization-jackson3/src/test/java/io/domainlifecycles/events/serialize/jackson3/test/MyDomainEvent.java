package io.domainlifecycles.events.serialize.jackson3.test;

import io.domainlifecycles.domain.types.DomainEvent;

public record MyDomainEvent(MyId id, MyVO vo) implements DomainEvent {
}
