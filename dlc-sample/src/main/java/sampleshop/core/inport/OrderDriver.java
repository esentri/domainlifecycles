package sampleshop.core.inport;

import nitrox.dlc.domain.types.Driver;
import sampleshop.core.domain.order.CancelOrder;
import sampleshop.core.domain.order.Order;
import sampleshop.core.domain.order.OrderStatus;
import sampleshop.core.domain.order.PlaceOrder;
import sampleshop.core.domain.order.ShipOrder;
import sampleshop.core.domain.product.Product;

import java.util.List;
import java.util.Optional;

/**
 * The OrderDriver contains all operations driving the application
 * in association with {@link Order}s.
 *
 * @author Mario Herb
 */
public interface OrderDriver extends Driver {

    /**
     * Cancels a pending order.
     *
     * @param cancelOrder {@link CancelOrder}
     * @return canceled {@code Order}
     */

    Optional<Order> cancel(CancelOrder cancelOrder);

    /**
     * Marks the order as shipped what triggers legal archiving.
     *
     * @param shipOrder {@link ShipOrder}
     * @return shipped {@code Order}
     */

    Optional<Order> ship(final ShipOrder shipOrder);

    /**
     * Creates a new {@link Order} for the {@link Product}s specified
     * by the passed {@code items} map.
     *
     * @param placeOrder order to be placed
     * @return a fresh {@code Order}
     */
    Order place(PlaceOrder placeOrder);

    /**
     * Finds existing orders filtered by status codes.
     * @param offset for paging
     * @param limit for paging
     * @param orderStatus to be filtered
     * @return found {@code Order}
     */
    List<Order> find(int offset, int limit, OrderStatus... orderStatus);
}
