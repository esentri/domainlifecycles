package fixtures.backwardflow;

import io.domainlifecycles.domain.types.DomainService;

/**
 * Calls {@link TargetRepositoryImpl#findById(TargetAggregateId)} - simulated via a hand-built
 * {@link io.domainlifecycles.staticanalysis.DomainCalls} in the tests.
 */
public class TargetDomainService implements DomainService {

    public void handle() {
    }
}
