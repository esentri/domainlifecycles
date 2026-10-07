package fixtures.flowcalls;

import io.domainlifecycles.domain.types.base.EntityBase;

// an entity of the Invoice aggregate
public class InvoiceLine extends EntityBase<InvoiceLineId> {

    private final InvoiceLineId id;

    public InvoiceLine(InvoiceLineId id, long concurrencyVersion) {
        super(concurrencyVersion);
        this.id = id;
    }

    public void cancel() {
    }
}
