package fixtures.backwardflow;

import io.domainlifecycles.domain.types.ApplicationService;

/**
 * The outermost entry point, standing in for a controller-like caller. Calls
 * {@link TargetDomainService#handle()} - simulated via a hand-built
 * {@link io.domainlifecycles.staticanalysis.DomainCalls} in the tests.
 */
public class EntryApplicationService implements ApplicationService {

    public void trigger() {
    }
}
