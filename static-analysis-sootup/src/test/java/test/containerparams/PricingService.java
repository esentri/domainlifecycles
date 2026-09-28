package test.containerparams;

import io.domainlifecycles.domain.types.DomainService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Methods taking containers and arrays - whose parameter types the mirror names by their element type, the bytecode by
 * the container or array type - both calling and called.
 */
public class PricingService implements DomainService {

    public int priceAll(Item item) {
        return total(List.of(item)) + total(Set.of(item)) + first(Optional.of(item)) + sum(new Item[] {item})
            + count(new int[][] {{1}}) + checksum(new byte[] {1}) + weigh(item, item);
    }

    public int total(List<Item> items) {
        int total = 0;
        for (Item item : items) {
            total += item.price();
        }
        return total;
    }

    // overloads total(List): the parameter types tell them apart
    public int total(Set<Item> items) {
        int total = 0;
        for (Item item : items) {
            total += item.weight();
        }
        return total;
    }

    public int first(Optional<Item> item) {
        return item.isPresent() ? item.get().price() : 0;
    }

    public int sum(Item[] items) {
        return items.length == 0 ? 0 : items[0].price();
    }

    public int count(int[][] matrix) {
        return matrix.length;
    }

    public int checksum(byte[] data) {
        return data.length;
    }

    public int weigh(Item... items) {
        return items.length == 0 ? 0 : items[0].weight();
    }
}
