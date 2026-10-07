package fixtures.flowcalls;

import io.domainlifecycles.domain.types.Identity;

public record InvoiceLineId(Long value) implements Identity<Long> {
}
