package sample;

import io.domainlifecycles.domain.types.Identity;

public class TestIdentity implements Identity<Long> {
    @Override
    public Long value() {
        return 1L;
    }
}
