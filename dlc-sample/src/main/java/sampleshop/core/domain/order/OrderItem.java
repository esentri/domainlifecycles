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

package sampleshop.core.domain.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import nitrox.dlc.domain.types.Identity;
import nitrox.dlc.domain.types.base.EntityBase;
import sampleshop.core.domain.Price;
import sampleshop.core.domain.product.Product;


/**
 * Order item entity.
 *
 * @author Mario Herb
 */
@Getter
public final class OrderItem extends EntityBase<OrderItem.OrderItemId> {

    /**
     * The "typed" OrderItemId as inner class (record) of the {@code OrderItem} class.
     * @param value
     */
    public record OrderItemId(@NotNull Long value) implements Identity<Long> { }

    /**
     * The identity of this item.
     */
    private final OrderItemId id;

    /**
     * The product to be ordered.
     */
    @NotNull
    private final Product.ProductId productId;

    /**
     * The price of the ordered product.
     */
    @NotNull
    private final Price productPrice;

    /**
     * The quantity of the product.
     */
    @Positive
    private int quantity;

    @Builder(setterPrefix = "set")
    private OrderItem(final long concurrencyVersion,
                      final OrderItem.OrderItemId id,
                      final Product.ProductId productId,
                      final Price productPrice,
                      final int quantity) {
        super(concurrencyVersion);
        this.id = id;
        this.productId = productId;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    /**
     * Calculates the price for this OrderItem.
     */
    public Price itemPrice() {
        return productPrice.multiply(quantity);
    }

    /**
     * Increase the quantity ordered.
     */
    public OrderItem addQuantity(int quantity){
        this.quantity += quantity;
        return this;
    }

}
