package test.domain;

public class MyPlainInterfaceImpl implements MyPlainInterface {

    // The only implementation of MyPlainInterface. It does NOT override defaultOperation, so
    // the body that runs for it is the interface's - which is what makes the default method an
    // entry point of this class.
    @Override
    public void declaredOnly(MyDomainCommand command) {
        command.id();
    }
}
