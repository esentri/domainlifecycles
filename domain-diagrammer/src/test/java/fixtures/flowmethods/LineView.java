package fixtures.flowmethods;

import io.domainlifecycles.domain.types.ReadModel;

// contained in OrderOverview, reached by no flow
public class LineView implements ReadModel {

    private String product;

    public String label() {
        return product;
    }

    public String details() {
        return product;
    }
}
