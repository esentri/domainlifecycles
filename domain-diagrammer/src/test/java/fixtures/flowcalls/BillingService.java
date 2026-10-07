package fixtures.flowcalls;

import io.domainlifecycles.domain.types.DomainService;

// reaches the Invoice aggregate over its repository
public class BillingService implements DomainService {

    private InvoiceRepository invoiceRepository;

    // calls InvoiceRepository.findInvoice and Invoice.amount
    public void bill() {
    }

    // references the exception, which is never shown nevertheless
    public BillingException failure() {
        return new BillingException("failed");
    }
}
