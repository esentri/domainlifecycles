package io.domainlifecycles.staticanalysis.fixture;

import io.domainlifecycles.domain.types.DomainService;

/**
 * Calls {@link TestTargetService#target()} - the actual call is simulated via a hand-built
 * {@link io.domainlifecycles.staticanalysis.DomainCalls} in the tests, this class only needs to
 * exist so the mirror knows its {@code callTarget} method.
 */
public class TestCallerService implements DomainService {

    public void callTarget() {
    }
}
