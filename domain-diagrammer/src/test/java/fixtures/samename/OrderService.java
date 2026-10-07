package fixtures.samename;

import io.domainlifecycles.domain.types.DomainService;

/**
 * Shares its name with {@link fixtures.samename.billing.OrderService} and {@link fixtures.samename.shipping.OrderService},
 * right in the package the other two share.
 */
public class OrderService implements DomainService {

    public void order() {
    }
}
