package fixtures.flowcalls;

import io.domainlifecycles.domain.types.DomainEventListener;

// a non-domain class listening to a domain event
public class InvoiceMailer {

    private BillingService billingService;

    @DomainEventListener
    public void onInvoiceIssued(InvoiceIssued event) {
    }
}
