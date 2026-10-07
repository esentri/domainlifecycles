package fixtures.flowcalls;

import io.domainlifecycles.domain.types.Repository;

public interface InvoiceRepository extends Repository<InvoiceId, Invoice> {

    Invoice findInvoice(InvoiceId id);
}
