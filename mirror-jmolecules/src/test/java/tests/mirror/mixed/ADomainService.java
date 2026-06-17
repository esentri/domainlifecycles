package tests.mirror.mixed;

import io.domainlifecycles.domain.types.DomainEventListener;
import io.domainlifecycles.domain.types.Publishes;
import org.jmolecules.ddd.annotation.Service;
import tests.mirror.annotation.RepositoryJMoleculesAnnotation;

import java.util.UUID;

@Service
public class ADomainService {

    private final ARepository repository;

    private final ACallout callout;

    public ADomainService(ARepository repository, ACallout callout) {
        this.repository = repository;
        this.callout = callout;
    }

    public void doSomething(String param){
        callout.call(new AnAggregateRoot.AnAggregateRootId(UUID.randomUUID()));
    }

    @DomainEventListener
    @Publishes(domainEventTypes = DidIt.class)
    public void onDoneSomething(DoneSomething doneSomething){

    }

}
