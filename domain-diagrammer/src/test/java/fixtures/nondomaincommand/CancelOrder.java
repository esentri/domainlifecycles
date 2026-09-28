package fixtures.nondomaincommand;

import io.domainlifecycles.domain.types.DomainCommand;

/**
 * Only received by {@link OrderController}, no service processes it.
 *
 * @param orderNumber the order to cancel
 */
public record CancelOrder(String orderNumber) implements DomainCommand {
}
