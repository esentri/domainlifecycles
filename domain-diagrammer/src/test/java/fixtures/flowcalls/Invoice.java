package fixtures.flowcalls;

import io.domainlifecycles.domain.types.base.AggregateRootBase;

public class Invoice extends AggregateRootBase<InvoiceId> {

    private final InvoiceId id;

    public Invoice(InvoiceId id, long concurrencyVersion) {
        super(concurrencyVersion);
        this.id = id;
    }

    public int amount() {
        return 0;
    }
}
