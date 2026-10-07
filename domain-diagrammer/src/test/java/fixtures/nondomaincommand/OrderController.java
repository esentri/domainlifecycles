package fixtures.nondomaincommand;

/**
 * A controller - no domain type - receiving the commands and forwarding {@link PlaceOrder} to the application service.
 */
public class OrderController {

    private final OrderApplicationService orderApplicationService;

    public OrderController(OrderApplicationService orderApplicationService) {
        this.orderApplicationService = orderApplicationService;
    }

    public void place(PlaceOrder command) {
        orderApplicationService.place(command);
    }

    public void cancel(CancelOrder command) {
    }
}
