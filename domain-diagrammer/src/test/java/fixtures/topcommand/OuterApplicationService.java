package fixtures.topcommand;

import io.domainlifecycles.domain.types.ApplicationService;

/**
 * The outermost, driving entry point for {@link TopCommand}: nothing in this fixture references it,
 * so it is always the top-level consumer regardless of the setting under test.
 */
public class OuterApplicationService implements ApplicationService {

    private final InnerDomainService innerDomainService;

    public OuterApplicationService(InnerDomainService innerDomainService) {
        this.innerDomainService = innerDomainService;
    }

    public void handle(TopCommand command) {
        innerDomainService.handle(command);
    }
}
