package tests.mirror;

public class EntityImpl implements EntityInterface {

    @Id
    private IdentityInterface identity;

    public EntityImpl(IdentityInterface identity) {
        this.identity = identity;
    }

    @Override
    public IdentityInterface id() {
        return identity;
    }

    @Override
    public long concurrencyVersion() {
        return 0;
    }
}
