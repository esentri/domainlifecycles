package io.domainlifecycles.autoconfig.model.events;


import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.domain.types.ListensTo;
import io.domainlifecycles.domain.types.Repository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.extern.slf4j.Slf4j;

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
        if (!instanceMap.containsKey(aggregateId)) {
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
