package fixtures.nondomain;

import io.domainlifecycles.domain.types.OutboundService;

/**
 * An OutboundService with a non-domain class ({@link NonDomainHelperClient}) injected as a field,
 * which it calls from within {@link #execute()}.
 */
public class SomeOutboundService implements OutboundService {

    private final NonDomainHelperClient client;

    public SomeOutboundService(NonDomainHelperClient client) {
        this.client = client;
    }

    public String execute() {
        return client.doSomething();
    }
}
