package tests.mirror.nondomain;

public class NonDomainCaller {

    private final NonDomainAwareService service;

    public NonDomainCaller(NonDomainAwareService service) {
        this.service = service;
    }

    public String call(NonDomainHelper param) {
        return service.useHelper(param);
    }
}
