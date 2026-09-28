package test.containerparams;

import io.domainlifecycles.domain.types.ValueObject;

/** A plain class rather than a record: records extend {@code java.lang.Record}, which is not on the analyzed classpath. */
public class Item implements ValueObject {

    private final int price;

    public Item(int price) {
        this.price = price;
    }

    public int price() {
        return price;
    }

    public int weight() {
        return 1;
    }
}
