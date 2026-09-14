package fixtures.topcommand;

import io.domainlifecycles.domain.types.DomainService;

/**
 * Processes {@link TopCommand} directly, and is referenced as a field by {@link
 * OuterApplicationService}, which also processes the same command - the middle of a three-level
 * delegation chain.
 */
public class InnerDomainService implements DomainService {

    public void handle(TopCommand command) {
        // processes TopCommand directly
    }
}
