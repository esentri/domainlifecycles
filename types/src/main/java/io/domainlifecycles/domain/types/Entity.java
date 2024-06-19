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

package io.domainlifecycles.domain.types;

import io.domainlifecycles.domain.types.internal.ConcurrencySafe;
import io.domainlifecycles.domain.types.internal.DomainObject;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is the common supertype to represent a entity according to the Domain
 * Driven Design approach.
 * <p>
 * Entities are domain objects defined primarily by their identity, continuity,
 * and persistence over time, not just by the attributes that compose them. As
 * Eric Evans says, "An object that is defined primarily by its identity is
 * called an entity." Entities form the basis for a model. Therefore, it is
 * strongly recommended to identify and design them carefully.
 * </p>
 * <p>
 * Entities implement behavior in addition to data attributes. Typically, an
 * entity implements domain logic or behavior related to the entity attributes.
 * A entity must implement domain logic or behavior related to the attributes.
 * For example, as part of an entity class for an order, you must implement
 * business logic and operations as methods for tasks such as adding an order
 * item, data validation, and total calculation. The entity's methods take care
 * of the entity's invariants and rules, rather than distributing those rules
 * through the service layer.
 * </p>
 * <p>
 * For more information, read about
 * <a href="https://www.domainlanguage.com/wp-content/uploads/2016/05/DDD_Reference_2015-03.pdf">
 * Entities</a>.
 * </p>
 * @param <ID> type of entity identifier
 *
 * @author Tobias Herb
 * @author Mario Herb
 */
public interface Entity<ID extends Identity<?>> extends DomainObject, Validatable, ConcurrencySafe {

    /**
     * Returns the value of the assigned entity identifier.
     */
    ID id();

    /**
     * Returns the "version" of this entity according to the underlying optimistic
     * concurrency model.
     * <p>
     * The version is tested against the connected persistent data store whenever
     * the aggregate is being written back. If the version is out of sync (i.e.
     * someone else has written to the store while this operation was processing),
     * then the store operation fails.
     *
     * @return current version of this entity
     */
    @Override
    long concurrencyVersion();

    // ----------------------------------------------------------
    //  ENTITY.@ID
    // ----------------------------------------------------------

    /**
     * Indicates the {@link Identity}-field that provides the identity in case
     * of ambiguous field candidates (due to the same type).
     * <p>
     * A typical scenario are self-referencing hierarchical arrangements, for
     * instance by using a self-referencing key in a table that identifies the
     * parent-child relationship.
     */
    @Documented
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Id { }
}
