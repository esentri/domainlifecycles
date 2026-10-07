package fixtures.flowcalls;

import io.domainlifecycles.domain.types.DomainService;

// gets the services it calls from the ServiceLocator, holding no field of them
public class ReportingService implements DomainService {

    // calls ServiceLocator.driver, SummaryDriver.compute and EscalationService.escalate
    public void report() {
    }
}
