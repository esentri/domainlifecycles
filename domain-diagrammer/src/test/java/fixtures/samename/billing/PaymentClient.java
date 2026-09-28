package fixtures.samename.billing;

import io.domainlifecycles.domain.types.OutboundService;

public interface PaymentClient extends OutboundService {

    void pay();
}
