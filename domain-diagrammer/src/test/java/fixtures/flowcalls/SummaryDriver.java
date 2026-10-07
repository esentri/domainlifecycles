package fixtures.flowcalls;

import io.domainlifecycles.domain.types.ApplicationService;

public class SummaryDriver implements ApplicationService {

    // calls Invoice.amount and InvoiceId.value
    public Summary compute() {
        return null;
    }

    // returns a read model a query handler provides
    public Stats stats() {
        return null;
    }
}
