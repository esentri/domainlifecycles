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

package sampleshop.configuration.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sampleshop.core.domain.order.OrderPlacementService;
import sampleshop.core.outport.OrderRepository;
import sampleshop.core.outport.ProductRepository;

/**
 * Spring wiring of all domain model related components.
 *
 * @author Tobias Herb
 * @author Mario Herb
 */
@Configuration
public class SampleShopConfiguration {

    @Bean
    public OrderPlacementService orderPlacementService(final OrderRepository orderRepository,
                                                       final ProductRepository productRepository) {
        return new OrderPlacementService(orderRepository, productRepository);
    }

}
