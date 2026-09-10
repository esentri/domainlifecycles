package test.domain;

import io.domainlifecycles.domain.types.DomainCommand;

public record MyDomainCommand(MyAggregateRoot.Id id) implements DomainCommand {
}
