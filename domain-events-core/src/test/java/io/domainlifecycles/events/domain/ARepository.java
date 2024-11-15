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

package io.domainlifecycles.events.domain;


import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.domain.types.ListensTo;
import io.domainlifecycles.domain.types.Repository;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
public class ARepository implements Repository<AnAggregate.AggregateId, AnAggregate> {

    public Queue<DomainEvent> received = new ConcurrentLinkedQueue<>();

    private Map<AnAggregate.AggregateId, AnAggregate> instanceMap = new HashMap<>();

    @ListensTo(domainEventType = ADomainEvent.class)
    public void onADomainEvent(ADomainEvent domainEvent){
        log.debug("ADomainEvent received in ARepository! Message = " + domainEvent.message());
        received.add(domainEvent);
    }

    @Override
    public Optional<AnAggregate> findById(AnAggregate.AggregateId aggregateId) {
        if(!instanceMap.containsKey(aggregateId)) {
            instanceMap.put(aggregateId, new AnAggregate(aggregateId, 1l));
        }
        return Optional.of(instanceMap.get(aggregateId));
    }

    @Override
    public AnAggregate insert(AnAggregate aggregateRoot) {
        return null;
    }

    @Override
    public AnAggregate update(AnAggregate aggregateRoot) {
        return null;
    }

    @Override
    public Optional<AnAggregate> deleteById(AnAggregate.AggregateId aggregateId) {
        return Optional.empty();
    }


}
