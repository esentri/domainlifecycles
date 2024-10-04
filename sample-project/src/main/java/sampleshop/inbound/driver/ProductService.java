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
import sampleshop.core.domain.product.Product;
import sampleshop.core.inport.ProductDriver;
import sampleshop.core.outport.ProductRepository;

import java.util.Optional;

/**
 * The ProductService implements the ProductDriver.
 *
 * @author Mario Herb
 * @author Tobias Herb
 */
@Service
@Transactional
public class ProductService implements ProductDriver {

    private final ProductRepository repository;

    public ProductService(final ProductRepository repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Product create(Product product) {
        return repository.insert(product);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAll() {
        repository.removeAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Product> find(Product.ProductId productId) {
        return repository.findById(productId);
    }
}
