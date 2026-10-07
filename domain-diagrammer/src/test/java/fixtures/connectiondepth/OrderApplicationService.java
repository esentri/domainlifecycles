package fixtures.connectiondepth;

import io.domainlifecycles.domain.types.ApplicationService;

public class OrderApplicationService implements ApplicationService {

    private final OrderDomainService orderDomainService;

    public OrderApplicationService(OrderDomainService orderDomainService) {
        this.orderDomainService = orderDomainService;
    }

    public void handle(PlaceOrder command) {
        orderDomainService.place(command.article());
    }
}
