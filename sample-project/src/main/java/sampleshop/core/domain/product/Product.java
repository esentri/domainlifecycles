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

package sampleshop.core.domain.product;

import io.domainlifecycles.assertion.DomainAssertions;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.base.AggregateRootBase;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import sampleshop.core.domain.Price;

import java.net.URI;
import java.util.Optional;

/**
 * Product aggregate root.
 *
 * @author Tobias Herb
 * @author Mario Herb
 */
@Getter
public final class Product extends AggregateRootBase<Product.ProductId> {

    /**
     * The "typed" ProductId as inner class (record) of the {@code Product} class.
     */
    public record ProductId(@NotNull Long value) implements Identity<Long> {
    }

    /**
     * The identity of this Product.
     */
    private final ProductId id;

    /**
     * The product description.
     */
    private Optional<@Size(max = 1000) String> description;

    /**
     * The name of this product.
     */
    @NotEmpty
    @Size(max = 200)
    private String name;

    /**
     * The image resource of this product.
     */
    private Optional<URI> image;

    /**
     * The price of this product.
     */
    @NotNull
    private Price price;

    @Builder
    private Product(final long concurrencyVersion,
                    final Product.ProductId id,
                    final String description,
                    final String name,
                    final URI image,
                    final Price price) {
        super(concurrencyVersion);
        this.id = id;
        this.description = Optional.ofNullable(description);
        this.name = name;
        this.image = Optional.ofNullable(image);
        this.price = price;
    }

    /**
     * A special validation, which can't be expressed by simple bean validation annotations.
     */
    @Override
    public void validate() {
        image.ifPresent(uri ->
            DomainAssertions.hasLength(
                uri.toString(),
                0,
                1000,
                "The product image URI must have less than 1000 characters."
            )
        );
    }
}
