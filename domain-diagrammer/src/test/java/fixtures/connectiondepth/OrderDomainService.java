package fixtures.connectiondepth;

import io.domainlifecycles.domain.types.DomainService;

public interface OrderDomainService extends DomainService {

    void place(String article);
}
