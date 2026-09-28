package fixtures.flowmethods;

import io.domainlifecycles.domain.types.ApplicationService;

/**
 * The calls between the fixture's methods are simulated via a hand-built
 * {@link io.domainlifecycles.staticanalysis.DomainCalls} in the tests.
 */
public class OrderApplicationService implements ApplicationService {

    private OrderRepository orderRepository;
    private PricingService pricingService;
    private OrderOverviewQueryHandler overviewQueryHandler;

    // calls PricingService.price, Order.confirm and OrderRepository.store
    public void placeOrder(OrderId id) {
    }

    // calls OrderRepository.findOrder and Order.cancel
    public void cancelOrder(OrderId id) {
    }

    // calls OrderOverviewQueryHandler.find and OrderOverview.total
    public int report(OrderId id) {
        return 0;
    }
}
