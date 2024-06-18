/*
 *
 *     ___
 *     │   ╲                 _
 *     │    ╲ ___ _ __  __ _(_)_ _
 *     |     ╲ _ ╲ '  ╲╱ _` │ │ ' ╲
 *     |_____╱___╱_│_│_╲__,_│_│_||_|
 *     │ │  (_)╱ _│___ __ _  _ __│ |___ ___
 *     │ │__│ │  _╱ -_) _│ ││ ╱ _│ ╱ -_|_-<
 *     │____│_│_│ ╲___╲__│╲_, ╲__│_╲___╱__╱
 *                      |__╱
 *
 *  Copyright 2019-2024 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package sampleshop.core.inport;

import io.domainlifecycles.domain.types.Driver;
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
