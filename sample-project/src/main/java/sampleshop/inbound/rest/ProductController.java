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
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sampleshop.core.domain.product.Product;
import sampleshop.inbound.driver.ProductService;

/**
 * Implementation of the REST {@link ProductAPI} web controller.
 *
 * @author Mario Herb
 * @author Tobias Herb
 */
@RestController()
public class ProductController implements ProductAPI {

    private final ProductService productDriver;
    private final ResponseEntityBuilder responseEntityBuilder;

    ProductController(ProductService productDriver, ResponseEntityBuilder responseEntityBuilder) {
        this.productDriver = productDriver;
        this.responseEntityBuilder = responseEntityBuilder;
    }

    /**
     * Endpoint implementation for creating new products.
     */
    @Override
    public ResponseEntity<ResponseObject<Product>> create(
        @NotNull @RequestBody Product product
    ) {
        return responseEntityBuilder.build(productDriver.create(product), HttpStatus.CREATED);
    }

    /**
     * Endpoint implementation to GET a product by id.
     */
    @Override
    public ResponseEntity<ResponseObject<Product>> find(
        @RequestParam("id") Product.ProductId id
    ) {
        return responseEntityBuilder.build(productDriver.find(id).orElse(null), HttpStatus.OK);
    }

    /**
     * Endpoint implementation to DELETE all products
     */
    @Override
    public void removeAll() {
        productDriver.removeAll();
    }
}
