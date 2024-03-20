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

package nitrox.dlc.events.receive.execution.detector;

import nitrox.dlc.domain.types.AggregateDomainEvent;
import nitrox.dlc.domain.types.AggregateRoot;
import nitrox.dlc.domain.types.Identity;
import nitrox.dlc.domain.types.Repository;
import nitrox.dlc.events.receive.execution.detector.ExecutionContext;

/**
 * Represents the execution context for a domain event handler within an {@link AggregateRoot}.
 *
 * It holds the aggregate repository instance needed to access the AggregateRoot,
 * the handler method name of the method executed on the AggregateRoot instance, and the aggregate domain event being dispatched.
 *
 * @author Mario Herb
 */
public record AggregateExecutionContext<I extends Identity<?>, A extends AggregateRoot<I>>(Repository<I,A> aggregateRepository, String aggregateHandlerMethodName, AggregateDomainEvent<I,A> domainEvent) implements ExecutionContext {
}
