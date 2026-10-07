package fixtures.connectiondepth;

import java.util.Optional;

public class OrderRepositoryImpl implements OrderRepository {

    @Override
    public Optional<Order> findById(OrderId id) {
        return Optional.empty();
    }

    @Override
    public Order insert(Order aggregateRoot) {
        return aggregateRoot;
    }

    @Override
    public Order update(Order aggregateRoot) {
        return aggregateRoot;
    }

    @Override
    public Optional<Order> deleteById(OrderId id) {
        return Optional.empty();
    }
}
