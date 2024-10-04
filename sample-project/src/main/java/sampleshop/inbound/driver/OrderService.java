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

package sampleshop.inbound.driver;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sampleshop.core.domain.order.CancelOrder;
import sampleshop.core.domain.order.Order;
import sampleshop.core.domain.order.OrderPlacementService;
import sampleshop.core.domain.order.OrderStatus;
import sampleshop.core.domain.order.PlaceOrder;
import sampleshop.core.domain.order.ShipOrder;

import sampleshop.core.inport.OrderDriver;
import sampleshop.core.outport.OrderRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The OrderService implements the OrderDriver.
 *
 * @author Mario Herb
 * @author Tobias Herb
 */
@Service
@Transactional
public class OrderService implements OrderDriver {

    private final OrderPlacementService orderPlacementService;

    private final OrderRepository orderRepository;

    public OrderService(
        final OrderPlacementService orderPlacementService,
        final OrderRepository orderRepository
    ) {
        this.orderPlacementService = orderPlacementService;
        this.orderRepository = orderRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Order> cancel(CancelOrder cancelOrder) {
        var canceled = orderRepository
            .findById(cancelOrder.orderId())
            .map(order ->
                orderRepository.update(order.cancel())
            );
        return canceled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Order> ship(final ShipOrder shipOrder) {
        var shipped = orderRepository
            .findById(shipOrder.orderId())
            .map(order ->
                orderRepository.update(order.ship())
            );
        return shipped;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Order place(PlaceOrder placeOrder) {
        return orderPlacementService.placeOrder(placeOrder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Order> find(int offset, int limit, OrderStatus... orderStatus) {
        return orderRepository.find(offset, limit, orderStatus)
            .collect(Collectors.toList());
    }


}
