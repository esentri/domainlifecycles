package test.domain;

public class MySecondOverridingService extends MyBaseService {

    // The second concrete owner of the inherited baseTemplate(...). Its existence is what makes
    // the per-owner resolution observable: both subclasses inherit the very same body, yet the
    // call it makes must resolve to a different process(...) for each of them.
    @Override
    public void process(MyDomainCommand command) {
        // subclass domain logic, deliberately without a super call
    }
}
