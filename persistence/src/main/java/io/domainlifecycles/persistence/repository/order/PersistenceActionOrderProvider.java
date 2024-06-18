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

package io.domainlifecycles.persistence.repository.order;

import io.domainlifecycles.domain.types.internal.DomainObject;

import java.util.List;

/**
 * A PersistenceActionOrderProvider provides the order in which {@link DomainObject}s (=entities, value objects) have to be persisted.
 *
 * @author Mario Herb
 */
public interface PersistenceActionOrderProvider {

    /**
     * Returns the order in which {@link DomainObject}s have to be persisted.
     *
     * @param aggregateRootClassName the full qualified aggregate root class name
     * @return the order in which {@link DomainObject}s have to be persisted
     */
    List<String> insertionOrder(String aggregateRootClassName);

    /**
     * Returns the order in which {@link DomainObject}s have to be deleted.
     *
     * @param aggregateRootClassName the full qualified aggregate root class name
     * @return the order in which {@link DomainObject}s have to be deleted
     */
    List<String> deletionOrder(String aggregateRootClassName);

}
