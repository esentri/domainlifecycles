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

package tests.shared.complete.ecommerce.order;

import io.domainlifecycles.assertion.DomainAssertionException;
import io.domainlifecycles.assertion.DomainAssertions;
import io.domainlifecycles.domain.types.Publishes;
import io.domainlifecycles.domain.types.base.AggregateRootBase;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import tests.shared.complete.ecommerce.delivery.DeliveryStarted;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderBv3 extends AggregateRootBase<OrderIdBv3> {

    @NotNull
    private OrderIdBv3 id;

    @NotNull
    @DecimalMax(value = "3.0")
    @DecimalMin(value = "1.0")
    private Byte priority;

    @NotNull
    private CustomerNumberBv3 customerNumber;

    @NotNull
    private DeliveryAddressBv3 deliveryAddress;

    @NotNull
    @Size(min = 1)
    private final List<OrderItemBv3> orderItems;

    @NotNull
    private OrderStatusBv3 orderStatus;

    @NotNull
    private PriceBv3 totalPrice;

    @NotNull
    private final List<OrderCommentBv3> orderComments;
    private List<PromoCodeBv3> promoCodes;

    @Builder(setterPrefix = "set")
    public OrderBv3(OrderIdBv3 id,
                         long concurrencyVersion,
                         Byte priority,
                         DeliveryAddressBv3 deliveryAddress,
                         CustomerNumberBv3 customerNumber,
                         List<OrderItemBv3> orderItems,
                         OrderStatusBv3 orderStatus,
                         List<OrderCommentBv3> orderComments,
                         List<PromoCodeBv3> promoCodes,
                         PriceBv3 totalPrice
    ) {
        super(concurrencyVersion);
        this.id = id;
        this.priority = priority;
        this.customerNumber = customerNumber;
        this.orderItems = orderItems;
        this.orderStatus = orderStatus;
        this.orderComments = orderComments;
        this.promoCodes = promoCodes;
        this.deliveryAddress = deliveryAddress;
        calculateTotalPrice();

    }

    public void setPriority(Byte priority) {
        this.priority = priority;
    }

    public void setCustomerNumber(CustomerNumberBv3 customerNumber) {
        this.customerNumber = customerNumber;
    }

    private void calculateTotalPrice() {
        var totalPriceAmount = BigDecimal.ZERO;
        var currency = orderItems.get(0).getUnitPrice().currency();
        for (OrderItemBv3 p : orderItems) {
            totalPriceAmount = totalPriceAmount.add(
                p.getUnitPrice().amount().multiply(BigDecimal.valueOf(p.getQuantity())));
        }
        this.totalPrice = new PriceBv3(currency, totalPriceAmount);
    }

    public void setOrderStatus(OrderStatusBv3 orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void addOrderComment(OrderCommentBv3 comment) {
        this.orderComments.add(comment);
    }

    public void removeOrderComment(OrderCommentBv3 comment) {
        this.orderComments.remove(comment);
    }

    public void addOrderItem(OrderItemBv3 item) {
        this.orderItems.add(item);
        calculateTotalPrice();
    }

    public void removeOrderItem(OrderItemBv3 item) {
        this.orderItems.remove(item);
        calculateTotalPrice();
    }

    public void setDeliveryAddress(DeliveryAddressBv3 deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    @Override
    public void validate() throws DomainAssertionException {
        OrderItemBv3 firstItem = orderItems.get(0);
        long itemsWithSameCurrency = orderItems.stream()
            .map(p -> p.getUnitPrice())
            .filter(p -> firstItem.getUnitPrice().currency().equals(p.currency()))
            .count();
        DomainAssertions.isTrue(itemsWithSameCurrency == orderItems.size(),
            "Alle Bestellpositionen müssen dieselbe Währung haben!");
    }

    public void setPromoCodes(List<PromoCodeBv3> promoCodes) {
        this.promoCodes = promoCodes;
    }

    @Publishes(domainEventTypes = DeliveryStarted.class)
    public void startDelivery() {
        getOrderStatus().setStatusChangedAt(LocalDateTime.now());
        getOrderStatus().setStatusCode(OrderStatusCodeEnumBv3.DELIVERY_IN_PROGRESS);
        DeliveryStarted event = new DeliveryStarted(this, false);
        //Domain.publish(event);
    }

}
