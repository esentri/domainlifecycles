package fixtures.containedreadmodel;

import io.domainlifecycles.domain.types.ReadModel;

/**
 * Contained in {@link OrderOverview}, and containing a read model itself.
 */
public class LineView implements ReadModel {

    private int quantity;

    private ProductView product;
}
