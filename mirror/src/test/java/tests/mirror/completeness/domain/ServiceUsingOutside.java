package tests.mirror.completeness.domain;

import io.domainlifecycles.domain.types.DomainService;
import tests.mirror.completeness.outside.OutsideValue;

public class ServiceUsingOutside implements DomainService {

    public void use(OutsideValue value) {
    }
}
