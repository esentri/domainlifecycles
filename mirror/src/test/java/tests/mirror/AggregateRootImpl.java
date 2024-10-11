package tests.mirror;

public class AggregateRootImpl implements AggregateRootInterface {
    @Override
    public IdentityInterface id() {
        return null;
    }

    @Override
    public long concurrencyVersion() {
        return 0;
    }
}
