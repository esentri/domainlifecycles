package io.domainlifecycles.events;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AnotherService {

    public ADomainEventCarryingAnId getIdentity(Long val){
        log.debug("Injected anotherService called!");
        return new ADomainEventCarryingAnId(new AnIdentity(val));
    }
}
