package fixtures.flowcalls;

import io.domainlifecycles.domain.types.DomainEvent;

public record InvoiceIssued(InvoiceId invoiceId) implements DomainEvent {
}
