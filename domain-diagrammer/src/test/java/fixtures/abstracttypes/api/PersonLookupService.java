package fixtures.abstracttypes.api;

import io.domainlifecycles.domain.types.DomainService;

// returns the read model interface Person, which no query handler provides
public class PersonLookupService implements DomainService {

    public Person find(String name) {
        return null;
    }
}
