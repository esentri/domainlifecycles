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

/**
 * This is the common supertype (marker interface) to represent DomainCommand.
 *
 * <p>
 * DomainCommands should be used to express that someone (e.g. a user or an external system) wishes that something should happen in the domain.
 * DomainCommands can be rejected (in contrast to DomainEvents). DomainCommands play an important role in the modification of Aggregates.
 * They protect Aggregate boundaries, because command handlers only allow distinct mutations on Aggregates in contrast to, for example,
 * services with general `update(MyAggregate a)` methods. Moreover, they improve the readability and understandability of the code,
 * because they explicitly describe the intentions of actions that can be requested and generally performed on Aggregates.
 * Furthermore, they can reduce the amount of parameters needed at methods that implement the corresponding action.
 *</p>
 *
 * @author Mario Herb
 */
public interface DomainCommand extends Validatable{
}
