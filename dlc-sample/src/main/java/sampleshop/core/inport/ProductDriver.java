package sampleshop.core.inport;

import nitrox.dlc.domain.types.Driver;
import sampleshop.core.domain.product.Product;

import java.util.Optional;

/**
 * The ProductDriver contains all operations driving the application
 * in association with {@link Product}s.
 *
 * @author Mario Herb
 */
public interface ProductDriver extends Driver {

    /**
     * Create a new product
     */
    Product create(Product product);

    /**
     * Remove all products
     */
    void removeAll();

    /**
     * Find a Product by id
     */
    Optional<Product> find(Product.ProductId productId);
}
