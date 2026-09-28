package fixtures.flowmethods;

import io.domainlifecycles.domain.types.ReadModel;

import java.util.List;

public class OrderOverview implements ReadModel {

    private List<LineView> lines;

    public int total() {
        return 0;
    }

    public int count() {
        return 0;
    }
}
