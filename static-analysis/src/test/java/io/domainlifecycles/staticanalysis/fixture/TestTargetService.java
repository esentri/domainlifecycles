package io.domainlifecycles.staticanalysis.fixture;

import io.domainlifecycles.domain.types.DomainEventListener;
import io.domainlifecycles.domain.types.DomainService;

/**
 * The target of {@link TestCallerService#callTarget()}, and listener of {@link TestEvent} /
 * processor of {@link TestCommand}, used to test the backward ({@code flowTo}) traversal.
 */
public class TestTargetService implements DomainService {

    public void target() {
    }

    @DomainEventListener
    public void onEvent(TestEvent event) {
    }

    public void process(TestCommand command) {
    }
}
