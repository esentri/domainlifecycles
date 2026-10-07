package fixtures.backwardflow;

import io.domainlifecycles.domain.types.DomainService;

/**
 * Not connected to anything in this fixture - must be excluded whenever {@code includeFlowsTo} (or
 * {@code includeFlowsFrom}) restricts the diagram.
 */
public class UnrelatedService implements DomainService {

    public void doOther() {
    }
}
