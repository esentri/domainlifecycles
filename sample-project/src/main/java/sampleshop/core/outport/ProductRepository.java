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

package sampleshop.core.outport;

import io.domainlifecycles.domain.types.Repository;
import sampleshop.core.domain.product.Product;

import java.util.Optional;

/**
 * Defines {@link Product} persistence operations.
 *
 * @author Tobias Herb
 * @author Mario Herb
 */
public interface ProductRepository extends Repository<Product.ProductId, Product> {

    /**
     * Produces a fresh {@code Product.ProductId}.
     */
    Product.ProductId newProductId();

    /**
     * Stores an new {@code Product} into this repository.
     *
     * @param entity to to be stored
     * @return updated {@code entity} instance 
     */
    @Override
    Product insert(Product entity);

    /**
     * Updates an existent {@code Product} in this repository.
     *
     * @param entity to to be updated
     * @return updated {@code entity} instance 
     */
    @Override
    Product update(Product entity);

    /**
     * Removes all products from the database
     */
    void removeAll();

    /**
     * Finds a product by its ID.
     *
     * @param productId the*/
    @Override
    Optional<Product> findById(Product.ProductId productId);

    /**
     * Deletes a product with the given product ID from the repository.
     *
     * @param productId the ID of the product to delete
     * @return an optional containing the deleted product if it exists, otherwise an empty optional
     */
    @Override
    Optional<Product> deleteById(Product.ProductId productId);
}
