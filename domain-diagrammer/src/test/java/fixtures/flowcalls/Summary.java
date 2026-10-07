package fixtures.flowcalls;

import io.domainlifecycles.domain.types.ReadModel;

// provided by no query handler, but by SummaryDriver.compute
public class Summary implements ReadModel {

    private int total;

    public int total() {
        return total;
    }

    public int count() {
        return 0;
    }

    public int average() {
        return 0;
    }

    public int max() {
        return 0;
    }
}
