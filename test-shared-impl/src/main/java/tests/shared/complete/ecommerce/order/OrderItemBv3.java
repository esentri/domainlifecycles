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

import io.domainlifecycles.domain.types.base.EntityBase;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderItemBv3 extends EntityBase<OrderItemIdBv3> {

    @NotNull
    private OrderItemIdBv3 id;

    @NotNull
    private ArticleIdBv3 articleId;

    @NotNull
    private PriceBv3 unitPrice;

    @Positive
    private int quantity;


    @Builder(setterPrefix = "set")
    public OrderItemBv3(OrderItemIdBv3 id,
                              long concurrencyVersion,
                              ArticleIdBv3 articleId,
                              PriceBv3 unitPrice,
                              int quantity
    ) {
        super(concurrencyVersion);
        this.id = id;
        this.articleId = articleId;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public void setArticleId(ArticleIdBv3 articleId) {
        this.articleId = articleId;
    }

    public void setUnitPrice(PriceBv3 unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
