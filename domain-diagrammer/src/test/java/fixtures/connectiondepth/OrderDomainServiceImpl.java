package fixtures.connectiondepth;

public class OrderDomainServiceImpl implements OrderDomainService {

    private final OrderRepository orderRepository;

    public OrderDomainServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void place(String article) {
        orderRepository.insert(new Order(new OrderId(1L), 0));
    }
}
