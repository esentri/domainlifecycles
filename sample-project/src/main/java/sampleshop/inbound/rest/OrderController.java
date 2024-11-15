/*
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

package sampleshop.inbound.rest;

import io.domainlifecycles.spring.http.ResponseEntityBuilder;
import io.domainlifecycles.spring.http.ResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sampleshop.core.domain.order.CancelOrder;
import sampleshop.core.domain.order.Order;
import sampleshop.core.domain.order.OrderStatus;
import sampleshop.core.domain.order.PlaceOrder;
import sampleshop.core.domain.order.ShipOrder;
import sampleshop.inbound.driver.OrderService;

import java.util.List;

/**
 * Implementation of the REST {@link OrderAPI} web controller.
 *
 * @author Mario Herb
 * @author Tobias Herb
 */
@RestController()
public class OrderController implements OrderAPI {

    private final OrderService orderDriver;
    private final ResponseEntityBuilder responseEntityBuilder;

    OrderController(OrderService orderDriver, ResponseEntityBuilder responseEntityBuilder) {
        this.orderDriver = orderDriver;
        this.responseEntityBuilder = responseEntityBuilder;
    }

    /**
     * Endpoint implementation to find existing Orders py status, paginated
     */
    @Override
    public ResponseEntity<ResponseObject<List<Order>>> find(
        @RequestParam(value = "offset", defaultValue = "0") int offset,
        @RequestParam(value = "limit", defaultValue = "10") int limit,
        @RequestParam(value = "status") OrderStatus... orderStatuses) {
        return responseEntityBuilder.build(orderDriver.find(offset, limit, orderStatuses), HttpStatus.OK);
    }

    /**
     * Endpoint implementation to place a new Order
     */
    @Override
    public ResponseEntity<ResponseObject<Order>> place(
        @RequestBody PlaceOrder placeOrder
    ) {
        return responseEntityBuilder.build(orderDriver.place(placeOrder), HttpStatus.CREATED);
    }

    /**
     * Endpoint implementation to cancel an existing Order
     */
    @Override
    public ResponseEntity<ResponseObject<Order>> cancel(
        @PathVariable("id") Order.OrderId id) {
        return responseEntityBuilder.build(orderDriver.cancel(new CancelOrder(id)).orElse(null), HttpStatus.OK);
    }

    /**
     * Endpoint implementation to ship an existing Order
     */
    @Override
    public ResponseEntity<ResponseObject<Order>> ship(
        @PathVariable("id") Order.OrderId id) {
        return responseEntityBuilder.build(orderDriver.ship(new ShipOrder(id)).orElse(null), HttpStatus.OK);
    }
}
