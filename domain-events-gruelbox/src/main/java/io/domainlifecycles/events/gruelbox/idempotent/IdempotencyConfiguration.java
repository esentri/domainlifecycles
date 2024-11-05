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

package io.domainlifecycles.events.gruelbox.idempotent;

import io.domainlifecycles.events.consume.execution.detector.AggregateExecutionContext;
import io.domainlifecycles.events.consume.execution.detector.ExecutionContext;
import io.domainlifecycles.events.consume.execution.detector.ServiceExecutionContext;
import io.domainlifecycles.mirror.api.Domain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * The IdempotencyConfiguration class represents the configuration settings for idempotency protection in a system.
 * It allows defining scheduling delays and ordering rules for scheduled idempotency protected handler executions.
 *
 * @author Mario Herb
 */
public final class IdempotencyConfiguration {

    private static final Logger log = LoggerFactory.getLogger(IdempotencyConfiguration.class);

    private static final Duration PUBLISHING_SCHEDULING_DELAY_DEFAULT = Duration.ZERO;
    private static final boolean PUBLISHING_ORDERED_BY_DOMAIN_EVENT_TYPE_DEFAULT = false;

    private List<IdempotencyConfigurationEntry> configurationEntries = new ArrayList<>();
    private final Duration idempotencySchedulingDelay;
    private final boolean idempotencyOrderedByDomainEventType;

    /**
     * New IdempotencyConfiguration
     *
     * @param idempotencySchedulingDelay Duration specifying the delay for idempotency scheduling
     * @param idempotencyOrderedByDomainEventType Boolean indicating if idempotency ordering is based on domain event type
     */
    public IdempotencyConfiguration(Duration idempotencySchedulingDelay, boolean idempotencyOrderedByDomainEventType) {
        this.idempotencySchedulingDelay = Objects.requireNonNull(idempotencySchedulingDelay, "idempotencySchedulingDelay is required!");
        this.idempotencyOrderedByDomainEventType = idempotencyOrderedByDomainEventType;
    }

    /**
     * Constructs a new IdempotencyConfiguration object with default values for idempotencySchedulingDelay and idempotencyOrderedByDomainEventType.
     */
    public IdempotencyConfiguration() {
        this(PUBLISHING_SCHEDULING_DELAY_DEFAULT, PUBLISHING_ORDERED_BY_DOMAIN_EVENT_TYPE_DEFAULT);
    }

    /**
     * Adds a configuration entry to the IdempotencyConfiguration.
     *
     * @param entry The IdempotencyConfigurationEntry to add
     */
    public void addConfigurationEntry(IdempotencyConfigurationEntry entry){
        if(!isDomainEventHandlerMethodWithExpectedName(entry)){
            var msg = String.format("Domain event handler '%s' has no handler method called '%s'", entry.handlerClass().getName(), entry.methodName());
            log.error(msg);
            throw new IllegalStateException(msg);
        }
        configurationEntries.add(entry);
    }

    private boolean isDomainEventHandlerMethodWithExpectedName(IdempotencyConfigurationEntry entry){
        var mirror = Domain.getInitializedDomain().allTypeMirrors().get(entry.handlerClass().getName());
        if(mirror != null){
            try {
                var methodMirror = mirror.methodByName(entry.methodName());
                var domainEventMirror = methodMirror.getListenedEvent().orElseThrow();
                return domainEventMirror.getTypeName().equals(entry.domainEventClass().getName());
            }catch(Throwable t){
                return false;
            }
        }
        return false;
    }

    /**
     * Retrieves the Idempotency Configuration entry that matches the provided execution context.
     *
     * @param executionContext The execution context for a domain event handler.
     * @return An Optional containing the matching IdempotencyConfigurationEntry, or empty if no match is found.
     */
    public Optional<IdempotencyConfigurationEntry> idempotencyProtectionConfiguration(ExecutionContext executionContext){
        return configurationEntries.stream().filter(cfg -> match(executionContext, cfg)).findFirst();

    }

    private boolean match(ExecutionContext executionContext, IdempotencyConfigurationEntry entry){
        if(executionContext instanceof ServiceExecutionContext serviceExecutionContext){
            return serviceExecutionContext.handler().getClass().getName().equals(entry.handlerClass().getName())
                && serviceExecutionContext.handlerMethodName().equals(entry.methodName())
                && serviceExecutionContext.domainEvent().getClass().getName().equals(entry.domainEventClass().getName());
        }else if(executionContext instanceof AggregateExecutionContext<?,?> aggregateExecutionContext){
            var aggregateMirror = Domain.repositoryMirrorFor(aggregateExecutionContext.aggregateRepository()).getManagedAggregate().orElseThrow();
            return aggregateMirror.getTypeName().equals(entry.handlerClass().getName())
                && aggregateExecutionContext.aggregateHandlerMethodName().equals(entry.methodName())
                && aggregateExecutionContext.domainEvent().getClass().getName().equals(entry.domainEventClass().getName());
        }
        return false;
    }

    /**
     * Retrieves the delay used for idempotency scheduling.
     *
     * @return Duration specifying the delay for idempotency scheduling
     */
    public Duration getIdempotencySchedulingDelay() {
        return idempotencySchedulingDelay;
    }

    /**
     * Determines if idempotency is ordered by domain event type.
     *
     * @return True if idempotency is*/
    public boolean isIdempotencyOrderedByDomainEventType() {
        return idempotencyOrderedByDomainEventType;
    }
}
