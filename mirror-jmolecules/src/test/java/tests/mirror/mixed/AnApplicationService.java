package tests.mirror.mixed;

import io.domainlifecycles.domain.types.ApplicationService;
import io.domainlifecycles.domain.types.Publishes;

public class AnApplicationService implements ApplicationService {

    private final ARepository aRepository;

    private final ADomainService aDomainService;

    public AnApplicationService(ARepository aRepository, ADomainService aDomainService) {
        this.aRepository = aRepository;
        this.aDomainService = aDomainService;
    }

    @Publishes(domainEventTypes = DoneSomething.class)
    public AnAggregateRoot.AnAggregateRootId doSomeThing() {
        return null;
    }

}
