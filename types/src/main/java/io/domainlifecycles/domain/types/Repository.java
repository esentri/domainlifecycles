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

import java.util.Optional;

/**
 * This is the common supertype (marker interface) to represent Repositories.
 * <p>
 * A Repository is responsible for accessing Aggregates from the datastore. It provides methods to query,
 * insert, modify or delete Aggregates. Repositories help to hide datastore specific complexity and thus keep
 * the domain implementation clean and unaffected by requirements of the underlying datastore technology.
 * </p>
 * <p>
 * Further
 * Information: <a href="https://www.domainlanguage.com/wp-content/uploads/2016/05/DDD_Reference_2015-03.pdf">link</a>
 * </p>
 *
 * @param <ID> the type of the AggregateRoot's Identity
 * @param <A>  the type of accessed AggregateRoot
 * @author Mario Herb
 */
public interface Repository <ID extends Identity<?>, A extends AggregateRoot<ID>> extends ServiceKind {

    Optional<A> findById(ID id);

    A insert(A aggregateRoot);

    A update(A aggregateRoot);

    Optional<A> deleteById(ID id);
}
