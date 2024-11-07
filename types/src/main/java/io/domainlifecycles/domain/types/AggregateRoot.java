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

package io.domainlifecycles.domain.types;

/**
 * This is the common supertype to represent a aggregate root according to the
 * Domain Driven Design approach.
 * <p>
 * An aggregate describes a cluster or group of entities that can be treated as
 * a coherent unit. An aggregate is defined over transaction boundaries, i.e. it
 * groups entities that are to be treated as a consistent unit. A classic example
 * is an order that also contains a list of order items. An order item is normally
 * an entity. However, it is a child entity within the order aggregate, which also
 * contains the order entity as a root entity, usually referred to as the aggregate
 * root.
 * <p>
 * Identifying aggregates can be hard. An aggregate is a group of objects that
 * must be consistent together, but you can't just pick a group of objects and
 * call it an aggregate. One must start from a domain concept and consider which
 * entities are used in the most common transactions related to that concept.
 * These entities, which must be transactional consistent, then form an aggregate.
 * The best way to identify aggregates is probably to think about transactional
 * operations.
 * <p>
 * Cluster the entities and value objects into aggregates and define
 * boundaries around each.
 * Choose one entity to be the root of each aggregate, allow external
 * objects to hold references to the root only (references to internal members
 * passed out for use within a single operation only).
 * Define properties and invariants for the aggregate as a whole and give
 * enforcement responsibility to the root or some designated framework mechanism.
 * Use the same aggregate boundaries to govern transactions and distribution.
 * Within an aggregate boundary, apply consistency rules synchronously.
 * Across boundaries, handle updates asynchronously.
 * <p>
 * For more information, read about
 *
 * @param <ID> type of aggregate identifier
 * @author Tobias Herb
 * @author Mario Herb
 * @see <a href="https://www.domainlanguage.com/wp-content/uploads/2016/05/DDD_Reference_2015-03.pdf">
 * Aggregates</a>.
 */
public interface AggregateRoot<ID extends Identity<?>> extends Entity<ID> {
}

