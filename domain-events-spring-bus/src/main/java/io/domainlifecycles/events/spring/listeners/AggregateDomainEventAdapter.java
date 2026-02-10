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
 *  Copyright 2019-2026 the original author or authors.
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

package io.domainlifecycles.events.spring.listeners;

import io.domainlifecycles.domain.types.AggregateDomainEvent;
import io.domainlifecycles.mirror.api.AggregateRootMirror;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.api.RepositoryMirror;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.ReflectionUtils;

import java.util.Optional;

/**
 * The AggregateDomainEventAdapter is responsible for handling {@link AggregateDomainEvent}
 * typed domain events listened to by aggregate roots within the application. It listens to events, locates
 * the appropriate repository and aggregate, and invokes the corresponding event listener
 * methods on the aggregate. Additionally, it updates the aggregate state within its repository
 * once the event has been processed.
 *
 * This class integrates with Spring's application context to dynamically resolve beans and
 * utilizes reflection to dynamically invoke methods. It is also designed to work with Spring's
 * transactional and asynchronous mechanisms.
 *
 * @author Mario Herb
 */
public class AggregateDomainEventAdapter implements ApplicationContextAware {

    private final Logger log = LoggerFactory.getLogger(AggregateDomainEventAdapter.class);
    private final DomainMirror domainMirror;
    private ApplicationContext context;

    /**
     * Constructor for the AggregateDomainEventAdapter class.
     * Initializes the adapter with the provided DomainMirror instance,
     * which acts as a representation of domain metadata facilitating event handling.
     *
     * @param domainMirror an instance of DomainMirror that provides access
     *                     to the domain-specific metadata necessary for
     *                     processing aggregate domain events.
     */
    public AggregateDomainEventAdapter(DomainMirror domainMirror) {
        this.domainMirror = domainMirror;
    }

    /**
     * Handles domain events related to aggregates by invoking corresponding listener methods
     * in the aggregate and persisting any updates. This method is executed asynchronously
     * and ensures a new transactional context for each event.
     *
     * @param event the domain event associated with a specific aggregate. The event contains
     *              the target aggregate's identity and other event-specific details used for
     *              handling and processing within the domain.
     */
    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async
    public void onAggregateDomainEvent(AggregateDomainEvent<?,?> event){
        var aggregateDomainEventMirror = domainEventMirrorForAggregateDomainEvent(event);
        var repositoryMirror = repositoryMirrorForAggregateDomainEventMirror(aggregateDomainEventMirror);
        if(repositoryMirror != null){
            var repo = getBeanByClassName(repositoryMirror.getTypeName());
            var repoTargetClass = AopUtils.getTargetClass(repo);
            var methodFind = ReflectionUtils.findMethod(repoTargetClass, "findById", event.targetId().getClass());
            ReflectionUtils.makeAccessible(methodFind);
            var aggregateOptional = (Optional)ReflectionUtils.invokeMethod(methodFind, repo, event.targetId());
            if(aggregateOptional.isPresent()){
                var aggregate = aggregateOptional.get();
                var aggregateRootMirror = (AggregateRootMirror)domainMirror
                    .getDomainTypeMirror(aggregate.getClass().getName())
                    .orElse(null);
                if(aggregateRootMirror == null){
                    log.warn("Aggregate not found for : {}", aggregate.getClass().getName());
                    return;
                }
                var listenerMethod = aggregateRootMirror.getMethods().stream()
                    .filter(m -> m.listensTo(aggregateDomainEventMirror))
                    .findFirst().orElse(null);

                if(listenerMethod != null) {
                    var methodListener = ReflectionUtils.findMethod(aggregate.getClass(), listenerMethod.getName(), event.getClass());
                    ReflectionUtils.makeAccessible(methodListener);
                    ReflectionUtils.invokeMethod(methodListener, aggregate, event);

                    var methodUpdate = ReflectionUtils.findMethod(repoTargetClass, "update", aggregate.getClass());
                    ReflectionUtils.makeAccessible(methodUpdate);
                    ReflectionUtils.invokeMethod(methodUpdate, repo, aggregate);
                }else{
                    log.warn("No listener found for event: {}", event);
                }
            }else{
                log.warn("Aggregate not found for event: {}", event);
            }
        }else{
            log.warn("No repository found for event: {}", event);
        }

    }

    private Object getBeanByClassName(String fullyQualifiedClassName) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(fullyQualifiedClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return context.getBean(clazz);
    }

    private DomainEventMirror domainEventMirrorForAggregateDomainEvent(AggregateDomainEvent<?,?> event) {
        var aggregateDomainEventMirrorOptional = domainMirror.getDomainTypeMirror(event.getClass().getName());
        return (DomainEventMirror) aggregateDomainEventMirrorOptional.orElse(null);
    }

    private RepositoryMirror repositoryMirrorForAggregateDomainEventMirror(DomainEventMirror aggregateDomainEventMirror) {
        var aggregateRootMirrorOptional = domainMirror.getAllAggregateRootMirrors().stream()
            .filter(a -> a.listensTo(aggregateDomainEventMirror))
            .findFirst();
        if(aggregateRootMirrorOptional.isPresent()){
            var aggregateRootMirror = aggregateRootMirrorOptional.get();
            return domainMirror.getAllRepositoryMirrors().stream()
                .filter(r -> r.getManagedAggregate().isPresent()
                    && r.getManagedAggregate().get().equals(aggregateRootMirror))
                .findFirst().orElse(null);
        }
        return null;
    }

    /**
     * Sets the {@link ApplicationContext} instance to the current object.
     * This method is part of the {@link ApplicationContextAware} interface
     * and allows the object to gain access to the application context.
     *
     * @param applicationContext the instance of {@link ApplicationContext} to be assigned.
     *                           It provides access to the Spring application context,
     *                           enabling retrieval of beans and other configuration details.
     * @throws BeansException if there is an issue setting the application context,
     *                        such as when the context is invalid or unavailable.
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
