package fixtures.flowmethods;

import io.domainlifecycles.domain.types.DomainService;

public class PricingService implements DomainService {

    public int price() {
        return 1;
    }

    public int discount() {
        return 0;
    }
}
