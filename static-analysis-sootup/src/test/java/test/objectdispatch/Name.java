package test.objectdispatch;

import io.domainlifecycles.domain.types.ValueObject;

/** A plain class rather than a record: records extend {@code java.lang.Record}, which is not on the analyzed classpath. */
public class Name implements ValueObject {

    private final String value;

    public Name(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Name " + value;
    }
}
