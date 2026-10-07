package tests.mirror.nondomain;

import io.domainlifecycles.domain.types.DomainService;

public class NonDomainAwareService implements DomainService {

    private final NonDomainHelper helper = new NonDomainHelper();

    public String useHelper(NonDomainHelper param) {
        return helper.describe(param);
    }
}
