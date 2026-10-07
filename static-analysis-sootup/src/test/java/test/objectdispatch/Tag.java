package test.objectdispatch;

import io.domainlifecycles.domain.types.ValueObject;

/** Its {@code toString} calls a method of its own, which a caller of {@code Object::toString} must not take over. */
public class Tag implements ValueObject {

    private final String value;

    public Tag(String value) {
        this.value = value;
    }

    public String normalized() {
        return value.trim();
    }

    @Override
    public String toString() {
        return "Tag " + normalized();
    }
}
