package io.domainlifecycles.persistence.mapping.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BiMapTest {

    @Test
    public void testPutAndGet() {
        var biMap = new BiMap<String, Integer>();
        biMap.put("one", 1);

        assertThat(biMap.get("one")).isEqualTo(1);
    }

    @Test
    public void testGetInverse() {
        var biMap = new BiMap<String, Integer>();
        biMap.put("one", 1);

        assertThat(biMap.getInverse(1)).isEqualTo("one");
    }

    @Test
    public void testGetUnknownKeyReturnsNull() {
        var biMap = new BiMap<String, Integer>();

        assertThat(biMap.get("missing")).isNull();
        assertThat(biMap.getInverse(42)).isNull();
    }

    @Test
    public void testKeySetAndValueSetReflectEntries() {
        var biMap = new BiMap<String, Integer>();
        biMap.put("one", 1);
        biMap.put("two", 2);

        assertThat(biMap.keySet()).containsExactlyInAnyOrder("one", "two");
        assertThat(biMap.valueSet()).containsExactlyInAnyOrder(1, 2);
    }

    @Test
    public void testOverwritingAKeyUpdatesTheForwardMappingButLeavesTheOldReverseMappingInPlace() {
        // BiMap does not clean up the previous reverse entry when a key's value is replaced -
        // it is a plain pair of independent maps, not a set of consistently maintained pairs.
        var biMap = new BiMap<String, Integer>();
        biMap.put("one", 1);
        biMap.put("one", 2);

        assertThat(biMap.get("one")).isEqualTo(2);
        assertThat(biMap.getInverse(2)).isEqualTo("one");
        assertThat(biMap.getInverse(1)).isEqualTo("one");
    }
}
