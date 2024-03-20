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

package nitrox.dlc.events;


import nitrox.dlc.domain.types.DomainEvent;
import nitrox.dlc.domain.types.ListensTo;
import nitrox.dlc.domain.types.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class ARepository implements Repository<AnAggregate.AggregateId, AnAggregate> {

    public List<DomainEvent> received = new ArrayList<>();

    private Map<AnAggregate.AggregateId, AnAggregate> instanceMap = new HashMap<>();

    @ListensTo(domainEventType = ADomainEvent.class)
    public void onADomainEvent(ADomainEvent domainEvent){
        System.out.println("ADomainEvent received in ARepository! Message = " + domainEvent.message());
        received.add(domainEvent);
    }

    @ListensTo(domainEventType = PassThroughDomainEvent.class)
    public void onADomainEvent(PassThroughDomainEvent domainEvent){
        System.out.println("PassThroughDomainEvent received in ARepository! Message = " + domainEvent.message());
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
