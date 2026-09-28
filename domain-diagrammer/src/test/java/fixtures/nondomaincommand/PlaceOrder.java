package fixtures.nondomaincommand;

import io.domainlifecycles.domain.types.DomainCommand;

public record PlaceOrder(String orderNumber) implements DomainCommand {
}
