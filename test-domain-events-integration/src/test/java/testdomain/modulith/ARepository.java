package testdomain.modulith;

import io.domainlifecycles.domain.types.DomainEventListener;
import io.domainlifecycles.domain.types.Repository;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class ARepository implements Repository<AnAggregate.AggregateId, AnAggregate> {

    private Map<AnAggregate.AggregateId, AnAggregate> instanceMap = new HashMap<>();

    public static volatile int cnt=0;

    @DomainEventListener
    public void onDomainEvent(MulticastedDomainEvent domainEvent) {
        if (domainEvent.message().startsWith("DomainEventWithException")) {
            throw new RuntimeException("Provoked runtime error!");
        }
        log.debug("MulticastedDomainEvent received: {}", domainEvent);
        cnt++;
    }

    @Override
    public Optional<AnAggregate> findById(AnAggregate.AggregateId aggregateId) {
        if (!instanceMap.containsKey(aggregateId)) {
            instanceMap.put(aggregateId, new AnAggregate(aggregateId, 1l));
        }
        if(aggregateId.value() == 2) return Optional.empty();
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
