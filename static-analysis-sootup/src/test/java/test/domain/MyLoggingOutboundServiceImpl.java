package test.domain;

public class MyLoggingOutboundServiceImpl implements MyOutboundService {

    // The second implementation of MyOutboundService. A call written against the interface can
    // land in either of them at runtime, so the flow has to branch into both - the dispatch
    // counterpart to an event with several listeners.
    @Override
    public void doSomething(MyAggregateRoot root) {
        root.describe();
    }
}
