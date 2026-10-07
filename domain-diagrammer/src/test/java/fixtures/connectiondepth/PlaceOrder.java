package fixtures.connectiondepth;

import io.domainlifecycles.domain.types.DomainCommand;

public record PlaceOrder(String article) implements DomainCommand {
}
