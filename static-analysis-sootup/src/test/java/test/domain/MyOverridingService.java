package test.domain;

public class MyOverridingService extends MyBaseService {

    // Case F: override that calls super.process(...)
    // super.process is an invokespecial on the base class MyBaseService,
    // so the call-site signature carries <MyBaseService: process(...)>.
    @Override
    public void process(MyDomainCommand command) {
        super.process(command);   // invokespecial -> base class method
    }
}
