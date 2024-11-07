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
 * This is the common supertype (marker interface) to represent DomainEvents.
 * <p>DomainEvents are used to express something happened, that domain experts care about.
 * DomainEvents are often used to express domain rules, in form of a choreography:
 * "When the event A happened, then typically B has to do something". That way DomainEvents enable a good
 * separation of concerns for different Aggregates playing together with lesser coupling between them.
 * DomainEvents can be combined with techniques like Event Sourcing or CQRS, to be able to work with an explicit history
 * of what happened in the domain (audit trail) or to improve performance and scalability of the system.
 * Apart from pure technical advantages, the use of DomainEvents makes important side effects explicit within the domain
 * and thus improves the readability and understandability of the code.
 * <p>
 * <p>
 * Further
 * Information:
 *
 * @author Mario Herb
 * @see
 * <a href="https://www.domainlanguage.com/wp-content/uploads/2016/05/DDD_Reference_2015-03.pdf">Design Reference - Domain Events</a>
 * @see
 * <a href="https://learn.microsoft.com/de-de/dotnet/architecture/microservices/microservice-ddd-cqrs-patterns/domain-events-design-implementation">Microsoft DotNet Patterns (not Java but the concepts are described in a very nice way)</a>
 */
public interface DomainEvent extends Validatable {


}
