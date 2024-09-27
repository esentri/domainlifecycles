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

package io.domainlifecycles.events.domain;

import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.ListensTo;
import io.domainlifecycles.domain.types.base.AggregateRootBase;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AnAggregate extends AggregateRootBase<AnAggregate.AggregateId> {

    public record AggregateId(Long value) implements Identity<Long>{}

    private AggregateId id;
    public Queue<DomainEvent> received = new ConcurrentLinkedQueue<>();

    protected AnAggregate(AggregateId id, long concurrencyVersion) {
        super(concurrencyVersion);
        this.id = id;
    }

    @ListensTo(domainEventType = AnAggregateDomainEvent.class)
    public void onEvent(AnAggregateDomainEvent domainEvent){
        if(domainEvent.message().startsWith("TestAggregateDomainWithException")){
            throw new RuntimeException("Provoked runtime error!");
        }
        received.add(domainEvent);
    }

}
