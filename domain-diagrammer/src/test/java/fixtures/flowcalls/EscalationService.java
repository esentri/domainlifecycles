package fixtures.flowcalls;

import io.domainlifecycles.domain.types.DomainService;

// calls back ReportingService.report, holding no field of it
public class EscalationService implements DomainService {

    public void escalate() {
    }
}
