package fixtures.flowcalls;

import io.domainlifecycles.domain.types.DomainService;

// reads the summary it gets from elsewhere, reaching neither its provider nor it otherwise
public class AuditService implements DomainService {

    // calls Summary.count
    public void audit() {
    }
}
