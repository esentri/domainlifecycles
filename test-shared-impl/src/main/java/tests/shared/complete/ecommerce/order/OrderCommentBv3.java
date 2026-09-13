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
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class OrderCommentBv3 extends EntityBase<OrderCommentIdBv3> {

    @NotNull
    private final OrderCommentIdBv3 id;

    @Size(max = 1000)
    @NotEmpty
    private String commentText;

    @NotNull
    @Past
    private LocalDateTime commentedAt;


    @Builder(setterPrefix = "set")
    public OrderCommentBv3(OrderCommentIdBv3 id,
                               long concurrencyVersion,
                               String commentText,
                               LocalDateTime commentedAt
    ) {
        super(concurrencyVersion);
        this.id = id;
        this.commentText = commentText;
        this.commentedAt = commentedAt;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public void setCommentedAt(LocalDateTime commentedAt) {
        this.commentedAt = commentedAt;
    }

}
