package test.domain;

public class MyExtendingService extends MyConcreteBaseService {

    @Override
    public void execute(MyDomainCommand command) {
        // subclass domain logic, deliberately without a super call
    }
}
