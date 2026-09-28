package test.objectdispatch;

import io.domainlifecycles.domain.types.DomainService;

public class DescribingService implements DomainService {

    /** A call on {@code Object}: nothing tells which domain type it reaches. */
    public String describe(Object value) {
        return value.toString();
    }

    /** A call on a domain type. */
    public String describeName(Name name) {
        return name.toString();
    }
}
