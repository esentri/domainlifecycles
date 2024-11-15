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

package io.domainlifecycles.events.mq.consume;

import io.domainlifecycles.events.consume.execution.detector.ExecutionContext;
import io.domainlifecycles.events.consume.execution.handler.HandlerExecutor;
import io.domainlifecycles.events.gruelbox.idempotent.IdempotencyConfigurationEntry;

/**
 * The TransactionalIdempotencyAwareHandlerExecutorProxy interface extends the HandlerExecutor interface.
 * It provides a method for scheduling handler execution in a transactional manner used for idempotency protection.
 */
public interface TransactionalIdempotencyAwareHandlerExecutorProxy extends HandlerExecutor {

    /**
     * Schedules the execution context with the corresponding idempotency configuration entry.
     *
     * @param executionContext the execution context being scheduled
     * @param config the idempotency configuration for die scheduling case
     * @return true, if scheduling was successful
     */
    boolean schedule(ExecutionContext executionContext, IdempotencyConfigurationEntry config);
}
