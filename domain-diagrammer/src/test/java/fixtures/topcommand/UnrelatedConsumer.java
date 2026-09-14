package fixtures.topcommand;

import io.domainlifecycles.domain.types.OutboundService;

/**
 * Holds a field of type {@link InnerDomainService}, for a purpose entirely unrelated to {@link
 * TopCommand} - it never processes it. Modelled after a real-world bug: a second, unrelated holder
 * of the same field type must not by itself make {@link InnerDomainService} look top-level again,
 * regardless of which of its referencing types happens to be visited first.
 */
public class UnrelatedConsumer implements OutboundService {

    private final InnerDomainService innerDomainService;

    public UnrelatedConsumer(InnerDomainService innerDomainService) {
        this.innerDomainService = innerDomainService;
    }

    public void doSomethingElseEntirely() {
        // never touches TopCommand
    }
}
