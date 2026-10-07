package fixtures.connectiondepth;

import io.domainlifecycles.domain.types.DomainService;
import io.domainlifecycles.domain.types.ListensTo;

public class NotificationService implements DomainService {

    @ListensTo(domainEventType = OrderPlaced.class)
    public void onOrderPlaced(OrderPlaced event) {
    }
}
