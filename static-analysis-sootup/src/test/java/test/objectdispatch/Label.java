package test.objectdispatch;

import io.domainlifecycles.domain.types.ValueObject;

/** A plain class rather than a record: records extend {@code java.lang.Record}, which is not on the analyzed classpath. */
public class Label implements ValueObject {

    private final String text;

    public Label(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Label " + text;
    }
}
