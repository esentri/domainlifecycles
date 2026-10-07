package fixtures.abstracttypes.api;

import io.domainlifecycles.domain.types.DomainService;

public interface PricingService extends DomainService {

    int price();
}
