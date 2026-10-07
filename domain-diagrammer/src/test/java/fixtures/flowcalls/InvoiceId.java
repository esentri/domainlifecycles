package fixtures.flowcalls;

import io.domainlifecycles.domain.types.Identity;

public record InvoiceId(Long value) implements Identity<Long> {
}
