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

package io.domainlifecycles.events.gruelbox.dispatch;

import com.gruelbox.transactionoutbox.Instantiator;
import io.domainlifecycles.events.gruelbox.api.GruelboxDomainEventsConfiguration;
import io.domainlifecycles.events.gruelbox.idempotent.IdempotentExecutor;
import io.domainlifecycles.events.receive.execution.processor.ExecutionContextProcessor;

import java.util.Objects;

/**
 * The DomainEventsInstantiator class is an implementation of the Instantiator interface.
 * It is responsible for instantiating instances of the GruelboxDomainEventDispatcher class or
 * in case of idempotency protection the IdempotentExecutor delegate class.
 *
 * The instantiator is used by the Gruelbox implementation to create instances for the scheduled
 * event processing calls. By using this instantiator only outbox entries which contain scheduling calls refering
 * to a GruelboxEventDispatcher can be processed.
 *
 *
 * @author Mario Herb
 */
public final class DomainEventsInstantiator implements Instantiator {

    /**
     * Retrieves the name of the given class.
     * If the class is assignable from GruelboxDomainEventDispatcher, it returns the name of GruelboxDomainEventDispatcher class.
     * If the class is assignable from IdempotentExecutor, it returns the name of IdempotentExecutor class.
     *      *
     * Otherwise, it throws an IllegalStateException.
     *
     * @param clazz The class to get the name of.
     * @return The name of the class.
     * @throws IllegalStateException if the class is not assignable from GruelboxDomainEventDispatcher or IdempotentExecutor.
     */
    @Override
    public String getName(Class<?> clazz) {
        if(GruelboxDomainEventDispatcher.class.isAssignableFrom(clazz)){
            return GruelboxDomainEventDispatcher.class.getName();
        }else if(IdempotentExecutor.class.isAssignableFrom(clazz)){
            return IdempotentExecutor.class.getName();
        }
        throw new IllegalStateException("The DomainEventsInstantiator can only instantiate instances of GruelboxDomainEventDispatcher or IdempotentExecutor!");
    }

    /**
     * Retrieves an instance of {@link GruelboxDomainEventDispatcher} or {@link IdempotentExecutor}.
     *
     * @param name The class "name" as returned by {@link #getName(Class)}.
     * @return An instance of {@link GruelboxDomainEventDispatcher} or {@link IdempotentExecutor}.
     * @throws IllegalStateException If the provided name is not equal to the name of {@link GruelboxDomainEventDispatcher} or {@link IdempotentExecutor}.
     * @throws NullPointerException If a {@link GruelboxDomainEventDispatcher} or {@link IdempotentExecutor} is not configured.
     */
    @Override
    public Object getInstance(String name) {
        if(GruelboxDomainEventDispatcher.class.getName().equals(name)){
            return Objects.requireNonNull(GruelboxDomainEventsConfiguration.configuredDispatcher(), "A GruelboxDomainEventDispatcher must be configured!");
        } else if (IdempotentExecutor.class.getName().equals(name)){
            return Objects.requireNonNull(GruelboxDomainEventsConfiguration.configuredIdempotentExecutor(), "An IdempotentExecutor must be configured!");
        }
        throw new IllegalStateException("The DomainEventsInstantiator can only instantiate instances of GruelboxDomainEventDispatcher or IdempotentExecutor!");


    }
}
