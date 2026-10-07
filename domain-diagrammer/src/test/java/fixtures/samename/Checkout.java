package fixtures.samename;

import io.domainlifecycles.domain.types.ApplicationService;

/**
 * Uses the order services of the same name, and has a name of its own.
 */
public class Checkout implements ApplicationService {

    private fixtures.samename.billing.OrderService billing;
    private fixtures.samename.shipping.OrderService shipping;

    public void checkOut() {
    }
}
